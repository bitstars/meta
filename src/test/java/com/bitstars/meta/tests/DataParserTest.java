package com.bitstars.meta.tests;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import com.bitstars.meta.annotation.MetaAttr;
import com.bitstars.meta.models.SimpleObject;
import com.bitstars.meta.parsers.DataParser;

/**
 *
 * @author RU$$
 *
 */
public class DataParserTest {

	@Test
	public void simpleObjectWithoutMetaTest() {
		// Simple startegy
		SimpleObject so = generateSimpleObjectWithAttributes();
		JSONObject jo = new DataParser().parseSingleObject(so);
		System.out.println("SimpleObject as Object: " + jo);

		Assert.assertFalse(jo.isNull("id"));
		Assert.assertFalse(jo.isNull("name"));
		Assert.assertFalse(jo.isNull("description"));

		// Including strategy
		JSONObject jo2 = new DataParser().parseSingleObjectIncludingMetaAttrs(
				so, MetaAttr.FIELDS_PRIVATE + MetaAttr.TYPE_ID);
		System.out.println("SimpleObject as Object with including strategy: "
				+ jo2);

		Assert.assertFalse(jo2.isNull("id"));
		Assert.assertTrue(jo2.isNull("name"));
		Assert.assertFalse(jo2.isNull("description"));

		// Excluding strategy
		JSONObject jo3 = new DataParser().parseSingleObjectExcludingMetaAttrs(
				so, MetaAttr.FIELDS_PRIVATE + MetaAttr.TYPE_ID);
		System.out.println("SimpleObject as Object with excluding strategy: "
				+ jo3);

		Assert.assertTrue(jo3.isNull("id"));
		Assert.assertFalse(jo3.isNull("name"));
		Assert.assertTrue(jo3.isNull("description"));

		// Collection Object
		List<SimpleObject> simpleObjects = new LinkedList<SimpleObject>();
		simpleObjects.add(so);
		simpleObjects.add(generateSimpleObjectWithAttributes());

		JSONArray jo4 = new DataParser()
		.parseCollectionObjectExcludingMetaAttrs(simpleObjects,
				MetaAttr.FIELDS_PRIVATE + MetaAttr.TYPE_ID);
		System.out
				.println("Collection of SimpleObject with excluding strategie: "
						+ jo4);

		Assert.assertEquals("Data Parsing of collection was not correct!",
				jo4.length(), 2);
		Assert.assertTrue(jo4.getJSONObject(0).isNull("id"));
		Assert.assertFalse(jo4.getJSONObject(0).isNull("name"));
		Assert.assertTrue(jo4.getJSONObject(0).isNull("description"));

	}

	private SimpleObject generateSimpleObjectWithAttributes() {
		SimpleObject so = new SimpleObject();
		so.setDescription("myDescription");
		so.setId(123L);
		so.setName("myName");

		return so;
	}

}
