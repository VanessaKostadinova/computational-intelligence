import java.util.*;
import java.util.regex.Pattern;

public class ArrayGraph {
    private Double[][] collection;
    Pattern commaRegex = Pattern.compile(",");

    public ArrayGraph(Double[][] collection) {
        this.collection = collection;
    }

    public ArrayGraph(Scanner in) {
        in.reset();
        List<Float[]> entries = new LinkedList<>();

        while (in.hasNext()) {
            String str = in.nextLine();
            String[] values = commaRegex.split(str);
            if (values.length == 3 && Arrays.stream(values).noneMatch(e -> e.matches("[^0-9.]"))) {
                entries.add(new Float[]{Float.parseFloat(values[1]), Float.parseFloat(values[2])});
            }
        }

        int size = entries.size();
        collection = new Double[size][size];

        for (int i = 0; i < size; i++) {
            Float startX = entries.get(i)[0];
            Float startY = entries.get(i)[1];

            for (int r = 0; r < size; r++) {
                if (i != r) {
                    Float endX = entries.get(r)[0];
                    Float endY = entries.get(r)[1];

                    double distance = Math.sqrt((Math.pow((endX - startX), 2L) + Math.pow((endY - startY), 2L)));
                    collection[i][r] = distance;
                    collection[r][i] = distance;
                }
            }
        }
    }

    public Double walk(int[] path) {
        Double pathLength = 0D;
        int previous = path[0];

        for (int i = 1; i < path.length; i++) {
            pathLength += findLength(previous, path[i]);
            previous = path[i];
        }

        return pathLength;
    }

    public Double findLength(int start, int end) {
        return collection[start][end];
    }

    public List<int[]> getAllValidRoutes() {
        int[] temp = quickAndDirtyCombinationGeneration();
        List<int[]> combinations = new ArrayList<>();

        generate(temp.length, temp, combinations);

        return combinations;
    }

    private int[] quickAndDirtyCombinationGeneration() {
        int[] temp = new int[collection.length];

        for (int i = 0; i < collection.length; i++) {
            temp[i] = i;
        }

        return temp;
    }

    /**
     * This method swaps the elements at the two indexes given in an array;
     */
    private void swap(int[] array, final int indexA, final int indexB) {
        int a = array[indexA];
        array[indexA] = array[indexB];
        array[indexB] = a;
    }

    private void generate(int len, int[] array, List<int[]> out) {
        if (len == 1) {
            int [] finalArray = Arrays.copyOf(array, array.length + 1);
            finalArray[array.length] = array[0];
            out.add(finalArray);
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
}
