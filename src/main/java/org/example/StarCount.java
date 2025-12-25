package org.example;

public class StarCount {
    private Node<Integer> number;

    public StarCount(Node<Integer> number) {
        this.number = number;
    }

    public Node<Integer> getNumber() {
        return number;
    }

    public void setNumber(Node<Integer> number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "StarCount{" +
                "number=" + number +
                '}';
    }

    private Node<Integer> breakNumberAndReturnList(int num){
        if (num == 0){ return new Node<>(0); }

        Node<Integer> chain = null; // head of the new list (empty list)
        Node<Integer> tail = null; // tail of the new list

        var temp = num; // copy of the number

        while (temp > 0){
            var currentDigit = temp % 10;
            // create new Node that represent the current Digit (from the end of the number)
            var toAdd = new Node<Integer>(currentDigit);

            // add the new node to the list
            if (chain == null){
                // chain points to the new Node
                chain = toAdd;
                tail = toAdd; // tail will point to the end of the list
            }else{
                // add the new Node to the start of the list
                toAdd.setNext(chain);
                chain = toAdd;
            }

            temp /= 10; // remove last digit from the number
        }

        // return array that represent the number the function recive as parameter
        return chain;
    }

    public void fixNumber(){
        // if the chain == null,  return => nothing to do, no number
        if (this.number == null){ return; }

        // pointers for the TEMPORARY LIST:
        // pointer to the head of the new list
        Node<Integer> newey = null;
        // pointer to the tail of the new list
        Node<Integer> tail = null;

        // pointer for iteration on the current list
        var pos = this.number;

        // iteration
        while (pos != null){
            // keep current number
            var currentNumber = pos.getValue();
            // pointer for the head of the new list, that represent the current number
            var pointerToSmallChain = this.breakNumberAndReturnList(currentNumber);

            // if the new temp list is empty
            if (newey == null){
                // the head of the temp list will points to the head
                // of the new created list(that represent the current number)
                newey = pointerToSmallChain;
                // points the tail pointer to the new created list
                tail = newey;
            } else {
                // else if the temp list is not empty
                // add new list(that represent the current number)
                // to the end of the temp list
                tail.setNext(pointerToSmallChain);
            }

            // move the tail pointer of the temp list to the end of the new created list
            while (tail.getNext() != null){
                tail = tail.getNext();
            }

            // move to the original list next Node
            pos = pos.getNext();
        }

        // change the head of the list to points to the new temp list
        this.number = newey;
    }

    // Convert the current single linked list (this.number) into a doubly linked list and return its head
    private BinNode<Integer> convert_THIS_SingleListToDoubly() {
        // Create a pointer for iterating the original single linked list
        var pos = this.number;

        // Create a pointer to the head of the new doubly linked list
        BinNode<Integer> doublychain = null;

        // Create a pointer to the tail of the new doubly linked list
        BinNode<Integer> tail = null;

        // Iterate over the original list
        while (pos != null) {
            // Create a new BinNode that copies the current digit
            var toAdd = new BinNode<Integer>(pos.getValue());

            // If this is the first node in the doubly list
            if (doublychain == null) {
                // Set head to the first created BinNode
                doublychain = toAdd;

                // Set tail to the same first node
                tail = toAdd;
            } else {
                // Connect current tail to the new node (to the right)
                tail.setRight(toAdd);

                // Connect new node back to the tail (to the left)
                toAdd.setLeft(tail);

                // Move tail forward to the new last node
                tail = tail.getRight();
            }

            // Move to the next node in the original single list
            pos = pos.getNext();
        }

        // Return the head of the new doubly linked list (could be null if original list was null)
        return doublychain;
    }

    // Return the tail (last node) of a given doubly linked list
    private BinNode<Integer> returnTailOfDlist(BinNode<Integer> chain) {
        // Create a pointer that will move until it reaches the end
        var tail = chain;

        // Move right while there is a next node
        while (tail.getRight() != null) {
            // Advance the tail pointer to the right
            tail = tail.getRight();
        }

        // Return the last node of the list
        return tail;
    }

    // Convert a doubly linked list back into this.number (single linked list)
    private void convertDoublyListTo_THIS_Single(BinNode<Integer> doublychain) {
        // Create a pointer to iterate the doubly linked list from head to tail
        var position = doublychain;

        // Clear the current original list (we rebuild it from scratch)
        this.number = null;

        // Create a tail pointer for the rebuilt single list
        Node<Integer> newTail = null;

        // Iterate over the doubly list
        while (position != null) {
            // Read the current digit from the doubly node
            var currentnumber = position.getValue();

            // Create a new single Node that stores the digit
            var toAdd = new Node<Integer>(currentnumber);

            // If this is the first node in the rebuilt single list
            if (this.number == null) {
                // Set head of the rebuilt list
                this.number = toAdd;

                // Set tail of the rebuilt list
                newTail = toAdd;
            } else {
                // Attach the new node to the end of the rebuilt list
                newTail.setNext(toAdd);

                // Move tail pointer forward
                newTail = newTail.getNext();
            }

            // Move to the next node in the doubly list (right direction)
            position = position.getRight();
        }
    }

    // Add 1 to the big number represented by this.number (digits in nodes)
    public void addOne() {
        // LIFE-OR-DEATH FIX: if the list is null, treat it as 0 and make it 1 (prevents NPE later)
        if (this.number == null) {
            // Create a single node with value 1
            this.number = new Node<>(1);

            // Stop here because the operation is complete
            return;
        }

        // Convert the original single linked list to a doubly linked list
        var doublychain = this.convert_THIS_SingleListToDoubly();

        // Find the tail (last node) of the doubly linked list
        var dail = returnTailOfDlist(doublychain);

        // This flag indicates whether we still have a carry because we saw a 9
        var detectedLastNumberWasNine = false;

        // Iterate backward from tail to head
        while (dail != null) {
            // Always read the digit fresh from the current node
            var currentdigit = dail.getValue();

            // If we already have a carry from the previous digit
            if (detectedLastNumberWasNine) {
                // If current digit is not 9, we can just increment and finish
                if (currentdigit != 9) {
                    // Increment the current digit
                    dail.setValue(dail.getValue() + 1);

                    // Stop because carry is resolved
                    break;
                } else {
                    // If current digit is 9, it becomes 0 and carry continues
                    dail.setValue(0);

                    // Keep carry as true
                    detectedLastNumberWasNine = true;

                    // If we are at the head and it became 0, we must add a new head '1'
                    if (dail.getLeft() == null) {
                        // Create a new head node with value 1
                        dail.setLeft(new BinNode<>(1));

                        // Connect new head to the old head
                        dail.getLeft().setRight(dail);

                        // Update the head pointer of the doubly list
                        doublychain = dail.getLeft();

                        // Stop because the number is now fixed (e.g., 999 -> 1000)
                        break;
                    }

                    // Move left to continue processing carry
                    dail = dail.getLeft();

                    // CRITICAL: skip the next block to avoid double-processing in the same iteration
                    continue;
                }
            }

            // If current digit is 9, turn it into 0 and set carry
            if (currentdigit == 9) {
                // Set current digit to 0
                dail.setValue(0);

                // Set carry to true
                detectedLastNumberWasNine = true;

                // If we are at the head, add a new head '1'
                if (dail.getLeft() == null) {
                    // Create a new head node with value 1
                    dail.setLeft(new BinNode<>(1));

                    // Connect new head to the old head
                    dail.getLeft().setRight(dail);

                    // Update the head pointer of the doubly list
                    doublychain = dail.getLeft();

                    // Stop because the number is now fixed (e.g., 9 -> 10)
                    break;
                }

                // Move left to continue carry
                dail = dail.getLeft();
            } else {
                // If current digit is not 9, just increment it and finish
                dail.setValue(dail.getValue() + 1);

                // Stop because no carry remains
                break;
            }
        }

        // Convert the updated doubly linked list back into the original single linked list
        convertDoublyListTo_THIS_Single(doublychain);
    }

}
