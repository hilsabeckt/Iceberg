package main;
import java.util.*;
//import java.io.*;
public class Main {
	
	public static void main(String[] args) {
		LocationDataPoint test = new LocationDataPoint();
		HashMap<String, LocationDataPoint> JAVAdataset= test.createDataset("ucur","July");
		JAVAdataset = test.addDataset("vcur", "July",JAVAdataset);
		JAVAdataset = test.addDataset("sst_mean", "July", JAVAdataset);
		JAVAdataset = test.cleanDataset(JAVAdataset);
		/*
		String delims = ", ";
		List listLat = new ArrayList<String>();
		List listLon = new ArrayList<String>();
		
		String arr[] = new String[JAVAdataset.size()];
		for (String key : JAVAdataset.keySet()) {
			arr = key.split(delims);
			double lat = Double.valueOf(arr[0]);
			double lon = Double.valueOf(arr[1]);
			if (lon > 180) {
				lon = lon-360;
			}
			listLat.add(lat);
			listLon.add(lon);	
		}
		*/
		Search testSearch = new Search();
		testSearch.aStar(JAVAdataset.get("57.5, 322.5"), JAVAdataset.get("34.5, 140.5"), JAVAdataset, "Time", 20000e3 , 50.);
	}
}
