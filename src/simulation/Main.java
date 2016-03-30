package simulation;

import java.io.FileNotFoundException;

import model.Particle;
import model.SimulationData;
import parser.InformationParser;

public class Main {
	private static final boolean HAS_PERIODIC_BOUNDARIES = true;
	private static final int M = 13;
	private static final int T = 1;

	public static void main(String[] args) throws FileNotFoundException {
		String dynamicFilePath = "doc/examples/Dynamic500.txt";
		String staticFilePath = "doc/examples/Static500.txt";

		SimulationData simulationData = InformationParser
				.generateCellIndexObject(dynamicFilePath, staticFilePath)
				.build();

		if (!isAValidMValue(simulationData)) {
			System.out.println("Not a valid M value.");
			return;
		}
		
		new TP2Simulation(M, T, new TP2Simulation.Listener() {
			
			@Override
			public void onSnapshotAvailable(int t, SimulationData simulationData) {
//				save snapshot to file
			}
		}).simulate(simulationData);
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
