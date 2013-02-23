package org.tymonr.livechat.dao;

import javax.persistence.EntityManager;

import org.tymonr.livechat.model.BaseEntity;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class BaseDAO {

	protected EntityManager entityManager;

	public BaseDAO(EntityManager entityManager) {
		this.entityManager = checkNotNull(entityManager);
	}

	/**
	 * Query helper to be used with like statements.
	 * 
	 * @param filterString
	 * @return
	 */
	protected String lowerLike(String filterString) {
		return "%" + filterString.toLowerCase() + "%";
	}

	public <T> void remove(T entity) {
		entity = entityManager.merge(entity);
		entityManager.remove(entity);
	}

	public BaseEntity save(BaseEntity entity) {
		if (entity.getId() != null) {
			entity = entityManager.merge(entity);
		} else {
			entityManager.persist(entity);
		}
		return entity;
	}

	public <T> T find(Class<T> clazz, Object id) {
		return entityManager.find(clazz, id);
	}

}
