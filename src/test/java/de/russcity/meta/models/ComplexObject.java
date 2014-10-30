package de.russcity.meta.models;

import java.util.LinkedList;

import de.russcity.meta.annotation.MetaAttr;

public class ComplexObject {
	public SimpleObject simpleObject;
	public LinkedList<SimpleObject> collectionOfSimpleObjects;
	@MetaAttr(type = MetaAttr.TYPE_ID)
	public int id;
}
