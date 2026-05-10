package org.example.personalpractice;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unused")
public class BreakMapOfTreesOfCircularLists {
    // internal map of trees of circular lists
    private final HashMap<Integer,BinNode<Node<Integer>>> map;

    // constructor
    public BreakMapOfTreesOfCircularLists(HashMap<Integer,BinNode<Node<Integer>>> MapOftreeOfCrclists) {
        this.map = MapOftreeOfCrclists;
    }

    public void fixMap() {
        // -- ✅ -- //
        // O(n) time | O(n) space
        // Finds the node that should be treated as the tail.
        // If the list has a loop, this returns the node whose next pointer starts the loop.
        // list: [start-[1]] -> [2] -> [3] -> [4] -> [5] -> [end-[6]] -> [1]
        // map: {{1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}, {6, 1}}
        Function<Node<Integer>, Node<Integer>> findEnd = (listy) -> {
            // if listy is null, return null.
            if (listy == null) { return null; }

            // pointer to the list head.
            var pos = listy;

            // One-node circular listy: the only node points to itself.
            if (pos.getNext() == pos) {
                // Break the self-loop and return that node as the tail.
                pos.setNext(null);
                return pos;
            }

            // One-node non-circular listy: the only node is already the tail.
            if (pos.getNext() == null) { return pos; }

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

                // If nextNode is already a key, then current is the node that closes the loop.
                if (map.containsKey(nextNode)){ return pos; }

                // Move forward.
                pos = pos.getNext();
            }

            // Multi-node non-circular listy: no loop was found.
            return null;
        };
        // -- ✅ -- //

        // -- ✅ -- //
        // O(n) time | O(1) space
        // Find the tail or the node that closes the loop
        // and end the loop by making it point to null.
        Consumer<Node<Integer>> endLoop = (listy) -> {
            // Find the tail or the node that closes the loop.
            var tail = findEnd.apply(listy);
            // If a tail was found, make sure it does not point back into the list.
            if (tail != null) { tail.setNext(null); }
        };
        // -- ✅ -- //

        // -- ✅ -- //
        // O(n)^2 time | O(n) space
        Consumer<BinNode<Node<Integer>>> endAllLoops = (rootOfTree) -> {
            // If the root is null, there is nothing to do.
            if (rootOfTree == null) { return; }

            // Create a queue to enqueue the children of the root.
            var q = new LinkedList<BinNode<Node<Integer>>>();
            // Enqueue the root.
            q.offer(rootOfTree);

            // While the queue is not empty...
            while (!q.isEmpty()){
                // poll() removes the head(BinNode<Node<Integer>>)
                // of the queue and returns it.
                var current = q.poll();
                // getValue() returns the value(Node<Integer>)
                // of the head of the queue.
                var list = current.getValue();

                // call endLoop() with the current list as argument
                // to end the cercularity of the current list.
                endLoop.accept(list);

                // Enqueue the children of the current list.
                // If the child is null, it is not enqueued.

                // enqueue the left child
                if (current.getLeft() != null) { q.offer(current.getLeft()); }

                // enqueue the right child
                if (current.getRight() != null) { q.offer(current.getRight()); }
            }
        };
        // -- ✅ -- //

        // -- ✅ -- //
        // O(n)^3 time | O(1) space
        Consumer<HashMap<Integer,BinNode<Node<Integer>>>> endAllLoopsInMap = (map) -> {
            // If the map is null, there is nothing to do.
            if (map == null) { return; }

            // If the map is empty, there is nothing to do.
            if (map.isEmpty()) { return; }

            // Iterate over the map.
            for (var entry : map.entrySet()) {
                // Get the key and the value.
                // The key is the id of the tree.
                var id = entry.getKey();
                // The value is the root of the tree.
                var tree = entry.getValue();
                // Call endAllLoops() with the tree as argument.
                endAllLoops.accept(tree);
            }
        };
        // -- ✅ -- //

        // -- ✅ -- //
        endAllLoopsInMap.accept(this.map);
        // -- ✅ -- //
    }
}
