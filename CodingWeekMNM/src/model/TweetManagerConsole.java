package model;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Scanner;

import service.TweetManagerService;
import twitter4j.GeoLocation;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TweetManagerConsole {
	private Twitter twitter;
	private TweetManagerService twitterService = new TweetManagerService(twitter);

	public TweetManagerConsole(TweetManagerService twitterService) {
		this.twitterService = twitterService;
	}

	public void SelectSearch() throws SQLException {

		final Scanner sc = new Scanner(System.in);
		System.out.println("Start a new search by criteria ? : Y/N ");
		String rep = sc.next();
		if (rep.toLowerCase().equals("y")) {
			System.out.println("Search in previous research ? : Y/N ");
			String rep2=sc.next();
			if (rep2.toLowerCase().equals("y")) {
				System.out.println("enter the name of your search");
				String rep3=sc.next();
				List<TweetWrapper> l = twitterService.getCollection(rep3);

				for (int i = 0; i < l.size(); i++) {
					System.out.println(l.get(i).getText());
				}
			}else{

				while (rep.toLowerCase().equals("y")) {
					System.out.println("What do you want to search ?\n"
							+ "1-by hashtag\n2-by language\n3-by author\n4-by keyword\n5-by time period\n6-find latest tweet\n7-by location \n8-by place\n");

					int response = 0;
					try {
						response = sc.nextInt();
					} catch (final InputMismatchException e) {
						System.out.println("Please enter a valid choice");
						rep = sc.next();
					}
					switch (response) {
					case 1:
						getTweetsByHashtag(sc);

						break;
					case 2:
						getTweetsBylanguage(sc);
						break;
					case 3:
						getTweetsByAuthor(sc);
						break;
					case 4:
						getTweetsByKeyword(sc);
						break;
					case 5:
						getTweetsByTimePeriod(sc);
						break;
					case 6:
						getRecentTweets();
						break;
					case 7:
						getTweetsByLocation(sc);
						break;
					case 8:
						getTweetsByPlace(sc);
						break;
					}
					System.out.println("\n Start a new search by criteria ? : Y/N ");
					rep = sc.next();
				}
			}
		}

		else {
			sc.close();
			System.exit(1);
		}
	}

	private void getTweetsByPlace(Scanner sc) {
		System.out.println("Enter place: \n");
		final String place = sc.next();

		List<TweetWrapper> pl = null;
		try {
			pl = twitterService.getTweetsWithGivenPlace(place);
			twitterService.saveListTweets(pl);

		} catch (final TwitterException | SQLException e) {
			System.out.println(e.getMessage());
		}
		if (pl.size() == 0) {
			System.out.println("place doesn't exist");
		}
		for (int i = 0; i < pl.size(); i++) {
			System.out.println(pl.get(i).getText());
		}
	}

	private void getTweetsByLocation(Scanner sc) throws SQLException {
		System.out.println("Enter latitude");
		final String La = sc.next();
		final double la = Double.parseDouble(La);
		System.out.println("Enter longitude");
		final String Lo = sc.next();
		final double lo = Double.parseDouble(Lo);
		final GeoLocation location = new GeoLocation(la, lo);
		final List<TweetWrapper> loc = twitterService.getTweetsWithGivenLocation(location);
		twitterService.saveListTweets(loc);

		if (loc.size() == 0) {
			System.out.println("location doesn't exist");

		}

		for (int i = 0; i < loc.size(); i++) {
			System.out.println(loc.get(i).getText());
		}
	}

	private void getRecentTweets() throws SQLException {
		System.out.println("Latest tweets: \n");
		final List<TweetWrapper> lt = twitterService.getLatestTweets();
		twitterService.saveListTweets(lt);

		for (int i = 0; i < lt.size(); i++) {
			System.out.println(lt.get(i).getText());
		}
	}

	private void getTweetsByTimePeriod(Scanner sc) throws SQLException {
		System.out.println("Enter time period: \n");
		System.out.println("Enter hour: \n");
		final String ti = sc.next();
		final int time = Integer.parseInt(ti);
		System.out.println("keyword research: \n");
		final String keyword = sc.next();
		final List<TweetWrapper> t = twitterService.getTweetsSinceGivenTimeAgo(time, keyword);
		twitterService.saveListTweets(t);

		if (time < 0 || time > 20) {
			System.out.println("time period incorrect");
		}
		for (int i = 0; i < t.size(); i++) {
			System.out.println(t.get(i).getText());
			t.get(i).setCollectionId(keyword);
		}
	}

	private void getTweetsByKeyword(Scanner sc) throws SQLException {
		System.out.println("Enter keyword: \n");
		final String keyword = sc.next();
		final List<TweetWrapper> k = twitterService.getTweetsWithGivenKeyword(keyword);
		twitterService.saveListTweets(k);
		if (k.size() == 0) {
			System.out.println("keyword doesn't exist");

		}
		for (int i = 0; i < k.size(); i++) {
			System.out.println(k.get(i).getText());
			k.get(i).setCollectionId(keyword);
		}
	}

	private void getTweetsByAuthor(Scanner sc) throws SQLException {
		System.out.println("Enter author name: \n");
		final String auth = sc.next();
		final List<TweetWrapper> a = twitterService.getTweetsfromGivenAuthor(auth);
		twitterService.saveListTweets(a);
		if (a.size() == 0) {
			System.out.println("Author doesn't exist");

		}
		for (int i = 0; i < a.size(); i++) {
			System.out.println(a.get(i).getText());
			a.get(i).setCollectionId(auth);
		}
	}

	private void getTweetsBylanguage(Scanner sc) throws SQLException {
		System.out.println("Enter language code: \n");
		final String lang = sc.next();
		List<TweetWrapper> l = new ArrayList<>();
		try {
			l = twitterService.getTweetsInGivenlanguage(new Locale(lang));
			twitterService.saveListTweets(l);
		} catch (final MissingResourceException ex) {
			System.out.println("Language code doesnt exist");
		}
		for (int i = 0; i < l.size(); i++) {
			System.out.println(l.get(i).getText());
			l.get(i).setCollectionId(lang);
		}
	}

	private void getTweetsByHashtag(Scanner sc) throws SQLException {
		System.out.println("Hashtag research: \n");
		final String scan = sc.next();
		final List<TweetWrapper> h = twitterService.getTweetsWithGivenHashtag(scan);
		twitterService.saveListTweets(h);
		if (h.size() == 0) {
			System.out.println("hashtag doesn't exist");

		}
		for (int i = 0; i < h.size(); i++) {
			System.out.println(h.get(i).getText());
			h.get(i).setCollectionId(scan);

		}
	}

}
