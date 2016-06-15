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
package com.farmafene.cas.integration.spring;

import java.util.ArrayList;
import java.util.Collection;

import org.jasig.cas.client.validation.Assertion;
import org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CasUserDetailsService extends
		AbstractCasAssertionUserDetailsService {

	private static final String MEMBER_OF = "memberOf";
	private static final String CUSTOM_ATTRIBUTE = "customAttribute";

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService#loadUserDetails(org.jasig.cas.client.validation.Assertion)
	 */
	@Override
	protected UserDetails loadUserDetails(Assertion assertion) {
		CasUserDetails user = new CasUserDetails();
		user.setAttributePrincipal(assertion.getPrincipal());
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setUsername(user.getAttributePrincipal().getName());
		user.setCustomAttribute((String) user.getAttributePrincipal()
				.getAttributes().get(CUSTOM_ATTRIBUTE));
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		user.setAuthorities(authorities);
		String roles = (String) user.getAttributePrincipal().getAttributes()
				.get(MEMBER_OF);
		if (roles != null) {
			String[] r = roles.split(",");
			for (String i : r) {
				authorities.add(new SimpleGrantedAuthority(i));
			}
		}
		return user;
	}
}
