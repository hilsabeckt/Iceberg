package main;

public class Result {
	String Water;
	String Time;
	double realStartLat;
	double realStartLon;
	double realEndLat;
	double realEndLon;
	
	public String getWaterString() {
		return Water;
	}
	
	public String getTimeString() {
		return Time;
	}
	
	public double getRealStartLat() {
		return realStartLat;
	}
	
	public double getRealStartLon() {
		return realStartLon;
	}
	
	public double getRealEndLat() {
		return realEndLat;
	}
	
	public double getRealEndLon() {
		return realEndLon;
	}
	
	public void setWaterString(String newValue) {
		Water = newValue;
	}
	
	public void setTimeString(String newValue) {
		Time = newValue;
	}
	
	public void setRealStartLat(double newValue) {
		realStartLat = newValue;
	}
	
	public void setRealStartLon(double newValue) {
		realStartLon = newValue;
	}
	
	public void setRealEndLat(double newValue) {
		realEndLat = newValue;
	}
	public void setRealEndLon(double newValue) {
		realEndLon = newValue;
	}
}
