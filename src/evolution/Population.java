package evolution;

import collections.ArrayGraph;
import collections.ArrayGraphMaths;

import java.util.*;

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
        for(int i = 0; i < populationCount; i++){
            int[] path = ArrayGraphMaths.generateRandomOpenRoute(arrayGraph.getPathLength());

            Member newMember = new Member(path, arrayGraph.walkOpenPath(path));
            members.add(newMember);

            if(bestMember == null || bestMember.getFitness() > newMember.getFitness()){
                bestMember = newMember;
            }
        }
    }

    public void update(){
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
        return tournament(new LinkedList<>(members), populationCount/2 % 2 == 0 ? populationCount/2 : populationCount/2 - 1);
    }

    private List<Member> createChildren(List<Member> parents){
        List<Member> children = new LinkedList<>();

        while(!parents.isEmpty()){
            Member parent1;
            Member parent2;

            if(parents.size() != 2){
                parent1 = parents.remove(random.nextInt(parents.size()-1));
                parent2 = parents.remove(random.nextInt(parents.size()-1));
            } else {
                parent1 = parents.remove(0);
                parent2 = parents.remove(0);
            }

            for(int i = 0; i < 2; i++) {

                int[] childPath = EvolutionMaths.orderOneCrossover(parent1.getPath(), parent2.getPath());

                if (random.nextDouble(0, 1) <= probabilityOfMutation) {
                    EvolutionMaths.mutate(childPath);
                }

                Member newMember = new Member(childPath, arrayGraph.walkOpenPath(childPath));
                children.add(newMember);

                if (bestMember.getFitness() > newMember.getFitness()) {
                    bestMember = newMember;
                    System.out.println(newMember.fitness);
                    System.out.println(Arrays.toString(newMember.getPath()));
                }
            }
        }

        return children;
    }

    private List<Member> tournament(List<Member> combatants, int numberOfVictors){
        List<Member> victors = new LinkedList<>();
        //compare 1/4 at a time
        int selectionSize = numberOfVictors/4;
        while (victors.size() < numberOfVictors){
            List<Member> subList = new LinkedList<>();

            while(subList.size() != selectionSize * 2 && !combatants.isEmpty()){
                subList.add(combatants.remove(random.nextInt(combatants.size())));
            }

            subList.sort(Comparator.comparingDouble(Member::getFitness));

            List<Member> selected = new LinkedList<>();

            int i = 0;
            for(Member m : subList){
                if(i < selectionSize){
                    selected.add(m);
                } else {
                    combatants.add(m);
                }
            }
            victors.addAll(selected);
        }

        return victors;
    }

    private List<Member> roulette(List<Member> combatants, int numberOfVictors){
        double totalFitness = combatants.stream().mapToDouble(Member::getFitness).sum();
        return combatants.stream().filter(e -> random.nextDouble(totalFitness) <= e.getFitness()).toList();
    }

    public Member getBestMember() {
        return bestMember;
    }
}
