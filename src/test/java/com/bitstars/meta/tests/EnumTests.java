package com.bitstars.meta.tests;

import static org.junit.Assert.assertTrue;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.bitstars.meta.exceptions.UpdaterException;
import com.bitstars.meta.updater.MetaUpdater;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class EnumTests {

	enum MyEnum {
		a, b, c, d
	};

	public static class ExampleClass {
		MyEnum a;

		public ExampleClass() {
			a = com.bitstars.meta.tests.EnumTests.MyEnum.b;
		}
	}

	@Test
	public void testTwo() throws JsonSyntaxException, JSONException,
			UpdaterException {

		Number x = 8.0f;
		Integer y = 8;
		assertTrue(y.equals(x));

		ExampleClass f = new ExampleClass();

		Gson g = new Gson();
		String json = g.toJson(f);
		System.out.println("json=" + json);
		MetaUpdater meta = new MetaUpdater();
		f.a = MyEnum.c;
		meta.updateObject(f, new JSONObject(json));
		System.out.println("f.a=" + f.a.getClass());
		System.out.println("g.toJson(f)=" + g.toJson(f));
	}
}
