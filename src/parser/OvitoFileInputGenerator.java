package parser;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import model.Particle;
import model.SimulationData;

/*
 * Generates an input file for Ovito (Visualization Tool)
 */
public class OvitoFileInputGenerator {
	private static final String ENCODING = "UTF-8";
	private static final String BLUE = "0 0 255";

	private PrintWriter writer;
	private String filePath;

	public OvitoFileInputGenerator(String filePath) {
		this.filePath = filePath;
	}

	public void generateFile() throws FileNotFoundException,
			UnsupportedEncodingException {
		writer = new PrintWriter(filePath, ENCODING);
	}

	public void printSimulationInstance(SimulationData simulationData) {
		printHeaders(simulationData.getParticlesAmount());
		for (Particle particle : simulationData.getParticles()) {
			writer.println(generateLine(particle));
		}
		printBoundariesParticles(simulationData.getSpaceDimension(),
				simulationData.getParticlesAmount());
	}

	public void endSimulation() {
		writer.close();
	}

	private void printBoundariesParticles(int spaceDimension, int particleAmount) {
		printBoundaryParticle(particleAmount + 1, 0, 0);
		printBoundaryParticle(particleAmount + 2, 0, spaceDimension);
		printBoundaryParticle(particleAmount + 3, spaceDimension, 0);
		printBoundaryParticle(particleAmount + 4, spaceDimension,
				spaceDimension);
	}

	private void printBoundaryParticle(int id, int x, int y) {
		writer.println(id + " " + x + " " + y + " 0 0 " + BLUE + " 0 " + BLUE);
	}

	private void printHeaders(int particlesAmount) {
		writer.println(particlesAmount + 4);
		writer.println("ID X Y dx dy pR pG pB r vR vG vB");
	}

	private String generateLine(Particle particle) {
		StringBuilder line = new StringBuilder();
		String particleColor = generateParticleColor(particle);
		line.append(particle.getId()).append(" ").append(particle.getX())
				.append(" ").append(particle.getY()).append(" ")
				.append(Math.cos(particle.getAngle()) * particle.getVelocity())
				.append(" ")
				.append(Math.sin(particle.getAngle()) * particle.getVelocity())
				.append(" ").append(particleColor)
				.append(" ").append(particle.getRadius()).append(" ")
				.append(particleColor);
		return line.toString();
	}

	private String generateParticleColor(Particle particle) {
		double red = (Math.sin(particle.getAngle()) / 2) + 0.5;
		double green = (Math.cos(particle.getAngle()) / 2) + 0.5;
		return red + " " + green + " 0.2";
	}
}
