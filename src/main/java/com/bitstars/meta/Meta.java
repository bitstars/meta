package com.bitstars.meta;

import java.lang.reflect.Type;

import org.json.JSONObject;

import com.bitstars.meta.updater.MetaUpdater;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonElement;

public class Meta {

	public interface MetaListener {
		// TODO rename to onError
		public void showErrorMsg(String msg);

		// TODO rename to onWarning
		public void showWarningMsg(String msg);
	}

	private static final String LOG_TAG = "Meta";

	public <T> T cloneViaGson(T orig) {

		return (T) j.fromJson(j.toJson(orig), orig.getClass());
	}

	Gson j = new Gson();
	private final MetaUpdater updater;

	public Meta(MetaListener listener) {
		updater = new MetaUpdater();
		// updater.setListener(listener); // TODO
	}

	public boolean validate(Object o) {
		return validateUpdateObject(o, o);
	}

	/**
	 * not yet implemented without side-effects
	 * 
	 * will test if the new version of the object would be a valid update for
	 * the target object. to acutally update the object use
	 * {@link Meta#updateObject(Object, Object)} instead
	 * 
	 * @param target
	 *            the object which would be updated
	 * @param newObj
	 * @return true if the update would work correctly
	 */
	@Deprecated
	public <T> boolean validateUpdateObject(T target, T newObj) {
		// TODO refactor MetaUpdater to allow validating an update without
		// updating it, then change this implementation here
		return updateObject(target, newObj);
	}

	/**
	 * will update the target object (using the values from the updated version
	 * passed as the second parameter) if all conditions are met
	 * 
	 * @param target
	 * @param updatedVersion
	 * @return true if the update is allowed, false if a condition was not met
	 */
	public <T> boolean updateObject(T target, T updatedVersion) {
		try {
			String updatedVersionAsJson = j.toJson(updatedVersion);
			String updatedObj = j.toJson(updater.updateObject(target,
					new JSONObject(updatedVersionAsJson)));
			if (updatedObj == null) {
				return false;
			}
			if (!equalJson(updatedObj, updatedVersionAsJson)) {
				return false;
			}
			return fromJsonInto(updatedObj, target);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	// TODO add to MetaUpdater class
	public boolean fromJsonInto(String objAsJSON, final Object targetObj) {
		Gson gson = new GsonBuilder().registerTypeAdapter(targetObj.getClass(),
				new InstanceCreator() {
					@Override
					public Object createInstance(Type t) {
						// return the same object so that it is "updated"
						return targetObj;
					}
				}).create();
		gson.fromJson(objAsJSON, targetObj.getClass());
		return true;
	}

	public boolean equalJson(String a, String b) {
		com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
		JsonElement oa = parser.parse(a);
		JsonElement ob = parser.parse(b);
		return oa.equals(ob);
	}

	public void toString(String objName, Object obj) {
		String json = j.toJson(obj);
		System.out.println("obj " + objName + " as json: " + json);

	}
}
