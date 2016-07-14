package com.farmafene.cas.integration.basic;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jasig.services.persondir.IPersonAttributeDao;
import org.jasig.services.persondir.IPersonAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonAttributeDaoTest implements IPersonAttributeDao {

	private static final Logger logger = LoggerFactory
			.getLogger(PersonAttributeDaoTest.class);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jasig.services.persondir.IPersonAttributeDao#getPerson(java.lang.String)
	 */
	@Override
	public IPersonAttributes getPerson(String uid) {
		logger.info("Retornando los atributos de: {}", uid);
		return null;
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
