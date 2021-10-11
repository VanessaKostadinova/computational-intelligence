import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Scanner;

public class Main {

    static ClassLoader classloader = Thread.currentThread().getContextClassLoader();

    static Double[][] test = new Double[][]{
            {5D,1D,2D},
            {2D,4D,1D},
            {1D,4D,2D}
    };

    public static void main(String[] args) {

        try(Scanner sc = new Scanner(loadFile("ulysses16(1).csv"))) {
            ArrayGraph graph = new ArrayGraph(sc);
            Double lowestTotal = null;

            for(int[] i : graph.getAllValidRoutes()){
                Double cost = graph.walk(i);
                if (lowestTotal == null || cost < lowestTotal){
                    lowestTotal = cost;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static File loadFile(String name) throws FileNotFoundException {
        URL path = classloader.getResource(name);
        if(path != null){
            return new File(path.getPath());
        } else {
            throw new FileNotFoundException(name);
        }
    }
}
