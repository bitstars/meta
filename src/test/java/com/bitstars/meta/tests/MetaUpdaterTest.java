package com.bitstars.meta.tests;

import org.testng.annotations.Test;

import com.bitstars.meta.models.SimpleObject;

public class MetaUpdaterTest {

	@Test
	@org.junit.Test
	public void updateSimpleObjectTest() {
		SimpleObject so1 = new SimpleObject();
		so1.setId(123L);
		so1.setDescription("desc1");
		so1.setName("name1");
	}
}
