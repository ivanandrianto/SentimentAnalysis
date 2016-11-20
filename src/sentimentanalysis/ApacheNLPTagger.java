/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentimentanalysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

/**
 *
 * @author ivan
 */
public class ApacheNLPTagger{

    public static String[] SentenceDetect(String paragraph) throws InvalidFormatException,
		IOException {
 
	// always start with a model, a model is learned from training data
	InputStream is = new FileInputStream("en-sent.bin");
	SentenceModel model = new SentenceModel(is);
	SentenceDetectorME sdetector = new SentenceDetectorME(model);
 
	String sentences[] = sdetector.sentDetect(paragraph);

	is.close();
        
        return sentences;
    }
    
    public static ArrayList POSTag(String input) throws IOException {
	POSModel model = new POSModelLoader()	
		.load(new File("en-pos-maxent.bin"));
	PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
	POSTaggerME tagger = new POSTaggerME(model);
 
	ObjectStream<String> lineStream = new PlainTextByLineStream(
			new StringReader(input));
 
	ArrayList<String[]> listOfWordAndTag = new ArrayList();
        
	String line;
	while ((line = lineStream.read()) != null) {
            try {
                PreProcess preprocessor = new PreProcess();
                
                String whitespaceTokenizerLine[] = preprocessor.tokenize(line);
                String[] tags = tagger.tag(whitespaceTokenizerLine);

                POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
                //System.out.println(sample.toString() + "X");
                listOfWordAndTag.add(tags);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ApacheNLPTagger.class.getName()).log(Level.SEVERE, null, ex);
            }

	}
        
        return listOfWordAndTag;
}
    
    /**
     * @param args the command line arguments
     */
    /*public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
	//String paragraph = "Hi. How are you? This is Mike.";
        //String[] sentences  = SentenceDetect(paragraph);
        String input = "Hi. How are you? This is Mike.\n I have a meeting today, so can you give me a ride? I will gladly accept it.\n Please, I need it dearly";
        ArrayList<String[]> POSResult = POSTag(input);
        String[] words = null, tags = null;
        String finalSentence = "", finalTags = "";
        for (int i = 0; i < POSResult.size(); i++) {
            StringBuilder builder = new StringBuilder();
            if (i % 2 == 0){
                words = POSResult.get(i);
                for(String s : words) {
                    builder.append(s).append(" ");
                }
                finalSentence = finalSentence + " " + builder.toString();
            } else {
                tags = POSResult.get(i);
                for(String s : tags) {
                    builder.append(s).append(" ");
                }
                finalTags = finalTags + " " + builder.toString();
            }
            
        }
        
        System.out.println(finalSentence);
        System.out.println(finalTags);
    }
*/
    
}
