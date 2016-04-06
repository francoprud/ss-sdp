package simulation;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import model.Particle;
import model.SimulationData;
import parser.InformationParser;
import parser.OvitoFileInputGenerator;

public class Main {
	private static final int M = 1;
	private static final int T = 1000;
	private static final String OVITO_FILE_PATH = "doc/examples/results/result.txt";

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		System.out.println(Math.atan2(0.7, 0.5));
		System.out.println(Math.atan(0.7 / 0.5));
		String dynamicFilePath = "doc/examples/Dynamic300.txt";
		String staticFilePath = "doc/examples/Static300.txt";
		final OvitoFileInputGenerator ovito = new OvitoFileInputGenerator(OVITO_FILE_PATH);
		
		SimulationData simulationData = parseSimulationData(dynamicFilePath, staticFilePath);
		if (simulationData == null || !isAValidMValue(simulationData) || !generateOvitoFiles(ovito, simulationData)) {
			return;
		}
		long startTime = System.currentTimeMillis();
		new TP2Simulation(getOptimalValidM(simulationData), T, new TP2Simulation.Listener() {
			@Override
			public void onSnapshotAvailable(int instance,
					SimulationData simulationData) {
				ovito.printSimulationInstance(simulationData);
			}
		}).simulate(simulationData);
		long endTime = System.currentTimeMillis();
		System.out.println("Simulation time: " + (endTime - startTime));
		ovito.endSimulation();
	}

	private static boolean isAValidMValue(SimulationData simulationData) {
		int L = simulationData.getSpaceDimension();
		double r = simulationData.getInteractionRadius();
		if ( L / M > r + 2 * getMaximumRadius(simulationData)) {
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
	
	private static SimulationData parseSimulationData(String dynamicFilePath, String staticFilePath) {
		try {
			return InformationParser.generateCellIndexObject(dynamicFilePath, staticFilePath).build();
		} catch (FileNotFoundException e) {
			System.err.println("Can not generate cell index object. Error: " + e.getMessage());
			return null;
		}
	}
	
	private static boolean generateOvitoFiles(OvitoFileInputGenerator ovito, SimulationData simulationData) {
		try {
			ovito.generateFile();
			return true;
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			System.err.println("Can not generate ovito's files. Error: " + e.getMessage());
			return false;
		}
	}
	
	private static int getOptimalValidM(SimulationData simulationData) {
		int L = simulationData.getSpaceDimension();
		double r = simulationData.getInteractionRadius();
		return (int) Math.floor(L / (r + 2 * getMaximumRadius(simulationData) + 1)); 
	}
}