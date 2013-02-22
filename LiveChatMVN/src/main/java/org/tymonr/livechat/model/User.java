package org.tymonr.livechat.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
@NamedQuery(name="findUser", query = "select u from User u where u.username = :username")
public class User implements Serializable{
	private static final long serialVersionUID = -7831583513796681576L;
	
	public User(){
	}
	
	public User(String username){
		this.username = username;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Basic
	@Column(name = "USERNAME")
	private String username;
	@Basic
	@Column(name = "PASSWORD")
	private String password;
	
	@OneToMany(cascade = CascadeType.ALL, fetch= FetchType.EAGER)
	private Set<Role> roles = new HashSet<Role>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<Contact> contacts = new HashSet<Contact>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "other")
	private Set<Contact> reverseContacts = new HashSet<Contact>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
	}

	public Set<Contact> getReverseContacts() {
		return reverseContacts;
	}

	public void setReverseContacts(Set<Contact> reverseContacts) {
		this.reverseContacts = reverseContacts;
	}
	
	
	
	
}
