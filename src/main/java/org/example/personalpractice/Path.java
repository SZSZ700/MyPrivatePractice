package org.example.personalpractice;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Function;

// מחלקה המייצגת מסלול
public class Path {
    // מזהה ייחודי של הכביש (כדי שנוכל למחוק/לעדכן בעתיד)
    private int roadId;

    // תור של קורדינטות (המסלול)
    private Queue<Double> route;

    //מרחק אווירי
    private double airDistance;

    // מרחק בפועל עם כל הקפיצות (נחשב בעת יצירה)
    private double realDistance;

    // כמות הקפיצות במסלול (נחשב בעת יצירה)
    private int jumps;

    // פעולה בונה שמחשבת את השדות הפנימיים
    public Path(int roadId, Queue<Double> route) {
        //אתחול מזהה יחודי
        this.roadId = roadId;
        // העתקה פנימית של המסלול
        this.route = new LinkedList<>(route);
        // אתחול המרחק היבשתי
        calcRealDistance.run();
        //אתחול מרחק אווירי
        qdis.run();
        //אתחול מס הקפיצות
        this.jumps = this.route.size();
    }

    //פונקציה שמחזירה את המרחק האווירי בין התחלה לסוף
    private final Runnable qdis = () -> {
        var temp = new LinkedList<Double>(); // תור עזר לשיחזור
        var start = this.route.peek(); // האיבר הראשון
        var end = 0.0; // יכיל את האיבר האחרון

        //איטרציה על התור
        while (!this.route.isEmpty()) {
            end = this.route.poll(); // שומר את האיבר האחרון
            temp.offer(end); // הכנסה לתור עזר
        }

        // שחזור תור מקור
        while (!temp.isEmpty()) { this.route.offer(temp.poll()); }

        //מרחק אווירי בין התחלה לסוף
        this.airDistance =  Math.abs(end - start);
    };

    //פונקציה שמחזירה את המרחק האמיתי
    private final Runnable calcRealDistance = () -> {
        //תור עזר לשחזור תור מקור
        var temp = new LinkedList<Double>();
        // סכום כל הקפיצות, כלומר המרחק בפועל עם הקפיצות
        var sum = 0.0;

        // קורדינצייה ראשונה
        var first = this.route.poll();
        // הכנסתה לתור עזר
        temp.offer(first);

        while (!this.route.isEmpty()) {
            // שמירת קורדינציה נוכחית
            var next = this.route.poll();
            // סכום המרחק בין זוגות קורדינציות עוקבות
            sum += Math.abs(next - first);
            //הכנסתה לתור עזר לשיחזור
            temp.offer(next);
            //יקבל קורדינציה הבאה העוקבת המחוברת לקורדינציה נוכחית
            first = next;
        }

        // שחזור תור מקור
        while (!temp.isEmpty()) { this.route.offer(temp.poll()); }

        //קביעת מרחק בפועל
        this.realDistance = sum;
    };

    public int getRoadId() {
        return roadId;
    }

    public void setRoadId(int roadId) {
        this.roadId = roadId;
    }

    public Queue<Double> getRoute() {
        return route;
    }

    public void setRoute(Queue<Double> route) {
        this.route = route;
    }

    public double getRealDistance() {
        return realDistance;
    }

    public void setRealDistance(double realDistance) {
        this.realDistance = realDistance;
    }

    public int getJumps() {
        return jumps;
    }

    public void setJumps(int jumps) {
        this.jumps = jumps;
    }
}
