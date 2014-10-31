package com.bitstars.meta;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.bitstars.meta.annotation.MetaJSONTranslator;
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
	private MetaListener ml = null;

	public MetaUpdater() {
	}

	public MetaUpdater(MetaListener ml) {
		this.ml = ml;
	}

	public <T> T updatePrivate(T o1, String o2) throws JSONException {
		JSONObject oJ1 = new DataParser().parseSinglePrivateData(o1, true);
		JSONObject oJ2 = new JSONObject(o2);

		JSONObject jONew = new MetaValidator().setListener(ml).updateObject(
				oJ1.toString(), oJ2.toString(), true);

		T newOb = (T) new Gson().fromJson(jONew.toString(), o1.getClass());
		return newOb;
	}

	public JSONObject updateObject(JSONObject singleOldData,
			JSONObject validatedEntry, JSONObject meta) throws JSONException {

		JSONObject oOldDataJSON = singleOldData.getJSONArray(
				DataParser.ALL_DB_DATA).getJSONObject(0);

		ArrayList<String> transientFields = MetaParser.getAsArrayList(meta,
				MetaJSONTranslator.FIELDS_TRANSIENT_STR);

		ArrayList<String> readOnlyFields = MetaParser.getAsArrayList(meta,
				MetaJSONTranslator.FIELDS_READ_ONLY_STR);

		ArrayList<String> complexFields = MetaParser.getAsArrayList(meta,
				MetaJSONTranslator.FIELDS_COMPLEX_STR);

		ArrayList<String> updatedFields = new ArrayList<String>();
		@SuppressWarnings("unchecked")
		Iterator<String> iterator = oOldDataJSON.keys();
		while (iterator.hasNext()) {
			String field = iterator.next();
			if (!(transientFields.contains(field)
					|| readOnlyFields.contains(field) || complexFields
					.contains(field))) {
				updatedFields.add(field);
			}
		}

		for (String updatedField : updatedFields) {
			oOldDataJSON.put(updatedField, validatedEntry.get(updatedField));
		}

		for (String complexField : complexFields) {
			if (!(updatedFields.contains(complexField) || readOnlyFields
					.contains(complexField))) {
				JSONObject complexOject = updateObject(
						oOldDataJSON.getJSONObject(complexField),
						validatedEntry.getJSONObject(complexField),
						meta.getJSONObject(complexField));
				oOldDataJSON.put(complexField, complexOject);
			}
		}

		return oOldDataJSON;
	}

}
