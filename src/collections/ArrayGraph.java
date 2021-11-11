package collections;

import java.util.*;
import java.util.regex.Pattern;

public class ArrayGraph {
    private final Double[][] collection;

    public ArrayGraph(Double[][] collection) {
        this.collection = collection;
    }

    public ArrayGraph(Scanner in) {
        in.reset();
        List<Float[]> entries = new LinkedList<>();
        Pattern commaRegex = Pattern.compile(",");
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

    public double walkClosedPath(int[] path) {
        double pathLength = 0D;
        int previous = path[0];

        for (int i = 1; i < path.length; i++) {
            pathLength += findLength(previous, path[i]);
            previous = path[i];
        }

        return pathLength;
    }

    public double walkOpenPath(int[] path) {
        double pathLength = walkClosedPath(path);
        pathLength += findLength(path[path.length - 1], path[0]);

        return pathLength;
    }

    private Double findLength(int start, int end) {
        return collection[start][end];
    }

    public int getPathLength() {
        return collection.length;
    }
}
