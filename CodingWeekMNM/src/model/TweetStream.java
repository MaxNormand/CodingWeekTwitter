package model;

import java.util.ArrayList;
import java.util.List;

public class TweetStream {

	private List<TweetWrapper> tweetStreamList;

	public TweetStream(List<TweetWrapper> tweetStreamList) {
		super();
		this.tweetStreamList = tweetStreamList;
	}

	public TweetStream() {
		tweetStreamList= new ArrayList<>();
	}

	public List<TweetWrapper> getTweetStreamList() {
		return tweetStreamList;
	}

	public void setTweetStreamList(List<TweetWrapper> tweetStreamList) {
		this.tweetStreamList = tweetStreamList;
	}
	
	

}
