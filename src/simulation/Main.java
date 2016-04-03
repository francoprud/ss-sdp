package simulation;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import model.Particle;
import model.SimulationData;
import parser.InformationParser;
import parser.OvitoFileInputGenerator;

public class Main {
	private static final int M = 13;
	private static final int T = 1;
	private static final String OVITO_FILE_PATH = "doc/examples/results/results.txt";

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		String dynamicFilePath = "doc/examples/Dynamic500.txt";
		String staticFilePath = "doc/examples/Static500.txt";
		OvitoFileInputGenerator ovito = new OvitoFileInputGenerator(OVITO_FILE_PATH);
		
		SimulationData simulationData = InformationParser
				.generateCellIndexObject(dynamicFilePath, staticFilePath)
				.build();

		if (!isAValidMValue(simulationData)) {
			System.out.println("Not a valid M value.");
			return;
		}
		
		ovito.generateFile(simulationData.getParticlesAmount());
		
		new TP2Simulation(M, T, new TP2Simulation.Listener() {
			@Override
			public void onSnapshotAvailable(int instance, SimulationData simulationData) {
					ovito.printSimulationInstance(simulationData, instance);
			}
		}).simulate(simulationData);
		
		ovito.endSimulation();
	}

	private static Boolean isAValidMValue(SimulationData simulationData) {
		return simulationData.getSpaceDimension() / M > simulationData
				.getInteractionRadius() + 2 * getMaximumRadius(simulationData);
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
}
