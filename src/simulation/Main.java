package simulation;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import model.Particle;
import model.SimulationData;
import parser.InformationParser;
import parser.OvitoFileInputGenerator;

public class Main {
	private static final int M = 25;
	private static final int T = 10000;
	private static final String OVITO_FILE_PATH = "doc/examples/results/result.txt";

	public static void main(String[] args) {
		String dynamicFilePath = "doc/examples/Dynamic500.txt";
		String staticFilePath = "doc/examples/Static500.txt";
		final OvitoFileInputGenerator ovito = new OvitoFileInputGenerator(OVITO_FILE_PATH);
		
		SimulationData simulationData = parseSimulationData(dynamicFilePath, staticFilePath);
		if (simulationData == null || !isAValidMValue(simulationData) || !generateOvitoFiles(ovito, simulationData)) {
			return;
		}

		new TP2Simulation(M, T, new TP2Simulation.Listener() {
			@Override
			public void onSnapshotAvailable(int instance,
					SimulationData simulationData) {
				ovito.printSimulationInstance(simulationData);
			}
		}).simulate(simulationData);
		
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
			ovito.generateFile(simulationData.getParticlesAmount());
			return true;
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			System.err.println("Can not generate ovito's files. Error: " + e.getMessage());
			return false;
		}
	}
	
}
