/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentimentanalysis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import static sentimentanalysis.ApacheNLPTagger.*;
import sentimentanalysis.utils.db.DBConfig;
import sentimentanalysis.utils.db.GraphElement;
import sentimentanalysis.utils.db.SaveToDB;

/**
 *
 * @author ivan, satria
 */
public class SentimentAnalysis {
    private static String test = "Halo world! ahsdf";
    private static String paragraph = "#Trump's America\n Thanks non voters and nazi sympathizers. https://t.co/tFRy1QBsrK";
    
    private static String pathToSWN = "data/SentiWordNet_3.0.0.txt";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        DBConfig dbConfig = new DBConfig();
        Connection conn = dbConfig.getConnection();
        Statement statement = conn.createStatement();;
        String ReadDatabaseQuery = "SELECT * FROM trump";

        ResultSet rs = statement.executeQuery(ReadDatabaseQuery);
        // iterate through the java resultset
        SaveToDB saver = new SaveToDB();
        ArrayList<GraphElement> graphs = new ArrayList();
        int counter = 0;
        while (rs.next())
        {
            
            String tweet = rs.getString("content");
            Date time = rs.getDate("postTime");
            // print the results
            //System.out.println(tweet);
            float percentage = getTwitterSentiment(tweet);
            //attribute id is 0 for trump and 999 for Hillary
            GraphElement tempGraph = new GraphElement(percentage, time, 0);
            graphs.add(tempGraph);
            //++counter;
        }
        saver.saveGraph(graphs);
        
        
    }
    
    private static float getTwitterSentiment(String twitter) throws IOException, ClassNotFoundException{
        //Variable to content the total sentiment of a paragraph
        float paragraphSentiment = 0;
	SentiWordNetAnalyzer sentiwordnet = new SentiWordNetAnalyzer(pathToSWN);    
        //Detect the sentence and do sentiment analysis per sentence
        String[] sentences  = SentenceDetect(twitter);
        for (String sentence : sentences){
            ArrayList<String[]> POSResult = POSTag(sentence);
            PreProcess preprocessor = new PreProcess();
            String TokenizedWord[] = preprocessor.tokenize(sentence);
            //Do a sentiment analysis for a sentence
            float sentenceSentiment = 0;
            String[] ArrayOfTag = POSResult.get(0);
            for (int i = 0; i < ArrayOfTag.length; i++) {
                String tag = ArrayOfTag[i];
                char firstChar = tag.charAt(0);
                //System.out.println(TokenizedWord[i] + " tag :" + tag);
                
                String lemmaWord = preprocessor.lemmatize(TokenizedWord[i], tag);
                if(firstChar == 'J'){
                    //System.out.println("Adjective");
                    sentenceSentiment = (float) (sentenceSentiment + sentiwordnet.extract(lemmaWord, "a"));
                } else if(firstChar == 'V'){
                    //System.out.println("Verb");
                    sentenceSentiment = (float) (sentenceSentiment + sentiwordnet.extract(lemmaWord, "v"));
                } else if(firstChar == 'R'){
                    //System.out.println("AdVerb");
                    sentenceSentiment = (float) (sentenceSentiment + sentiwordnet.extract(lemmaWord, "r"));
                } else if(firstChar == 'N'){
                    //System.out.println("Noun");
                    sentenceSentiment = (float) (sentenceSentiment + sentiwordnet.extract(lemmaWord, "n"));
                } else {
                    //do nothing
                }
                
            }
            paragraphSentiment = paragraphSentiment + sentenceSentiment;
            //System.out.println(sentence + " Sentence Sentiment " + sentenceSentiment);
        }
        //System.out.println("Paragraph Sentiment " + paragraphSentiment);
        return paragraphSentiment;
    }
    
}
