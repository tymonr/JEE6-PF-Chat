package org.tymonr.livechat.session;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.SecurityUtils;
import org.tymonr.livechat.model.User;
import org.tymonr.livechat.service.local.ChatRepository;

@Named
@SessionScoped
public class Session implements Serializable{
	private static final long serialVersionUID = -1261077126537768750L;
	
	@Inject
	private ChatRepository chatRepository;
	
	private User user;
	
	@Produces
	@Named("loggedinUser")
	@Loggedin
	public User getLoggedinUser(){
		if(user == null){
			loadUser();
		}
		return user;
	}
	
	private void loadUser(){
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		user = chatRepository.userByName(username);
		if(user == null){
			throw new IllegalStateException("Can't load loggedin user data");
		}
	}

}
