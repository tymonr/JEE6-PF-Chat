package org.tymonr.livechat.dao;

import javax.persistence.EntityManager;
import static com.google.common.base.Preconditions.checkNotNull;

public abstract class BaseDAO {
	
	protected EntityManager entityManager;
	
	public BaseDAO(EntityManager entityManager){
		this.entityManager = checkNotNull(entityManager);
	}
	
	/**
	 * Query helper to be used with like statements.
	 * @param filterString
	 * @return
	 */
	protected String lowerLike(String filterString){
		return "%" + filterString.toLowerCase() + "%";
	}

}
