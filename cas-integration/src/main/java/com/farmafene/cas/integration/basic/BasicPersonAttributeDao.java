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
package com.farmafene.cas.integration.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jasig.services.persondir.IPersonAttributeDao;
import org.jasig.services.persondir.IPersonAttributes;
import org.jasig.services.persondir.support.NamedPersonImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicPersonAttributeDao implements IPersonAttributeDao {

	private static final Logger logger = LoggerFactory
			.getLogger(BasicPersonAttributeDao.class);

	public BasicPersonAttributeDao() {
		logger.info("{}<init>", this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("={");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jasig.services.persondir.IPersonAttributeDao#getPerson(java.lang.String)
	 */
	@Override
	public IPersonAttributes getPerson(String uid) {

		Map<String, List<Object>> attributes = new LinkedHashMap<String, List<Object>>();
		List<Object> values = new ArrayList<Object>();
		attributes.put("customAttribute",
				Arrays.asList(new Object[] { "00000000T" }));
		attributes.put("item1", Arrays.asList(new Object[] { "value1" }));
		attributes.put("item2", Arrays.asList(new Object[] { "value2" }));
		attributes.put("item3", Arrays.asList(new Object[] { "value3" }));
		values.add("ROLE_01");
		values.add("ROLE_02");
		attributes.put("memberOf", values);
		NamedPersonImpl impl = new NamedPersonImpl(uid, attributes);
		logger.info("Returning: {}", impl);
		return impl;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jasig.services.persondir.IPersonAttributeDao#getPeople(java.util.Map)
	 */
	@Override
	public Set<IPersonAttributes> getPeople(Map<String, Object> query) {
		logger.info("getPeople({})", query);
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jasig.services.persondir.IPersonAttributeDao#getPeopleWithMultivaluedAttributes(java.util.Map)
	 */
	@Override
	public Set<IPersonAttributes> getPeopleWithMultivaluedAttributes(
			Map<String, List<Object>> query) {
		logger.info("getPeopleWithMultivaluedAttributes({})", query);
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jasig.services.persondir.IPersonAttributeDao#getPossibleUserAttributeNames()
	 */
	@Override
	public Set<String> getPossibleUserAttributeNames() {
		// Debiera retornar null!, no los sabemos.
		logger.info("getPossibleUserAttributeNames()");
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jasig.services.persondir.IPersonAttributeDao#getAvailableQueryAttributes()
	 */
	@Override
	public Set<String> getAvailableQueryAttributes() {
		// Debiera retornar null!, no los sabemos.
		logger.info("getAvailableQueryAttributes()");
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jasig.services.persondir.IPersonAttributeDao#getMultivaluedUserAttributes(java.util.Map)
	 */
	@Override
	public Map<String, List<Object>> getMultivaluedUserAttributes(
			Map<String, List<Object>> seed) {
		logger.info("getMultivaluedUserAttributes({})", seed);
		// ¿FIXME?
		return seed;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jasig.services.persondir.IPersonAttributeDao#getMultivaluedUserAttributes(java.lang.String)
	 */
	@Override
	public Map<String, List<Object>> getMultivaluedUserAttributes(String uid) {
		logger.info("deprecated:getMultivaluedUserAttributes({})", uid);
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jasig.services.persondir.IPersonAttributeDao#getUserAttributes(java.util.Map)
	 */
	@Override
	public Map<String, Object> getUserAttributes(Map<String, Object> seed) {
		logger.info("deprecated:getUserAttributes({})", seed);
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jasig.services.persondir.IPersonAttributeDao#getUserAttributes(java.lang.String)
	 */
	@Override
	public Map<String, Object> getUserAttributes(String uid) {
		logger.info("deprecated:getUserAttributes({})", uid);
		// FIXME
		return null;
	}
}
