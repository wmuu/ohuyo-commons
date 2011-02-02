package org.springframework.samples.petclinic.web;

import javax.servlet.http.HttpServletRequest;

public interface ETagTokenFactory {
	String getToken(HttpServletRequest request);
}
