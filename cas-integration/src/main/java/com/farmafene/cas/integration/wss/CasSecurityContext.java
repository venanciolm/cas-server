/*
 * Copyright (c) 2009-2016 farmafene.com
 * All rights reserved.
 * 
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 * 
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 * 
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.farmafene.cas.integration.wss;

import java.security.Principal;
import java.util.LinkedHashSet;

import org.apache.cxf.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.cas.integration.basic.BasicRestClient;

public class CasSecurityContext implements SecurityContext {

	private static final Logger logger = LoggerFactory
			.getLogger(CasSecurityContext.class);
	private static final String CUSTOM_ATTRIBUTE = "customAttribute";
	private CasTokenPrincipal principal;
	private LinkedHashSet<String> roles;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.apache.cxf.security.SecurityContext#getUserPrincipal()
	 */
	@Override
	public Principal getUserPrincipal() {
		return principal;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.apache.cxf.security.SecurityContext#isUserInRole(java.lang.String)
	 */
	@Override
	public boolean isUserInRole(String role) {
		if (null == roles) {
			roles = new LinkedHashSet<String>();
			loadRoles(roles);
		}
		return roles.contains(role);
	}

	private void loadRoles(LinkedHashSet<String> roles2) {
		// TODO Auto-generated method stub

	}

	public void logout() {
		if (principal.isProxyGrantingTicket()) {
			logger.info("El ProxyGrantingTicket es {}",
					principal.getGrantingTicket());
		} else {
			logger.info("El TicketGrantingTicket  es {}",
					principal.getGrantingTicket());
		}
		BasicRestClient client = new BasicRestClient();
		client.setCasServerUrlPrefix(principal.getCasServerUrlPrefix());
		logger.info("La url es: {}", principal.getCasServerUrlPrefix());
		client.logout(principal.getGrantingTicket());
	}

	public String getCustomAttribute() {
		return (String) principal.getAssertion().getAttributes()
				.get(CUSTOM_ATTRIBUTE);
	}

	public void setCasTokenPrincipal(CasTokenPrincipal ctp) {
		this.principal = ctp;
	}
}
