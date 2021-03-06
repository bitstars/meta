package com.bitstars.meta.models;

import com.bitstars.meta.annotation.MetaAttr;

/**
 * Important! Do not edit this class any more. It is used for JUnit test.
 * Editing of this class can destroy the tests cases
 *
 * @author RU$$
 *
 */
public class SimpleObject {

	@MetaAttr(type = MetaAttr.TYPE_ID + MetaAttr.FIELDS_NOT_NULL)
	private Long id;

	@MetaAttr(type = MetaAttr.FIELDS_PUBLIC)
	private String name;

	@MetaAttr(type = MetaAttr.FIELDS_PRIVATE)
	private String private_name;

	@MetaAttr(type = MetaAttr.FIELDS_PRIVATE + MetaAttr.FIELDS_READ_ONLY
			+ MetaAttr.FIELDS_ADMIN)
	private String description;

	@MetaAttr(type = MetaAttr.TYPE_SKIP_META)
	private String unvisible_attribute;

	public String getPrivate_name() {
		return private_name;
	}

	public void setPrivate_name(String private_name) {
		this.private_name = private_name;
	}

	public String getUnvisible_attribute() {
		return unvisible_attribute;
	}

	public void setUnvisible_attribute(String unvisible_attribute) {
		this.unvisible_attribute = unvisible_attribute;
	}

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
