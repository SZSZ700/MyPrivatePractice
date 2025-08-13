package org.example;

public class Domino {
    private int left;
    private int right;

    public Domino(int left, int right) {
        this.left = left;
        this.right = right;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "Domino{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }

    public void rotate(){
        int temp = this.left;
        this.left = this.right;
        this.right = temp;
    }


}
