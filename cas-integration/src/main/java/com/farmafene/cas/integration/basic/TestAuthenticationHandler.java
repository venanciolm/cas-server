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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.FailedLoginException;
import javax.validation.constraints.NotNull;

import org.jasig.cas.authentication.AuthenticationHandler;
import org.jasig.cas.authentication.BasicCredentialMetaData;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.DefaultHandlerResult;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.MessageDescriptor;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.principal.DefaultPrincipalFactory;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.PrincipalFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class TestAuthenticationHandler implements AuthenticationHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(TestAuthenticationHandler.class);

	@NotNull
	@Autowired
	@Qualifier("principalFactory")
	private PrincipalFactory principalFactory = new DefaultPrincipalFactory();
	private IPrincipalInChain chain = new ThreadContextPrincipalInChain();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jasig.cas.authentication.AuthenticationHandler#authenticate(org.jasig.cas.authentication.Credential)
	 */
	@Override
	public HandlerResult authenticate(Credential cred)
			throws GeneralSecurityException, PreventedException {
		HandlerResult result;
		UsernamePasswordCredential credential = (UsernamePasswordCredential) cred;
		if (!credential.getUsername().equals(credential.getPassword())) {
			logger.error("Se produce un intento con: {}",
					credential.getUsername());
			throw new FailedLoginException();
		}
		Map<String, Object> attributes = new LinkedHashMap<String, Object>();
		attributes.put("LOGIN", "USER_PASSWORD");

		result = createHandlerResult(cred,
				this.principalFactory.createPrincipal(credential.getUsername(),
						attributes), null);
		logger.info("El valor de retorno es: {}", result);
		chain.put(result);
		return result;
	}

	private final HandlerResult createHandlerResult(
			final Credential credential, final Principal principal,
			final List<MessageDescriptor> warnings) {
		return new DefaultHandlerResult(this, new BasicCredentialMetaData(
				credential), principal, warnings);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jasig.cas.authentication.AuthenticationHandler#supports(org.jasig.cas.authentication.Credential)
	 */
	@Override
	public boolean supports(Credential credential) {
		boolean supports = UsernamePasswordCredential.class
				.isAssignableFrom(credential.getClass());
		logger.info("supports({}):={}", credential, supports);
		return supports;
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

}
