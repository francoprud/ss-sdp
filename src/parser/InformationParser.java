package parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Scanner;

import model.Particle;
import model.SimulationData;

public class InformationParser {
	public static SimulationData.Builder generateCellIndexObject(String dynamicFilePath, String staticFilePath) throws FileNotFoundException {
		SimulationData.Builder builder = SimulationData.Builder.create();
		
		InputStream dynamicIS = new FileInputStream(dynamicFilePath);
		Scanner dynamicScanner = new Scanner(dynamicIS).useLocale(Locale.US);
		InputStream staticIS = new FileInputStream(staticFilePath);
		Scanner staticScanner = new Scanner(staticIS).useLocale(Locale.US);
		
		int particlesAmount = staticScanner.nextInt();
		int spaceDimension = staticScanner.nextInt();
		double noiceAmplitude = staticScanner.nextDouble();
		double interactionRadius = staticScanner.nextDouble();
		double particleVelocity = staticScanner.nextDouble();
		
		builder = builder.withParticlesAmount(particlesAmount)
				.withSpaceDimension(spaceDimension)
				.withNoiceAmplitude(noiceAmplitude)
				.withInteractionRadius(interactionRadius);
		
		int instant = dynamicScanner.nextInt();
		
		for (int i = 1; i <= particlesAmount; i++) {
			double radius = staticScanner.nextDouble();
			double x = dynamicScanner.nextDouble();
			double y = dynamicScanner.nextDouble();
			double angle = dynamicScanner.nextDouble();
			Particle particle = new Particle(i, x, y, radius, particleVelocity, angle);
			builder = builder.withParticle(particle);
		}
		return builder;
	}
}