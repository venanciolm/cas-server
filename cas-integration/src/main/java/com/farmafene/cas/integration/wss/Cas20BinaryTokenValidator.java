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
import org.jasig.cas.client.proxy.ProxyRetriever;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class Cas20BinaryTokenValidator implements InitializingBean, Validator {
	private String service;
	private String casServerUrlPrefix;
	private String proxyCallbackUrl;
	private ProxyRetriever proxyRetriever;

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
			Assert.notNull(proxyRetriever,
					"Si se establece un callback, debe establecerse un retriever");
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
		Assertion assertion = null;
		final CasTokenPrincipal principal = new CasTokenPrincipal();
		try {
			Cas20ServiceTicketValidator ticketValidator = new Cas20ServiceTicketValidator(
					casServerUrlPrefix);
			if (null != proxyCallbackUrl) {
				ProxyGrantingTicketStorage proxyGrantingTicketStorage = new ProxyGrantingTicketStorage() {

					/**
					 * {@inheritDoc}
					 * 
					 * @see org.jasig.cas.client.proxy.ProxyGrantingTicketStorage#save(java.lang.String,
					 *      java.lang.String)
					 */
					@Override
					public void save(String proxyGrantingTicketIou,
							String proxyGrantingTicket) {
					}

					/**
					 * {@inheritDoc}
					 * 
					 * @see org.jasig.cas.client.proxy.ProxyGrantingTicketStorage#retrieve(java.lang.String)
					 */
					@Override
					public String retrieve(String proxyGrantingTicketIou) {
						String grantingTicket = retrieve(proxyGrantingTicketIou);
						principal.setGrantingTicket(grantingTicket);
						return grantingTicket;
					}

					/**
					 * {@inheritDoc}
					 * 
					 * @see org.jasig.cas.client.proxy.ProxyGrantingTicketStorage#cleanUp()
					 */
					@Override
					public void cleanUp() {
					}
				};
				ticketValidator.setProxyCallbackUrl(proxyCallbackUrl);
				ticketValidator
						.setProxyGrantingTicketStorage(proxyGrantingTicketStorage);
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
	 * @return the proxyRetriever
	 */
	public ProxyRetriever getProxyRetriever() {
		return proxyRetriever;
	}

	/**
	 * @param proxyRetriever
	 *            the proxyRetriever to set
	 */
	public void setProxyRetriever(ProxyRetriever proxyRetriever) {
		this.proxyRetriever = proxyRetriever;
	}

}
