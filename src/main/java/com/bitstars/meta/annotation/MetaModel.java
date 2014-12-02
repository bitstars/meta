package com.bitstars.meta.annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class MetaModel {

	private String CLASS_NAME;
	private String TYPE_ID;

	private List<String> TYPE_URL_IMAGE;
	private List<String> TYPE_DATE_LONG;
	private List<String> TYPE_SKIP_META;
	private List<String> TYPE_BOOLEAN;
	private List<String> TYPE_SIMPLE_COLLECTION;
	private List<String> TYPE_SIMPLE_MAP;

	private List<String> FIELDS_ALL;
	private List<String> FIELDS_READ_ONLY;
	private List<String> FIELDS_UNIQ_IN_SCOPE;
	private List<String> FIELDS_NOT_NULL;

	private List<String> FIELDS_TRANSIENT;
	private List<String> FIELDS_PRIVATE;
	private List<String> FIELDS_PUBLIC;
	private List<String> FIELDS_ADMIN;

	private List<Map<String, String>> FIELDS_REGEX;

	private List<MetaModelCompex> FIELDS_COMPLEX;

	public MetaModel() {
		CLASS_NAME = "";
		TYPE_ID = "";
		TYPE_URL_IMAGE = new ArrayList<String>();
		TYPE_DATE_LONG = new ArrayList<String>();
		TYPE_SKIP_META = new ArrayList<String>();
		TYPE_BOOLEAN = new ArrayList<String>();
		TYPE_SIMPLE_COLLECTION = new ArrayList<String>();
		TYPE_SIMPLE_MAP = new ArrayList<String>();

		FIELDS_ALL = new ArrayList<String>();
		FIELDS_READ_ONLY = new ArrayList<String>();
		FIELDS_UNIQ_IN_SCOPE = new ArrayList<String>();
		FIELDS_NOT_NULL = new ArrayList<String>();

		FIELDS_TRANSIENT = new ArrayList<String>();
		FIELDS_PRIVATE = new ArrayList<String>();
		FIELDS_PUBLIC = new ArrayList<String>();
		FIELDS_ADMIN = new ArrayList<String>();

		FIELDS_REGEX = new ArrayList<Map<String, String>>();

		FIELDS_COMPLEX = new ArrayList<MetaModelCompex>();

	}

	public String getCLASS_NAME() {
		return CLASS_NAME;
	}

	public void setCLASS_NAME(String cLASS_NAME) {
		CLASS_NAME = cLASS_NAME;
	}

	public String getTYPE_ID() {
		return TYPE_ID;
	}

	public void setTYPE_ID(String tYPE_ID) {
		TYPE_ID = tYPE_ID;
	}

	public List<String> getTYPE_URL_IMAGE() {
		return TYPE_URL_IMAGE;
	}

	public void setTYPE_URL_IMAGE(List<String> tYPE_URL_IMAGE) {
		TYPE_URL_IMAGE = tYPE_URL_IMAGE;
	}

	public List<String> getTYPE_DATE_LONG() {
		return TYPE_DATE_LONG;
	}

	public void setTYPE_DATE_LONG(List<String> tYPE_DATE_LONG) {
		TYPE_DATE_LONG = tYPE_DATE_LONG;
	}

	public List<String> getTYPE_SKIP_META() {
		return TYPE_SKIP_META;
	}

	public void setTYPE_SKIP_META(List<String> tYPE_SKIP_META) {
		TYPE_SKIP_META = tYPE_SKIP_META;
	}

	public List<String> getFIELDS_ALL() {
		return FIELDS_ALL;
	}

	public void setFIELDS_ALL(List<String> fIELDS_ALL) {
		FIELDS_ALL = fIELDS_ALL;
	}

	public List<String> getFIELDS_READ_ONLY() {
		return FIELDS_READ_ONLY;
	}

	public void setFIELDS_READ_ONLY(List<String> fIELDS_READ_ONLY) {
		FIELDS_READ_ONLY = fIELDS_READ_ONLY;
	}

	public List<String> getFIELDS_UNIQ_IN_SCOPE() {
		return FIELDS_UNIQ_IN_SCOPE;
	}

	public void setFIELDS_UNIQ_IN_SCOPE(List<String> fIELDS_UNIQ_IN_SCOPE) {
		FIELDS_UNIQ_IN_SCOPE = fIELDS_UNIQ_IN_SCOPE;
	}

	public List<String> getFIELDS_NOT_NULL() {
		return FIELDS_NOT_NULL;
	}

	public void setFIELDS_NOT_NULL(List<String> fIELDS_NOT_NULL) {
		FIELDS_NOT_NULL = fIELDS_NOT_NULL;
	}

	public List<String> getFIELDS_TRANSIENT() {
		return FIELDS_TRANSIENT;
	}

	public void setFIELDS_TRANSIENT(List<String> fIELDS_TRANSIENT) {
		FIELDS_TRANSIENT = fIELDS_TRANSIENT;
	}

	public List<String> getFIELDS_PRIVATE() {
		return FIELDS_PRIVATE;
	}

	public void setFIELDS_PRIVATE(List<String> fIELDS_PRIVATE) {
		FIELDS_PRIVATE = fIELDS_PRIVATE;
	}

	public List<String> getFIELDS_PUBLIC() {
		return FIELDS_PUBLIC;
	}

	public void setFIELDS_PUBLIC(List<String> fIELDS_PUBLIC) {
		FIELDS_PUBLIC = fIELDS_PUBLIC;
	}

	public List<MetaModelCompex> getFIELDS_COMPLEX() {
		return FIELDS_COMPLEX;
	}

	public void setFIELDS_COMPLEX(List<MetaModelCompex> fIELDS_COMPLEX) {
		FIELDS_COMPLEX = fIELDS_COMPLEX;
	}

	public JSONObject toJSON() {
		try {
			return new JSONObject(new Gson().toJson(this));
		} catch (JSONException e) {
			return null;
		}
	}

	public List<Map<String, String>> getFIELDS_REGEX() {
		return FIELDS_REGEX;
	}

	public void setFIELDS_REGEX(List<Map<String, String>> fIELDS_REGEX) {
		FIELDS_REGEX = fIELDS_REGEX;
	}

	public List<String> getTYPE_BOOLEAN() {
		return TYPE_BOOLEAN;
	}

	public void setTYPE_BOOLEAN(List<String> tYPE_BOOLEAN) {
		TYPE_BOOLEAN = tYPE_BOOLEAN;
	}

	public List<String> getFIELDS_ADMIN() {
		return FIELDS_ADMIN;
	}

	public void setFIELDS_ADMIN(List<String> fIELDS_ADMIN) {
		FIELDS_ADMIN = fIELDS_ADMIN;
	}

	public List<String> getTYPE_SIMPLE_COLLECTION_STR() {
		return TYPE_SIMPLE_COLLECTION;
	}

	public void setTYPE_SIMPLE_COLLECTION_STR(
			List<String> tYPE_SIMPLE_COLLECTION_STR) {
		TYPE_SIMPLE_COLLECTION = tYPE_SIMPLE_COLLECTION_STR;
	}

	public List<String> getTYPE_SIMPLE_MAP() {
		return TYPE_SIMPLE_MAP;
	}

	public void setTYPE_SIMPLE_MAP(List<String> tYPE_SIMPLE_MAP) {
		TYPE_SIMPLE_MAP = tYPE_SIMPLE_MAP;
	}

}
