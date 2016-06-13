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
package com.farmafene.cas.integration.clients;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.common.ext.WSSecurityException;

public class CxfCasTokenInjectInterceptor extends WSS4JOutInterceptor {

	private static final String BASE64_BINARY_ENCODING = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary";
	private static final String XSD_WSSE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
	private static final String XMLNS_WSU = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wsswssecurity-utility-1.0.xsd";

	private String serviceName;
	private ICasServiceTicketFactory serviceTicketFactory;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor#handleMessage(org.apache.cxf.binding.soap.SoapMessage)
	 */
	@Override
	public void handleMessage(SoapMessage message) throws Fault {
		@SuppressWarnings("unchecked")
		List<Header> headers = (List<Header>) message.get(Header.HEADER_LIST);
		try {
			headers.addAll(addSecurityHeader());
			message.put(Header.HEADER_LIST, headers);
		} catch (SOAPException e) {
			throw new Fault(e);
		} catch (Exception e) {
			throw new Fault(e);
		}
	}

	private List<Header> addSecurityHeader() throws SOAPException,
			WSSecurityException {
		String ticket = null;
		ticket = serviceTicketFactory.getServiceToken(serviceName);
		if (null == ticket) {
			throw new SOAPException(
					"No ha sido posible recuperar un ticket valido");
		}
		final List<Header> headers = new ArrayList<Header>();
		final SOAPFactory sf = SOAPFactory.newInstance();
		final SOAPElement securityElement = sf.createElement("Security",
				"wsse", XSD_WSSE);
		final SOAPElement authElement = sf.createElement("BinarySecurityToken",
				"wsse", XSD_WSSE);
		authElement.setAttribute("ValueType", "uri:farmafene.com#CASTOKEN");
		authElement.setAttribute("EncodingType", BASE64_BINARY_ENCODING);
		authElement.setAttribute("wsu:Id", "CasServiceTicket");
		authElement.addAttribute(new QName("xmlns:wsu"), XMLNS_WSU);
		authElement.addTextNode(Base64Utility.encode(ticket.getBytes()));
		securityElement.addChildElement(authElement);
		headers.add(new SoapHeader(new QName(null, "Security"), securityElement));
		return headers;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param serviceName
	 *            the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * @return the serviceTicketFactory
	 */
	public ICasServiceTicketFactory getServiceTicketFactory() {
		return serviceTicketFactory;
	}

	/**
	 * @param serviceTicketFactory
	 *            the serviceTicketFactory to set
	 */
	public void setServiceTicketFactory(
			ICasServiceTicketFactory serviceTicketFactory) {
		this.serviceTicketFactory = serviceTicketFactory;
	}
}