package application;

import exceptions.RuntimeFileNotFoundException;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    private static final PathService ps = new PathService();
    private static final Double[][] test = new Double[][]{
            {5D, 1D, 2D},
            {2D, 4D, 1D},
            {1D, 4D, 2D}
    };
    private static final int[] test2 = new int[]{1, 2, 3, 4};

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(loadFile("ulysses16(1).csv"))) {
            ArrayGraph ag = new ArrayGraph(sc);
            int[] localSearch = localSearchWithTimeLimit(ag, 10);
            int[] randomSearch = compareRandomWithTimeLimit(ag, 10);
            System.out.println(Arrays.toString(localSearch));
            System.out.println(ag.walk(localSearch));
            System.out.println(Arrays.toString(randomSearch));
            System.out.println(ag.walk(randomSearch));
            //ps.twoOptNeighbourhoodGeneration(test2).forEach(e -> System.out.println(Arrays.toString(e)));
        } catch (FileNotFoundException e) {
            throw new RuntimeFileNotFoundException(e);
        }
    }

    private static int[] localSearchWithTimeLimit(ArrayGraph graph, int secondsToRun) {
        int[] bestPath = null;
        Double bestCost = null;

        LocalDateTime timeToStop = LocalDateTime.now().plusSeconds(secondsToRun);

        int[] localOriginPath = ps.generateRandomClosedRoute(graph.getPathLength());
        Double localOriginCost = graph.walk(localOriginPath);

        while (LocalDateTime.now().isBefore(timeToStop)) {
            List<int[]> localNeighbours = ps.twoOptNeighbourhoodGeneration(localOriginPath);

            int[] localBest = bestPath(localNeighbours, graph);

            if(bestCost == null || graph.walk(localBest) < bestCost){
                bestPath = localBest;
                bestCost = graph.walk(localBest);
            }

            if(graph.walk(localBest) < localOriginCost){
                localOriginPath = localBest;
                localOriginCost = graph.walk(localBest);
            } else {
                localOriginPath = ps.generateRandomClosedRoute(graph.getPathLength());
                localOriginCost = graph.walk(localOriginPath);
            }
        }
        return bestPath;
    }

    private int[] compareAll(ArrayGraph graph) {
        return bestPath(ps.getAllValidRoutes(graph.getPathLength()), graph);
    }

    private static int[] bestPath(List<int[]> paths, ArrayGraph graph){
        Double lowestTotal = null;
        int[] chosenCombination = null;

        for (int[] i : paths) {
            Double cost = graph.walk(i);
            if (lowestTotal == null || cost < lowestTotal) {
                lowestTotal = cost;
                chosenCombination = i;
            }
        }

        return chosenCombination;
    }

    private int[] compareRandomWithLimit(ArrayGraph graph, int routeLimit) {
        Double lowestTotal = null;

        int[] chosenCombination = null;

        for (int i = 0; i < routeLimit; i++) {
            int[] combination = ps.generateRandomClosedRoute(graph.getPathLength());
            Double cost = graph.walk(combination);
            if (lowestTotal == null || cost < lowestTotal) {
                lowestTotal = cost;
                chosenCombination = combination;
            }
        }
        return chosenCombination;

    }

    public static int[] compareRandomWithTimeLimit(ArrayGraph graph, int secondsToRun) {
        Double lowestTotal = null;

        int[] chosenCombination = null;
        LocalDateTime timeToStop = LocalDateTime.now().plusSeconds(secondsToRun);

        while (LocalDateTime.now().isBefore(timeToStop)) {
            int[] combination = ps.generateRandomClosedRoute(graph.getPathLength());
            Double cost = graph.walk(combination);
            if (lowestTotal == null || cost < lowestTotal) {
                lowestTotal = cost;
                chosenCombination = combination;
            }
        }
        return chosenCombination;
    }

    private static File loadFile(String name) {
        URL path = classloader.getResource(name);
        if (path != null) {
            return new File(path.getPath());
        } else {
            throw new RuntimeFileNotFoundException(name);
        }
    }
}
