package main;
import java.util.*;
public class Search extends Model{

	public void aStar(LocationDataPoint startLocation, LocationDataPoint endLocation,
				      HashMap<String,LocationDataPoint> JAVAdataset, String heuristic,
				      Double boatPower, Double icebergSize) {
		
		Set<LocationDataPoint> openPoints = new HashSet<LocationDataPoint>();
		openPoints.add(startLocation);
		startLocation.setG(0);
		Set<LocationDataPoint> closedPoints = new HashSet<LocationDataPoint>();
		
		JAVAdataset.forEach((key,datapoint)->{
			//datapoint.setH(CalcTime(boatPower, icebergSize, datapoint, endLocation));
			//datapoint.setH(CalcDistance(datapoint.getLat(),datapoint.getLon(),endLocation.getLat(),endLocation.getLon()));
			datapoint.setH(0);
		});
		
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
	
		PriorityQueue<LocationDataPoint> queue = new PriorityQueue<LocationDataPoint>(100,comparator);
		startLocation.setG(0);
		queue.add(startLocation);
		boolean found = false;
		while ((!queue.isEmpty()&&!found)){
			LocationDataPoint current = queue.poll();
			closedPoints.add(current);
			
			
			for (LocationDataPoint neighbor : current.getNeighbors()) {
				if (closedPoints.contains(neighbor)) {
					continue;
				}
				double tempG = current.getG() + CalcTime(boatPower, icebergSize, current, neighbor);
				double tempF = tempG + current.getH();
				if ((closedPoints.contains(neighbor)) && (tempF >= neighbor.getF())){
					continue;
				}
				else if ((!queue.contains(neighbor)) || (tempF < neighbor.getF())){
					path.put(neighbor, current);
					neighbor.setG(tempG);
					neighbor.setF(tempG + neighbor.getH());
				}
				if (queue.contains(neighbor)) {
					queue.remove(neighbor);
				}
				queue.add(neighbor);
			}
		}
		List<LocationDataPoint> pathList = assemblePath(endLocation,path);
		/*
		System.out.println("Lat: ");
		closedPoints.forEach((point)->{
			System.out.println(point.getLat());
		});
		System.out.println("Lon: ");
		closedPoints.forEach((point)->{
			double lon = point.getLon();
			if (point.getLon()>180) {
				lon = lon - 360;
			}
			System.out.println(lon);
		});
		*/
		System.out.println("Size of ClosedPoints: " + closedPoints.size());
		System.out.println("Size of PathList: " + pathList.size());
		System.out.println(endLocation.getG());
	}

	private List<LocationDataPoint> assemblePath(LocationDataPoint endLocation, Map<LocationDataPoint, LocationDataPoint> path) {
		List<LocationDataPoint> pathList = new ArrayList<LocationDataPoint>();
		pathList.add(endLocation);
		while (path.containsKey(endLocation)) {
			endLocation = path.get(endLocation);
			pathList.add(endLocation);
		}
		
		Collections.reverse(pathList);
		
		System.out.println("Lat: ");
		pathList.forEach((point)->{	
			double lat = point.getLat();
			System.out.println(lat);
		});
		
		System.out.println("Lon: ");
		pathList.forEach((point)->{	
			double lon = point.getLon();
			if(lon>180) {
				lon = lon-360;
			}
			System.out.println(lon);
		});
		
		return pathList;
	}
}
