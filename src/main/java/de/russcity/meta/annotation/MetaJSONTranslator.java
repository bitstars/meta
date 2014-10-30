package de.russcity.meta.annotation;

public class MetaJSONTranslator {
	// String presentation of types
	public static final String TYPE_ID_STR = "TYPE_ID";
	public static final String TYPE_URL_IMAGE_STR = "TYPE_URL_IMAGE";
	public static final String TYPE_DATE_LONG_STR = "TYPE_DATE_LONG";
	public static final String FIELDS_READ_ONLY_STR = "FIELDS_READ_ONLY";
	public static final String FIELDS_UNIQ_IN_SCOPE_STR = "FIELDS_UNIQ_IN_SCOPE";
	public static final String FIELDS_TRANSIENT_STR = "FIELDS_TRANSIENT";
	public static final String FIELDS_PRIVATE_STR = "FIELDS_PRIVATE";
	public static final String FIELDS_PUBLIC_STR = "FIELDS_PUBLIC";

	// By adding of new field descriptors this array should be also extends with
	// it new attributes. The order is not important
	public static int[] allAttrs = { MetaAttr.FIELDS_READ_ONLY,
		MetaAttr.FIELDS_UNIQ_IN_SCOPE, MetaAttr.FIELDS_TRANSIENT,
		MetaAttr.TYPE_DATE_LONG, MetaAttr.TYPE_ID, MetaAttr.TYPE_URL_IMAGE,
		MetaAttr.FIELDS_PRIVATE, MetaAttr.FIELDS_PUBLIC, MetaAttr.FIELDS_NOT_NULL };

	// Main meta descriptor
	public static final String META_DATA_STR = "META_DATA";
	public static final String META_TABLE_STR = "META_TABLE";

	// Field descriptors
	public static final String FIELDS_ALL_STR = "FIELDS_ALL";
	public static final String FIELDS_REGEX_STR = "FIELDS_REGEX";
	public static final String FIELDS_COMPLEX_STR = "FIELDS_COMPLEX";
	public static final String FIELDS_NOT_NULL_STR = "FIELDS_NOT_NULL";
	public static final String COMPLEX_SINGLE_DATA_STR = "COMPLEX_SINGLE_DATA";
	public static final String COMPLEX_MULTIPLE_DATA_STR = "COMPLEX_MULTIPLE_DATA";
	
	// Class name
	public static final String CLASS_NAME_STR = "CLASS_NAME";

}
