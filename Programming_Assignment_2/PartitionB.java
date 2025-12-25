public class PartitionB<E> {
    int numberOfCluster;
    NodeB<E> dummyCluster;  // Links all clusters together

    public PartitionB() {
        dummyCluster = new NodeB<>(null, null, null);
        dummyCluster.setTail(dummyCluster);
    }

    public NodeB<E> makeClusterB(E x) {
        NodeB<E> child = new NodeB<>(x, null, null);
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

    public void unionB(NodeB<E> p, NodeB<E> q) {
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
            NodeB<E> lastOfOne = p.getClusterReference().getTail();
            lastOfOne.setNext(q.getClusterReference());
            p.getClusterReference().setTail(q.getClusterReference().getTail());

            // update cluster references
            NodeB<E> current = q.getClusterReference();
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
            NodeB<E> lastOfTwo = q.getClusterReference().getTail();
            lastOfTwo.setNext(p.getClusterReference());
            q.getClusterReference().setTail(p.getClusterReference().getTail());

            NodeB<E> current = p.getClusterReference();
            while (current != null) {
                current.setClusterReference(q.getClusterReference());
                current = current.getNext();
            }
        }
        numberOfCluster--;
    }

    public NodeB<E> findB(NodeB<E> p) {
        return p.getClusterReference();  // returns cluster leader
    }

    public E elementB(NodeB<E> p) {
        return p.getElement();
    }
    public int numberOfClustersB() {
        return numberOfCluster;
    }

    public int clusterSizeB(NodeB<E> p) {
        return p.getClusterSize();
    }

    public NodeB<E>[] clusterPositionsB(NodeB<E> p) {
        NodeB<E>[] array = new NodeB[clusterSizeB(p)];
        NodeB<E> current = p.getClusterReference();
        int i = 0;
        while (current != null) {
            array[i++] = current;
            current = current.getNext();
        }
        return array;
    }

    public int[] clusterSizesB() {
        int[] array = new int[numberOfClustersB()];
        int i = 0;
        if (dummyCluster.getClusterNext() == null) return null;

        // collect all cluster sizes
        NodeB<E> current = dummyCluster.getClusterNext();
        while (current != null) {
            array[i++] = clusterSizeB(current);
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
