package Index;

import java.io.*;
import java.util.HashMap;

public class ObjectOutputStreamTest{


	static HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
	static HashMap<String, Integer> doc2docid = new HashMap<String, Integer>();
	private static HashMap<Integer, HashMap<Integer, Double>> termid2docidtf_idf = new HashMap<Integer, HashMap<Integer, Double>>();
	
	//Put this method in index.java
	//Write the output stream of a map object to a particular file
	public static void storeObject(Object inputMap, String filename){
        
        OutputStream ops = null;
        ObjectOutputStream objOps = null;
        try {
            ops = new FileOutputStream(filename);
            objOps = new ObjectOutputStream(ops);
            objOps.writeObject(inputMap);
            objOps.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try{
                if(objOps != null) objOps.close();
            } catch (Exception ex){
                 
            }
        }
         
    }
	
	//Keep this method in the search class
	//Call this method to retrieve the hashmaps from text files the index output streamed to
	public static Object loadObject(Object index, String filename){

        try {
        	InputStream fileIs = new BufferedInputStream( new FileInputStream(filename));
	        ObjectInputStream objIs = new ObjectInputStream(fileIs);
            index = objIs.readObject();
            objIs.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
		return index; 
         
    }

}
