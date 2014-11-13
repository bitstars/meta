package com.bitstars.meta.annotation;

public class MetaJSONTranslator {
	// String presentation of types
	public static final String TYPE_ID_STR = "TYPE_ID";
	public static final String TYPE_URL_IMAGE_STR = "TYPE_URL_IMAGE";
	public static final String TYPE_DATE_LONG_STR = "TYPE_DATE_LONG";
	public static final String TYPE_BOOLEAN_STR = "TYPE_BOOLEAN";

	// By adding of new field descriptors this array should be also extends with
	// it new attributes. The order is not important
	public static int[] allAttrs = { MetaAttr.FIELDS_READ_ONLY,
		MetaAttr.FIELDS_UNIQ_IN_SCOPE, MetaAttr.FIELDS_TRANSIENT,
		MetaAttr.TYPE_DATE_LONG, MetaAttr.TYPE_ID, MetaAttr.TYPE_URL_IMAGE,
		MetaAttr.FIELDS_PRIVATE, MetaAttr.FIELDS_PUBLIC,
		MetaAttr.FIELDS_NOT_NULL, MetaAttr.TYPE_BOOLEAN };

	// Main meta descriptor
	public static final String META_DATA_STR = "META_DATA";
	public static final String META_TABLE_STR = "META_TABLE";

	// Field descriptors
	public static final String FIELDS_ALL_STR = "FIELDS_ALL";
	public static final String FIELDS_REGEX_STR = "FIELDS_REGEX";
	public static final String FIELDS_COMPLEX_STR = "FIELDS_COMPLEX";
	public static final String FIELDS_NOT_NULL_STR = "FIELDS_NOT_NULL";
	public static final String FIELDS_READ_ONLY_STR = "FIELDS_READ_ONLY";
	public static final String FIELDS_UNIQ_IN_SCOPE_STR = "FIELDS_UNIQ_IN_SCOPE";
	public static final String FIELDS_TRANSIENT_STR = "FIELDS_TRANSIENT";
	public static final String FIELDS_PRIVATE_STR = "FIELDS_PRIVATE";
	public static final String FIELDS_PUBLIC_STR = "FIELDS_PUBLIC";
	public static final String FIELDS_ADMIN_STR = "FIELDS_ADMIN";

	// Class name
	public static final String CLASS_NAME_STR = "CLASS_NAME";

	// Complex class
	public static final String ATTRIBUTE_NAME_STR = "ATTRIBUTE_NAME";
	public static final String ATTRIBUTE_TYPE_STR = "ATTRIBUTE_TYPE";
	public static final String ATTRIBUTE_TYPE_SINGLE_STR = "SINGLE";
	public static final String ATTRIBUTE_TYPE_COLLECTION_STR = "COLLECTION";

	/**
	 * Translates an integer attribute to its string representation. Important!:
	 * this method should be updated if there are new types added in MetaAtt
	 *
	 * @param type
	 *            - defined in MetaAtt
	 * @return its string representation, defined in MetaJSONTranslator
	 */
	public static String translateType(int type) {
		switch (type) {
		case MetaAttr.FIELDS_READ_ONLY:
			return FIELDS_READ_ONLY_STR;
		case MetaAttr.FIELDS_UNIQ_IN_SCOPE:
			return FIELDS_UNIQ_IN_SCOPE_STR;
		case MetaAttr.FIELDS_TRANSIENT:
			return FIELDS_TRANSIENT_STR;
		case MetaAttr.TYPE_DATE_LONG:
			return TYPE_DATE_LONG_STR;
		case MetaAttr.TYPE_ID:
			return TYPE_ID_STR;
		case MetaAttr.TYPE_URL_IMAGE:
			return TYPE_URL_IMAGE_STR;
		case MetaAttr.FIELDS_PRIVATE:
			return FIELDS_PRIVATE_STR;
		case MetaAttr.FIELDS_PUBLIC:
			return FIELDS_PUBLIC_STR;
		case MetaAttr.FIELDS_NOT_NULL:
			return FIELDS_NOT_NULL_STR;
		case MetaAttr.FIELDS_ADMIN:
			return FIELDS_ADMIN_STR;
		case MetaAttr.TYPE_BOOLEAN:
			return TYPE_BOOLEAN_STR;
		default:
			return "unknownType";
		}
	}

	public static int getMetaAttrTypeOfField(MetaModel metaModel, String field) {
		int result = 0;
		if (metaModel.getFIELDS_ADMIN().contains(field)) {
			result += MetaAttr.FIELDS_ADMIN;
		}
		if (metaModel.getFIELDS_NOT_NULL().contains(field)) {
			result += MetaAttr.FIELDS_NOT_NULL;
		}
		if (metaModel.getFIELDS_PRIVATE().contains(field)) {
			result += MetaAttr.FIELDS_PRIVATE;
		}
		if (metaModel.getFIELDS_PUBLIC().contains(field)) {
			result += MetaAttr.FIELDS_PUBLIC;
		}
		if (metaModel.getFIELDS_READ_ONLY().contains(field)) {
			result += MetaAttr.FIELDS_READ_ONLY;
		}
		if (metaModel.getFIELDS_TRANSIENT().contains(field)) {
			result += MetaAttr.FIELDS_TRANSIENT;
		}
		if (metaModel.getFIELDS_UNIQ_IN_SCOPE().contains(field)) {
			result += MetaAttr.FIELDS_UNIQ_IN_SCOPE;
		}
		if (metaModel.getTYPE_BOOLEAN().contains(field)) {
			result += MetaAttr.TYPE_BOOLEAN;
		}
		if (metaModel.getTYPE_DATE_LONG().contains(field)) {
			result += MetaAttr.TYPE_DATE_LONG;
		}
		if (metaModel.getTYPE_ID().equals(field)) {
			result += MetaAttr.TYPE_ID;
		}
		if (metaModel.getTYPE_SKIP_META().contains(field)) {
			result += MetaAttr.TYPE_SKIP_META;
		}
		if (metaModel.getTYPE_URL_IMAGE().contains(field)) {
			result += MetaAttr.TYPE_URL_IMAGE;
		}
		return result;
	}

}
