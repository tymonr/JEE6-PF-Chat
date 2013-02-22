package org.tymonr.livechat.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "CONVERSATIONS")
public class Conversation extends BaseEntity{
	private static final long serialVersionUID = -2564889198214025335L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OWNER")
	private User owner;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECEIVER")
	private User receiver;
	
	@Column(name = "TIME_STARTED")
	private Date timeStarted;
	
	@Column(name = "TIME_ENDED")
	private Date timeEnded;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "conversation")
	private Set<Message> messages;

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public Date getTimeStarted() {
		return timeStarted;
	}

	public void setTimeStarted(Date timeStarted) {
		this.timeStarted = timeStarted;
	}

	public Date getTimeEnded() {
		return timeEnded;
	}

	public void setTimeEnded(Date timeEnded) {
		this.timeEnded = timeEnded;
	}

	public Set<Message> getMessages() {
		return messages;
	}

	public void setMessages(Set<Message> messages) {
		this.messages = messages;
	}
	
	
	
	
}
