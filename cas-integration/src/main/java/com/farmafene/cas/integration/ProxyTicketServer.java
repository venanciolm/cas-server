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

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.cas.integration.basic.InMemoryMapPersistentAdapterForProxyTickets;

@SuppressWarnings("serial")
public class ProxyTicketServer extends HttpServlet {
	private static final Logger logger = LoggerFactory
			.getLogger(ProxyTicketServer.class);
	private static final String PROXY_SUCCESS_RESPONSE = "<casClient:proxySuccess xmlns:casClient=\"http://www.yale.edu/tp/casClient\"/>";
	private static final String PGT_IOU = "pgtIou";
	private static final String PGT_ID = "pgtId";
	private IPersistentAdapterForProxyTickets persistentAdapter = new InMemoryMapPersistentAdapterForProxyTickets();

	public ProxyTicketServer() {
		super();
		logger.info("Hemos inicializado el casProxy!");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pgtId = request.getParameter(PGT_ID);
		String pgtIou = request.getParameter(PGT_IOU);
		if ((pgtId != null) && (pgtIou != null)) {
			persistentAdapter.save(pgtIou, pgtId);
			PrintWriter out = response.getWriter();
			out.println(PROXY_SUCCESS_RESPONSE);
			out.flush();
			logger.info("ProxyTicket stored!");
		} else if (pgtIou != null) {
			PrintWriter out = response.getWriter();
			out.println(persistentAdapter.get(pgtIou));
			out.flush();
			persistentAdapter.remove(pgtIou);
			logger.info("ProxyTicket removed from storage");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Change de IPersistentAdapterForProxyTickets
		super.init(config);
	}

}
