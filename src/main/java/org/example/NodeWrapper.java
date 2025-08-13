package org.example;

public class NodeWrapper<T> {
    private Node<T> head;

    public NodeWrapper(Node<T> head) {
        this.head = head;
    }

    public Node<T> getHead() {
        return head;
    }

    public void setHead(Node<T> head) {
        this.head = head;
    }
}

