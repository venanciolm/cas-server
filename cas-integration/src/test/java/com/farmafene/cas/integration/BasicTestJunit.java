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
package com.farmafene.cas.integration;

import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.cas.integration.basic.BasicRestClient;

public class BasicTestJunit {

	private static final Logger LOG = LoggerFactory
			.getLogger(BasicTestJunit.class);
	private static final String USER = "A";
	private static final String PWD = "A";
	private static String TGT;
	private static String CAS_SERVER = "https://local.dev.farmafene.com/cas";
	private static String CAS_PROXY_CALLBACK = CAS_SERVER + "/casProxy";
	private static BasicRestClient UTIL;
	private static String PGT;

	@BeforeClass
	public static void genenetateTickets() {
		UTIL = new BasicRestClient();
		UTIL.setCasServerUrlPrefix(CAS_SERVER);
		TGT = UTIL.getTicketGrantingTicket(USER, PWD);
		Assert.assertNotNull("No se ha generado el ticket Granting ticket", TGT);
	}

	@AfterClass
	public static void Logout() {
		LOG.info("Destruyendo TGT: {}", TGT);
		UTIL.logout(TGT);
		LOG.info("Destruido TGT: {}", TGT);
	}

	@Test
	public void _01test() {
		String ticket = UTIL.getServiceTicket(TGT, CAS_SERVER);
		Assert.assertNotNull("No se ha generado el service '{}'", ticket);
		Assert.assertNotNull("No se ha generado el service ", ticket);
		LOG.info("El ticket es: {}", ticket);
		String user = UTIL.validate(CAS_SERVER, ticket);
		Assert.assertNotNull("No se ha devuelto el usuario '{}'", user);
		Assert.assertEquals("El usuarion no corresponde con el loggeado", USER,
				user);
		LOG.info("El usuario es: {}", user);
	}

	@Test
	public void _02test() {
		String ticket = UTIL.getServiceTicket(TGT, CAS_SERVER);
		Assert.assertNotNull("No se ha generado el service '{}'", ticket);
		LOG.info("El ticket es: {}", ticket);
		String user = UTIL.serviceValidate(CAS_SERVER, ticket);
		Assert.assertNotNull("No se ha devuelto el usuario ", user);
		Assert.assertEquals("El usuario no corresponde con el loggeado", USER,
				user);
		LOG.info("El usuario es: {}", user);
	}

	@Test
	public void _03test() {
		String ticket = UTIL.getServiceTicket(TGT, CAS_SERVER);
		Assert.assertNotNull("No se ha generado el service '{}'", ticket);
		LOG.info("El ticket es: {}", ticket);
		Map<String, String> map = UTIL.serviceValidate(CAS_SERVER, ticket,
				CAS_PROXY_CALLBACK);
		Assert.assertNotNull("No se ha devuelto el mapa '{}'", map);
		LOG.info("El mapa es: {}", map);
		Assert.assertNotNull("No se ha devuelto el usuario '{}'",
				map.get(BasicRestClient.USER));
		Assert.assertEquals("El usuarion no corresponde con el loggeado", USER,
				map.get(BasicRestClient.USER));
		LOG.info("El usuario es: {}", map.get(BasicRestClient.USER));
		Assert.assertNotNull("No se ha devuelto el PGT ",
				map.get(BasicRestClient.PGT));
		LOG.info("El PGT: {}", map.get(BasicRestClient.PGT));
		PGT = map.get(BasicRestClient.PGT);
	}

	@Test
	public void _04test() {
		String ticket = UTIL.getProxyServiceTicket(PGT, CAS_SERVER);
		Assert.assertNotNull("No se ha generado el service '{}'", ticket);
		LOG.info("El ticket es: {}", ticket);
		String user = UTIL.proxyValidate(CAS_SERVER, ticket);
		Assert.assertNotNull("No se ha devuelto el usuario ", user);
		Assert.assertEquals("El usuario no corresponde con el loggeado", USER,
				user);
		LOG.info("El usuario es: {}", user);
	}

	@Test
	public void _05test() {
		String newService = "https://localhost";
		String ticket = UTIL.getProxyServiceTicket(PGT, newService);
		Assert.assertNotNull("No se ha generado el service '{}'", ticket);
		LOG.info("El ticket es: {}", ticket);
		String user = UTIL.proxyValidate(newService, ticket);
		Assert.assertNotNull("No se ha devuelto el usuario ", user);
		Assert.assertEquals("El usuario no corresponde con el loggeado", USER,
				user);
		LOG.info("El usuario es: {}", user);
	}
}
