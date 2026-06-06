package org.example.personalpractice.TGraph;
import java.util.*;

@SuppressWarnings("unused")
public class Graph<T> {
    // This map stores each vertex and its list of neighbors.
    private HashMap<T, ArrayList<T>> adjList;

    // This variable tells us if the graph is directed or undirected.
    private final boolean directed;

    // This constructor creates an undirected graph by default.
    public Graph() {
        this.adjList = new HashMap<>();
        this.directed = false;
    }

    // This constructor creates a graph according to the given direction type.
    public Graph(boolean directed) {
        this.adjList = new HashMap<>();
        this.directed = directed;
    }

    // This method returns the adjacency list of the graph.
    public HashMap<T, ArrayList<T>> getAdjList() { return this.adjList; }

    // This method sets a new adjacency list for the graph.
    public void setAdjList(HashMap<T, ArrayList<T>> adjList) {
        this.adjList = adjList;
    }

    // This method returns true if the graph is directed.
    public boolean getDirected() { return this.directed; }

    // This method adds a new vertex to the graph.
    public void addVertex(T vertex) {
        // If the vertex does not exist, add it with an empty neighbors list.
        if (!this.adjList.containsKey(vertex)) {
            this.adjList.put(vertex, new ArrayList<>());
        }
    }

    // This method adds an edge between two vertices.
    public void addEdge(T v1, T v2) {
        // Make sure the first vertex exists in the graph.
        this.addVertex(v1);
        // Make sure the second vertex exists in the graph.
        this.addVertex(v2);

        // Add v2 to the neighbors list of v1 if it is not already there.
        if (!this.adjList.get(v1).contains(v2)) {
            this.adjList.get(v1).add(v2);
        }

        // If the graph is undirected, add v1 to the neighbors list of v2.
        if (!this.directed) {
            // Add v1 to the neighbors list of v2 if it is not already there.
            if (!this.adjList.get(v2).contains(v1)) {
                this.adjList.get(v2).add(v1);
            }
        }
    }

    // This method checks if a vertex exists in the graph.
    public boolean hasVertex(T vertex) {
        return this.adjList.containsKey(vertex);
    }

    // This method checks if an edge exists between two vertices.
    public boolean hasEdge(T v1, T v2) {
        // If the first vertex does not exist, the edge cannot exist.
        if (!this.adjList.containsKey(v1)) { return false; }

        // Check if v2 is inside the neighbors list of v1.
        return this.adjList.get(v1).contains(v2);
    }

    // This method checks if there is any connection between two vertices in any direction.
    public boolean hasConnection(T v1, T v2) {
        // This variable checks the edge from v1 to v2.
        boolean fromFirstToSecond = false;

        // This variable checks the edge from v2 to v1.
        boolean fromSecondToFirst = false;

        // If v1 exists, check if v2 is inside the neighbors list of v1.
        if (this.adjList.containsKey(v1)) {
            fromFirstToSecond = this.adjList.get(v1).contains(v2);
        }

        // If v2 exists, check if v1 is inside the neighbors list of v2.
        if (this.adjList.containsKey(v2)) {
            fromSecondToFirst = this.adjList.get(v2).contains(v1);
        }

        // Return true if at least one direction exists.
        return fromFirstToSecond || fromSecondToFirst;
    }

    // This method removes an edge between two vertices.
    public void removeEdge(T v1, T v2) {
        // If v1 exists, remove v2 from its neighbors list.
        if (this.adjList.containsKey(v1)) { this.adjList.get(v1).remove(v2); }

        // If the graph is undirected, also remove v1 from the neighbors list of v2.
        if (!this.directed) {
            // If v2 exists, remove v1 from its neighbors list.
            if (this.adjList.containsKey(v2)) {
                this.adjList.get(v2).remove(v1);
            }
        }
    }

    // This method removes a vertex from the graph.
    public void removeVertex(T vertex) {
        // If the vertex does not exist, stop the method.
        if (!this.adjList.containsKey(vertex)) { return; }

        // Create a list of all vertices in the graph.
        var keys = new ArrayList<>(this.adjList.keySet());

        // Go over all vertices in the graph.
        for (T current : keys) {
            // Get the current vertex.
            // Remove the given vertex from the neighbors list of the current vertex.
            this.adjList.get(current).remove(vertex);
        }

        // Remove the vertex itself from the graph.
        this.adjList.remove(vertex);
    }

    // This method returns the neighbors of a given vertex.
    public ArrayList<T> getNeighbors(T vertex) {
        // If the vertex does not exist, return an empty list.
        if (!this.adjList.containsKey(vertex)) { return new ArrayList<>(); }

        // Return the neighbors list of the given vertex.
        return this.adjList.get(vertex);
    }

    // This method returns the number of vertices in the graph.
    public int getVertexCount() { return this.adjList.size(); }

    // This method returns the number of edges in the graph.
    public int getEdgeCount() {
        // This variable counts all neighbor connections.
        var count = 0;

        // Create a list of all vertices in the graph.
        var keys = new ArrayList<>(this.adjList.keySet());

        // Go over all vertices.
        for (T current : keys) {
            // Get the current vertex.
            // Add the number of neighbors of the current vertex.
            count += this.adjList.get(current).size();
        }

        // In a directed graph, each edge is counted once.
        if (this.directed) { return count; }

        // In an undirected graph, each edge is counted twice.
        return count / 2;
    }

    // This method checks if the graph is empty.
    public boolean isEmpty() { return this.adjList.isEmpty(); }

    // This method removes all vertices and edges from the graph.
    public void clear() { this.adjList.clear(); }

    // This method prints the graph.
    public void printGraph() {
        // Create a list of all vertices in the graph.
        var keys = new ArrayList<>(this.adjList.keySet());

        // Go over all vertices.
        for (T vertex : keys) {
            // Get the current vertex.
            // Print the vertex and its neighbors.
            System.out.println(vertex + " -> " + this.adjList.get(vertex));
        }
    }

    // This method returns the BFS traversal
    // as a list starting from a given vertex.
    public ArrayList<T> bfsList(T start) {
        // This list stores the BFS result.
        var result = new ArrayList<T>();

        // If the start vertex does not exist, return an empty list.
        if (!this.adjList.containsKey(start)) { return result; }

        // This list stores all visited vertices.
        var visited = new HashSet<>();
        // Add the start vertex to the visited list.
        visited.add(start);

        // This queue stores the vertices that still need to be processed.
        var q = new LinkedList<T>();
        // Add the start vertex to the queue.
        q.offer(start);

        // Continue while the queue is not empty.
        while (!q.isEmpty()) {
            // Take the first vertex from the queue.
            T current = q.poll();

            // Add the current vertex to the result list.
            result.add(current);

            // Get the neighbors list of the current vertex.
            var neighbors = this.adjList.get(current);

            // Go over all neighbors of the current vertex.
            for (T neighbor : neighbors) {
                // Get the current neighbor.
                // If the neighbor was not visited yet, add it to visited and to the queue.
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    q.offer(neighbor);
                }
            }
        }

        // Return the BFS result.
        return result;
    }
}