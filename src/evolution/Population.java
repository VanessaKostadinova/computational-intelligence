package evolution;

import collections.ArrayGraph;
import collections.ArrayGraphMaths;

import java.util.*;

import static collections.ArrayGraphMaths.closePath;
import static evolution.EvolutionMaths.mutate;

public class Population {
    private final int populationCount;
    private double probabilityOfMutation = 0.7;
    private final Random random;
    private final ArrayGraph arrayGraph;
    private List<Member> members;
    private Member bestMember;

    public Population(ArrayGraph arrayGraph, int populationCount) {
        this.members = new LinkedList<>();
        this.arrayGraph = arrayGraph;
        this.populationCount = populationCount;
        this.random = new Random();

        //init all
        for (int i = 0; i < populationCount; i++) {
            int[] path = ArrayGraphMaths.generateRandomOpenRoute(arrayGraph.getPathLength());

            Member newMember = new Member(path, arrayGraph.walkOpenPath(path));
            members.add(newMember);

            if (bestMember == null || bestMember.getFitness() > newMember.getFitness()) {
                bestMember = newMember;
            }
        }
    }

    public void update() {
        //create new population
        List<Member> parents = selectParents();
        List<Member> children = createChildren(parents);

        //select new generation
        List<Member> combatants = new LinkedList<>();
        combatants.addAll(children);
        combatants.addAll(members);
        members = tournament(combatants, populationCount);
    }

    private List<Member> selectParents() {
        return tournament(new LinkedList<>(members), populationCount / 2 % 2 == 0 ? populationCount / 2 : populationCount / 2 - 1);
    }

    private List<Member> createChildren(List<Member> parents) {
        List<Member> children = new LinkedList<>();

        while (!parents.isEmpty()) {
            Member parent1;
            Member parent2;

            if (parents.size() != 2) {
                parent1 = parents.remove(random.nextInt(parents.size() - 1));
                parent2 = parents.remove(random.nextInt(parents.size() - 1));
            } else {
                parent1 = parents.remove(0);
                parent2 = parents.remove(0);
            }

            for (int i = 0; i < 2; i++) {

                int[] childPath = EvolutionMaths.orderOneCrossover(parent1.getPath(), parent2.getPath());

                if (random.nextDouble(0, 1) <= probabilityOfMutation) {
                    mutate(childPath);
                }

                Member newMember = new Member(childPath, arrayGraph.walkOpenPath(childPath));
                children.add(newMember);

                if (bestMember.getFitness() > newMember.getFitness()) {
                    bestMember = newMember;
                    System.out.println(newMember.fitness);
                    System.out.println(Arrays.toString(closePath(newMember.getPath())));
                    System.out.println("-----");
                }
            }
        }

        return children;
    }

    private List<Member> tournament(List<Member> combatants, int numberOfVictors) {
        List<Member> victors = new LinkedList<>();
        //compare 3 at a time
        int selectionSize = 3;
        while (victors.size() < numberOfVictors) {
            List<Member> subList = new LinkedList<>();

            while (subList.size() != selectionSize && !combatants.isEmpty()) {
                subList.add(combatants.remove(random.nextInt(combatants.size())));
            }

            Member victor = subList.stream().min(Comparator.comparing(Member::getFitness)).orElseThrow();
            victors.add(victor);

            combatants.addAll(subList.stream().filter(e -> e != victor).toList());
        }

        return victors;
    }

    public int[] getBestPath() {
        return closePath(bestMember.path);
    }
}
