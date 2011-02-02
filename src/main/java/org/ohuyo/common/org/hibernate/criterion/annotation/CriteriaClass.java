package org.ohuyo.common.org.hibernate.criterion.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义查询类的orm Class
 * 
 * @author rabbit
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CriteriaClass {
	Class<?> clazz();
}
