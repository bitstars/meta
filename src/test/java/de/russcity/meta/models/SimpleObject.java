package de.russcity.meta.models;

import de.russcity.meta.annotation.MetaAttr;

public class SimpleObject {

	@MetaAttr(type = MetaAttr.TYPE_ID)
	private Long id;

	@MetaAttr(type = MetaAttr.FIELDS_PUBLIC)
	private String name;

	@MetaAttr(type = MetaAttr.FIELDS_PRIVATE)
	private String private_name;

	@MetaAttr(type = MetaAttr.FIELDS_PRIVATE + MetaAttr.FIELDS_READ_ONLY)
	private String description;

	@MetaAttr(type = MetaAttr.TYPE_SKIP_META)
	private String unvisible_attribute;

	public SimpleObject() {
	}

	public SimpleObject(Long id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
