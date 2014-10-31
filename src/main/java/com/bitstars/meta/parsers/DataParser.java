package com.bitstars.meta.parsers;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bitstars.meta.MetaValidator;
import com.bitstars.meta.annotation.MetaAttr;
import com.bitstars.meta.annotation.MetaJSONTranslator;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class can be used for parsing an set of data objects with its meta data
 *
 * @author Ruslan
 *
 */
public class DataParser {
	@Deprecated
	public static final String ALL_DB_DATA = "allDbData";
	@Deprecated
	public static final String SINGLE_DB_DATA = "singleDbData";
	@Deprecated
	Gson gsonParserAllData;
	@Deprecated
	Gson gsonParserAllDataWithoutUnvisibleFields;
	@Deprecated
	Gson gsonParserPublicFields;
	@Deprecated
	Gson gsonParserPrivateFields;

	public DataParser() {
		this.gsonParserAllData = new GsonBuilder().create();
		this.gsonParserAllDataWithoutUnvisibleFields = new GsonBuilder()
				.setExclusionStrategies(new CDExclusionStrategy()).create();
		this.gsonParserPublicFields = new GsonBuilder().setExclusionStrategies(
				new CDExclusionPublicStrategy()).create();
		this.gsonParserPrivateFields = new GsonBuilder()
				.setExclusionStrategies(new CDExclusionPrivateStrategy())
				.create();

		// .disableInnerClassSerialization().create();
	}

	/**
	 * This strategy excluded all fields which are marked with MetaAttr as
	 * transient
	 *
	 * @author Ruslan
	 *
	 */
	@Deprecated
	private static class CDExclusionStrategy implements ExclusionStrategy {

		public boolean shouldSkipField(FieldAttributes f) {
			if (f.getAnnotation(MetaAttr.class) != null) {
				MetaAttr attr = f.getAnnotation(MetaAttr.class);
				if (attr.type() != 0) {
					if ((attr.type() & MetaAttr.FIELDS_TRANSIENT) != 0) {
						return true;
					}
				}
			}
			return false;
		}

		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}
	}

	/**
	 * This strategy included only fields which are marked with MetaAttr as
	 * public
	 *
	 * @author Ruslan
	 *
	 */
	@Deprecated
	private static class CDExclusionPublicStrategy implements ExclusionStrategy {

		public boolean shouldSkipField(FieldAttributes f) {
			if (f.getAnnotation(MetaAttr.class) != null) {
				MetaAttr attr = f.getAnnotation(MetaAttr.class);
				if (attr.type() != 0) {
					if ((attr.type() & MetaAttr.FIELDS_PUBLIC) != 0) {
						return false;
					}
				}
			}
			return true;
		}

		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}
	}

	/**
	 * This strategy included only fields which are marked with MetaAttr as
	 * public or private
	 *
	 * @author Ruslan
	 *
	 */
	@Deprecated
	private static class CDExclusionPrivateStrategy implements
			ExclusionStrategy {

		public boolean shouldSkipField(FieldAttributes f) {
			if (f.getAnnotation(MetaAttr.class) != null) {
				MetaAttr attr = f.getAnnotation(MetaAttr.class);
				if (attr.type() != 0) {
					if ((attr.type() & MetaAttr.FIELDS_PUBLIC) != 0) {
						return false;
					} else if ((attr.type() & MetaAttr.FIELDS_PRIVATE) != 0) {
						return false;
					}
				}
			}
			return true;
		}

		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}
	}

	/**
	 * This strategy is a common strategy which can be used to include only
	 * fields, that developer need
	 *
	 * @author Ruslan
	 *
	 */
	private static class CommonIxclusionPrivateStrategy implements
			ExclusionStrategy {
		int includedAttr;

		public CommonIxclusionPrivateStrategy(int metaAttributes) {
			includedAttr = metaAttributes;
		}

		public boolean shouldSkipField(FieldAttributes f) {
			if (f.getAnnotation(MetaAttr.class) != null) {
				MetaAttr attr = f.getAnnotation(MetaAttr.class);
				if ((attr.type() & includedAttr) != 0) {
					return false;
				} else {
					return true;
				}
			}
			return true;
		}

		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}
	}

	/**
	 * This strategy is a common strategy which can be used to exclude only
	 * fields, that developer doesn't need
	 *
	 * @author Ruslan
	 *
	 */
	private static class CommonExclusionPrivateStrategy implements
			ExclusionStrategy {
		int excludedAttr;

		public CommonExclusionPrivateStrategy(int metaAttributes) {
			excludedAttr = metaAttributes;
		}

		public boolean shouldSkipField(FieldAttributes f) {
			if (f.getAnnotation(MetaAttr.class) != null) {
				MetaAttr attr = f.getAnnotation(MetaAttr.class);
				if ((attr.type() & excludedAttr) != 0) {
					return true;
				} else {
					return false;
				}
			}
			return false;
		}

		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}
	}

	/**
	 * Simple parsing of object without constraints
	 *
	 * @param object
	 * @return object as JSON in root
	 */
	public JSONObject parseSingleObject(Object object) {
		return new JSONObject(new Gson().toJson(object));
	}

	/**
	 * Parsing of object attributes, which have given meta attributes
	 *
	 * @param object
	 * @param metaAttributes
	 *            - e.g. MetaAttr.FIELDS_READ_ONLY + MetaAttr.PRIVATE
	 * @return
	 */
	public JSONObject parseSingleObjectIncludingMetaAttrs(Object object,
			int metaAttributes) {
		Gson gsonParser = new GsonBuilder().setExclusionStrategies(
				new CommonIxclusionPrivateStrategy(metaAttributes)).create();
		return new JSONObject(gsonParser.toJson(object));
	}

	/**
	 * Parsing of object attributes, which doesn't have given meta attributes
	 *
	 * @param object
	 * @param metaAttributes
	 *            - e.g. MetaAttr.FIELDS_READ_ONLY + MetaAttr.PRIVATE
	 * @return
	 */
	public JSONObject parseSingleObjectExcludingMetaAttrs(Object object,
			int metaAttributes) {
		Gson gsonParser = new GsonBuilder().setExclusionStrategies(
				new CommonExclusionPrivateStrategy(metaAttributes)).create();
		return new JSONObject(gsonParser.toJson(object));
	}

	/**
	 * Simple parsing of object without constraints
	 *
	 * @param object
	 * @return object as JSON in root
	 */
	public JSONArray parseCollectionObject(Object object) {
		return new JSONArray(new Gson().toJson(object));
	}

	/**
	 * Parsing of object attributes, which have given meta attributes
	 *
	 * @param object
	 * @param metaAttributes
	 *            - e.g. MetaAttr.FIELDS_READ_ONLY + MetaAttr.PRIVATE
	 * @return
	 */
	public JSONArray parseCollectionObjectIncludingMetaAttrs(Object object,
			int metaAttributes) {
		Gson gsonParser = new GsonBuilder().setExclusionStrategies(
				new CommonIxclusionPrivateStrategy(metaAttributes)).create();
		return new JSONArray(gsonParser.toJson(object));
	}

	/**
	 * Parsing of object attributes, which doesn't have given meta attributes
	 *
	 * @param object
	 * @param metaAttributes
	 *            - e.g. MetaAttr.FIELDS_READ_ONLY + MetaAttr.PRIVATE
	 * @return
	 */
	public JSONArray parseCollectionObjectExcludingMetaAttrs(Object object,
			int metaAttributes) {
		Gson gsonParser = new GsonBuilder().setExclusionStrategies(
				new CommonExclusionPrivateStrategy(metaAttributes)).create();
		return new JSONArray(gsonParser.toJson(object));
	}

	/**
	 * This method is for parsing all data from objects excluding its transient
	 * fields defined by module.meta.MetaAttr
	 *
	 * @param allData
	 * @param includeInvisibleFields
	 * @return
	 */
	@Deprecated
	public JSONObject parseDBSet(Iterator<?> allData,
			boolean includeInvisibleFields, boolean includeMeta) {
		JSONObject result = new JSONObject();
		Object lastObject = null;
		boolean hasData = false;
		// collect all data which is visible to front end
		while (allData.hasNext()) {
			hasData = true;
			try {
				lastObject = allData.next();
				String entry = "";
				if (!includeInvisibleFields) {
					entry = gsonParserAllDataWithoutUnvisibleFields
							.toJson(lastObject);
				} else {
					entry = gsonParserAllData.toJson(lastObject);
				}
				result = MetaValidator.myAppend(result, ALL_DB_DATA,
						new JSONObject(entry));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		// add meta data to the all data if needed
		if (includeMeta && hasData) {
			try {
				result.put(MetaJSONTranslator.META_DATA_STR,
						new MetaParser().getMetaAsJSON(lastObject));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@Deprecated
	public JSONObject parseSingleData(Object singleData,
			boolean includeInvisibleFields, boolean includeMeta) {
		List<Object> temp = new LinkedList<Object>();
		temp.add(singleData);
		return parseDBSet(temp.iterator(), includeInvisibleFields, includeMeta);
	}

	@Deprecated
	public JSONObject parseSinglePublicData(Object singleData,
			boolean includeMeta) {

		JSONObject result = new JSONObject();
		try {
			String entry = gsonParserPublicFields.toJson(singleData);
			result.put(SINGLE_DB_DATA, new JSONObject(entry));
			// add meta data to the all data if needed
			if (includeMeta) {
				result.put(MetaJSONTranslator.META_DATA_STR,
						new MetaParser().getMetaAsJSON(singleData));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Deprecated
	public JSONObject parseSinglePrivateData(Object singleData,
			boolean includeMeta) {

		JSONObject result = new JSONObject();
		try {
			String entry = gsonParserPrivateFields.toJson(singleData);
			result.put(SINGLE_DB_DATA, new JSONObject(entry));
			// add meta data to the all data if needed
			if (includeMeta) {
				result.put(MetaJSONTranslator.META_DATA_STR,
						new MetaParser().getMetaAsJSON(singleData));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Deprecated
	public Gson getGsonForAllData() {
		return gsonParserAllData;
	}
}
