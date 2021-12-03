package collections;

import java.util.*;

public class ArrayGraphMaths {

    private static final Random random = new Random();

    public static int[] generateRandomClosedRoute(int numberOfDigits) {
        return closePath(generateRandomOpenRoute(numberOfDigits));
    }

    public static int[] generateRandomOpenRoute(int numberOfDigits) {
        int[] combination = new int[numberOfDigits];
        int[] temp = generateSequentialArray(numberOfDigits);
        System.arraycopy(temp, 0, combination, 0, temp.length);

        for (int i = 0; i < numberOfDigits; i++) {
            swap(combination, i, random.nextInt(numberOfDigits - 1));
        }

        return combination;
    }

    public static List<int[]> getAllValidRoutes(int pathLength) {
        int[] temp = generateSequentialArray(pathLength);
        List<int[]> combinations = new ArrayList<>();

        heapsAlgorithm(temp.length, temp, combinations);

        return combinations;
    }

    private static int[] generateSequentialArray(int numberOfDigits) {
        int[] temp = new int[numberOfDigits];

        for (int i = 0; i < numberOfDigits; i++) {
            temp[i] = i;
        }

        return temp;
    }

    public static void swap(int[] array, final int indexA, final int indexB) {
        int a = array[indexA];
        array[indexA] = array[indexB];
        array[indexB] = a;
    }

    private static void heapsAlgorithm(int depth, int[] array, List<int[]> out) {
        //if at last item then all permutations calculated
        if (depth == 1) {
            out.add(closePath(array));
            return;
        }

        heapsAlgorithm(depth - 1, array, out);

        for (int i = 0; i < depth - 1; i++) {
            if (depth % 2 == 0) {
                swap(array, i, depth - 1);
            } else {
                swap(array, 0, depth - 1);
            }

            heapsAlgorithm(depth - 1, array, out);
        }
    }

    public static int[] closePath(int[] array) {
        int[] finalArray = Arrays.copyOf(array, array.length + 1);
        finalArray[array.length] = array[0];
        return finalArray;
    }

    public static List<int[]> twoOptNeighbourhoodGeneration(int[] path) {
        List<int[]> list = new LinkedList<>();
        path = openPath(path);

        for (int i = 0; i < path.length; i++) {
            int r = i + 1;
            int mod = r % path.length;
            while (mod != i) {
                int[] workingArray = path.clone();
                swap(workingArray, i, mod);
                list.add(closePath(workingArray));
                r++;
                mod = r % path.length;
            }
        }
        return list;
    }

    public static int[] openPath(int[] path) {
        return Arrays.copyOf(path, path.length - 1);
    }
}
