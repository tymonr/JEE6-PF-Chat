package org.tymonr.livechat.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringEscapeUtils;
import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tymonr.livechat.exception.MessageHandler;
import org.tymonr.livechat.model.Contact;
import org.tymonr.livechat.model.Message;
import org.tymonr.livechat.model.User;
import org.tymonr.livechat.service.local.ChatRepository;
import org.tymonr.livechat.session.Loggedin;

@Named
@SessionScoped
public class Chat implements Serializable {
	private static final long serialVersionUID = 1358544872182255815L;
	private static final Logger log = LoggerFactory.getLogger(Chat.class);
	private static final SimpleDateFormat dateFormater = new SimpleDateFormat(
			"HH:mm:ss");

	@Inject
	@Loggedin
	private User user;

	@Inject
	private ChatRepository chatRepository;

	/** last n messages from shoutbox */
	private List<Message> messages;
	private String message;

	@PostConstruct
	public void init() {
		log.trace("Chat bean - initialized");
	}

	@PreDestroy
	public void dispose() {
		log.trace("Chat bean - disposed");
	}

	public String removeContact(Contact contact) {
		try {
			chatRepository.remove(contact);
			user.getContacts().remove(contact);
		} catch (Exception e) {
			MessageHandler.error("Error while removing contact", e);
		}
		return null;
	}

	public String sendMessage() {
		try {
			PushContext pushContext = PushContextFactory.getDefault()
					.getPushContext();
			Date date = new Date();
			String escapedMessage = StringEscapeUtils.escapeHtml(message);

			pushContext.push("/chat", formatedMessage(date, escapedMessage));

			Message messageEntity = new Message();
			messageEntity.setAuthor(user);
			messageEntity.setTimeSent(date);
			messageEntity.setContent(escapedMessage);
			/*
			 * Messages without conversation set will be considered to belong to
			 * shoutbox.
			 */
			messageEntity.setConversation(null);
			chatRepository.save(messageEntity);

			message = null;
		} catch (Exception e) {
			MessageHandler.error("Error while sending a message", e);
		}
		return null;
	}
	
	public String formatedDate(Date date){
		return dateFormater.format(date);
	}

	private String formatedMessage(Date date, String message) {

		StringBuilder msg = new StringBuilder();
		msg.append("<span class=\"message-date\">");
		msg.append("[" + dateFormater.format(date) + "] ");
		msg.append("</span>");

		msg.append("<span class=\"message-author\">");
		msg.append(user.getUsername());
		msg.append("</span>");
		msg.append(": ");
		msg.append("<span class=\"message-content\">");
		msg.append(message);
		msg.append("</span>");
		return msg.toString();
	}
	
	private void loadShoutboxMessages(){
		List<Message> list = chatRepository.loadShoutboxMessages(50);
		Collections.reverse(list);
		messages = list;
	}

	public List<Contact> getContacts() {
		return new ArrayList<Contact>(user.getContacts());
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Message> getMessages() {
		if(messages == null){
			loadShoutboxMessages();
		}
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	
}
