package org.example;
import java.util.*;

public class Graph<T> {
    private Map<T, List<T>> adjList; // מפת שכנים של כל צומת

    // בונה גרף חדש
    public Graph() {
        adjList = new HashMap<>(); // אתחול מפת השכנים
    }

    // מוסיף צומת חדש לגרף אם הוא לא קיים
    public void addVertex(T vertex) {
        adjList.putIfAbsent(vertex, new ArrayList<>()); // אם הצומת לא קיים, הוסף אותו עם רשימה ריקה של שכנים
    }

    // מוסיף קשר בין שני צמתים
    public void addEdge(T vertex1, T vertex2) {
        addVertex(vertex1); // הוסף את vertex1 אם הוא לא קיים
        addVertex(vertex2); // הוסף את vertex2 אם הוא לא קיים
        adjList.get(vertex1).add(vertex2); // הוסף את vertex2 לשכנים של vertex1
        adjList.get(vertex2).add(vertex1); // הוסף את vertex1 לשכנים של vertex2 (הגרף לא מכוון)
    }

    // מסירה את הקשר בין שני צמתים
    public void removeEdge(T vertex1, T vertex2) {
        List<T> neighbors1 = adjList.get(vertex1);
        List<T> neighbors2 = adjList.get(vertex2);
        if (neighbors1 != null) {
            neighbors1.remove(vertex2); // הסר את vertex2 מרשימת השכנים של vertex1
        }
        if (neighbors2 != null) {
            neighbors2.remove(vertex1); // הסר את vertex1 מרשימת השכנים של vertex2
        }
    }

    // מסירה צומת מהגרף ומסירה את כל הקשרים אליו
    public void removeVertex(T vertex) {
        adjList.values().forEach(neighbors -> neighbors.remove(vertex)); // הסר את vertex מכל רשימות השכנים
        adjList.remove(vertex); // הסר את הצומת עצמו מהגרף
    }

    // מחזירה את רשימת השכנות של צומת נתון
    public List<T> getNeighbors(T vertex) {
        return adjList.getOrDefault(vertex, new ArrayList<>()); // מחזיר רשימה ריקה אם הצומת לא נמצא
    }

    // בודקת אם יש קשר בין שני צמתים
    public boolean hasEdge(T vertex1, T vertex2) {
        return adjList.containsKey(vertex1) && adjList.get(vertex1).contains(vertex2); // אם vertex1 קיים ויש לו קשר עם vertex2
    }

    // מדפיס את הגרף
    public void printGraph() {
        // לולאת while על המפת השכנים
        Iterator<Map.Entry<T, List<T>>> iterator = adjList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<T, List<T>> entry = iterator.next();
            T vertex = entry.getKey(); // הצומת הנוכחי
            List<T> neighbors = entry.getValue(); // רשימת השכנים
            System.out.print(vertex + ": ");
            // לולאת while להדפסת השכנים
            Iterator<T> neighborIterator = neighbors.iterator();
            while (neighborIterator.hasNext()) {
                System.out.print(neighborIterator.next() + " ");
            }
            System.out.println();
        }
    }

    // חיפוש BFS על הגרף
    public void bfs(T start) {
        Set<T> visited = new HashSet<>(); // שמירה על הצמתים שביקרנו בהם
        Queue<T> queue = new LinkedList<>(); // תור עבור BFS

        queue.offer(start); // הוסף את הצומת ההתחלתי לתור
        visited.add(start); // סימן את הצומת כביקור

        while (!queue.isEmpty()) { // כל עוד יש תור
            T vertex = queue.poll(); // שלוף את הצומת הראשון בתור
            System.out.print(vertex + " "); // הדפס את הצומת

            // עבור על השכנים של הצומת הנוכחי
            Iterator<T> neighborIterator = getNeighbors(vertex).iterator();
            while (neighborIterator.hasNext()) {
                T neighbor = neighborIterator.next();
                if (!visited.contains(neighbor)) { // אם השכן לא ביקר בו
                    visited.add(neighbor); // סימן אותו כביקור
                    queue.offer(neighbor); // הוסף אותו לתור
                }
            }
        }
        System.out.println();
    }
}
