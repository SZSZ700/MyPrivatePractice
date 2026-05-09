package org.example.personalpractice;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unused")
public class BreakCircularList {
    // Head of the list this object works on.
    private Node<Integer> chain;

    // Save the given list head.
    public BreakCircularList(Node<Integer> list) {
        // -- ✅ -- //
        // Save the list head.
        this.chain = list;
        // -- ✅ -- //



        // -- ✅ -- //
        // Finds the node that should be treated as the tail.
        // If the list has a loop, this returns the node whose next pointer starts the loop.
        // list: [start-[1]] -> [2] -> [3] -> [4] -> [5] -> [end-[6]] -> [1]
        // map: {{1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}, {6, 1}}
        Function<Node<Integer>, Node<Integer>> findEnd = (listy) -> {
            this.chain = listy;

            // Empty listy: there is no tail.
            if (this.chain == null) { return null; }

            // One-node circular listy: the only node points to itself.
            if (this.chain.getNext() == this.chain) {
                // Break the self-loop and return that node as the tail.
                this.chain.setNext(null);
                return this.chain;
            }

            // One-node non-circular listy: the only node is already the tail.
            if (this.chain.getNext() == null) { return this.chain; }

            // Start scanning from the head.
            var pos = this.chain;

            // Keys are nodes we already visited.
            // Values keep each node's next pointer, matching the map example above.
            var map = new HashMap<Node<Integer>, Node<Integer>>();

            // Walk through the listy until it ends or until a next pointer points
            // to a node that was already seen.
            while (pos != null) {
                var currentNode = pos;
                var nextNode = pos.getNext();

                // Mark the current node as visited.
                map.put(currentNode, nextNode);

                // If nextNode is already a key, then pos is the node that closes the loop.
                if (map.containsKey(nextNode)){
                    return pos;
                }

                // Move forward.
                pos = pos.getNext();
            }

            // Multi-node non-circular listy: no loop was found.
            return null;
        };
        // -- ✅ -- //



        // -- ✅ -- //
        // Find the tail or the node that closes the loop
        // and end the loop by making it point to null.
        Consumer<Node<Integer>> endLoop = (listy) -> {
            // Find the tail or the node that closes the loop.
            var tail = findEnd.apply(this.chain);
            // If a tail was found, make sure it does not point back into the list.
            if (tail != null) { tail.setNext(null); }
        };
        // -- ✅ -- //



        // -- ✅ -- //
        endLoop.accept(this.chain);
        // -- ✅ -- //
    }
}
