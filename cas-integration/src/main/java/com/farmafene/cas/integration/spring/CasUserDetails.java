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

import java.util.Collection;
import java.util.Collections;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
public class CasUserDetails implements UserDetails {

	private String customAttribute;
	private AttributePrincipal attributePrincipal;
	private boolean enabled = false;
	private boolean credentialsNonExpired = true;
	private boolean accountNonLocked = false;
	private boolean accountNonExpired = true;
	private String username;
	private Collection<? extends GrantedAuthority> authorities;

	public CasUserDetails() {
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("={");
		sb.append("username=").append(username);
		sb.append(", customAttribute=").append(customAttribute);
		sb.append(", enabled=").append(enabled);
		sb.append(", authorities=").append(authorities);
		sb.append(", credentialsNonExpired=").append(credentialsNonExpired);
		sb.append(", accountNonLocked=").append(accountNonLocked);
		sb.append(", accountNonExpired=").append(accountNonExpired);
		sb.append(", attributePrincipal=").append(attributePrincipal);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities()
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.unmodifiableCollection(this.authorities);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#getPassword()
	 */
	@Override
	public String getPassword() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#getUsername()
	 */
	@Override
	public String getUsername() {
		return this.username;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
	 */
	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
	 */
	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * @return the customAttribute
	 */
	public String getCustomAttribute() {
		return customAttribute;
	}

	/**
	 * @param customAttribute
	 *            the customAttribute to set
	 */
	void setCustomAttribute(String customAttribute) {
		this.customAttribute = customAttribute;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @param credentialsNonExpired
	 *            the credentialsNonExpired to set
	 */
	void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	/**
	 * @param accountNonLocked
	 *            the accountNonLocked to set
	 */
	void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	/**
	 * @param accountNonExpired
	 *            the accountNonExpired to set
	 */
	void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the attributePrincipal
	 */
	public AttributePrincipal getAttributePrincipal() {
		return attributePrincipal;
	}

	/**
	 * @param attributePrincipal
	 *            the attributePrincipal to set
	 */
	void setAttributePrincipal(AttributePrincipal attributePrincipal) {
		this.attributePrincipal = attributePrincipal;
	}

	/**
	 * @param authorities
	 *            the authorities to set
	 */
	void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}
}
