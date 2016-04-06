package input_generator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class InputGenerator {
	private static final String DEFAULT_PARTICLE_RADIO = "0.3700";

	public static void generateInput(int N, int spaceDimensions,
			double noiceAmplitude, double interactionRadius,
			double particleVelocity) throws FileNotFoundException,
			UnsupportedEncodingException {

		PrintWriter dynamicWriter = new PrintWriter("doc/examples/Dynamic" + N
				+ ".txt", "UTF-8");
		PrintWriter staticWriter = new PrintWriter("doc/examples/Static" + N
				+ ".txt", "UTF-8");

		staticWriter.println(N);
		staticWriter.println(spaceDimensions);
		staticWriter.println(noiceAmplitude);
		staticWriter.println(interactionRadius);
		staticWriter.println(particleVelocity);

		for (int i = 0; i < N; i++) {
			staticWriter.println(DEFAULT_PARTICLE_RADIO);
			dynamicWriter.println(PointGenerator.randomPointBetween(0,
					spaceDimensions));
		}

		staticWriter.close();
		dynamicWriter.close();
	}

	public static void main(String[] args) throws FileNotFoundException,
			UnsupportedEncodingException {
		generateInput(400, 10, 1.0, 1.0, 0.03);
	}
}