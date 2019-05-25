package main;

import java.net.*;
import java.io.*;

public class HttpClient {

	private static HttpURLConnection connection;
	
	
	public static void main(String[] args) {
		try {
			URL url = new URL("https://www.ncdc.noaa.gov/cdo-web/api/v2/datasets?limit=1000");
			connection = (HttpURLConnection) url.openConnection();
			
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			connection.setRequestProperty("Token","EobGrktjaVViRUyjqzSbiQkpbEiKpHDA");
			
	        BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
	        	String inputLine;
	        	while ((inputLine = in.readLine()) != null) 
	        		System.out.println(inputLine);
	        	in.close();
		
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}