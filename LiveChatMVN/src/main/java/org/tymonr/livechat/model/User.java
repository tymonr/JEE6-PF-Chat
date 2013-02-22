package org.tymonr.livechat.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
@NamedQuery(name="byUsername", query = "select u from User u where u.username = :username")
public class User extends BaseEntity{
	private static final long serialVersionUID = -7831583513796681576L;
	
	public User(){
	}
	
	public User(String username){
		this.username = username;
	}

	
	@Basic
	@Column(name = "USERNAME")
	private String username;
	
	/* TODO: hash */
	@Basic
	@Column(name = "PASSWORD")
	private String password;
	
	@Basic
	@Column(name = "FIRSTNAME")
	private String firstname;
	
	@Basic
	@Column(name = "LASTNAME")
	private String lastname;
	
	@OneToMany(cascade = CascadeType.ALL, fetch= FetchType.EAGER)
	private Set<Role> roles = new HashSet<Role>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<Contact> contacts = new HashSet<Contact>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "other")
	private Set<Contact> reverseContacts = new HashSet<Contact>();

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

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	
	
	
}
