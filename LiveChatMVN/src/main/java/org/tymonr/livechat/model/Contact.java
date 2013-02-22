package org.tymonr.livechat.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Table(name = "CONTACTS")
public class Contact extends BaseEntity implements Serializable{
	private static final long serialVersionUID = 553111498777373987L;
	
	public Contact(){
	}
	
	public Contact(User other){
		this.other = other;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER")
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OTHER")
	private User other;
	
	@Column(name = "BLOCKED")
	private boolean blocked;
	
	@Column(name = "CREATION_DATE")
	private Date creationDate;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getOther() {
		return other;
	}

	public void setOther(User other) {
		this.other = other;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	
	
	
}
