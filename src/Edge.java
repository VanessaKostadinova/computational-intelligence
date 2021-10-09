import java.util.Objects;

public class Edge {
    private int weight;
    private char startNode;
    private char endNode;

    public Edge(int weight, char startNode, char endNode) {
        this.weight = weight;
        this.startNode = startNode;
        this.endNode = endNode;
    }

    public int getWeight() {
        return weight;
    }

    public char getStartNode() {
        return startNode;
    }

    public char getEndNode() {
        return endNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return startNode == edge.startNode &&
                endNode == edge.endNode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startNode, endNode);
    }
}
