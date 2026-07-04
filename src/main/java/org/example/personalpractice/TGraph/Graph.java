package org.example.personalpractice.TGraph;

import org.jetbrains.annotations.NotNull;
import java.util.*;
import java.util.function.*;

/** a generic graph class
 * @param <T> the type of the vertices in the graph
 **/
@SuppressWarnings("unused")
public class Graph<T> {
    /** This map stores each vertex and its set of neighbors.
     * The key is the vertex
     * and The value is the set of neighbors.
     */
    private HashMap<T, LinkedHashSet<T>> adjList;

    /** This variable tells us if the graph is directed or undirected.
     * If it is directed, the graph will only store edges in one direction.
     * If it is undirected, the graph will store edges in both directions.
     * The default value is false.
     */
    private final boolean directed;

    /** This inner class represents an edge between two vertices.
     * @param <T> the type of the vertices in the edge
     */
    @SuppressWarnings("ClassCanBeRecord")
    public static class Edge<T> {
        /** This variable stores the vertex where the edge starts. */
        private final T from;

        /** This variable stores the vertex where the edge ends. */
        private final T to;

        /** This constructor creates a new edge between two vertices.
         * @param from the vertex where the edge starts
         * @param to the vertex where the edge ends
         */
        public Edge(@NotNull T from, @NotNull T to) {
            this.from = from;
            this.to = to;
        }

        /** This method returns the vertex where the edge starts.
         * @return the vertex where the edge starts
         */
        public T getFrom() { return this.from; }

        /** This method returns the vertex where the edge ends.
         * @return the vertex where the edge ends
         */
        public T getTo() { return this.to; }

        /** This method returns a string representation of the edge.
         * @return a string representation of the edge
         */
        @Override
        public String toString() { return this.from + " -> " + this.to; }
    }

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
     * and The value is the set of neighbors.
     * @return the adjacency list of the graph.
     */
    public HashMap<T, LinkedHashSet<T>> getAdjList() { return this.adjList; }

    /** This method sets a new adjacency list for the graph.
     * The adjacency list is a map where:
     * The key is the vertex
     * and The value is the set of neighbors.
     * @param adjList the new adjacency list to set.
     */
    public void setAdjList(@NotNull HashMap<T, LinkedHashSet<T>> adjList) {
        this.adjList = adjList;
    }

    /** This method returns true if the graph is directed.
     * Otherwise, it returns false.
     * @return true if the graph is directed, false otherwise.
     */
    public boolean getDirected() { return this.directed; }

    /** This method adds a new vertex to the graph.
     * If the vertex already exists, nothing happens.
     * @param vertex the vertex to add
     */
    public void addVertex(@NotNull T vertex) {
        // If the vertex does not exist, add it with an empty neighbors set.
        if (!this.adjList.containsKey(vertex)) {
            this.adjList.put(vertex, new LinkedHashSet<>());
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

        // Add v2 to the neighbors set of v1.
        this.adjList.get(v1).add(v2);

        // If the graph is undirected, add v1 to the neighbors set of v2.
        if (!this.directed) { this.adjList.get(v2).add(v1); }
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

        // Check if v2 is inside the neighbors set of v1.
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

        // If v1 exists, check if v2 is inside the neighbors set of v1.
        if (this.adjList.containsKey(v1)) {
            fromFirstToSecond = this.adjList.get(v1).contains(v2);
        }

        // If v2 exists, check if v1 is inside the neighbors set of v2.
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
        // If v1 exists, remove v2 from its neighbors set.
        if (this.adjList.containsKey(v1)) { this.adjList.get(v1).remove(v2); }

        // If the graph is undirected, also remove v1 from the neighbors set of v2.
        if (!this.directed) {
            if (this.adjList.containsKey(v2)) { this.adjList.get(v2).remove(v1); }
        }
    }

    /** This method removes a vertex from the graph.
     * @param vertex the vertex to remove
     */
    public void removeVertex(@NotNull T vertex) {
        // If the vertex does not exist, stop the method.
        if (!this.adjList.containsKey(vertex)) { return; }

        // Remove the given vertex from all neighbors sets.
        this.adjList.values().forEach(neighbors -> neighbors.remove(vertex));

        // Remove the vertex itself from the graph.
        this.adjList.remove(vertex);
    }

    /** This method returns copy of the neighbors of a given vertex.
     * If the vertex does not exist, an empty list is returned.
     * @param vertex the vertex to get the neighbors of
     * @return a list of the neighbors of the given vertex
     */
    public ArrayList<T> getCopyOfNeighbors(@NotNull T vertex) {
        // If the vertex does not exist, return an empty list.
        if (!this.adjList.containsKey(vertex)) { return new ArrayList<>(); }

        // Return a copy of the neighbors set as a list.
        return new ArrayList<>(this.adjList.get(vertex));
    }

    /** This method returns the number of vertices in the graph.
     * @return the number of vertices in the graph
     */
    public int getVertexCount() { return this.adjList.size(); }

    /** This method returns the number of edges in the graph.
     * If the graph is directed, each edge is counted once.
     * If the graph is undirected, each edge is returned only once.
     * @return the number of edges in the graph
     */
    public int getEdgeCount() {
        // If the graph is directed, count all outgoing edges.
        if (this.directed) {
            var c = 0;

            // Create a list of all vertices in the graph.
            var keys = new ArrayList<>(this.adjList.keySet());

            // Go over all vertices.
            for (T current : keys) { c += this.adjList.get(current).size(); }

            return c; // return the counter of all outgoing edges.
        }

        // In an undirected graph, getAllEdges returns each edge only once.
        return this.getAllEdges().size();
    }

    /** This method checks if the graph is empty.
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
        // Go over all vertices and their neighbors in the graph.
        for (var entry : this.adjList.entrySet()) {
            // Print the vertex and its neighbors.
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    /** This method returns the BFS traversal as a list starting from a given vertex.
     * If the start vertex does not exist, an empty list is returned.
     * @param start the vertex to start the BFS from
     * @return a list of the vertices in the BFS traversal, starting from the start vertex
     **/
    public ArrayList<T> bfsList(@NotNull T start) {
        var result = new ArrayList<T>(); // This list stores the BFS result.

        // If the start vertex does not exist, return an empty list.
        if (!this.adjList.containsKey(start)) { return result; }

        var visited = new HashSet<T>(); // This set stores all visited vertices.
        visited.add(start); // Add the start vertex to the visited set.

        // This deque stores the vertices that still need to be processed.
        var dq = new ArrayDeque<T>();
        dq.addLast(start); // Add the start vertex to the deque.

        // Continue while the deque is not empty.
        while (!dq.isEmpty()) {
            T current = dq.removeFirst(); // Take the first vertex from the deque.
            result.add(current); // Add the current vertex to the result list.

            // Get the neighbors set of the current vertex.
            var neighbors = this.adjList.get(current);

            // Go over all neighbors of the current vertex.
            for (T neighbor : neighbors) {
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
     * This method does not use recursion.
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

            // Get the neighbors set of the current vertex.
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

            // Get the neighbors set of the current vertex.
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

    /** This method returns the shortest path between two vertices.
     * If one of the vertices does not exist, an empty list is returned.
     * If there is no path between the vertices, an empty list is returned.
     * If the graph is directed, the path is checked from the start vertex to the target vertex.
     * @param start the vertex to start the search from
     * @param target the vertex to search for
     * @return a list of the vertices in the shortest path from start to target
     **/
    public ArrayList<T> shortestPath(@NotNull T start, @NotNull T target) {
        var path = new ArrayList<T>(); // This list stores the shortest path.

        // If one of the vertices does not exist, return an empty list.
        if (!this.adjList.containsKey(start) || !this.adjList.containsKey(target)) {
            return path;
        }

        // If both vertices are the same, return a path with only this vertex.
        if (start.equals(target)) {
            path.add(start);
            return path;
        }

        var visited = new HashSet<T>(); // This set stores all visited vertices.
        visited.add(start); // Add the start vertex to the visited set.

        // This map stores from which vertex we reached each vertex.
        var previous = new HashMap<T, T>();

        // This deque stores the vertices that still need to be processed.
        var dq = new ArrayDeque<T>();
        dq.addLast(start); // Add the start vertex to the deque.

        var found = false; // This variable tells us if the target was found.

        // Continue while the deque is not empty and the target was not found yet.
        while (!dq.isEmpty() && !found) {
            T current = dq.removeFirst(); // Take the first vertex from the deque.

            // Get the neighbors set of the current vertex.
            var neighbors = this.adjList.get(current);

            // Go over all neighbors of the current vertex.
            for (T neighbor : neighbors) {
                // If the neighbor was not visited yet, add it to visited and to the deque.
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    previous.put(neighbor, current);
                    dq.addLast(neighbor);

                    // If the target was found, stop the search.
                    if (neighbor.equals(target)) {
                        found = true;
                        break;
                    }
                }
            }
        }

        // If the target was not found, return an empty list.
        if (!found) { return path; }

        // Rebuild the path from target back to start.
        T current = target;

        while (current != null) {
            path.add(current);
            current = previous.get(current);
        }

        // Reverse the path because it was built from target to start.
        Collections.reverse(path);

        return path; // Return the shortest path.
    }

    /** This private helper method returns all connected neighbors of a given vertex.
     * If the graph is undirected, it returns the regular neighbors.
     * If the graph is directed, it returns outgoing and incoming neighbors.
     * This method is used for weak connection checks.
     * @param vertex the vertex to get all connected neighbors of
     * @return a set of all connected neighbors of the given vertex
     */
    private LinkedHashSet<T> getAllConnectedNeighbors(@NotNull T vertex) {
        var neighbors = new LinkedHashSet<T>(); // This set stores all connected neighbors.

        // If the vertex does not exist, return an empty set.
        if (!this.adjList.containsKey(vertex)) { return neighbors; }

        // Add all outgoing neighbors.
        neighbors.addAll(this.adjList.get(vertex));

        // If the graph is undirected, outgoing neighbors are enough.
        if (!this.directed) { return neighbors; }

        // Create a list of all vertices in the graph.
        var keys = new ArrayList<>(this.adjList.keySet());

        // Add all incoming neighbors.
        for (T current : keys) {
            // If current has an edge to vertex, then current is connected to vertex.
            if (this.adjList.get(current).contains(vertex)) {
                neighbors.add(current);
            }
        }

        return neighbors; // Return all connected neighbors.
    }

    /** This method checks if the graph is connected.
     * If the graph is empty, true is returned.
     * If the graph is directed, the method checks weak connection between the vertices.
     * This means that the direction of the edges is ignored only for this check.
     * @return true if the graph is connected, false otherwise
     */
    public boolean isConnected() {
        // If the graph is empty, it is considered connected.
        if (this.adjList.isEmpty()) { return true; }

        // Get any vertex from the graph to start the traversal.
        T start = this.adjList.keySet().iterator().next();

        var visited = new HashSet<T>(); // This set stores all visited vertices.

        // This deque stores the vertices that still need to be processed.
        var dq = new ArrayDeque<T>();
        dq.addLast(start); // Add the start vertex to the deque.
        visited.add(start); // Add the start vertex to the visited set.

        // Continue while the deque is not empty.
        while (!dq.isEmpty()) {
            T current = dq.removeFirst(); // Take the first vertex from the deque.

            // Get all connected neighbors of the current vertex.
            var neighbors = this.getAllConnectedNeighbors(current);

            // Go over all connected neighbors.
            for (T neighbor : neighbors) {
                // If the neighbor was not visited yet, add it to visited and to the deque.
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    dq.addLast(neighbor);
                }
            }
        }

        // If all vertices were visited, the graph is connected.
        return visited.size() == this.adjList.size();
    }

    /** This method returns all connected components in the graph.
     * If the graph is directed, the method ignores the direction of the edges.
     * Each connected component is returned as a list of vertices.
     * @return a list of all connected components in the graph
     */
    public ArrayList<ArrayList<T>> getConnectedComponents() {
        var components = new ArrayList<ArrayList<T>>(); // This list stores all connected components.
        var visited = new HashSet<T>(); // This set stores all visited vertices.

        // Create a list of all vertices in the graph.
        var keys = new ArrayList<>(this.adjList.keySet());

        // Go over all vertices in the graph.
        for (T vertex : keys) {
            // If the vertex was already visited, skip it.
            if (visited.contains(vertex)) { continue; }

            var component = new ArrayList<T>(); // This list stores the current connected component.

            // This deque stores the vertices that still need to be processed.
            var dq = new ArrayDeque<T>();
            dq.addLast(vertex); // Add the vertex to the deque.
            visited.add(vertex); // Add the vertex to the visited set.

            // Continue while the deque is not empty.
            while (!dq.isEmpty()) {
                T current = dq.removeFirst(); // Take the first vertex from the deque.
                component.add(current); // Add the current vertex to the current component.

                // Get all connected neighbors of the current vertex.
                var neighbors = this.getAllConnectedNeighbors(current);

                // Go over all connected neighbors.
                for (T neighbor : neighbors) {
                    // If the neighbor was not visited yet, add it to visited and to the deque.
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        dq.addLast(neighbor);
                    }
                }
            }

            // Add the current component to the components list.
            components.add(component);
        }

        return components; // Return all connected components.
    }

    /** This method checks if the graph has a cycle.
     * If the graph is directed, it checks for a directed cycle.
     * If the graph is undirected, it checks for an undirected cycle.
     * This method does not use recursion.
     * @return true if the graph has a cycle, false otherwise
     */
    public boolean hasCycle() {
        // Create a list of all vertices in the graph.
        var keys = new ArrayList<>(this.adjList.keySet());

        // If the graph is directed, use Kahn's Algorithm.
        if (this.directed) {
            // This map stores the in degree of each vertex.
            var inDegree = new HashMap<T, Integer>();

            // Set the initial in degree of all vertices to 0.
            for (T vertex : keys) {
                inDegree.put(vertex, 0);
            }

            // Calculate the in degree of each vertex.
            for (T from : keys) {
                // Get the neighbors set of the current vertex.
                var neighbors = this.adjList.get(from);

                // Go over all neighbors of the current vertex.
                for (T to : neighbors) {
                    inDegree.put(to, inDegree.get(to) + 1);
                }
            }

            // This deque stores all vertices with in degree 0.
            var dq = new ArrayDeque<T>();

            // Add all vertices with in degree 0 to the deque.
            for (T vertex : keys) {
                if (inDegree.get(vertex) == 0) {
                    dq.addLast(vertex);
                }
            }

            var removedCount = 0; // This variable counts how many vertices were removed.

            // Continue while the deque is not empty.
            while (!dq.isEmpty()) {
                T current = dq.removeFirst(); // Take the first vertex from the deque.
                removedCount++; // Count this vertex as removed.

                // Get the neighbors set of the current vertex.
                var neighbors = this.adjList.get(current);

                // Go over all neighbors of the current vertex.
                for (T neighbor : neighbors) {
                    // Reduce the in degree of the neighbor by 1.
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);

                    // If the neighbor now has in degree 0, add it to the deque.
                    if (inDegree.get(neighbor) == 0) {
                        dq.addLast(neighbor);
                    }
                }
            }

            // If not all vertices were removed, there is a directed cycle.
            return removedCount != this.adjList.size();
        }

        // If the graph is undirected, use BFS with parent tracking.
        var visited = new HashSet<T>(); // This set stores all visited vertices.

        // Go over all vertices in the graph.
        for (T start : keys) {
            // If the vertex was already visited, skip it.
            if (visited.contains(start)) { continue; }

            // This map stores the parent of each vertex in the BFS traversal.
            var parent = new HashMap<T, T>();

            // This deque stores the vertices that still need to be processed.
            var dq = new ArrayDeque<T>();
            dq.addLast(start); // Add the start vertex to the deque.
            visited.add(start); // Add the start vertex to the visited set.
            parent.put(start, null); // The start vertex has no parent.

            // Continue while the deque is not empty.
            while (!dq.isEmpty()) {
                T current = dq.removeFirst(); // Take the first vertex from the deque.

                // Get the neighbors set of the current vertex.
                var neighbors = this.adjList.get(current);

                // Go over all neighbors of the current vertex.
                for (T neighbor : neighbors) {
                    // If the neighbor was not visited yet, add it to visited and to the deque.
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        parent.put(neighbor, current);
                        dq.addLast(neighbor);
                    }
                    // If the neighbor is visited and it is not the parent, a cycle exists.
                    else {
                        T currentParent = parent.get(current);

                        if (!neighbor.equals(currentParent)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false; // Return false if no cycle was found.
    }

    /** This method checks if the graph is a tree.
     * A tree is an undirected connected graph with no cycles.
     * If the graph is directed, false is returned.
     * @return true if the graph is a tree, false otherwise
     */
    public boolean isTree() {
        // A directed graph is not considered a tree in this method.
        if (this.directed) { return false; }

        // A tree must be connected and must not have a cycle.
        return this.isConnected() && !this.hasCycle();
    }

    /** This method returns the degree of a given vertex.
     * If the vertex does not exist, 0 is returned.
     * If the graph is undirected, the degree is the number of neighbors.
     * If the graph is directed, the degree is the in degree plus the out degree.
     * @param vertex the vertex to get the degree of
     * @return the degree of the given vertex
     */
    public int getDegree(@NotNull T vertex) {
        // If the vertex does not exist, return 0.
        if (!this.adjList.containsKey(vertex)) { return 0; }

        // If the graph is undirected, return the number of neighbors.
        if (!this.directed) {
            return this.adjList.get(vertex).size();
        }

        // In a directed graph, the degree is in degree plus out degree.
        return this.getInDegree(vertex) + this.getOutDegree(vertex);
    }

    /** This method returns the in degree of a given vertex.
     * The in degree is the number of edges that enter the vertex.
     * If the vertex does not exist, 0 is returned.
     * @param vertex the vertex to get the in degree of
     * @return the in degree of the given vertex
     */
    public int getInDegree(@NotNull T vertex) {
        // If the vertex does not exist, return 0.
        if (!this.adjList.containsKey(vertex)) { return 0; }

        var count = 0; // This variable counts the incoming edges.

        // Create a list of all vertices in the graph.
        var keys = new ArrayList<>(this.adjList.keySet());

        // Go over all vertices in the graph.
        for (T current : keys) {
            // If the current vertex has an edge to the given vertex, increase the count.
            if (this.adjList.get(current).contains(vertex)) {
                count++;
            }
        }

        return count; // Return the in degree.
    }

    /** This method returns the out degree of a given vertex.
     * The out degree is the number of edges that leave the vertex.
     * If the vertex does not exist, 0 is returned.
     * @param vertex the vertex to get the out degree of
     * @return the out degree of the given vertex
     */
    public int getOutDegree(@NotNull T vertex) {
        // If the vertex does not exist, return 0.
        if (!this.adjList.containsKey(vertex)) { return 0; }

        // The out degree is the number of neighbors of the vertex.
        return this.adjList.get(vertex).size();
    }

    /** This method returns all vertices in the graph.
     * It returns a copy of the vertices list.
     * @return a list of all vertices in the graph
     */
    public ArrayList<T> getAllVertices() {
        // Return a copy of all vertices in the graph.
        return new ArrayList<>(this.adjList.keySet());
    }


    /** This method returns all edges in the graph.
     * If the graph is undirected, each edge is returned only once.
     * If the graph is directed, each edge is returned according to its direction.
     * @return a list of all edges in the graph
     */
    public ArrayList<Edge<T>> getAllEdges() {
        var edges = new ArrayList<Edge<T>>(); // This list stores all edges.

        // This function checks if an edge list already contains a connection.
        BiFunction<ArrayList<Edge<T>>, Edge<T>, Boolean> contains;
        contains = (list, newEdge) -> {
            // Go over all edges in the list.
            for (var edge : list) {
                // If one of the directions exists, the connection already exists.
                return edge.getFrom().equals(newEdge.getFrom())
                        && edge.getTo().equals(newEdge.getTo())
                        || edge.getFrom().equals(newEdge.getTo())
                        && edge.getTo().equals(newEdge.getFrom());
            }
            return false; // Return false if the connection was not found.
        };

        // Go over all vertices and their neighbors in the graph.
        for (var entry : this.adjList.entrySet()) {
            T from = entry.getKey(); // Get the current vertex.
            var neighbors = entry.getValue(); // Get the neighbors set of the current vertex.

            // Go over all neighbors of the current vertex.
            for (T to : neighbors) {
                // Create the current edge.
                var currentEdge = new Edge<>(from, to);

                // Add the edge if the graph is directed or if the undirected connection was not added yet.
                if (this.directed || !contains.apply(edges, currentEdge)) {
                    edges.add(currentEdge);
                }
            }
        }

        return edges; // Return all edges.
    }

    /** This method returns a copy of the graph.
     * The copied graph has the same direction type, vertices and edges.
     * @return a copy of the graph
     */
    public Graph<T> shallowcopy() {
        var graph = new Graph<T>(this.directed); // This variable stores the copied graph.
        var map = new HashMap<T, LinkedHashSet<T>>();

        for (var entry : this.adjList.entrySet()) {
            map.put(entry.getKey(), new LinkedHashSet<>(entry.getValue()));
        }
        graph.setAdjList(map);

        return graph; // Return the copied graph.
    }

    /** This method returns a deep copy of the graph.
     * The copied graph has the same direction type, copied vertices and copied edges.
     * This method uses a copy function because the graph does not know how to copy T by itself.
     * @param copyFunction a function that knows how to copy a vertex
     * @return a deep copy of the graph
     */
    // var copiedGraph = graph.deepCopy(SOME_ENTRY_VALUE_CLASS::new);
    public Graph<T> deepCopy(@NotNull Function<T, T> copyFunction) {
        var graph = new Graph<T>(this.directed); // This variable stores the copied graph.

        // This map stores the relation between each original vertex and its copied vertex.
        var copiedVertices = new HashMap<T, T>();

        // Go over all vertices in the graph.
        for (T vertex : this.adjList.keySet()) {
            // Create a copy of the current vertex.
            T copiedVertex = copyFunction.apply(vertex);

            // Store the relation between the original vertex and the copied vertex.
            copiedVertices.put(vertex, copiedVertex);

            // Add the copied vertex to the copied graph.
            graph.addVertex(copiedVertex);
        }

        // Go over all vertices and their neighbors in the graph.
        for (var entry : this.adjList.entrySet()) {
            T originalFrom = entry.getKey(); // Get the original from vertex.
            var originalNeighbors = entry.getValue(); // Get the original neighbors set.

            // Get the copied from vertex.
            T copiedFrom = copiedVertices.get(originalFrom);

            // Go over all original neighbors.
            for (T originalTo : originalNeighbors) {
                // Get the copied to vertex.
                T copiedTo = copiedVertices.get(originalTo);

                // Add an edge between the copied vertices.
                graph.addEdge(copiedFrom, copiedTo);
            }
        }

        return graph; // Return the deep copied graph.
    }

    /** This method returns a reversed copy of the graph.
     * If the graph is directed, all edges will be reversed.
     * If the graph is undirected, the reversed graph will be the same as a copy.
     * @return a reversed copy of the graph
     */
    public Graph<T> reverse() {
        // If the graph is undirected, the reverse is the same as a copy.
        if (!this.directed) {
            return this.shallowcopy();
        }

        var reversedGraph = new Graph<T>(true); // This variable stores the reversed graph.

        // Create a list of all vertices in the graph.
        var vertices = new ArrayList<>(this.adjList.keySet());

        // Add all vertices to the reversed graph.
        for (T vertex : vertices) {
            reversedGraph.addVertex(vertex);
        }

        // Get all edges in the graph.
        var edges = this.getAllEdges();

        // Add all edges in the opposite direction.
        for (Edge<T> edge : edges) {
            reversedGraph.addEdge(edge.getTo(), edge.getFrom());
        }

        return reversedGraph; // Return the reversed graph.
    }
}