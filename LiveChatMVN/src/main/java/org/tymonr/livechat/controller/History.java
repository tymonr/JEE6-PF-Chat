package org.tymonr.livechat.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tymonr.livechat.exception.MessageHandler;
import org.tymonr.livechat.model.Contact;
import org.tymonr.livechat.model.Conversation;
import org.tymonr.livechat.model.Message;
import org.tymonr.livechat.model.User;
import org.tymonr.livechat.model.filter.ConversationFilter;
import org.tymonr.livechat.service.local.ChatRepository;
import org.tymonr.livechat.session.Loggedin;

/* TODO: 
 * 1. see Chat.java
 * 2. to viewScope && cleanup after dialog close
 */
/**
 * Conversations history controller
 */
/**
 * Controller for loggedin user conversations history.
 * 
 */
@Named
@SessionScoped
public class History implements Serializable {
	private static final long serialVersionUID = -679514164751015685L;
	private static final Logger log = LoggerFactory.getLogger(History.class);

	@Inject
	@Loggedin
	private User user;

	@Inject
	private ChatRepository chatRepository;

	private List<Conversation> conversations;
	private List<Message> messages;

	/**
	 * Loads conversations for given contact
	 * 
	 * @param contact
	 * @return
	 */
	public String showConversationsHistory(Contact contact) {
		try {
			log.debug("Showing conversations history for "
					+ contact.getOther().getUsername());

			ConversationFilter filter = new ConversationFilter();
			filter.setOwner(user);
			filter.setReceiver(contact.getOther());
			conversations = chatRepository.loadConversations(filter, 0,
					Integer.MAX_VALUE);

		} catch (Exception e) {
			MessageHandler.error("Error while loading conversations list", e);
		}
		return null;
	}

	/**
	 * Load messages for given conversation, Invoked to show messages of
	 * selected conversation.
	 * 
	 * @param conversation
	 * @return
	 */
	public String showMessages(Conversation conversation) {
		try {
			messages = chatRepository.loadConversationMessages(conversation,
					Integer.MAX_VALUE);
		} catch (Exception e) {
			MessageHandler.error("Error while loading messages", e);
		}
		return null;
	}

	public List<Conversation> getConversations() {
		if (conversations == null) {
			conversations = new ArrayList<Conversation>();
		}
		return conversations;
	}

	public void setConversations(List<Conversation> conversations) {
		this.conversations = conversations;
	}

	public List<Message> getMessages() {
		if (messages == null) {
			messages = new ArrayList<Message>();
		}
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

}
