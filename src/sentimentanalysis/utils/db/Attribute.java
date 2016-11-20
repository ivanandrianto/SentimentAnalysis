/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentimentanalysis.utils.db;

/**
 *
 * @author Satria
 */
public class Attribute {
    private String attribute_name;
    private int candidate_id;
            
    public Attribute(String attribute_name, int candidate_id){
        this.attribute_name = attribute_name;
        this.candidate_id = candidate_id;
    }

    public String getAttributeName() {
        return attribute_name;
    }

    public int getCandidateId() {
        return candidate_id;
    }

}
