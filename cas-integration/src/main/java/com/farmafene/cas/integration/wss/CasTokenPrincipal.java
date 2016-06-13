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

import java.io.Serializable;
import java.security.Principal;

import org.jasig.cas.client.validation.Assertion;
import org.w3c.dom.Element;

@SuppressWarnings("serial")
public class CasTokenPrincipal implements Principal, Serializable {

	private Assertion assertion;
	private Element tokenElement;
	private String grantingTicket;
	private boolean proxyGrantingTicket = true;
	private String casServerUrlPrefix;

	public CasTokenPrincipal() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.security.Principal#getName()
	 */
	@Override
	public String getName() {
		return assertion.getPrincipal().getName();
	}

	public String getGrantingTicket() {
		return grantingTicket;
	}

	public void setGrantingTicket(String grantingTicket) {
		this.grantingTicket = grantingTicket;
	}

	public Element getTokenElement() {
		return tokenElement;
	}

	public void setTokenElement(Element tokenElement) {
		this.tokenElement = tokenElement;
	}

	/**
	 * @param assertion
	 *            the assertion to set
	 */
	public void setAssertion(Assertion assertion) {
		this.assertion = assertion;
	}

	/**
	 * @return the assertion
	 */
	public Assertion getAssertion() {
		return assertion;
	}

	/**
	 * @return the proxyGrantingTicket
	 */
	public boolean isProxyGrantingTicket() {
		return proxyGrantingTicket;
	}

	/**
	 * @param proxyGrantingTicket
	 *            the proxyGrantingTicket to set
	 */
	public void setProxyGrantingTicket(boolean proxyGrantingTicket) {
		this.proxyGrantingTicket = proxyGrantingTicket;
	}

	/**
	 * @return the casServerUrlPrefix
	 */
	public String getCasServerUrlPrefix() {
		return casServerUrlPrefix;
	}

	/**
	 * @param casServerUrlPrefix the casServerUrlPrefix to set
	 */
	public void setCasServerUrlPrefix(String casServerUrlPrefix) {
		this.casServerUrlPrefix = casServerUrlPrefix;
	}
}