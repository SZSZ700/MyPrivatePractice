package org.example.Q6; // package name for this class
import org.example.Node; // Import the Node class from the org.example package
import java.util.function.*;

public class CharList {
    // private field that points to the head of the character linked list
    private Node<Character> head;

    // private field that points to the tail of the character linked list
    private final Node<Character> tail;

    // default constructor
    public CharList(){
        this.head = null; // Initialize the head pointer to null (empty list)
        this.tail = null; // Initialize the tail pointer to null (empty list)
    }

    // constructor that receives an existing chain (head node)
    public CharList(Node<Character> chain){
        this.head = chain; // Set this list head to the received chain
        // find the tail of the list
        var til = this.head;
        while (til.getNext() != null){ til = til.getNext(); }
        this.tail = til;
    }

    // method that finds the first node with a given letter starting from a given chain
    private Node<Character> firstAfterChain(Node<Character> chain, char letter){
        var pos = chain; // Create an iterator pointer starting at the given chain head

        while (pos != null){ // Loop while we did not reach the end of the list
            // Check if the current node value equals the target letter
            if (pos.getValue() == letter){
                return pos; // Return the current node because it is the first match
            }

            pos = pos.getNext(); // Move the iterator to the next node
        }

        return null; // Return null if the letter was not found in the chain
    }

    // method that finds the last occurrence node of the given letter in this list
    private Node<Character> last (char letter){
        // Create an iterator pointer starting at the head of this list
        var pos = this.head;
        // Create a pointer that will store the last found node that matches the letter
        Node<Character> wanted = null;
        var found = false;

        while (pos != null){ // Loop while we did not reach the end of the list
            // Check if the current node value equals the target letter
            if (pos.getValue() == letter){
                // Update wanted to point to the current node (so it becomes the latest match)
                wanted = pos;
                if (!found){ found = true; }
            }

            pos = pos.getNext(); // Move the iterator to the next node
        }

        if (found){ return wanted; }
        return null; // Return null if the letter was not found in the chain
    }

    public void swap(char letter){
        // internal lambda function to count the num of occurences
        // of some letter in single linkedlist
        Function<Character, Boolean> only2 = (tav) -> {
            var pos = this.head; // pointer for the head of the list
            var count = 0; // counter - to count the num of occurences of the letter
            // iteration + count
            while (pos != null){ if (pos.getValue() == tav){ count++; } }
            // if the letter shown less than twice in a list return false
            if (count < 2){ return false; }
            return true; // else return true
        };

        // ⚠️ if the letter found less than twice in list -> EXIT
        if (!only2.apply(letter)){ return; }

        // ⚠️ if the head/tail pointers points to null -> EXIT
        if (this.head == null || this.tail == null){ return; }

        // ⚠️ if the letter found in the first Node or in the last node -> EXIT
        if (this.head.getValue() == letter || this.tail.getValue() == letter){ return; }

        // POINTERS:
        // ✅ pointer for the first Node that contains the letter
        Node<Character> first = this.firstAfterChain(this.head, letter);

        // ✅ pointer for the Node before "sec-Node"
        var between = first;
        while (first.getNext() != null  && first.getNext().getValue() != letter){
            between = between.getNext();
        }

        // ✅ pointer for the second Node that contains the letter
        Node<Character> sec = this.last(letter);

        // ✅ pointer for the next node, after the second Node that contains the letter
        Node<Character> next_to_secoc = sec.getNext();

        // ✅ pointer for the tail of the list
        Node<Character> tail_after_next_to_secoc = null;
        while (tail_after_next_to_secoc.getNext() != null){
            tail_after_next_to_secoc = tail_after_next_to_secoc.getNext();
        }

        // 🔁 reconect the original list
        between.setNext(null);
        sec.setNext(this.head);
        tail_after_next_to_secoc.setNext(first);
        this.head = next_to_secoc;
    }
}
