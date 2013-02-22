package org.tymonr.livechat.service.local;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.tymonr.livechat.dao.UserDAO;

/**
 * Base class for stateless local services.
 * Contains factory methods for daos.
 */
@Stateless
public abstract class LocalService {
	@PersistenceContext
	protected EntityManager entityManager;
	
	private UserDAO userDAO;

	public UserDAO getUserDAO() {
		if(userDAO == null){
			userDAO = new UserDAO(entityManager);
		}
		return userDAO;
	}


}
