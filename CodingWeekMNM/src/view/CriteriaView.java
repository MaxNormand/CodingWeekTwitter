package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import model.TweetWrapper;
import service.TweetManagerService;
import twitter4j.GeoLocation;
import twitter4j.TwitterException;

public class CriteriaView extends JPanel {
	JButton searchButton, clear,save;
	JComboBox<String> criteriaChoice;
	JTextField input;
	JScrollPane scrollableResults;
	JPanel searchPane,resultPane;
	JLabel title;
	JLabel label;
	TweetManagerService tweetManagerService;

	public CriteriaView(TweetManagerService tweetService) {
		this.tweetManagerService = tweetService;
		final String[] searchCriteria = { "Tweets By Hashtag", "Tweets By language", "Tweets By Author", "Tweets By Keyword",
				"Tweets By Time Period", "Recent Tweets", "Tweets By Location", "Tweets By Place" };
		this.setBackground(Color.decode("#c0deed"));
		this.setPreferredSize(new Dimension(700, 800));
		this.setLayout(new BorderLayout());

		criteriaChoice = new JComboBox<String>(searchCriteria);
		input = new JTextField();
		input.setPreferredSize(new Dimension(200, 25));
		searchButton = new JButton("Search");
		clear = new JButton("clear");
		save=new JButton("SaveSearch");
		label = new JLabel("Choose your search criteria:");
		title = new JLabel("Search Results");
		title.setForeground(Color.white);

		searchPane = new JPanel();
		searchPane.add(label);
		searchPane.add(criteriaChoice);
		searchPane.add(input);
		searchPane.add(searchButton);
		searchPane.add(clear);
		searchPane.add(save);
		searchPane.setPreferredSize(new Dimension(700, 100));
		searchPane.setBackground(Color.decode("#c0deed"));
		//searchPane.add(title);

		this.add(Box.createRigidArea(new Dimension(700, 220)));

		resultPane = new JPanel();
		resultPane.setAutoscrolls(true);
		resultPane.setLayout(new BoxLayout(resultPane, BoxLayout.Y_AXIS));
		resultPane.setBackground(Color.decode("#c0deed"));
		resultPane.setBorder(new LineBorder(Color.white, 1));
		scrollableResults = new JScrollPane(resultPane);

		this.add(searchPane, BorderLayout.NORTH);
		this.add(scrollableResults, BorderLayout.CENTER);
		addListeners();
	}

	public void addListeners() {

		SaveSearchListener x=new SaveSearchListener();
		searchButton.addActionListener(x.new SearchButtonListener());
		clear.addActionListener(new ClearButtonListener());
		save.addActionListener(new SaveSearchListener());
	}

	public void addTweetsFromList(List<TweetWrapper> tweetList) {
		resultPane.removeAll();;
		for (final TweetWrapper tweet : tweetList) {
			resultPane.add(new TweetView(tweet,tweetManagerService));
		}
		resultPane.revalidate();
	}

	class ClearButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			resultPane.removeAll();
			revalidate();

		}

	}
	class SaveSearchListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			final int confirmation = JOptionPane.PLAIN_MESSAGE;
			JOptionPane.showConfirmDialog (null, "The search has been succesfully saved :)","",confirmation);
			final int choice = criteriaChoice.getSelectedIndex();
			final String parameter = input.getText();

			switch (choice) {
			case 0:
				hashtag(parameter);
				break;
			case 1:
				Language(parameter);
				break;
			case 2:
				author(parameter);
				break;
			case 3:
				keyword(parameter);
				break;
			case 4:
				time(parameter);
				break;
			case 5:
				latestTweets();
				break;
			case 6:
				localisation(parameter);
				break;
			case 7:
				place(parameter);
				break;
			}


		}

		private void place(final String parameter) {
			try {
				List<TweetWrapper> p=tweetManagerService.getTweetsWithGivenPlace(parameter);
				addTweetsFromList(p);
				for(int i=0;i<p.size();i++){
					p.get(i).setCollectionId(parameter);
				}
				try {
					tweetManagerService.saveListTweets(p);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (final TwitterException e) {
				System.out.println("Place doesn't exist");
			}
		}

		private void localisation(final String parameter) {
			final String[] coordinates = parameter.split(",");
			final String regex_double = "[0-9]{1,13}(\\.[0-9]*)?";
			if (coordinates[0].matches(regex_double) && coordinates[1].matches(regex_double)) {
				List<TweetWrapper> l=tweetManagerService.getTweetsWithGivenLocation(
						new GeoLocation(Double.valueOf(coordinates[0]), Double.valueOf(coordinates[1])));
				addTweetsFromList(l);
				for(int i=0;i<l.size();i++){
					l.get(i).setCollectionId(parameter);
				}
				try {
					tweetManagerService.saveListTweets(l);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		private void latestTweets() {
			List<TweetWrapper> tl=tweetManagerService.getLatestTweets();
			addTweetsFromList(tl);

			try {
				tweetManagerService.saveListTweets(tl);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void time(final String parameter) {
			final String[] parameters = parameter.split(",");
			if (parameters[0].matches("[0-9]*")) {
				List<TweetWrapper> t=tweetManagerService.getTweetsSinceGivenTimeAgo(Integer.parseInt(parameters[0]), parameters[1]);
				addTweetsFromList(t);
				for(int i=0;i<t.size();i++){
					t.get(i).setCollectionId(parameter);
				}
				try {
					tweetManagerService.saveListTweets(t);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		private void keyword(final String parameter) {
			List<TweetWrapper> k=tweetManagerService.getTweetsWithGivenKeyword(parameter);
			addTweetsFromList(k);
			for(int i=0;i<k.size();i++){
				k.get(i).setCollectionId(parameter);
			}
			try {
				tweetManagerService.saveListTweets(k);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void author(final String parameter) {
			List<TweetWrapper> a=tweetManagerService.getTweetsfromGivenAuthor(parameter);
			addTweetsFromList(a);
			for(int i=0;i<a.size();i++){
				a.get(i).setCollectionId(parameter);
			}
			try {
				tweetManagerService.saveListTweets(a);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void Language(final String parameter) {
			if (parameter.matches("[a-zA-Z]{2}")) {
				List<TweetWrapper> l=tweetManagerService.getTweetsInGivenlanguage(new Locale(parameter));
				addTweetsFromList(l);
				for(int i=0;i<l.size();i++){
					l.get(i).setCollectionId(parameter);
				}
				try {
					tweetManagerService.saveListTweets(l);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		private void hashtag(final String parameter) {
			List<TweetWrapper> h = tweetManagerService.getTweetsWithGivenHashtag(parameter);
			for(int i=0;i<h.size();i++){
				h.get(i).setCollectionId(parameter);

			}
			try {
				tweetManagerService.saveListTweets(h);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			addTweetsFromList(h);
		}

		class SearchButtonListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				final int choice = criteriaChoice.getSelectedIndex();
				final String parameter = input.getText();
				switch (choice) {
				case 0:
					addTweetsFromList(tweetManagerService.getTweetsWithGivenHashtag(parameter));
					break;
				case 1:
					if (parameter.matches("[a-zA-Z]{2}")) {
						addTweetsFromList(tweetManagerService.getTweetsInGivenlanguage(new Locale(parameter)));
					}
					break;
				case 2:
					addTweetsFromList(tweetManagerService.getTweetsfromGivenAuthor(parameter));
					break;
				case 3:
					addTweetsFromList(tweetManagerService.getTweetsWithGivenKeyword(parameter));
					break;
				case 4:
					final String[] parameters = parameter.split(",");
					if (parameters[0].matches("[0-9]*")) {
						addTweetsFromList(tweetManagerService.getTweetsSinceGivenTimeAgo(Integer.parseInt(parameters[0]), parameters[1]));
					}
					break;
				case 5:
					addTweetsFromList(tweetManagerService.getLatestTweets());
					break;
				case 6:
					final String[] coordinates = parameter.split(",");
					final String regex_double = "[0-9]{1,13}(\\.[0-9]*)?";
					if (coordinates[0].matches(regex_double) && coordinates[1].matches(regex_double)) {
						addTweetsFromList(tweetManagerService.getTweetsWithGivenLocation(
								new GeoLocation(Double.valueOf(coordinates[0]), Double.valueOf(coordinates[1]))));
					}
					break;
				case 7:
					try {
						addTweetsFromList(tweetManagerService.getTweetsWithGivenPlace(parameter));
					} catch (final TwitterException e) {
						System.out.println("Place doesn't exist");
					}
					break;
				}

			}

		}
	}
}
