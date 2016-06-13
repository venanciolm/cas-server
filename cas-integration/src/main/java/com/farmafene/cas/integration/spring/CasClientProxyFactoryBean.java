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

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.farmafene.cas.integration.clients.CxfCasTokenInjectInterceptor;
import com.farmafene.cas.integration.clients.ICasServiceTicketFactory;

public class CasClientProxyFactoryBean<T> implements InitializingBean,
		FactoryBean<T> {
	private Class<T> serviceClass;
	private String serviceName;
	private String address;
	private ICasServiceTicketFactory serviceTicketFactory;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(serviceClass, "Debe establecerse una clase de servicio");
		Assert.notNull(address, "Debe establecerse un endpoint");
		Assert.notNull(serviceTicketFactory,
				"Debe establecerse un serviceTicketFactory");
		Assert.notNull(serviceName, "Debe establecerse el nombre de servicio");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public T getObject() throws Exception {
		ClientProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setServiceClass(serviceClass);
		factory.setAddress(address);
		CxfCasTokenInjectInterceptor filter = new CxfCasTokenInjectInterceptor();
		filter.setServiceName(serviceName);
		filter.setServiceTicketFactory(serviceTicketFactory);
		@SuppressWarnings("unchecked")
		T service = (T) factory.create();
		Client proxy = ClientProxy.getClient(service);
		proxy.getOutInterceptors().add(filter);
		return service;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<?> getObjectType() {
		return serviceClass;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

}
