package main;

public class Model {
	protected double CalcTime(double boatPower, double icebergSize, LocationDataPoint from, LocationDataPoint to) {
		//U = E-W
		//V = N-S
		double lat1 = from.getLat();
		double lat2 = to.getLat();
		double lon1 = from.getLon();
		double lon2 = to.getLon();
		double ucur = (from.getUcur() + to.getUcur())/2;
		double vcur = (from.getVcur() + to.getVcur())/2;
		double bearing = CalcBearing(lat1, lon1, lat2, lon2);
		double curPerpendicular = ucur*Math.cos(bearing) + vcur*Math.sin(bearing);
		double curParallel = ucur*Math.sin(bearing) + vcur*Math.cos(bearing);
		double distance = CalcDistance(lat1, lon1, lat2, lon2);
		double radius = icebergSize;
		double forceDragFactor = .5*997*.47*Math.PI*Math.pow(radius, 2);
		
		double boatPowerPerpendicular = forceDragFactor*Math.pow(curPerpendicular, 3);
		double boatPowerParallel = boatPower - boatPowerPerpendicular;
		if (boatPowerParallel <= 0) {
			System.out.println("Error: Boat does not have enough power");
		}
		double realVelocity = SolveThirdDegree(1,
											   -3*curParallel,
					                           3*Math.pow(curParallel, 2),
					                           -Math.pow(curParallel, 3) - (boatPowerParallel/forceDragFactor));
		
		double time = distance / realVelocity;
		return time;
			
	}
	protected double SolveThirdDegree(double a, double b, double c, double d) {
		double alpha = (-Math.pow(b,3))/(27*Math.pow(a,3)) + (b*c)/(6*a*a) - d/(2*a);
		double beta = c/(3*a) - (Math.pow(b,2))/(9*a*a);
		double gamma = b/(3*a);
		double x = Math.cbrt(alpha + Math.sqrt(Math.pow(alpha, 2) + Math.pow(beta, 3))) + Math.cbrt(alpha - Math.sqrt(Math.pow(alpha, 2) + Math.pow(beta, 3))) - gamma;
		return x;
	}


	protected double CalcBearing(double lat1, double lon1, double lat2, double lon2){
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);
		lon1 = Math.toRadians(lon1);
		lon2 = Math.toRadians(lon2);
		double y = Math.sin(lon2 - lon1)*Math.cos(lat2);
		double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)*Math.cos(lat2)*Math.cos(lon2 - lon1);
		return ((Math.atan2(y, x)) + 2*Math.PI) % (2*Math.PI);
	}
	
	protected double CalcDistance(double lat1, double lon1, double lat2, double lon2){
		double r = 6371e3;
		double rlat1 = Math.toRadians(lat1);
		double rlat2 = Math.toRadians(lat2);
		double rdeltalat = Math.toRadians(lat2 - lat1);
		double rdeltalon = Math.toRadians(lon2 - lon1);
		
		
		double alpha = Math.pow(Math.sin(rdeltalat/2), 2) + Math.cos(rlat1)*Math.cos(rlat2)*Math.pow(Math.sin(rdeltalon/2), 2);
		double beta = 2*Math.atan2(Math.sqrt(alpha), Math.sqrt(1-alpha));
		return r*beta;
	}

}
