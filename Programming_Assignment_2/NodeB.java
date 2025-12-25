public class NodeB<E>{
  private E element;
  private NodeB<E> next;
  private NodeB<E> clusterReference;
  private int clusterSize; 
  private NodeB<E> tail; 
  private NodeB<E> prev;
  private NodeB<E> ClusterNext;
  // Creates a node with null references to its element and next node. 
  public NodeB() {
    this(null, null, null);
  }
  // Creates a node with the given element and next node. 
  public NodeB(E e, NodeB<E> n, NodeB<E> c) {
    element = e;
    next = n;
    clusterReference = c;
  }
  // Accessor methods:
  public E getElement() {
    return element; 
  }
  public NodeB<E> getNext() { 
    return next;
  }
  // Modifier methods:
  public void setElement(E newElem) { 
    element = newElem; 
  }
  public void setNext(NodeB<E> newNext) {
    next = newNext; 
  }
  public NodeB<E> getClusterReference(){
    return clusterReference;
  }
  public void setClusterReference(NodeB<E> newCluster){
    clusterReference = newCluster;

  }
    public int getClusterSize(){
    return clusterSize;
  }
  public void setClusterSize(int size){
    clusterSize = size;

  }
    public void setTail(NodeB<E> newTail) {
    tail = newTail;
  }
  public NodeB<E> getTail() { 
    return tail; 
  }
    public void setPrev(NodeB<E> newPrev){
    prev = newPrev;
  }
  public NodeB<E> getPrev() { 
    return prev; 
  }
  public void setClusterNext(NodeB<E> newClusterNext){
    ClusterNext = newClusterNext;
  }
  public NodeB<E> getClusterNext() { 
    return ClusterNext; 
  }
}
