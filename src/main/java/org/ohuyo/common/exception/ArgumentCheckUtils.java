package org.ohuyo.common.exception;

import org.apache.commons.lang.StringUtils;

/**
 * ������鹤�ߡ����ͨ����ʱ���׳��׳�{@link java.lang.IllegalArgumentException
 * IllegalArgumentException}
 * <p>
 * ����ʹ�ó���
 * <li>��������ʱ����У��</li>
 * <li>Spring bean��ʼ��У��</li>
 * <li>��Ĵ��������췽����У��</li>
 * 
 * @author rabbit
 * 
 */
public class ArgumentCheckUtils {

	/**
	 * У�������ΪNULL
	 * 
	 * @param toCheck
	 *            ��ҪУ���ֵ
	 * @param argName
	 *            ������
	 * @exception java.lang.IllegalArgumentException
	 *                ���ΪNULL
	 */
	public static void notNull(Object toCheck, String argName) {
		if (toCheck == null) {
			throw new IllegalArgumentException(argName + " can not be null.");
		}
	}

	/**
	 * У��String������Ϊ�ա�
	 * 
	 * @param toCheck
	 *            ��ҪУ���ֵ
	 * @param argName
	 *            ������
	 * @exception java.lang.IllegalArgumentException
	 *                ���Ϊ��
	 * @see org.apache.commons.lang.StringUtils#isEmpty
	 */
	public static void notBlank(String toCheck, String argName) {
		if (StringUtils.isBlank(toCheck)) {
			throw new IllegalArgumentException(argName + " can not be empty.");
		}
	}

	/**
	 * У����ֵ�����Ƿ�С��ĳֵ��
	 * 
	 * @param toCheck
	 *            У�����ֵ
	 * @param vlaue
	 *            ĳֵ
	 * @param argName
	 *            ������
	 * @exception java.lang.IllegalArgumentException
	 *                �����С��
	 */
	public static void less(long toCheck, long vlaue, String argName) {
		if (toCheck >= vlaue) {
			throw new IllegalArgumentException(argName + " must less " + vlaue
					+ ".");
		}
	}

	/**
	 * У����ֵ�����Ƿ�С����ĳֵ��
	 * 
	 * @param toCheck
	 *            У�����ֵ
	 * @param vlaue
	 *            ĳֵ
	 * @param argName
	 *            ������
	 * @exception java.lang.IllegalArgumentException
	 *                �����С����
	 */
	public static void lessEqual(long toCheck, long vlaue, String argName) {
		if (toCheck > vlaue) {
			throw new IllegalArgumentException(argName + " must lessEqual "
					+ vlaue + ".");
		}
	}

	/**
	 * У����ֵ�����Ƿ����ĳֵ��
	 * 
	 * @param toCheck
	 *            У�����ֵ
	 * @param vlaue
	 *            ĳֵ
	 * @param argName
	 *            ������
	 * @exception java.lang.IllegalArgumentException
	 *                ���������
	 */
	public static void equal(long toCheck, long vlaue, String argName) {
		if (toCheck > vlaue) {
			throw new IllegalArgumentException(argName + " must equal " + vlaue
					+ ".");
		}
	}

	/**
	 * У����ֵ�����Ƿ�����ĳֵ��
	 * 
	 * @param toCheck
	 *            У�����ֵ
	 * @param vlaue
	 *            ĳֵ
	 * @param argName
	 *            ������
	 * @exception java.lang.IllegalArgumentException
	 *                ����������
	 */
	public static void greaterEqual(long toCheck, long vlaue, String argName) {
		if (toCheck < vlaue) {
			throw new IllegalArgumentException(argName + " must greaterEqual "
					+ vlaue + ".");
		}
	}

	/**
	 * У����ֵ�����Ƿ����ĳֵ��
	 * 
	 * @param toCheck
	 *            У�����ֵ
	 * @param vlaue
	 *            ĳֵ
	 * @param argName
	 *            ������
	 * @exception java.lang.IllegalArgumentException
	 *                ���������
	 */
	public static void greater(long toCheck, long vlaue, String argName) {
		if (toCheck <= vlaue) {
			throw new IllegalArgumentException(argName + " must greater "
					+ vlaue + ".");
		}
	}

	/**
	 * У������Ƿ���ĳ����߽ӿڵ�ʵ����
	 * 
	 * @param toCheck
	 *            У�����ֵ
	 * @param clz
	 *            ����߽ӿ�����
	 * @param argName
	 *            ������
	 * @exception java.lang.IllegalArgumentException
	 *                �������ָ��������߽ӿ�����
	 */
	public static void instanceOf(Object toCheck, Class<?> clz, String argName) {
		if (toCheck == null || !clz.isInstance(toCheck)) {
			throw new IllegalArgumentException(argName + " must be instance of"
					+ clz + ".");
		}
	}

}