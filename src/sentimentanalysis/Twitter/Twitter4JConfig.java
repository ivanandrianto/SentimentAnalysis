/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentimentanalysis.Twitter;

import java.io.FileReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author ivan
 */
public class Twitter4JConfig {

    private final String CONFIG_FILE_PATH = "twitter4j_config.txt";
    private final String CONSUMER_KEY;
    private final String CONSUMER_SECRET;
    private final String ACCESS_KEY;
    private final String ACCESS_SECRET;

    public Twitter4JConfig() {
        JSONObject configJson = readConfig();
        CONSUMER_KEY = (String) configJson.get("CONSUMER_KEY");
        CONSUMER_SECRET = (String) configJson.get("CONSUMER_SECRET");
        ACCESS_KEY = (String) configJson.get("ACCESS_KEY");
        ACCESS_SECRET = (String) configJson.get("ACCESS_SECRET");
    }

    private JSONObject readConfig() {
        JSONObject configJson = null;
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(CONFIG_FILE_PATH));
            configJson = (JSONObject) obj;
            return configJson;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return configJson;
    }

    public ConfigurationBuilder getConfigurationBuilder() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
            .setOAuthConsumerKey(CONSUMER_KEY)
            .setOAuthConsumerSecret(CONSUMER_SECRET)
            .setOAuthAccessToken(ACCESS_KEY)
            .setOAuthAccessTokenSecret(ACCESS_SECRET);
        return cb;
    }

}
