package main;
import java.util.*;
import java.awt.Desktop;
import java.io.*;

public class Main {
	
	public static void main(String[] args){
		String month;
		while (true) {
			try {
				InputStreamReader input = new InputStreamReader(System.in);
				BufferedReader reader = new BufferedReader(input);
				System.out.print("Month: ");
				month = reader.readLine();
				if  (!month.contentEquals("January") && (!month.contentEquals("February")) && (!month.contentEquals("March")) &&
					(!month.contentEquals("April")) && (!month.contentEquals("May")) && (!month.contentEquals("June")) &&
					(!month.contentEquals("July")) && !month.contentEquals("August") && !month.contentEquals("September") &&
					(!month.contentEquals("October")) && (!month.contentEquals("November")) && (!month.contentEquals("December")))
					{
					System.out.println("Please input valid month. (Uppercase)");
					continue;
				}
				break;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		int numberBoats;
		while (true) {
			try {
				InputStreamReader input = new InputStreamReader(System.in);
				BufferedReader reader = new BufferedReader(input);
				System.out.print("Number of Tugboats: ");
				String boats = reader.readLine();
				if  (Integer.valueOf(boats) <= 0){
					System.out.println("Please input valid number of tugboats.");
					continue;
				}
				numberBoats = Integer.valueOf(boats);
				break;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			catch (NumberFormatException e) {
				System.out.println("Please input valid number of tugboats.");
				continue;
			}
		}
		
		int icebergClass;
		while (true) {
			try {
				InputStreamReader input = new InputStreamReader(System.in);
				BufferedReader reader = new BufferedReader(input);
				System.out.print("Iceberg Size (1-10): ");
				String size = reader.readLine();
				if  (Integer.valueOf(size) <= 0){
					System.out.println("Please input valid size of iceberg.");
					continue;
				}
				icebergClass = Integer.valueOf(size);
				break;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			catch (NumberFormatException e) {
				System.out.println("Please input valid size of iceberg.");
				continue;
			}
		}
		
		
		LocationDataPoint test = new LocationDataPoint();
		HashMap<String, LocationDataPoint> JAVAdataset= test.createDataset("ucur",month);
		JAVAdataset = test.addDataset("vcur", month,JAVAdataset);
		JAVAdataset = test.addDataset("sst_mean", month, JAVAdataset);
		JAVAdataset = test.cleanDataset(JAVAdataset);
		
		//Ask for Starting LatLon and EndLatLon
		//Floor then add .5 to each
		//Check to see if value points
		//if not, move LatLon in direction of goal and check again
		//Note: 360 + lon if negative = adjusted
		
		double startLat = 57.5;
		double startLon = 322.5;
		double endLat = 34.5;
		double endLon = 140.5;
		String start = startLat + ", " + startLon;
		String end = endLat + ", " + endLon;
		
		String Water = new Search().aStar(JAVAdataset.get(start), JAVAdataset.get(end), JAVAdataset, "Water", 4270290.*numberBoats, icebergClass);
		String Time = new Search().aStar(JAVAdataset.get(start), JAVAdataset.get(end), JAVAdataset, "Time", 4270290.*numberBoats, icebergClass);
		
		File templateFile = new File("template.html");
		String originalText = "";
		BufferedReader reader = null;
		FileWriter writer = null;
		
		try {
			reader = new BufferedReader(new FileReader(templateFile));
			String line = reader.readLine();
			
			while (line != null) {
				originalText = originalText + line + System.lineSeparator();
				line = reader.readLine();
			}
			
			String newText = originalText.replaceAll("\\$waterPathSet", Water);
			newText = newText.replaceAll("\\$timePathSet", Time);
			newText = newText.replaceAll("\\$startPostion", "\t\t\tposition: {lat: " + startLat + ", lng: " + startLon + "},");
			newText = newText.replaceAll("\\$endPostion", "\t\t\tposition: {lat: " + endLat + ", lng: " + endLon + "},");
			
			writer = new FileWriter("map.html");
			writer.write(newText);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				reader.close();
				writer.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		File htmlFile = new File("map.html");
		try {
		Desktop.getDesktop().browse(htmlFile.toURI());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}