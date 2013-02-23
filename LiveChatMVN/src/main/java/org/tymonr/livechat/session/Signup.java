package org.tymonr.livechat.session;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.omnifaces.util.Faces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tymonr.livechat.exception.MessageHandler;
import org.tymonr.livechat.model.User;
import org.tymonr.livechat.service.local.UserRepository;

@Named
@RequestScoped
public class Signup {
	private static final Logger log = LoggerFactory.getLogger(Signup.class);

	@Inject
	private UserRepository userRepository;

	private String login;
	private String password;
	private String confirmation;
	private String firstname;
	private String lastname;

	public String submit() {
		User existing = userRepository.userByUsername(login);

		if (existing == null) {
			try {
				log.debug("Creating new user");
				User user = new User();
				user.setUsername(login);
				user.setPassword(password);
				user.setFirstname(firstname);
				user.setLastname(lastname);

				userRepository.save(user);

				SecurityUtils.getSubject().login(
						new UsernamePasswordToken(login, password));
				Faces.redirect("/livechat/ui/chat/chat.xhtml");
				return null;
			} catch (Exception e) {
				MessageHandler.error("Error while signup", e);
				return null;
			}
		} else {
			MessageHandler.info("User with that login already exists");
		}
		return null;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmation() {
		return confirmation;
	}

	public void setConfirmation(String confirmation) {
		this.confirmation = confirmation;
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
