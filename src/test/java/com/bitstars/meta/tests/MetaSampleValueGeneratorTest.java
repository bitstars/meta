package com.bitstars.meta.tests;

import nl.flotsam.xeger.Xeger;

import org.junit.Assert;
import org.junit.Test;

public class MetaSampleValueGeneratorTest {

	@Test
	public void generatedValueTest() {
		String regex = "[ab]{4,6}c";
		Xeger generator = new Xeger(regex);
		String result = generator.generate();
		System.out.println(result);
		Assert.assertTrue(result.matches(regex));
	}
}
