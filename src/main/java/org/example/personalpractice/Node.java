package org.example;

public class Node<T> {
    private T value;
    private Node<T> next;

    public Node (T x){
        this.value = x;
        this.next = null;
    }

    public Node (T x, Node<T>next){
        this.value = x;
        this.next = next;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "Node [value=" + value + ", next=" + next + "]";
    }
    public boolean hasNext() {
        return this.next != null;
    }
}
