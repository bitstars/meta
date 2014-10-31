package com.bitstars.meta.models;

import com.bitstars.meta.annotation.MetaAttr;

/**
 * Important! Do not edit this class any more. It is used for JUnit test.
 * Editing of this class can destroy the tests cases
 *
 * @author RU$$
 *
 */
public class SubClassOfSimpleObject extends SimpleObject {

	@MetaAttr(regex = "true|false")
	private boolean bValue;

	public boolean isbValue() {
		return bValue;
	}

	public void setbValue(boolean bValue) {
		this.bValue = bValue;
	}
}
