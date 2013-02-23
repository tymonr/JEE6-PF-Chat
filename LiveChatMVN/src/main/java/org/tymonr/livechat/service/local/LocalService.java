package org.tymonr.livechat.service.local;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.tymonr.livechat.dao.UserDAO;
import org.tymonr.livechat.model.BaseEntity;

/**
 * Base class for stateless local services. Contains factory methods for daos.
 */
@Stateless
public abstract class LocalService {
	@PersistenceContext
	protected EntityManager entityManager;

	private UserDAO userDAO;

	public UserDAO getUserDAO() {
		if (userDAO == null) {
			userDAO = new UserDAO(entityManager);
		}
		return userDAO;
	}

	public <T> void remove(T entity) {
		getUserDAO().remove(entity);
	}

	public BaseEntity save(BaseEntity entity) {
		entity = getUserDAO().save(entity);
		return entity;
	}

	public <T> T find(Class<T> clazz, Object id) {
		return getUserDAO().find(clazz, id);
	}

}
