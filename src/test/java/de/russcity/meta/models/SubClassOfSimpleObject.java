package de.russcity.meta.models;

import de.russcity.meta.annotation.MetaAttr;

/**
 * Important! Do not edit this class any more. It is used for JUnit test.
 * Editing of this class can destroy the tests cases
 *
 * @author RU$$
 *
 */
public class SubClassOfSimpleObject extends SimpleObject {

	// This attribute just hidden the id attribute from superclass
	private String id;
	@MetaAttr(regex = "true|false")
	private boolean bValue;
}
