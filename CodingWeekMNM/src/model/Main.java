package model;

import service.TweetManagerService;
import utils.TweetManagerUtils;
import view.MainFrame;

public class Main {
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		if (args.length== 0){
			System.out.println("Entrez un argument SVP !! Soit -console soit -ig soit -sc");
		}
		else {
			String s = args[0];
			if (args[0].equals("-ig")){
				TweetManagerService twitterService= new TweetManagerService(TweetManagerUtils.authenticate());
				new MainFrame(twitterService);
			}
			else if (args[0].equals("-console")){
				TweetManagerService twitterService= new TweetManagerService(TweetManagerUtils.authenticate());
				TweetManagerConsole c=new TweetManagerConsole(twitterService);
				c.SelectSearch();
		/*TweetManagerDao d=new TweetManagerDao();
		d.daoAuthenticate();*/
			}
			else if (args[0].equals("-sc")){
				final TweetManagerService twitterService = new TweetManagerService(TweetManagerUtils.authenticate());
				while(true){
					final TweetWrapper tweet=twitterService.getTweetStream().get(0);
					System.out.println("Author: "+tweet.getAuthor()+" Date: "+tweet.getCreationDate()+" Text: "+tweet.getText());
				}
			}	
			else {
				System.out.println("Erreur argument !! Entrez soit -console soit -ig soit -sc svp");
			}
		}	
		
	}
}

