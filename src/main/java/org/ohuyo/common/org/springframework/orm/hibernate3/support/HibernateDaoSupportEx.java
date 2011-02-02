package org.ohuyo.common.org.springframework.orm.hibernate3.support;

import org.hibernate.SessionFactory;
import org.ohuyo.common.exception.ArgumentCheckUtils;
import org.ohuyo.common.org.springframework.orm.hibernate3.HibernateTemplateEx;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * HibernateDaoSupport功能扩展类
 * 
 * @author rabbit
 * 
 */
public abstract class HibernateDaoSupportEx extends HibernateDaoSupport {
	/**
	 * 
	 */
	@Override
	protected HibernateTemplate createHibernateTemplate(
			SessionFactory sessionFactory) {
		return new HibernateTemplateEx(sessionFactory);
	}

	/**
	 * initDao
	 */
	@Override
	protected void initDao() throws Exception {
		super.initDao();
		HibernateTemplate ht = getHibernateTemplate();
		ArgumentCheckUtils.instanceOf(ht, HibernateTemplateEx.class,
				"hibernateTemplate");
	}

	public HibernateTemplateEx getHibernateTemplateEx() {
		return (HibernateTemplateEx) getHibernateTemplate();
	}

}
