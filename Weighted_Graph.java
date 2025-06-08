import java.util.*;

// Creation of Adjacency List
// The adjacency List consist of an ArrayList within an
// ArrayList. The inner ArrayList holds the HashMap of
// (vertices,weight)
public class Weighted_Graph {
    int v;
    ArrayList<HashMap<Integer, Integer> >  adj;
    Weighted_Graph(int v)
    {
        this.v = v;
        this.adj = new ArrayList<>();

        for (int i = 0; i < v; i++) {
            this.adj.add(new HashMap<>());
        }
    }
    // Function to add an Edge
    void addEdge(int u, int v, int weight)
    {
        this.adj.get(u).put(v, weight);

        this.adj.get(v).put(u, weight);
    }

    // Function for printing the whole graph
    // Stream API has been used
    // to easily access the HashMap elements
    // This code may not work in versions
    // prior to java 8

    void printGraph()
    {
        for (int i = 0; i < this.v; i++) {
    System.out.println("\nNode " + i + " makes an edge with:");
    for (Map.Entry<Integer, Integer> entry : this.adj.get(i).entrySet()) {
        System.out.println("\tNode " + entry.getKey() + " with edge weight " + entry.getValue());
    }
}

    }
    // Main method
    public static void main(String[] args)
    {
        int v = 5;//how many nodes there are 
        Weighted_Graph obj = new Weighted_Graph(v);
        obj.addEdge(0, 1, 10);  // node a links to node b with a weight of c
        obj.addEdge(0, 4, 20);
        obj.addEdge(1, 2, 30);
        obj.addEdge(1, 3, 40);
        obj.addEdge(1, 4, 50);
        obj.addEdge(2, 3, 60);
        obj.addEdge(3, 4, 70);
        obj.printGraph();
    }
}