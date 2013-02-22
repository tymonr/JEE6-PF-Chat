package org.tymonr.livechat.controller;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;

@Named
@ApplicationScoped
public class Counter implements Serializable{
	private static final long serialVersionUID = 7082490938072889555L;

	private int count;
	public synchronized void increment(){
		count ++;
		
		PushContext pushContext = PushContextFactory.getDefault().getPushContext();
		pushContext.push("/counter", String.valueOf(count));
	}
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
