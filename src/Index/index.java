package Index;
import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class index{
	public static void main(String[] args){
		Data data = null;
		ObjectMapper  mapper = new ObjectMapper();
		try {
			data = mapper.readValue(new File("c://users/Ahsan Memon/workspaceJava/Project3/FileDump/1.txt"), Data.class);
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
		System.out.println(data);
		
	}
	
}
