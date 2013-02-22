package org.tymonr.livechat.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tymonr.livechat.model.User;
import org.tymonr.livechat.model.filter.UserFilter;
import org.tymonr.livechat.service.local.ChatRepository;

/* TODO: migrate to JSF2.2, change to ViewScope */
@Named
@SessionScoped
public class FriendsSearch implements Serializable {
	private static final long serialVersionUID = 4046192369977778174L;
	private static final Logger log = LoggerFactory.getLogger(FriendsSearch.class);

	private List<User> users;
	private UserFilter filter;

	@Inject
	private ChatRepository chatRepository;

	public FriendsSearch() {
	}

	/** Search actions */
	public String search() {
		try {
			log.debug("search...");
			log.error("err...");
			users = chatRepository.findUsers(filter, 0, Integer.MAX_VALUE);
		} catch (Exception e) {
			log.error("", e);
		}
		return null;
	}

	public List<User> getUsers() {
		if (users == null) {
			users = new ArrayList<User>();
		}
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public UserFilter getFilter() {
		if (filter == null) {
			filter = new UserFilter();
		}
		return filter;
	}

	public void setFilter(UserFilter filter) {
		this.filter = filter;
	}

}
