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
package com.farmafene.cas.integration.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.cas.integration.basic.BasicRestClient;

public class CasClientService {

	private static final String CAS_SERVICE = "https://local.dev.farmafene.com/sample/cas/login";
	private static final Logger LOG = LoggerFactory.getLogger(CasClientService.class);

	@SuppressWarnings("deprecation")
	@Test
	public void main() throws InvalidPropertiesFormatException, FileNotFoundException, IOException {
		Properties props = new Properties();
		props.loadFromXML(new FileInputStream(CasClientLogin.TICKET_FILE));
		// /serviceValidate, /proxyValidate, /p3/serviceValidate,
		// /p3/proxyValidate, /proxy
		String tgt = props.getProperty(CasClientLogin.TGT);
		BasicRestClient client = new BasicRestClient();
		client.setCasServerUrlPrefix(CasClientLogin.CAS_URL);
		String ticket = client.getServiceTicket(tgt, CAS_SERVICE);
		LOG.info("Ticket = '{}'", ticket);
		System.err.println(String.format("%1$s/serviceValidate?ticket=%2$s&service=%3$s", CasClientLogin.CAS_URL,
				ticket, URLEncoder.encode(CAS_SERVICE)));
		System.err.println(String.format("%1$s/proxyValidate?ticket=%2$s&service=%3$s", CasClientLogin.CAS_URL, ticket,
				URLEncoder.encode(CAS_SERVICE)));
		System.err.println(String.format("%1$s/p3/serviceValidate?ticket=%2$s&service=%3$s", CasClientLogin.CAS_URL,
				ticket, URLEncoder.encode(CAS_SERVICE)));
		System.err.println(String.format("%1$s/p3/serviceValidate?ticket=%2$s&service=%3$s&format=json",
				CasClientLogin.CAS_URL, ticket, URLEncoder.encode(CAS_SERVICE)));
		System.err.println(String.format("%1$s/p3/proxyValidate?ticket=%2$s&service=%3$s", CasClientLogin.CAS_URL,
				ticket, URLEncoder.encode(CAS_SERVICE)));
		System.err.println(String.format("%1$s/p3/proxyValidate?ticket=%2$s&service=%3$s&format=json",
				CasClientLogin.CAS_URL, ticket, URLEncoder.encode(CAS_SERVICE)));
		System.err.println();
	}
}
