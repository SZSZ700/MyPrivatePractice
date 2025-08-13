package org.example;

public class EmergencyList {
    Node<Call> calls;

    public EmergencyList(Node<Call> calls) {
        this.calls = calls;
    }

    public Node<Call> getCalls() {
        return calls;
    }

    public void setCalls(Node<Call> calls) {
        this.calls = calls;
    }

    @Override
    public String toString() {
        return "EmergencyList{" +
                "calls=" + calls +
                '}';
    }
}
