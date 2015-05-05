package com.bitstars.meta.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bitstars.meta.annotation.MetaAttr;
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

	public DataParser() {

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

		@Override
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

		@Override
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

		@Override
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

		@Override
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
	public static JSONObject parseSingleObject(Object object) {
		try {
			return new JSONObject(new Gson().toJson(object));
		} catch (JSONException e) {
			return null;
		}
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
		try {
			return new JSONObject(gsonParser.toJson(object));
		} catch (JSONException e) {
			return null;
		}
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
		try {
			return new JSONObject(gsonParser.toJson(object));
		} catch (JSONException e) {
			return null;
		}
	}

	/**
	 * Simple parsing of object without constraints
	 *
	 * @param object
	 * @return object as JSON in root
	 */
	public JSONArray parseCollectionObject(Object object) {
		try {
			return new JSONArray(new Gson().toJson(object));
		} catch (JSONException e) {
			return null;
		}
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
		try {
			return new JSONArray(gsonParser.toJson(object));
		} catch (JSONException e) {
			return null;
		}
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
		try {
			return new JSONArray(gsonParser.toJson(object));
		} catch (JSONException e) {
			return null;
		}
	}

}
