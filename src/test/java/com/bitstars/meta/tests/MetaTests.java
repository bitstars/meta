package com.bitstars.meta.tests;

import junit.framework.TestCase;

import org.junit.Test;

import com.bitstars.meta.Meta;
import com.bitstars.meta.Meta.MetaListener;
import com.bitstars.meta.annotation.MetaAttr;
import com.bitstars.meta.utils.RegexUtils;

public class MetaTests extends TestCase {

	public class ExampleClass {

		@MetaAttr(type = MetaAttr.TYPE_ID + MetaAttr.FIELDS_READ_ONLY
				+ MetaAttr.FIELDS_UNIQ_IN_SCOPE)
		private long id;

		@MetaAttr(regex = "YES|MAYBE|NO")
		private String myField1;
		@MetaAttr(regex = RegexUtils.EMAIL_ADDRESS)
		private String email;
		@MetaAttr(type = MetaAttr.TYPE_DATE_LONG)
		private Long createDate;

	}

	@Test
	public void testMeta() {

		Meta meta = new Meta(new MetaListener() {

			@Override
			public void showWarningMsg(String msg) {
				// TODO Auto-generated method stub

			}

			@Override
			public void showErrorMsg(String msg) {
				fail();
			}
		});

		ExampleClass a = new ExampleClass();
		a.id = 2l;
		a.myField1 = "YES";
		a.email = "firstmail@b.com";
		a.createDate = null;

		// test if all fields in object A are valid:
		assertTrue(meta.validate(a));

		ExampleClass newA = meta.cloneViaGson(a);
		String secondValidMail = "secondMail@d.com";
		newA.email = secondValidMail;

		// simulate a change in the object:
		assertTrue(meta.updateObject(a, newA));

		// test if the valid changes were applied to obj A:
		assertTrue(a.email.equals(secondValidMail));

		ExampleClass invalidA = meta.cloneViaGson(a);
		invalidA.id = 3l; // changing the id later on is not allowed

		// test if the validation notices the invalid change:
		assertFalse(meta.validateUpdateObject(a, invalidA));

		// test if the update method does not update a:
		assertFalse(meta.updateObject(a, invalidA));
		assertTrue(a.id == 2l);

		// print out a and invalidA as json:
		meta.toString("a", a);
		meta.toString("invalidA", invalidA);

		ExampleClass b = new ExampleClass();
		b.id = 2l;
		b.myField1 = "YESs";
		b.email = "firstmail@com";

		// test if all fields in object A are valid:
		assertFalse(meta.validate(b));

	}

}
