package org.example.personalpractice;

public class BinNode<T> {
    private BinNode<T> left;  // חיבור לילד השמאלי
    private T value;          // הערך של הצומת
    private BinNode<T> right; // חיבור לילד הימני

    public BinNode(BinNode<T> left, T value, BinNode<T> right) {
        this.left = left;
        this.value = value;
        this.right = right;
    }

    public BinNode(T x) {
        this.left=null;
        this.value=x;
        this.right=null;
    }

    public BinNode<T> getLeft() {
        return left;
    }

    public void setLeft(BinNode<T> left) {
        this.left = left;
    }

    public T getValue() {
        return value;
    }

    public BinNode<T> getRight() {
        return right;
    }

    public void setRight(BinNode<T> right) {
        this.right = right;
    }


    public void setValue(T value) {
        this.value = value;
    }

    // פעולה בוליאנית שבודקת אם יש חוליה ימנית
    public boolean hasRight() {
        return this.right != null;
    }

    // פעולה בוליאנית שבודקת אם יש חוליה שמאלית
    public boolean hasLeft() {
        return this.left != null;
    }

    // פעולה בוליאנית שבודקת אם החוליה היא עלה (אין שמאלי ואין ימני)
    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }
}
