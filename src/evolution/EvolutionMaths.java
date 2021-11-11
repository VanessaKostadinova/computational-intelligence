package evolution;

import java.util.Random;

import static collections.ArrayGraphMaths.swap;

public class EvolutionMaths {
    private static final Random random = new Random();

    public static void encode() {

    }

    public static void decode() {

    }

    public static void mutate(int[] path) {
        int startIndex = random.nextInt(0, path.length - 1);
        int endIndex = random.nextInt(0, path.length - 1);
        if (startIndex == endIndex) {
            endIndex = (endIndex + 1) % (path.length - 1);
        }

        swap(path, startIndex, endIndex);
    }

    public static int[] orderOneCrossover(int[] p1, int[] p2) {
        int[] path1 = p1.clone();
        int[] path2 = p2.clone();
        if (path1.length != path2.length) throw new IllegalArgumentException("Path lengths are not equal");
        int[] crossover = new int[path1.length];
        int startIndex = random.nextInt(0, path1.length - 1);
        int length = random.nextInt(1, path1.length);

        //copy over crossover
        for (int i = 0; i < length; i++) {
            int index = (i + startIndex) % (path1.length);
            crossover[index] = path1[index];

            //filter number out of path2 by setting to invalid
            for (int r = 0; r < path2.length; r++) {
                if (path2[r] == crossover[index]) {
                    path2[r] = -1;
                }
            }
        }
        //point at which crossover is being written to
        int endIndex = (startIndex + length - 1) % (crossover.length);

        //copy path2 into crossover
        for(int i = 0; i < path2.length; i++){
            int index = (i + startIndex + length) % (path2.length);
            if(path2[index] != -1){
                endIndex = (endIndex + 1) % (crossover.length);
                crossover[endIndex] = path2[index];
            }
        }
        return crossover;
    }
}
