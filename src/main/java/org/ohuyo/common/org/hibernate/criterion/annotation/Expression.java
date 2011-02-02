package org.ohuyo.common.org.hibernate.criterion.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author rabbit
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Expression {
	Operator operator();

	String propertyName();

	LikeMatchMode likeMatchMode() default LikeMatchMode.exact;

}
