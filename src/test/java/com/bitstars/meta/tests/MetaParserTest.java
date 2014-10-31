package com.bitstars.meta.tests;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.bitstars.meta.annotation.MetaJSONTranslator;
import com.bitstars.meta.models.ComplexObject;
import com.bitstars.meta.models.MetaModel;
import com.bitstars.meta.models.SimpleObject;
import com.bitstars.meta.models.SubClassOfSimpleObject;
import com.bitstars.meta.parsers.MetaParser;

/**
 * This method has two Test annotations. This is for you. So you can run this
 * tests either with Run As -> Maven-Test or JUnit-Test. Both works.
 *
 * @author RU$$
 *
 */
public class MetaParserTest {

	/**
	 * Test parsing of simple object to a JSON meta
	 */
	@Test
	@org.junit.Test
	public void simpleObjectAsJSONTest() {
		SimpleObject so = new SimpleObject();
		JSONObject jo = new MetaParser().getMetaAsJSON(so);
		System.out.println("SimpleObject: " + jo);

		// 4 attributes total
		Assert.assertEquals(jo.getJSONArray(MetaJSONTranslator.FIELDS_ALL_STR)
				.length(), 4,
				"SimpleObject should have 4 attributes. The 5th should be skipped");

	}

	/**
	 * Test parsing of complex object to a JSON meta
	 */
	@Test
	@org.junit.Test
	public void complextObjectAsJSONTest() {
		ComplexObject co = new ComplexObject();
		JSONObject jo = new MetaParser().getMetaAsJSON(co);
		System.out.println("ComplexObject: " + jo);

		// 3 attributes total
		Assert.assertEquals(jo.getJSONArray(MetaJSONTranslator.FIELDS_ALL_STR)
				.length(), 3, "ComplexObject should have 3 attributes.");

		// 2 complex attributes
		Assert.assertEquals(
				jo.getJSONArray(MetaJSONTranslator.FIELDS_COMPLEX_STR).length(),
				2, "ComplexObject should have 2 complex attributes.");

		// 1 private attribute
		Assert.assertEquals(
				jo.getJSONArray(MetaJSONTranslator.FIELDS_PRIVATE_STR).length(),
				1, "ComplexObject should have 1 private attribute.");

	}

	/**
	 * Test parsing of sub object of simple object to a JSON meta
	 */
	@Test
	@org.junit.Test
	public void subClassOfSimpleObjectAsJSONTest() {
		SubClassOfSimpleObject co = new SubClassOfSimpleObject();
		JSONObject jo = new MetaParser().getMetaAsJSON(co);
		System.out.println("SubClassOfSimpleObject: " + jo);

		// 5 attributes total
		Assert.assertEquals(jo.getJSONArray(MetaJSONTranslator.FIELDS_ALL_STR)
				.length(), 5,
				"SubClassOfSimpleObject should have 5 attributes.");

		// 1 public attribute
		Assert.assertEquals(
				jo.getJSONArray(MetaJSONTranslator.FIELDS_PUBLIC_STR).length(),
				1, "SubClassOfSimpleObject should have 1 public attribute.");
	}

	/**
	 * Test parsing of simple object to a object MetaModel
	 */
	@Test
	@org.junit.Test
	public void simpleObjectAsObjectTest() {
		SimpleObject so = new SimpleObject();
		MetaModel mo = new MetaParser().getMetaAsObject(so);

		// check class name
		Assert.assertEquals(mo.getCLASS_NAME(), "SimpleObject",
				"ClassName of SimpleObject was not right parsed!");

		// check id attribute
		Assert.assertEquals(mo.getTYPE_ID(), "id",
				"id of SimpleObject was not right parsed!");

		// check public attributes
		Assert.assertEquals(mo.getFIELDS_PUBLIC().size(), 1,
				"Public fields of SimpleObject was not right parsed!");

		// check public name attribute
		Assert.assertTrue(mo.getFIELDS_PUBLIC().contains("name"),
				"Public fields of SimpleObject was not right parsed!");

		// check all attributes
		Assert.assertEquals(mo.getFIELDS_ALL().size(), 4,
				"All fields of SimpleObject was not right parsed!");

		// check id attribute from all attributes
		Assert.assertTrue(mo.getFIELDS_ALL().contains("id"),
				"All fields of SimpleObject was not right parsed!");

		// check name attribute from all attributes
		Assert.assertTrue(mo.getFIELDS_ALL().contains("name"),
				"All fields of SimpleObject was not right parsed!");

		// check private_name attribute from all attributes
		Assert.assertTrue(mo.getFIELDS_ALL().contains("private_name"),
				"All fields of SimpleObject was not right parsed!");

		// check description attribute from all attributes
		Assert.assertTrue(mo.getFIELDS_ALL().contains("description"),
				"All fields of SimpleObject was not right parsed!");

		// check private attributes
		Assert.assertEquals(mo.getFIELDS_PRIVATE().size(), 2,
				"Private fields of SimpleObject was not right parsed!");

		// check description attribute from private attributes
		Assert.assertTrue(mo.getFIELDS_PRIVATE().contains("description"),
				"Private fields of SimpleObject was not right parsed!");

	}

	/**
	 * Test parsing of complex object to a object MetaModel
	 */
	@Test
	@org.junit.Test
	public void complextObjectAsObjectTest() {
		ComplexObject co = new ComplexObject();
		MetaModel mo = new MetaParser().getMetaAsObject(co);

		// check all attributes
		Assert.assertEquals(mo.getFIELDS_ALL().size(), 3,
				"All fields of ComplexObject was not right parsed!");

		// check id attribute from all attributes
		Assert.assertTrue(mo.getFIELDS_ALL().contains("id"),
				"All fields of ComplexObject was not right parsed!");

		// check simpleObject attribute from all attributes
		Assert.assertTrue(mo.getFIELDS_ALL().contains("simpleObject"),
				"All fields of ComplexObject was not right parsed!");

		// check collectionOfSimpleObjects attribute from all attributes
		Assert.assertTrue(
				mo.getFIELDS_ALL().contains("collectionOfSimpleObjects"),
				"All fields of ComplexObject was not right parsed!");

		// check private attributes
		Assert.assertEquals(mo.getFIELDS_PRIVATE().size(), 1,
				"Private fields of ComplexObject was not right parsed!");

		// check simpleObject attribute from private attributes
		Assert.assertTrue(mo.getFIELDS_PRIVATE().contains("simpleObject"),
				"Private fields of ComplexObject was not right parsed!");

		// check complex attributes
		Assert.assertEquals(mo.getFIELDS_COMPLEX().size(), 2,
				"Complex fields of ComplexObject was not right parsed!");

	}

	/**
	 * Test parsing of sub object of simple object to a object MetaModel
	 */
	@Test
	@org.junit.Test
	public void subClassOfSimpleObjectAsObjectTest() {
		SubClassOfSimpleObject co = new SubClassOfSimpleObject();
		MetaModel mo = new MetaParser().getMetaAsObject(co);

		// check all attributes
		Assert.assertEquals(mo.getFIELDS_ALL().size(), 5,
				"All fields of ComplexObject was not right parsed!");

		// check id attribute from all attributes
		Assert.assertTrue(mo.getFIELDS_ALL().contains("id"),
				"All fields of ComplexObject was not right parsed!");

		// check bValue attribute from all attributes
		Assert.assertTrue(mo.getFIELDS_ALL().contains("bValue"),
				"All fields of ComplexObject was not right parsed!");

		// check name attribute from all attributes
		Assert.assertTrue(mo.getFIELDS_ALL().contains("name"),
				"All fields of ComplexObject was not right parsed!");

		// check private_name attribute from all attributes
		Assert.assertTrue(mo.getFIELDS_ALL().contains("private_name"),
				"All fields of ComplexObject was not right parsed!");

		// check description attribute from all attributes
		Assert.assertTrue(mo.getFIELDS_ALL().contains("description"),
				"All fields of ComplexObject was not right parsed!");

		// check private attributes
		Assert.assertEquals(mo.getFIELDS_PRIVATE().size(), 2,
				"Private fields of ComplexObject was not right parsed!");

		// check private_name attribute from private attribute
		Assert.assertTrue(mo.getFIELDS_PRIVATE().contains("private_name"),
				"Private fields of ComplexObject was not right parsed!");

		// check description attribute from private attribute
		Assert.assertTrue(mo.getFIELDS_PRIVATE().contains("description"),
				"Private fields of ComplexObject was not right parsed!");

		// check complex attributes
		Assert.assertTrue(mo.getFIELDS_COMPLEX().size() == 0,
				"Complex fields of ComplexObject was not right parsed!");

		// check regex attributes
		Assert.assertEquals(mo.getFIELDS_REGEX().size(), 1,
				"Regex fields of ComplexObject was not right parsed!");

		// check bValue attribute from regex attribute
		Assert.assertTrue(mo.getFIELDS_REGEX().get(0).containsKey("bValue"),
				"Regex fields of ComplexObject was not right parsed!");

		// check value of bValue attribute from regex attribute
		Assert.assertTrue(
				mo.getFIELDS_REGEX().get(0).get("bValue").equals("true|false"),
				"Regex fields of ComplexObject was not right parsed!");

	}

}
