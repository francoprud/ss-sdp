package simulation;

import model.SimulationData;

public class TP2Simulation implements Simulation {

	private int M;
	private int T;
	private Listener listener;
	
	public interface Listener {
		public void onSnapshotAvailable(int t, SimulationData simulationData);
	}

	public TP2Simulation(int M, int T, TP2Simulation.Listener listener) {
		this.M = M;
		this.T = T;
		this.listener = listener;
	}

	@Override
	public void simulate(SimulationData simulationData) {
		listener.onSnapshotAvailable(0, simulationData);
		for (int i = 0; i < T; i++) {
			new CellIndexMethodSimulation(M, true).simulate(simulationData);
			new SelfDrivenParticleSimulation().simulate(simulationData);
			listener.onSnapshotAvailable(i + 1, simulationData);
		}
	}

}
