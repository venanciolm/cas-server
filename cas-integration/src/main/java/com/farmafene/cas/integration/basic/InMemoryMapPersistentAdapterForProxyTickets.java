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

import java.util.concurrent.TimeUnit;

import com.farmafene.cas.integration.IPersistentAdapterForProxyTickets;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class InMemoryMapPersistentAdapterForProxyTickets implements
		IPersistentAdapterForProxyTickets {
	private static final long MAX_CACHE = 500;
	private static final long MAX_SECONDS = 5;
	private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

	private Cache<String, String> pgtMap = CacheBuilder.newBuilder()
			.maximumSize(getCacheSize())
			.expireAfterWrite(getTimeValue(), getTimeUnit()).build();

	public InMemoryMapPersistentAdapterForProxyTickets() {
	}

	private TimeUnit getTimeUnit() {
		return TIME_UNIT;
	}

	private long getTimeValue() {
		return MAX_SECONDS;
	}

	private long getCacheSize() {
		return MAX_CACHE;
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
		return pgtMap.getIfPresent(pgtIou);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.cas.integration.IPersistentAdapterForProxyTickets#remove(java.lang.String)
	 */
	@Override
	public String remove(String pgtIou) {
		String pgt = pgtMap.getIfPresent(pgtIou);
		if (null != pgt) {
			pgtMap.invalidate(pgtIou);
		}
		return pgt;
	}

}
