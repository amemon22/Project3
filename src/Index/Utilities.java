//Ahsan Memon 59212236
//Evan Munemura 43513937

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
			if (str.length() > 1){
				tokens.add(str.toLowerCase());
			}
		}
		return tokens;
	}
}