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
import java.util.*;

import static collections.ArrayGraphMaths.*;

public class Main {
    private static final ClassLoader classloader = Thread.currentThread().getContextClassLoader();

    public static void main(String[] args) {
        //lab03();
        try (Scanner sc = new Scanner(loadFile("ulysses16(1).csv"))) {
            ArrayGraph ag = new ArrayGraph(sc);

            int[] evolutionary = evolutionaryAlgorithm(ag);

            System.out.println(Arrays.toString(evolutionary));
            System.out.println(ag.walkOpenPath(evolutionary));

            int[] localSearch = localSearchWithTimeLimit(ag, 10);
            int[] randomSearch = compareRandomWithTimeLimit(ag, 10);
            System.out.println(Arrays.toString(localSearch));
            System.out.println(ag.walkClosedPath(localSearch));
            System.out.println(Arrays.toString(randomSearch));
            System.out.println(ag.walkClosedPath(randomSearch));
        } catch (FileNotFoundException e) {
            throw new RuntimeFileNotFoundException(e);
        }
    }

    private static int[] evolutionaryAlgorithm(ArrayGraph arrayGraph){
        Population population = new Population(arrayGraph, 50);
        LocalDateTime endTime = LocalDateTime.now().plusSeconds(30);

        while (endTime.isAfter(LocalDateTime.now())) {
            population.update();
        }

        return population.getBestMember().getPath();
    }

    private static int[] localSearchWithTimeLimit(ArrayGraph arrayGraph, int secondsToRun) {
        int[] bestPath = null;
        Double bestCost = null;

        LocalDateTime timeToStop = LocalDateTime.now().plusSeconds(secondsToRun);

        int[] localOriginPath = generateRandomClosedRoute(arrayGraph.getPathLength());
        Double localOriginCost = arrayGraph.walkClosedPath(localOriginPath);

        while (LocalDateTime.now().isBefore(timeToStop)) {
            List<int[]> localNeighbours = twoOptNeighbourhoodGeneration(localOriginPath);

            int[] localBest = bestPath(localNeighbours, arrayGraph);

            if (bestCost == null || arrayGraph.walkClosedPath(localBest) < bestCost) {
                bestPath = localBest;
                bestCost = arrayGraph.walkClosedPath(localBest);
            }

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

    private int[] compareRandomWithLimit(ArrayGraph graph, int routeLimit) {
        Double lowestTotal = null;

        int[] chosenCombination = null;

        for (int i = 0; i < routeLimit; i++) {
            int[] combination = generateRandomClosedRoute(graph.getPathLength());
            Double cost = graph.walkClosedPath(combination);
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
            int[] combination = generateRandomClosedRoute(graph.getPathLength());
            Double cost = graph.walkClosedPath(combination);
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

    private static void lab03() {
        AntennaArray antennaArray = new AntennaArray(10, 90D);
        SwarmNetwork swarmNetwork = new SwarmNetwork(antennaArray);
        LocalDateTime l = LocalDateTime.now().plusSeconds(30);
        while (l.isAfter(LocalDateTime.now())) {
            swarmNetwork.update();
        }
        System.out.println(Arrays.toString(swarmNetwork.getGlobalBestPosition()));
        System.out.println(antennaArray.evaluate(swarmNetwork.getGlobalBestPosition()));
        /*System.out.println(antennaArray.evaluate(new double[]{0.3D, 0.8D, 1.5D}));
        double[][] bounds = antennaArray.bounds();
        List<Double> randoms = new LinkedList<>();
        double[] antennaPlacements = new double[bounds.length];
        for (int i = 0; i < bounds.length; i++) {
            antennaPlacements[i] = random.nextDouble(bounds[i][0], bounds[i][1]);
        }
        Arrays.sort(antennaPlacements);
        System.out.println(Arrays.toString(antennaPlacements));
        Arrays.stream(antennaArray.bounds()).forEach(e -> System.out.println(Arrays.toString(e)));*/
    }
}
