package application;

import antenna.AntennaArray;
import collections.ArrayGraph;
import evolution.Population;
import exceptions.RuntimeFileNotFoundException;
import swarm.SwarmNetwork;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static collections.ArrayGraphMaths.*;

public class Main {
    private static final ClassLoader classloader = Thread.currentThread().getContextClassLoader();

    public static void main(String[] args) {

        try (Scanner sc = new Scanner(loadFile("13_40.csv"))) {
            ArrayGraph arrayGraph = new ArrayGraph(sc);

            System.out.println("Given path:");
            System.out.println(arrayGraph.walkClosedPath(new int[]{2,3,0,1,4,5,6,7,8,9,2}));

            System.out.println();
            System.out.println("Evo Algorithm");
            evolutionaryAlgorithm(arrayGraph);

            System.out.println();
            System.out.println("Local Search");
            localSearchWithTimeLimit(arrayGraph, 10);

            System.out.println();
            System.out.println("Random Search");
            randomSearchWithTimeLimit(arrayGraph, 10);

        } catch (FileNotFoundException e) {
            throw new RuntimeFileNotFoundException(e);
        }

        System.out.println();
        System.out.println("Swarm");
        swarmOnAntennaArray(new AntennaArray(5, 55));
    }

    private static int[] evolutionaryAlgorithmWithTimeLimit(ArrayGraph arrayGraph) {
        Population population = new Population(arrayGraph, 50);
        LocalDateTime endTime = LocalDateTime.now().plusSeconds(30);

        while (endTime.isAfter(LocalDateTime.now())) {
            population.update();
        }

        return population.getBestPath();
    }

    private static int[] evolutionaryAlgorithm(ArrayGraph arrayGraph) {
        Population population = new Population(arrayGraph, 50);
        int i = 0;

        while (i < 50) {
            i++;
            population.update();
        }

        return population.getBestPath();
    }

    private static int[] localSearchWithTimeLimit(ArrayGraph arrayGraph, int secondsToRun) {
        int[] bestPath = null;
        Double bestCost = null;

        LocalDateTime timeToStop = LocalDateTime.now().plusSeconds(secondsToRun);

        int[] localOriginPath = generateRandomClosedRoute(arrayGraph.getPathLength());
        double localOriginCost = arrayGraph.walkClosedPath(localOriginPath);

        while (LocalDateTime.now().isBefore(timeToStop)) {
            List<int[]> localNeighbours = twoOptNeighbourhoodGeneration(localOriginPath);

            int[] localBest = bestPath(localNeighbours, arrayGraph);

            //set best
            if (bestCost == null || arrayGraph.walkClosedPath(localBest) < bestCost) {
                bestPath = localBest;
                bestCost = arrayGraph.walkClosedPath(localBest);

                System.out.println(localOriginCost);
                System.out.println(Arrays.toString(localOriginPath));
                System.out.println("-----");
            }

            //if local best is better, then go with that, else generate new
            if (arrayGraph.walkClosedPath(localBest) < localOriginCost) {
                localOriginPath = localBest;
                localOriginCost = arrayGraph.walkClosedPath(localBest);
            } else {
                localOriginPath = generateRandomClosedRoute(arrayGraph.getPathLength());
                localOriginCost = arrayGraph.walkClosedPath(localOriginPath);
            }
        }
        return bestPath;
    }

    private int[] compareAll(ArrayGraph graph) {
        return bestPath(getAllValidRoutes(graph.getPathLength()), graph);
    }

    private static int[] bestPath(List<int[]> paths, ArrayGraph graph) {
        Double lowestTotal = null;
        int[] chosenCombination = null;

        for (int[] i : paths) {
            Double cost = graph.walkClosedPath(i);
            if (lowestTotal == null || cost < lowestTotal) {
                lowestTotal = cost;
                chosenCombination = i;
            }
        }

        return chosenCombination;
    }

    private int[] compareRandomWithRouteLimit(ArrayGraph graph, int routeLimit) {
        Double lowestTotal = null;

        int[] chosenCombination = null;

        for (int i = 0; i < routeLimit; i++) {
            int[] combination = generateRandomClosedRoute(graph.getPathLength());
            double cost = graph.walkClosedPath(combination);
            if (lowestTotal == null || cost < lowestTotal) {
                lowestTotal = cost;
                chosenCombination = combination;
            }
        }
        return chosenCombination;
    }

    public static int[] randomSearchWithTimeLimit(ArrayGraph graph, int secondsToRun) {
        Double lowestTotal = null;

        int[] chosenCombination = null;
        LocalDateTime timeToStop = LocalDateTime.now().plusSeconds(secondsToRun);

        while (LocalDateTime.now().isBefore(timeToStop)) {
            int[] combination = generateRandomClosedRoute(graph.getPathLength());
            double cost = graph.walkClosedPath(combination);
            if (lowestTotal == null || cost < lowestTotal) {
                lowestTotal = cost;
                chosenCombination = combination;

                System.out.println(cost);
                System.out.println(Arrays.toString(combination));
                System.out.println("-----");
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

    private static void swarmOnAntennaArray(AntennaArray antennaArray) {
        SwarmNetwork swarmNetwork = new SwarmNetwork(antennaArray);
        LocalDateTime l = LocalDateTime.now().plusSeconds(30);
        while (l.isAfter(LocalDateTime.now())) {
            swarmNetwork.update();
        }
    }
}
