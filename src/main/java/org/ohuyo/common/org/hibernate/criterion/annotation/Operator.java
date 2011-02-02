package org.ohuyo.common.org.hibernate.criterion.annotation;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * 
 * @author rabbit
 * 
 */
public enum Operator {
	/**
	 * 
	 */
	idEq(new CriterionFactory() {
		public Criterion getInstance(String propertyName, Object value,
				LikeMatchMode likeMatchMode) {
			return Restrictions.idEq(value);
		}
	}),

	/**
	 * µÈÓÚ
	 */
	eq(new CriterionFactory() {
		public Criterion getInstance(String propertyName, Object value,
				LikeMatchMode likeMatchMode) {
			return Restrictions.eq(propertyName, value);
		}
	}),

	/**
	 * 
	 */
	ne(new CriterionFactory() {
		public Criterion getInstance(String propertyName, Object value,
				LikeMatchMode likeMatchMode) {
			return Restrictions.ne(propertyName, value);
		}
	}),

	/**
	 * 
	 */
	like(new CriterionFactory() {
		public Criterion getInstance(String propertyName, Object value,
				LikeMatchMode likeMatchMode) {
			return Restrictions.like(propertyName, (String) value,
					likeMatchMode.hibernateMatchMode());
		}
	}),

	/**
	 * 
	 */
	ilike(new CriterionFactory() {
		public Criterion getInstance(String propertyName, Object value,
				LikeMatchMode likeMatchMode) {
			return Restrictions.ilike(propertyName, (String) value,
					likeMatchMode.hibernateMatchMode());
		}
	}),

	/**
	 * 
	 */
	gt(new CriterionFactory() {
		public Criterion getInstance(String propertyName, Object value,
				LikeMatchMode likeMatchMode) {
			return Restrictions.gt(propertyName, value);
		}
	}),

	/**
	 * 
	 */
	lt(new CriterionFactory() {
		public Criterion getInstance(String propertyName, Object value,
				LikeMatchMode likeMatchMode) {
			return Restrictions.lt(propertyName, value);
		}
	}),

	/**
	 * 
	 */
	le(new CriterionFactory() {
		public Criterion getInstance(String propertyName, Object value,
				LikeMatchMode likeMatchMode) {
			return Restrictions.le(propertyName, value);
		}
	}),

	/**
	 * 
	 */
	ge(new CriterionFactory() {
		public Criterion getInstance(String propertyName, Object value,
				LikeMatchMode likeMatchMode) {
			return Restrictions.ge(propertyName, value);
		}
	});
	// , between, in, isNull, eqProperty,
	// neProperty, ltProperty, leProperty, gtProperty, geProperty, isNotNull,
	// and, or, not, sqlRestriction, Conjunction, Disjunction, allEq, isEmpty,
	// isNotEmpty, sizeEq, sizeNe, sizeGt, sizeLt, sizeGe, sizeLe, naturalId;

	private CriterionFactory criterionFactory;

	Operator(CriterionFactory criterionFactory) {
		this.criterionFactory = criterionFactory;
	}

	public CriterionFactory getCriterionFactory() {
		return criterionFactory;
	}

}
