package input_generator;

public class PointGenerator {
	public static MyPoint randomPointBetween(int x, int y) {
		return new MyPoint(randomBetween(x, y), randomBetween(x, y), randomAngle());
	}

	public static double randomBetween(double x, double y) {
		return Math.random() * (y - x) + x;
	}

	public static double randomAngle() {
		return randomBetween(0, 2 * Math.PI);
	}

}
