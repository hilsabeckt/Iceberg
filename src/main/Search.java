package main;
import java.util.*;

public class Search extends Model{

	public String aStar(LocationDataPoint startLocation, LocationDataPoint endLocation,
				      HashMap<String,LocationDataPoint> JAVAdataset, String heuristic,
				      Double boatPower, Integer icebergSize) throws NullPointerException{
		

		Set<LocationDataPoint> closedPoints = new HashSet<LocationDataPoint>();
		Map<LocationDataPoint, LocationDataPoint> path = new HashMap<LocationDataPoint, LocationDataPoint>();
		
		Comparator<LocationDataPoint> comparator = new Comparator<LocationDataPoint>() {
			public int compare(LocationDataPoint i, LocationDataPoint j) {
				if (i.getF() > j.getF()) {
					return 1;
				}
				else if (i.getF() < j.getF()) {
					return -1;
				}
				else {return 0;}
			}
		};
	
		PriorityQueue<LocationDataPoint> queue = new PriorityQueue<LocationDataPoint>(1000,comparator);
		startLocation.setG(0);
		startLocation.setTime(0);
		Iceberg iceberg = new Iceberg();
		iceberg.setSize(icebergSize);
		Iceberg startIceberg = iceberg.cloneIceberg();
		startLocation.setIceberg(startIceberg);
		queue.add(startLocation);
		
		Iceberg miniIceberg = new Iceberg();
		miniIceberg.setSize(1);
		JAVAdataset.forEach((key,datapoint)->{
			datapoint.setH(0);
		});
		
		while ((!queue.isEmpty())){
			LocationDataPoint current = queue.poll();
			if (current == endLocation) {
				break;
			}
			closedPoints.add(current);
			for (LocationDataPoint neighbor : current.getNeighbors()) {
				if (closedPoints.contains(neighbor)) {
					continue;
				}

				double tempG = Double.NaN;
				if (heuristic == "Time") {
					tempG = current.getG() + CalcTime(boatPower, current.getIceberg(), current, neighbor);
				}
				if (heuristic == "Water") {
				tempG = current.getG() + CalcIcebergSize(boatPower, current.getIceberg(), current, neighbor);
				}
				
				double tempF = tempG + current.getH();
				if ((closedPoints.contains(neighbor)) && (tempF >= neighbor.getF())){
					continue;
				}
				else if ((!queue.contains(neighbor)) || (tempF < neighbor.getF())){
					path.put(neighbor, current);
					neighbor.setG(tempG);
					neighbor.setF(tempG + neighbor.getH());
					Iceberg cloneIceberg = current.getIceberg().cloneIceberg();
					cloneIceberg.CalcShrink(CalcIcebergSize(boatPower, current.getIceberg(), current, neighbor));
					neighbor.setIceberg(cloneIceberg);
					neighbor.setTime(current.getTime() + CalcTime(boatPower, current.getIceberg(), current, neighbor));
				}
				if (queue.contains(neighbor)) {
					queue.remove(neighbor);
				}
				queue.add(neighbor);
			}
		}
		List<LocationDataPoint> pathList = assemblePath(endLocation,path);
		
		Double startSize = startIceberg.getMass();
		Double endSize = endLocation.getIceberg().getMass();
		Double totalTime = endLocation.getTime()/86400;
		
		String color = "";
		if (heuristic == "Water") {
			color = "(White)";
		}
		else if (heuristic == "Time") {
			color = "(Yellow)";
		}		
		System.out.println(heuristic + ": " + color);
		System.out.printf("Start Size of Iceberg: %.2e kg %n", startSize);
		System.out.printf("Final Size of Iceberg: %.2e kg %n", endSize);
		System.out.printf("Total Time: %.1f Days %n", totalTime);
		
		StringBuilder htmlList = new StringBuilder();
		for (int i = 0 ; i < pathList.size(); i++) {
			htmlList.append("\t\t\t{lat: ");
			htmlList.append(pathList.get(i).getLat());
			htmlList.append(", lng: ");
			double lon = pathList.get(i).getLon();
			if(lon>180) {
				lon = lon - 360;
			}
			htmlList.append(lon);
			htmlList.append("},");
			htmlList.append(System.lineSeparator());
		}
		return htmlList.toString();
	}

	private List<LocationDataPoint> assemblePath(LocationDataPoint endLocation, Map<LocationDataPoint, LocationDataPoint> path) {
		List<LocationDataPoint> pathList = new ArrayList<LocationDataPoint>();
		pathList.add(endLocation);
		while (path.containsKey(endLocation)) {
			endLocation = path.get(endLocation);
			pathList.add(endLocation);
		}
		
		Collections.reverse(pathList);
		
		return pathList;
	}
	
	public static Result implementSearch(HashMap<String,LocationDataPoint> JAVAdataset,
									   Double startLat, Double startLon, Double endLat, Double endLon,
									   Integer numberBoats, Integer icebergClass) {
									   
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
		
		boolean realPoint = false;
		/*Checks to see if Start/End Location are known data points.
		 *If not a known data point:
		 *Find closest point in known data points to user-defined point.
		 *If no path available from Start to End:
		 *Find next closest data point and repeat.
		 */
	
		while (!realPoint) {
			startDistance = Double.MAX_VALUE;
			endDistance = Double.MAX_VALUE;
			try {
				if (keySet.contains(startLat + ", " + startLon)) {
					start = startLat + ", " + startLon;
					realStartLat = startLat;
					realStartLon = startLon;	
				}
				else {
					for (String key : keySet) {
						if (checked.contains(key)) {
							continue;
						}
						Double lat2 = JAVAdataset.get(key).getLat();
						Double lon2 = JAVAdataset.get(key).getLon();
						Double tempDistance = Model.CalcDistance(startLat, startLon, lat2, lon2);
						if (tempDistance < startDistance) {
							start = key;
							realStartLat = lat2;
							realStartLon = lon2;
							startDistance = tempDistance;
						}
					}
				}	
		
				if (keySet.contains(endLat + ", " + endLon)) {
					end = endLat + ", " + endLon;
					realEndLat = endLat;
					realEndLon = endLon;	
				}
				else {
					for (String key : keySet) {
						if (checked.contains(key)) {
							continue;
						}
						Double lat2 = JAVAdataset.get(key).getLat();
						Double lon2 = JAVAdataset.get(key).getLon();
						Double tempDistance = Model.CalcDistance(endLat, endLon, lat2, lon2);
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
				realPoint = true;
			}
			/*Catches Exception.
			 *If Caught, this mean points do not connect (lake, sea, etc.):
			 *Add to list of checked points and repeat.
			 */
			catch (Exception e) {
				checked.add(end);
				continue;
			}
		}
		Result result = new Result();
		result.setWaterString(Water);
		result.setTimeString(Time);
		result.setRealStartLat(realStartLat);
		result.setRealStartLon(realStartLon);
		result.setRealEndLat(realEndLat);
		result.setRealEndLon(realEndLon);
		return result;
	}
}