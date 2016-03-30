package simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.RandomUtils;
import model.Particle;
import model.SimulationData;
import model.Vector2;

public class SelfDrivenParticleSimulation implements Simulation {

	private SimulationData simulationData;

	@Override
	public void simulate(SimulationData simulationData) {
		this.simulationData = simulationData;
		calculateNewAngles();
		calculateNewPositions();
	}

	private void calculateNewAngles() {
		simulationData.clearMarks();
		for (Particle particle : simulationData.getParticles()) {
			calculateNewAnglesRec(particle);
		}
	}

	private void calculateNewAnglesRec(Particle particle) {
		if (particle.isMarked())
			return;
		double newAngle = calculateNewAngle(particle);
		particle.mark();
		for (Particle neighbor : particle.getNeighbors()) {
			calculateNewAnglesRec(neighbor);
		}
		particle.getNeighbors().clear();
		particle.setAngle(newAngle);
	}

	private double calculateNewAngle(Particle particle) {
		List<Double> angles = new ArrayList<Double>();
		angles.add(particle.getAngle());
		for (Particle neighbor : particle.getNeighbors()) {
			angles.add(neighbor.getAngle());
		}
		return angleFunction(angles);
	}

	private void calculateNewPositions() {
		for (Particle particle : simulationData.getParticles()) {
			calculateNewPosition(particle);
		}
	}

	private void calculateNewPosition(Particle particle) {
		Vector2 position = new Vector2(particle.getX(), particle.getY());
		double distance = particle.getVelocity();
		double angle = particle.getAngle();
		Vector2 newPosition = position.newPosition(distance, angle);
		int spaceDimension = simulationData.getSpaceDimension();
		particle.setX((newPosition.getX() + spaceDimension) % spaceDimension);
		particle.setY((newPosition.getY() + spaceDimension) % spaceDimension);
	}

	private double angleFunction(List<Double> angles) {
		double sinAcum = 0;
		double cosAcum = 0;
		for (Double angle : angles) {
			sinAcum += Math.sin(angle);
			cosAcum += Math.cos(angle);
		}
		double sinAvg = sinAcum / angles.size();
		double cosAvg = cosAcum / angles.size();
		return Math.atan(sinAvg / cosAvg) + randomizeNoise();
	}

	private double randomizeNoise() {
		double n = simulationData.getNoiceAmplitude();
		return RandomUtils.randomBetween(-n / 2, n / 2);
	}
}
