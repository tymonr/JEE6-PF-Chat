package org.tymonr.livechat.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tymonr.livechat.model.Contact;
import org.tymonr.livechat.model.User;

@Named
@SessionScoped
public class Chat implements Serializable{
	private static final long serialVersionUID = 1358544872182255815L;
	private static final Logger log = LoggerFactory.getLogger(Chat.class);
	
	@PostConstruct
	public void init(){
		log.debug("Chat bean - init");
	}
	
	public List<Contact> getContacts(){
		List<Contact> contacts = new ArrayList<Contact>();
		contacts.add(new Contact(new User("Alice")));
		contacts.add(new Contact(new User("Bob")));
		
		return contacts;
	}

}
