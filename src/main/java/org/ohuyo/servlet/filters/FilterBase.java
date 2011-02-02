/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ohuyo.servlet.filters;

import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.ohuyo.servlet.filters.expire.IntrospectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for filters that provides generic initialisation and a simple
 * no-op destruction.
 * 
 * @author xxd
 * 
 */
public abstract class FilterBase implements Filter {
	private Logger log = LoggerFactory.getLogger(FilterBase.class);

	/**
	 * 自动填充filter参数
	 */
	@SuppressWarnings("unchecked")
	public void init(FilterConfig filterConfig) throws ServletException {
		showInitParameter(filterConfig);
	}

	/**
	 * 显示输入的变量
	 * 
	 * @param filterConfig
	 */
	@SuppressWarnings("unchecked")
	protected void showInitParameter(FilterConfig filterConfig) {
		Enumeration<String> e = filterConfig.getInitParameterNames();
		StringBuilder sb = new StringBuilder();
		boolean b = false;
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String value = filterConfig.getInitParameter(key);
			if (b) {
				sb.append('\n');
			} else {
				b = true;
			}
			sb.append(key + " = " + value);
		}
		log.info("initParameter:\n" + sb.toString());
	}

	/**
	 * 根据变量名取值
	 * 
	 * @param paraName
	 * @param filterConfig
	 * @return
	 */
	protected String getPara(String paraName, FilterConfig filterConfig) {
		String value = filterConfig.getInitParameter(paraName);
		if (StringUtils.isEmpty(value)) {
			throw new IllegalArgumentException(paraName + " can not be empty");
		}
		return value;
	}

	public void destroy() {
		// NOOP
	}

}
