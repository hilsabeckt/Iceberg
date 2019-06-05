package main;
import java.util.*;
public class Search extends Model{

	public void aStar(LocationDataPoint startLocation, LocationDataPoint endLocation,
				      HashMap<String,LocationDataPoint> JAVAdataset, String heuristic,
				      Double boatPower, Integer icebergSize){
		

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
			//datapoint.setH(CalcTime(boatPower, miniIceberg, datapoint, endLocation));
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

		System.out.println("Size of PathList: " + pathList.size());
		System.out.println("Start Size of Iceberg: " + startIceberg.getMass());
		System.out.println("Final Size of Iceberg: " + endLocation.getIceberg().getMass());
		System.out.println("Total time: " + endLocation.getTime()/86400. + " Days");
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
