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
package com.farmafene.cas.integration.sample.impl;

import javax.xml.ws.WebServiceContext;

import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.farmafene.cas.integration.sample.ISampleBasicService;
import com.farmafene.cas.integration.sample.SampleRequest;
import com.farmafene.cas.integration.sample.SampleResponse;

@Service("sample2BasicImpl")
public class Sample2BasicImpl implements ISampleBasicService {

	private static final Logger logger = LoggerFactory
			.getLogger(Sample2BasicImpl.class);
	private WebServiceContext wsctx = new WebServiceContextImpl();

	@Autowired
	@Qualifier("sample3BasicClient")
	ISampleBasicService service;

	public Sample2BasicImpl() {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.cas.integration.sample.ISampleBasicService#echo(com.farmafene.cas.integration.sample.SampleRequest)
	 */
	@Override
	public SampleResponse echo(SampleRequest request) {
		SampleResponse response = new SampleResponse();
		response.setMessage(request.getMessage());
		logger.info("Context {}", wsctx);
		if (wsctx != null) {
			logger.info("Principal {}", wsctx.getClass());
			logger.info("Principal {}", wsctx.getUserPrincipal());
			if (null != wsctx.getUserPrincipal()) {
				logger.info("Principal Name es  {}", wsctx.getUserPrincipal()
						.getName());
				logger.info("¿Está el role {}?, {} ", "ROLE_01",
						wsctx.isUserInRole("ROLE_01"));
			}
		}
		logger.info("El servicio es: {}", service);
		return service.echo(request);
	}

}
