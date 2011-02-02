package org.ohuyo.common.exception;

import org.apache.commons.lang.StringUtils;

/**
 * 参数检查工具。检查通不过时，抛出抛出{@link java.lang.IllegalArgumentException
 * IllegalArgumentException}
 * <p>
 * 建议使用场合
 * <li>方法调用时参数校验</li>
 * <li>Spring bean初始化校验</li>
 * <li>类的带参数构造方法的校验</li>
 * 
 * @author rabbit
 * 
 */
public class ArgumentCheckUtils {

	/**
	 * 校验参数不为NULL
	 * 
	 * @param toCheck
	 *            需要校验的值
	 * @param argName
	 *            参数名
	 * @exception java.lang.IllegalArgumentException
	 *                如果为NULL
	 */
	public static void notNull(Object toCheck, String argName) {
		if (toCheck == null) {
			throw new IllegalArgumentException(argName + " can not be null.");
		}
	}

	/**
	 * 校验String参数不为空。
	 * 
	 * @param toCheck
	 *            需要校验的值
	 * @param argName
	 *            参数名
	 * @exception java.lang.IllegalArgumentException
	 *                如果为空
	 * @see org.apache.commons.lang.StringUtils#isEmpty
	 */
	public static void notBlank(String toCheck, String argName) {
		if (StringUtils.isBlank(toCheck)) {
			throw new IllegalArgumentException(argName + " can not be empty.");
		}
	}

	/**
	 * 校验数值参数是否小于某值。
	 * 
	 * @param toCheck
	 *            校验的数值
	 * @param vlaue
	 *            某值
	 * @param argName
	 *            参数名
	 * @exception java.lang.IllegalArgumentException
	 *                如果不小于
	 */
	public static void less(long toCheck, long vlaue, String argName) {
		if (toCheck >= vlaue) {
			throw new IllegalArgumentException(argName + " must less " + vlaue
					+ ".");
		}
	}

	/**
	 * 校验数值参数是否小等于某值。
	 * 
	 * @param toCheck
	 *            校验的数值
	 * @param vlaue
	 *            某值
	 * @param argName
	 *            参数名
	 * @exception java.lang.IllegalArgumentException
	 *                如果不小等于
	 */
	public static void lessEqual(long toCheck, long vlaue, String argName) {
		if (toCheck > vlaue) {
			throw new IllegalArgumentException(argName + " must lessEqual "
					+ vlaue + ".");
		}
	}

	/**
	 * 校验数值参数是否等于某值。
	 * 
	 * @param toCheck
	 *            校验的数值
	 * @param vlaue
	 *            某值
	 * @param argName
	 *            参数名
	 * @exception java.lang.IllegalArgumentException
	 *                如果不等于
	 */
	public static void equal(long toCheck, long vlaue, String argName) {
		if (toCheck > vlaue) {
			throw new IllegalArgumentException(argName + " must equal " + vlaue
					+ ".");
		}
	}

	/**
	 * 校验数值参数是否大等于某值。
	 * 
	 * @param toCheck
	 *            校验的数值
	 * @param vlaue
	 *            某值
	 * @param argName
	 *            参数名
	 * @exception java.lang.IllegalArgumentException
	 *                如果不大等于
	 */
	public static void greaterEqual(long toCheck, long vlaue, String argName) {
		if (toCheck < vlaue) {
			throw new IllegalArgumentException(argName + " must greaterEqual "
					+ vlaue + ".");
		}
	}

	/**
	 * 校验数值参数是否大于某值。
	 * 
	 * @param toCheck
	 *            校验的数值
	 * @param vlaue
	 *            某值
	 * @param argName
	 *            参数名
	 * @exception java.lang.IllegalArgumentException
	 *                如果不大于
	 */
	public static void greater(long toCheck, long vlaue, String argName) {
		if (toCheck <= vlaue) {
			throw new IllegalArgumentException(argName + " must greater "
					+ vlaue + ".");
		}
	}

	/**
	 * 校验参数是否是某类或者接口的实例。
	 * 
	 * @param toCheck
	 *            校验的数值
	 * @param clz
	 *            类或者接口类型
	 * @param argName
	 *            参数名
	 * @exception java.lang.IllegalArgumentException
	 *                如果不是指定的类或者接口类型
	 */
	public static void instanceOf(Object toCheck, Class<?> clz, String argName) {
		if (toCheck == null || !clz.isInstance(toCheck)) {
			throw new IllegalArgumentException(argName + " must be instance of"
					+ clz + ".");
		}
	}

}