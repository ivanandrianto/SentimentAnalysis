/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentimentanalysis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import static sentimentanalysis.ApacheNLPTagger.*;
import sentimentanalysis.utils.db.DBConfig;

/**
 *
 * @author ivan, satria
 */
public class SentimentAnalysis {
    private static String test = "Halo world! ahsdf";
    private static String paragraph = "Hi. How bad you? This is fuck Mike.\n I have a bad day today, so can you give me a worst ride? I will gladly accept it.\n Please, I need it dearly";
    
    private static String pathToSWN = "data/SentiWordNet_3.0.0.txt";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SQLException {
//        DBConfig dbConfig = new DBConfig();
//        Connection conn = dbConfig.getConnection();
//        PreparedStatement statement = null;
//        String ReadDatabaseQuery = "SELECT * FROM trump";
//
//        ResultSet rs = statement.executeQuery(ReadDatabaseQuery);
//        // iterate through the java resultset
//        while (rs.next())
//        {
//          int id = rs.getInt("id");
//          String firstName = rs.getString("first_name");
//          String lastName = rs.getString("last_name");
//          Date dateCreated = rs.getDate("date_created");
//          boolean isAdmin = rs.getBoolean("is_admin");
//          int numPoints = rs.getInt("num_points");
//
//          // print the results
//          System.out.format("%s, %s, %s, %s, %s, %s\n", id, firstName, lastName, dateCreated, isAdmin, numPoints);
//        }
          System.out.println(getTwitterSentiment(paragraph));
    }
    
    private static float getTwitterSentiment(String twitter) throws IOException{
        //Variable to content the total sentiment of a paragraph
        float paragraphSentiment = 0;
	SentiWordNetAnalyzer sentiwordnet = new SentiWordNetAnalyzer(pathToSWN);    
        //Detect the sentence and do sentiment analysis per sentence
        String[] sentences  = SentenceDetect(twitter);
        for (String sentence : sentences){
            ArrayList<String[]> POSResult = POSTag(sentence);
            String whitespaceTokenizeWord[] = WhitespaceTokenizer.INSTANCE.tokenize(sentence);
            //Do a sentiment analysis for a sentence
            float sentenceSentiment = 0;
            for (int i = 0; i < whitespaceTokenizeWord.length; i++) {
                String word = POSResult.get(0)[i];
                String parsedWord = word.replaceAll("[^A-z]", "");
                char firstChar = POSResult.get(0)[i].charAt(0);
                
                if(firstChar == 'J'){
                    //System.out.println("Adjective");
                    //System.out.println(whitespaceTokenizeWord[i] +"#a "+sentiwordnet.extract(whitespaceTokenizeWord[i], "a"));
                    sentenceSentiment = (float) (sentenceSentiment + sentiwordnet.extract(whitespaceTokenizeWord[i], "a"));
                } else if(firstChar == 'V'){
                    //System.out.println("Verb");
                    //System.out.println(whitespaceTokenizeWord[i] +"#v "+sentiwordnet.extract(whitespaceTokenizeWord[i], "v"));
                    sentenceSentiment = (float) (sentenceSentiment + sentiwordnet.extract(whitespaceTokenizeWord[i], "v"));
                } else if(firstChar == 'R'){
                    //System.out.println("AdVerb");
                    //System.out.println(whitespaceTokenizeWord[i] +"#r "+sentiwordnet.extract(whitespaceTokenizeWord[i], "r"));
                    sentenceSentiment = (float) (sentenceSentiment + sentiwordnet.extract(whitespaceTokenizeWord[i], "r"));
                } else if(firstChar == 'N'){
                    //System.out.println("Noun");
                    //System.out.println(whitespaceTokenizeWord[i] +"#n "+sentiwordnet.extract(whitespaceTokenizeWord[i], "n"));
                    sentenceSentiment = (float) (sentenceSentiment + sentiwordnet.extract(whitespaceTokenizeWord[i], "n"));
                } else {
                    //do nothing
                }
                
            }
            paragraphSentiment = paragraphSentiment + sentenceSentiment;
            System.out.println(sentence + " Sentence Sentiment " + sentenceSentiment);
        }
        System.out.println("Paragraph Sentiment " + paragraphSentiment);
        return paragraphSentiment;
    }
    
}
