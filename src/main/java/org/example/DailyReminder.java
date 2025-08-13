package org.example;

public class DailyReminder {
    Node<Reminder> chain;//רשימת תזכורות

    public DailyReminder(Node<Reminder> chain) {
        this.chain = chain;
    }

    public Node<Reminder> getChain() {
        return chain;
    }

    public void setChain(Node<Reminder> chain) {
        this.chain = chain;
    }

    @Override
    public String toString() {
        return "DailyReminder{" +
                "chain=" + chain +
                '}';
    }
}
