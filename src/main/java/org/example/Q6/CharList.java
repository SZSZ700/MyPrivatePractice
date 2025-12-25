package org.example.Q6; // package name for this class
import org.checkerframework.checker.units.qual.N;
import org.example.Node; // Import the Node class from the org.example package

public class CharList {
    // private field that points to the head of the character linked list
    private Node<Character> head;

    // private field that points to the tail of the character linked list
    private Node<Character> tail;

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
    public Node<Character> firstAfterChain(Node<Character> chain, char letter){
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
    public Node<Character> last (char letter){
        // Create an iterator pointer starting at the head of this list
        var pos = this.head;
        // Create a pointer that will store the last found node that matches the letter
        Node<Character> wanted = null;

        while (pos != null){ // Loop while we did not reach the end of the list
            // Check if the current node value equals the target letter
            if (pos.getValue() == letter){
                // Update wanted to point to the current node (so it becomes the latest match)
                wanted = pos;
            }

            pos = pos.getNext(); // Move the iterator to the next node
        }

        return null; // Return null if the letter was not found in the chain
    }

    public void swap(char letter){
        if (this.head == null || this.tail == null){
            return;
        }

        if (this.head.getValue() == letter || this.tail.getValue() == letter){
            return;
        }

        var pos = this.head;
        Node<Character> firstoc = null;
        Node<Character> secoc = null;
        Node<Character> next_to_secoc = null;
        Node<Character> tail_after_next_to_secoc = null;
        var canComplete = false;

        while (pos != null){
            if (pos.getNext() != null){
                if (pos.getNext().getValue() == letter){

                    //⚠️////⚠️////⚠️////⚠️//
                    firstoc = pos.getNext();
                    //⚠️////⚠️////⚠️////⚠️//


                    //⚠️////⚠️////⚠️////⚠️//
                    pos.setNext(null);
                    pos = firstoc;
                    //⚠️////⚠️////⚠️////⚠️//

                    while (pos != null){
                        if (pos.getNext() != null){
                            if (pos.getNext().getValue() == letter){

                                //⚠️////⚠️////⚠️////⚠️//
                                secoc = pos.getNext();
                                //⚠️////⚠️////⚠️////⚠️//

                                //⚠️////⚠️////⚠️////⚠️////⚠️////⚠️////⚠️//
                                // the new head of the original list
                                next_to_secoc = pos.getNext().getNext();
                                //⚠️////⚠️////⚠️////⚠️////⚠️////⚠️////⚠️//


                                //⚠️////⚠️////⚠️////⚠️////⚠️////⚠️////⚠️//
                                tail_after_next_to_secoc = next_to_secoc;
                                //⚠️////⚠️////⚠️////⚠️////⚠️////⚠️////⚠️//


                                while (tail_after_next_to_secoc.getNext() != null){
                                    tail_after_next_to_secoc =
                                            tail_after_next_to_secoc.getNext();
                                }

                                //❗🛑❗////❗🛑❗//
                                canComplete = true;
                                break;
                                //❗🛑❗////❗🛑❗//
                            }
                        }

                        pos = pos.getNext();
                    } // end of internal while loop
                }
            } // end of external if statement

            //❗🛑❗////❗🛑❗//
            if (canComplete){
                break;
            }
            //❗🛑❗////❗🛑❗//

            pos = pos.getNext();
        } // end of external while loop

        secoc.setNext(this.head);
        this.head = next_to_secoc;
        tail_after_next_to_secoc.setNext(firstoc);
    }
}
