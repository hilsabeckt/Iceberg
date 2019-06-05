package main;

public class Model {
/*
 * Formulas based on "Validation and Quantitative Assessment of the Deterioration Mechanisms of ArcticIcebergs
 * El-Tahan, M.S., Venkatesh, S., El-Tahan, H., 1987. 
 */
	protected double CalcTime(double boatForce, Iceberg iceberg, LocationDataPoint from, LocationDataPoint to) {
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
		double icebergLength = iceberg.getLength();
		double icebergWidth = iceberg.getWidth();
		double icebergHeight = iceberg.getHeight();
		if (icebergLength <= 0) {
			return Double.MAX_VALUE;
		}
		
		double boatForcePerpendicular = .5*1029*.9*icebergLength*icebergHeight*Math.pow(curPerpendicular, 2);
		double boatForceParallel = boatForce - boatForcePerpendicular;
		if (boatForceParallel <= 0) {
			return Double.MAX_VALUE;
		}
		
		double relativeVelocity = Math.sqrt(boatForceParallel/(.5*1029*.9*icebergWidth*icebergHeight));
		double icebergVelocity = Math.abs(relativeVelocity - curParallel);
		double time = distance / icebergVelocity;
		return time;
	}
	
	protected double CalcIcebergSize(double boatForce, Iceberg iceberg, LocationDataPoint from, LocationDataPoint to) {
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
		double icebergLength = iceberg.getLength();
		double icebergWidth = iceberg.getWidth();
		double icebergHeight = iceberg.getHeight();
		if (icebergLength <= 0) {
			return Double.MAX_VALUE;
		}
		
		double boatForcePerpendicular = .5*1029*.9*icebergLength*icebergHeight*Math.pow(curPerpendicular, 2);
		double boatForceParallel = boatForce - boatForcePerpendicular;
		if (boatForceParallel <= 0) {
			return Double.MAX_VALUE;
		}
		
		double relativeVelocity = Math.sqrt(boatForceParallel/(.5*1029*.9*icebergWidth*icebergHeight));
		double icebergVelocity = Math.abs(relativeVelocity - curParallel);
		double time = distance / icebergVelocity;
		double sst = (to.getSst() + from.getSst())/2;
		
		//Solar Radiation
		double insolation = Double.NaN; // Solar Energy per Area W/m^2
		double averageLat = (lat1 + lat2)/2;
		if (averageLat >= 85) {
			insolation = 3160;
		}
		else if (averageLat >= 75 && averageLat < 85) {
			insolation = 4013;
		}
		else if (averageLat >= 65 && averageLat < 75) {
			insolation = 4745;
		}
		else if (averageLat >= 55 && averageLat < 65) {
			insolation = 5613;
		}
		else if (averageLat >= 45 && averageLat < 55) {
			insolation = 6743;
		}
		else if (averageLat >= 35 && averageLat < 45) {
			insolation = 7828;
		}
		else if (averageLat >= 25 && averageLat < 35) {
			insolation = 8698;
		}
		else if (averageLat >= 15 && averageLat < 25) {
			insolation = 9403;
		}
		else if (averageLat >= 5 && averageLat < 15) {
			insolation = 9825;
		}
		else if (averageLat >= -5 && averageLat < 5) {
			insolation = 9950;
		}
		else if (averageLat >= -15 && averageLat < -5) {
			insolation = 9865;
		}
		else if (averageLat >= -25 && averageLat < -15) {
			insolation = 9480;
		}
		else if (averageLat >= -35 && averageLat < -25) {
			insolation = 8808;
		}
		else if (averageLat >= -45 && averageLat < -35) {
			insolation = 7968;
		}
		else if (averageLat >= -55 && averageLat < -45) {
			insolation = 6910;
		}
		else if (averageLat >= -65 && averageLat < -55) {
			insolation = 5800;
		}
		else if (averageLat >= -75 && averageLat < -65) {
			insolation = 4950;
		}
		else if (averageLat >= -85 && averageLat < -75) {
			insolation = 4228;
		}
		else if (averageLat < -85) {
			insolation = 3378;
		}
		insolation = insolation*.3;
		double kgLossSolar = insolation*icebergLength*icebergWidth/334000;
		double volumeLossSolar = kgLossSolar/916.8*time;
		
		//Buoyant Vertical Convection
		double Vmb = (2.78*sst + .47*Math.pow(sst, 2))/3.154e7;
		double volumeLossBuoyant = Vmb*time;
		
		//Forced Convection
		double Re = 916.8*Math.abs(curParallel)*icebergLength/3.09;
		double Pr = 7.56;
		double Nu = .055*Math.pow(Re, .8)*Math.pow(Pr, .4);
		double qw = Nu*.6*sst/icebergLength;
		double Vmf = qw/(916.8);
		double volumeLossForced = Vmf*time;
		
		//Wave Erosion
		double Vmw = .000146*Math.pow((.01/5),.2)*5/10;
		double volumeLossErosion = Vmw*time*sst;
		
		double volumeLoss = volumeLossSolar + volumeLossBuoyant + volumeLossForced + volumeLossErosion;
		return volumeLoss;
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
