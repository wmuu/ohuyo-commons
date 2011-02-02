package org.ohuyo.common.org.hibernate.criterion;

import org.hibernate.criterion.Order;

/**
 * 
 * @author rabbit
 * 
 */
public class OrderEx extends Order {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7633914179885909362L;

	public OrderEx(String propertyName, boolean asc) {
		super(propertyName, asc);
	}

	public static Order asc(String propertyName) {
		return new OrderEx(propertyName, true);
	}

	public static Order desc(String propertyName) {
		return new OrderEx(propertyName, false);
	}
}
