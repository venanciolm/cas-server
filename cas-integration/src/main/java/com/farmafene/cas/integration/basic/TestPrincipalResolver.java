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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.principal.DefaultPrincipalFactory;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.PrincipalFactory;
import org.jasig.cas.authentication.principal.PrincipalResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class TestPrincipalResolver implements PrincipalResolver {

	private static final Logger logger = LoggerFactory
			.getLogger(TestPrincipalResolver.class);
	@NotNull
	@Autowired
	@Qualifier("principalFactory")
	private PrincipalFactory principalFactory = new DefaultPrincipalFactory();
	private IPrincipalInChain chain = new ThreadContextPrincipalInChain();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jasig.cas.authentication.principal.PrincipalResolver#resolve(org.jasig.cas.authentication.Credential)
	 */
	@Override
	public Principal resolve(Credential credential) {
		UsernamePasswordCredential cred = (UsernamePasswordCredential) credential;
		HandlerResult result = chain.get(cred.getUsername());
		Map<String, Object> attributes = new LinkedHashMap<String, Object>();
		if (null != result) {
			logger.warn("vienen: {}", result);
			logger.warn("vienen: {}", result.getPrincipal());
			logger.warn("vienen: {}", result.getPrincipal().getAttributes());
			attributes.putAll(result.getPrincipal().getAttributes());
		}
		logger.warn("Tenemos: {}", cred.getUsername());
		logger.warn("Tenemos: {}", attributes);
		if (!attributes.containsKey("memberOf")) {
			logger.warn("Populando 'memberOf'");
			List<Object> values = new ArrayList<Object>();
			values.add("ROLE_01");
			values.add("ROLE_02");
			StringBuilder sb = new StringBuilder();
			for (Object a : values) {
				if (sb.length() > 0) {
					sb.append(",");
				}
				sb.append(a);
			}
			attributes.put("memberOf", values.toArray(new Object[0]));
		}
		logger.warn("Retornando: {}", attributes);
		String id = cred.getUsername();
		return principalFactory.createPrincipal(id, attributes);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jasig.cas.authentication.principal.PrincipalResolver#supports(org.jasig.cas.authentication.Credential)
	 */
	@Override
	public boolean supports(Credential credential) {
		return UsernamePasswordCredential.class.isAssignableFrom(credential
				.getClass());
	}

}
