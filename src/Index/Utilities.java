package Index;

import java.util.ArrayList;


public class Utilities{
/**
 * Modified original Utilities class so that it takes in Strings rather 
 * than files. Used for tokenizing text in files.	
 */
	
	public static ArrayList<String> tokenizeFile(String input) {
			
		ArrayList<String> tokens = new ArrayList<String>();
		for (String str : input.split("\\W")){
			if (str.length() > 1 && !WordFrequencyCounter.getStopwords().contains(str.toLowerCase())){  //if the string length is greater and 1 and is not a stopword
				tokens.add(str.toLowerCase());
			}
		}
		return tokens;
	}
}