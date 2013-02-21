package org.tymonr.livechat.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.tymonr.livechat.model.User;

public class UserDAO {
	
	@PersistenceContext
	protected EntityManager entityManager;

	public User findUser(String username){
		TypedQuery<User> query = entityManager.createNamedQuery("findUser", User.class);
		query.setParameter("username", username);
		User user = query.getSingleResult();
		return user;
	}
}
