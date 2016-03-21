package model;

import java.util.Date;

import twitter4j.GeoLocation;
import twitter4j.MediaEntity;
import twitter4j.Status;

public class TweetWrapper {
	private long id;
	private String collectionId;
	private String author;
	private Date creationDate;
	private String text;
	private String place;
	private String languageCode;
	private MediaEntity[] media;
	private GeoLocation location;

	public TweetWrapper(Status status) {
		this.id=status.getId();
		this.author = status.getUser().getScreenName();
		this.creationDate = status.getCreatedAt();
		this.text = status.getText();
		this.languageCode = status.getLang();
		this.media = status.getMediaEntities();
		this.place = status.getPlace()!=null?status.getPlace().getName():"";
		this.location = status.getGeoLocation();
	}

	public TweetWrapper(long id,String author, Date creationDate, String text, String place, String languageCode,
			MediaEntity[] media, GeoLocation location) {
		super();
		this.id=id;
		this.author = author;
		this.creationDate = creationDate;
		this.text = text;
		this.place = place;
		this.languageCode = languageCode;
		this.media = media;
		this.location = location;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getText() {
		return text.replaceAll("\\P{Print}", "");
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public MediaEntity[] getMedia() {
		return media;
	}

	public void setMedia(MediaEntity[] media) {
		this.media = media;
	}

	public GeoLocation getLocation() {
		return location;
	}

	public void setLocation(GeoLocation location) {
		this.location = location;
	}

	public String getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}

}
