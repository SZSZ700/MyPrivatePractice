package org.example;
import java.util.Arrays;

public class TeachingStaff {
    //מערך של כל אנשי הצוות. במכללה יכולים לעבוד לא יותר מ - 200 אנשי צוות
    private Employee [] employees;
    private int current; // מס אנשי צוות בפועל

    public TeachingStaff(Employee[] employees, int current) {
        this.employees = employees;
        this.current = current;
    }

    public Employee[] getEmployees() {
        return employees;
    }

    public void setEmployees(Employee[] employees) {
        this.employees = employees;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    @Override
    public String toString() {
        return "TeachingStaff{" +
                "employees=" + Arrays.toString(employees) +
                ", current=" + current +
                '}';
    }

    // כמה צוותים יש במכללה
    public int howManyGroups(){
        // מונה ראשי צוות
        var count = 0;

        // איטרציה על מערך העובדים במכללה
        for (var temp : this.employees){
            // אם עובד זה הינו ראש צוות נקדם מונה באחד
            if (temp instanceof Head){
                count++;
            }
        }

        // החזרת מס צוותים, הרי שכמות ראשי הצוות מעידה על כמות הצוותים
        return count;
    }

    public Tutor lastNeweyTutorTeachinSpecificCourse(int cnum){
        // יכיל מתרגל האחרון שנכנס למערכת ומלמד בקורס מסויים
        Tutor tutor = null;
        // יכיל מס עובד של המתרגל האחרון שנכנס למערכת
        var max = Integer.MIN_VALUE;

        // מעבכר על מערך העובדים
        for (var temp : this.employees){

            // אם עובד זה אכן מתרגל
            if (temp instanceof Tutor){

                // מתרגל נוכחי
                Tutor tempTutor = ((Tutor) temp);

                if (tempTutor.getCourseNum() == cnum){
                    // מס עובד של מתרגל נוכחי זה
                    int workerNum = tempTutor.getWorkerNumber();

                    // אם מס עובד של מתרגל זה יותר גדול
                    // ממס העובד, השמור במשתנה המכיל את המס של המתרגל שנכנס אחרון ומלמד בקורס מסויים
                    if (workerNum > max){
                        // שמירת מס העובד שלו
                        max = workerNum;
                        // שמירת מתרגל זה
                        tutor = tempTutor;
                    }
                }
            }
        }

        return tutor;
    }
}
