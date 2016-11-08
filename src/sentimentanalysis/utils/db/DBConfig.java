/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentimentanalysis.utils.db;

import com.mysql.jdbc.Driver;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author ivan
 */
public class DBConfig {

    private final String CONFIG_FILE_PATH = "db_config.txt";
    private final String ADDRESS;
    private final String USERNAME;
    private final String PASSWORD;

    public DBConfig() {
        JSONObject configJson = readConfig();
        ADDRESS = (String) configJson.get("ADDRESS");
        USERNAME = (String) configJson.get("USERNAME");
        PASSWORD = (String) configJson.get("PASSWORD");
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
    
    public Connection getConnection() throws SQLException {
        new Driver();
        return DriverManager.getConnection(ADDRESS, USERNAME, PASSWORD);
    }
    
}
