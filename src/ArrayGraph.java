import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
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
            String str = in.next();
            String[] values = commaRegex.split(str);
            if (values.length == 3) {
                entries.add(new Float[]{Float.parseFloat(values[1]), Float.parseFloat(values[2])});
            }
        }
        int size = entries.size() - 1;
        collection = new Double[size][size];

        for (int i = 0; i <= size; i++) {
            Float startX = entries.get(i)[1];
            Float startY = entries.get(i)[2];

            for (int r = 0; r <= size; r++) {
                if (i != r) {
                    Float endX = entries.get(r)[1];
                    Float endY = entries.get(r)[2];

                    double distance = Math.sqrt((Math.pow((endX - startX), 2L) + Math.pow((endY - startY), 2L)));
                    collection[i][r] = distance;
                    collection[r][i] = distance;
                }
            }
        }
    }

    public Double walk(Integer[][] path) {
        Double pathLength = 0D;
        for (Integer[] coords : path) {
            int start = coords[0];
            int end = coords[1];

            pathLength += findLength(start, end);
        }

        return pathLength;
    }

    public Double findLength(int start, int end) {
        return collection[start][end];
    }

    public int[][] getAllValidRoutes() {
        ArrayList<int[]> tempList = new ArrayList<>();
        for (int start = 0; start < collection.length; start++) {
            
        }
    }
}
