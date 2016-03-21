package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import model.TweetWrapper;
import service.TweetManagerService;

public class CollectionManagementView extends JPanel {
	TweetManagerService service;
	JLabel collectionLabel;
	JPanel collectionPane;
	JButton deleteCollection, searchCollection;
	JTextField collectionName;

	public CollectionManagementView(TweetManagerService service) {
		this.service=service;
		this.setLayout(new BorderLayout());
		this.setBackground(Color.decode("#c0deed"));
		collectionLabel=new JLabel("COLLECTION NAME");
		collectionLabel.setForeground(Color.white);
		collectionName= new JTextField();
		collectionName.setPreferredSize(new Dimension(100, 25));
		collectionPane= new JPanel();
		collectionPane.setLayout(new BoxLayout(collectionPane, BoxLayout.Y_AXIS));
		collectionPane.setBackground(Color.decode("#c0deed"));
		collectionPane.setBorder(new LineBorder(Color.white, 1));
		collectionPane.setPreferredSize(new Dimension(800, 700));
		collectionPane.setAutoscrolls(true);
		final JScrollPane scrollPane = new JScrollPane(collectionPane);
		deleteCollection= new JButton("Delete collection");
		searchCollection= new JButton("Search collection");
		searchCollection.addActionListener(new SearchButtonListener());
		deleteCollection.addActionListener(new ClearButtonListener());

		final JPanel north= new JPanel();
		north.setBackground(Color.decode("#c0deed"));
		north.add(collectionLabel);
		north.add(collectionName);
		north.add(searchCollection);
		north.add(deleteCollection);

		final JPanel center= new JPanel();
		center.setPreferredSize(new Dimension(800, 700));
		center.setBackground(Color.decode("#c0deed"));
		center.add(scrollPane);
		this.add(north, BorderLayout.NORTH);
		this.add(center, BorderLayout.CENTER);
	}

	public void addTweetsFromList(List<TweetWrapper> tweetList) {
		collectionPane.removeAll();
		for (final TweetWrapper tweet : tweetList) {
			collectionPane.add(new TweetView(tweet,service));
		}
		collectionPane.revalidate();
	}

	class ClearButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			collectionPane.removeAll();
			repaint();

		}
	}

	class SearchButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			addTweetsFromList(service.getCollection(collectionName.getText()));

		}
	}
}


