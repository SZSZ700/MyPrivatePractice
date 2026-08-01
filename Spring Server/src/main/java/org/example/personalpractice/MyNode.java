package org.example.personalpractice;

public class MyNode {
    private int value;
    private int howManyBig;
    private MyNode next;

    public MyNode(int val) {
        this.value = val;
        this.howManyBig = 0;
        this.next = null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getHowManyBig() {
        return howManyBig;
    }

    public void setHowManyBig(int howManyBig) {
        this.howManyBig = howManyBig;
    }

    public MyNode getNext() {
        return next;
    }

    public void setNext(MyNode next) {
        this.next = next;
    }
}
