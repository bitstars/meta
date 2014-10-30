package de.russcity.meta.parser;

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
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.russcity.meta.annotation.MetaAttr;
import de.russcity.meta.annotation.MetaJSONTranslator;

public class MetaParser {

	/**
	 * Determines the meta model of given object including all inner classes
	 *
	 * @param object
	 *            to get meta
	 * @return meta model as JSONObject
	 */
	public JSONObject getMeta(Object object) {
		Class<? extends Object> clazz = object.getClass();
		return getMeta(clazz);
	}

	/**
	 * Determines the meta model of given object including all inner classes
	 *
	 * @param object
	 *            to get meta
	 * @return meta model as JSONObject
	 */
	public JSONObject getMeta(Class<? extends Object> clazz) {

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
					allFields.add(field);
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
							result.accumulate(
									MetaJSONTranslator.FIELDS_REGEX_STR,
									new JSONObject().accumulate(
											field.getName(), attr.regex()));
						}
						if (attr.type() != 0) {
							result = addAllTypes(result, attr.type(),
									field.getName());
						}
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
					result = myAppend(
							result,
							MetaJSONTranslator.FIELDS_COMPLEX_STR,
							new JSONObject()
									.accumulate(
											cClass.fieldName,
											new JSONObject()
													.accumulate(
															cClass.collection ? MetaJSONTranslator.COMPLEX_MULTIPLE_DATA_STR
																	: MetaJSONTranslator.COMPLEX_SINGLE_DATA_STR,
															getMeta(cClass.clazz))));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		return new JSONObject().put(MetaJSONTranslator.META_DATA_STR, result);
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
		Class<?> clazz;
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
			if ((types & attr) != 0) {
				try {
					jsonObject = myAppend(jsonObject, translateType(attr),
							fieldName);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		return jsonObject;
	}

	/**
	 * Translates an integer attribute to its string representation. Important!:
	 * this method should be updated if there are new types added in
	 * ADMetaAttribute
	 *
	 * @param type
	 *            - defined in CDMetaAttribute
	 * @return its string representation, defined in CDMetaParser
	 */
	public String translateType(int type) {
		switch (type) {
		case MetaAttr.FIELDS_READ_ONLY:
			return MetaJSONTranslator.FIELDS_READ_ONLY_STR;
		case MetaAttr.FIELDS_UNIQ_IN_SCOPE:
			return MetaJSONTranslator.FIELDS_UNIQ_IN_SCOPE_STR;
		case MetaAttr.FIELDS_TRANSIENT:
			return MetaJSONTranslator.FIELDS_TRANSIENT_STR;
		case MetaAttr.TYPE_DATE_LONG:
			return MetaJSONTranslator.TYPE_DATE_LONG_STR;
		case MetaAttr.TYPE_ID:
			return MetaJSONTranslator.TYPE_ID_STR;
		case MetaAttr.TYPE_URL_IMAGE:
			return MetaJSONTranslator.TYPE_URL_IMAGE_STR;
		case MetaAttr.FIELDS_PRIVATE:
			return MetaJSONTranslator.FIELDS_PRIVATE_STR;
		case MetaAttr.FIELDS_PUBLIC:
			return MetaJSONTranslator.FIELDS_PUBLIC_STR;
		default:
			return "unknownType";
		}
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
	public static JSONObject myAppend(JSONObject root, String field,
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
								MetaJSONTranslator.COMPLEX_SINGLE_DATA_STR)) {
							return subComplex
									.getJSONObject(complexField)
									.getJSONObject(
											MetaJSONTranslator.COMPLEX_SINGLE_DATA_STR);
						} else if (!subComplex
								.getJSONObject(complexField)
								.isNull(MetaJSONTranslator.COMPLEX_MULTIPLE_DATA_STR)) {
							return subComplex
									.getJSONObject(complexField)
									.getJSONObject(
											MetaJSONTranslator.COMPLEX_MULTIPLE_DATA_STR);
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
}
