package Index;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class index{
	public static void main(String[] args){
		Data data = null;
		ObjectMapper  mapper = new ObjectMapper();
		File folder = new File ("c://users/Ahsan Memon/workspaceJava/Project3/FileDump"); 
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> tokens = new ArrayList<String>();
		for (File file : listOfFiles){
			try {
				data = mapper.readValue(file, Data.class);
				tokens = Utilities.tokenizeFile(data.getText());
				
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
			System.out.println(tokens);
		}
		
		
	}
	
}
