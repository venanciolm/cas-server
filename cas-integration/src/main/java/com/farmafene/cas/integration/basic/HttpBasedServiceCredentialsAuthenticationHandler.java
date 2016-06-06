package com.farmafene.cas.integration.basic;

import java.net.URL;
import java.security.GeneralSecurityException;

import javax.security.auth.login.FailedLoginException;
import javax.validation.constraints.NotNull;

import org.jasig.cas.authentication.AbstractAuthenticationHandler;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.DefaultHandlerResult;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.HttpBasedServiceCredential;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.util.http.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class HttpBasedServiceCredentialsAuthenticationHandler extends
		AbstractAuthenticationHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(HttpBasedServiceCredentialsAuthenticationHandler.class);
	@NotNull
	@Autowired
	@Qualifier("supportsTrustStoreSslSocketFactoryHttpClient")
	private HttpClient httpClient;

	public HttpBasedServiceCredentialsAuthenticationHandler() {
		logger.info("creando el bean: {}", this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jasig.cas.authentication.AuthenticationHandler#authenticate(org.jasig.cas.authentication.Credential)
	 */
	@Override
	public HandlerResult authenticate(Credential credential)
			throws GeneralSecurityException, PreventedException {
		final HttpBasedServiceCredential httpCredential = (HttpBasedServiceCredential) credential;
		logger.info("Validando el proxy: {}", httpCredential.getCallbackUrl());
		if (!httpCredential.getService().getProxyPolicy()
				.isAllowedProxyCallbackUrl(httpCredential.getCallbackUrl())) {
			logger.warn(
					"Proxy policy for service [{}] cannot authorize the requested callback url [{}].",
					httpCredential.getService().getServiceId(),
					httpCredential.getCallbackUrl());
			throw new FailedLoginException(httpCredential.getCallbackUrl()
					+ " cannot be authorized");
		}
		logger.debug("Attempting to authenticate {}", httpCredential);
		logger.debug("Attempting to authenticate {}", this.httpClient);
		final URL callbackUrl = httpCredential.getCallbackUrl();
		if (!this.httpClient.isValidEndPoint(callbackUrl)) {
			logger.error("El punto no ha podido establecerse");
			throw new FailedLoginException(callbackUrl.toExternalForm()
					+ " sent an unacceptable response status code");
		}
		HandlerResult result = new DefaultHandlerResult(this, httpCredential,
				this.principalFactory.createPrincipal(httpCredential.getId()));
		logger.info("Hemos autorizado: {}", httpCredential.getId());
		logger.info("Hemos autorizado: {}", result);
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jasig.cas.authentication.AuthenticationHandler#supports(org.jasig.cas.authentication.Credential)
	 */
	@Override
	public boolean supports(Credential credential) {
		boolean supports = credential instanceof HttpBasedServiceCredential;
		logger.info("{}.supports(): {}", this, supports);
		return supports;
	}

	/**
	 * Sets the HttpClient which will do all of the connection stuff.
	 * 
	 * @param httpClient
	 *            http client instance to use
	 **/
	public void setHttpClient(final HttpClient httpClient) {
		this.httpClient = httpClient;
	}
}
