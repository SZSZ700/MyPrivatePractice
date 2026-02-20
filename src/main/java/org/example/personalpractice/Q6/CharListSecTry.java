package org.example.personalpractice.Q6; // Package declaration
import org.example.personalpractice.Node; // Import Node class
import java.util.function.BiFunction; // Import BiFunction for (Node, Character) -> Node
import java.util.function.Consumer; // Import Consumer for letter -> void
import java.util.function.Function; // Import Function for letter -> Node

@SuppressWarnings("unused")
public class CharListSecTry { // Class definition

    private Node<Character> head; // Reference to the first node in the list
    private Node<Character> tail; // Reference to the last node in the list

    @SuppressWarnings("unused")
    public CharListSecTry() { // Default constructor
        this.head = null; // Initialize head to null
        this.tail = null; // Initialize tail to null
    }

    // Getter for head - Return head reference
    @SuppressWarnings("unused")
    public Node<Character> getHead() { return this.head; }

    // Setter for head - Assign head reference
    @SuppressWarnings("unused")
    public void setHead(Node<Character> head) { this.head = head; }

    // Getter for tail - // Return tail reference
    @SuppressWarnings("unused")
    public Node<Character> getTail() { return this.tail; }

    // Setter for tail - Assign tail reference
    @SuppressWarnings("unused")
    public void setTail(Node<Character> tail) { this.tail = tail; }

    @Override
    public String toString() { // Convert list to string
        if (this.head == null) return "no list"; // If list is empty return message

        var str = new StringBuilder(); // Builder for output string
        var pos = this.head; // Start from head

        while (pos != null) { // Traverse until end
            str.append(pos.getValue()); // Append current character
            pos = pos.getNext(); // Move to next node
        }

        return str.toString(); // Return built string
    }

    // Lambda that rearranges list based on 'letter'
    private final Consumer<Character> swap = (letter) -> {
        // Critical guard: empty list
        if (this.head == null) return;
        // Critical guard: tail missing
        if (this.tail == null) return;
        // Critical guard: single node list
        if (this.head == this.tail) return;

        // Check both values are not null
        // If both ends are 'letter', do nothing
        if (this.head.getValue() != null && this.tail.getValue() != null
                && this.head.getValue() == letter && this.tail.getValue() == letter) return;

        // firstAfterChain lambda
        BiFunction<Node<Character>, Character, Node<Character>> firstAfterChain = (chain, Letter) -> {
            if (chain == null) return null; // If chain is null, return null

            var pos = chain.getNext(); // Start from the node after chain

            while (pos != null) { // Traverse forward
                // If this node matches the letter
                // Return the first matching node after chain
                if (pos.getValue() != null && pos.getValue() == Letter) { return pos; }
                // Move to next node
                pos = pos.getNext();
            }

            return null; // If not found, return null
        };

        // Last lambda using firstAfterChain
        Function<Character, Node<Character>> Last = (Letter) -> {
            // If list is empty, return null
            if (this.head == null) return null;

            Node<Character> prevFound = null; // Stores last found node

            // Check if head itself matches
            if (this.head.getValue() != null && this.head.getValue() == Letter) {
                prevFound = this.head; // Save head as candidate
            }

            // Find first match after head
            var currFound = firstAfterChain.apply(this.head, Letter);

            // While more matches exist
            while (currFound != null) {
                // Save current match
                if (currFound.getValue() == letter) { prevFound = currFound; }
                // Find next match after current
                currFound = firstAfterChain.apply(currFound, Letter);
            }

            return prevFound; // Return last match (or null)
        };

        // Find first occurrence after head
        var first = firstAfterChain.apply(this.head, letter);
        // Find last occurrence in the list
        var last = Last.apply(letter);

        if (last == null) { return; } // letter not found anywhere
        if (first == null) { return; } // no occurrence after head
        if (last == this.tail) { return; } // afterLast would be null
        var afterLast = last.getNext(); // Save node after the last occurrence
        if (afterLast == null) return; // if null, stop (prevents head=null corruption)

        // Start from head to find the node BEFORE 'first'
        var beforeFirst = this.head;

        // Traverse until next is 'first'
        while (beforeFirst.getNext() != null && beforeFirst.getNext() != first) {
            beforeFirst = beforeFirst.getNext(); // Move forward
        }

        // if 'first' not reachable, stop
        if (beforeFirst.getNext() != first) return;

        var oldHead = this.head; // Save old head
        var oldTail = this.tail; // Save old tail

        // Cut: end the prefix part so it becomes the new tail later
        beforeFirst.setNext(null);
        // Connect: last -> oldHead (attach prefix after the last)
        last.setNext(oldHead);
        // Connect: oldTail -> first (attach middle block after the suffix)
        oldTail.setNext(first);

        this.head = afterLast; // Set new head to the suffix (after last)
        this.tail = beforeFirst; // update tail to the node whose next is now null
    };

    // invoke dynamiclly this.swap()
    public void swapy(char letter){ this.swap.accept(letter); }
}
