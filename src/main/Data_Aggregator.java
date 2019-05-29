package main;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 



public class Data_Aggregator {
	public JSONObject openFile(String filename){
		JSONObject dataset = new JSONObject();
		try {
		Object obj = new JSONParser().parse(new FileReader(filename));
		dataset = (JSONObject) obj;
	}
	catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ParseException e) {
		e.printStackTrace();
	}
		
		return dataset;
}
	
	public Object convertJSONtoObj(JSONObject dataset) {
		Object test = new Object();
		return test;
	}

	
}
