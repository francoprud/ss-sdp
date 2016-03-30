package model;

import util.MatrixUtils;

public class Vector2 {

	private double x;
	private double y;

	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Vector2 rotate(double angle) {
		return MatrixUtils.rotate(this, angle);
	}

	public Vector2 sum(Vector2 vector) {
		return new Vector2(x + vector.x, y + vector.y);
	}
	
	public Vector2 newPosition(double distance, double angle) {
		Vector2 aux = new Vector2(distance, 0);
		aux = aux.rotate(angle);
		return sum(aux);
	}
}
