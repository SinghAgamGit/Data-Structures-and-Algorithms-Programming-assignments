public class Partition<E> {
    int numberOfCluster;
    Node<E> dummyCluster;  // Links all clusters together

    public Partition() {
        dummyCluster = new Node<>(null, null, null);
        dummyCluster.setTail(dummyCluster);
    }


    public Node<E> makeCluster(E x) {
        Node<E> child = new Node<>(x, null, null);
        // attach to end of cluster list
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

    public void union(Node<E> p, Node<E> q) {
        // if already in same cluster, do nothing
        if (p.getClusterReference() == q.getClusterReference()) return;

        int one = p.getClusterReference().getClusterSize();
        int two = q.getClusterReference().getClusterSize();

        if (one > two) { // attach smaller cluster to larger
            // remove q’s cluster from cluster list
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

            // update cluster references
            Node<E> current = q.getClusterReference();
            while (current != null) {
                current.setClusterReference(p.getClusterReference());
                current = current.getNext();
            }
        } else {
            // same logic, merging p into q
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

    public Node<E> find(Node<E> p) {
        return p.getClusterReference();  // returns cluster leader
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

    public int[] clusterSizes() {
        int[] array = new int[numberOfClusters()];
        int i = 0;
        if (dummyCluster.getClusterNext() == null) return null;

        // collect all cluster sizes
        Node<E> current = dummyCluster.getClusterNext();
        while (current != null) {
            array[i++] = clusterSize(current);
            current = current.getClusterNext();
        }
        // sort descending
        java.util.Arrays.sort(array);
        for (int j = 0; j < array.length / 2; j++) {
            int temp = array[j];
            array[j] = array[array.length - j - 1];
            array[array.length - j - 1] = temp;
        }
        return array;
    }
}
