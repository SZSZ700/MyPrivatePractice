package org.example.Q4;
import org.example.Node;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Function;

public class Restaurant {
    // queue of clients
    private Queue<Client> clients;
    // list of tables
    private Node<Table> tables;
    // number of table
    private static int num = 0;

    // function that recive the number of small/medium/big tables and build list with
    // all type of tables
    public Restaurant(int small, int medium, int large){
        // private lambda function to create tables list, of given size
        Function<Integer, Node<Table>[]> createTbl = (size)-> {
            // pointer for the head of the new list
            Node<Table> chain = null;
            // pointer for the tail of the new list
            Node<Table> tail = null;
            // var for iteration
            var i = 0;

            // iteration
            while (i < size){
                // create new table
                var tableToAdd = new Table(++Restaurant.num, size, size);
                // create new node with the new table
                var nodeToAdd = new Node<Table>(tableToAdd);

                // add the new node to the new list
                if (chain == null){
                    chain = nodeToAdd;
                    tail = nodeToAdd;
                }else{
                    tail.setNext(nodeToAdd);
                    tail = tail.getNext();
                }

                i++;
            }

            // return array witch the first cell points to the head of the new list
            // and the second cell points to the tail of the new list
            return new Node[]{chain,tail};
        };

        // initialize the queue
        this.clients = new LinkedList<>();

        // array witch the first cell points to the head of the new small list
        // and the second cell points to the tail of the new small list
        var arrayOflistOfSmallTables = createTbl.apply(small);
        var head1 = arrayOflistOfSmallTables[0];
        var tail1 = arrayOflistOfSmallTables[1];

        // array witch the first cell points to the head of the new medium list
        // and the second cell points to the tail of the new medium list
        var arrayOflistOfMediumTables = createTbl.apply(medium);
        var head2 = arrayOflistOfMediumTables[0];
        var tail2 = arrayOflistOfMediumTables[1];

        // array witch the first cell points to the head of the new big list
        // and the second cell points to the tail of the new big list
        var arrayOflistOfBigTables = createTbl.apply(large);
        var head3 = arrayOflistOfBigTables[0];
        var tail3 = arrayOflistOfBigTables[1];

        this.tables = head1;
        tail1.setNext(head2);
        tail2.setNext(head3);
    }

    // function that recive the num of diners, and returns the number of the table that
    // can contains them all
    public int findAvailableTable(int numOfDiners){
        // pointer for the list of tables
        var pos = this.tables;

        while (pos != null){
            // current table
            var currentTable = pos.getValue();
            // table number
            var num = currentTable.getNum();
            // how many diners can seat on the table
            var places = currentTable.getPlaces();
            // num of empty chairs
            var free = currentTable.getFree();
            // return thr number of the table
            if (free >= numOfDiners && places >= numOfDiners ){ return num; }
            // move to the next table
            pos = pos.getNext();
        }

        return -1; // ther's no table that can contains all diners
    }

    public boolean seatNextClient(){
        // restoration queue
        var temp = new LinkedList<Client>();
        // size of the clients queue
        var sizeofq = this.clients.size();
        // var to detect if the function found table that can fit some of the client
        var tableFound = false;

        while (!this.clients.isEmpty()){
            // current client
            var currentClient = this.clients.poll();
            // name of current client
            var name = currentClient.getName();
            // num of diners that came with the client
            var diners = currentClient.getDiners();
            // [num of the table] that can fit the client,
            // and the diners that came with him
            var numOfTable = this.findAvailableTable(diners);

            // if the function cant find table that fit the client
            // offer the client to the restoration queue
            if (numOfTable == -1){
                temp.offer(currentClient);
            }else {
                // pointer for the list of tables
                var pos = this.tables;

                // find [the table] that can fit the client
                while (pos != null){
                    // current table
                    var currentTable = pos.getValue();
                    // table number
                    var num = currentTable.getNum();
                    // if the table got found
                    if (num == numOfTable){
                        // reinitialzie the num of the free places for seating
                        currentTable.setFree(currentTable.getFree() - diners);
                        tableFound = true;
                        break;
                    }

                    // move to the next table
                    pos = pos.getNext();
                }

            }
        }

        // restore the original queue
        while (!temp.isEmpty()){
            this.clients.offer(temp.poll());
        }

        // if the function couldn't find table for any of the clients, return false
        // else return true
        return tableFound;
    }
}
