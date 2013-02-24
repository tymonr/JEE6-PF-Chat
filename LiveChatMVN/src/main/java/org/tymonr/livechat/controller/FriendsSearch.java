package org.tymonr.livechat.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.tymonr.livechat.exception.MessageHandler;
import org.tymonr.livechat.model.Contact;
import org.tymonr.livechat.model.User;
import org.tymonr.livechat.model.filter.UserFilter;
import org.tymonr.livechat.service.local.ChatRepository;
import org.tymonr.livechat.session.Loggedin;

/* TODO: 
 * 1. migrate to JSF2.2, change to ViewScope
 * 2. see Chat.java */
@Named
@SessionScoped
public class FriendsSearch implements Serializable {
	private static final long serialVersionUID = 4046192369977778174L;

	private List<User> users;
	private UserFilter filter;

	@Inject
	private ChatRepository chatRepository;

	@Inject
	@Loggedin
	private User user;

	public FriendsSearch() {
	}

	/** Search action */
	public String search() {
		try {
			users = chatRepository.findUsers(filter, 0, Integer.MAX_VALUE);
		} catch (Exception e) {
			MessageHandler.error("Error occured while loading users", e);
			users = Collections.emptyList();
		}
		return null;
	}

	/**
	 * Add new contact for loggedin user
	 * 
	 * @param other
	 * @return
	 */
	public String addContact(User other) {
		try {
			Contact contact = new Contact();
			contact.setCreationDate(new Date());
			contact.setUser(user);
			contact.setOther(other);
			contact = (Contact) chatRepository.save(contact);
			user.getContacts().add(contact);
		} catch (Exception e) {
			MessageHandler.error("Error occurred while adding a contact", e);
		}
		return null;
	}

	/**
	 * Is given user already in loggedin user contacts ?
	 * 
	 * @param other
	 */
	public boolean isAlreadyAdded(User other) {
		if (user.getId().equals(other.getId())) {
			/* loggedin user can see himself on the list but can't add */
			return true;
		}
		boolean ret = false;
		for (Contact contact : user.getContacts()) {
			if (contact.getOther().getId().equals(other.getId())) {
				ret = true;
				break;
			}
		}
		return ret;
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
