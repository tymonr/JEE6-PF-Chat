package org.tymonr.livechat.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tymonr.livechat.exception.MessageHandler;
import org.tymonr.livechat.model.Contact;
import org.tymonr.livechat.model.User;
import org.tymonr.livechat.service.local.ChatRepository;
import org.tymonr.livechat.session.Loggedin;

@Named
@SessionScoped
public class Chat implements Serializable{
	private static final long serialVersionUID = 1358544872182255815L;
	private static final Logger log = LoggerFactory.getLogger(Chat.class);
	
	@Inject
	@Loggedin
	private User user;
	
	@Inject
	private ChatRepository chatRepository;
	
	
	private int count;
	
	@PostConstruct
	public void init(){
		log.trace("Chat bean - initialized");
	}
	
	@PreDestroy
	public void dispose(){
		log.trace("Chat bean - disposed");
	}
	
	public synchronized void increment(){
		count ++;
		
		PushContext pushContext = PushContextFactory.getDefault().getPushContext();
		pushContext.push("/counter", String.valueOf(count));
	}
	
	public String removeContact(Contact contact){
		try{
			chatRepository.remove(contact);
			user.getContacts().remove(contact);
		}catch(Exception e){
			MessageHandler.error("Error while removing contact", e);
		}
		return null;
	}
	
	public List<Contact> getContacts(){
		return new ArrayList<Contact>(user.getContacts());
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	
}
