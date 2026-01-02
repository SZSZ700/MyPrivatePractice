// Declare the package this class belongs to.
package org.example.Q4;

// Import the custom Node type used for the linked list of tables.
import org.example.Node;

// Import LinkedList to implement the client queue and the temporary restoration queue.
import java.util.LinkedList;

// Import Queue interface for FIFO behavior of waiting clients.
import java.util.Queue;

// Import Function to define the lambda that builds a list of tables.
import java.util.function.Function;

// Import ObjectProvider to request fresh prototype beans from Spring on demand.
import org.springframework.beans.factory.ObjectProvider;

// Define the main restaurant logic: manage tables + waiting clients + seating algorithm.
public class Restaurant {

    // Keep a FIFO queue of clients waiting to be seated.
    private Queue<Client> clients;

    // Keep a linked list of all tables (small + medium + large) using your Node class.
    private Node<Table> tables;

    // Keep a global counter to assign unique table numbers across all created tables.
    private static int num = 0;

    // Keep a Spring provider that can create a new Table instance each time (prototype).
    private final ObjectProvider<Table> tableProvider;

    // Keep a Spring provider that can create a new Client instance each time (prototype).
    private final ObjectProvider<Client> clientProvider;

    // Build a restaurant with a specific number of small/medium/large tables.
    public Restaurant(int small, int medium, int large,
                      ObjectProvider<Table> tableProvider,
                      ObjectProvider<Client> clientProvider) {

        // Save the provider so we can request new tables on demand.
        this.tableProvider = tableProvider;

        // Save the provider so we can request new clients on demand.
        this.clientProvider = clientProvider;

        // Define a helper lambda that creates a linked list of tables of a given size.
        Function<Integer, Node<Table>[]> createTbl = (size) -> {

            // Point to the head of the new linked list (starts empty).
            Node<Table> chain = null;

            // Point to the tail of the new linked list (used for O(1) append).
            Node<Table> tail = null;

            // Use an index variable for the loop.
            var i = 0;

            // Create "size" tables (your original logic: size controls both count and capacity).
            while (i < size) {

                // Ask Spring for a fresh Table instance (prototype).
                var tableToAdd = this.tableProvider.getObject();

                // Assign the next unique table number.
                tableToAdd.setNum(++Restaurant.num);

                // Set capacity to match the "size" parameter (your current model).
                tableToAdd.setPlaces(size);

                // Start with all seats free at the beginning.
                tableToAdd.setFree(size);

                // Wrap the table inside a Node to insert into the linked list.
                var nodeToAdd = new Node<Table>(tableToAdd);

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
            return new Node[]{ chain, tail };
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

        // Connect small list to medium list.
        tail1.setNext(head2);

        // Connect medium list to big list.
        tail2.setNext(head3);
    }

    // Add a new client group into the waiting queue.
    public void addClient(String name, int diners) {

        // Ask Spring for a fresh Client instance (prototype).
        var c = this.clientProvider.getObject();

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
            if (free >= numOfDiners && places >= numOfDiners) { return num; }

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

        // Restore all unseated clients back into the main queue in the same order.
        while (!temp.isEmpty()) { this.clients.offer(temp.poll()); }

        // Return whether at least one seating operation succeeded.
        return tableFound;
    }
}
