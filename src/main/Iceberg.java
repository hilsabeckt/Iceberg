package main;

public class Iceberg {
	double length = Double.NaN;
	double width = Double.NaN;
	double height = Double.NaN;
	double mass = Double.NaN;
			
	public void setSize(int sizeClass) {		
		if (sizeClass == 1) {
			length = 100;
			width = 67;
			height = 67 + 13.4;
			mass = .491e9;
		}
		if (sizeClass == 2) {
			length = 200;
			width = 133;
			height = 133 + 26.5;
			mass = 3.88e9;
		}
		if (sizeClass == 3) {
			length = 300;
			width = 200;
			height = 200 + 40.0;
			mass = 13.1e9;
		}
		if (sizeClass == 4) {
			length = 400;
			width = 267;
			height = 267 + 53.0;
			mass = 31.2e9;
		}
		if (sizeClass == 5) {
			length = 500;
			width = 333;
			height = 300 + 60.0;
			mass = 54.7e9;
		}
		if (sizeClass == 6) {
			length = 600;
			width = 400;
			height = 300 + 60.0;
			mass = 78.8e9;
		}
		if (sizeClass == 7) {
			length = 750;
			width = 500;
			height = 300 + 60.0;
			mass = 123e9;
		}
		if (sizeClass == 8) {
			length = 900;
			width = 600;
			height = 300 + 60.0;
			mass = 177e9;
		}
		if (sizeClass == 9) {
			length = 1200;
			width = 800;
			height = 300 + 60.0;
			mass = 315e9;
		}
		if (sizeClass == 10) {
			length = 1500;
			width = 1000;
			height = 300 + 60.0;
			mass = 492e9;
		}
	}
	public double getLength() {
		return length;
	}
	public double getWidth() {
		return width;
	}
	public double getHeight() {
		return height;
	}
	public double getMass() {
		return mass;
	}
	public void setLength(double newValue) {
		length = newValue;
	}
	public void setWidth(double newValue) {
		width = newValue;
	}
	public void setHeight(double newValue) {
		height = newValue;
	}
	public void setMass(double newValue) {
		mass = newValue;
	}
	
	public void CalcShrink(double volumeLoss) {
		double newVolume = length*width*height - volumeLoss;
		double percentLoss = newVolume/(length*width*height);
		length = length*Math.cbrt(percentLoss);
		width = width*Math.cbrt(percentLoss);
		height = height*Math.cbrt(percentLoss);
		mass = mass*percentLoss;
	}
	public Iceberg cloneIceberg(){
		Iceberg cloned = new Iceberg();
		cloned.setLength(length);
		cloned.setWidth(width);
		cloned.setHeight(height);
		cloned.setMass(mass);
		return cloned;
	}
}


