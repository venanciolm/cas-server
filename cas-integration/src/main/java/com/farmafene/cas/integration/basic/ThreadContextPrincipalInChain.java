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

import org.jasig.cas.authentication.HandlerResult;

public class ThreadContextPrincipalInChain implements IPrincipalInChain {

	private ThreadLocal<HandlerResult> repo = new ThreadLocal<HandlerResult>();

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.cas.integration.basic.IPrincipalInChain#put(org.jasig.cas.authentication.HandlerResult)
	 */
	@Override
	public void put(HandlerResult result) {
		repo.set(result);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.cas.integration.basic.IPrincipalInChain#get(java.lang.String)
	 */
	@Override
	public HandlerResult get(String uid) {
		HandlerResult result = repo.get();
		HandlerResult validResult = null;
		if (null != result && result.getPrincipal().getId().equals(uid)) {
			validResult = result;
		}
		repo.remove();
		return validResult;
	}
}
