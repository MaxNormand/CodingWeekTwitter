package service;

import java.util.ArrayList;
import java.util.List;

import twitter4j.DirectMessage;
import twitter4j.Logger;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.TwitterStream;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;

public final class TweetStreamService  {
	private Logger logger = Logger.getLogger(TweetManagerService.class);
	
	private final Object lock = new Object();

	public List<Status> getTweetStream(TwitterStream twitterStream) {

		final List<Status> statuses = new ArrayList<Status>();

		final UserStreamListener listener = new UserStreamListener() {
			@Override
			public void onStatus(Status status) {
				statuses.add(status);
				System.out.println(status.getText());
				if (statuses.size() == 1) {
					synchronized (lock) {
						lock.notify();
					}
				}
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTrackLimitationNotice(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onException(Exception arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onBlock(User arg0, User arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDeletionNotice(long arg0, long arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDirectMessage(DirectMessage arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFavorite(User arg0, User arg1, Status arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFavoritedRetweet(User arg0, User arg1, Status arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFollow(User arg0, User arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFriendList(long[] arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onQuotedTweet(User arg0, User arg1, Status arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onRetweetedRetweet(User arg0, User arg1, Status arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUnblock(User arg0, User arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUnfavorite(User arg0, User arg1, Status arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUnfollow(User arg0, User arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUserDeletion(long arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUserListCreation(User arg0, UserList arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUserListDeletion(User arg0, UserList arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUserListMemberAddition(User arg0, User arg1, UserList arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUserListMemberDeletion(User arg0, User arg1, UserList arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUserListSubscription(User arg0, User arg1, UserList arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUserListUnsubscription(User arg0, User arg1, UserList arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUserListUpdate(User arg0, UserList arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUserProfileUpdate(User arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUserSuspension(long arg0) {
				// TODO Auto-generated method stub

			}
		};
		twitterStream.addListener(listener);
		twitterStream.user();

		try {
			synchronized (lock) {
				lock.wait();
			}
		} catch (InterruptedException e) {
			logger.error("unable to retrieve tweet stream", e);
		}
		twitterStream.shutdown();
		return statuses;
	}
}