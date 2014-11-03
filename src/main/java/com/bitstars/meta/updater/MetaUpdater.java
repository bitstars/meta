package com.bitstars.meta.updater;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.bitstars.meta.annotation.MetaJSONTranslator;
import com.bitstars.meta.exceptions.UpdaterException;
import com.bitstars.meta.models.MetaModel;
import com.bitstars.meta.parsers.DataParser;
import com.bitstars.meta.parsers.MetaParser;
import com.google.gson.Gson;

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
	 * @param o1
	 * @param jo2
	 * @return
	 * @throws UpdaterException
	 */
	public <T> T updateObject(T o1, JSONObject jo2) throws UpdaterException {
		MetaModel mm = new MetaParser().getMetaAsObject(o1);
		JSONObject jo1 = new DataParser().parseSingleObject(o1);

		JSONObject result = updateObject(mm, jo1, jo2);

		T newOb = (T) new Gson().fromJson(jo1.toString(), o1.getClass());
		return newOb;
	}

	/**
	 * Updates object jo1 with attributes of object jo2 depends on meta model of
	 * mm
	 *
	 * @param mm
	 * @param jo1
	 * @param jo2
	 * @return
	 * @throws UpdaterException
	 */
	public JSONObject updateObject(MetaModel mm, JSONObject jo1, JSONObject jo2)
			throws UpdaterException {
		Iterator<?> keys = jo1.keys();

		// Iterate over attributes of original object
		while (keys.hasNext()) {
			String key = (String) keys.next();

			// Skip read only and id fields
			if (!mm.getFIELDS_READ_ONLY().contains(key)
					&& !mm.getTYPE_ID().equals(key)) {

				// Check fields NOT_NULL
				if (jo2.has(key)) {
					if ((jo2.get(key) == null || jo2.get(key).toString()
							.equals(""))
							&& (jo1.get(key) != null || !jo1.get(key)
									.toString().equals(""))
							&& mm.getFIELDS_NOT_NULL().contains(key)) {
						throw new UpdaterException("Can not update field '"
								+ key + "' of origin object by null ");
					}
				}

				boolean complex = false;
				// Check if attribute is complex
				for (MetaModel.MetaComplexModel complexField : mm
						.getFIELDS_COMPLEX()) {

					// Only the single complex objects will be updated regarding
					// its MetaAttr. The collection of complex objects will be
					// just completely replaced, because it is not possible to
					// difference between order of the objects
					if (complexField.getATTRIBUTE_NAME().equals(key)
							&& complexField
									.getATTRIBUTE_TYPE()
									.equals(MetaJSONTranslator.ATTRIBUTE_TYPE_SINGLE_STR)) {
						complex = true;
						jo1.put(key,
								updateObject(complexField.getMETA_DATA(),
										jo1.getJSONObject(key),
										jo2.getJSONObject(key)));
					}
				}

				if (!complex) {
					// check if field has a regex
					for (Map<String, String> map : mm.getFIELDS_REGEX()) {
						if (map.containsKey(key)) {
							if (!checkRegex(jo2.get(key).toString(),
									map.get(key))) {
								throw new UpdaterException("Key '" + key
										+ "' has a regex '" + map.get(key)
										+ "' which is not matched by value '"
										+ jo2.get(key) + "'");
							}
						}
					}
					jo1.put(key, jo2.get(key));
				}

			}
		}
		return jo1;
	}

	/**
	 * This method checks a value by matching with given regex. The grammar of
	 * regex can be read <a href=
	 * "http://www.vogella.com/articles/JavaRegularExpressions/article.html"
	 * >here</a>
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
