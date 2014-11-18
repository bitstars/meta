package com.bitstars.meta.annotation;

public class MetaModelCompex {
	private String ATTRIBUTE_NAME;
	private String ATTRIBUTE_TYPE;
	private MetaModel META_DATA;

	public MetaModelCompex() {
		ATTRIBUTE_NAME = "";
		ATTRIBUTE_TYPE = "";
		META_DATA = new MetaModel();
	}

	public String getATTRIBUTE_NAME() {
		return ATTRIBUTE_NAME;
	}

	public void setATTRIBUTE_NAME(String aTTRIBUTE_NAME) {
		ATTRIBUTE_NAME = aTTRIBUTE_NAME;
	}

	public String getATTRIBUTE_TYPE() {
		return ATTRIBUTE_TYPE;
	}

	public void setATTRIBUTE_TYPE(String aTTRIBUTE_TYPE) {
		ATTRIBUTE_TYPE = aTTRIBUTE_TYPE;
	}

	public MetaModel getMETA_DATA() {
		return META_DATA;
	}

	public void setMETA_DATA(MetaModel mETA_DATA) {
		META_DATA = mETA_DATA;
	}

}
