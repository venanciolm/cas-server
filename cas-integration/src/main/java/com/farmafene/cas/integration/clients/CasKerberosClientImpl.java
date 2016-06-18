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

import javax.xml.soap.SOAPException;

import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.ws.security.kerberos.KerberosClient;
import org.apache.cxf.ws.security.tokenstore.SecurityToken;
import org.apache.wss4j.dom.message.token.KerberosSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CasKerberosClientImpl extends KerberosClient {

	private static final String FARMAFENE_CAS_ID = "CasServiceTicket";
	private static final String FARMAFENE_TOKEN_TYPE = "uri:farmafene.com#CASTOKEN";
	private ICasServiceTicketFactory serviceTicketFactory;
	private static final Logger logger = LoggerFactory
			.getLogger(CasKerberosClientImpl.class);

	public ICasServiceTicketFactory getServiceTicketFactory() {
		return serviceTicketFactory;
	}

	public void setServiceTicketFactory(
			ICasServiceTicketFactory serviceTicketFactory) {
		this.serviceTicketFactory = serviceTicketFactory;
	}

	public CasKerberosClientImpl() {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.apache.cxf.ws.security.kerberos.KerberosClient#requestSecurityToken()
	 */
	@Override
	public SecurityToken requestSecurityToken() throws Exception {
		SecurityToken token = null;
		if (null != serviceTicketFactory) {
			String ticket = null;
			ticket = serviceTicketFactory.getServiceToken(getServiceName());
			if (null == ticket) {
				SOAPException se = new SOAPException(
						"No ha sido posible recuperar un ticket valido");
				logger.warn("No  hemos generardo un ticket", se);
				throw se;
			}
			KerberosSecurity bst = new KerberosSecurity(
					DOMUtils.createDocument());
			logger.debug("Hemos Recibido {}", ticket);
			bst.addWSUNamespace();
			bst.setID(getTokenId());
			bst.setValueType(FARMAFENE_TOKEN_TYPE);
			bst.setToken(ticket.getBytes());
			token = new SecurityToken(bst.getID());
			token.setToken(bst.getElement());
			token.setWsuId(bst.getID());
			token.setTokenType(bst.getValueType());
			token.setData(bst.getToken());
		} else {
			token = super.requestSecurityToken();
		}
		return token;
	}

	private String getTokenId() {
		return FARMAFENE_CAS_ID;
	}

}
