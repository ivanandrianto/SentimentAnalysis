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
            String insertPostSQL = "INSERT INTO trump(user, content, link, postTime) VALUES(?, ?, ?, ?)";

            insertStatement = conn.prepareStatement(insertPostSQL);
            insertStatement.setString(1, user);
            insertStatement.setString(2, content);
            insertStatement.setString(3, link);
            insertStatement.setDate(4, postTime);
            insertStatement.executeUpdate();
        }

        conn.close();    

    }
    
    public static void saveAttributes(ArrayList<Attribute> attributes) throws SQLException {
        
        DBConfig dbConfig = new DBConfig();
        Connection conn = dbConfig.getConnection();
                
        for (Attribute attr : attributes) {
            String attribute_name = attr.getAttributeName();
            int candidate_id = attr.getCandidateId();
            
            PreparedStatement insertStatement = null;
            String insertPostSQL = "INSERT INTO attributes(attribute_name, candidate_id) VALUES(?, ?, ?, ?)";

            insertStatement = conn.prepareStatement(insertPostSQL);
            insertStatement.setString(1, attribute_name);
            insertStatement.setInt(2, candidate_id);
            insertStatement.executeUpdate();
        }

        conn.close();    

    }
    
    public static void saveGraph(ArrayList<GraphElement> graphElements) throws SQLException {
        
        DBConfig dbConfig = new DBConfig();
        Connection conn = dbConfig.getConnection();
                
        for (GraphElement graphElmt : graphElements) {
            float percentage = graphElmt.getPercentage();
            Date postTime = graphElmt.getTime();
            int attribute_id = graphElmt.getAttributeId();
            
            PreparedStatement insertStatement = null;
            String insertPostSQL = "INSERT INTO graphs(percentage ,date, attribute_id) VALUES(?, ?, ?)";

            insertStatement = conn.prepareStatement(insertPostSQL);
            insertStatement.setFloat(1, percentage);
            insertStatement.setDate(2, postTime);
            insertStatement.setInt(3, attribute_id);
            insertStatement.executeUpdate();
        }

        conn.close();    

    }

}
