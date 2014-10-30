package de.russcity.meta.model;

import java.util.List;

public class MetaModel {
	private String CLASS_NAME;
	private String TYPE_ID;

	private List<String> TYPE_URL_IMAGE;
	private List<String> TYPE_DATE_LONG;
	private List<String> TYPE_SKIP_META;

	private List<String> FIELDS_ALL;
	private List<String> FIELDS_READ_ONLY;
	private List<String> FIELDS_UNIQ_IN_SCOPE;
	private List<String> FIELDS_NOT_NULL;

	private List<String> FIELDS_TRANSIENT;
	private List<String> FIELDS_PRIVATE;
	private List<String> FIELDS_PUBLIC;

	private List<MetaComplexModel> FIELDS_COMPLEX;

	class MetaComplexModel {
		private String ATTRIBUTE_NAME;
		private String ATTRIBUTE_TYPE;
		private String META_DATA;
	}
}
