package com.bitstars.meta.parsers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bitstars.meta.annotation.MetaAttr;
import com.bitstars.meta.annotation.MetaJSONTranslator;
import com.bitstars.meta.annotation.MetaModel;
import com.google.gson.Gson;

public class MetaParser {

	/**
	 * Determines the meta model of given object including all inner classes
	 *
	 * @param object
	 *            to get meta
	 * @return meta model as JSONObject
	 */
	public JSONObject getMetaAsJSON(Object object) {
		Class<? extends Object> clazz = object.getClass();
		return getMetaAsJSON(clazz);
	}

	/**
	 * Determines the meta model of given object including all inner classes
	 * with including strategy. Only attributes which have <b>metaAttributes</b>
	 * will be parsed
	 *
	 * @param object
	 *            to get meta
	 * @param metaAttr
	 *            fields that should be parsed
	 * @return meta model as JSONObject
	 */
	public JSONObject getMetaAsJSONIncludingMetaAttr(Object object, int metaAttr) {
		Class<? extends Object> clazz = object.getClass();
		return getMetaAsJSONIncludingMetaAttr(clazz, metaAttr);
	}

	/**
	 * Determines the meta model of given object including all inner classes
	 *
	 * @param object
	 *            to get meta
	 * @return meta model as MetaModel.java
	 */
	public MetaModel getMetaAsObject(Object object) {
		Class<? extends Object> clazz = object.getClass();
		JSONObject metaJSON = getMetaAsJSON(clazz);
		return new Gson().fromJson(metaJSON.toString(), MetaModel.class);
	}

	/**
	 * Determines the meta model of class including all inner classes
	 *
	 * @param object
	 *            to get meta
	 * @return meta model as MetaModel.java
	 */
	public MetaModel getMetaAsObject(Class<? extends Object> clazz) {
		return new Gson().fromJson(getMetaAsJSON(clazz).toString(),
				MetaModel.class);
	}

	/**
	 * Determines the meta model of given class including all inner classes
	 *
	 * @param clazz
	 *            to get meta
	 * @return meta model as JSONObject
	 */
	public JSONObject getMetaAsJSON(Class<? extends Object> clazz) {

		JSONObject result = new JSONObject();
		List<Field> allFields = new LinkedList<Field>();
		Set<ComplexClass> complexeClasses = new HashSet<ComplexClass>();

		// store object class name
		try {
			result.accumulate(MetaJSONTranslator.CLASS_NAME_STR,
					clazz.getSimpleName());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		// get all fields
		do {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if (!field.isAnnotationPresent(MetaAttr.class)
						|| (field.getAnnotation(MetaAttr.class).type() & MetaAttr.TYPE_SKIP_META) == 0) {
					// Scan just those types that are shouldn't be skipped
					// Also check if some attributes of superclass where hidden
					// by attributes of subclass. Do not consider these
					// attributes
					if (notContainName(allFields, field)) {
						allFields.add(field);
					}
				}
			}
			clazz = clazz.getSuperclass();
		} while (clazz != Object.class);

		// iterate over all fields
		for (Field field : allFields) {
			if (!Modifier.isFinal(field.getModifiers())) {
				try {

					// save all non primitive types to call it recursive later
					if (!isPrimitive(field)) {
						if (!isCollection(field.getType())) {
							ComplexClass cc;

							field.setAccessible(true);

							// takes just class
							cc = new ComplexClass(null, field.getType(),
									field.getName(), false);

							if (!cc.isInterface()) {
								complexeClasses.add(cc);
							}
						} else {
							ComplexClass cc;

							field.setAccessible(true);

							// takes class
							cc = new ComplexClass(null,
									((Class<?>) ((ParameterizedType) field
											.getGenericType())
											.getActualTypeArguments()[0]),
									field.getName(), true);

							if (!cc.isInterface()) {
								complexeClasses.add(cc);
							}
						}
					}

					// save all fields
					result.accumulate(MetaJSONTranslator.FIELDS_ALL_STR,
							field.getName());

					// check annotation specific fields
					if (field.isAnnotationPresent(MetaAttr.class)) {
						MetaAttr attr = field.getAnnotation(MetaAttr.class);
						if (!attr.regex().equals("")) {
							result = appendAsArray(result,
									MetaJSONTranslator.FIELDS_REGEX_STR,
									new JSONObject().accumulate(
											field.getName(), attr.regex()));
						}
						if (attr.type() != 0) {
							result = addAllTypes(result, attr.type(),
									field.getName());
						}
					}

					// check if collection and primitive
					if (isCollection(field.getType()) && isPrimitive(field)) {
						appendAsArray(result,
								MetaJSONTranslator.TYPE_SIMPLE_COLLECTION_STR,
								field.getName());
					}

					// check if map
					if (isMap(field.getType())) {
						appendAsArray(result,
								MetaJSONTranslator.TYPE_SIMPLE_MAP_STR,
								field.getName());
					}
				} catch (JSONException e) {
					e.printStackTrace();
					return null;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					return null;
				}
			}
		}

		// recursive call for complexe classes
		if (complexeClasses.size() != 0) {
			for (ComplexClass cClass : complexeClasses) {
				try {
					result = appendAsArray(
							result,
							MetaJSONTranslator.FIELDS_COMPLEX_STR,
							(new JSONObject()
									.put(MetaJSONTranslator.ATTRIBUTE_NAME_STR,
											cClass.fieldName)
									.put(MetaJSONTranslator.ATTRIBUTE_TYPE_STR,
											(cClass.collection ? MetaJSONTranslator.ATTRIBUTE_TYPE_COLLECTION_STR
													: MetaJSONTranslator.ATTRIBUTE_TYPE_SINGLE_STR))
									.put(MetaJSONTranslator.META_DATA_STR,
											getMetaAsJSON(cClass.clazz))));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	private boolean isMap(Class<?> type) {
		if (type.equals(Map.class)) {
			return true;
		}
		return false;
	}

	private boolean notContainName(List<Field> allFields, Field field) {
		for (Field fieldExist : allFields) {
			if (fieldExist.getName().equals(field.getName())) {
				return false;
			}
		}
		return true;
	}

	private static boolean isCollection(Class<?> clazzTest) {
		while (clazzTest != null && clazzTest != Object.class) {
			Class<?>[] allInterfaces = clazzTest.getInterfaces();
			for (Class<?> interfaze : allInterfaces) {
				if (interfaze == Collection.class) {
					return true;
				}
			}
			clazzTest = clazzTest.getSuperclass();
		}
		return false;
	}

	public static boolean isPrimitive(Field field) {
		if (isCollection(field.getType())) {
			return isPrimitive(((Class<?>) ((ParameterizedType) field
					.getGenericType()).getActualTypeArguments()[0]));
		}
		return isPrimitive(field.getType());
	}

	/**
	 * check if given class is primitive or is array of primitive classes
	 *
	 * @param clazz
	 *            - to check
	 * @return true if it primitive
	 */
	public static boolean isPrimitive(Class<?> clazz) {

		if (clazz.isArray()) {
			return isPrimitive(clazz.getComponentType());
		}

		Set<Class<?>> allPrimitives = new HashSet<Class<?>>();
		allPrimitives.add(Boolean.class);
		allPrimitives.add(Character.class);
		allPrimitives.add(Byte.class);
		allPrimitives.add(Short.class);
		allPrimitives.add(Integer.class);
		allPrimitives.add(Long.class);
		allPrimitives.add(Float.class);
		allPrimitives.add(Double.class);
		allPrimitives.add(Void.class);
		allPrimitives.add(String.class);
		allPrimitives.add(int.class);
		allPrimitives.add(boolean.class);
		allPrimitives.add(byte.class);
		allPrimitives.add(char.class);
		allPrimitives.add(short.class);
		allPrimitives.add(long.class);
		allPrimitives.add(float.class);
		allPrimitives.add(double.class);
		allPrimitives.add(void.class);
		allPrimitives.add(Object.class);
		allPrimitives.add(Date.class);

		return allPrimitives.contains(clazz);
	}

	private class ComplexClass {
		Object object;
		public Class<?> clazz;
		String fieldName;
		boolean collection;

		public ComplexClass(Object object, Class<?> clazz, String fieldName,
				boolean collection) {
			this.object = object;
			this.clazz = clazz;
			this.fieldName = fieldName;
			this.collection = collection;
		}

		public boolean isInterface() {
			boolean result = false;
			if (object != null) {
				result = result || object.getClass().isInterface();
			}
			if (clazz != null) {
				result = result || clazz.isInterface();
			}
			return result;
		}
	}

	/**
	 * Sets all types defined in CDMetaAttribute. Important!: this method should
	 * be updated if there are new types added in ADMetaAttribute
	 *
	 * @param jsonObject
	 *            in which new types should be added
	 * @param types
	 *            - all set types
	 * @param fieldName
	 *            - which should be tested for a type
	 * @return updated JSONObject
	 */
	private JSONObject addAllTypes(JSONObject jsonObject, int types,
			String fieldName) {

		int[] allAttrs = MetaJSONTranslator.allAttrs;

		for (int attr : allAttrs) {
			if (((types & attr) != 0) && ((attr & MetaAttr.TYPE_ID) == 0)) {
				// append attribute names in an array, also if there only one
				// member
				try {
					jsonObject = appendAsArray(jsonObject,
							MetaJSONTranslator.translateType(attr), fieldName);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (((types & attr) != 0)
					&& ((attr & MetaAttr.TYPE_ID) != 0)) {
				// put attribute, without creating an array, because id field
				// can be only one
				jsonObject.put(MetaJSONTranslator.translateType(attr),
						fieldName);

			}
		}

		return jsonObject;
	}

	/**
	 * Gives a list of fields of an object, which have a certain parameter
	 * defined by <b>MetaParser</b>
	 *
	 * @param object
	 *            - JSONObject which should be proved to find fields
	 * @param parameter
	 *            - defined by CDGsonParser
	 * @return a list of fields defined by parameter
	 */
	public static List<String> getAllFieldsByParameter(JSONObject object,
			String parameter) {
		List<String> result = new LinkedList<String>();
		if (object != null && !object.isNull(parameter)) {
			JSONArray specificFieldJSON;
			try {
				if (object.get(parameter) instanceof JSONArray) {
					specificFieldJSON = (JSONArray) object.get(parameter);
					for (int i = 0; i < specificFieldJSON.length(); i++) {
						result.add(specificFieldJSON.get(i).toString());
					}
				} else {
					result.add(object.get(parameter).toString());
				}
			} catch (JSONException e) {

			}
		}
		return result;
	}

	public static List<String> getAllComplexFields(JSONObject meta) {
		List<String> result = new LinkedList<String>();
		if (!meta.isNull(MetaJSONTranslator.FIELDS_COMPLEX_STR)) {
			try {
				JSONArray allComplexfields = meta
						.getJSONArray(MetaJSONTranslator.FIELDS_COMPLEX_STR);
				for (int i = 0; i < allComplexfields.length(); i++) {
					JSONObject complexField = allComplexfields.getJSONObject(i);
					@SuppressWarnings("unchecked")
					Iterator<String> iterator = complexField.keys();
					while (iterator.hasNext()) {
						result.add(iterator.next());
					}
				}
			} catch (JSONException e) {
			}
		}

		return result;
	}

	/**
	 * This method is used to append one object like function append to an
	 * array, which is not supported in android
	 *
	 * @param root
	 * @param field
	 * @param toBeAppend
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject appendAsArray(JSONObject root, String field,
			Object toBeAppend) throws JSONException {
		if (root.isNull(field)) {
			root.put(field, new JSONArray().put(toBeAppend));
		} else {
			root.accumulate(field, toBeAppend);
		}
		return root;
	}

	public static JSONObject getMetaFromComplexField(JSONObject rootMeta,
			String complexField) {
		try {
			if (!rootMeta.isNull(MetaJSONTranslator.FIELDS_COMPLEX_STR)) {
				JSONArray complexArray = rootMeta
						.getJSONArray(MetaJSONTranslator.FIELDS_COMPLEX_STR);
				for (int i = 0; i < complexArray.length(); i++) {
					JSONObject subComplex = complexArray.getJSONObject(i);
					if (!subComplex.isNull(complexField)) {
						if (!subComplex.getJSONObject(complexField).isNull(
								MetaJSONTranslator.ATTRIBUTE_TYPE_SINGLE_STR)) {
							return subComplex
									.getJSONObject(complexField)
									.getJSONObject(
											MetaJSONTranslator.ATTRIBUTE_TYPE_SINGLE_STR);
						} else if (!subComplex
								.getJSONObject(complexField)
								.isNull(MetaJSONTranslator.ATTRIBUTE_TYPE_COLLECTION_STR)) {
							return subComplex
									.getJSONObject(complexField)
									.getJSONObject(
											MetaJSONTranslator.ATTRIBUTE_TYPE_COLLECTION_STR);
						}
					}
				}
			}
		} catch (JSONException e) {

		}
		return null;
	}

	/**
	 * This method does from an key:value or key:[values] a ArrayList with
	 * values
	 *
	 * @param jsonObject
	 * @param fieldType
	 * @return
	 * @throws JSONException
	 */
	public static ArrayList<String> getAsArrayList(JSONObject jsonObject,
			String fieldType) throws JSONException {
		ArrayList<String> result = new ArrayList<String>();
		if (!jsonObject.isNull(fieldType)) {
			Object object = jsonObject.get(fieldType);
			if (object instanceof JSONObject) {
				result.add(object.toString());
			} else if (object instanceof JSONArray) {
				JSONArray array = (JSONArray) object;
				for (int i = 0; i < array.length(); i++) {
					result.add(array.getString(i));
				}
			} else if (object instanceof String) {
				result.add(object.toString());
			}
		}
		return result;
	}

	/**
	 * Determines the meta model of given class including all inner classes with
	 * including strategy. Only attributes which have <b>metaAttributes</b> will
	 * be parsed
	 *
	 * @param clazz
	 *            to get meta
	 * @param metaAttr
	 *            fields that should be parsed
	 * @return meta model as JSONObject
	 */
	public JSONObject getMetaAsJSONIncludingMetaAttr(
			Class<? extends Object> clazz, int metaAttributes) {

		JSONObject result = new JSONObject();
		List<Field> allFields = new LinkedList<Field>();
		Set<ComplexClass> complexeClasses = new HashSet<ComplexClass>();

		// store object class name
		try {
			result.accumulate(MetaJSONTranslator.CLASS_NAME_STR,
					clazz.getSimpleName());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		// get all fields
		do {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if ((field.isAnnotationPresent(MetaAttr.class) && (field
						.getAnnotation(MetaAttr.class).type() & metaAttributes) != 0)) {
					// Scan just those types that are shouldn't be skipped
					// Also check if some attributes of superclass where hidden
					// by attributes of subclass. Do not consider these
					// attributes
					if (notContainName(allFields, field)) {
						allFields.add(field);
					}
				}
			}
			clazz = clazz.getSuperclass();
		} while (clazz != Object.class);

		// iterate over all fields
		for (Field field : allFields) {
			if (!Modifier.isFinal(field.getModifiers())) {
				try {

					// save all non primitive types to call it recursive later
					if (!isPrimitive(field)) {
						if (!isCollection(field.getType())) {
							ComplexClass cc;

							field.setAccessible(true);

							// takes just class
							cc = new ComplexClass(null, field.getType(),
									field.getName(), false);

							if (!cc.isInterface()) {
								complexeClasses.add(cc);
							}
						} else {
							ComplexClass cc;

							field.setAccessible(true);

							// takes class
							cc = new ComplexClass(null,
									((Class<?>) ((ParameterizedType) field
											.getGenericType())
											.getActualTypeArguments()[0]),
									field.getName(), true);

							if (!cc.isInterface()) {
								complexeClasses.add(cc);
							}
						}
					}

					// save all fields
					result.accumulate(MetaJSONTranslator.FIELDS_ALL_STR,
							field.getName());

					// check annotation specific fields
					if (field.isAnnotationPresent(MetaAttr.class)) {
						MetaAttr attr = field.getAnnotation(MetaAttr.class);
						if (!attr.regex().equals("")) {
							result = appendAsArray(result,
									MetaJSONTranslator.FIELDS_REGEX_STR,
									new JSONObject().accumulate(
											field.getName(), attr.regex()));
						}
						if (attr.type() != 0) {
							result = addAllTypes(result, attr.type(),
									field.getName());
						}
					}

					// check if collection
					if (isCollection(field.getType())) {
						appendAsArray(result,
								MetaJSONTranslator.TYPE_SIMPLE_COLLECTION_STR,
								field.getName());
					}

					// check if map
					if (isMap(field.getType())) {
						appendAsArray(result,
								MetaJSONTranslator.TYPE_SIMPLE_MAP_STR,
								field.getName());
					}
				} catch (JSONException e) {
					e.printStackTrace();
					return null;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					return null;
				}
			}
		}

		// recursive call for complexe classes
		if (complexeClasses.size() != 0) {
			for (ComplexClass cClass : complexeClasses) {
				try {
					result = appendAsArray(
							result,
							MetaJSONTranslator.FIELDS_COMPLEX_STR,
							(new JSONObject()
									.put(MetaJSONTranslator.ATTRIBUTE_NAME_STR,
											cClass.fieldName)
									.put(MetaJSONTranslator.ATTRIBUTE_TYPE_STR,
											(cClass.collection ? MetaJSONTranslator.ATTRIBUTE_TYPE_COLLECTION_STR
													: MetaJSONTranslator.ATTRIBUTE_TYPE_SINGLE_STR))
									.put(MetaJSONTranslator.META_DATA_STR,
											getMetaAsJSON(cClass.clazz))));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}
}
