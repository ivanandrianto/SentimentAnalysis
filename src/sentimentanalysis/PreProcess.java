/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentimentanalysis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import opennlp.tools.tokenize.*;
import opennlp.tools.*;
import opennlp.tools.coref.DefaultLinker;
import opennlp.tools.coref.DiscourseEntity;
import opennlp.tools.coref.Linker;
import opennlp.tools.coref.LinkerMode;
import opennlp.tools.coref.mention.DefaultParse;
import opennlp.tools.coref.mention.Mention;
import opennlp.tools.coref.mention.Parse;
import opennlp.tools.entitylinker.EntityLinker;
import opennlp.tools.lemmatizer.SimpleLemmatizer;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.parser.AbstractBottomUpParser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.util.Span;
/**
 *
 * @author ivan
 */
public class PreProcess {            
         
    Linker _linker = null;
    
    public PreProcess() throws IOException, ClassNotFoundException {
        
    }
    
    public void execute() {
        
    }
    
    private static void tokenize() throws FileNotFoundException, IOException {
        InputStream is = new FileInputStream("lib/en-token.bin");
	TokenizerModel model = new TokenizerModel(is);
	Tokenizer tokenizer = new TokenizerME(model);
	String tokens[] = tokenizer.tokenize("Hi. How are you? This is Mike.");
 
	for (String a : tokens) {
            System.out.println(a);
        }	
 
	is.close();
    }
    
    private String lemmatize(String word, String postag) throws IOException {
        SimpleLemmatizer lemmatizer = null;
        if (lemmatizer == null) {
            InputStream is = getClass().getResourceAsStream("lib/en-lemmatizer.dict");
            lemmatizer = new SimpleLemmatizer(is);
            is.close();
        }
        String lemma = lemmatizer.lemmatize(word, postag);
        return lemma;
    }
    
    private static void doNameFinder() throws FileNotFoundException {
        InputStream modelIn = new FileInputStream("lib/en-ner-person.bin");

        try {
            TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
            NameFinderME nameFinder = new NameFinderME(model);

            String[] sentence = new String[]{
                "Ivan",
                "Trump",
                "who",
                "is",
                "Donald",
                "Trump",
                "?"
            };
            Span nameSpans[] = nameFinder.find(sentence);
            for (int i = 0; i < nameSpans.length; i++) {
                System.out.println(nameSpans[i].getStart());
                System.out.println(nameSpans[i].getEnd());
                System.out.println("----------------");
            }
        }
        catch (IOException e) {
          e.printStackTrace();
        }
        finally {
          if (modelIn != null) {
            try {
              modelIn.close();
            }
            catch (IOException e) {
            }
          }
        }

    }
    
    
    public static void main(String args[]) throws IOException {
        doNameFinder();
    }
    
    
}
