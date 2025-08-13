package org.example;
import java.util.Arrays;

public class EmergencyCenter {
    //מערך של רשימות הקריאות לפי סוג אירוע החירום
    //התא 0 מיועד לרשימת קריאות שהטיפול בהן הסתיים
    private EmergencyList [] arr = new EmergencyList [6];

    public EmergencyCenter(EmergencyList[] arr) {
        this.arr = arr;
    }

    public EmergencyList[] getArr() {
        return arr;
    }

    public void setArr(EmergencyList[] arr) {
        this.arr = arr;
    }

    @Override
    public String toString() {
        return "EmergencyCenter{" +
                "arr=" + Arrays.toString(arr) +
                '}';
    }

    //O(n)
    public void addCall(int type, String location, String description, String callerName, int priority){
        //יצירת אובייקט קריאה לעזרה
        Call caller = new Call(type, location, description, callerName, priority);

        //יצירת חולייה המפנה לאובייקט קריאה לעזרה
        Node<Call> callerToAdd = new Node<>(caller);

        //איטרציה על מערך רשימות קריאות
        for (int i = 0; i < this.arr.length; i++) {
            //פןיינטר לרשימה בתא נוכחי
            Node<Call> pos = this.arr[i].getCalls();

            //הוספת חוליית קריאה לרשימה במיקום הנוכחי
            if (pos.getValue().getType() == type){
                //חיבור החולייה החדשה לראש
                callerToAdd.setNext(this.arr[i].getCalls());
                //שינוי פוינטר הרשימה לחולייה החדשה
                this.arr[i].setCalls(callerToAdd);
                return;
            }
        }
    }

    public Call mostUrgentCall(int type){
        //משתנה לשמירת רמת הדחיפות הגבוה ביותר עבור קריאה מהסוג שהתקבל בפונק כפרמטר
        int maxPriority = Integer.MAX_VALUE;
        //הפנייה שתשמור את הפנייה לאובייקט הראשון של "קריאה לעזרה" שדחיפותו הכי גבוהה
        Call finalToRemove = null;

        //איטרציה על מערך רשימות קריאות
        for (int i = 1; i < this.arr.length; i++) {
            //פןיינטר לרשימה בתא נוכחי
            Node<Call> pos = this.arr[i].getCalls();

            //איטרציה על רשימת הקריאות בתא נוכחי
            while (pos != null){
                //שמירת הפניה לאובייקט "קריאה לעזרה"
                Call tempCaller = pos.getValue();
                //שמירת רמת הדחיפות של קריאה נוכחית
                int tempPriority = tempCaller.getPriority();

                if (tempPriority < maxPriority){
                    //עדכון משתנה רמת הדחיפות עבור קריאה לעזרה
                    maxPriority = tempPriority;
                    //עדכון ההפנייה לאובייקט "קריאה לעזרה" בעל הדחיפות הגבוהה ביותר
                    finalToRemove = tempCaller;
                }

                //מעבר לחולייה הבאה ברשימת הקריאות לעזרה
                pos = pos.getNext();
            }
        }

        //מחיקת הקריאה מרשימת הקריאות
        //איטרציה על מערך רשימות קריאות
        for (int i = 1; i < this.arr.length; i++) {
            //פןיינטר לרשימה בתא נוכחי
            Node<Call> pos = this.arr[i].getCalls();

            //מקצר את הסיבוכיות זמן ריצה הפנימית ללולאה זו לO(n)
            if (pos.getValue().getType() == finalToRemove.getType()) {

                //איטרציה על רשימת הקריאות בתא נוכחי

                //הסרה מההתחלה
                if (pos.getValue().equals(finalToRemove)) {
                    //הפויינטר לרשימה בתא נוכחי יצביע על החולייה הבאה
                    this.arr[i].setCalls(this.arr[i].getCalls().getNext());
                    //החולייה הוסרה בהצלחה יציאה מלולאת ה FOR
                    break;
                }

                //משתנה בוליאני לשמירה אם חולייה הוסרה בהצלחה
                boolean deleted = false;

                //הסרה מהאמצע ומהסוף
                while (pos.getNext() != null) {
                    //אם החולייה הבאה היא להסרה
                    if (pos.getNext().getValue().equals(finalToRemove)) {
                        //נדלג על חולייה זו ובכך נסיר אותה
                        pos.setNext(pos.getNext().getNext());
                        //הוסרה בהצלחה
                        deleted = true;
                        //יציאה מהלולאה - יעול, אין סיבה להמשיך לרוץ על הרשימה אחרי שהחוליה הוסרה
                        break;
                    }

                    //התקמות לחולייה הבאה
                    pos = pos.getNext();
                }

                //אם החולייה הוסרה אין סיבה להמשיך באיטרציה על המאגר
                if (deleted) {
                    break;
                }
            }
        }

        //העברתו לתא במיקום 0
        //יצירת חולייה חדשה להוספה
        Node<Call> toAdd = new Node<>(finalToRemove);
        //חיבור חולייה חדשה לראש הרשימה התא 0
        toAdd.setNext(this.arr[0].getCalls());
        //ראש הרשימה בתא 0 יצביע על חולייה חדשה זו
        this.arr[0].setCalls(toAdd);
        toAdd.getValue().setPriority(0);

        //החזרת הקריאה שהוסרה
        return finalToRemove;
    }

    public void printPerLoc(String location){
        //מערך שמות עבור אירועי מצבי חירום
        String [] typesOfErg = new String[]{
                "Fire",
                "Traffic Accident",
                "Rescue Operation",
                "Criminal Incident",
                "Security Threat"
        };

        //מערך שימנה כמה קריאות יש במיקום מסויים מכל סוג אירוע חירום
        int [] emrgMonim = new int[5];

        //מעבר על מערך רשימות קריאות לעזרה
        for (EmergencyList olst : this.arr) {

            //פויינטר לרשימת קריאות הנמצאת באובייקט רשימת קריאות
            Node<Call> pos = olst.getCalls();

            //איטרציה על הרשימה
            while (pos != null) {
                //אובייקט "קריאה לעזרה"
                Call temp = pos.getValue();
                //סוג קריאה
                int type = temp.getType();
                //מיקום
                String tempLocation = temp.getLocation();

                if (tempLocation.equals(location)) { emrgMonim[type -1]++; }

                //מעבר לקריאה הבאה
                pos = pos.getNext();
            }

        }

        System.out.println("number of calls per emergency type: ");

        for (int i = 0; i < typesOfErg.length; i++) {
            //סוג מצב חירום
            String type = typesOfErg[i];
            //כמה קריאות היו לסוג מצב חירום זה
            int howMany = emrgMonim[i];
            //הדפסה למשתשמ
            System.out.println("emergency type: " + type + ", num of calls: " + howMany);
        }
    }
}
