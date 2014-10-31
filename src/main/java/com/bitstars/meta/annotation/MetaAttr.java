package com.bitstars.meta.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CrowDev-Meta-Attribute can annotate one field. There are following
 * attributes:<br>
 * <ul>
 * <li><b>regex </b>, default: <b>""</b><br>
 * Read about regex: <a
 * href="http://www.vogella.com/articles/JavaRegularExpressions/article.html"
 * >here</a></li>
 * <li><b>type</b>, default: <b>""</b>
 *
 * </li>
 * </ul>
 *
 * @author Ruslan
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MetaAttr {

	// Type descriptors
	public static final int TYPE_ID = 1;
	public static final int TYPE_URL_IMAGE = 1 << 1;
	public static final int TYPE_DATE_LONG = 1 << 2;
	public static final int TYPE_SKIP_META = 1 << 3;

	// Field descriptors
	public static final int FIELDS_READ_ONLY = 1 << 10;
	public static final int FIELDS_UNIQ_IN_SCOPE = 1 << 11;
	public static final int FIELDS_TRANSIENT = 1 << 12;
	public static final int FIELDS_PRIVATE = 1 << 13;
	public static final int FIELDS_PUBLIC = 1 << 14;
	public static final int FIELDS_NOT_NULL = 1 << 15;

	public String regex() default "";

	public int type() default 0;
}
