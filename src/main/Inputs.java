package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Inputs {

	public static String getMonthFromUser(){
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		String month = "";
		
		//Ask User for Month. Check to see if Valid.
		
		boolean validMonth = false;
		String validMonthString = "Please input valid month. (e.g. June)";
		while (!validMonth) {
			try {
				System.out.print("Month: ");
				month = reader.readLine();
				if  (!month.contentEquals("January") && (!month.contentEquals("February")) && (!month.contentEquals("March")) &&
					(!month.contentEquals("April")) && (!month.contentEquals("May")) && (!month.contentEquals("June")) &&
					(!month.contentEquals("July")) && !month.contentEquals("August") && !month.contentEquals("September") &&
					(!month.contentEquals("October")) && (!month.contentEquals("November")) && (!month.contentEquals("December")))
					{
					System.out.println(validMonthString);
				}
				else {
					validMonth = true;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println(validMonthString);
			}
		}
		return month;
	}
	
	public static int getNumberOfBoatsFromUser(){
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		int numberBoats = -1;
		
		//Ask User for Number of Boats. Check to see if Valid.
		
		boolean validNumberOfBoats = false;
		String validNumberOfBoatsString = "Please input valid number of tugboats. (e.g. 1)";
		while (!validNumberOfBoats) {
			try {
				System.out.print("Number of Tugboats: ");
				String boats = reader.readLine();
				if  (Integer.valueOf(boats) <= 0){
					System.out.println(validNumberOfBoatsString);
				}
				else {
					numberBoats = Integer.valueOf(boats);
					validNumberOfBoats = true;
				}
			}
			catch (Exception e) {
				System.out.println(validNumberOfBoatsString);
			}
		}
		return numberBoats;
	}
	
	public static int getIcebergClassFromUser(){
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		int icebergClass = -1;
		
		//Ask User for Size of Iceberg. Check to see if Valid.
		//Size Classifications based on "Modelling the dynamics and thermodynamics of icebergs" (1997).
		
		boolean validIcebergClass = false;
		String validIcebergClassString = "Please input valid class of iceberg. (e.g. 2)";
		while (!validIcebergClass) {
			try {
				System.out.print("Iceberg Size (1-10): ");
				String size = reader.readLine();
				if  (Integer.valueOf(size) <= 0 || Integer.valueOf(size) > 10){
					System.out.println(validIcebergClassString);
				}
				else {
					icebergClass = Integer.valueOf(size);
					validIcebergClass = true;
				}	
			}
			catch (Exception e) {
				System.out.println(validIcebergClassString);
			}
		}
		return icebergClass;
	}
	
	public static double getLatitude(String point){
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		double lat = -1;
		
		//Ask User for Latitude. Check to see if Valid.
		
		boolean validLatitude = false;
		String validLatitudeString = "Please input valid latitude. (-90 to 90)";
		while (!validLatitude) {
			try {
				System.out.print(point + " Latitude: ");
				String latitude = reader.readLine();
				if  (Double.valueOf(latitude) < -90 || Double.valueOf(latitude) > 90){
					System.out.println(validLatitudeString);
				}
				else {
					lat = Double.valueOf(latitude);
					validLatitude = true;
				}	
			}
			catch (Exception e) {
				System.out.println(validLatitudeString);
			}
		}
		lat = Math.floor(lat) + .5;
		return lat;
	}
	
	public static double getLongitude(String point){
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		double lon = -1;
		
		//Ask User for Longitude. Check to see if Valid.
		
		boolean validLongitude = false;
		String validLongitudeString = "Please input valid longitude. (-180 to 180)";
		while (!validLongitude) {
			try {
				System.out.print(point + " Longitude: ");
				String longitude = reader.readLine();
				if  (Double.valueOf(longitude) < -180 || Double.valueOf(longitude) > 360){
					System.out.println(validLongitudeString);
				}
				else {
					lon = Double.valueOf(longitude);
					validLongitude = true;
				}	
			}
			catch (Exception e) {
				System.out.println(validLongitudeString);
			}
		}
		if (lon < 0) {
			lon = lon + 360;
		}
		lon = Math.floor(lon) + .5;
		return lon;
	}
	
	
}
