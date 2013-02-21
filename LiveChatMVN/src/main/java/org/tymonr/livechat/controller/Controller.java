package org.tymonr.livechat.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@SessionScoped
public class Controller implements Serializable{
	private static final long serialVersionUID = -5169800075510492797L;
	private static final Logger log = LoggerFactory.getLogger(Controller.class);
	
	@PostConstruct
	public void init(){
		log.debug("Controller init");
	}
	

}
