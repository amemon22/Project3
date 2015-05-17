package Index;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class index{
	
	//HashMaps for storing our data
	private static HashMap<String, Integer> term2termid = new HashMap<String, Integer>();
	private static HashMap<String, Integer> doc2docid = new HashMap<String, Integer>();
	private static HashMap<Integer, ArrayList<Integer>> docid2termid = new HashMap<Integer, ArrayList<Integer>>();
	private static HashMap<Integer, String> termid2term = new HashMap<Integer, String>();
	private static HashMap<Integer, HashMap<Integer, Integer>> termid2docidfrequency = new HashMap<Integer, HashMap<Integer, Integer>>();
	
	//Comparators for sorting our maps
	static ValueComparator t2tID = new ValueComparator(term2termid);
	static ValueComparator d2dID = new ValueComparator(doc2docid);
	
	//TreeMaps for our sorted Maps
	private static TreeMap<String, Integer> sortedterm2termid = new TreeMap<String, Integer>(t2tID);
	private static TreeMap<String, Integer> sorteddoc2docid = new TreeMap<String, Integer>(d2dID);
	
	//Counters for various IDs
	private static Integer term2termidCounter = 0;
	
	//Calculates (Number of times term t appears in a document) / (Total number of terms in the document)
	//private static void calcTermFrequency()
	
	
	
	
	//Helper method for adding termIDs to docIDs and term frequency
	private static void addTermID2DocIDFrequency(Integer docID, ArrayList<Frequency> frequencies){
		for (Frequency f: frequencies){
			int termID = term2termid.get(f.getText());
			if(!termid2docidfrequency.containsKey(termID)){
				termid2docidfrequency.put(termID, new HashMap<Integer, Integer>());
			}
			termid2docidfrequency.get(termID).put(docID, f.getFrequency());
		
		}
	}
	//Helper method for adding termIDs to terms for out termid2term Map
	private static void addTermID2Term(Integer counter, String token){
		termid2term.put(counter, token);
	}
	
	//Helper method for adding docIDs with their term IDs for our docid2termID Map
	private static void addDocID2TermID(Integer docID, ArrayList<Integer> termIDs){
		docid2termid.put(docID, termIDs);
	}
	
	//Helper method for adding unique tokens for our term2termID Map
	private static void addTerm2TermID(ArrayList<String> tokens){
		for (String token : tokens){
			if (!term2termid.containsKey(token)){
				term2termidCounter++;
				term2termid.put(token, term2termidCounter);
				addTermID2Term(term2termidCounter, token);
			}
		}
	}
	
	//Helper Method for adding docs and docIDs to doc2docid Map
	private static void addDoc2DocID(String URL, Integer ID){
		if(!doc2docid.containsKey(URL)){
			doc2docid.put(URL, ID);
		}
	}
	
	//Helper Method to write to a given text to a given filename
	private static void writeToFile(String text, String filename){
		PrintWriter writer;
		try {
			writer = new PrintWriter(filename, "UTF-8");
			writer.write(text);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	//Returns all the termIDs associated with a list of tokens
	private static ArrayList<Integer> getTermIDs(ArrayList<String> tokens){
		ArrayList<Integer> termIDs = new ArrayList<Integer>();
		for (String token : tokens){
			termIDs.add(term2termid.get(token));
		}
		return termIDs;
	}
	
	
	public static void main(String[] args){
		
		WordFrequencyCounter.storeStopWords();
		Data data = null;
		ObjectMapper  mapper = new ObjectMapper();
		//"c://users/Ahsan/Documents/CS121/FileDump" Big Dump
		//"c://users/Ahsan/Workspace/Java/Project3/FileDump Small Dump"
		File folder = new File ("c://users/Ahsan/Documents/CS121/FileDump"); 
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> tokens = new ArrayList<String>();
		ArrayList<Integer> termIDs = new ArrayList<Integer>();
		ArrayList<Frequency> frequencies = new ArrayList<Frequency>();
		
		String URL;
		int id;
		
		for (File file : listOfFiles){
			try {
				data = mapper.readValue(file, Data.class);
				tokens = Utilities.tokenizeFile(data.getText());
				id = data.getID();
				URL = data.get_ID();
				frequencies = WordFrequencyCounter.computeWordFrequencies(tokens);
				
				System.out.println(id + " " + URL);
	
				addTerm2TermID(tokens);
				
				termIDs = getTermIDs(tokens);
				
				addTermID2DocIDFrequency(id,frequencies);
				
				addDoc2DocID(URL,id);
				
				addDocID2TermID(id, termIDs);
				
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//These are used to makes the map in order
		sortedterm2termid.putAll(term2termid);
		sorteddoc2docid.putAll(doc2docid);
		
		//After writing to the file, we clear the maps that we don't need just to speed up the process
		writeToFile(sortedterm2termid.toString(), "term2termid.txt");
		sortedterm2termid.clear();
		term2termid.clear();
		writeToFile(sorteddoc2docid.toString(), "doc2docid.txt");
		sorteddoc2docid.clear();
		doc2docid.clear();
		writeToFile(docid2termid.toString(), "docid2termid.txt");
		docid2termid.clear();
		writeToFile(termid2term.toString(), "termid2term.txt");
		termid2term.clear();
		writeToFile(termid2docidfrequency.toString(), "termid2docidfrequency.txt");
		
		System.out.println("Done");
	}
	
}

class ValueComparator implements Comparator<String> {

    HashMap<String, Integer> base;
    public ValueComparator(HashMap<String, Integer> base) {
        this.base = base;
    }
    public int compare(String a, String b) {
        if (base.get(a) <= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}


