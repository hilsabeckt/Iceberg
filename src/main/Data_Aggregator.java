package main;
import java.io.FileReader;
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
		catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			try {
				reader.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dataset;
	}
}
