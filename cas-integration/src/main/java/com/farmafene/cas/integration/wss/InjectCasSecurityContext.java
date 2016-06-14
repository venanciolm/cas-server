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
package com.farmafene.cas.integration.wss;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.security.SecurityContext;
import org.apache.wss4j.common.ext.WSSecurityException;

import com.farmafene.cas.integration.basic.BasicRestClient;

public class InjectCasSecurityContext extends AbstractPhaseInterceptor<Message> {

	public InjectCasSecurityContext() {
		super(Phase.PRE_INVOKE);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.message.Message)
	 */
	@Override
	public void handleMessage(Message message) throws Fault {
		SecurityContext sc = message.get(SecurityContext.class);
		if (sc != null && sc.getUserPrincipal() != null
				&& (sc.getUserPrincipal() instanceof CasTokenPrincipal)) {
			CasTokenPrincipal ctp = (CasTokenPrincipal) sc.getUserPrincipal();
			CasSecurityContext ctx = new CasSecurityContext();
			ctx.setCasTokenPrincipal(ctp);
			BasicRestClient client = new BasicRestClient();
			client.setCasServerUrlPrefix(ctp.getCasServerUrlPrefix());
			message.put(SecurityContext.class, ctx);
			return;
		}
		// else if (message.get(CasSecurityContext.class) != null) {
		// CasSecurityContext csc = (CasSecurityContext) message
		// .get(CasSecurityContext.class);
		// BasicRestClient client = new BasicRestClient();
		// client.setCasServerUrlPrefix(((CasTokenPrincipal) csc
		// .getUserPrincipal()).getCasServerUrlPrefix());
		// message.put(SecurityContext.class, csc);
		// return;
		// }
		throw new Fault(new WSSecurityException(
				WSSecurityException.ErrorCode.INVALID_SECURITY_TOKEN));
	}

}
