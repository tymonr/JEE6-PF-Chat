package org.tymonr.livechat.service.local;

import java.util.List;

import javax.ejb.Stateless;

import org.tymonr.livechat.model.Message;
import org.tymonr.livechat.model.User;
import org.tymonr.livechat.model.filter.UserFilter;
import static com.google.common.base.Preconditions.checkNotNull;

@Stateless
public class ChatRepository extends LocalService {

	public List<User> findUsers(UserFilter filter, int startIndex,
			int maxResults) {

		/**
		 * Search for users with query by example filter
		 * 
		 * @param filter
		 * @param startIndex
		 * @param maxResults
		 * @return
		 */
		List<User> result = getUserDAO().findUsers(filter, startIndex,
				maxResults);

		return result;
	}
	
	public User userByName(String username){
		checkNotNull(username);
		
		return getUserDAO().userByUsername(username);
	}

	public List<Message> loadShoutboxMessages(int numberOfMessages) {
		return getUserDAO().loadShoutboxMessages(numberOfMessages);
	}

}
