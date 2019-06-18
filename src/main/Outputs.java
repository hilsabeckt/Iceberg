package main;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Outputs {
	public static void plotResults(Result result) {
		
		String Water = result.getWaterString();
		String Time = result.getTimeString();
		double realStartLat = result.getRealStartLat();
		double realStartLon = result.getRealStartLon();
		double realEndLat = result.getRealEndLat();
		double realEndLon = result.getRealEndLon();
		
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
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				reader.close();
				writer.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//Open map.html for user to see.
		
		File htmlFile = new File("map.html");
		try {
		Desktop.getDesktop().browse(htmlFile.toURI());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
