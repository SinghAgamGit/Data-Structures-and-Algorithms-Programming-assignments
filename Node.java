public class Node<E>{
  private E element;
  private Node<E> next;
  private Node<E> clusterReference;
  private int clusterSize; 
  private Node<E> tail; 
  private Node<E> prev;
  private Node<E> ClusterNext;
  // Creates a node with null references to its element and next node. 
  public Node() {
    this(null, null, null);
  }
  // Creates a node with the given element and next node. 
  public Node(E e, Node<E> n, Node<E> c) {
    element = e;
    next = n;
    clusterReference = c;
  }
  // Accessor methods:
  public E getElement() {
    return element; 
  }
  public Node<E> getNext() { 
    return next;
  }
  // Modifier methods:
  public void setElement(E newElem) { 
    element = newElem; 
  }
  public void setNext(Node<E> newNext) {
    next = newNext; 
  }
  public Node<E> getClusterReference(){
    return clusterReference;
  }
  public void setClusterReference(Node<E> newCluster){
    clusterReference = newCluster;

  }
    public int getClusterSize(){
    return clusterSize;
  }
  public void setClusterSize(int size){
    clusterSize = size;

  }
    public void setTail(Node<E> newTail) {
    tail = newTail;
  }
  public Node<E> getTail() { 
    return tail; 
  }
    public void setPrev(Node<E> newPrev){
    prev = newPrev;
  }
  public Node<E> getPrev() { 
    return prev; 
  }
  public void setClusterNext(Node<E> newClusterNext){
    ClusterNext = newClusterNext;
  }
  public Node<E> getClusterNext() { 
    return ClusterNext; 
  }
}
