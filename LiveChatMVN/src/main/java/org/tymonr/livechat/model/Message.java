package org.tymonr.livechat.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "MESSAGES")
public class Message extends BaseEntity{
	private static final long serialVersionUID = 2259935515088345163L;
	
	@Column(name = "CONTENT")
	private String content;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AUTHOR")
	private User author;
	
	@Column(name = "TIME_SENT")
	private Date timeSent;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONVERSATION")
	private Conversation conversation;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Date getTimeSent() {
		return timeSent;
	}

	public void setTimeSent(Date timeSent) {
		this.timeSent = timeSent;
	}

	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	
	
}
