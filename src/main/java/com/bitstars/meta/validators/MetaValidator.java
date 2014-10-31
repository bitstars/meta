package com.bitstars.meta.validators;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bitstars.meta.annotation.MetaJSONTranslator;
import com.bitstars.meta.exceptions.ValidatorException;
import com.bitstars.meta.models.MetaModel;
import com.bitstars.meta.models.MetaModel.MetaComplexModel;
import com.bitstars.meta.parsers.DataParser;
import com.bitstars.meta.parsers.MetaParser;

/**
 * This class is used for validation objects depends on its meta model
 *
 * @author RU$$
 *
 */

public class MetaValidator {

	/**
	 * Checks if object is valid depends on its meta model
	 *
	 * @param o
	 *            - object to be tested
	 * @return
	 * @throws ValidatorException
	 */
	public boolean isValidObject(Object o) throws ValidatorException {
		// Create meta model object
		MetaModel mm = new MetaParser().getMetaAsObject(o);
		// Get all object data
		JSONObject jo = new DataParser().parseSingleObject(o);

		return isValidObject(mm, jo);
	}

	/**
	 * Checks if object is valid depends on its meta model
	 *
	 * @param mm
	 *            - meta model of the object
	 * @param jo
	 *            - the object
	 * @return
	 * @throws ValidatorException
	 */
	public boolean isValidObject(MetaModel mm, JSONObject jo)
			throws ValidatorException {
		Iterator<?> keys = jo.keys();

		// Iterate over object attributes
		while (keys.hasNext()) {
			String key = (String) keys.next();

			if (!mm.getFIELDS_ALL().contains(key)) {

				// If the object has other attributes that its meta model than
				// it is invalid
				throw new ValidatorException("Key '" + key
						+ "' is not contained in meta model " + mm.toJSON());
			}

			// Value of the attribute that will be checked
			Object value = jo.get(key);

			for (Map<String, String> regexMap : mm.getFIELDS_REGEX()) {
				if (regexMap.containsKey(key)) {

					// if the attribute that will be checked has a regex
					// expression
					String regex = regexMap.get(key);
					if (!checkRegex(value.toString(), regex)) {
						throw new ValidatorException("Key '" + key
								+ "' and its value '" + value.toString()
								+ "' are not match regex '" + regex + "'");
					}
				}
			}

		}

		// Check NOT_NULL fields
		for (String notNull : mm.getFIELDS_NOT_NULL()) {
			if (!jo.has(notNull) || jo.isNull(notNull)
					|| jo.get(notNull).toString().equals("")) {
				throw new ValidatorException("Value of key '" + notNull
						+ "' cannot be null");
			}
		}

		// Check recursively complex fields
		for (MetaComplexModel complexField : mm.getFIELDS_COMPLEX()) {
			if (!jo.isNull(complexField.getATTRIBUTE_NAME())) {
				if (complexField.getATTRIBUTE_TYPE().equals(
						MetaJSONTranslator.ATTRIBUTE_TYPE_SINGLE_STR)) {
					if (!isValidObject(complexField.getMETA_DATA(),
							jo.getJSONObject(complexField.getATTRIBUTE_NAME()))) {
						return false;
					}
				} else if (complexField.getATTRIBUTE_TYPE().equals(
						MetaJSONTranslator.ATTRIBUTE_TYPE_COLLECTION_STR)) {
					JSONArray ja = jo.getJSONArray(complexField
							.getATTRIBUTE_NAME());
					for (int i = 0; i < ja.length(); i++) {
						if (!isValidObject(complexField.getMETA_DATA(),
								ja.getJSONObject(i))) {
							return false;
						}
					}
				}
			}
		}

		return true;
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
