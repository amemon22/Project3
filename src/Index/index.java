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
	
	//Comparators for sorting our maps
	static ValueComparator t2tID = new ValueComparator(term2termid);
	static ValueComparator d2dID = new ValueComparator(doc2docid);
	
	//TreeMaps for our sorted Maps
	private static TreeMap<String, Integer> sortedterm2termid = new TreeMap<String, Integer>(t2tID);
	private static TreeMap<String, Integer> sorteddoc2docid = new TreeMap<String, Integer>(d2dID);
	
	//Counters for various IDs
	private static Integer term2termidCounter = 0;
	
	//Helper method for adding unique tokens for our term2termID Map
	private static void addTerm2TermID(ArrayList<String> tokens){
		for (String token : tokens){
			if (!term2termid.containsKey(token)){
				term2termidCounter++;
				term2termid.put(token, term2termidCounter);
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
	
	public static void main(String[] args){
		Data data = null;
		ObjectMapper  mapper = new ObjectMapper();
		//"c://users/Ahsan/Documents/CS121/FileDump" Big Dump
		//"c://users/Ahsan/Workspace/Java/Project3/FileDump Small Dump"
		File folder = new File ("c://users/Ahsan/Documents/CS121/FileDump"); 
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> tokens = new ArrayList<String>();
		String URL;
		int id;
		
		for (File file : listOfFiles){
			try {
				data = mapper.readValue(file, Data.class);
				tokens = Utilities.tokenizeFile(data.getText());
				addTerm2TermID(tokens);
				
				id = data.getID();
				URL = data.get_ID();
				addDoc2DocID(URL,id);
				
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		sortedterm2termid.putAll(term2termid);
		sorteddoc2docid.putAll(doc2docid);
		writeToFile(sortedterm2termid.toString(), "term2termid.txt");
		writeToFile(sorteddoc2docid.toString(), "doc2docid.txt");
		//System.out.println("Term2TermID: " + sortedterm2termid.toString());
		//System.out.println("Doc2DocID: " + sorteddoc2docid.toString());
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
