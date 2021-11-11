package swarm;

import antenna.AntennaArray;

import java.util.Arrays;
import java.util.Random;

import static antenna.AntennaMaths.generatePosition;
import static swarm.VectorMaths.*;

public class SwarmNetwork {
    private final SwarmParticle[] particles;
    private final AntennaArray antennaArray;
    private static final double coefficientFi = 1.1193D;
    private static final double coefficientEta = 0.721D;
    private final Random random;
    private double[] globalBestPosition;
    private Double globalBestValue;

    public SwarmNetwork(AntennaArray antennaArray) {
        this.antennaArray = antennaArray;
        this.globalBestPosition = null;
        this.globalBestValue = null;
        this.particles = new SwarmParticle[20 + (int) Math.sqrt(antennaArray.bounds().length)];
        this.random = new Random();

        //generate all random particles
        for (int i = 0; i < particles.length; i++) {
            double[] generatedPosition;

            do  {
                generatedPosition = generatePosition(antennaArray.bounds());
            } while (!antennaArray.is_valid(generatedPosition));

            double generatedPositionValue = Math.abs(antennaArray.evaluate(generatedPosition));

            if (globalBestValue == null || generatedPositionValue < globalBestValue) {
                globalBestPosition = generatedPosition;
                globalBestValue = generatedPositionValue;
            }

            particles[i] = new SwarmParticle(generatedPosition, new Vector(Arrays.copyOf(generatedPosition, generatedPosition.length - 1)));
        }
    }

    public void update() {
        for (SwarmParticle s : particles) {
            Vector v = generateNewVelocityVector(
                    s.getVelocity(),
                    clipAperture(s.getBestPosition()),
                    clipAperture(globalBestPosition),
                    clipAperture(s.getCurrentPosition()),
                    coefficientFi,
                    coefficientFi,
                    coefficientEta,
                    random.nextDouble(0, 1),
                    random.nextDouble(0, 1));

            clamp(v, 0.1D);
            double[] newPos = addVectorToCoordinate(v, s.getCurrentPosition());

            if (antennaArray.is_valid(newPos)) {
                s.update(v, newPos);

                double newValue = Math.abs(antennaArray.evaluate(newPos));

                if (newValue < antennaArray.evaluate(s.getBestPosition()))
                    s.setBestPosition(newPos);
                if (newValue < globalBestValue) {
                    globalBestPosition = newPos;
                    globalBestValue = newValue;
                }
            }
        }
    }

    public double[] getGlobalBestPosition() {
        return globalBestPosition;
    }

    private double[] clipAperture(double[] coordinates){
        return Arrays.copyOf(coordinates, coordinates.length - 1);
    }
}
