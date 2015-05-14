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
	
	private static HashMap<String, Long> term2termid = new HashMap<String, Long>();
	ValueComparator bvc = new ValueComparator(term2termid);
	private static TreeMap<String, Long> sortedterm2termid = new TreeMap<String, Long>();
	private static long term2termidCounter = 0;
	
	private static void addTerm2TermID(ArrayList<String> tokens){
		/**Takes the tokens and adds them to our term2termid map*/
		for (String token : tokens){
			if (!term2termid.containsKey(token)){
				term2termidCounter++;
				term2termid.put(token, term2termidCounter);
			}
		}
	}
	
	private static void writeToFile(String text, String filename){
		PrintWriter writer;
		try {
			writer = new PrintWriter(filename, "UTF-8");
			writer.write(text);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		Data data = null;
		ObjectMapper  mapper = new ObjectMapper();
		//"c://users/Ahsan/Documents/CS121/FileDump"
		File folder = new File ("c://users/Ahsan/Workspace/Java/Project3/FileDump"); 
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> tokens = new ArrayList<String>();
		for (File file : listOfFiles){
			try {
				data = mapper.readValue(file, Data.class);
				tokens = Utilities.tokenizeFile(data.getText());
				addTerm2TermID(tokens);
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//writeToFile(term2termid.toString(), "output.txt");
		sortedterm2termid.putAll(term2termid);
		System.out.println(sortedterm2termid.toString());
		
	}
	
}

class ValueComparator implements Comparator<String> {

    HashMap<String, Long> base;
    public ValueComparator(HashMap<String, Long> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}
