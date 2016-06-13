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

import javax.xml.ws.WebServiceContext;

import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.farmafene.cas.integration.basic.BasicRestClient;
import com.farmafene.cas.integration.clients.ICasServiceTicketFactory;

public class CxfICasServiceTicketFactory implements ICasServiceTicketFactory,
		InitializingBean {

	public String casServerUrlPrefix;
	private WebServiceContext webServiceContext = new WebServiceContextImpl();
	private BasicRestClient client;

	public CxfICasServiceTicketFactory() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(casServerUrlPrefix, "Debe establecerse servidor de CAS");
		this.client = new BasicRestClient();
		client.setCasServerUrlPrefix(casServerUrlPrefix);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.cas.integration.clients.ICasServiceTicketFactory#getServiceToken(java.lang.String)
	 */
	@Override
	public String getServiceToken(String serviceName)
			throws WSSecurityException {
		String ticket = null;
		Principal principal = webServiceContext.getUserPrincipal();
		if (!(principal instanceof CasTokenPrincipal)) {
			WSSecurityException wsse = new WSSecurityException(
					WSSecurityException.ErrorCode.INVALID_SECURITY_TOKEN);
			throw wsse;
		}
		CasTokenPrincipal casPrincipal = (CasTokenPrincipal) principal;
		if (casPrincipal.isProxyGrantingTicket()) {
			ticket = casPrincipal.getAssertion().getPrincipal()
					.getProxyTicketFor(serviceName);
		} else {
			ticket = client.getServiceTicket(casPrincipal.getGrantingTicket(),
					serviceName);
		}
		return ticket;
	}

	/**
	 * @return the casServerUrlPrefix
	 */
	public String getCasServerUrlPrefix() {
		return casServerUrlPrefix;
	}

	/**
	 * @param casServerUrlPrefix
	 *            the casServerUrlPrefix to set
	 */
	public void setCasServerUrlPrefix(String casServerUrlPrefix) {
		this.casServerUrlPrefix = casServerUrlPrefix;
	}

}
