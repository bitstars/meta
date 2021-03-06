package com.bitstars.meta.updater;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.bitstars.meta.annotation.MetaJSONTranslator;
import com.bitstars.meta.annotation.MetaModel;
import com.bitstars.meta.annotation.MetaModelCompex;
import com.bitstars.meta.exceptions.UpdaterException;
import com.bitstars.meta.parsers.DataParser;
import com.bitstars.meta.parsers.MetaParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonSyntaxException;

/**
 * This tool is used for update of objects
 *
 * @author Ruslan
 *
 */
public class MetaUpdater {

	public MetaUpdater() {
	}

	/**
	 * Updates object o1 with attributes of object jo2 depends on meta model of
	 * o1
	 *
	 * @param target
	 * @param updatedObject
	 * @return
	 * @throws UpdaterException
	 */
	public <T> T updateObject(final T target, JSONObject updatedObject) throws UpdaterException, JsonSyntaxException {
		MetaModel mm = new MetaParser().getMetaAsObject(target);
		JSONObject jo1 = new DataParser().parseSingleObject(target);
		JSONObject result = updateObject(mm, jo1, updatedObject);
		writeJsonIntoObject(target, result);
		return target;
	}

	/**
	 * Warning: This method does not obey the meta rules! It will overwrite all
	 * values based on the passed {@link JSONObject}
	 *
	 * @param target
	 * @param source
	 */
	public <T> void writeJsonIntoObject(final T target, JSONObject source) {
		Gson gson = new GsonBuilder().registerTypeAdapter(target.getClass(), new InstanceCreator() {
			public Object createInstance(Type t) {
				// return the same object so that it is "updated"
				return target;
			}
		}).create();
		gson.fromJson(source.toString(), target.getClass());
	}

	/**
	 * Updates object o1 with attributes of object jo2 depends on meta model of
	 * o1 with including strategy. That means that only the fields with
	 * <b>inclMetaAttr</b> will be updated
	 *
	 * @param target
	 * @param updatedObject
	 * @param inclMetaAttr
	 * @return
	 * @throws UpdaterException
	 */
	public <T> T updateObjectIncludingAttr(T target, JSONObject updatedObject, int inclMetaAttr)
			throws UpdaterException {
		MetaModel mm = new MetaParser().getMetaAsObject(target);
		JSONObject jo1 = new DataParser().parseSingleObject(target);

		JSONObject result = updateObjectIncludingAttr(mm, jo1, updatedObject, inclMetaAttr);

		T newOb = (T) new Gson().fromJson(result.toString(), target.getClass());
		return newOb;
	}

	/**
	 * Updates object jo1 with attributes of object jo2 depends on meta model of
	 * mm
	 *
	 * @param mm
	 * @param target
	 * @param updatedObject
	 * @return
	 * @throws UpdaterException
	 */
	public JSONObject updateObject(MetaModel mm, JSONObject target, JSONObject updatedObject) throws UpdaterException {
		Iterator<String> keys = mm.getFIELDS_ALL().iterator();

		// Iterate over attributes of original object
		while (keys.hasNext()) {
			String key = keys.next();
			// Skip read only and id fields
			if (!mm.getFIELDS_READ_ONLY().contains(key) && !mm.getTYPE_ID().equals(key)) {
				try {
					updateField(mm, target, updatedObject, key);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return target;
	}

	/**
	 * Updated an object with an including strategy. That means, that only the
	 * fields which have <b>inclMetaAttr</b> attributes will be updated
	 *
	 * @param mm
	 * @param target
	 * @param updatedObject
	 * @param inclMetaAttr
	 * @return
	 * @throws UpdaterException
	 */
	public JSONObject updateObjectIncludingAttr(MetaModel mm, JSONObject target, JSONObject updatedObject,
			int inclMetaAttr) throws UpdaterException {
		Iterator<String> keys = mm.getFIELDS_ALL().iterator();

		// Iterate over attributes of original object
		while (keys.hasNext()) {
			String key = keys.next();

			// Skip read only and id fields
			if (!mm.getFIELDS_READ_ONLY().contains(key) && !mm.getTYPE_ID().equals(key)
					&& ((MetaJSONTranslator.getMetaAttrTypeOfField(mm, key) & inclMetaAttr) != 0)) {
				try {
					updateField(mm, target, updatedObject, key);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return target;
	}

	private void updateField(MetaModel mm, JSONObject target, JSONObject updatedObject, String key)
			throws JSONException, UpdaterException {
		boolean hasKey = updatedObject.has(key);
		// Check fields NOT_NULL

		if (mm.getFIELDS_NOT_NULL().contains(key)
				&& (!hasKey || updatedObject.get(key) == null || updatedObject.get(key).toString().equals(""))
				&& (target.get(key) != null || !target.get(key).toString().equals(""))) {
			throw new UpdaterException("Can not update field '" + key + "' of origin object by null ");
		}

		boolean complex = false;
		// Check if attribute is complex
		for (MetaModelCompex complexField : mm.getFIELDS_COMPLEX()) {

			// Only the single complex objects will be updated regarding
			// its MetaAttr. The collection of complex objects will be
			// just completely replaced, because it is not possible to
			// difference between order of the objects
			if (complexField.getATTRIBUTE_NAME().equals(key)
					&& complexField.getATTRIBUTE_TYPE().equals(MetaJSONTranslator.ATTRIBUTE_TYPE_SINGLE_STR)) {
				complex = true;
				JSONObject updatedValue = updateObject(complexField.getMETA_DATA(), target.getJSONObject(key),
						updatedObject.getJSONObject(key));
				target.put(key, updatedValue);
			}
		}

		if (!complex) {
			// check if field has a regex
			for (Map<String, String> map : mm.getFIELDS_REGEX()) {
				if (map.containsKey(key)) {
					if (hasKey && !checkRegex(updatedObject.get(key).toString(), map.get(key))) {

						throw new UpdaterException("Key '" + key + "' has a regex '" + map.get(key)
								+ "' which is not matched by value '" + (hasKey ? updatedObject.get(key) : "") + "'.");
					}
				}
			}
			if (hasKey) {
				target.put(key, updatedObject.get(key));
			} else {
				target.put(key, JSONObject.NULL);
			}
		}
	}

	/**
	 * This method checks a value by matching with given regex. The grammar of
	 * regex can be read <a href=
	 * "http://www.vogella.com/articles/JavaRegularExpressions/article.html" >
	 * here</a>
	 *
	 * @param value
	 * @param regex
	 * @return
	 */
	private boolean checkRegex(String value, String regex) {
		if (regex.charAt(0) == '?') {
			// TODO: define own regex rules
			return true;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
}
