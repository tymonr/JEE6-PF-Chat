package org.tymonr.livechat.service.local;

import javax.ejb.Stateless;

import org.tymonr.livechat.model.User;

@Stateless
public class UserRepository extends LocalService {

	public User userByUsername(String username) {
		User user = null;
		user = getUserDAO().userByUsername(username);
		return user;
	}
}
