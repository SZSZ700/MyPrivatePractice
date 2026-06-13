package org.example.personalpractice.TGraph;
import org.jetbrains.annotations.NotNull;
import java.util.*;

/** a generic graph class
 * @param <T> the type of the vertices in the graph
 **/
@SuppressWarnings("unused")
public class Graph<T> {
    /** This map stores each vertex and its list of neighbors.
     * The key is the vertex
     * and The value is the list of neighbors.
     */
    private HashMap<T, ArrayList<T>> adjList;

    /** This variable tells us if the graph is directed or undirected.
     * If it is directed, the graph will only store edges in one direction.
     * If it is undirected, the graph will store edges in both directions.
     * The default value is false.
     */
    private final boolean directed;

    /** This constructor creates an undirected graph by default. **/
    public Graph() {
        this.adjList = new HashMap<>();
        this.directed = false;
    }

    /**
     * This constructor creates a graph according to the given direction type.
     * @param directed true if the graph is directed, false if it is undirected.
     */
    public Graph(boolean directed) {
        this.adjList = new HashMap<>();
        this.directed = directed;
    }

    /** This method returns the adjacency list of the graph.
     * The adjacency list is a map where:
     * The key is the vertex
     * and The value is the list of neighbors.
     * @return the adjacency list of the graph.
     */
    public HashMap<T, ArrayList<T>> getAdjList() { return this.adjList; }

    /** This method sets a new adjacency list for the graph.
     * The adjacency list is a map where:
     * The key is the vertex
     * and The value is the list of neighbors.
     * @param adjList the new adjacency list to set.
     */
    public void setAdjList(@NotNull HashMap<T, ArrayList<T>> adjList) {
        this.adjList = adjList;
    }

    /** This method returns true if the graph is directed.
     * Otherwise, it returns false.
     * @return true if the graph is directed, false otherwise.
     */
    public boolean getDirected() { return this.directed; }

    /** This method adds a new vertex to the graph.
     * If the vertex already exists, nothing happens.
     * If the graph is undirected, the vertex will be added to both directions.
     * If the graph is directed, the vertex will be added only to the first direction.
     * @param vertex the vertex to add
     */
    public void addVertex(@NotNull T vertex) {
        // If the vertex does not exist, add it with an empty neighbors list.
        if (!this.adjList.containsKey(vertex)) {
            this.adjList.put(vertex, new ArrayList<>());
        }
    }

    /** This method adds an edge between two vertices.
     * If the graph is undirected, the edge will be added in both directions.
     * If the graph is directed, the edge will be added only in the first direction.
     * @param v1 1st vertex to add the edge to
     * @param v2 2nd vertex to add the edge to
     */
    public void addEdge(@NotNull T v1, @NotNull T v2) {
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

    /** This method checks if a vertex exists in the graph.
     * @param vertex the vertex to check for
     * @return true if the vertex exists, false otherwise.
     **/
    public boolean hasVertex(@NotNull T vertex) {
        return this.adjList.containsKey(vertex);
    }

    /** This method checks if an edge exists between two vertices.
     * @param v1 1st vertex to check for the edge
     * @param v2 2nd vertex to check for the edge
     * @return true if the edge exists, false otherwise.
     **/
    public boolean hasEdge(@NotNull T v1, @NotNull T v2) {
        // If the first vertex does not exist, the edge cannot exist.
        if (!this.adjList.containsKey(v1)) { return false; }

        // Check if v2 is inside the neighbors list of v1.
        return this.adjList.get(v1).contains(v2);
    }

    /** This method checks if there is any connection between two vertices in any direction.
     * @param v1 1st vertex to check for connection
     * @param v2 2nd vertex to check for connection
     * @return true if there is a connection between the vertices, false otherwise.
     */
    public boolean hasConnection(@NotNull T v1, @NotNull T v2) {
        // This variable checks the edge from v1 to v2.
        var fromFirstToSecond = false;

        // This variable checks the edge from v2 to v1.
        var fromSecondToFirst = false;

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

    /** This method removes an edge between two vertices.
     * If the graph is undirected, the edge will be removed in both directions.
     * @param v1 1st vertex to remove the edge from
     * @param v2 2nd vertex to remove the edge from
     */
    public void removeEdge(@NotNull T v1, @NotNull T v2) {
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

    /** This method removes a vertex from the graph.
     * If the graph is undirected, the vertex will be removed in both directions.
     * If the graph is directed, the vertex will be removed only from the first direction.
     * @param vertex the vertex to remove
     */
    public void removeVertex(@NotNull T vertex) {
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

    /** This method returns copy of the neighbors of a given vertex.
     * If the vertex does not exist, an empty list is returned.
     * If the graph is directed, the neighbors are the vertices that can be reached from the given vertex.
     * If the graph is undirected, the neighbors are the vertices that can be reached from the given vertex in both directions.
     * @param vertex the vertex to get the neighbors of
     * @return a list of the neighbors of the given vertex
     */
    public ArrayList<T> getCopyOfNeighbors(@NotNull T vertex) {
        // If the vertex does not exist, return an empty list.
        if (!this.adjList.containsKey(vertex)) { return new ArrayList<>(); }

        // Return the neighbors list of the given vertex.
        return new ArrayList<>(this.adjList.get(vertex));
    }

    /** This method returns the number of vertices in the graph.
     * @return the number of vertices in the graph
     */
    public int getVertexCount() { return this.adjList.size(); }

    /** This method returns the number of edges in the graph.
     * If the graph is directed, each edge is counted once.
     * If the graph is undirected, each edge is counted twice.
     * @return the number of edges in the graph
     */
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

    /** This method checks if the graph is empty.
     * If the graph is empty, it returns true.
     * If the graph is not empty, it returns false.
     * @return true if the graph is empty, false otherwise
     */
    public boolean isEmpty() { return this.adjList.isEmpty(); }

    /** This method removes all vertices and edges from the graph.
     * It clears the adjacency list.
     */
    public void clear() { this.adjList.clear(); }

    /** This method prints the graph.
     * It prints the vertices and their neighbors.
     */
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

    /** This method returns the BFS traversal as a list starting from a given vertex.
     * If the start vertex does not exist, an empty list is returned.
     * If the graph is directed, the traversal is from the start vertex to all reachable vertices.
     * @param start the vertex to start the BFS from
     * @return a list of the vertices in the BFS traversal, starting from the start vertex
     **/
    public ArrayList<T> bfsList(@NotNull T start) {
        var result = new ArrayList<T>(); // This list stores the BFS result.

        // If the start vertex does not exist, return an empty list.
        if (!this.adjList.containsKey(start)) { return result; }

        var visited = new HashSet<>(); // This set stores all visited vertices.
        visited.add(start); // Add the start vertex to the visited set.

        // This deque stores the vertices that still need to be processed.
        var dq = new ArrayDeque<T>();
        dq.addLast(start); // Add the start vertex to the deque.

        // Continue while the deque is not empty.
        while (!dq.isEmpty()) {
            T current = dq.removeFirst(); // Take the first vertex from the deque.
            result.add(current); // Add the current vertex to the result list.
            // Get the neighbors list of the current vertex.
            var neighbors = this.adjList.get(current);

            // Go over all neighbors of the current vertex.
            for (T neighbor : neighbors) {
                // Get the current neighbor.
                // If the neighbor was not visited yet, add it to visited and to the deque.
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    dq.addLast(neighbor);
                }
            }
        }

        return result; // Return the BFS result.
    }

    /** This method returns the DFS traversal as a list starting from a given vertex.
     * If the start vertex does not exist, an empty list is returned.
     * If the graph is directed, the traversal is from the start vertex to all reachable vertices.
     * @param start the vertex to start the DFS from
     * @return a list of the vertices in the DFS traversal, starting from the start vertex
     **/
    public ArrayList<T> dfsList(@NotNull T start) {
        var result = new ArrayList<T>(); // This list stores the DFS result.

        // If the start vertex does not exist, return an empty list.
        if (!this.adjList.containsKey(start)) { return result; }

        var visited = new HashSet<T>(); // This set stores all visited vertices.

        // This deque works like a stack for the vertices that still need to be processed.
        var stack = new ArrayDeque<T>();
        stack.addLast(start); // Add the start vertex to the stack.

        // Continue while the stack is not empty.
        while (!stack.isEmpty()) {
            T current = stack.removeLast(); // Take the last vertex from the stack.

            // If the current vertex was already visited, skip it.
            if (visited.contains(current)) { continue; }

            visited.add(current); // Add the current vertex to the visited set.
            result.add(current); // Add the current vertex to the result list.

            // Get the neighbors list of the current vertex.
            var neighbors = this.adjList.get(current);

            // Go over all neighbors of the current vertex.
            for (T neighbor : neighbors) {
                // If the neighbor was not visited yet, add it to the stack.
                if (!visited.contains(neighbor)) {
                    stack.addLast(neighbor);
                }
            }
        }

        return result; // Return the DFS result.
    }

    /** This method checks if there is a path between two vertices.
     * If one of the vertices does not exist, false is returned.
     * If the graph is directed, the path is checked from the start vertex to the target vertex.
     * @param start the vertex to start the search from
     * @param target the vertex to search for
     * @return true if there is a path from start to target, false otherwise
     **/
    public boolean hasPath(@NotNull T start, @NotNull T target) {
        // If one of the vertices does not exist, there cannot be a path.
        if (!this.adjList.containsKey(start) || !this.adjList.containsKey(target)) {
            return false;
        }

        // If both vertices are the same, there is a path from the vertex to itself.
        if (start.equals(target)) { return true; }

        var visited = new HashSet<T>(); // This set stores all visited vertices.
        visited.add(start); // Add the start vertex to the visited set.

        // This deque stores the vertices that still need to be processed.
        var dq = new ArrayDeque<T>();
        dq.addLast(start); // Add the start vertex to the deque.

        // Continue while the deque is not empty.
        while (!dq.isEmpty()) {
            T current = dq.removeFirst(); // Take the first vertex from the deque.

            // Get the neighbors list of the current vertex.
            var neighbors = this.adjList.get(current);

            // Go over all neighbors of the current vertex.
            for (T neighbor : neighbors) {
                // If the target was found, return true.
                if (neighbor.equals(target)) { return true; }

                // If the neighbor was not visited yet, add it to visited and to the deque.
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    dq.addLast(neighbor);
                }
            }
        }

        return false; // Return false if no path was found.
    }
}