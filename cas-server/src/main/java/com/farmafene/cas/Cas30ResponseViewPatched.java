package com.farmafene.cas;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.validation.Assertion;
import org.jasig.cas.validation.ImmutableAssertion;
import org.jasig.cas.web.view.Cas30JsonResponseView;

public class Cas30ResponseViewPatched extends Cas30JsonResponseView {

	private static final String ASSERTION = "assertion";

	public Cas30ResponseViewPatched() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jasig.cas.web.view.Cas30JsonResponseView#prepareMergedOutputModel(java.util.Map,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void prepareMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Assertion assertion = (Assertion) model.get(ASSERTION);
		if (assertion != null && assertion.getChainedAuthentications() != null
				&& assertion.getChainedAuthentications().size() > 0) {
			ImmutableAssertion is = new ImmutableAssertion(assertion.getChainedAuthentications().get(0),
					assertion.getChainedAuthentications(), assertion.getService(), assertion.isFromNewLogin());
			model.put(ASSERTION, is);
		}
		super.prepareMergedOutputModel(model, request, response);
	}

}
