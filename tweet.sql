CREATE TABLE tweets (
  `tweet_id` int(11) NOT NULL AUTO_INCREMENT,
  `author` varchar(45) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `text` varchar(141) DEFAULT NULL,
  `place` varchar(50) DEFAULT NULL,
  `langageCode` varchar(20) DEFAULT NULL,
  `media` bit(64) DEFAULT NULL,
  `location` varchar(45) DEFAULT NULL,
  `collection_id` varchar(15) DEFAULT NULL,
  `image` blob,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`tweet_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
ALTER TABLE tweets MODIFY COLUMN tweet_id INT AuTO_INCREMENT;
ALTER TABLE bd.tweets MODIFY COLUMN text VARCHAR(141)  
    CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;
    

    

