package de.russcity.meta;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import de.russcity.meta.annotation.MetaJSONTranslator;
import de.russcity.meta.models.ComplexObject;
import de.russcity.meta.models.SimpleObject;
import de.russcity.meta.parser.MetaParser;

public class MetaParserTest {

	@Test
	public void simpleObjectTest() {
		SimpleObject so = new SimpleObject();
		JSONObject jo = new MetaParser().getMeta(so);
		System.out.println(jo);

		Assert.assertEquals(jo.getJSONObject(MetaJSONTranslator.META_DATA_STR)
				.getJSONArray(MetaJSONTranslator.FIELDS_ALL_STR).length(), 4,
				"SimpleObject should have 4 attributes. The 5th should be skipped");

	}

	@Test
	public void complextObjectTest() {
		ComplexObject co = new ComplexObject();
		JSONObject jo = new MetaParser().getMeta(co);
		System.out.println(jo);
	}
}
