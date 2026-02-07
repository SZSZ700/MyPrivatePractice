package org.example.Q6; // Package declaration

import org.example.Node; // Import Node class

import java.util.function.BiFunction; // Import BiFunction for (Node, Character) -> Node
import java.util.function.Consumer; // Import Consumer for letter -> void
import java.util.function.Function; // Import Function for letter -> Node

public class CharListSecTry { // Class definition

    private Node<Character> head; // Reference to the first node in the list
    private Node<Character> tail; // Reference to the last node in the list

    public CharListSecTry() { // Default constructor
        this.head = null; // Initialize head to null
        this.tail = null; // Initialize tail to null
    }

    public Node<Character> getHead() { // Getter for head
        return head; // Return head reference
    }

    public void setHead(Node<Character> head) { // Setter for head
        this.head = head; // Assign head reference
    }

    public Node<Character> getTail() { // Getter for tail
        return tail; // Return tail reference
    }

    public void setTail(Node<Character> tail) { // Setter for tail
        this.tail = tail; // Assign tail reference
    }

    @Override
    public String toString() { // Convert list to string
        if (this.head == null) return "no list"; // If list is empty return message

        StringBuilder str = new StringBuilder(); // Builder for output string
        Node<Character> pos = this.head; // Start from head

        while (pos != null) { // Traverse until end
            str.append(pos.getValue()); // Append current character
            pos = pos.getNext(); // Move to next node
        }

        return str.toString(); // Return built string
    }

    final Consumer<Character> swap = (letter) -> { // Lambda that rearranges list based on 'letter'

        if (this.head == null) return; // Critical guard: empty list
        if (this.tail == null) return; // Critical guard: tail missing
        if (this.head == this.tail) return; // Critical guard: single node list

        if (this.head.getValue() != null && this.tail.getValue() != null // Check both values are not null
                && this.head.getValue() == letter && this.tail.getValue() == letter) return; // If both ends are 'letter', do nothing

        BiFunction<Node<Character>, Character, Node<Character>> firstAfterChain = (chain, Letter) -> { // firstAfterChain lambda
            if (chain == null) return null; // If chain is null, return null

            var pos = chain.getNext(); // Start from the node after chain

            while (pos != null) { // Traverse forward
                if (pos.getValue() != null && pos.getValue() == Letter) { // If this node matches the letter
                    return pos; // Return the first matching node after chain
                }
                pos = pos.getNext(); // Move to next node
            }

            return null; // If not found, return null
        };

        Function<Character, Node<Character>> Last = (Letter) -> { // Last lambda using firstAfterChain
            if (this.head == null) return null; // If list is empty, return null

            Node<Character> prevFound = null; // Stores last found node

            if (this.head.getValue() != null && this.head.getValue() == Letter) { // Check if head itself matches
                prevFound = this.head; // Save head as candidate
            }

            var currFound = firstAfterChain.apply(this.head, Letter); // Find first match after head

            while (currFound != null) { // While more matches exist
                prevFound = currFound; // Save current match
                currFound = firstAfterChain.apply(currFound, Letter); // Find next match after current
            }

            return prevFound; // Return last match (or null)
        };

        var first = firstAfterChain.apply(this.head, letter); // Find first occurrence after head
        var last = Last.apply(letter); // Find last occurrence in the list

        if (last == null) return; // Critical guard: letter not found anywhere
        if (first == null) return; // Critical guard: no occurrence after head (avoids breaking the list)
        if (last == this.tail) return; // Critical guard: afterLast would be null

        var afterLast = last.getNext(); // Save node after the last occurrence
        if (afterLast == null) return; // Extra safety: if null, stop (prevents head=null corruption)

        // Start from head to find the node BEFORE 'first'
        var beforeFirst = this.head;

        // Traverse until next is 'first'
        while (beforeFirst.getNext() != null && beforeFirst.getNext() != first) {
            beforeFirst = beforeFirst.getNext(); // Move forward
        }

        if (beforeFirst.getNext() != first) return; // Safety: if 'first' not reachable, stop

        var oldHead = this.head; // Save old head
        var oldTail = this.tail; // Save old tail

        beforeFirst.setNext(null); // Cut: end the prefix part so it becomes the new tail later
        last.setNext(oldHead); // Connect: last -> oldHead (attach prefix after the last)
        oldTail.setNext(first); // Connect: oldTail -> first (attach middle block after the suffix)

        this.head = afterLast; // Set new head to the suffix (after last)
        this.tail = beforeFirst; // update tail to the node whose next is now null
    };
}
