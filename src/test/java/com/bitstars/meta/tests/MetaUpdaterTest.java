package com.bitstars.meta.tests;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.bitstars.meta.exceptions.UpdaterException;
import com.bitstars.meta.models.SimpleObject;
import com.bitstars.meta.models.SubClassOfSimpleObject;
import com.bitstars.meta.parsers.DataParser;
import com.bitstars.meta.updater.MetaUpdater;

/**
 * This method has two Test annotations. This is for you. So you can run this
 * tests either with Run As -> Maven-Test or JUnit-Test. Both works.
 *
 * @author RU$$
 *
 */
public class MetaUpdaterTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	@org.junit.Test
	public void updateSimpleObjectTest() {
		SimpleObject so1 = new SimpleObject();
		so1.setId(123L);
		so1.setDescription("desc1");
		so1.setName("name1");

		JSONObject jo = new JSONObject();
		jo.put("id", 134L);
		jo.put("description", "desc2");
		jo.put("name", "name2");

		SimpleObject so2 = null;

		try {
			so2 = new MetaUpdater().updateObject(so1, jo);
		} catch (UpdaterException e) {
			System.out.println(e);
		}

		Assert.assertEquals(so2.getId(), so1.getId(),
				"Id field cannot be changed!");

		Assert.assertEquals(so2.getDescription(), so1.getDescription(),
				"Description is read only field, but was changed!");

		Assert.assertEquals(so2.getName(), "name2",
				"Name attribute was not changed!");

		System.out.println("Updated object: "
				+ new DataParser().parseSingleObject(so2));
	}

	@Test
	@org.junit.Test
	public void updateSubClassOfSimpleObjectTest() throws UpdaterException {
		SubClassOfSimpleObject scso1 = new SubClassOfSimpleObject();
		scso1.setId(123L);
		scso1.setDescription("desc1");
		scso1.setName("name1");
		scso1.setbValue(true);

		JSONObject jo = new JSONObject();
		jo.put("id", 134L);
		jo.put("description", "desc2");
		jo.put("name", "name2");
		jo.put("bValue", "trrrue");

		SubClassOfSimpleObject so2 = null;
		thrown.expect(UpdaterException.class);
		thrown.expectMessage("Key 'bValue' has a regex 'true|false' which is not matched by value 'trrrue'");

		so2 = new MetaUpdater().updateObject(scso1, jo);

	}
}
