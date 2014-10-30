package de.russcity.meta;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import de.russcity.meta.annotation.MetaJSONTranslator;
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
	public void simpleObjectTest() {
		SimpleObject so = new SimpleObject();
		JSONObject jo = new MetaParser().getMeta(so);
		System.out.println("SimpleObject: " + jo);

		// 4 attributes total
		Assert.assertEquals(jo.getJSONArray(MetaJSONTranslator.FIELDS_ALL_STR)
				.length(), 4,
				"SimpleObject should have 4 attributes. The 5th should be skipped");

	}

	@Test
	@org.junit.Test
	public void complextObjectTest() {
		ComplexObject co = new ComplexObject();
		JSONObject jo = new MetaParser().getMeta(co);
		System.out.println("ComplexObject" + jo);

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
	public void subClassOfSimpleObjectTest() {
		SubClassOfSimpleObject co = new SubClassOfSimpleObject();
		JSONObject jo = new MetaParser().getMeta(co);
		System.out.println(jo);
	}
}
