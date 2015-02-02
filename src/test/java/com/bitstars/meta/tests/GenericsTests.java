package com.bitstars.meta.tests;

import static org.junit.Assert.assertTrue;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.bitstars.meta.exceptions.UpdaterException;
import com.bitstars.meta.models.ExampleGenericClass;
import com.bitstars.meta.models.SimpleObject;
import com.bitstars.meta.updater.MetaUpdater;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class GenericsTests {
	@Test
	public void testOne() throws JsonSyntaxException, JSONException,
			UpdaterException {
		ExampleGenericClass<Number> f = new ExampleGenericClass<Number>();
		f.x = 7;
		f.y = 3;
		f.messageType = new SimpleObject();

		// f.key = "ns=4;s=machinetemp";
		// f.caption = "Machine Critical";
		// f.id = 5;
		f.message = "Machine temperature critical at >80°C";
		// f.type = ComponentType.INTEGER;
		f.max = 100;
		f.min = 80;

		Gson g = new Gson();
		String json = g.toJson(f);
		System.out.println("json=" + json);
		MetaUpdater meta = new MetaUpdater();
		f.x = 0;
		f.y = 0;
		meta.updateObject(f, new JSONObject(json));
		System.out.println("f.x=" + f.x);
		Double d = (double) 7f;

		assertTrue(f.y == 3f);
	}

	@Test
	public void testTwo() throws JsonSyntaxException, JSONException,
			UpdaterException {
		ExampleGenericClass<Number> f = new ExampleGenericClass<Number>();
		f.x = 7;
		f.y = 3;

		Gson g = new Gson();
		String json = g.toJson(f);
		System.out.println("json=" + json);
		MetaUpdater meta = new MetaUpdater();
		f.x = 0;
		f.y = 0;
		meta.updateObject(f, new JSONObject(json));
		System.out.println("f.x=" + f.x);
		System.out.println("f.x.getClass()=" + f.x.getClass());
		Double d = (double) 7f;

		assertTrue(f.y == 3f);
	}
}
