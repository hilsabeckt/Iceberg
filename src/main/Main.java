package main;
import java.util.*;
import java.awt.Desktop;
import java.io.*;

public class Main {
	
	public static void main(String[] args){
		String month;
		
		//Ask User for Month. Check to see if Valid.
		
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
		
		//Ask User for Number of Boats. Check to see if Valid.
		
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
		
		//Ask User for Size of Iceberg. Check to see if Valid.
		//Size Classifications based on "Modelling the dynamics and thermodynamics of icebergs" (1997).
		
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
		
		
		Double StartLat;
		Double StartLon;
		Double EndLat;
		Double EndLon;
		
		//Ask User for Start and End Location. Check to see if Valid.
		
		while (true) {
			try {
				InputStreamReader input = new InputStreamReader(System.in);
				BufferedReader reader = new BufferedReader(input);
				System.out.print("Starting Latitude: ");
				String lat = reader.readLine();
				if  (Double.valueOf(lat) < -90 || Double.valueOf(lat) > 90)
					{
					System.out.println("Please input valid latitude (-90 to 90).");
					continue;
				}
				System.out.print("Starting Longitude: ");
				String lon = reader.readLine();
				if  (Double.valueOf(lon) < -180 || Double.valueOf(lon) > 360)
					{
					System.out.println("Please input valid longitude (-180 to 180).");
					continue;
				}
				StartLat = Double.valueOf(lat);
				StartLat = Math.floor(StartLat) + .5;
				StartLon = Double.valueOf(lon);
				if (StartLon < 0) {
					StartLon = StartLon + 360;
				}
				StartLon = Math.floor(StartLon) + .5;
				break;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			catch (NumberFormatException e) {
				System.out.println("Please input valid number.");
				continue;
			}
		}
		
		
		while (true) {
			try {
				InputStreamReader input = new InputStreamReader(System.in);
				BufferedReader reader = new BufferedReader(input);
				System.out.print("Ending Latitude: ");
				String lat = reader.readLine();
				if  (Double.valueOf(lat) < -90 || Double.valueOf(lat) > 90)
					{
					System.out.println("Please input valid latitude (-90 to 90).");
					continue;
				}
				System.out.print("Ending Longitude: ");
				String lon = reader.readLine();
				if  (Double.valueOf(lon) < -180 || Double.valueOf(lon) > 360)
					{
					System.out.println("Please input valid longitude (-180 to 180).");
					continue;
				}
				EndLat = Double.valueOf(lat);
				EndLat = Math.floor(EndLat) + .5;
				EndLon = Double.valueOf(lon);
				if (EndLon < 0) {
					EndLon = EndLon + 360;
				}
				EndLon = Math.floor(EndLon) + .5;
				break;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			catch (NumberFormatException e) {
				System.out.println("Please input valid number.");
				continue;
			}
		}
		Double startDistance = Double.MAX_VALUE;
		Double endDistance = Double.MAX_VALUE;
		String start = "";
		String end = "";
		Double realStartLat = Double.NaN;
		Double realStartLon = Double.NaN;
		Double realEndLat = Double.NaN;
		Double realEndLon = Double.NaN;
		
		Set<String> keySet = JAVAdataset.keySet();
		Set<String> checked = new HashSet<>();
		String Water = "";
		String Time = "";
		
		/*Checks to see if Start/End Location are known data points.
		 *If not a known data point:
		 *Find closest point in known data points to user-defined point.
		 *If no path available from Start to End:
		 *Find next closest data point and repeat.
		 */
		
		while (true) {
		startDistance = Double.MAX_VALUE;
		endDistance = Double.MAX_VALUE;
		try {
			if (keySet.contains(StartLat + ", " + StartLon)) {
				start = StartLat + ", " + StartLon;
				realStartLat = StartLat;
				realStartLon = StartLon;	
			}
			else {
			for (String key : keySet) {
				if (checked.contains(key)) {
					continue;
				}
				Double lat2 = JAVAdataset.get(key).getLat();
				Double lon2 = JAVAdataset.get(key).getLon();
				Double tempDistance = Model.CalcDistance(StartLat, StartLon, lat2, lon2);
				if (tempDistance < startDistance) {
					start = key;
					realStartLat = lat2;
					realStartLon = lon2;
					startDistance = tempDistance;
					}
				}
			}	
			
			if (keySet.contains(EndLat + ", " + EndLon)) {
				end = EndLat + ", " + EndLon;
				realEndLat = EndLat;
				realEndLon = EndLon;	
			}
			else {
				for (String key : keySet) {
					if (checked.contains(key)) {
						continue;
					}
					Double lat2 = JAVAdataset.get(key).getLat();
					Double lon2 = JAVAdataset.get(key).getLon();
					Double tempDistance = Model.CalcDistance(EndLat, EndLon, lat2, lon2);
					if (tempDistance < endDistance) {
						end = key;
						realEndLat = lat2;
						realEndLon = lon2;
						endDistance = tempDistance;
					}
				}
			}
		Water = new Search().aStar(JAVAdataset.get(start), JAVAdataset.get(end), JAVAdataset, "Water", 4270290.*numberBoats, icebergClass);
		Time = new Search().aStar(JAVAdataset.get(start), JAVAdataset.get(end), JAVAdataset, "Time", 4270290.*numberBoats, icebergClass);
		break;
		}
		/*Catches NullPointerException.
		 *If Caught, this mean points do not connect (lake, sea, etc.):
		 *Add to list of checked points and repeat.
		 */
		catch (NullPointerException e) {
			checked.add(end);
			continue;
		}
		}
		
		File templateFile = new File("template.html");
		String originalText = "";
		BufferedReader reader = null;
		FileWriter writer = null;
		
		//In order to plot our path, open the template.html file and replace labels with correct values.
		
		try {
			reader = new BufferedReader(new FileReader(templateFile));
			String line = reader.readLine();
			
			while (line != null) {
				originalText = originalText + line + System.lineSeparator();
				line = reader.readLine();
			}
			
			String newText = originalText.replaceAll("\\$waterPathSet", Water);
			newText = newText.replaceAll("\\$timePathSet", Time);
			newText = newText.replaceAll("\\$startPostion", "\t\t\tposition: {lat: " + realStartLat + ", lng: " + realStartLon + "},");
			newText = newText.replaceAll("\\$endPostion", "\t\t\tposition: {lat: " + realEndLat + ", lng: " + realEndLon + "},");
			
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
		
		//Open map.html for user to see.
		
		File htmlFile = new File("map.html");
		try {
		Desktop.getDesktop().browse(htmlFile.toURI());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}