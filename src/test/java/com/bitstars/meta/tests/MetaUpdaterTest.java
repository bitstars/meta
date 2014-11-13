package com.bitstars.meta.tests;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bitstars.meta.annotation.MetaAttr;
import com.bitstars.meta.exceptions.UpdaterException;
import com.bitstars.meta.models.SimpleObject;
import com.bitstars.meta.models.SubClassOfSimpleObject;
import com.bitstars.meta.parsers.DataParser;
import com.bitstars.meta.updater.MetaUpdater;

/**
 *
 * @author RU$$
 *
 */
public class MetaUpdaterTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
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

		Assert.assertEquals("Id field cannot be changed!", so2.getId(),
				so1.getId());

		Assert.assertEquals("Description is read only field, but was changed!",
				so2.getDescription(), so1.getDescription());

		Assert.assertEquals("Name attribute was not changed!", so2.getName(),
				"name2");

		System.out.println("Updated object: "
				+ new DataParser().parseSingleObject(so2));
	}

	@Test
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

	@Test
	public void updateObjectIncludingStrategy() throws UpdaterException {
		SimpleObject so = new SimpleObject();
		so.setId(123L);
		so.setDescription("Desc1");
		so.setName("Name1");
		so.setPrivate_name("Private_Name1");
		so.setUnvisible_attribute("Unvisible 1");

		SimpleObject so2 = new SimpleObject();
		so2.setId(345L);
		so2.setDescription("Desc2");
		so2.setName("Name2");
		so2.setPrivate_name("Private_Name2");
		so2.setUnvisible_attribute("Unvisible 2");

		so = new MetaUpdater().updateObjectIncludingAttr(so,
				new DataParser().parseSingleObject(so2),
				MetaAttr.FIELDS_PRIVATE);

		Assert.assertTrue(so.getId() == 123L);
		Assert.assertEquals(so.getDescription(), "Desc1");
		Assert.assertEquals(so.getPrivate_name(), "Private_Name2");
		Assert.assertEquals(so.getUnvisible_attribute(), "Unvisible 1");

	}
}
