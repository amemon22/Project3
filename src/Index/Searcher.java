package Index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

class Searcher{
	
	private static HashMap<String, Integer> term2termid = new HashMap<String, Integer>();
	private static HashMap<String, Integer> doc2docid = new HashMap<String, Integer>();
	private static HashMap<Integer, String> docid2doc = new HashMap<Integer, String>();
	private static HashMap<Integer, ArrayList<Integer>> docid2termid = new HashMap<Integer, ArrayList<Integer>>();
	private static HashMap<Integer, String> termid2term = new HashMap<Integer, String>();
	private static HashMap<Integer, HashMap<Integer, Integer>> termid2docidfrequency = new HashMap<Integer, HashMap<Integer, Integer>>();
	private static HashMap<Integer, HashMap<Integer, Double>> termid2docidtf_idf = new HashMap<Integer, HashMap<Integer, Double>>();
	
	
	@SuppressWarnings({ "unchecked" })
	private static void loadMaps(){
		term2termid = (HashMap<String, Integer>) ObjectOutputStreamTest.loadObject(term2termid, "term2termid.txt");
		doc2docid = (HashMap<String, Integer>) ObjectOutputStreamTest.loadObject(doc2docid, "doc2docid.txt");
		docid2doc = (HashMap<Integer, String>) ObjectOutputStreamTest.loadObject(docid2doc, "docid2doc.txt");
		docid2termid = (HashMap<Integer, ArrayList<Integer>>) ObjectOutputStreamTest.loadObject(docid2termid, "docid2termid.txt");
		termid2term = (HashMap<Integer, String>) ObjectOutputStreamTest.loadObject(termid2term, "termid2term.txt");
		termid2docidfrequency = (HashMap<Integer, HashMap<Integer, Integer>>) ObjectOutputStreamTest.loadObject(termid2docidfrequency, "termid2docidfrequency.txt");
		termid2docidtf_idf = (HashMap<Integer, HashMap<Integer, Double>>) ObjectOutputStreamTest.loadObject(termid2docidtf_idf, "termid2docidtf_idf.txt");
	}
	
	
	//returns top websites given a word
	private static StringBuilder searchWord(String input){
			
			StringBuilder ret = new StringBuilder();
			if(term2termid.containsKey(input.toLowerCase())){
				int termID = term2termid.get(input.toLowerCase());
				ArrayList<Integer> docIDs = getTopDocIds(termID);
				
				for (Integer docID : docIDs){
					ret.append(docid2doc.get(docID) + "\n");
				}
			}
			else{
				ret.append("Cannot locate that word");
			}	
			return ret;
		}
	
	//returns for 5 docIDs in docid2tf_idf given a termID
	private static ArrayList<Integer> getTopDocIds(Integer termID){
		HashMap<Integer, Double> docidtf_idf = termid2docidtf_idf.get(termID);
		ValueComparator2 bvc = new ValueComparator2(docidtf_idf);
		TreeMap<Integer, Double> sorted_map = new TreeMap<Integer, Double>(bvc);
		ArrayList<Integer> ret = new ArrayList<Integer>();
		
		sorted_map.putAll(docidtf_idf);
		
		for (Map.Entry<Integer, Double> entry : sorted_map.entrySet()){
			if (ret.size() != 5){
				ret.add(entry.getKey());
			}
			else{
				break;
			}
		}
		return ret;
	}
		

	public static void main(String[] args){
		loadMaps();
		
		@SuppressWarnings("resource")
		Scanner reader = new Scanner(System.in);
		while(true){
			System.out.println("What would you like to search for? ");
			String input = reader.nextLine();
			System.out.println(searchWord(input));
		}
	}
		
	
}