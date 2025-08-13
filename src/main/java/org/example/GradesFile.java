package org.example;
import java.util.Arrays;

public class GradesFile {
    private Node<StudentG> [] grades;

    public GradesFile(Node<StudentG>[] grades) {
        this.grades = grades;
    }

    public GradesFile() {
        this.grades = null;
    }

    public Node<StudentG>[] getGrades() {
        return grades;
    }

    public void setGrades(Node<StudentG>[] grades) {
        this.grades = grades;
    }

    @Override
    public String toString() {
        return "GradesFile{" +
                "grades=" + Arrays.toString(grades) +
                '}';
    }

    //החזרת הסטודנט הראשון באוסף
    public StudentG getStudent(int k){
        if (!isEmpty(k)){
            //פויינטר לתא הנוכחי במערך
            Node<StudentG> temp = this.grades[k];
            //שליפת הערך מהפויינטר(סטודנט)
            StudentG tem = temp.getValue();
            //החזרת הסטודנט
            return tem;
        }
        return null;
    }

    //פעולה מחזירה אמת אם אוסף הסטודנטים במקום הקיי ריק
    public boolean isEmpty(int k){
        return k < 0 || k >= grades.length || grades[k] == null;
    }

    //מחזירה אמת אם כל הסטודנטים הנמצאים באוסף במקום ה K מתאים למקום זה לפי המזהה שלהם
    public boolean listIsGood(int k){
        if (k < 0 || k > this.grades.length){
            return true;
        }

        Node<StudentG> pos = this.grades[k];

        while (pos != null){
            if (pos.getValue().getCode() != k){
                return false;
            }
            pos = pos.getNext();
        }
        return true;
    }

    public void moveStudent(int k, int j){
        if (k >= 0 && k <= this.grades.length && j >= 0 && j <= this.grades.length){

            //קבלת התלמיד הראשון במיקום הקיי
            StudentG first = getStudent(k);
            //מחיקת סטודנט ממקום זה
            Node<StudentG> temp = this.grades[k];
            temp = temp.getNext();
            this.grades[k] = temp;//הפנית התא הנוכחי לתלמיד השני בשרשרת

            if (grades[j] == null){
                grades[j] = new Node<>(first);
            }else {
                //העברתו להיות סטודנט אחרון בשרשרת במיקום הגי
                Node<StudentG> tail = this.grades[j];
                //find the tail of list
                while (tail.getNext() != null) {
                    tail = tail.getNext();
                }
                tail.setNext(new Node<>(first));
            }
        }
    }

}
