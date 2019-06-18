package main;
import java.util.*;

import org.json.simple.JSONObject; 

public class LocationDataPoint {
	double lat;
	double lon ;
	double sst = Double.NaN;
	double ucur = Double.NaN;
	double vcur = Double.NaN;
	HashSet<LocationDataPoint> neighbors;
	double f = Double.MAX_VALUE;
	double g = Double.MAX_VALUE;
	double h;
	Iceberg iceberg;
	double time;
	HashMap<LocationDataPoint, Double> NeighborsF;
	HashMap<LocationDataPoint, Double> NeighborsG;
	
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
	public HashSet<LocationDataPoint> getNeighbors() {
		return neighbors;
	}
	public double getF() {
		return f;
	}
	public double getG() {
		return g;
	}
	public double getH() {
		return h;
	}
	public HashMap<LocationDataPoint, Double> getNeighborsF() {
		return NeighborsF;
	}
	public HashMap<LocationDataPoint, Double> getNeighborsG() {
		return NeighborsG;
	}
	public Iceberg getIceberg() {
		return iceberg;
	}
	public double getTime() {
		return time;
	}
	private void setLat(double newValue) {
		lat = newValue;
	}
	private void setLon(double newValue) {
		lon = newValue;
	}
	private void setSst(double newValue) {
		sst = newValue;
	}
	private void setUcur(double newValue) {
		ucur = newValue;
	}
	private void setVcur(double newValue) {
		vcur = newValue;
	}
	public void setF(double newValue) {
		f = newValue;
	}
	public void setG(double newValue) {
		g = newValue;
	}
	public void setH(double newValue) {
		h = newValue;
	}
	public void setNeighborsF(HashMap<LocationDataPoint, Double> newValue) {
		NeighborsF = newValue;
	}
	public void setNeighborsG(HashMap<LocationDataPoint, Double> newValue) {
		NeighborsG = newValue;
	}
	public void setIceberg(Iceberg newIceberg) {
		iceberg = newIceberg;
	}
	public void setTime(double newValue) {
		time = newValue;
	}
	private void createNeighbors(HashMap<String, LocationDataPoint> dataset) {
		Set<String> keys = dataset.keySet();
			neighbors = new HashSet<LocationDataPoint>();
			String lat0 = Double.toString(lat);
			String latplus = Double.toString(lat+1);
			String latminus = Double.toString(lat-1);
			String lon0 = Double.toString(lon);	
			double correctlonplus;
			double correctlonminus;
			if (lon == 359.5) {
				correctlonplus = 0.5;
			}
			else {
				correctlonplus = lon + 1;
			}
			if (lon == 0.5) {
				correctlonminus = 359.5;
			}
			else {
				correctlonminus = lon - 1;
			}
			String lonplus = Double.toString(correctlonplus);
			String lonminus = Double.toString(correctlonminus);;

			//Check North
			String north = latplus + ", " + lon0;
			if (keys.contains(north)) {
				neighbors.add(dataset.get(north));
			}
			//Check South
			String south = latminus + ", " + lon0;
			if (keys.contains(south)) {
				neighbors.add(dataset.get(south));
			}
			//Check East
			String east = lat0 + ", " + lonplus;
			if (keys.contains(east)) {
				neighbors.add(dataset.get(east));
			}
			//Check West
			String west = lat0 + ", " + lonminus;
			if (keys.contains(west)) {
				neighbors.add(dataset.get(west));
		}
			
			//Check NW
			String nw = latplus + ", " + lonminus;
			if (keys.contains(nw)) {
				neighbors.add(dataset.get(nw));
		}
			//Check NE
			String ne = latplus + ", " + lonplus;
			if (keys.contains(ne)) {
				neighbors.add(dataset.get(ne));
		}
			//Check SW
			String sw = latminus + ", " + lonminus;
			if (keys.contains(sw)) {
				neighbors.add(dataset.get(sw));
		}
			//Check SE
			String se = latminus + ", " + lonplus;
			if (keys.contains(se)) {
				neighbors.add(dataset.get(se));
		}
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, LocationDataPoint> createDataset(String dataType, String month) {
		HashMap<String, LocationDataPoint> JAVAdataset = new HashMap<String, LocationDataPoint>();
		Data_Aggregator dataset = new Data_Aggregator();
		String filename = "data/" +dataType+"_" +month+".json";
		JSONObject JSONdataset = dataset.openFile(filename);
		Set<String> JSONkeys = JSONdataset.keySet();
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
		return JAVAdataset;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, LocationDataPoint> addDataset(String dataType, String month, HashMap<String, LocationDataPoint> JAVAdataset) {
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

	public static HashMap<String, LocationDataPoint> cleanDataset(HashMap<String, LocationDataPoint> JAVAdataset) {
		Set<String> removeList = new HashSet<String>();
		Set<String> keys = JAVAdataset.keySet();
		for (String x : keys) {
			if (Double.isNaN(JAVAdataset.get(x).getSst()) ||
				Double.isNaN(JAVAdataset.get(x).getUcur()) ||
				Double.isNaN(JAVAdataset.get(x).getVcur())) {
				removeList.add(x);
			}
		}
		for (String k : removeList) {
			JAVAdataset.remove(k);
		}
		JAVAdataset.forEach((key,datapoint)->{
			datapoint.createNeighbors(JAVAdataset);
			;
		});

		return JAVAdataset;
	}
}
