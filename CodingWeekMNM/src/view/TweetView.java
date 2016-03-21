package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import model.TweetWrapper;
import service.TweetManagerService;

public class TweetView extends JButton {
	TweetWrapper tweet;
	JLabel tweetText;
	JLabel tweetAuthor;
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.FRANCE);
	JPopupMenu popupMenu;
	JMenuItem saveButton;
	JMenuItem col1;
	JMenuItem col2;
	JMenuItem col3;
	TweetManagerService tweetManagerService;

	public TweetView(TweetWrapper tweet, TweetManagerService service) {
		this.tweet=tweet;
		this.tweetManagerService=service;
		popupMenu = new JPopupMenu();
		saveButton = new JMenuItem("Enregistrer");
		saveButton.setHorizontalTextPosition(JMenuItem.RIGHT);
		saveButton.addActionListener(new SaveListener());
		popupMenu.add(saveButton);
		popupMenu.setBorder(new BevelBorder(BevelBorder.RAISED));
		//popupMenu.addMouseListener(new MousePopupListener());
		this.setComponentPopupMenu(popupMenu);
		this.setLayout(new BorderLayout());
		this.setBorder(new LineBorder(Color.decode("#c0deed"), 1));
		this.setBackground(Color.white);
		this.setPreferredSize(new Dimension(50, 50));
		this.setToolTipText(tweet.getText());
		tweetAuthor = new JLabel(sdf.format(tweet.getCreationDate()) + " by " + tweet.getAuthor() + ":");
		tweetText = new JLabel(tweet.getText());
		tweetAuthor.setForeground(Color.decode("#00aced"));
		tweetText.setForeground(Color.black);
		this.add(tweetAuthor, BorderLayout.NORTH);
		this.add(tweetText, BorderLayout.SOUTH);
		this.addActionListener(new SaveListener());
	}

	class SaveListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			tweetManagerService.saveTweet(tweet);
			final int confirmation = JOptionPane.PLAIN_MESSAGE;
			JOptionPane.showConfirmDialog (null, "The tweet has been succesfully saved :)","",confirmation);
		}

	}

	class addListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.out.println("added");

		}

	}

	class MousePopupListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			checkPopup(e);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			checkPopup(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			checkPopup(e);
		}

		private void checkPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popupMenu.show(TweetView.this, e.getX(), e.getY());
			}
		}
	}

}
