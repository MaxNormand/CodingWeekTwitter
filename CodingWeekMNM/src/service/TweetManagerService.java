package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import model.TweetWrapper;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.Logger;
import twitter4j.Query;
import twitter4j.Query.Unit;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import dao.TweetManagerDao;

public class TweetManagerService {

	private static final int MAX_QUERY_COUNT = 500;

	private final Logger logger = Logger.getLogger(TweetManagerService.class);

	private Twitter twitter;

	private final TweetManagerDao dao;

	public TweetManagerService(Twitter twitter) {
		dao= new TweetManagerDao();
		this.twitter = twitter;
	}

	/**
	 *
	 * @param hashtag
	 * @return
	 */
	public List<TweetWrapper> getTweetsWithGivenHashtag(String hashtag) {
		return getTweetsWithGivenKeyword("#" + hashtag);
	}

	/**
	 *
	 * @param keyword
	 * @return
	 */
	public List<TweetWrapper> getTweetsWithGivenKeyword(String keyword) {
		final Query query = new Query(keyword);
		List<TweetWrapper> tweets = null;
		try {
			tweets = getAllTweetsFromQuery(query);
		} catch (final TwitterException e) {
			logger.error("Unable to retrieve tweets with hashtag " + keyword, e);
		}
		return tweets;
	}

	/**
	 *
	 * @param query
	 * @return
	 * @throws TwitterException
	 */
	private List<TweetWrapper> getAllTweetsFromQuery(Query query) throws TwitterException {
		long lastID = Long.MAX_VALUE;
		final ArrayList<Status> tweets = new ArrayList<Status>();
		while (tweets.size() < MAX_QUERY_COUNT) {
			if (MAX_QUERY_COUNT - tweets.size() > 100)
				query.setCount(100);
			else
				query.setCount(MAX_QUERY_COUNT - tweets.size());

			final QueryResult result = twitter.search(query);
			tweets.addAll(result.getTweets());
			for (final Status t : tweets)
				if (t.getId() < lastID)
					lastID = t.getId();

			query.setMaxId(lastID - 1);
		}

		return ToTweetList(tweets);
	}

	/**
	 *
	 * @param location
	 * @return
	 */
	public List<TweetWrapper> getTweetsWithGivenLocation(GeoLocation location) {
		final Query query = new Query();
		query.setGeoCode(location, 5.0, Unit.km);
		List<TweetWrapper> tweetList = null;
		try {
			tweetList = getAllTweetsFromQuery(query);
		} catch (final TwitterException e) {
			logger.error("Unable to retrieve tweets with location (latitude=" + location.getLatitude() + "longitude=:"
					+ location.getLongitude() + ")", e);
		}
		return tweetList;
	}

	/**
	 *
	 * @param time
	 * @return
	 */
	public List<TweetWrapper> getTweetsSinceGivenTimeAgo(int time, String keyWord) {
		final Query query = new Query(keyWord);
		final Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR, -time);
		final Date timeBack = cal.getTime();
		query.setCount(MAX_QUERY_COUNT);
		final List<TweetWrapper> tweetList = new ArrayList<>();
		try {
			final List<TweetWrapper> tweets = getAllTweetsFromQuery(query);
			for (final TweetWrapper tweet : tweets) {
				if (tweet.getCreationDate().before(new Date()) && tweet.getCreationDate().after(timeBack)) {
					tweetList.add(tweet);
				}
			}
		} catch (final TwitterException e) {
			logger.error("Unable to retrieve tweets from " + time + "h ago", e);
		}
		return tweetList;
	}

	/**
	 *
	 * @return
	 */
	public List<TweetWrapper> getLatestTweets() {
		List<TweetWrapper> tweetList = null;
		try {
			tweetList = ToTweetList(twitter.getHomeTimeline());
		} catch (final TwitterException e) {
			logger.error("Unable to retrieve latest tweets", e);
		}
		return tweetList;
	}

	/**
	 *
	 * @param author
	 * @return
	 */
	public List<TweetWrapper> getTweetsfromGivenAuthor(String author) {
		final List<TweetWrapper> tweetList = new ArrayList<>();
		try {
			int page = 1;
			List<User> users;
			do {
				users = twitter.searchUsers(author, page);
				for (final User user : users) {
					final Status st = user.getStatus();
					if (st != null) {
						tweetList.add(new TweetWrapper(st.getId(),author, st.getCreatedAt(), st.getText(), st.getPlace().getName(),
								st.getLang(), st.getMediaEntities(), st.getGeoLocation()));
					}
				}
				page++;
			} while (users.size() != 0 && tweetList.size() < MAX_QUERY_COUNT);
		} catch (final TwitterException e) {
			logger.error("Unable to retrieve tweets from" + author, e);
		}
		return tweetList;
	}

	/**
	 *
	 * @param language
	 * @return
	 */
	public List<TweetWrapper> getTweetsInGivenlanguage(Locale locale) {
		locale.getISO3Language();
		final Query query = new Query("lang:" + locale.getLanguage());
		List<TweetWrapper> tweetList = null;
		try {
			tweetList = getAllTweetsFromQuery(query);
		} catch (final TwitterException e) {
			logger.error("Unable to retrieve tweets in" + locale.getDisplayLanguage(), e);
		}
		return tweetList;
	}

	/**
	 *
	 * @param place
	 * @return
	 * @throws TwitterException
	 */
	public List<TweetWrapper> getTweetsWithGivenPlace(String place) throws TwitterException {
		final GeoQuery geoQuery = new GeoQuery((String)null);
		geoQuery.setQuery(place);
		geoQuery.maxResults(1);
		final GeoLocation coordinates = twitter.searchPlaces(geoQuery).get(0).getBoundingBoxCoordinates()[0][0];
		return getTweetsWithGivenLocation(coordinates);
	}

	/**
	 *
	 * @param tweetList
	 * @return
	 */
	public List<TweetWrapper> ToTweetList(List<Status> statusList) {
		final List<TweetWrapper> tweetList = new ArrayList<>();
		for (final Status status : statusList) {
			tweetList.add(new TweetWrapper(status));
		}
		return tweetList;
	}

	/**
	 *
	 * @return
	 */
	public List<TweetWrapper> getTweetStream() {
		final TwitterStream twitterStream = new TwitterStreamFactory().getInstance(twitter.getAuthorization());
		final TweetStreamService streamService = new TweetStreamService();
		return ToTweetList(streamService.getTweetStream(twitterStream));
	}

	public Twitter getTwitter() {
		return twitter;
	}

	public void setTwitter(Twitter twitter) {
		this.twitter = twitter;
	}


	public void saveTweet(TweetWrapper tweet){
		dao.saveTweet(tweet);
	}
	public void saveListTweets(List<TweetWrapper> liste) throws SQLException{
		dao.saveListTweets(liste);
	}
	public List<TweetWrapper> getCollection(String collectionId){
		return dao.getTweets(collectionId);
	}
}
