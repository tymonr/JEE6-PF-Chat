package org.tymonr.livechat.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tymonr.livechat.model.Contact;
import org.tymonr.livechat.model.User;
import org.tymonr.livechat.session.Loggedin;

@Named
@SessionScoped
public class Chat implements Serializable{
	private static final long serialVersionUID = 1358544872182255815L;
	private static final Logger log = LoggerFactory.getLogger(Chat.class);
	
	@Inject
	@Loggedin
	private User user;
	
	@PostConstruct
	public void init(){
		log.trace("Chat bean - initialized");
	}
	
	@PreDestroy
	public void dispose(){
		log.trace("Chat bean - disposed");
	}
	
	public List<Contact> getContacts(){
		return new ArrayList<Contact>(user.getContacts());
	}

}