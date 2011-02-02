package org.ohuyo.common.org.hibernate.criterion;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.ohuyo.common.org.hibernate.criterion.annotation.Expression;
import org.ohuyo.common.org.hibernate.criterion.annotation.LikeMatchMode;
import org.ohuyo.common.org.hibernate.criterion.annotation.Operator;

/**
 * ¿Îœﬂ≤È—Ø¿©’π
 * 
 * @author rabbit
 * 
 */
public abstract class DetachedCriteriaEx implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5743507820545880711L;
	/**
	 * ≈≈–Ú
	 */
	private OrderEx[] orderExs;

	public final OrderEx[] getOrderExs() {
		return orderExs;
	}

	public final DetachedCriteria getDetachedCriteria() {
		DetachedCriteria c = getBaseDetachedCriteria();
		if (orderExs != null) {
			for (OrderEx o : orderExs) {
				c.addOrder(o);
			}
		}
		return c;
	}

	public final DetachedCriteria getDetachedCriteriaCount() {
		DetachedCriteria c = getBaseDetachedCriteria();
		c.setProjection(Projections.rowCount());
		return c;
	}

	protected DetachedCriteria getBaseDetachedCriteria() {
		Class clazz = null;
		DetachedCriteria dc = DetachedCriteria.forClass(clazz);

		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			try {
				Criterion cr = genCriterion(field);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return dc;
	}

	private Criterion genCriterion(final Field field)
			throws IllegalArgumentException, IllegalAccessException,
			SecurityException, NoSuchMethodException, InvocationTargetException {
		Expression ep = field.getAnnotation(Expression.class);
		if (ep == null) {
			return null;
		}
		Object value = null;
		if (field.isAccessible()) {
			value = field.get(this);
		} else {
			Class<?> clz = field.getDeclaringClass();
			String name = field.getName();
			name = "get" + name.substring(0, 1).toUpperCase()
					+ name.substring(1);
			Method method = clz.getDeclaredMethod(name, (Class[]) null);
			value = method.invoke(this, (Object[]) null);
		}
		if (value == null) {
			return null;
		}
		if (value instanceof String) {
			if (StringUtils.isBlank((String) value)) {
				return null;
			}
		}
		Operator op = ep.operator();
		String propertyName = ep.propertyName();
		LikeMatchMode likeMatchMode = ep.likeMatchMode();
		return op.getCriterionFactory().getInstance(propertyName, value,
				likeMatchMode);
	}

	public final void setOrderExs(OrderEx[] orderExs) {
		this.orderExs = orderExs;
	}

}
