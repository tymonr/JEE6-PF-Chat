package org.tymonr.livechat.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.tymonr.livechat.model.Conversation;
import org.tymonr.livechat.model.Message;
import org.tymonr.livechat.model.User;
import org.tymonr.livechat.model.filter.UserFilter;

import static com.google.common.base.Preconditions.checkNotNull;

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

	/**
	 * Find user by username, pull associated lazy collections.
	 * @param username
	 * @return null if not found
	 */
	public User userByUsername(String username) {
		checkNotNull(username);
		StringBuilder jpql = new StringBuilder("select u from " + User.class.getCanonicalName() + " u ");
		jpql.append(" left join fetch u.contacts c ");
		jpql.append(" left join fetch c.other ");
		jpql.append(" where u.username = :username ");
		
		TypedQuery<User> query = entityManager.createQuery(jpql.toString(), User.class);
		query.setParameter("username", username);
		
		User result = null;
		try{
		result = query.getSingleResult();
		}catch(NoResultException nre){
			// swallow on purpose
		}
		return result;
	}

	public List<Message> loadShoutboxMessages(int numberOfMessages) {
		StringBuilder jpql = new StringBuilder("select m from " + Message.class.getCanonicalName() + " m ");
		jpql.append("join fetch m.author ");
		jpql.append("where m.conversation is null ");
		jpql.append("order by m.timeSent desc ");
		
		TypedQuery<Message> query = entityManager.createQuery(jpql.toString(), Message.class);
		query.setMaxResults(numberOfMessages);
		
		List<Message> result;
		try{
			result = query.getResultList();
		}catch(NoResultException nre){
			result = new ArrayList<Message>();
		}
		return result;
	}

	public List<Conversation> loadActiveConversations(User user) {
		checkNotNull(user);
		StringBuilder jpql = new StringBuilder("select c from " + Conversation.class.getCanonicalName() + " c ");
		jpql.append("join fetch c.owner o ");
		jpql.append("join fetch c.receiver r ");
		jpql.append("where (o = :user or r = :user) ");
		jpql.append("and c.timeEnded is null ");
		jpql.append("order by c.timeStarted desc "); 
		
		TypedQuery<Conversation> query = entityManager.createQuery(jpql.toString(), Conversation.class);
		query.setParameter("user", user);
		
		List<Conversation> result;
		try{
			result = query.getResultList();
		}catch(NoResultException nre){
			result = new ArrayList<Conversation>();
		}
		return result;
	}

	public List<Message> loadConversationMessages(Conversation conversation,
			int numberOfMessages) {
		checkNotNull(conversation);
		
		StringBuilder jpql = new StringBuilder("select m from " + Message.class.getCanonicalName() + " m ");
		jpql.append("join fetch m.author ");
		if(conversation.getId() == null){
			jpql.append("where m.conversation is null ");
		}else{
			jpql.append("where m.conversation = :conversation ");
		}
		jpql.append("order by m.timeSent desc ");
		
		TypedQuery<Message> query = entityManager.createQuery(jpql.toString(), Message.class);
		if(conversation.getId() != null){
			query.setParameter("conversation", conversation);
		}
		query.setMaxResults(numberOfMessages);
		
		List<Message> result;
		try{
			result = query.getResultList();
		}catch(NoResultException nre){
			result = new ArrayList<Message>();
		}
		return result;
	}
	
	
}
