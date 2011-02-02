package org.ohuyo.common.org.hibernate.criterion;

import java.io.Serializable;

/**
 * 
 * @author rabbit
 * 
 */
public class Page implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -108714648958363801L;
	private long resultTotalNum = 0;
	private int resultOffset = 0;
	private int resultLength = 0;
	private int resultPerPage = 20;
	private int pageTotalNum = 0;
	private int pageOffset = 0;

	public long getResultTotalNum() {
		return resultTotalNum;
	}

	public void setResultTotalNum(long resultTotalNum) {
		this.resultTotalNum = resultTotalNum;
	}

	public int getResultOffset() {
		return resultOffset;
	}

	public void setResultOffset(int resultOffset) {
		this.resultOffset = resultOffset;
	}

	public int getResultLength() {
		return resultLength;
	}

	public void setResultLength(int resultLength) {
		this.resultLength = resultLength;
	}

	public int getResultPerPage() {
		return resultPerPage;
	}

	public void setResultPerPage(int resultPerPage) {
		this.resultPerPage = resultPerPage;
	}

	public int getPageTotalNum() {
		return pageTotalNum;
	}

	public void setPageTotalNum(int pageTotalNum) {
		this.pageTotalNum = pageTotalNum;
	}

	public int getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(int pageOffset) {
		this.pageOffset = pageOffset;
	}

}
