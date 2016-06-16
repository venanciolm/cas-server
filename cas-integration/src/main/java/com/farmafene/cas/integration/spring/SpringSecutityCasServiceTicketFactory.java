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
package com.farmafene.cas.integration.spring;

import org.apache.wss4j.common.ext.WSSecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.farmafene.cas.integration.clients.ICasServiceTicketFactory;

public class SpringSecutityCasServiceTicketFactory implements
		ICasServiceTicketFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(SpringSecutityCasServiceTicketFactory.class);

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.cas.integration.clients.ICasServiceTicketFactory#getServiceToken(java.lang.String)
	 */
	@Override
	public String getServiceToken(String serviceName)
			throws WSSecurityException {
		String proxyTicket = null;
		if (null == serviceName) {
			throw new NullPointerException("Service must be not null!");
		}
		if (null != SecurityContextHolder.getContext()
				&& null != SecurityContextHolder.getContext()
						.getAuthentication()
				&& SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal() instanceof CasUserDetails) {
			CasUserDetails details = (CasUserDetails) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();
			proxyTicket = details.getAttributePrincipal().getProxyTicketFor(
					serviceName);
		} else {
			logger.error("No se ha podido localizar el principal");
		}
		return proxyTicket;
	}
}
