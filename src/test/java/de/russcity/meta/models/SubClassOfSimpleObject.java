package de.russcity.meta.models;

import de.russcity.meta.annotation.MetaAttr;

public class SubClassOfSimpleObject extends SimpleObject {

	// This attribute just hidden the id attribute from superclass
	private String id;
	@MetaAttr(regex = "true|false")
	private boolean bValue;
}
