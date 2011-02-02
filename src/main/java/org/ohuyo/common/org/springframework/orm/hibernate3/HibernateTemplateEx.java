package org.ohuyo.common.org.springframework.orm.hibernate3;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.ohuyo.common.org.hibernate.criterion.DetachedCriteriaEx;
import org.ohuyo.common.org.hibernate.criterion.Page;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * HibernateTemplate扩展<br/>
 * 该类是HibernateTemplate的扩展，提供了一些DAO常用方法的
 * 
 * @author rabbit
 * 
 */
public class HibernateTemplateEx extends HibernateTemplate {

	public HibernateTemplateEx() {
		super();
	}

	public HibernateTemplateEx(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public HibernateTemplateEx(SessionFactory sessionFactory,
			boolean allowCreate) {
		super(sessionFactory, allowCreate);
	}

	/**
	 * 取得满足条件的第一条记录
	 * 
	 * @param criteria
	 * @see org.hibernate.criterion.DetachedCriteria DetachedCriteria
	 * @see #findByCriteria(DetachedCriteria)
	 * @return
	 */
	public Object getByCriteria(DetachedCriteria criteria) {
		List<?> l = findByCriteria(criteria, 0, 1);
		return getFistValue(l);
	}

	/**
	 * 取得满足条件的第一条记录
	 * 
	 * @param hqlQueryString
	 * @return
	 */
	public Object getByHql(String hqlQueryString) {
		List<?> l = findByHql(hqlQueryString, 0, 1);
		return getFistValue(l);
	}

	/**
	 * 取得满足条件的第一条记录
	 * 
	 * @param hqlQueryString
	 * @param value
	 * @return
	 */
	public Object getByHql(String hqlQueryString, Object value) {
		List<?> l = findByHql(hqlQueryString, value, 0, 1);
		return getFistValue(l);
	}

	/**
	 * 取得满足条件的第一条记录
	 * 
	 * @param hqlQueryString
	 * @param values
	 * @return
	 */
	public Object getByHql(String hqlQueryString, Object[] values) {
		List<?> l = findByHql(hqlQueryString, values, 0, 1);
		return getFistValue(l);
	}

	/**
	 * 根据criteria获取记录条数<br/>
	 * criteria对象将被修改
	 * 
	 * @param criteria
	 * @return
	 */
	public long countByCriteria(DetachedCriteria criteria) {
		criteria.setProjection(Projections.rowCount());
		List<?> l = findByCriteria(criteria);
		return getLongValue(l);
	}

	/**
	 * 根据criteriaEx获取记录
	 * 
	 * @param criteriaEx
	 * @return
	 */
	public List<?> findByCriteriaEx(DetachedCriteriaEx criteriaEx) {
		return this.findByCriteria(criteriaEx.getDetachedCriteria());
	}

	/**
	 * 根据criteriaEx获取记录
	 * 
	 * @param criteriaEx
	 * @return
	 */
	public List<?> findByCriteriaEx(DetachedCriteriaEx criteriaEx,
			int firstResult, int maxResults) {
		return this.findByCriteria(criteriaEx.getDetachedCriteria(),
				firstResult, maxResults);
	}

	/**
	 * 根据criteriaEx和page获取记录
	 * 
	 * @param criteriaEx
	 * @return
	 */
	public List<?> findByCriteriaEx(DetachedCriteriaEx criteriaEx, Page page) {
		page.setResultTotalNum(countByCriteriaEx(criteriaEx));
		int pageNum = (int) (page.getResultTotalNum() / page.getResultPerPage());

		if (page.getPageOffset() < 0) {
			page.setPageOffset(0);
		} else if (page.getPageOffset() > pageNum) {
			page.setPageOffset(pageNum);
		}
		page.setResultOffset(page.getResultOffset() * page.getResultPerPage());
		List<?> l = this.findByCriteria(criteriaEx.getDetachedCriteria(), page
				.getResultOffset(), page.getResultPerPage());
		page.setResultLength(l.size());
		return l;
	}

	/**
	 * 根据criteriaEx获取记录数
	 * 
	 * @param criteriaEx
	 * @return
	 */
	public long countByCriteriaEx(DetachedCriteriaEx criteriaEx) {
		return countByCriteria(criteriaEx.getDetachedCriteriaCount());
	}

	// -------------------------------------------------------------------------
	// Convenience finder methods for HQL strings
	// -------------------------------------------------------------------------

	public List<?> findByHql(String hqlQueryString, int firstResult,
			int maxResults) {
		return findByHql(hqlQueryString, (Object[]) null, firstResult,
				maxResults);
	}

	/**
	 * 根据hqlQueryString获取记录
	 * 
	 * @param hqlQueryString
	 * @param value
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	public List<?> findByHql(String hqlQueryString, Object value,
			int firstResult, int maxResults) {
		return findByHql(hqlQueryString, new Object[] { value }, firstResult,
				maxResults);
	}

	/**
	 * 根据hqlQueryString获取记录
	 * 
	 * @param hqlQueryString
	 * @param values
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	public List<?> findByHql(final String hqlQueryString,
			final Object[] values, final int firstResult, final int maxResults) {
		return (List<?>) executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Query queryObject = session.createQuery(hqlQueryString);
				prepareQuery(queryObject);
				if (firstResult >= 0) {
					queryObject.setFirstResult(firstResult);
				}
				if (maxResults > 0) {
					queryObject.setMaxResults(maxResults);
				}
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				return queryObject.list();
			}
		});
	}

	/**
	 * 根据hqlQueryString获取记录数
	 * 
	 * @param hqlQueryString
	 *            普通HQL查询,不需要加上count(*)
	 * @return
	 */
	public long countByHql(String hqlQueryString) {
		return countByHql(hqlQueryString, (Object[]) null);
	}

	/**
	 * 根据hqlQueryString获取记录数
	 * 
	 * @param hqlQueryString
	 *            普通HQL查询,不需要加上count(*)
	 * @param value
	 * @return
	 */
	public long countByHql(String hqlQueryString, Object value) {
		return countByHql(hqlQueryString, new Object[] { value });
	}

	/**
	 * 根据hqlQueryString获取记录数
	 * 
	 * @param hqlQueryString
	 *            普通HQL查询,不需要加上count(*)
	 * @param values
	 * @return
	 */
	public long countByHql(String hqlQueryString, final Object[] values) {
		hqlQueryString = "select count(*) " + hqlQueryString;
		List<?> l = find(hqlQueryString, values);
		return getLongValue(l);
	}

	// -------------------------------------------------------------------------
	// Convenience finder methods for SQL strings
	// -------------------------------------------------------------------------

	/**
	 * 取得满足条件的第一条记录
	 * 
	 * @param sqlQueryString
	 * @return
	 */
	public Object getBySql(String sqlQueryString) {
		List<?> l = findBySql(sqlQueryString, 0, 1);
		return getFistValue(l);
	}

	public Object getBySql(String sqlQueryString, Object... args) {
		List<?> l = findBySql(sqlQueryString, args, 0, 1);
		return getFistValue(l);
	}

	public List<?> findBySql(String sqlQueryString) {
		return findBySql(sqlQueryString, (Object[]) null);
	}

	/**
	 * 根据sqlQueryString获取记录
	 * 
	 * @param sqlQueryString
	 * @param value
	 * @return
	 */
	public List<?> findBySql(String sqlQueryString, Object value) {
		return findBySql(sqlQueryString, new Object[] { value });
	}

	/**
	 * 根据sqlQueryString获取记录
	 * 
	 * @param sqlQueryString
	 * @param values
	 * @return
	 */
	public List<?> findBySql(final String sqlQueryString, final Object[] values) {
		return findBySql(sqlQueryString, values, -1, -1);
	}

	/**
	 * 根据sqlQueryString获取记录
	 * 
	 * @param sqlQueryString
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	public List<?> findBySql(String sqlQueryString, int firstResult,
			int maxResults) {
		return findBySql(sqlQueryString, (Object[]) null, firstResult,
				maxResults);
	}

	/**
	 * 根据sqlQueryString获取记录
	 * 
	 * @param sqlQueryString
	 * @param value
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	public List<?> findBySql(String sqlQueryString, Object value,
			int firstResult, int maxResults) {
		return findBySql(sqlQueryString, new Object[] { value }, firstResult,
				maxResults);
	}

	/**
	 * 根据sqlQueryString获取记录
	 * 
	 * @param sqlQueryString
	 * @param values
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	public List<?> findBySql(final String sqlQueryString,
			final Object[] values, final int firstResult, final int maxResults) {
		return (List<?>) executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				SQLQuery queryObject = session.createSQLQuery(sqlQueryString);
				prepareQuery(queryObject);
				if (firstResult >= 0) {
					queryObject.setFirstResult(firstResult);
				}
				if (maxResults > 0) {
					queryObject.setMaxResults(maxResults);
				}
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				return queryObject.list();
			}
		});
	}

	/**
	 * 根据sqlQueryString获取记录数
	 * 
	 * @param sqlQueryString
	 *            普通SQL查询,不需要加上count(*)
	 * @param value
	 * @return
	 */
	public long countBySql(String sqlQueryString) {
		return countBySql(sqlQueryString, (Object[]) null);
	}

	/**
	 * 根据sqlQueryString获取记录数
	 * 
	 * @param sqlQueryString
	 *            普通SQL查询,不需要加上count(*)
	 * @param value
	 * @return
	 */
	public long countBySql(String sqlQueryString, Object value) {
		return countBySql(sqlQueryString, new Object[] { value });
	}

	/**
	 * 根据sqlQueryString获取记录数
	 * 
	 * @param sqlQueryString
	 *            普通SQL查询,不需要加上count(*)
	 * @param values
	 * @return
	 */
	public long countBySql(String sqlQueryString, Object[] values) {
		sqlQueryString = "select count(*) from (" + sqlQueryString + " )";
		List<?> l = findBySql(sqlQueryString, values);
		return getLongValue(l);
	}

	/**
	 * 执行非查询的hql语句
	 * 
	 * @param hqlQueryString
	 * @return
	 */
	public int executeUpdateByHql(String hqlQueryString) {
		return executeUpdateByHql(hqlQueryString, (Object[]) null);
	}

	/**
	 * 执行非查询的hql语句
	 * 
	 * @param hqlQueryString
	 * @param value
	 * @return
	 */
	public int executeUpdateByHql(String hqlQueryString, Object value) {
		return executeUpdateByHql(hqlQueryString, new Object[] { value });
	}

	/**
	 * 执行非查询的hql语句
	 * 
	 * @param hqlQueryString
	 * @param values
	 * @return
	 */
	public int executeUpdateByHql(final String hqlQueryString,
			final Object[] values) {
		return (Integer) executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Query queryObject = session.createQuery(hqlQueryString);
				prepareQuery(queryObject);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				return queryObject.executeUpdate();
			}
		});
	}

	/**
	 * 执行非查询的sql语句
	 * 
	 * @param sqlQueryString
	 * @return
	 */
	public int executeUpdateBySql(String sqlQueryString) {
		return executeUpdateBySql(sqlQueryString, (Object[]) null);
	}

	/**
	 * 执行非查询的sql语句
	 * 
	 * @param sqlQueryString
	 * @param args
	 * @return 执行影响的记录或者表的个数
	 */
	public int executeUpdateBySql(final String sqlQueryString,
			final Object... args) {
		return (Integer) executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Query queryObject = session.createSQLQuery(sqlQueryString);
				prepareQuery(queryObject);
				if (args != null) {
					for (int i = 0; i < args.length; i++) {
						queryObject.setParameter(i, args[i]);
					}
				}
				return queryObject.executeUpdate();
			}
		});
	}

	private long getLongValue(List<?> l) {
		return (l.isEmpty() ? 0 : (Long) l.get(0));
	}

	private Object getFistValue(List<?> l) {
		return (l.isEmpty() ? null : l.get(0));
	}

}
