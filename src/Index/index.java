//Ahsan Memon 59212236
//Evan Munemura 43513937

package Index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class index{
	//Helper Map for creating our termid2docidtf_ids
	private static HashMap<Integer, Integer> docid2docsize = new HashMap<Integer, Integer>();
	
	//HashMaps for storing our data
	private static HashMap<String, Integer> term2termid = new HashMap<String, Integer>();
	private static HashMap<String, Integer> doc2docid = new HashMap<String, Integer>();
	private static HashMap<Integer, String> docid2doc = new HashMap<Integer, String>();
	private static HashMap<Integer, ArrayList<Integer>> docid2termid = new HashMap<Integer, ArrayList<Integer>>();
	private static HashMap<Integer, String> termid2term = new HashMap<Integer, String>();
	private static HashMap<Integer, HashMap<Integer, Integer>> termid2docidfrequency = new HashMap<Integer, HashMap<Integer, Integer>>();
	private static HashMap<Integer, HashMap<Integer, Double>> termid2docidtf_idf = new HashMap<Integer, HashMap<Integer, Double>>();
	
	//Get methods for obtaining the maps
	public static HashMap<String, Integer> getTerm2TermID(){
		return term2termid;
	}
	
	public HashMap<String, Integer> getDoc2DocID(){
		return doc2docid;
	}
	
	public HashMap<Integer, String> getDocID2Doc(){
		return docid2doc;
	}
	
	public HashMap<Integer, ArrayList<Integer>> getDocID2TermID(){
		return docid2termid;
	}
	
	public HashMap<Integer, String> getTermID2Term(){
		return termid2term;
	}
	
	public HashMap<Integer, HashMap<Integer, Integer>> getTermID2DocIDFrequency(){
		return termid2docidfrequency;
	}
	
	public HashMap<Integer, HashMap<Integer, Double>> getTermID2DocIDTF_IDF(){
		return termid2docidtf_idf;
	}
	
	//Comparators for sorting our maps
	static ValueComparator t2tID = new ValueComparator(term2termid);
	static ValueComparator d2dID = new ValueComparator(doc2docid);
	
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
	
	//Helper method for addid docIDs and docs to the docid2doc Map
	private static void addDocID2Doc(Integer ID, String URL){
		if(!docid2doc.containsKey(ID)){
			docid2doc.put(ID, URL);
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
	
	//returns for 5 docIDs in docid2tf_idf given a termID
	private static ArrayList<Integer> getTopDocIds(String termID){
		HashMap<Integer, Double> docidtf_idf = termid2docidtf_idf.get(termID);
		ArrayList<Double> tf_idf = new ArrayList(docidtf_idf.values());
		Collections.sort(tf_idf);
		ArrayList<Integer> ret = new ArrayList<Integer>();
		return ret;
	}
	
	//returns top websites given a word
	private static String searchWord(String input){
		int termID = term2termid.get(input);

		
		
		return "hello";
	}
	
	public static void main(String[] args){
		
		WordFrequencyCounter.storeStopWords();
		Data data = null;
		ObjectMapper  mapper = new ObjectMapper();
		//"c://users/Ahsan/Documents/CS121/FileDump" Big Dump
		//"c://users/Ahsan/Workspace/Java/Project3/FileDump Small Dump"
		File folder = new File ("c://users/ahsanm/Desktop/FileDump"); 

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
				
				addDocID2Doc(id, URL);
				
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
		
		addTermID2DocIDtf_idf();


		System.out.println("Done");
		Scanner reader = new Scanner(System.in);
		while(true){
			System.out.println("What would you like to search for? ");
			String input = reader.nextLine();
			System.out.println(searchWord(input));
		}
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

