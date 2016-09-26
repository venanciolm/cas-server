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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.jasig.cas.client.proxy.ProxyGrantingTicketStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyGrantingTicketStorageForTicketServer implements
		ProxyGrantingTicketStorage {

	private static final String ENCODING = "UTF-8";
	private static final int READ_TIMEOUT_DEFAULT = 60000;
	private static final int CONNECT_TIMEOUT_DEFAULT = 60000;
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String ACCEPT_ENCODING = "Accept-Encoding";
	private static final String URL_ENCODED = "application/x-www-form-urlencoded";

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProxyGrantingTicketStorageForTicketServer.class);

	private String retrieveUrl;
	private String charEncoding = ENCODING;
	private String encoding = ENCODING;
	private int connectTimeoutMs = CONNECT_TIMEOUT_DEFAULT;
	private int readTimeoutMs = READ_TIMEOUT_DEFAULT;

	public ProxyGrantingTicketStorageForTicketServer() {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jasig.cas.client.proxy.ProxyGrantingTicketStorage#retrieve(java.lang.String)
	 */
	@Override
	public String retrieve(String proxyGrantingTicketIou) {
		Map<String, String> items = new HashMap<String, String>();
		items.put("pgtIou", proxyGrantingTicketIou);
		HttpURLConnection con = null;
		String pgt = null;
		try {

			String urlParameters = urlEncode(items, encoding);
			String url = String.format("%1$s?%2$s", retrieveUrl, urlParameters);
			URL uri = null;
			uri = new URL(url);
			con = (HttpURLConnection) uri.openConnection();
			con.setRequestMethod("GET");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setConnectTimeout(connectTimeoutMs);
			con.setReadTimeout(readTimeoutMs);
			con.setRequestProperty(ACCEPT_ENCODING, charEncoding);
			con.setRequestProperty(CONTENT_TYPE, URL_ENCODED);
			con.setUseCaches(false);
			con.connect();
			int responseCode = con.getResponseCode();
			LOGGER.debug("El response de getProxyTicket es: '{}'", responseCode);
			switch (responseCode) {
			case HttpURLConnection.HTTP_OK:
				InputStreamReader isr = new InputStreamReader(
						con.getInputStream());
				BufferedReader br = new BufferedReader(isr);
				pgt = br.readLine();
				if (null == pgt || "".equals(pgt.trim())) {
					throw new IllegalStateException("Invalid pgtIou: "
							+ retrieveUrl + "-" + proxyGrantingTicketIou);
				}
				break;
			case HttpURLConnection.HTTP_BAD_REQUEST:
			case HttpURLConnection.HTTP_UNSUPPORTED_TYPE:
			default:
				throw new IllegalStateException("Invalid request "
						+ responseCode + " - " + con.getResponseMessage());
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		} finally {
			if (null != con) {
				con.disconnect();
			}
		}
		LOGGER.info("retrieve({}): {}", proxyGrantingTicketIou, pgt);
		return pgt;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jasig.cas.client.proxy.ProxyGrantingTicketStorage#cleanUp()
	 */
	@Override
	public void cleanUp() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jasig.cas.client.proxy.ProxyGrantingTicketStorage#save(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void save(String proxyGrantingTicketIou, String proxyGrantingTicket) {
	}

	private String urlEncode(Map<String, String> items, String enc)
			throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		for (String var : items.keySet()) {
			if (sb.length() > 0) {
				sb.append("&");
			}
			sb.append(URLEncoder.encode(var, enc));
			sb.append("=");
			sb.append(URLEncoder.encode(items.get(var), enc));
		}
		return sb.toString();
	}

	/**
	 * @return the retrieveUrl
	 */
	public String getRetrieveUrl() {
		return retrieveUrl;
	}

	/**
	 * @param retrieveUrl the retrieveUrl to set
	 */
	public void setRetrieveUrl(String retrieveUrl) {
		this.retrieveUrl = retrieveUrl;
	}

	/**
	 * @return the charEncoding
	 */
	public String getCharEncoding() {
		return charEncoding;
	}

	/**
	 * @param charEncoding the charEncoding to set
	 */
	public void setCharEncoding(String charEncoding) {
		this.charEncoding = charEncoding;
	}

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @return the connectTimeoutMs
	 */
	public int getConnectTimeoutMs() {
		return connectTimeoutMs;
	}

	/**
	 * @param connectTimeoutMs the connectTimeoutMs to set
	 */
	public void setConnectTimeoutMs(int connectTimeoutMs) {
		this.connectTimeoutMs = connectTimeoutMs;
	}

	/**
	 * @return the readTimeoutMs
	 */
	public int getReadTimeoutMs() {
		return readTimeoutMs;
	}

	/**
	 * @param readTimeoutMs the readTimeoutMs to set
	 */
	public void setReadTimeoutMs(int readTimeoutMs) {
		this.readTimeoutMs = readTimeoutMs;
	}

}
