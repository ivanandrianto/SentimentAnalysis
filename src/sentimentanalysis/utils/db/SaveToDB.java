/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentimentanalysis.utils.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author ivan
 */
public class SaveToDB {
    Connection conn;
    
    public SaveToDB(Connection _conn){
        conn = _conn;
    }
    
    public void savePosts(ArrayList<Post> posts) throws SQLException {
                
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
 

    }
    
    public void saveAttributes(Attribute attr) throws SQLException {
        
                
        String attribute_name = attr.getAttributeName();
        int candidate_id = attr.getCandidateId();

        PreparedStatement insertStatement = null;
        String insertPostSQL = "INSERT INTO attributes(attribute_name, candidate_id,date,sentiment) VALUES(?, ?, ?, ?)";

        insertStatement = conn.prepareStatement(insertPostSQL);
        insertStatement.setString(1, attribute_name);
        insertStatement.setInt(2, candidate_id);
        insertStatement.setDate(3,attr.getDate());
        insertStatement.setFloat(4,attr.getSentiment());
        insertStatement.executeUpdate();


    }
    
    public void saveGraph(GraphElement graphElmt) throws SQLException {
        
                
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

}
