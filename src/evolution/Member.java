package evolution;

public class Member {
    double fitness;
    int[] path;

    public Member(int[] path, double fitness) {
        this.fitness = fitness;
        this.path = path;
    }

    public double getFitness() {
        return fitness;
    }

    public int[] getPath() {
        return path;
    }
}
