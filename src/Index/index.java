//Ahsan Memon 59212236
//Evan Munemura 43513937

package Index;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class index{
	//Helper Map for creating our termid2docidtf_ids
	private static HashMap<Integer, Integer> docid2docsize = new HashMap<Integer, Integer>();
	
	//HashMaps for storing our data
	private static HashMap<String, Integer> term2termid = new HashMap<String, Integer>();
	private static HashMap<String, Integer> doc2docid = new HashMap<String, Integer>();
	private static HashMap<Integer, ArrayList<Integer>> docid2termid = new HashMap<Integer, ArrayList<Integer>>();
	private static HashMap<Integer, String> termid2term = new HashMap<Integer, String>();
	private static HashMap<Integer, HashMap<Integer, Integer>> termid2docidfrequency = new HashMap<Integer, HashMap<Integer, Integer>>();
	private static HashMap<Integer, HashMap<Integer, Double>> termid2docidtf_idf = new HashMap<Integer, HashMap<Integer, Double>>();
	
	//Comparators for sorting our maps
	static ValueComparator t2tID = new ValueComparator(term2termid);
	static ValueComparator d2dID = new ValueComparator(doc2docid);
	
	//TreeMaps for our sorted Maps
	private static TreeMap<String, Integer> sortedterm2termid = new TreeMap<String, Integer>(t2tID);
	private static TreeMap<String, Integer> sorteddoc2docid = new TreeMap<String, Integer>(d2dID);
	
	//Counters for various IDs
	private static Integer term2termidCounter = 0;
	
	//Returns number of documents with a certain term
	private static int getNumOfDocumentsWithTerm(int termID){
		return termid2docidfrequency.get(termID).size();
	}
	
	//Helper method for adding to our docid2docsize
	private static void addDocID2DocSize(int DocID, int size){
		docid2docsize.put(DocID, size);
	}
	
	//Calculates log_e(Total Number of Documents/Number of documents with term in it)
	private static double calculateIDF(int termID, int numOfDocuments){
		int numOfDocumentsWithTerm = getNumOfDocumentsWithTerm(termID);
		return Math.log((double) numOfDocuments/numOfDocumentsWithTerm);
	}
	
	
	//Calculates (Number of times term appears in document) / (Total number of terms in the document)
	private static double calculateTF(int termID, int docID, int total_words){
		int frequency = termid2docidfrequency.get(termID).get(docID);
		return (double) frequency / total_words;
	}
	
	//Helper method for adding termIDs to docIDs and tf_idf
	private static void addTermID2DocIDtf_idf(){
		int numOfDocuments = docid2docsize.size();
		for (Map.Entry<Integer, HashMap<Integer, Integer>> entry : termid2docidfrequency.entrySet()){
			int termID = entry.getKey();
			termid2docidtf_idf.put(termID, new HashMap<Integer, Double>());
			for(Map.Entry<Integer, Integer> docidfrequency : entry.getValue().entrySet()){
				int docID = docidfrequency.getKey();
				int docSize = docid2docsize.get(docID);
				double tf = calculateTF(termID, docID, docSize);
				double idf = calculateIDF(termID, numOfDocuments);
				double tf_idf = Math.round(tf * idf * 1000.0) / 1000.0 ;
				termid2docidtf_idf.get(termID).put(docID, tf_idf);
			}
		}
	}
	
	//Helper method for adding termIDs to docIDs and term frequency
	private static void addTermID2DocIDFrequency(Integer docID, ArrayList<Frequency> frequencies){
		for (Frequency f : frequencies){
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
	
	//CSV File Writers
	//Helper Method to write from a given <String, Integer> hashmap to a given csv file
	private static void writeTermToIDCSV(TreeMap<String, Integer> sortedterm2termid2, String filename){
		PrintWriter writer;
		try {
			writer = new PrintWriter(filename, "UTF-8");
			for(Entry<String, Integer> entry : sortedterm2termid2.entrySet()){
				writer.write(entry.getKey() + ", " + entry.getValue() + "\r\n");
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	//Helper Method to write from a termid2term hashmap to csv file
	private static void writeIDToTermCSV(HashMap<Integer, String> map, String filename){
		PrintWriter writer;
		try {
			writer = new PrintWriter(filename, "UTF-8");
			for(Object id : map.keySet()){
				writer.write(id + ", " + map.get(id) + "\r\n");
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	//Helper Method to write from a given tfidf hashmap to a given csv file
	private static void writetfidfCSV(HashMap<Integer, HashMap<Integer, Double>> map, String filename){
		PrintWriter writer;
		try {
			writer = new PrintWriter(filename, "UTF-8");
			for(Integer id : map.keySet() ){
				writer.write(id + ", ");
				HashMap<Integer, Double> innerMap = map.get(id);
				for(Entry<Integer, Double> entry : innerMap.entrySet()){
			        writer.write(entry.getKey() + " = " + entry.getValue());
			        writer.write(", ");
				}
				writer.write("\r\n");
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	//Helper Method to write from a given termid2docidfrequency hashmap to a given csv file
	private static void writeFrequencyCSV(HashMap<Integer, HashMap<Integer, Integer>> map, String filename){
		PrintWriter writer;
		try {
			writer = new PrintWriter(filename, "UTF-8");
			for(Integer id : map.keySet() ){
				writer.write(id + ", ");
				HashMap<Integer, Integer> innerMap = map.get(id);
				for(Entry<Integer, Integer> entry : innerMap.entrySet()){
			        writer.write(entry.getKey() + " = " + entry.getValue());
			        writer.write(", ");
				}
				writer.write("\r\n");
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	//Helper Method to write from a given doc2termid hashmap to a given csv file
	private static void writeDoc2TermIDCSV(HashMap<Integer, ArrayList<Integer>> map, String filename){
		PrintWriter writer;
		try {
			writer = new PrintWriter(filename, "UTF-8");
			for(Integer id : map.keySet() ){
				writer.write(id + ", ");
				ArrayList<Integer> inner = map.get(id);
				for(int i = 0; i < inner.size(); i++){
					if(inner.equals(null) || inner.isEmpty()){
						writer.write("\r\n");
					}else {
						writer.write(inner.get(i) + ", ");
					}
			        
				}
				writer.write("\r\n");
			}
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
<<<<<<< HEAD
		File folder = new File ("c://users/Ahsan/Documents/CS121/FileDump"); 
=======
		File folder = new File ("LargeDump"); 
>>>>>>> origin/master
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
				
				addDocID2DocSize(id, tokens.size());
				
				
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
		//Write to CSV Files
		writeTermToIDCSV(sortedterm2termid, "term2termid.csv");
		//sortedterm2termid.clear();
		//term2termid.clear();
		writeTermToIDCSV(sorteddoc2docid, "doc2docid.csv");
		//sorteddoc2docid.clear();
		//doc2docid.clear();
		writeIDToTermCSV(termid2term, "termid2term.csv");
		//termid2term.clear();
		writeFrequencyCSV(termid2docidfrequency, "frequency.csv");
		//termid2docidfrequency.clear();
		writetfidfCSV(termid2docidtf_idf, "tfidf.csv");
		//termid2docidtf_idf.clear();
		writeDoc2TermIDCSV(docid2termid, "doc2termid.csv");
		//docid2termid.clear();
		
		//Write to Text Files
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
		
		addTermID2DocIDtf_idf();
		writeToFile(termid2docidtf_idf.toString(), "termid2docidtf_idf.txt");

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
