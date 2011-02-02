package org.ohuyo.common.org.hibernate.criterion.annotation;

import org.hibernate.criterion.Criterion;

/**
 * 
 * @author rabbit
 * 
 */
public interface CriterionFactory {
	Criterion getInstance(String propertyName, Object value,
			LikeMatchMode likeMatchMode);
}
