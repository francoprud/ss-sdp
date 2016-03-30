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
	private static final String RED = "255 0 0";
	private static final String GREEN = "0 255 0";
	private static final String BLUE = "0 0 255";

	private Particle selectedParticle;
	private SimulationData simulationData;

	public OvitoFileInputGenerator(SimulationData simulationData) {
		this.simulationData = simulationData;
	}

	public OvitoFileInputGenerator(SimulationData simulationData,
			int selectedParticleId) {
		this.simulationData = simulationData;
		this.selectedParticle = simulationData
				.getParticleById(selectedParticleId);
	}

	public void setSelectedParticle(int particleId) {
		this.selectedParticle = simulationData.getParticleById(particleId);
	}

	public void generateFile(String filePath) throws FileNotFoundException,
			UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(filePath, ENCODING);
		writer.println(simulationData.getParticlesAmount());
		writer.println("ID X Y R G B r"); // This second line is a comment for
											// Ovito
		for (Particle particle : simulationData.getParticles()) {
			if (particle.equals(selectedParticle)) {
				writer.println(generateLine(particle, RED));
			} else if (selectedParticle.getNeighbors().contains(particle)) {
				writer.println(generateLine(particle, GREEN));
			} else {
				writer.println(generateLine(particle, BLUE));
			}
		}
		writer.close();
	}

	private String generateLine(Particle particle, String RGB) {
		StringBuilder line = new StringBuilder();
		line.append(particle.getId()).append(" ").append(particle.getX())
				.append(" ").append(particle.getY()).append(" ").append(RGB)
				.append(" ").append(particle.getRadius());
		return line.toString();
	}

	public void generateResult(String filePath) throws FileNotFoundException,
			UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(filePath, "UTF-8");
		for (Particle particle : simulationData.getParticles()) {
			writer.print(particle.getId() + "  ");
			for (Particle p : particle.getNeighbors()) {
				writer.print(p.getId() + " ");
			}
			writer.println("");
		}
		writer.close();
	}

}
