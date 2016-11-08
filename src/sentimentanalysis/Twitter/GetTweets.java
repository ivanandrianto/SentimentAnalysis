package sentimentanalysis.Twitter;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import sentimentanalysis.utils.db.Post;
import sentimentanalysis.Twitter.Twitter4JConfig;
import sentimentanalysis.utils.db.SaveToDB;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ivan
 */
public class GetTweets {
    
    public static void main(String args[]) {

        Twitter4JConfig t4jConfig = new Twitter4JConfig();
        ConfigurationBuilder cb = t4jConfig.getConfigurationBuilder();

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        String keyword = "Trump";

        ArrayList<Post> TweetResultList = new ArrayList<Post>();
        int count = 0;
        try {

            Query query = new Query(keyword);
            QueryResult result;

            do {
                result = twitter.search(query);
                ArrayList<Status> tweets = (ArrayList<Status>) result.getTweets();

                for (Status tweet : tweets) {
                    count++;

                    String user = tweet.getUser().getScreenName();
                    String content = tweet.getText();
                    Date time = tweet.getCreatedAt();
                    String tweet_url = "https://twitter.com/" + tweet.getUser().getScreenName() 
                        + "/status/" + tweet.getId();

                    Post tr = new Post(user, content, tweet_url,  new java.sql.Date(time.getTime()));
                    TweetResultList.add(tr);

                    System.out.println("[user]@" + tweet.getUser().getScreenName() 
                        + "[/user][tweet]" + tweet.getText()+"[/tweet][link]"+tweet_url+"[/link]");
                }

            } while (((query = result.nextQuery()) != null) && (count < 100));
            
            try {
                SaveToDB.savePosts(TweetResultList);
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(GetTweets.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (TwitterException te) {
            te.printStackTrace();
        }

    }

}
