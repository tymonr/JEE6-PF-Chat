package org.tymonr.livechat.exception;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@SessionScoped
public class MessageHandler implements Serializable{
	private static final long serialVersionUID = -2102566098477162729L;
	private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);
	
	
	public static void error(String message, Throwable throwable){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage facesMessage = new FacesMessage(message);
		facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
		context.addMessage("errorGrowl", facesMessage);
//		context.validationFailed();
		log.error(message, throwable);
	}

}
