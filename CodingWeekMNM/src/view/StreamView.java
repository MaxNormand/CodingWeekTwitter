package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.TweetStream;

public class StreamView extends JPanel {
	TweetStream stream;
	JButton liveUpdates;
	JLabel  title;

	public StreamView() {
		stream = new TweetStream();
		this.setLayout(new BorderLayout());
		this.setBackground(Color.decode("#c0deed"));
		this.setPreferredSize(new Dimension(500, 800));
		liveUpdates = new JButton("LIVE UPDATES");
		title= new JLabel("Twitter Stream");
		title.setForeground(Color.white);
		this.add(title,BorderLayout.NORTH);
		this.add(liveUpdates,BorderLayout.SOUTH);
	}

}
