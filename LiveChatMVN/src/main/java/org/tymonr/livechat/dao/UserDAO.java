package org.tymonr.livechat.dao;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.tymonr.livechat.model.User;
import org.tymonr.livechat.model.filter.UserFilter;

/* TODO: 
 * switch to criteria api and hibernates query by example ? 
 * (JPA2 spec does cover query by example if I remember it right,
 * so we would have to lock to one provider... or build some facade
 * around it as all major providers support QBE. ~tymonr) */
/**
 *  User DAO
 */
public class UserDAO extends BaseDAO{
	
	public UserDAO(EntityManager entityManager) {
		super(entityManager);
	}

	/**
	 * Search for users with query by example filter
	 * 
	 * @param filter
	 * @param startIndex
	 * @param maxResults
	 * @return
	 */
	public List<User> findUsers(UserFilter filter, int startIndex, int maxResults){
		StringBuilder jpql = new StringBuilder("select u from " + User.class.getCanonicalName()+ " u ");
		if(filter != null){
			jpql.append("where 1=1 ");
			if(!StringUtils.isBlank(filter.getUsername())){
				jpql.append("and lower(u.username) like  :username ");
			}
			if(!StringUtils.isBlank(filter.getFirstname())){
				jpql.append("and lower(u.firstname) like :firstname ");
			}
			if(!StringUtils.isBlank(filter.getLastname())){
				jpql.append("and lower(u.lastname) like :lastname ");
			}
		}
		TypedQuery<User> query = entityManager.createQuery(jpql.toString(), User.class);
		if(filter != null){
			if(!StringUtils.isBlank(filter.getUsername())){
				query.setParameter("username", lowerLike(filter.getUsername()));
			}
			if(!StringUtils.isBlank(filter.getFirstname())){
				query.setParameter("firstname", lowerLike(filter.getFirstname()));
			}
			if(!StringUtils.isBlank(filter.getLastname())){
				query.setParameter("lastname", lowerLike(filter.getLastname()));
			}
		}
		query.setFirstResult(startIndex);
		query.setMaxResults(maxResults);
		
		List<User> result;
		try{
			result = query.getResultList();
		}catch(NoResultException nre){
			/* don't propagate JPA NRE exception up the stack,
			 * just return empty immutable list.
			 */
			result = Collections.emptyList();
		}
		return result;
	}
	
	
}
