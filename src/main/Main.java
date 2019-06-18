package main;
import java.util.*;

public class Main {
	
	public static void main(String[] args){
		
		//Gather Inputs.
		String month = Inputs.getMonthFromUser();
		int numberBoats = Inputs.getNumberOfBoatsFromUser();
		int icebergClass = Inputs.getIcebergClassFromUser();
		double startLat = Inputs.getLatitude("Start");
		double startLon = Inputs.getLongitude("Start");
		double endLat = Inputs.getLatitude("End");
		double endLon = Inputs.getLongitude("End");	
			
		//Establish Dataset.
		HashMap<String, LocationDataPoint> JAVAdataset= LocationDataPoint.createDataset("ucur",month);
		JAVAdataset = LocationDataPoint.addDataset("vcur", month,JAVAdataset);
		JAVAdataset = LocationDataPoint.addDataset("sst_mean", month, JAVAdataset);
		JAVAdataset = LocationDataPoint.cleanDataset(JAVAdataset);
		
		//Search and Return Result.
		Result result = Search.implementSearch(JAVAdataset, startLat, startLon, endLat, endLon, numberBoats, icebergClass);
		
		//Plot Results.
		Outputs.plotResults(result);
	}
}