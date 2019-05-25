package main;
import java.util.*;

import org.json.simple.JSONObject; 

public class LocationDataPoint {
	double lat;
	double lon ;
	double sst = 123.456;
	double ucur = 123.456;
	double vcur = 123.456;
	HashMap<String, LocationDataPoint> JAVAdataset;
	HashSet<String> neighbors;
	
	public double getLat() {
		return lat;
	}
	public double getLon() {
		return lon;
	}
	public double getSst() {
		return sst;
	}
	public double getUcur() {
		return ucur;
	}
	public double getVcur() {
		return vcur;
	}
	public Set<String> getNeighbors() {
		return neighbors;
	}
	public void setLat(double newValue) {
		lat = newValue;
	}
	public void setLon(double newValue) {
		lon = newValue;
	}
	public void setSst(double newValue) {
		sst = newValue;
	}
	public void setUcur(double newValue) {
		ucur = newValue;
	}
	public void setVcur(double newValue) {
		vcur = newValue;
	}
	public void createNeighbors(HashMap<String, LocationDataPoint> dataset) {
		Set<String> keys = dataset.keySet();
			neighbors = new HashSet <String>();
			//Check North
			String north = Double.toString(lat + 1) + ", " + Double.toString(lon);
			if (keys.contains(north)) {
				neighbors.add(north);
			}
			//Check South
			String south = Double.toString(lat - 1) + ", " + Double.toString(lon);
			if (keys.contains(south)) {
				neighbors.add(south);
			}
			//Check East
			String east = Double.toString(lat) + ", " + Double.toString(lon + 1);
			if (keys.contains(east)) {
				neighbors.add(east);
			}
			//Check West
			String west = Double.toString(lat) + ", " + Double.toString(lon - 1);
			if (keys.contains(west)) {
				neighbors.add(west);
		}
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, LocationDataPoint> createDataset(String dataType, String month) {
		Data_Aggregator dataset = new Data_Aggregator();
		String filename = "data/" +dataType+"_" +month+".json";
		JSONObject JSONdataset = dataset.openFile(filename);
		Set<String> JSONkeys = JSONdataset.keySet();
		if (JAVAdataset == null) {
			JAVAdataset = new HashMap<>();
		}
		int n = JSONdataset.size();
		String arr[] = new String[n];
		for (String x : JSONkeys) {
			LocationDataPoint datapoint = new LocationDataPoint();
			double value = (double) JSONdataset.get(x);
			String delims = ", ";
			arr = x.split(delims);
			datapoint.setLon(Double.valueOf(arr[1]));
			datapoint.setLat(Double.valueOf(arr[0]));
			
			//Checks Which Data to Add Here
			if (dataType.contentEquals("sst_mean")) {
			datapoint.setSst(value);
			}
			if (dataType.contentEquals("ucur")) {
				datapoint.setUcur(value);
				}
			if (dataType.contentEquals("vcur")) {
				datapoint.setVcur(value);
				}
			
			JAVAdataset.put(x, datapoint);
		}
		JAVAdataset.forEach((k,v)->{
			v.createNeighbors(JAVAdataset);
		});
		return JAVAdataset;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, LocationDataPoint> addDataset(String dataType, String month, HashMap<String, LocationDataPoint> JAVAdataset) {
		Data_Aggregator dataset = new Data_Aggregator();
		String filename = "data/" +dataType+"_" +month+".json";
		JSONObject JSONdataset = dataset.openFile(filename);
		Set<String> JSONkeys = JSONdataset.keySet();
		int n = JSONdataset.size();
		String arr[] = new String[n];
		for (String x : JSONkeys) {
			if (JAVAdataset.get(x) == null) {
				continue;
			}
			double value = (double) JSONdataset.get(x);
			String delims = ", ";
			arr = x.split(delims);
			
			Double checkLat = Double.valueOf(arr[0]);
			Double checkLon = Double.valueOf(arr[1]);
			//Checks Which Data to Add Here
			if (dataType.contentEquals("sst_mean")) {
				if (JAVAdataset.get(x).getLat() == (checkLat)) {
					if (JAVAdataset.get(x).getLon() == checkLon) {
						JAVAdataset.get(x).setSst(value);
					}
				}
			}
			if (dataType.contentEquals("ucur")) {
				if (JAVAdataset.get(x).getLat() == (checkLat)) {
					if (JAVAdataset.get(x).getLon() == checkLon) {
						JAVAdataset.get(x).setUcur(value);
					}
				}
				}
			if (dataType.contentEquals("vcur")) {
				if (JAVAdataset.get(x).getLat() == (checkLat)) {
					if (JAVAdataset.get(x).getLon() == checkLon) {
						JAVAdataset.get(x).setVcur(value);
					}
				}
				}
			
		}
		return JAVAdataset;
}

	public HashMap<String, LocationDataPoint> cleanDataset(HashMap<String, LocationDataPoint> JAVAdataset) {
		Set<String> removeList = new HashSet<String>();
		Set<String> keys = JAVAdataset.keySet();
		for (String x : keys) {
			if (JAVAdataset.get(x).getSst() == 123.456 ||
				JAVAdataset.get(x).getUcur() == 123.456 ||
				JAVAdataset.get(x).getVcur() == 123.456) {
				removeList.add(x);
			}
		}
		for (String k : removeList) {
			JAVAdataset.remove(k);
		}
		JAVAdataset.forEach((k,v)->{
			v.createNeighbors(JAVAdataset);
			System.out.println("Location: " + k + "		Neighbors: " + v.getNeighbors());
		});

		return JAVAdataset;
	}
}
