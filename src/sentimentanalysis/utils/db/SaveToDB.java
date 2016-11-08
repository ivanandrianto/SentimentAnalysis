/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentimentanalysis.utils.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author ivan
 */
public class SaveToDB {
    
    public static void savePosts(ArrayList<Post> posts) throws SQLException {
        
        DBConfig dbConfig = new DBConfig();
        Connection conn = dbConfig.getConnection();
                
        for (Post post : posts) {
            String user = post.getUser();
            String content = post.getContent();
            String link = post.getLink();
            Date postTime = post.getTime();
            
            PreparedStatement insertStatement = null;
            String insertPostSQL = "INSERT INTO posts(user, content, link, postTime) VALUES(?, ?, ?, ?)";

            insertStatement = conn.prepareStatement(insertPostSQL);
            insertStatement.setString(1, user);
            insertStatement.setString(2, content);
            insertStatement.setString(3, link);
            insertStatement.setDate(4, postTime);
            insertStatement.executeUpdate();
        }

        conn.close();    

    }

}
