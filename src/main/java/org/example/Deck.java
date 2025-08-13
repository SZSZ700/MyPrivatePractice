package org.example;

public class Deck {
    private Node<Domino> head;
    private int current;

    public Deck(){
        this.head = null;
        this.current = 0;
    }

    public Node<Domino> getHead() {
        return head;
    }

    public void setHead(Node<Domino> head) {
        this.head = head;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    //רשימת אבני דומינו נקראת "מסודרת היטב" אם ערך של מספרי צד ימני של כל אבן (פרט לאבן האחרונה
    //ברשימה) זהה לערך של מספרי צד שמאלי של האבן הבאה.
    public boolean orderdWell(){
        if (this.head == null || this.current == 1){
            return true;
        }

        Node<Domino> pos = this.head;

        while (pos.getNext() != null){
            int currentRight = pos.getValue().getRight();
            int currentLeft = pos.getNext().getValue().getLeft();
            if (currentRight != currentLeft){
                return false;
            }
            pos = pos.getNext();
        }

        return true;
    }

    public boolean canAdd(Domino d1){
        //חולייה להוספה
        Node<Domino> toAdd = new Node<>(d1);

        //הוספה בהתחלה
        if (d1.getRight() == this.head.getValue().getLeft()){
            toAdd.setNext(this.head);
            this.head = toAdd;
            return true;
        }

        //הוספה באמצע
        Node<Domino> pos = this.head;
        while (pos.getNext() != null){
            if(d1.getRight() == pos.getNext().getValue().getLeft()){
                if (d1.getLeft() == pos.getValue().getRight()){
                    toAdd.setNext(pos.getNext());
                    pos.setNext(toAdd);
                    return true;
                }
            }
            pos = pos.getNext();
        }

        //הוספה בסוף
        if (d1.getLeft() == pos.getValue().getRight()){
            pos.setNext(toAdd);
            return true;
        }

        //לא ניתן להוסיף
        return false;
    }

    @Override
    public String toString() {
        return "Deck{" +
                "head=" + head +
                '}';
    }
}
