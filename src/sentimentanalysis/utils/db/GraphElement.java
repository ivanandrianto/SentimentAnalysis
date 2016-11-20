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
public class GraphElement {
    private float percentage;
    private Date time;
    private int attribute_id;
            
    public GraphElement(float percentage, Date time, int attribute_id){
        this.percentage = percentage;
        this.time = time;
        this.attribute_id = attribute_id;
    }


    public float getPercentage() {
        return percentage;
    }

    public int getAttributeId() {
        return attribute_id;
    }
    
    public Date getTime() {
        return time;
    }
}
