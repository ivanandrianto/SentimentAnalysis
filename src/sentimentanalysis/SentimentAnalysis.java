/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentimentanalysis;

import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.coref.data.Mention;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import static sentimentanalysis.ApacheNLPTagger.*;
import sentimentanalysis.utils.db.Attribute;
import sentimentanalysis.utils.db.DBConfig;
import sentimentanalysis.utils.db.GraphElement;
import sentimentanalysis.utils.db.SaveToDB;

/**
 *
 * @author ivan, satria
 */
public class SentimentAnalysis {
    private static String pathToSWN = "data/SentiWordNet_3.0.0.txt";
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        indexAttribute(1);
        indexAttribute(2);
    }
    
    
    
    private static void indexTrump() throws SQLException, IOException, ClassNotFoundException{
        DBConfig dbConfig = new DBConfig();
        Connection conn = dbConfig.getConnection();
        Statement statement = conn.createStatement();;
        String ReadDatabaseQuery = "SELECT * FROM trump";

        ResultSet rs = statement.executeQuery(ReadDatabaseQuery);
        // iterate through the java resultset
        SaveToDB saver = new SaveToDB(conn);
        ArrayList<GraphElement> graphs = new ArrayList();
        int counter = 0;
        while (rs.next())
        {
            String tweet = rs.getString("content");
            Date time = rs.getDate("postTime");
            float percentage = getTwitterSentiment(tweet);
            //attribute id is 0 for trump and -1 for Hillary
            GraphElement tempGraph = new GraphElement(percentage, time, 0);
            saver.saveGraph(tempGraph);
            System.out.println(++counter);
        }
        conn.close();
    }
    
    private static void indexClinton() throws SQLException, IOException, ClassNotFoundException{
        DBConfig dbConfig = new DBConfig();
        Connection conn = dbConfig.getConnection();
        Statement statement = conn.createStatement();
        String ReadDatabaseQuery = "SELECT * FROM clinton";

        ResultSet rs = statement.executeQuery(ReadDatabaseQuery);
        // iterate through the java resultset
        SaveToDB saver = new SaveToDB(conn);
        ArrayList<GraphElement> graphs = new ArrayList();
        int counter = 0;
        while (rs.next())
        {     
            String tweet = rs.getString("content");
            Date time = rs.getDate("postTime");
            float percentage = getTwitterSentiment(tweet);
            //attribute id is 0 for trump and -1 for Hillary
            GraphElement tempGraph = new GraphElement(percentage, time, -1);
            saver.saveGraph(tempGraph);
            System.out.println(++counter);
        }
        conn.close();
    }
    
    private static void indexAttribute(int candidate) throws SQLException, IOException, ClassNotFoundException{
        DBConfig dbConfig = new DBConfig();
        Connection conn = dbConfig.getConnection();
        Statement statement = conn.createStatement();
        String ReadDatabaseQuery = "SELECT * FROM clinton";
        if(candidate==1){
            ReadDatabaseQuery = "SELECT * FROM trump";
        }
        
        ResultSet rs = statement.executeQuery(ReadDatabaseQuery);
        // iterate through the java resultset
        SaveToDB saver = new SaveToDB(conn);
        int counter = 0;
        while (rs.next())
        {
            String tweet = rs.getString("content");
            Date time = rs.getDate("postTime");
            
            ArrayList<String> allAttributes = getAttributes(tweet);
            if(!allAttributes.isEmpty()){
                System.out.println("----Attributes : " + allAttributes);
                float sentiment = getTwitterSentiment(tweet);
                for(int i=0;i<allAttributes.size();i++){
                    Attribute A = new Attribute(allAttributes.get(i),candidate,sentiment,time);
                    saver.saveAttributes(A);
                }
            }
            System.out.println(++counter);
        }
        conn.close();
    }
    
    private static ArrayList<String> getAttributes(String twitter) throws IOException, ClassNotFoundException{
        ArrayList<String> attributes = new ArrayList<String>();
        String[] sentences = SentenceDetect(twitter);
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,mention,coref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        
        for(String sentence : sentences){
            Annotation document = new Annotation(sentence);
            pipeline.annotate(document);
            for (CoreMap sent : document.get(CoreAnnotations.SentencesAnnotation.class)) {
                for (Mention m : sent.get(CorefCoreAnnotations.CorefMentionsAnnotation.class)) {
                  
                  if(m.getRelation()=="verbArg"){
                      ArrayList<String[]> POSResult = POSTag(m.toString());
                      PreProcess preprocessor = new PreProcess();
                      String TokenizedWord[] = preprocessor.tokenize(m.toString());
                      String[] ArrayOfTag = POSResult.get(0);
                      for (int i = 0; i < ArrayOfTag.length; i++) {
                           if(ArrayOfTag[i].length()> 2){
                                if(ArrayOfTag[i].substring(0,2).equals("JJ") && (i+1)<ArrayOfTag.length){
                                   if(ArrayOfTag[i+1].length()>1){
                                        if(ArrayOfTag[i+1].substring(0,2).equals("NN")){
                                           String attr = TokenizedWord[i] + " " + TokenizedWord[i+1];                                   
                                           attributes.add(attr);
                                        }
                                   }
                                }
                                if(ArrayOfTag[i].equals("NNS") && !TokenizedWord[i].toLowerCase().contains("https") && !TokenizedWord[i].contains("http")){
                                    String attr = TokenizedWord[i];
                                    attributes.add(attr);
                                }
                           }
                       }
                  }
                }
          }
        }
        return attributes;
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
        }
        return paragraphSentiment;
    }
    
}
