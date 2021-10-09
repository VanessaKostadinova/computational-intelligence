import java.util.LinkedHashSet;
import java.util.Set;

public class EdgeGraph {
    Set<Edge> graph;

    public EdgeGraph() {
        graph = new LinkedHashSet<>();
    }

    public void insertEdge(int weight, char startNode, char endNode) {
        graph.add(new Edge(weight, startNode, endNode));
    }
}
