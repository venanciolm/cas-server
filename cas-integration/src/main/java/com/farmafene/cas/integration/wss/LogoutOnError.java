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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogoutOnError extends AbstractPhaseInterceptor<Message> {

	private static final Logger logger = LoggerFactory
			.getLogger(LogoutOnError.class);

	public LogoutOnError() {
		super(Phase.PRE_INVOKE);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.message.Message)
	 */
	@Override
	public void handleMessage(Message message) throws Fault {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.apache.cxf.phase.AbstractPhaseInterceptor#handleFault(org.apache.cxf.message.Message)
	 */
	@Override
	public void handleFault(Message message) {
		logger.error("===========================================");
		logger.error("= FAIL MESSAGE IN *** FORCING LOGUT ***   =");
		logger.error("===========================================");
		SecurityContext sc = message.get(SecurityContext.class);
		logger.info("Validando si existe Logout {}", sc);
		if (sc instanceof CasSecurityContext) {
			logger.info("Procediendo al logout");
			((CasSecurityContext) sc).logout();
		}
	}

}
