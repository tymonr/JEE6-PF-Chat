package org.tymonr.livechat.session;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.apache.shiro.SecurityUtils;
import org.omnifaces.util.Faces;

@Named
@RequestScoped
public class Logout {
	public void submit() throws IOException{
		SecurityUtils.getSubject().logout();
		Faces.invalidateSession();
		Faces.redirect("/livechat/ui/chat/chat.xhtml");
	}
}
