package com.bitstars.meta.tests;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import com.bitstars.meta.annotation.MetaJSONTranslator;
import com.bitstars.meta.annotation.MetaModel;
import com.bitstars.meta.models.ComplexObject;
import com.bitstars.meta.models.SimpleObject;
import com.bitstars.meta.models.SubClassOfSimpleObject;
import com.bitstars.meta.parsers.MetaParser;

/**
 * 
 * @author RU$$
 *
 */
public class MetaParserTest {

	/**
	 * Test parsing of simple object to a JSON meta
	 */
	@Test
	public void simpleObjectAsJSONTest() {
		SimpleObject so = new SimpleObject();
		JSONObject jo = new MetaParser().getMetaAsJSON(so);
		System.out.println("SimpleObject: " + jo);

		// 4 attributes total
		Assert.assertEquals(
				"SimpleObject should have 4 attributes. The 5th should be skipped",
				jo.getJSONArray(MetaJSONTranslator.FIELDS_ALL_STR).length(), 4);

	}

	/**
	 * Test parsing of complex object to a JSON meta
	 */
	@Test
	public void complextObjectAsJSONTest() {
		ComplexObject co = new ComplexObject();
		JSONObject jo = new MetaParser().getMetaAsJSON(co);
		System.out.println("ComplexObject: " + jo);

		// 3 attributes total
		Assert.assertEquals("ComplexObject should have 3 attributes.", jo
				.getJSONArray(MetaJSONTranslator.FIELDS_ALL_STR).length(), 3);

		// 2 complex attributes
		Assert.assertEquals(
				"ComplexObject should have 2 complex attributes.",
				jo.getJSONArray(MetaJSONTranslator.FIELDS_COMPLEX_STR).length(),
				2);

		// 1 private attribute
		Assert.assertEquals(
				"ComplexObject should have 1 private attribute.",
				jo.getJSONArray(MetaJSONTranslator.FIELDS_PRIVATE_STR).length(),
				1);

	}

	/**
	 * Test parsing of sub object of simple object to a JSON meta
	 */
	@Test
	public void subClassOfSimpleObjectAsJSONTest() {
		SubClassOfSimpleObject co = new SubClassOfSimpleObject();
		JSONObject jo = new MetaParser().getMetaAsJSON(co);
		System.out.println("SubClassOfSimpleObject: " + jo);

		// 5 attributes total
		Assert.assertEquals("SubClassOfSimpleObject should have 5 attributes.",
				jo.getJSONArray(MetaJSONTranslator.FIELDS_ALL_STR).length(), 5);

		// 1 public attribute
		Assert.assertEquals(
				"SubClassOfSimpleObject should have 1 public attribute.", jo
						.getJSONArray(MetaJSONTranslator.FIELDS_PUBLIC_STR)
						.length(), 1);
	}

	/**
	 * Test parsing of simple object to a object MetaModel
	 */
	@Test
	public void simpleObjectAsObjectTest() {
		SimpleObject so = new SimpleObject();
		MetaModel mo = new MetaParser().getMetaAsObject(so);

		// check class name
		Assert.assertEquals("ClassName of SimpleObject was not right parsed!",
				mo.getCLASS_NAME(), "SimpleObject");

		// check id attribute
		Assert.assertEquals("id of SimpleObject was not right parsed!",
				mo.getTYPE_ID(), "id");

		// check public attributes
		Assert.assertEquals(
				"Public fields of SimpleObject was not right parsed!", mo
				.getFIELDS_PUBLIC().size(), 1);

		// check public name attribute
		Assert.assertTrue(
				"Public fields of SimpleObject was not right parsed!", mo
				.getFIELDS_PUBLIC().contains("name"));

		// check all attributes
		Assert.assertEquals("All fields of SimpleObject was not right parsed!",
				mo.getFIELDS_ALL().size(), 4);

		// check id attribute from all attributes
		Assert.assertTrue("All fields of SimpleObject was not right parsed!",
				mo.getFIELDS_ALL().contains("id"));

		// check name attribute from all attributes
		Assert.assertTrue("All fields of SimpleObject was not right parsed!",
				mo.getFIELDS_ALL().contains("name"));

		// check private_name attribute from all attributes
		Assert.assertTrue("All fields of SimpleObject was not right parsed!",
				mo.getFIELDS_ALL().contains("private_name"));

		// check description attribute from all attributes
		Assert.assertTrue("All fields of SimpleObject was not right parsed!",
				mo.getFIELDS_ALL().contains("description"));

		// check private attributes
		Assert.assertEquals(
				"Private fields of SimpleObject was not right parsed!", mo
				.getFIELDS_PRIVATE().size(), 2);

		// check description attribute from private attributes
		Assert.assertTrue(
				"Private fields of SimpleObject was not right parsed!", mo
				.getFIELDS_PRIVATE().contains("description"));

	}

	/**
	 * Test parsing of complex object to a object MetaModel
	 */
	@Test
	public void complextObjectAsObjectTest() {
		ComplexObject co = new ComplexObject();
		MetaModel mo = new MetaParser().getMetaAsObject(co);

		// check all attributes
		Assert.assertEquals(
				"All fields of ComplexObject was not right parsed!", mo
				.getFIELDS_ALL().size(), 3);

		// check id attribute from all attributes
		Assert.assertTrue("All fields of ComplexObject was not right parsed!",
				mo.getFIELDS_ALL().contains("id"));

		// check simpleObject attribute from all attributes
		Assert.assertTrue("All fields of ComplexObject was not right parsed!",
				mo.getFIELDS_ALL().contains("simpleObject"));

		// check collectionOfSimpleObjects attribute from all attributes
		Assert.assertTrue("All fields of ComplexObject was not right parsed!",
				mo.getFIELDS_ALL().contains("collectionOfSimpleObjects"));

		// check private attributes
		Assert.assertEquals(
				"Private fields of ComplexObject was not right parsed!", mo
				.getFIELDS_PRIVATE().size(), 1);

		// check simpleObject attribute from private attributes
		Assert.assertTrue(
				"Private fields of ComplexObject was not right parsed!", mo
				.getFIELDS_PRIVATE().contains("simpleObject"));

		// check complex attributes
		Assert.assertEquals(
				"Complex fields of ComplexObject was not right parsed!", mo
				.getFIELDS_COMPLEX().size(), 2);

	}

	/**
	 * Test parsing of sub object of simple object to a object MetaModel
	 */
	@Test
	public void subClassOfSimpleObjectAsObjectTest() {
		SubClassOfSimpleObject co = new SubClassOfSimpleObject();
		MetaModel mo = new MetaParser().getMetaAsObject(co);

		// check all attributes
		Assert.assertEquals(
				"All fields of ComplexObject was not right parsed!", mo
						.getFIELDS_ALL().size(), 5);

		// check id attribute from all attributes
		Assert.assertTrue("All fields of ComplexObject was not right parsed!",
				mo.getFIELDS_ALL().contains("id"));

		// check bValue attribute from all attributes
		Assert.assertTrue("All fields of ComplexObject was not right parsed!",
				mo.getFIELDS_ALL().contains("bValue"));

		// check name attribute from all attributes
		Assert.assertTrue("All fields of ComplexObject was not right parsed!",
				mo.getFIELDS_ALL().contains("name"));

		// check private_name attribute from all attributes
		Assert.assertTrue("All fields of ComplexObject was not right parsed!",
				mo.getFIELDS_ALL().contains("private_name"));

		// check description attribute from all attributes
		Assert.assertTrue("All fields of ComplexObject was not right parsed!",
				mo.getFIELDS_ALL().contains("description"));

		// check private attributes
		Assert.assertEquals(
				"Private fields of ComplexObject was not right parsed!", mo
						.getFIELDS_PRIVATE().size(), 2);

		// check private_name attribute from private attribute
		Assert.assertTrue(
				"Private fields of ComplexObject was not right parsed!", mo
				.getFIELDS_PRIVATE().contains("private_name"));

		// check description attribute from private attribute
		Assert.assertTrue(
				"Private fields of ComplexObject was not right parsed!", mo
						.getFIELDS_PRIVATE().contains("description"));

		// check complex attributes
		Assert.assertTrue(
				"Complex fields of ComplexObject was not right parsed!", mo
						.getFIELDS_COMPLEX().size() == 0);

		// check regex attributes
		Assert.assertEquals(
				"Regex fields of ComplexObject was not right parsed!", mo
						.getFIELDS_REGEX().size(), 1);

		// check bValue attribute from regex attribute
		Assert.assertTrue(
				"Regex fields of ComplexObject was not right parsed!", mo
						.getFIELDS_REGEX().get(0).containsKey("bValue"));

		// check value of bValue attribute from regex attribute
		Assert.assertTrue(
				"Regex fields of ComplexObject was not right parsed!",
				mo.getFIELDS_REGEX().get(0).get("bValue").equals("true|false"));

	}

}
