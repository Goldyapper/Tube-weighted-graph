import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Creation of Adjacency List
// The adjacency List consist of an ArrayList which holds the HashMap of
// (vertices,weight)
public class Weighted_Graph {
    int v;
    ArrayList<HashMap<Integer, Integer> >  adj;
    Map<String, Integer> nameToIndex = new HashMap<>();
    Map<Integer, String> indexToName = new HashMap<>();

    Weighted_Graph(int v) {
        this.v = v;
        this.adj = new ArrayList<>();

        for (int i = 0; i < v; i++) {
            this.adj.add(new HashMap<>());
        }
    }

    void loadEdgesFromCSV(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;  // skip blank lines
                String[] parts = line.split(",");
                if (parts.length != 3) continue;     // skip malformed lines

                String src = parts[0].trim().toLowerCase();
                String dest = parts[1].trim().toLowerCase();
                String weightStr = parts[2].trim();
                
                if (weightStr.isEmpty()) {
                    System.out.println("Skipping line with empty weight: " + line);
                    continue;
                }

                int weight;
                try {
                    weight = Integer.parseInt(weightStr);
                } catch (NumberFormatException e) {
                    System.out.println("Skipping line with invalid weight: " + line);
                    continue;
                }

                Integer u = nameToIndex.get(src);
                Integer v = nameToIndex.get(dest);

                if (u != null && v != null) {
                    this.addEdge(u, v, weight);
                } else {
                    System.out.println("Skipping invalid edge: " + src + " -> " + dest);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }

    void addStationNames(String[] names) {
        for (int i = 0; i < names.length; i++) {
            nameToIndex.put(names[i].toLowerCase(), i);
            indexToName.put(i, names[i]);
        }
    }
    // Function to add an Edge
    void addEdge(String Station_A, String Station_B, int weight){
        Integer u = nameToIndex.get(Station_A.toLowerCase());
        Integer v = nameToIndex.get(Station_B.toLowerCase());

        if (u == null || v == null) {
        System.out.println("Error: One or both station names are invalid."    + Station_A + ", " + Station_B);
        return;
        }
        adj.get(u).put(v, weight);
        adj.get(v).put(u, weight);
    }
    void addEdge(int u, int v, int weight) {
        this.adj.get(u).put(v, weight);
        this.adj.get(v).put(u, weight);  // for undirected graph
}
    // Function for printing the whole graph
    void printGraph(){
        for (int i = 0; i < this.v; i++) {
            String fromStation = indexToName.getOrDefault(i, "Node " + i);
            System.out.println("\n " + fromStation + " connects with:");
            
            
            for (Map.Entry<Integer, Integer> entry : this.adj.get(i).entrySet())
                {
                String toStation = indexToName.getOrDefault(entry.getKey(), "Node " + entry.getKey());
                System.out.println("\t " + toStation + " with travel time " + entry.getValue()+ " mins");
                }
        }
    }
    static String[] loadStationNamesFromFile(String filename) {
        List<String> stationList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stationList.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading station names: " + e.getMessage());
        }
        return stationList.toArray(new String[0]);
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
            System.out.println("\nThere is no path from " + indexToName.get(src) + " to " + indexToName.get(dest) + ".");
            return;
        }
            
        // Reconstruct path
        List<Integer> path = new ArrayList<>();
        for (int at = dest; at != -1; at = prev[at]) {
            path.add(at);
        }
        Collections.reverse(path);

        // Print result
        System.out.println("\nShortest distance from " + indexToName.get(src) + " to " + indexToName.get(dest) + " is: " + dist[dest] + " mins");
        System.out.println("");
        System.out.println("Path:");
        for (int i = 0; i < path.size() -1; i++) {
            int from = path.get(i);
            int to = path.get(i+1);
            int weight = adj.get(from).get(to);
            System.out.println(indexToName.get(from) + " -> " + indexToName.get(to)+" ("+ weight +" mins)");
        }
        System.out.println();
    }
    void printAllDistancesFrom(String stationName) {
        Integer src = nameToIndex.get(stationName.toLowerCase());
        if (src == null) {
            System.out.println("Error: Station not found - " + stationName);
            return;
        }

        int[] dist = new int[v];
        Arrays.fill(dist, Integer.MAX_VALUE);
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
                    pq.add(new int[]{v, dist[v]});
                }
            }
        }
         // Collect results for sorting
        List<int[]> reachable = new ArrayList<>();
        List<Integer> unreachable = new ArrayList<>();

        for (int i = 0; i < v; i++) {
            if (i == src) continue;
            if (dist[i] != Integer.MAX_VALUE) {
                reachable.add(new int[]{i, dist[i]});
            } else {
                unreachable.add(i);
            }
        }

        // Sort by travel time
        reachable.sort(Comparator.comparingInt(a -> a[1]));
        System.out.println("\nShortest travel times from " + indexToName.get(src) + ":");
         // Print reachable stations
        for (int[] entry : reachable) {
            int dest = entry[0];
            int time = entry[1];

            System.out.println(indexToName.get(dest) + ": " + time + " mins");
        }

        // Print unreachable stations
        if (!unreachable.isEmpty()) {
            System.out.println("\nUnreachable stations:");
            for (int dest : unreachable) {
                System.out.println(indexToName.get(dest) + ": Unreachable");
            }
        }
    }
    void printAllDistancesFromAllStations() {
        for (int i = 0; i < v; i++) {
            String stationName = indexToName.get(i);
            System.out.println("\n=== Distances from " + stationName + " ===");
            printAllDistancesFrom(stationName);
        }
    }

    // Main method
    public static void main(String[] args)
    {
        
        Scanner scanner = new Scanner(System.in);
        String[] stations = loadStationNamesFromFile("tubestations.txt");
        Weighted_Graph obj = new Weighted_Graph(stations.length);
        obj.addStationNames(stations);
        obj.loadEdgesFromCSV("edges.csv");
        
        obj.printAllDistancesFromAllStations();    
        //obj.printGraph();

        while (true) {
            // User input for source node
            System.out.print("\nEnter a station to see shortest paths to all others or 'skip' to skip: ");
            String stationName = scanner.nextLine().trim();
            if (stationName.equalsIgnoreCase("skip")) {
                // skip straight to normal path finding
            } else {
                obj.printAllDistancesFrom(stationName);
            }
            System.out.print("\nStations that you can choose from are only those on the following lines: Bakerloo, Central, Jubilee, Metropolitian");
            System.out.print("\nEnter the source station name (or 'exit' to quit): ");
            String srcName = scanner.nextLine().trim();
            if (srcName.equalsIgnoreCase("exit")) break;

            System.out.print("Enter the destination station name: ");
            String destName = scanner.nextLine().trim();

            Integer src = obj.nameToIndex.get(srcName.toLowerCase());
            Integer dest = obj.nameToIndex.get(destName.toLowerCase());

            if (src == null || dest == null) {
                System.out.println("Invalid source or destination node.");
                continue;
            }
            obj.dijkstra(src, dest);
        }
        scanner.close();
    }
}
