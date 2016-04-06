package parser;

import input_generator.InputGenerator;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import model.Particle;
import model.SimulationData;
import simulation.TP2Simulation;

public class AverageNormalizedVelocityFileGenerator {
	private static final String ENCODING = "UTF-8";

	// private static final Point[] INPUTS = {new Point(40, 3), new Point(100,
	// 5)};
	// private static final Point[] INPUTS = {new Point(40, 3), new Point(100,
	// 5), new Point(400, 10)};
	private static final Point[] INPUTS = { new Point(400, 10) };
	private static final int T = 1500;
	private static final double INIT_NOISE = 0.0;
	private static final double FINAL_NOISE = 5.0;
	private static final double NOISE_STEP = 0.25;

	private PrintWriter writer;

	public void generateFile(String filePath) throws FileNotFoundException,
			UnsupportedEncodingException {
		writer = new PrintWriter(filePath, ENCODING);
		writer.println("n, va");
	}

	public void printSimulationNormalizedVelocity(SimulationData simulationData) {
		double constantParticleVelocity = simulationData.getParticles().get(0)
				.getVelocity();
		double xVelocityAvg = 0;
		double yVelocityAvg = 0;
		for (Particle particle : simulationData.getParticles()) {
			xVelocityAvg += particle.getXVelocity();
			yVelocityAvg += particle.getYVelocity();
		}
		double absVelocity = Math.sqrt(Math.pow(xVelocityAvg, 2)
				+ Math.pow(yVelocityAvg, 2));
		writer.println(simulationData.getNoiceAmplitude()
				+ ", "
				+ absVelocity
				/ (simulationData.getParticlesAmount() * constantParticleVelocity));
	}

	public void endSimulation() {
		writer.close();
	}

	public static void main(String[] args) throws FileNotFoundException,
			UnsupportedEncodingException {

		for (Point input : INPUTS) {
			int N = input.x;
			int L = input.y;
			System.out.println("generating for input N = " + N + "; L = " + L);
			InputGenerator.generateInput(N, L, 1.0, 1.0, 0.03);

			String dynamicFilePath = "doc/examples/Dynamic" + N + ".txt";
			String staticFilePath = "doc/examples/Static" + N + ".txt";
			runSimulations(dynamicFilePath, staticFilePath);
		}
	}

	private static void runSimulations(String dynamicFilePath,
			String staticFilePath) throws FileNotFoundException,
			UnsupportedEncodingException {
		String avgVelocityFilePath = "doc/examples/results/result";
		SimulationData simulationData = parseSimulationData(dynamicFilePath,
				staticFilePath);
		int M = getOptimalValidM(simulationData);
		System.out.println(getOptimalValidM(simulationData));
		if (simulationData == null || !isAValidMValue(simulationData, M)) {
			return;
		}

		AverageNormalizedVelocityFileGenerator averageVelocity = new AverageNormalizedVelocityFileGenerator();
		averageVelocity.generateFile(avgVelocityFilePath
				+ simulationData.getParticlesAmount() + "-" + T + ".csv");

		long startTime = System.currentTimeMillis();
		for (double n = INIT_NOISE; n <= FINAL_NOISE; n += NOISE_STEP) {
			simulationData = parseSimulationData(dynamicFilePath,
					staticFilePath);
			simulationData.setNoiceAmplitude(n);
			System.out.println("simulating with noise " + n);
			new TP2Simulation(M, T, new TP2Simulation.Listener() {
				@Override
				public void onSnapshotAvailable(int instance,
						SimulationData simulationData) {
				}
			}).simulate(simulationData);
			averageVelocity.printSimulationNormalizedVelocity(simulationData);
		}
		long endTime = System.currentTimeMillis();
		System.out.println("finished with all noises in "
				+ (endTime - startTime));
		averageVelocity.endSimulation();
	}

	private static boolean isAValidMValue(SimulationData simulationData, int M) {
		int L = simulationData.getSpaceDimension();
		double r = simulationData.getInteractionRadius();
		if (L / M > r + 2 * getMaximumRadius(simulationData)) {
			return true;
		} else {
			System.err.println("Not a valid M value.");
			return false;
		}
	}

	private static Double getMaximumRadius(SimulationData simulationData) {
		Double max = 0.0;
		for (Particle particle : simulationData.getParticles()) {
			if (max < particle.getRadius()) {
				max = particle.getRadius();
			}
		}
		return max;
	}

	private static SimulationData parseSimulationData(String dynamicFilePath,
			String staticFilePath) {
		try {
			return InformationParser.generateCellIndexObject(dynamicFilePath,
					staticFilePath).build();
		} catch (FileNotFoundException e) {
			System.err.println("Can not generate cell index object. Error: "
					+ e.getMessage());
			return null;
		}
	}

	private static int getOptimalValidM(SimulationData simulationData) {
		int L = simulationData.getSpaceDimension();
		double r = simulationData.getInteractionRadius();
		return (int) Math.floor(L
				/ (r + 2 * getMaximumRadius(simulationData) + 1));
	}
}
