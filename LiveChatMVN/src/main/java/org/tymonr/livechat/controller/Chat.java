package org.tymonr.livechat.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;
import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tymonr.livechat.exception.MessageHandler;
import org.tymonr.livechat.model.Contact;
import org.tymonr.livechat.model.Conversation;
import org.tymonr.livechat.model.Message;
import org.tymonr.livechat.model.User;
import org.tymonr.livechat.service.local.ChatRepository;
import org.tymonr.livechat.session.Loggedin;
import org.tymonr.livechat.utils.json.JSONBuilder;

/* TODO: 
 * 1. When switched to JSF2.2 change scope to view to get rid
 * of data initialisation problems on get requests (mostly page refreshes).
 * 
 * 2. Break down to smaller classes, this one starts to feel a bit too fat.
 */
/** Main chat controller */
@Named
@SessionScoped
public class Chat implements Serializable {
	private static final long serialVersionUID = 1358544872182255815L;
	private static final Logger log = LoggerFactory
			.getLogger(Chat.class);

	/**
	 * Maximum number of messages to pull for each active conversation when the
	 * chat is initialised (including shoutbox).
	 */
	private static final int MAX_MESSAGES = 50;

	/** Title of shoutbox conversation tab */
	private static final String SHOUTBOX = "Shoutbox";

	/** Formatter for date put in front of each sent message */
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"HH:mm:ss");

	private static final String DEFAULT_CHANNEL = "/chat";

	/**
	 * Markers to be send to client, indicating that a new conversation
	 * started/ended/new message was sent
	 */
	private static final String EVENT_NEW_CONVERSATION = "ENC";
	private static final String EVENT_END_CONVERSATION = "EEC";
	private static final String EVENT_STATUS_CHANGE = "ESC";
	private static final String EVEN_NEW_MESSAGE = "ENM";

	/**
	 * PrimeFaces server push context - WebSocket (if available) or
	 * Comet/Long-pooling
	 */
	private final PushContext pushContext = PushContextFactory.getDefault()
			.getPushContext();

	@Inject
	@Loggedin
	private User user;

	@Inject
	private ChatRepository chatRepository;

	/** Plaintext message to be sent (this came from the input component) */
	private String message;

	/** List of active conversations for loggedin user */
	private List<Conversation> conversations;

	/** Conversation that is currently selected and visible */
	private Conversation activeConversation;

	private Map<Conversation, List<Message>> conversationMessageMap;

	/** Status of loggedin user */
	private String status;

	@PostConstruct
	public void init() {
		loadConversations();
		if (StringUtils.isBlank(user.getStatus())) {
			status = "Online";
		} else {
			status = user.getStatus();
		}
		log.trace("Chat bean - initialized");
	}

	@PreDestroy
	public void dispose() {
		log.trace("Chat bean - disposed");
	}

	/* actions */

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
			Date date = new Date();
			String escapedMessage = StringEscapeUtils.escapeHtml(message);

			JSONBuilder builder = new JSONBuilder();
			builder.add("event", EVEN_NEW_MESSAGE);
			builder.add("content", escapedMessage);
			builder.add("date", formatedDate(date));
			builder.add("user", user.getUsername());
			builder.add("conversationId", conversationId(activeConversation));
			pushContext.push(DEFAULT_CHANNEL, builder.toString());

			Message messageEntity = new Message();
			messageEntity.setAuthor(user);
			messageEntity.setTimeSent(date);
			messageEntity.setContent(escapedMessage);
			/*
			 * Messages without conversation set will be considered to belong to
			 * shoutbox.
			 */
			if (activeConversation.getId() == null) {
				messageEntity.setConversation(null);
			} else {
				messageEntity.setConversation(activeConversation);
			}
			chatRepository.save(messageEntity);

			message = null;
		} catch (Exception e) {
			MessageHandler.error("Error while sending a message", e);
		}
		return null;
	}

	public String startConversation(Contact contact) {
		try {
			log.debug("Starting conversation with "
					+ contact.getOther().getUsername());
			Conversation conversation = new Conversation();
			conversation.setOwner(user);
			conversation.setReceiver(contact.getOther());
			conversation.setTimeStarted(new Date());

			conversation = (Conversation) chatRepository.save(conversation);
			conversations.add(1, conversation);

			JSONBuilder builder = new JSONBuilder();
			builder.add("event", EVENT_NEW_CONVERSATION);
			pushContext.push(DEFAULT_CHANNEL, builder.toString());
		} catch (Exception e) {
			MessageHandler.error("Error while starting new conversation", e);
		}
		return null;
	}

	public void reloadData() {
		loadConversations();
	}

	/* event handlers */

	public void endConversation(TabCloseEvent event) {
		try {
			Conversation conversation = (Conversation) event.getData();
			conversation.setTimeEnded(new Date());
			chatRepository.save(conversation);
			conversations.remove(conversation);

			JSONBuilder builder = new JSONBuilder();
			builder.add("event", EVENT_END_CONVERSATION);
			pushContext.push(DEFAULT_CHANNEL, builder.toString());
		} catch (Exception e) {
			MessageHandler.error("Error while ending conversation", e);
		}
	}

	public void switchActiveConversation(TabChangeEvent event) {
		activeConversation = (Conversation) event.getData();
		log.debug("switching active conversation to " + activeConversation);
	}

	public void statusChanged() {
		log.debug("Status was changed to: " + status);
		try {
			user.setStatus(status);
			chatRepository.save(user);

			JSONBuilder builder = new JSONBuilder();
			builder.add("event", EVENT_STATUS_CHANGE);
			builder.add("user", user.getUsername());
			builder.add("status", status);
			pushContext.push(DEFAULT_CHANNEL, builder.toString());
		} catch (Exception e) {
			MessageHandler.error("Error while changing status", e);
		}
	}

	/* helper methods */

	public List<Message> messagesOfConversation(Conversation conversation) {
		return conversationMessageMap.get(conversation);
	}

	public String conversationTitle(Conversation conversation) {
		if (conversation.getId() == null) {
			return SHOUTBOX;
		}
		String title = "???";
		if (conversation.getOwner().getId().equals(user.getId())) {
			title = conversation.getReceiver().getUsername();
		} else {
			title = conversation.getOwner().getUsername();
		}
		return title;
	}

	public boolean isTabClosable(Conversation conversation) {
		if (conversation.getId() == null) {
			return false;
		}
		return true;
	}

	public String formatedDate(Date date) {
		return dateFormatter.format(date);
	}

	public String conversationId(Conversation conversation) {
		if (conversation == null) {
			return null;
		}
		return conversation.getId() == null ? "0" : conversation.getId()
				.toString();
	}

	private void loadActiveConversations() {
		try {
			List<Conversation> list = chatRepository
					.loadActiveConversations(user);
			conversations.addAll(list);
		} catch (Exception e) {
			MessageHandler.error("Error while loading active conversations", e);
		}
	}

	private void loadConversations() {
		conversations = new ArrayList<Conversation>();
		Conversation shoutbox = new Conversation();
		conversations.add(shoutbox);
		loadActiveConversations();
		activeConversation = shoutbox;

		conversationMessageMap = new HashMap<Conversation, List<Message>>();
		for (Conversation conversation : conversations) {
			List<Message> list = chatRepository.loadConversationMessages(
					conversation, MAX_MESSAGES);
			Collections.reverse(list);
			conversationMessageMap.put(conversation, list);
		}

	}

	/* get / set */

	public List<Contact> getContacts() {
		return new ArrayList<Contact>(user.getContacts());
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Conversation> getConversations() {
		return conversations;
	}

	public void setConversations(List<Conversation> conversations) {
		this.conversations = conversations;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
