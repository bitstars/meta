package com.bitstars.meta.tests;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bitstars.meta.exceptions.ValidatorException;
import com.bitstars.meta.models.SimpleObject;
import com.bitstars.meta.models.SubClassOfSimpleObject;
import com.bitstars.meta.parsers.MetaParser;
import com.bitstars.meta.validators.MetaValidator;

/**
 * 
 * @author RU$$
 *
 */
public class MetaValidatorTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	/**
	 * Test parsing of simple object to a JSON meta
	 *
	 * @throws ValidatorException
	 */
	@Test
	public void simpleObjectAsJSONTest() throws ValidatorException {
		SimpleObject so = generateSimpleObjectWithAttributes();

		Assert.assertTrue(new MetaValidator().isValidObject(so));

		JSONObject jo = new JSONObject();
		jo.put("description", "ddd");
		jo.put("id", "2323L");
		jo.put("name", "nameS");

		Assert.assertTrue(new MetaValidator().isValidObject(
				new MetaParser().getMetaAsObject(so), jo));

		JSONObject jo2 = new JSONObject();
		jo2.put("description", "ddd");
		jo2.put("name", "nameS");

		thrown.expect(ValidatorException.class);
		thrown.expectMessage("Value of key 'id' cannot be null");

		// SimpleObject without id is corrupted
		Assert.assertFalse(new MetaValidator().isValidObject(
				new MetaParser().getMetaAsObject(so), jo2));

	}

	@Test
	public void subClassOfSimpleObjectTest() throws ValidatorException {
		SubClassOfSimpleObject scso = new SubClassOfSimpleObject();
		scso.setbValue(true);
		scso.setId(234L);

		JSONObject jo = new JSONObject();
		jo.put("id", "123L");
		jo.put("bValue", "false");

		Assert.assertTrue(new MetaValidator().isValidObject(scso));

		Assert.assertTrue(new MetaValidator().isValidObject(
				new MetaParser().getMetaAsObject(scso), jo));

		jo.put("bValue", "falses");

		thrown.expect(ValidatorException.class);
		thrown.expectMessage("Key 'bValue' and its value 'falses' are not match regex 'true|false'");

		Assert.assertFalse(new MetaValidator().isValidObject(
				new MetaParser().getMetaAsObject(scso), jo));

	}

	private SimpleObject generateSimpleObjectWithAttributes() {
		SimpleObject so = new SimpleObject();
		so.setDescription("myDescription");
		so.setId(123L);
		so.setName("myName");

		return so;
	}
}
