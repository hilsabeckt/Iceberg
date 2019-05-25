package main;

import java.util.HashMap;

public class Main {
	
	public static void main(String[] args) {
		LocationDataPoint test = new LocationDataPoint();
		HashMap<String, LocationDataPoint> JAVAdataset= test.createDataset("ucur","July");
		JAVAdataset = test.addDataset("vcur", "July",JAVAdataset);
		JAVAdataset = test.addDataset("sst_mean", "July", JAVAdataset);
		JAVAdataset = test.cleanDataset(JAVAdataset);
	}
}

