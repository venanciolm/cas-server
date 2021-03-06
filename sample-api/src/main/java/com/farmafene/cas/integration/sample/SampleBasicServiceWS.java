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
package com.farmafene.cas.integration.sample;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.soap.MTOM;

import org.apache.cxf.annotations.Policies;
import org.apache.cxf.annotations.Policy;
import org.apache.cxf.annotations.Policy.Placement;

@WebService(
//
targetNamespace = "http://samples.farmafene.com/cxf/schema",
//
serviceName = "sampleBasic",
//
portName = "sampleBasicPort",
//
name = "sampleBasicBinding"
//
)
@MTOM
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@Policies(value = {
		@Policy(uri = "policies/bst-policy.xml", placement = Placement.BINDING),
		@Policy(uri = "policies/mtom-policy.xml") })
public interface SampleBasicServiceWS extends ISampleBasicService {
	@WebMethod
	@WebResult(
	//
	name = "SampleResponse",
	//
	partName = "SampleResponse",
	//
	targetNamespace = "http://samples.farmafene.com/cxf/schema"
	//
	)
	public SampleResponse echo(//
			@WebParam(
			//
			name = "SampleRequest",
			//
			partName = "SampleRequest",
			//
			targetNamespace = "http://samples.farmafene.com/cxf/schema"
			//
			) SampleRequest request);
}
