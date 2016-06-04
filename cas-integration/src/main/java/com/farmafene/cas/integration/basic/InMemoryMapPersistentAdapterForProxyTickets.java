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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.farmafene.cas.integration.IPersistentAdapterForProxyTickets;

public class InMemoryMapPersistentAdapterForProxyTickets implements
		IPersistentAdapterForProxyTickets {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map pgtMap = Collections.synchronizedMap(new HashMap());

	public InMemoryMapPersistentAdapterForProxyTickets() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("={");
		sb.append("with ").append(pgtMap.size())
				.append(" proxyGrantingTickets");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.cas.integration.IPersistentAdapterForProxyTickets#save(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void save(String pgtIou, String pgtId) {
		pgtMap.put(pgtIou, pgtId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.cas.integration.IPersistentAdapterForProxyTickets#get(java.lang.String)
	 */
	@Override
	public String get(String pgtIou) {
		return (String) pgtMap.get(pgtIou);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.cas.integration.IPersistentAdapterForProxyTickets#remove(java.lang.String)
	 */
	@Override
	public String remove(String pgtIou) {
		return (String) pgtMap.remove(pgtIou);

	}

}
