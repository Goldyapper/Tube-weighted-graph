import java.util.*;

// Creation of Adjacency List
// The adjacency List consist of an ArrayList which holds the HashMap of
// (vertices,weight)
public class Weighted_Graph {
    int v;
    ArrayList<HashMap<Integer, Integer> >  adj;
    Weighted_Graph(int v) {
        this.v = v;
        this.adj = new ArrayList<>();

        for (int i = 0; i < v; i++) {
            this.adj.add(new HashMap<>());
        }
    }
    // Function to add an Edge
    void addEdge(int u, int v, int weight){
        this.adj.get(u).put(v, weight);
        this.adj.get(v).put(u, weight);
    }
    // Function for printing the whole graph
    void printGraph(){
        for (int i = 0; i < this.v; i++) {
            System.out.println("\nNode " + i + " makes an edge with:");
            for (Map.Entry<Integer, Integer> entry : this.adj.get(i).entrySet()) {
                System.out.println("\tNode " + entry.getKey() + " with edge weight " + entry.getValue());
            }
        }
    }

    void dijkstra(int src, int dest) {
        int[] dist = new int[v];
        int[] prev = new int[v];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);
        dist[src] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[]{src, 0});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];
            int d = current[1];

            if (d > dist[u]) continue;

            for (Map.Entry<Integer, Integer> neighbor : adj.get(u).entrySet()) {
                int v = neighbor.getKey();
                int weight = neighbor.getValue();
                if (dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    prev[v] = u;
                    pq.add(new int[]{v, dist[v]});
                }
            }
        }
            // If destination is unreachable
        if (dist[dest] == Integer.MAX_VALUE) {
            System.out.println("\nThere is no path from node " + src + " to node " + dest + ".");
            return;
        }
            
        // Reconstruct path
        List<Integer> path = new ArrayList<>();
        for (int at = dest; at != -1; at = prev[at]) {
            path.add(at);
        }
        Collections.reverse(path);

        // Print result
        System.out.println("\nShortest distance from node " + src + " to node " + dest + " is: " + dist[dest]);
        System.out.println("Path: ");
        for (int i = 0; i < path.size() -1; i++) {
            int from = path.get(i);
            int to = path.get(i+1);
            int weight = adj.get(from).get(to);
            System.out.println(from + " -> " + to +" (weight " + weight + ")");
        }
        System.out.println();
    }
    // Main method
    public static void main(String[] args)
    {
        
        Scanner scanner = new Scanner(System.in);
        int v = 6;//how many nodes there are 
        Weighted_Graph obj = new Weighted_Graph(v);
        
        
        //add edges
        obj.addEdge(0, 1, 10);  // node a links to node b with a weight of c
        obj.addEdge(0, 4, 20);
        obj.addEdge(1, 2, 30);
        obj.addEdge(1, 3, 40);
        obj.addEdge(1, 4, 50);
        obj.addEdge(2, 3, 60);
        obj.addEdge(3, 4, 70);
        obj.addEdge(3, 5, 20);
        obj.addEdge(4, 5, 80);
        
        obj.printGraph();
        while (true) {
        // User input for source node
        System.out.print("\nEnter the source node (0 to " + (v - 1) + ", or -1 to quit): ");
        int src = scanner.nextInt();
        if (src == -1) break;

        System.out.print("Enter the destination node (0 to " + (v - 1) + "): ");
        int dest = scanner.nextInt();

        if (src >= 0 && src < v && dest >= 0 && dest < v) {
            obj.dijkstra(src,dest);
        } else {
            System.out.println("Invalid source or destination node.");
        }
        }
        scanner.close();
    }
}
