package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.TweetWrapper;

public class TweetManagerDao {
	private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	private static final String INSERT_TWEET = "INSERT INTO tweets(tweet_id,author,creationDate, text, place, langageCode,media,location,collection_id,image) VALUES(?,?,?,?,?,?,?,?,?,?);";
	private static final String SELECT_TWEET_BY_COLLECTION = "select * from tweets where collection_id=?";
	private static String URL = null;
	private static String USER = "root";
	private static String PASSWORD = null;
	private static Connection connection = null;

	public TweetManagerDao(){
		connect();
	}


	public static void connect() {
		try {
			File a = new File("keys2.txt");
			FileReader fileReader = new FileReader(a);
			BufferedReader b = new BufferedReader(fileReader);
			PASSWORD=b.readLine();
			URL=b.readLine();
			b.close();
			Class.forName(MYSQL_DRIVER);
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("Connection suceeded!");

		} catch (final Exception e) {
			System.out.println("Oups, something went wrong !");
		}
	}

	public void disConnect() {
		if (connection != null)
			try {
				connection.close();
				System.out.println("connection closed");
			} catch (final SQLException ignore) {
				/*
				 * Si une erreur survient lors de la fermeture, il suffit de
				 * l'ignorer.
				 */
			}
	}

	public void saveTweet(TweetWrapper tweet) {

		try {
			final PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TWEET);
			buildInsertionQuery(tweet, preparedStatement);
			preparedStatement.executeUpdate();

		} catch (final SQLException e) {

		}
	}


	private void buildInsertionQuery(TweetWrapper tweet, final PreparedStatement preparedStatement)
			throws SQLException {
		preparedStatement.setInt(1, (int) tweet.getId());
		preparedStatement.setString(2, tweet.getAuthor());
		preparedStatement.setDate(3, new Date(tweet.getCreationDate().getTime()));
		preparedStatement.setString(4, tweet.getText());
		preparedStatement.setString(5, tweet.getPlace()!=null?tweet.getPlace():null);
		preparedStatement.setString(6, tweet.getLanguageCode());
		preparedStatement.setString(7, null);
		preparedStatement.setString(8, tweet.getLocation()!=null? Double.toString(tweet.getLocation().getLatitude()) + ","
				+ Double.toString(tweet.getLocation().getLongitude()):null);
		preparedStatement.setString(9, tweet.getCollectionId());
		preparedStatement.setString(10, null);
	}

	public void saveListTweets(List<TweetWrapper> liste) throws SQLException{
		for(int i=0; i<liste.size(); i++) {
			TweetWrapper t = liste.get(i);
			saveTweet(t);

		}
	}
	public List<TweetWrapper> getTweets(String collectionId){
		final ArrayList<TweetWrapper> tweetList = new ArrayList<>();
		try {
			final PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TWEET_BY_COLLECTION);
			preparedStatement.setString(1, collectionId);
			final ResultSet rs=preparedStatement.executeQuery();
			while (rs.next()) {
				final TweetWrapper tweet=new TweetWrapper(rs.getLong("tweet_id"), rs.getString("author"), rs.getDate("creationDate"), rs.getString("text"),
						rs.getString("place"), rs.getString("langageCode"), null, null);
				tweetList.add(tweet);
			}

		} catch (final SQLException e) {
			
		}
		return tweetList;
	}
}
