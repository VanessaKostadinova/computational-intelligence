package antenna;

import java.util.Arrays;
import java.util.Random;

public class AntennaMaths {
    private static final Random random = new Random();

    public static double[] generatePosition(double[][] bounds) {
        double minimumSpacing = AntennaArray.MIN_SPACING;
        double minimumLeftoverSpace = minimumSpacing * bounds.length;
        double maximumStartingSpace = 0;
        double[] antennaPlacements = new double[bounds.length];

        for (int i = 0; i < bounds.length - 1; i++) {
            antennaPlacements[i] = random.nextDouble(bounds[i][0] + maximumStartingSpace, bounds[i][1] - minimumLeftoverSpace);
            minimumLeftoverSpace -= minimumSpacing;
            maximumStartingSpace += minimumSpacing;
        }

        antennaPlacements[antennaPlacements.length - 1] = bounds.length / 2D;

        Arrays.sort(antennaPlacements);
        return antennaPlacements;
    }
}
