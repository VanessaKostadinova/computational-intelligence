package application;

import java.util.*;

public class PathService {

    Random random;

    public PathService() {
        random = new Random();
    }

    public int[] generateRandomClosedRoute(int numberOfDigits) {
        return closePath(generateRandomOpenRoute(numberOfDigits));
    }

    public int[] generateRandomOpenRoute(int numberOfDigits) {
        int[] combination = new int[numberOfDigits];
        int[] temp = quickAndDirtyCombinationGeneration(numberOfDigits);
        System.arraycopy(temp, 0, combination, 0, temp.length);

        for (int i = 0; i < numberOfDigits; i++) {
            swap(combination, i, random.nextInt(numberOfDigits - 1));
        }

        return combination;
    }

    public List<int[]> getAllValidRoutes(int pathLength) {
        int[] temp = quickAndDirtyCombinationGeneration(pathLength);
        List<int[]> combinations = new ArrayList<>();

        generate(temp.length, temp, combinations);

        return combinations;
    }

    private int[] quickAndDirtyCombinationGeneration(int numberOfDigits) {
        int[] temp = new int[numberOfDigits];

        for (int i = 0; i < numberOfDigits; i++) {
            temp[i] = i;
        }

        return temp;
    }

    /**
     * This method swaps the elements at the two indexes given in an array.
     */
    private void swap(int[] array, final int indexA, final int indexB) {
        int a = array[indexA];
        array[indexA] = array[indexB];
        array[indexB] = a;
    }

    private void generate(int len, int[] array, List<int[]> out) {
        if (len == 1) {
            out.add(closePath(array));
            return;
        }

        generate(len - 1, array, out);

        for (int i = 0; i < len - 1; i++) {
            if (len % 2 == 0) {
                swap(array, i, len - 1);
            } else {
                swap(array, 0, len - 1);
            }

            generate(len - 1, array, out);
        }
    }

    public int[] closePath(int[] array) {
        int[] finalArray = Arrays.copyOf(array, array.length + 1);
        finalArray[array.length] = array[0];
        return finalArray;
    }

    public List<int[]> twoOptNeighbourhoodGeneration(int[] path) {
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

    public int[] openPath(int[] path){
        return Arrays.copyOf(path, path.length - 1);
    }
}
