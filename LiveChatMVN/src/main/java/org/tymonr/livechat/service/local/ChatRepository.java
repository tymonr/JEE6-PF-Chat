package org.tymonr.livechat.service.local;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import javax.ejb.Stateless;

import org.tymonr.livechat.mode.filter.ConversationFilter;
import org.tymonr.livechat.model.Conversation;
import org.tymonr.livechat.model.Message;
import org.tymonr.livechat.model.User;
import org.tymonr.livechat.model.filter.UserFilter;

@Stateless
public class ChatRepository extends LocalService {

	/**
	 * Search for users with query by example filter
	 * 
	 * @param filter
	 * @param startIndex
	 * @param maxResults
	 * @return
	 */
	public List<User> findUsers(UserFilter filter, int startIndex,
			int maxResults) {
		List<User> result = getUserDAO().findUsers(filter, startIndex,
				maxResults);
		return result;
	}

	/**
	 * Load user by username,- with contacts
	 * 
	 * @param username
	 * @return
	 */
	public User userByName(String username) {
		checkNotNull(username);
		return getUserDAO().userByUsername(username);
	}

	@Deprecated
	public List<Message> loadShoutboxMessages(int numberOfMessages) {
		return getUserDAO().loadShoutboxMessages(numberOfMessages);
	}

	/**
	 * Load active conversations (ones without end time set) for given user.
	 * 
	 * @param user
	 * @return
	 */
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
	 * @param numberOfMessages
	 *            maximum number of messages to load.
	 * @return
	 */
	public List<Message> loadConversationMessages(Conversation conversation,
			int numberOfMessages) {
		checkNotNull(conversation);
		return getUserDAO().loadConversationMessages(conversation,
				numberOfMessages);
	}

	/**
	 * Load conversations with query by example filter
	 * 
	 * @param filter
	 * @param startIndex
	 * @param maxResults
	 * @return
	 */
	public List<Conversation> loadConversations(ConversationFilter filter,
			int startIndex, int maxResults) {

		return getUserDAO().loadConversations(filter, startIndex, maxResults);
	}

}
