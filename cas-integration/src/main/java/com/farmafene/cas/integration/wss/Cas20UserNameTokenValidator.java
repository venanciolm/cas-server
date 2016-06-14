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

import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.dom.handler.RequestData;
import org.apache.wss4j.dom.message.token.UsernameToken;
import org.apache.wss4j.dom.validate.Credential;
import org.apache.wss4j.dom.validate.Validator;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.farmafene.cas.integration.basic.BasicRestClient;

public class Cas20UserNameTokenValidator implements InitializingBean, Validator {
	private static final Logger logger = LoggerFactory
			.getLogger(Cas20UserNameTokenValidator.class);
	private BasicRestClient client;
	private String service;
	private String casServerUrlPrefix;

	public Cas20UserNameTokenValidator() {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("={");
		sb.append("service=").append(service);
		sb.append(", casServerUrlPrefix=").append(casServerUrlPrefix);
		sb.append(", client=").append(client);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(service, "Debe establecerse el servicio");
		Assert.notNull(casServerUrlPrefix, "Debe establecerse servidor de CAS");
		if (client == null) {
			client = new BasicRestClient();
			client.setCasServerUrlPrefix(casServerUrlPrefix);
		}
		client.setCasServerUrlPrefix(casServerUrlPrefix);
		logger.debug("Tenemos establecido: {}", this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.apache.wss4j.dom.validate.Validator#validate(org.apache.wss4j.dom.validate.Credential,
	 *      org.apache.wss4j.dom.handler.RequestData)
	 */
	@Override
	public Credential validate(Credential credential, RequestData data)
			throws WSSecurityException {
		Assertion assertion = null;
		CasTokenPrincipal principal = new CasTokenPrincipal();
		principal.setProxyGrantingTicket(false);
		String ticket = getTicket(principal, credential.getUsernametoken());
		try {
			Cas20ServiceTicketValidator ticketValidator = new Cas20ServiceTicketValidator(
					casServerUrlPrefix);
			assertion = ticketValidator.validate(ticket, service);
		} catch (TicketValidationException e) {
			WSSecurityException wsse = new WSSecurityException(
					WSSecurityException.ErrorCode.FAILED_AUTHENTICATION);
			wsse.initCause(e);
			throw wsse;
		}
		if (logger.isDebugEnabled()) {
			for (Object a : assertion.getPrincipal().getAttributes().keySet()) {
				logger.debug("{}:= {}", a, assertion.getPrincipal()
						.getAttributes().get(a));
			}
		}
		principal.setAssertion(assertion);
		principal.setCasServerUrlPrefix(casServerUrlPrefix);
		principal.setTokenElement(credential.getUsernametoken().getElement());
		credential.setPrincipal(principal);
		return credential;
	}

	private String getTicket(CasTokenPrincipal principal,
			UsernameToken usernameToken) throws WSSecurityException {
		String user = usernameToken.getName();
		String password = usernameToken.getPassword();
		String tgt = null;
		try {
			tgt = client.getTicketGrantingTicket(user, password);
		} catch (IllegalStateException e) {
			WSSecurityException wsse = new WSSecurityException(
					WSSecurityException.ErrorCode.FAILED_AUTHENTICATION);
			wsse.initCause(e);
			throw wsse;
		}
		if (null == tgt) {
			WSSecurityException wsse = new WSSecurityException(
					WSSecurityException.ErrorCode.FAILED_AUTHENTICATION);
			throw wsse;
		}
		String ticket = null;
		principal.setGrantingTicket(tgt);
		try {
			ticket = client.getServiceTicket(tgt, service);
		} catch (IllegalStateException e) {
			WSSecurityException wsse = new WSSecurityException(
					WSSecurityException.ErrorCode.FAILURE);
			wsse.initCause(e);
			throw wsse;
		}
		if (null == ticket) {
			WSSecurityException wsse = new WSSecurityException(
					WSSecurityException.ErrorCode.FAILURE);
			throw wsse;
		}
		return ticket;
	}

	/**
	 * @return the client
	 */
	public BasicRestClient getClient() {
		return client;
	}

	/**
	 * @param client
	 *            the client to set
	 */
	public void setClient(BasicRestClient client) {
		this.client = client;
	}

	/**
	 * @return the service
	 */
	public String getService() {
		return service;
	}

	/**
	 * @param service
	 *            the service to set
	 */
	public void setService(String service) {
		this.service = service;
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
