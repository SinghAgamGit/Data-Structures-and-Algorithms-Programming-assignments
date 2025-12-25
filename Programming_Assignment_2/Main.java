import java.util.PriorityQueue;

public class Main {

  // Edge represents an undirected weighted edge between two stations
  static class Edge {
    private Integer u;
    private Integer v;
    private Integer Cost;
    Edge() {
      this(0, 0, 0);
    }

    Edge(Integer Station1index, Integer Station2index, Integer c) {
      u = Station1index;
      v = Station2index;
      Cost = c;
    }

    public Integer getCost() {
      return Cost;
    }

    public Integer getu() {
      return u;
    }

    public Integer getv() {
      return v;
    }
  }

  // Helper to find index of a station name in the Stations array
  static class FindIndex {
    public static int findElementIndex(String[] arr, String target) {
      for (int i = 0; i < arr.length; i++) {
        if (arr[i].equals(target)) {
          return i; // return index if the target is found
        }
      }
      return -1; // not found
    }
  }

  // Node used inside the Partition structure (linked-list based UF)
  static class Node<E> {
    private E element;
    private Node<E> next;
    private Node<E> clusterReference;
    private int clusterSize;
    private Node<E> tail;
    private Node<E> prev;
    private Node<E> ClusterNext;

    // default constructor: empty node
    public Node() {
      this(null, null, null);
    }

    // creates a node with the given element and references
    public Node(E e, Node<E> n, Node<E> c) {
      element = e;
      next = n;
      clusterReference = c;
    }

    // Accessor methods
    public E getElement() {
      return element;
    }

    public Node<E> getNext() {
      return next;
    }

    // Mutators
    public void setElement(E newElem) {
      element = newElem;
    }

    public void setNext(Node<E> newNext) {
      next = newNext;
    }

    public Node<E> getClusterReference() {
      return clusterReference;
    }

    public void setClusterReference(Node<E> newCluster) {
      clusterReference = newCluster;
    }

    public int getClusterSize() {
      return clusterSize;
    }

    public void setClusterSize(int size) {
      clusterSize = size;
    }

    public void setTail(Node<E> newTail) {
      tail = newTail;
    }

    public Node<E> getTail() {
      return tail;
    }

    public void setPrev(Node<E> newPrev) {
      prev = newPrev;
    }

    public Node<E> getPrev() {
      return prev;
    }

    public void setClusterNext(Node<E> newClusterNext) {
      ClusterNext = newClusterNext;
    }

    public Node<E> getClusterNext() {
      return ClusterNext;
    }
  }

  // Partition = union–find structure implemented using linked lists of Nodes
  static class Partition<E> {
    int numberOfCluster;
    Node<E> dummyCluster; // links all cluster heads together

    public Partition() {
      dummyCluster = new Node<>(null, null, null);
      dummyCluster.setTail(dummyCluster);
    }

    // creates a singleton cluster containing x
    public Node<E> makeCluster(E x) {
      Node<E> child = new Node<>(x, null, null);

      // attach new cluster at the end of the cluster list
      child.setPrev(dummyCluster.getTail());
      dummyCluster.getTail().setClusterNext(child);
      dummyCluster.setTail(child);

      // initialize cluster properties
      child.setClusterReference(child);
      numberOfCluster++;
      child.setClusterSize(1);
      child.setTail(child);
      return child;
    }

    // union by size: merge clusters of p and q
    public void union(Node<E> p, Node<E> q) {
      // already in same cluster -> nothing to do
      if (p.getClusterReference() == q.getClusterReference()) return;

      int one = p.getClusterReference().getClusterSize();
      int two = q.getClusterReference().getClusterSize();

      if (one > two) { // attach smaller cluster to larger (q into p)
        // detach q’s cluster from the cluster list
        if (dummyCluster.getTail() == q.getClusterReference()) {
          q.getClusterReference().getPrev().setClusterNext(null);
          dummyCluster.setTail(q.getClusterReference().getPrev());
        } else {
          q.getClusterReference().getPrev().setClusterNext(q.getClusterReference().getClusterNext());
          q.getClusterReference().getClusterNext().setPrev(q.getClusterReference().getPrev());
        }

        // merge q into p
        p.getClusterReference().setClusterSize(one + two);
        Node<E> lastOfOne = p.getClusterReference().getTail();
        lastOfOne.setNext(q.getClusterReference());
        p.getClusterReference().setTail(q.getClusterReference().getTail());

        // redirect all nodes in q's cluster to p's clusterReference
        Node<E> current = q.getClusterReference();
        while (current != null) {
          current.setClusterReference(p.getClusterReference());
          current = current.getNext();
        }
      } else { // attach p into q
        if (dummyCluster.getTail() == p.getClusterReference()) {
          p.getClusterReference().getPrev().setClusterNext(null);
          dummyCluster.setTail(p.getClusterReference().getPrev());
        } else {
          p.getClusterReference().getPrev().setClusterNext(p.getClusterReference().getClusterNext());
          p.getClusterReference().getClusterNext().setPrev(p.getClusterReference().getPrev());
        }

        q.getClusterReference().setClusterSize(one + two);
        Node<E> lastOfTwo = q.getClusterReference().getTail();
        lastOfTwo.setNext(p.getClusterReference());
        q.getClusterReference().setTail(p.getClusterReference().getTail());

        Node<E> current = p.getClusterReference();
        while (current != null) {
          current.setClusterReference(q.getClusterReference());
          current = current.getNext();
        }
      }
      numberOfCluster--;
    }

    // find returns the cluster leader node
    public Node<E> find(Node<E> p) {
      return p.getClusterReference();
    }

    public E element(Node<E> p) {
      return p.getElement();
    }

    public int numberOfClusters() {
      return numberOfCluster;
    }

    public int clusterSize(Node<E> p) {
      return p.getClusterSize();
    }

    // returns all positions in p's cluster as an array
    public Node<E>[] clusterPositions(Node<E> p) {
      Node<E>[] array = new Node[clusterSize(p)];
      Node<E> current = p.getClusterReference();
      int i = 0;
      while (current != null) {
        array[i++] = current;
        current = current.getNext();
      }
      return array;
    }

    // returns all cluster sizes sorted in descending order
    public int[] clusterSizes() {
      int[] array = new int[numberOfClusters()];
      int i = 0;
      if (dummyCluster.getClusterNext() == null) return null;

      // collect sizes of all clusters
      Node<E> current = dummyCluster.getClusterNext();
      while (current != null) {
        array[i++] = clusterSize(current);
        current = current.getClusterNext();
      }

      // sort ascending then reverse to descending
      java.util.Arrays.sort(array);
      for (int j = 0; j < array.length / 2; j++) {
        int temp = array[j];
        array[j] = array[array.length - j - 1];
        array[array.length - j - 1] = temp;
      }
      return array;
    }
  }

  // fields used during Kruskal
  int checker = 0;
  boolean possible = true;

  // Kruskal's algorithm to compute MST from a list of edges
  public Edge[] Kruskal(Edge[] edges, Partition<Integer> partition, int numberOfVertices, int numberOfEdges, Node<Integer>[] nodes) {
    checker = 0;

    // min-heap of edges by cost
    PriorityQueue<Edge> sorted_Edges = new PriorityQueue<>((a, b) -> a.getCost() - b.getCost());
    for (int i = 0; i < edges.length; i++) {
      sorted_Edges.add(edges[i]);
    }

    Edge[] T = new Edge[numberOfVertices - 1]; // MST will have V-1 edges

    // pick cheapest edges that connect different components
    while (!sorted_Edges.isEmpty() && checker < numberOfVertices - 1) {
      Edge e = sorted_Edges.poll();
      Node<Integer> u = nodes[e.getu()];
      Node<Integer> v = nodes[e.getv()];
      if (partition.find(u) != partition.find(v)) {
        T[checker] = e;
        partition.union(u, v);
        checker++;
      }
    }

    // verify that we produced a valid spanning tree
    possible = possiblitychecker(T, numberOfVertices, numberOfEdges);
    return T;
  }

  // checks if T corresponds to a valid MST
  public boolean possiblitychecker(Edge[] T, int numberOfVertices, int numberOfEdges) {
    boolean nonull = true;
    for (int j = 0; j < T.length; j++) {
      if (T[j] == null) {
        nonull = false;
        break;
      }
    }
    // must be full (no null), have exactly V-1 edges, and at least one edge if >1 vertex
    if (nonull == false || checker != numberOfVertices - 1 || (numberOfVertices > 1 && numberOfEdges == 0)) {
      return false;
    }
    return true;
  }

  public boolean getpossible() {
    return possible;
  }

  // sums cost of all edges in T
  public int costfinder(Edge[] T) {
    int sum = 0;
    for (int i = 0; i < T.length; i++) {
      sum += T[i].getCost();
    }
    return sum;
  }

  // main: read graph, run Kruskal, print result per instance
  public static void main(String[] args) {
    java.util.Scanner sc = new java.util.Scanner(System.in);
    int ver = sc.nextInt();
    int e = sc.nextInt();

    // multiple test cases until "0 0" is read
    while (!(ver == 0 && e == 0)) {
      int numberOfVertices = ver;
      int numberOfEdges = e;

      Partition<Integer> vertices = new Partition<>();
      Node<Integer>[] nodes = new Node[numberOfVertices];
      String[] Stations = new String[numberOfVertices];
      Edge[] connections = new Edge[numberOfEdges];

      // read station names and create singleton clusters
      for (int i = 0; i < numberOfVertices; i++) {
        Stations[i] = sc.next();
        nodes[i] = vertices.makeCluster(i);
      }

      // read all edges as (stationName1, stationName2, cost)
      for (int i = 0; i < numberOfEdges; i++) {
        int u = FindIndex.findElementIndex(Stations, sc.next());
        int v = FindIndex.findElementIndex(Stations, sc.next());
        connections[i] = new Edge(u, v, sc.nextInt());
      }

      // read origin (used only for single-vertex special case)
      String origin = sc.next();

      Main Kruskal = new Main();
      Edge[] T = Kruskal.Kruskal(connections, vertices, numberOfVertices, numberOfEdges, nodes);

      if (Kruskal.getpossible() == false) {
        System.out.println("Impossible");
      } else if (numberOfVertices == 1 && origin.equals(Stations[0])) {
        // trivial case: one station only
        System.out.println(0);
      } else {
        // print total MST cost
        System.out.println(Kruskal.costfinder(T));
      }

      // read next instance
      ver = sc.nextInt();
      e = sc.nextInt();
    }
  }
}
