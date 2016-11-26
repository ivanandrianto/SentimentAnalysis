/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentimentanalysis.utils.db;

import java.sql.Date;

/**
 *
 * @author Satria
 */
public class Attribute {
    private String attribute_name;
    private float sentiment;
    private Date time;
    private int candidate_id;
            
    public Attribute(String attribute_name, int candidate_id, float _sentiment, Date _time){
        this.attribute_name = attribute_name;
        this.candidate_id = candidate_id;
        this.time = _time;
        this.sentiment = _sentiment;
    }

    public String getAttributeName() {
        return attribute_name;
    }

    public int getCandidateId() {
        return candidate_id;
    }
    
    public float getSentiment(){
        return sentiment;
    }
    
    public Date getDate(){
        return time;
    }

}
