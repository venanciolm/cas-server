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
import org.apache.wss4j.dom.validate.Credential;
import org.apache.wss4j.dom.validate.Validator;
import org.jasig.cas.client.proxy.ProxyGrantingTicketStorage;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ProxyTicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class Cas20BinaryTokenValidator implements InitializingBean, Validator {

	private static final Logger logger = LoggerFactory
			.getLogger(Cas20BinaryTokenValidator.class);
	private String service;
	private String casServerUrlPrefix;
	private String proxyCallbackUrl;
	private ProxyGrantingTicketStorage proxyGrantingTicketStorage;

	public Cas20BinaryTokenValidator() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(service, "Debe establecerse el servicio");
		Assert.notNull(casServerUrlPrefix, "Debe establecerse servicio de CAS");
		if (null != proxyCallbackUrl) {
			Assert.notNull(proxyGrantingTicketStorage);
		}
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

		String ticket = new String(credential.getBinarySecurityToken()
				.getToken());
		logger.info("==========================================================================");
		logger.info("Estamos Validando el token  {}", ticket);
		logger.info("==========================================================================");
		Assertion assertion = null;
		final CasTokenPrincipal principal = new CasTokenPrincipal();
		try {
			Cas20ProxyTicketValidator ticketValidator = new Cas20ProxyTicketValidator(
					casServerUrlPrefix);
			if (null != proxyCallbackUrl) {
				ProxyGrantingTicketStorageDelegate proxyGrantingTicketStorageWrapper = new ProxyGrantingTicketStorageDelegate();
				proxyGrantingTicketStorageWrapper.setPrincipal(principal);
				proxyGrantingTicketStorageWrapper
						.setProxyGrantingTicketStorage(proxyGrantingTicketStorage);
				ticketValidator.setProxyCallbackUrl(proxyCallbackUrl);
				ticketValidator
						.setProxyGrantingTicketStorage(proxyGrantingTicketStorageWrapper);
				ticketValidator.setAcceptAnyProxy(true);
			}
			assertion = ticketValidator.validate(ticket, service);
		} catch (TicketValidationException e) {
			WSSecurityException wsse = new WSSecurityException(
					WSSecurityException.ErrorCode.FAILED_AUTHENTICATION);
			wsse.initCause(e);
			throw wsse;
		}
		principal.setCasServerUrlPrefix(casServerUrlPrefix);
		principal.setAssertion(assertion);
		principal.setTokenElement(credential.getBinarySecurityToken()
				.getElement());
		credential.setPrincipal(principal);
		return credential;
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
	 * @return the proxyCallbackUrl
	 */
	public String getProxyCallbackUrl() {
		return proxyCallbackUrl;
	}

	/**
	 * @param proxyCallbackUrl
	 *            the proxyCallbackUrl to set
	 */
	public void setProxyCallbackUrl(String proxyCallbackUrl) {
		this.proxyCallbackUrl = proxyCallbackUrl;
	}

	/**
	 * @return the proxyGrantingTicketStorage
	 */
	public ProxyGrantingTicketStorage getProxyGrantingTicketStorage() {
		return proxyGrantingTicketStorage;
	}

	/**
	 * @param proxyGrantingTicketStorage
	 *            the proxyGrantingTicketStorage to set
	 */
	public void setProxyGrantingTicketStorage(
			ProxyGrantingTicketStorage proxyGrantingTicketStorage) {
		this.proxyGrantingTicketStorage = proxyGrantingTicketStorage;
	}
}
