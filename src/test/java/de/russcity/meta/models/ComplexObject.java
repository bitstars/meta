package de.russcity.meta.models;

import java.util.LinkedList;

import de.russcity.meta.annotation.MetaAttr;

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
