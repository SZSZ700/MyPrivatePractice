package org.example.personalpractice.T2025B;
import org.example.personalpractice.Node;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unused")
public class TwentyTwentyFiveTest {
    // first question
    @SuppressWarnings("unused")
    private final Runnable one = () -> {
        // assist function to calculate the size of some number
        Function<Integer, Integer> assistNumSize = (num) -> {
            // if num smaller than 10 , size of number is one -> return 1
            if (num >= 0 && num <= 9) {
                return 1;
            }
            // var for the length of the number
            var len = 0;

            // iteration on the number
            while (num > 0) {
                // increment the length var by 1
                len++;
                // shortend the number by one digit
                num /= 10;
            }

            // return the length of the number
            return len;
        };

        // assist function to return the first digit of some number
        Function<Integer, Integer> assistFirstDigit = (num) -> {
            // initialize the var that will contain the first digit with zero
            var first = 0;

            // iteration on the number
            while (num > 0) {
                // keep the last digit of the number
                // in the end of the iteartion it will have the first digit of course
                first = num % 10;
                // shortend the number by one digit
                num /= 10;
            }

            // return the first digit of the number
            return first;
        };

        // main function to check if the stack is valid
        BiFunction<Stack<Integer>, Integer, Boolean> isProperK = (stk, num) -> {
            // create new counters array
            var counters = new AtomicIntegerArray(stk.size());
            // create variable that represent an index in the counters array
            var index = new AtomicInteger(0);

            // iteration on the original stack
            // the diraction of the iteration is not important
            stk.forEach(
                    (n) -> {
                        // calculate the size of the current number in the stack
                        var size = assistNumSize.apply(n);
                        // keep the index of the counters array in other variable
                        // for concurency issue
                        var INDEX = index.get();

                        // if the size of the current number is 1
                        // check if the number equals to num
                        if (size == 1 && Objects.equals(n, num)) {
                            // if it is equals => counters[index]++;
                            counters.incrementAndGet(INDEX);
                        }
                        // else if the size of the current number is bigger than 1
                        // check if the first digit of the current number equals to num
                        else if (size > 1 && Objects.equals(assistFirstDigit.apply(n), num)) {
                            // if it is equals => counters[index]++;
                            counters.incrementAndGet(INDEX);
                        }
                        // else the number is not important
                        else {
                            // so set the value in the current index in the array
                            // to be zero => counters[index] = 0;
                            counters.set(INDEX, 0);
                        }

                        // increment the index by one =>i++;
                        index.incrementAndGet();
                    }
            );

            // index for the itartion
            var i = 0;

            // iterate on the counters array till reach the index
            // that the value in it equals to
            while (i < counters.length() && counters.get(i) == 0) { i++; }

            // if it reach the end it is mean that num even don't exist
            // in the original stack
            if (i == counters.length()) { return false; }

            // keep iterate on the array while the values equals to 1
            // it stop if the value at some index equals to zero
            while (i < counters.length() && counters.get(i) == 1) { i++; }

            // the final iteration - from now
            // if the value 1 will be found again in the array it's mean that the stack
            // isn't valid, and the function will return false
            while (i < counters.length()) {
                // if 1 found return false
                if (counters.get(i) == 1) { return false; }
            }

            // return true the stack is valid
            return true;
        };

        BiConsumer<Stack<Integer>, Integer> fixIt = (stk, num) -> {
            // if the stack is not valid
            if (!isProperK.apply(stk, num)) {
                // create same-stack
                var same = new Stack<Integer>();
                // create notsame-stack
                var notsame = new Stack<Integer>();

                stk.forEach(
                        (n) -> {
                            // calculate the size of the current number in the stack
                            var size = assistNumSize.apply(n);

                            // if the size of the current number is 1
                            // check if the number equals to num
                            if (size == 1 && Objects.equals(n, num)) {
                                // add the value to the same-stack
                                same.push(n);
                            }
                            // else if the size of the current number is bigger than 1
                            // check if the first digit of the current number equals to num
                            else if (size > 1 && Objects.equals(assistFirstDigit.apply(n), num)) {
                                // add the value to the notsame-stack
                                notsame.push(n);
                            }
                        }
                );

                // emtying the original stack
                // and fill it in way it will become valid
                while (!stk.empty()) { stk.pop(); }

                // emptying the notsame-stack to the original stack
                while (!notsame.empty()) { stk.push(notsame.pop()); }

                // emptying the same-stack to the original stack
                while (!same.empty()) { stk.push(same.pop()); }
            }
        };
    };

    private final Runnable four = () -> {
        // Define a simple data class that represents a client/group waiting to be seated.
        class Client {
            // Store the client name (for identification / debugging).
            private String name;

            // Store how many diners are in this client group.
            private int diners;

            // Provide a no-arg constructor so frameworks (like Spring) can instantiate it easily.
            public Client() { }

            // Initialize a client with name and number of diners.
            public Client(String name, int diners) {
                // Save the given name.
                this.name = name;
                // Save the given group size.
                this.diners = diners;
            }

            // Return the client's name.
            public String getName() { return this.name; }

            // Update the client's name.
            public void setName(String name) { this.name = name; }

            // Return the number of diners in the group.
            public int getDiners() { return this.diners; }

            // Update the number of diners in the group.
            public void setDiners(int diners) { this.diners = diners; }

            // Convert this object into a readable string for debugging/logging.
            @Override
            public String toString() {
                // Build a descriptive string that includes all fields.
                return "Client{" +
                        // Include the name value with quotes.
                        "name='" + this.name + '\'' +
                        // Include the diners count.
                        ", diners=" + this.diners +
                        // Close the object format.
                        '}';
            }
        }

        // Define a simple data class that represents a restaurant table.
        class Table {

            // Store the unique table number (identifier).
            private int num;

            // Store how many diners the table can hold (2/4/8 by your model).
            private int places;

            // Store how many chairs are currently free (changes after seating).
            int free;

            // Provide a no-arg constructor so frameworks (like Spring) can instantiate it easily.
            public Table() { }

            // Initialize a table with all of its fields explicitly.
            public Table(int num, int places, int free) {
                // Save the given table number.
                this.num = num;
                // Save the given capacity.
                this.places = places;
                // Save the given free seats count.
                this.free = free;
            }

            // Return the table number.
            public int getNum() { return this.num; }

            // Update the table number.
            public void setNum(int num) { this.num = num; }

            // Return the table capacity.
            public int getPlaces() { return this.places; }

            // Update the table capacity.
            public void setPlaces(int places) { this.places = places; }

            // Return how many seats are currently free.
            public int getFree() { return this.free; }

            // Update how many seats are currently free.
            public void setFree(int free) { this.free = free; }

            // Convert this object into a readable string for debugging/logging.
            @Override
            public String toString() {
                // Build a descriptive string that includes all fields.
                return "Table{" +
                        // Include table number.
                        "num=" + this.num +
                        // Include capacity.
                        ", places=" + this.places +
                        // Include free seats.
                        ", free=" + this.free +
                        // Close the object format.
                        '}';
            }
        }

        // Define the main restaurant logic: manage tables + waiting clients + seating algorithm.
        //noinspection FieldMayBeFinal
        class Restaurant {
            // Keep a FIFO queue of clients waiting to be seated.
            private Queue<Client> clients;

            // Keep a linked list of all tables (small + medium + large) using your Node class.
            private Node<Table> tables;

            // Keep a global counter to assign unique table numbers across all created tables.
            private static int num = 0;

            // Build a restaurant with a specific number of small/medium/large tables.
            public Restaurant(int small, int medium, int large) {
                // Define a helper lambda that creates a linked list of tables of a given size.
                Function<Integer, Node<Table>[]> createTbl = (size) -> {
                    // Point to the head of the new linked list (starts empty).
                    Node<Table> chain = null;

                    // Point to the tail of the new linked list (used for O(1) append).
                    Node<Table> tail = null;

                    // Use an index variable for the loop.
                    var i = 0;

                    // Create "size" tables.
                    while (i < size) {

                        // Create a fresh Table instance directly.
                        var tableToAdd = new Table();

                        // Assign the next unique table number.
                        tableToAdd.setNum(++Restaurant.num);

                        // Set capacity to match the "size" parameter (your current model).
                        tableToAdd.setPlaces(size);

                        // Start with all seats free at the beginning.
                        tableToAdd.setFree(size);

                        // Wrap the table inside a Node to insert into the linked list.
                        var nodeToAdd = new Node<>(tableToAdd);

                        // If the list is empty, this node becomes both head and tail.
                        if (chain == null) {
                            // Set head.
                            chain = nodeToAdd;

                            // Set tail.
                            tail = nodeToAdd;
                        } else {
                            // Link current tail to the new node.
                            tail.setNext(nodeToAdd);

                            // Move tail forward.
                            tail = tail.getNext();
                        }

                        // Advance the loop counter.
                        i++;
                    }

                    // Return both head and tail so callers can chain lists efficiently.
                    //noinspection unchecked
                    return new Node[]{chain, tail};
                };

                // Initialize the clients queue as an empty FIFO structure.
                this.clients = new LinkedList<>();

                // Build the small-tables list and get its head and tail.
                var arrayOflistOfSmallTables = createTbl.apply(small);

                // Extract the head of the small list.
                var head1 = arrayOflistOfSmallTables[0];

                // Extract the tail of the small list.
                var tail1 = arrayOflistOfSmallTables[1];

                // Build the medium-tables list and get its head and tail.
                var arrayOflistOfMediumTables = createTbl.apply(medium);

                // Extract the head of the medium list.
                var head2 = arrayOflistOfMediumTables[0];

                // Extract the tail of the medium list.
                var tail2 = arrayOflistOfMediumTables[1];

                // Build the large-tables list and get its head and tail.
                var arrayOflistOfBigTables = createTbl.apply(large);

                // Extract the head of the large list.
                var head3 = arrayOflistOfBigTables[0];

                // Extract the tail of the large list.
                var tail3 = arrayOflistOfBigTables[1];

                // Set the restaurant tables list to start at the small tables.
                this.tables = head1;

                // If there are no small tables, start from medium tables.
                if (this.tables == null) {
                    this.tables = head2;
                }

                // If there are no small or medium tables, start from big tables.
                if (this.tables == null) {
                    this.tables = head3;
                }

                // Connect small list to medium list if small tables exist.
                if (tail1 != null) {
                    tail1.setNext(head2);
                }

                // If there are no small tables but there are medium tables, keep medium as the start.
                if (tail1 == null && head1 == null && head2 != null) {
                    //noinspection DataFlowIssue
                    this.tables = head2;
                }

                // Connect medium list to big list if medium tables exist.
                if (tail2 != null) {
                    tail2.setNext(head3);
                } else if (tail1 != null) {
                    // If there are no medium tables, connect small list directly to big list.
                    tail1.setNext(head3);
                }

                // Keep the tail of the large list extracted for completeness.
                var unusedTail3 = tail3;
            }

            // Add a new client group into the waiting queue.
            public void addClient(String name, int diners) {

                // Create a fresh Client instance directly.
                var c = new Client();

                // Set the client's name.
                c.setName(name);

                // Set the group size.
                c.setDiners(diners);

                // Enqueue the client at the end of the waiting queue.
                this.clients.offer(c);
            }

            // Find the first table that can fit the given number of diners.
            public int findAvailableTable(int numOfDiners) {

                // Start scanning from the head of the tables list.
                var pos = this.tables;

                // Walk through the linked list until we run out of tables.
                while (pos != null) {

                    // Read the table object stored in the current node.
                    var currentTable = pos.getValue();

                    // Read the table number.
                    var num = currentTable.getNum();

                    // Read the total capacity of the table.
                    var places = currentTable.getPlaces();

                    // Read how many seats are currently free.
                    var free = currentTable.getFree();

                    // If this table can fit the group, return its number immediately.
                    if (free >= numOfDiners && places >= numOfDiners) {
                        return num;
                    }

                    // Move to the next node/table.
                    pos = pos.getNext();
                }

                // Return -1 to indicate that no suitable table exists right now.
                return -1;
            }

            // Try to seat the next possible client from the queue; return true if anyone was seated.
            public boolean seatNextClient() {

                // Create a temporary queue to preserve clients that cannot be seated now.
                var temp = new LinkedList<Client>();

                // Capture the original queue size (not used later, but kept from your draft).
                var sizeofq = this.clients.size();

                // Track whether at least one client got seated during this call.
                var tableFound = false;

                // Process clients until the original queue becomes empty.
                while (!this.clients.isEmpty()) {

                    // Dequeue the next waiting client.
                    var currentClient = this.clients.poll();

                    // Read the client's name (currently not used later, but kept).
                    var name = currentClient.getName();

                    // Read how many diners are in the group.
                    var diners = currentClient.getDiners();

                    // Search for an available table that can fit the group.
                    var numOfTable = this.findAvailableTable(diners);

                    // If no table fits, re-queue the client into the temporary queue.
                    if (numOfTable == -1) {

                        // Preserve the client for later seating attempts.
                        temp.offer(currentClient);

                    } else {

                        // Start scanning the tables list again to locate the chosen table node.
                        var pos = this.tables;

                        // Walk tables until we find the exact table number.
                        while (pos != null) {

                            // Read the current table.
                            var currentTable = pos.getValue();

                            // Read the current table number.
                            var num = currentTable.getNum();

                            // If this is the selected table, update its free seats.
                            if (num == numOfTable) {

                                // Reduce free seats by the group size (seating happens here).
                                currentTable.setFree(currentTable.getFree() - diners);

                                // Mark that we successfully seated someone.
                                tableFound = true;

                                // Stop scanning tables for this client.
                                break;
                            }

                            // Move to the next table.
                            pos = pos.getNext();
                        }
                    }
                }

                // Move the rest of the clients to the temporary queue.
                //noinspection ConstantValue
                while (!this.clients.isEmpty()) {
                    temp.offer(this.clients.poll());
                }

                // Restore all unseated clients back into the main queue in the same order.
                while (!temp.isEmpty()) {
                    this.clients.offer(temp.poll());
                }

                // Use the saved queue size variable so it remains part of the method as in your draft.
                var unusedSizeOfQueue = sizeofq;

                // Return whether at least one seating operation succeeded.
                return tableFound;
            }
        }
    };

    private final Runnable six = () -> {
        @SuppressWarnings("unused")
        class CharListSecTry { // Class definition

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
    };
}
