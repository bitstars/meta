package de.russcity.meta;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import de.russcity.meta.annotation.MetaJSONTranslator;
import de.russcity.meta.model.MetaModel;
import de.russcity.meta.models.ComplexObject;
import de.russcity.meta.models.SimpleObject;
import de.russcity.meta.models.SubClassOfSimpleObject;
import de.russcity.meta.parser.MetaParser;

/**
 * This method has two Test annotations. This is for you. So you can run this
 * tests either with Run As -> Maven-Test or JUnit-Test. Both works.
 *
 * @author RU$$
 *
 */
public class MetaParserTest {

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

	@Test
	@org.junit.Test
	public void simpleObjectAsObjectTest() {
		SimpleObject so = new SimpleObject();
		MetaModel mo = new MetaParser().getMetaAsObject(so);

		Assert.assertEquals(mo.getCLASS_NAME(), "SimpleObject",
				"ClassName of SimpleObject was not right parsed!");

		Assert.assertEquals(mo.getTYPE_ID(), "id",
				"id of SimpleObject was not right parsed!");

		Assert.assertEquals(mo.getFIELDS_PUBLIC().size(), 1,
				"Public fields of SimpleObject was not right parsed!");

		Assert.assertTrue(mo.getFIELDS_PUBLIC().contains("name"),
				"Public fields of SimpleObject was not right parsed!");

		Assert.assertEquals(mo.getFIELDS_ALL().size(), 4,
				"All fields of SimpleObject was not right parsed!");

		Assert.assertTrue(mo.getFIELDS_ALL().contains("id"),
				"All fields of SimpleObject was not right parsed!");

		Assert.assertTrue(mo.getFIELDS_ALL().contains("name"),
				"All fields of SimpleObject was not right parsed!");

		Assert.assertTrue(mo.getFIELDS_ALL().contains("private_name"),
				"All fields of SimpleObject was not right parsed!");

		Assert.assertTrue(mo.getFIELDS_ALL().contains("description"),
				"All fields of SimpleObject was not right parsed!");

		Assert.assertEquals(mo.getFIELDS_PRIVATE().size(), 2,
				"Private fields of SimpleObject was not right parsed!");

		Assert.assertTrue(mo.getFIELDS_PRIVATE().contains("description"),
				"Private fields of SimpleObject was not right parsed!");

	}

	@Test
	@org.junit.Test
	public void complextObjectAsObjectTest() {
		ComplexObject co = new ComplexObject();
		MetaModel mo = new MetaParser().getMetaAsObject(co);

		Assert.assertEquals(mo.getFIELDS_ALL().size(), 3,
				"All fields of ComplexObject was not right parsed!");

		Assert.assertTrue(mo.getFIELDS_ALL().contains("id"),
				"All fields of ComplexObject was not right parsed!");

		Assert.assertTrue(mo.getFIELDS_ALL().contains("simpleObject"),
				"All fields of ComplexObject was not right parsed!");

		Assert.assertTrue(
				mo.getFIELDS_ALL().contains("collectionOfSimpleObjects"),
				"All fields of ComplexObject was not right parsed!");

		Assert.assertEquals(mo.getFIELDS_PRIVATE().size(), 1,
				"Private fields of ComplexObject was not right parsed!");

		Assert.assertTrue(mo.getFIELDS_PRIVATE().contains("simpleObject"),
				"Private fields of ComplexObject was not right parsed!");

		Assert.assertEquals(mo.getFIELDS_COMPLEX().size(), 2,
				"Complex fields of ComplexObject was not right parsed!");

	}

}
