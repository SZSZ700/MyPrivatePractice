package org.example;
import java.util.HashSet;
import java.util.Set;

// מחלקה עוטפת לסט
public class SetOfNumbers {
    private HashSet<Integer> myset; // תכונת סט פרטית

    // בנאי
    public SetOfNumbers() {
        this.myset = new HashSet<>();
    }

    // האם סט ריק ?
    public boolean isEmpty(){
        return myset.size() == 0;
    }

    // הוספה לסט
    public void addToSet(int num){
        myset.add(num);
    }

    // הסרת ערך רנדומלי מסט
    public int removeRandom(){
        var removedNum = 0; // שמירת הערך שהוסר

        // איטרציה על הסט
        for (Integer temp : this.myset){
            removedNum = temp; // שמירת מס נוכחי
            myset.remove(temp); // הסרתו מהסט
            break; // עצירה, מכיוון ואני רוצה להסיר איבר אחד ולא את כל איברי הסט
        }

        return removedNum; // החזרת הערך שהוסר
    }

    // גודל סט
    public int sizeOfSet(){
        var temp = new SetOfNumbers(); // סט אחר לשיחזור
        var count = 0; // מונה  לאורך

        // איטרציה על הסט הנוכחי
        while (!this.isEmpty()){
            var num = removeRandom(); // שליפת ערך רנדומלי
            count++; // מניית איבר זה
            temp.addToSet(num); // הוספתו לסט לשיחזור
        }

        // שיחזור סט נוכחי
        while (!temp.isEmpty()){
            // שליפת ערך והכנסתו לסט זה
            this.addToSet(temp.removeRandom());
        }

        return count; // החזרת אורך הסט
    }

    public int removeMin(){
        var min = Integer.MAX_VALUE; // מציאת המינימום
        var temp = new SetOfNumbers(); // סט אחר לשיחזור

        // חיפוש מינימום
        while (!this.isEmpty()){
            var num = this.removeRandom(); // שליפת ערך רנדומלי מהסט הנוכחי
            min = num < min ? num : min; // שמירת ערך מקסימלי
            temp.addToSet(num); // הוספת הערך שנשלף לסט שיחזורים
        }

        // שיחזור סט מקור
        while (!temp.isEmpty()){ this.addToSet(temp.removeRandom()); }

        this.myset.remove(min); // הסרת ערך מינימלי מהסט הנוכחי

        return min; // החזרת הערך המינימלי שהוסר בהצלחה
    }

    public static boolean bigger(SetOfNumbers sn1,SetOfNumbers sn2){
        var temp1 = new SetOfNumbers(); // סט אחר לשיחזור סט ראשון
        var temp2 = new SetOfNumbers(); // סט אחר לשיחזור סט שני
        var notValid = false;

        while (!sn1.isEmpty()){
            // שליפת ערך מסט 1
            var current  = sn1.removeRandom();
            temp1.addToSet(current); // הוספת הערך לסט שיחזור

            while (!sn2.isEmpty()){
                // שליפת ערך מסט 2
                var nextNum = sn2.removeRandom();
                // אם ערך אחד לפחות מסט 1 קטן מערך מסט 2 , סט 1 לא תקין, לבסוף יוחזר שקר
                // כעת לא ניתן להחזיר שקר מכיוון ויש לשחזר סט מקור
                if (current <= nextNum){ notValid = true; }
                temp2.addToSet(nextNum); // הוספת הערך לסט שיחזור
            }

            // שיחזור סט 2
            while (!temp2.isEmpty()){ sn2.addToSet(temp2.removeRandom()); }
        }

        //שיחזור סט 1
        while (!temp1.isEmpty()){ sn1.addToSet(temp1.removeRandom()); }

        return notValid ? false : true; // החזרה תגובה אם סט 1 עונה על התנאים
    }

}
