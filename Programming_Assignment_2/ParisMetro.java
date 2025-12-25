import java.util.*;

public class ParisMetro {

    // Core graph data: names, edges, IDs and adjacency lists
    private String[] stationNames;
    private EdgeB[] connections;
    private int[] vertexNumber;
    private ArrayList<EdgeB>[] metroGraph;

    // Union–find structure over vertices using walking edges (cost -1)
    private PartitionB<Integer> hubs;
    private NodeB<Integer>[] nodes;

    // Input sizes
    private int n;
    private int m;

    // Hub-related bookkeeping
    int count = 0;          // number of hub clusters
    int vcount = 0;         // total vertices that belong to hubs
    String[] hubnamesBIG;   // temporary hub names
    String[] hubnames;      // final hub names
    int[] hubIndex;         // hubIndex[v] = hub id of v, or -1 if not in a hub

    // Kruskal / MST state
    private int checker = 0;
    private boolean possible = true;

    public static void main(String[] args) {
        ParisMetro pm = new ParisMetro();
        pm.readMetro();
        pm.solve();
    }

    // Returns true if key appears in the array (ignores null entries)
    public Boolean finder(String[] str, String key) {
        if (str == null) return false;
        for (int i = 0; i < str.length; i++) {
            if (str[i] != null && str[i].equals(key)) {
                return true;
            }
        }
        return false;
    }

    // Linear search in array of strings, returns index or -1
    public int findElementIndex(String[] arr, String target) {
        if (arr == null) return -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null && arr[i].equals(target)) {
                return i;
            }
        }
        return -1;
    }

    // Reads the metro description and builds the graph and hub union–find
    public void readMetro() {
        Scanner scan = new Scanner(System.in);

        n = scan.nextInt();
        m = scan.nextInt();

        System.out.println("Paris Metro Graph has " + n + " vertices and " + m + " edges." + "\n");

        stationNames = new String[n];
        connections = new EdgeB[m];
        vertexNumber = new int[n];
        metroGraph = new ArrayList[n];
        hubs = new PartitionB<Integer>();
        nodes = new NodeB[n];

        // Read vertices: id + station name, initialize adjacency and UF node
        for (int i = 0; i < n; i++) {
            vertexNumber[i] = scan.nextInt();
            String stationName = scan.nextLine().trim();
            stationNames[i] = stationName;
            metroGraph[i] = new ArrayList<EdgeB>();
            nodes[i] = hubs.makeClusterB(i);
        }

        scan.nextLine(); // discard separator line (e.g., '$')

        // Read edges and connect vertices; walking edges are merged into hub clusters
        for (int i = 0; i < m; i++) {
            int v1 = scan.nextInt();
            int v2 = scan.nextInt();
            int weight = scan.nextInt();
            EdgeB e = new EdgeB(v1, v2, weight);
            connections[i] = e;
            metroGraph[v1].add(e);

            if (weight == -1) {
                hubs.unionB(nodes[v1], nodes[v2]);
            }
        }
    }

    // High-level driver: compute hub roots, build hub graph, compute MST
    public void solve() {
        NodeB<Integer>[] root = makeRoots();
        makeHubs(root);
        EdgeB[] Segments = makeSegments();
        makeMST(Segments);
    }

    // Builds an array of representatives: root[i] is the hub leader of vertex i
    private NodeB<Integer>[] makeRoots() {
        @SuppressWarnings("unchecked")
        NodeB<Integer>[] root = new NodeB[n];
        for (int i = 0; i < n; i++) {
            root[i] = hubs.findB(nodes[i]);
        }
        return root;
    }

    // Identifies all hub clusters (size > 1) and assigns a hub index to every vertex
    private void makeHubs(NodeB<Integer>[] root) {
        @SuppressWarnings("unchecked")
        NodeB<Integer>[] hubRoots = new NodeB[n];
        String[] hubNamesTemp = new String[n];

        hubIndex = new int[n];
        for (int i = 0; i < n; i++) {
            hubIndex[i] = -1;
        }

        int hubCount = 0;
        vcount = 0;

        // Scan all vertices and group by cluster representative
        for (int i = 0; i < n; i++) {
            NodeB<Integer> r = root[i];
            int size = hubs.clusterSizeB(r);

            if (size > 1) {
                vcount++;

                int pos = -1;
                for (int j = 0; j < hubCount; j++) {
                    if (hubRoots[j] == r) {
                        pos = j;
                        break;
                    }
                }

                // First occurrence of this hub cluster
                if (pos == -1) {
                    pos = hubCount;
                    hubRoots[hubCount] = r;
                    hubNamesTemp[hubCount] = stationNames[i];
                    hubCount++;
                }

                hubIndex[i] = pos;
            }
        }

        count = hubCount;
        hubnamesBIG = new String[count];
        hubnames = new String[count];
        for (int i = 0; i < count; i++) {
            hubnamesBIG[i] = hubNamesTemp[i];
            hubnames[i] = hubnamesBIG[i];
        }

        System.out.println("Hub Stations = " + Arrays.toString(hubnames) + "\n");
        System.out.println("Number of Hub Stations = " + count + " (total Hub Vertices = " + vcount + ")" + "\n");
    }

    // Builds all candidate “segments” (expensive connections) between hub pairs
    private EdgeB[] makeSegments() {
        ArrayList<EdgeB> SegmentsList = new ArrayList<EdgeB>();

        // Start from every vertex that belongs to a hub
        for (int start = 0; start < n; start++) {
            int hubStart = hubIndex[start];
            if (hubStart == -1){ 
                continue;
            }

            // Follow each positive-weight edge going out of this vertex
            for (int k = 0; k < metroGraph[start].size(); k++) {
                EdgeB e = metroGraph[start].get(k);

                if (e.getCost() <= 0) {
                    continue;
                }
                int prev = start;
                int curr = e.getv();
                int cost = e.getCost();

                // Walk along the metro line until we reach another hub or get stuck
                while (true) {
                    if (curr < 0 || curr >= n) {
                        break;
                    }
                    int hubCurr = hubIndex[curr];

                    // Reached another hub cluster
                    if (hubCurr != -1) {
                        if (hubCurr != hubStart) {
                            SegmentsList.add(new EdgeB(hubStart, hubCurr, cost));
                        }
                        break;
                    }

                    // Choose next non-walking edge that does not immediately go back
                    EdgeB next = null;
                    for (int j = 0; j < metroGraph[curr].size(); j++) {
                        EdgeB e2 = metroGraph[curr].get(j);
                        if (e2.getCost() > 0 && e2.getv() != prev) {
                            next = e2;
                            break;
                        }
                    }

                    if (next == null) {
                        // No further extension possible
                        break;
                    }

                    prev = curr;
                    curr = next.getv();
                    cost += next.getCost();
                }
            }
        }

        EdgeB[] Segments = new EdgeB[SegmentsList.size()];
        SegmentsList.toArray(Segments);

        System.out.println("Number of Possible Segments = " + Segments.length + "\n");
        return Segments;
    }

    // Runs Kruskal on the hub graph and prints MST cost and chosen segments
    private void makeMST(EdgeB[] Segments) {
        if (count <= 1) {
            System.out.println("Impossible");
            return;
        }
        PartitionB<Integer> hubsSmall = new PartitionB<Integer>();
        NodeB<Integer>[] nodesSmall = new NodeB[count];

        // Create a UF node for each hub
        for (int i = 0; i < count; i++) {
            nodesSmall[i] = hubsSmall.makeClusterB(i);
        }
        EdgeB[] T = Kruskal(Segments, hubsSmall, count, Segments.length, nodesSmall);
        if (!getpossible()) {
            System.out.println("Impossible");
            return;
        }
        int totalCost = costfinder(T);
        System.out.println("Total cost of the MST = $" + totalCost);
        System.out.println("Segments to Buy:");
        // Collect and sort non-null MST edges by cost
        ArrayList<EdgeB> mstEdges = new ArrayList<EdgeB>();
        for (int i = 0; i < T.length; i++) {
            if (T[i] != null) {
                mstEdges.add(T[i]);
            }
        }
        Collections.sort(mstEdges, new Comparator<EdgeB>() {
            public int compare(EdgeB a, EdgeB b) {
                return a.getCost() - b.getCost();
            }
        });
        int num = 1;
        for (int i = 0; i < mstEdges.size(); i++) {
            EdgeB e = mstEdges.get(i);
            String nameU = hubnames[e.getu()];
            String nameV = hubnames[e.getv()];
            System.out.println(num + "( " + nameU + " - " + nameV + " ) - $" + e.getCost());
            num++;
        }
    }

    // Kruskal’s algorithm over the hub graph using PartitionB, This is from the slides. 
    public EdgeB[] Kruskal(EdgeB[] edges, PartitionB<Integer> partition, int numberOfVertices, int numberOfEdges, NodeB<Integer>[] nodes) {
        if (numberOfVertices <= 1) {
            possible = true;
            return new EdgeB[0];
        }
        checker = 0;
        PriorityQueue<EdgeB> sorted_Edges =new PriorityQueue<EdgeB>(edges.length, new Comparator<EdgeB>() {
                    public int compare(EdgeB a, EdgeB b) {
                        return a.getCost() - b.getCost();
                    }
                });
        for (int i = 0; i < edges.length; i++) {
            sorted_Edges.add(edges[i]);
        }
        // initilizing the T used for MST
        EdgeB[] T = new EdgeB[numberOfVertices - 1];

        //  add the cheapest edges that connect different components
        while (!sorted_Edges.isEmpty() && checker < numberOfVertices - 1) {
            EdgeB e = sorted_Edges.poll();
            NodeB<Integer> u = nodes[e.getu()];
            NodeB<Integer> v = nodes[e.getv()];
            if (partition.findB(u) != partition.findB(v)) {
                T[checker] = e;
                partition.unionB(u, v);
                checker++;
            }
        }
        // calls the possiblitychecker method explained on line 318
        possible = possiblitychecker(T, numberOfVertices, numberOfEdges);
        return T;
    }

    // checks if the MST is possible aftet running kruskals 
    public boolean possiblitychecker(EdgeB[] T, int numberOfVertices, int numberOfEdges) {
        boolean nonull = true;
        // if any values in T is null, nonull bool is set to false, this comes in play in the next if condition. if T has any value as null, the edges didnt get full therefore no MST.
        for (int j = 0; j < T.length; j++) {
            if (T[j] == null) {
                nonull = false;
                break;
            }
        }
        // this just checks edge cases like prev mentioned, if any fail, deems the MST impossible. 
        if (!nonull || checker != numberOfVertices - 1 || (numberOfVertices > 1 && numberOfEdges == 0)) {
            return false;
        }
        return true;
    }

    public boolean getpossible() {
        return possible;
    }

    // Computes total cost of the MST after Kruskal 
    public int costfinder(EdgeB[] T) {
        int sum = 0;
        // the for loop to interatr through and add each edge's cost
        for (int i = 0; i < T.length; i++) {
            if (T[i] != null) {
                sum += T[i].getCost();
            }
        }
        return sum;
    }
}
