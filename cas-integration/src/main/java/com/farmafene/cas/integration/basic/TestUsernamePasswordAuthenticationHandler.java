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
package com.farmafene.cas.integration.basic;

import java.security.GeneralSecurityException;

import javax.security.auth.login.FailedLoginException;

import org.jasig.cas.authentication.AuthenticationHandler;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestUsernamePasswordAuthenticationHandler extends
		AbstractUsernamePasswordAuthenticationHandler implements
		AuthenticationHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(TestUsernamePasswordAuthenticationHandler.class);

	public TestUsernamePasswordAuthenticationHandler() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jasig.cas.authentication.AuthenticationHandler#getName()
	 */
	@Override
	public String getName() {
		return this.getClass().getCanonicalName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler#authenticateUsernamePasswordInternal(org.jasig.cas.authentication.UsernamePasswordCredential)
	 */
	@Override
	protected HandlerResult authenticateUsernamePasswordInternal(
			UsernamePasswordCredential cred) throws GeneralSecurityException,
			PreventedException {
		HandlerResult result = null;
		logger.error("Se produce un intento con: {}", cred.getUsername());
		if (!cred.getUsername().equals(cred.getPassword())) {
			throw new FailedLoginException();
		}
		result = createHandlerResult(cred,
				this.principalFactory.createPrincipal(cred.getUsername()), null);
		logger.info("El valor de retorno es: {}", result);
		return result;
	}
}
