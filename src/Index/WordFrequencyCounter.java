//Ahsan Memon 59212236
//Evan Munemura 4351393

package Index;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/**
 * Counts the total number of words and their frequencies in a text file.
 */
public final class WordFrequencyCounter {
	
	/**
	 * This class should not be instantiated.
	 */
	
	static List<String> stopwords = new ArrayList<String>();
	
	private WordFrequencyCounter() {}
	
	/**
	 * Takes the input list of words and processes it, returning a list
	 * of {@link Frequency}s.
	 * 
	 * This method expects a list of lowercase alphanumeric strings.
	 * If the input list is null, an empty list is returned.
	 * 
	 * There is one frequency in the output list for every 
	 * unique word in the original list. The frequency of each word
	 * is equal to the number of times that word occurs in the original list. 
	 * 
	 * The returned list is ordered by decreasing frequency, with tied words sorted
	 * alphabetically.
	 * 
	 * The original list is not modified.
	 * 
	 * Example:
	 * 
	 * Given the input list of strings 
	 * ["this", "sentence", "repeats", "the", "word", "sentence"]
	 * 
	 * The output list of frequencies should be 
	 * ["sentence:2", "the:1", "this:1", "repeats:1",  "word:1"]
	 *  
	 * @param words A list of words.
	 * @return A list of word frequencies, ordered by decreasing frequency.
	 */
	public static ArrayList <Frequency> computeWordFrequencies(ArrayList<String> words) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		ArrayList <Frequency> ret = new ArrayList<Frequency>();
		
		for (String word: words){ 
			if (map.containsKey(word)){
				map.put(word, map.get(word) + 1);
			}
			else{
				map.put(word, 1);
			}
		}
	
		for (Map.Entry<String, Integer> entry : map.entrySet()){
			ret.add(new Frequency(entry.getKey(),entry.getValue()));
		}
		
		Collections.sort(ret,new Comparator<Frequency>(){
			@Override
			public int compare(Frequency o1, Frequency o2) {
				// reversed sequence
				int cmp = o2.getFrequency() - o1.getFrequency();
				if (cmp == 0){
					cmp = o1.getText().compareTo(o2.getText());
				}
				return cmp;
			}
		});
		
		return ret;
	}
	
	public static void storeStopWords(){
		File file = new File("stopwords.txt");
		Scanner scanner;
		try {
			scanner = new Scanner(file);
			while (scanner.hasNextLine()){
				stopwords.add(scanner.nextLine());
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
	}
	
	public static List<String> getStopwords(){
		return stopwords;
	}
	
}
