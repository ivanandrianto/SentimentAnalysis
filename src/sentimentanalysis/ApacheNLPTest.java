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
public class ApacheNLPTest{

    public static void SentenceDetect() throws InvalidFormatException,
		IOException {
	String paragraph = "Hi. How are you? This is Mike.";
 
	// always start with a model, a model is learned from training data
	InputStream is = new FileInputStream("en-sent.bin");
	SentenceModel model = new SentenceModel(is);
	SentenceDetectorME sdetector = new SentenceDetectorME(model);
 
	String sentences[] = sdetector.sentDetect(paragraph);
 
	System.out.println(sentences[0]);
	System.out.println(sentences[1]);
	is.close();
    }
    public static void POSTag() throws IOException {
	POSModel model = new POSModelLoader()	
		.load(new File("en-pos-maxent.bin"));
	PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
	POSTaggerME tagger = new POSTaggerME(model);
 
	String input = "Hi. How are you? This is Mike. I have a meeting today, so can you give me a ride? I will gladly accept it. Please, I need it dearly";
	ObjectStream<String> lineStream = new PlainTextByLineStream(
			new StringReader(input));
 
	perfMon.start();
	String line;
	while ((line = lineStream.read()) != null) {
 
		String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
				.tokenize(line);
		String[] tags = tagger.tag(whitespaceTokenizerLine);
 
		POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
		System.out.println(sample.toString());
 
		perfMon.incrementCounter();
	}
	perfMon.stopAndPrintFinalResult();
}
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        SentenceDetect();
        POSTag();
    }

    
}
