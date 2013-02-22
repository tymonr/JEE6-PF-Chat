package org.tymonr.livechat.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PERMISSIONS")
public class Permission extends BaseEntity implements Serializable{
	private static final long serialVersionUID = -3230134212759662985L;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	

}
