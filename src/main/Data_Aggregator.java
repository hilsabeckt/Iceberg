package main;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 



public class Data_Aggregator {
	public JSONObject openFile(String filename){
		JSONObject dataset = new JSONObject();
		FileReader reader = null;
		try {
			reader = new FileReader(filename);
			Object obj = new JSONParser().parse(reader);
			dataset = (JSONObject) obj;
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		finally {
			try {
				reader.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dataset;
	}
	
}
