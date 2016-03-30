package util;
import model.Vector2;


public class MatrixUtils {
	public static Vector2 rotate(Vector2 vector, double angle) {
		double[][] rm = rotationMatrix(angle);
		return new Vector2(rm[0][0]*vector.getX() + rm[0][1]*vector.getY(), rm[1][0]*vector.getX() + rm[1][1]*vector.getY());
	}
	
	private static double[][] rotationMatrix(double a) {
		return new double[][]{{Math.cos(a), -Math.sin(a)}, {Math.sin(a), Math.cos(a)}};
	}
}
