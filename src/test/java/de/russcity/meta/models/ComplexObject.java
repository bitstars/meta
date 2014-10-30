package de.russcity.meta.models;

import java.util.LinkedList;

import de.russcity.meta.annotation.MetaAttr;

/**
 * Important! Do not edit this class any more. It is used for JUnit test.
 * Editing of this class can destroy the tests cases
 * 
 * @author RU$$
 *
 */
public class ComplexObject {
	@MetaAttr(type = MetaAttr.TYPE_ID)
	public int id;
	@MetaAttr(type = MetaAttr.FIELDS_PRIVATE)
	public SimpleObject simpleObject;
	@MetaAttr(type = MetaAttr.FIELDS_NOT_NULL)
	public LinkedList<SimpleObject> collectionOfSimpleObjects;

	@MetaAttr(type = MetaAttr.TYPE_SKIP_META)
	public LinkedList<SimpleObject> thisAttributeShouldNotBeConsideredByMeta;

}
