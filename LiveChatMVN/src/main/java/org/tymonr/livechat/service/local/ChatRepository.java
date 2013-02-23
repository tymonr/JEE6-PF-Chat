package org.tymonr.livechat.service.local;

import java.util.List;

import javax.ejb.Stateless;

import org.tymonr.livechat.model.Conversation;
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

	public User userByName(String username) {
		checkNotNull(username);
		return getUserDAO().userByUsername(username);
	}

	public List<Message> loadShoutboxMessages(int numberOfMessages) {
		return getUserDAO().loadShoutboxMessages(numberOfMessages);
	}

	public List<Conversation> loadActiveConversations(User user) {
		checkNotNull(user);
		return getUserDAO().loadActiveConversations(user);
	}

	/**
	 * Load messages of given conversation, if conversation has no id set it is
	 * considered as shoutbox and all shoutbox messages (messages without
	 * conversation set) will be pulled instead.
	 * 
	 * @param conversation
	 * @param numberOfMessages maximum number of messages to load.
	 * @return
	 */
	public List<Message> loadConversationMessages(Conversation conversation,
			int numberOfMessages) {
		checkNotNull(conversation);
		return getUserDAO().loadConversationMessages(conversation,
				numberOfMessages);
	}

}
