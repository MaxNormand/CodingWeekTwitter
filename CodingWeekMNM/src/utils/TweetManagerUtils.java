package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import twitter4j.Logger;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public final class TweetManagerUtils {
	private static Logger logger = Logger.getLogger(TweetManagerUtils.class);
	private static Long USER_ID = new Long("0");
	private static String ACCESS_TOKEN_SECRET = null;
	private static String ACCESS_TOKEN = null;
	private static String CONSUMER_KEY_SECRET = null;
	private static String CONSUMER_KEY = null;

	public static Twitter authenticate() {
		try {

			File a = new File("keys.txt");
			FileReader fileReader = new FileReader(a);
			BufferedReader b = new BufferedReader(fileReader);

			USER_ID = new Long(b.readLine());
			ACCESS_TOKEN_SECRET = b.readLine();
			ACCESS_TOKEN = b.readLine();
			CONSUMER_KEY_SECRET = b.readLine();
			CONSUMER_KEY = b.readLine();

			b.close();

		} catch (Exception e) {
		}

		Twitter twitter = TwitterFactory.getSingleton();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
		AccessToken accessToken = new AccessToken(ACCESS_TOKEN, ACCESS_TOKEN_SECRET, USER_ID);
		twitter.setOAuthAccessToken(accessToken);
		try {
			twitter.verifyCredentials().getId();
		} catch (TwitterException e) {
			logger.error("Unable to connect to twitter account", e);
		}
		return twitter;
	}

}
