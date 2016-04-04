package input_generator;

import java.util.Locale;

public class MyPoint {
	public double x;
	public double y;
	public double angle;
	
	public MyPoint(double x, double y, double angle) {
		this.x = x;
		this.y = y;
		this.angle = angle;
	}

	public String toString() {
		return String.format(Locale.US, "%1.7e %1.7e %1.7e", x, y, angle);
	}
}
