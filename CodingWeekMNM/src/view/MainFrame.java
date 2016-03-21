package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import model.TweetStream;
import model.TweetWrapper;
import service.TweetManagerService;

public class MainFrame extends JFrame {
	TweetManagerService twitterService;
	JMenuItem about, quit;
	JPanel streamView;
	JPanel criteriaView;
	JPanel tweetPane;
	JTabbedPane tabPane;
	JPanel collectionView;


	public MainFrame(TweetManagerService twitterService) {
		this.twitterService = twitterService;
		this.setLayout(new BorderLayout());
		this.setBackground(Color.decode("#c0deed"));
		this.setTitle("Crazy Tweet Manager");
		this.setSize(1600, 850);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);


		// MenuBar
		final JMenuBar bar = new JMenuBar();
		final JMenu menu = new JMenu("Menu");
		quit = new JMenuItem("Quit");
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		about = new JMenuItem("About");
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"Ceci est une application de collecte et de traitements de tweet\n"
								+ "Developpers: Team codingMNM\n" + "Project : Coding week\n"
								+ "Contacts: contact@telecomnancy.net\n",
								"About", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		tabPane= new JTabbedPane();
		//tabPane.setLayout(new BorderLayout());
		this.add(tabPane, BorderLayout.CENTER);
		menu.add(quit);
		menu.add(about);
		bar.add(menu);
		this.setJMenuBar(bar);
		initCriteriaView();
		initCollectionManagementView();
		initStreamView();
	}

	public void initStreamView() {
		this.streamView = new StreamView();
		final TweetStream stream = new TweetStream();
		stream.setTweetStreamList(twitterService.getLatestTweets());
		tweetPane = new JPanel();
		tweetPane.setLayout(new BoxLayout(tweetPane, BoxLayout.Y_AXIS));
		tweetPane.setAutoscrolls(true);
		addTweetsFromList(tweetPane, stream.getTweetStreamList());
		final JScrollPane scrollFrame = new JScrollPane(tweetPane);
		streamView.add(scrollFrame, BorderLayout.CENTER);
		((StreamView) streamView).liveUpdates.addActionListener(new RefreshFeedListener());
		tabPane.addTab("Twitter Stream",streamView);
		//tabPane.addTab("COLLECTION", new ColorsPanel());
		this.setVisible(true);
		validate();
	}

	public void initCriteriaView() {
		this.criteriaView = new CriteriaView(twitterService);
		tabPane.addTab("Collect tweets", criteriaView);
		this.setVisible(true);
		validate();
	}

	public void initCollectionManagementView() {
		this.collectionView = new CollectionManagementView(twitterService);
		tabPane.addTab("Managed collected tweets", collectionView);
		this.setVisible(true);
		validate();
	}


	public void addTweetsFromList(JPanel panel, List<TweetWrapper> tweetList) {
		panel.removeAll();
		for (final TweetWrapper tweet : tweetList) {
			panel.add(new TweetView(tweet,twitterService));
		}
		revalidate();
	}

	class RefreshFeedListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			((StreamView) streamView).stream.getTweetStreamList();
			final Thread newThread = new Thread() {
				@Override
				public void run() {
					while (true) {
						final TweetWrapper tweet = twitterService.getTweetStream().get(0);
						tweetPane.add(new TweetView(tweet,twitterService),0);
						revalidate();
					}
				}
			};
			newThread.start();
		}

	}

}
