package org.example;
import java.util.Arrays;

public class TrickTruck {
    //מערך שמות נהגים
    private Node<String> drivers;
    //מערך לוחיות רישוי
    private Node<Integer> cars;
    //מערך קבלות עבור חודש מסויים
    private Node<Payment> [] payments;

    public TrickTruck(Node<String> drivers, Node<Integer> cars, Node<Payment>[] payments) {
        this.drivers = null;
        this.cars = null;
        this.payments = null;
    }

    public Node<String> getDrivers() {
        return drivers;
    }

    public void setDrivers(Node<String> drivers) {
        this.drivers = drivers;
    }

    public Node<Integer> getCars() {
        return cars;
    }

    public void setCars(Node<Integer> cars) {
        this.cars = cars;
    }

    public Node<Payment>[] getPayments() {
        return payments;
    }

    public void setPayments(Node<Payment>[] payments) {
        this.payments = payments;
    }

    @Override
    public String toString() {
        return "TrickTruck{" +
                "drivers=" + drivers +
                ", cars=" + cars +
                ", payments=" + Arrays.toString(payments) +
                '}';
    }

    //פונקציה המחזירה אם נהג נהג החודש או לא
    private boolean existDriver(String name){
        // איטרציה על מערך הימים אשר כל יום מפנה לרשימת חשבוניות
        for (var payment : this.payments) {
            var pos = payment; // פויינטר למעבר על רשימה ביום נוכחי

            // איטרציה על הרשימה
            while (pos != null) {
                // אם שם הנהג נמצא, נחזיר אמת
                if (pos.getValue().getDriverName().equals(name)) { return true; }
                pos = pos.getNext(); // נתקדם לחולייה הבאה - לקבלה הבאה
            }
        }

        return false; // יוחזר שקר משמע נהג לא עבד בחודש יוני
    }

    //פונק המחזירה אם רכב עבד או לא
    private boolean existCar(int num){
        // איטרציה על מערך הימים אשר כל יום מפנה לרשימת חשבוניות
        for (Node<Payment> payment : this.payments) {
            Node<Payment> pos = payment;  // פויינטר למעבר על רשימה ביום נוכחי

            // איטרציה על הרשימה
            while (pos != null) {
                if (pos.getValue().getPlateNumber() == num) { return true; }// אם מס לוחית רישוי נמצא, נחזיר אמת
                pos = pos.getNext();  // נתקדם לחולייה הבאה - לקבלה הבאה
            }
        }

        return false; //יוחזר שקר רכב זה לא עבד במהלך חודש יוני
    }

    //הפעולה מחזירה אמת אם נהג הגיש קבלת תשלום ביום DAY
    private boolean worked(String name, int day){
        if (day < 1 || day > 31){ return false; } // אם היום בחודש שנמסר אינו תקין יוחזר שקר

        var pos = this.payments[day]; // פויינטר לרשימת הקבלות ביום מסויים בחודש

        // איטרציה על הרשימה ביום זה כדי לבדוק אם נהג עבד ביום זה
        while (pos != null){
            if (pos.getValue().getDriverName().equals(name)){ return true; } // אם נמצא שמו של הנהג יוחזר אמת
            pos = pos.getNext(); // התקדמות לחולייה הבאה - לקבלה הבאה
        }

        return false; // החזר שקר, הנהג ששמו התקבל בפונקציה כפרמטר לא עבד ביום זה
    }

    //פעולה המוסיפה חשבונית למערך החשבוניות
    public boolean addPayment(int day, int plate, String name, double fuel){
        // אם לוחית רישוי לא קיימת, ושם הנהג גם לא קיים
        if (!existCar(plate) || !existDriver(name)){ return false; }
        // יצירת קבלה חדשה להוספה
        this.payments[day] = new Node<>(new Payment(day,plate,name,fuel),this.payments[day]);
        return true;// יוחזר אמת קבלה נוספה בהצלחה

    }

    //כמות הדלק הכללית שתדלקו ברכב מסויים
    public double totalFuel(int num){
        var total = 0.0; // צובר כמות דלק כללית

        // איטרציה על מערך הקבלות
        for (var pos : this.payments) {

            // איטרציה על רשימת קבלות ביום מסויים
            while (pos != null) {
                var temp = pos.getValue(); //שמירת קבלה נוכחית
                var plate = temp.getPlateNumber(); //שמירת לוחית רישוי

                //אם לוחית רישוי נוכחית זהה ללוחית רישוי שהתקבלה בפונקציה
                if (plate == num) {
                    var fuel = temp.getHowMuch(); //שמירת כמות הדלק שתודלקה ברכב בעל לוחית רישוי זו
                    total += fuel; //צבירת כמות הדלק לצובר
                }

                pos = pos.getNext(); //התקדמות לחוליה הבאה - לקבלה הבאה
            }
        }

        return total; //החזרת כמות הדלק הכללית שתודלקה פר לוחית רישוי
    }

    //פונקציה עוטפת הקוראת לפונקצית למבדה שמדפיסה עבור כל נהג את מס הימים שעבורם הוא הציג קבלות
    public void printWorkDays(){ printWorkDays.run(); }

    //פונקציית הלמבדה שמדפיסה עבור כל נהג את מס הימים שעבורם הוא הציג קבלות
    private final Runnable printWorkDays = () -> {
        var pos = this.drivers; //פויינטר לרשימת שמות הנהגים

        //מעבר על רשימת שמות הנהגים
        while (pos != null) {
            var tName = pos.getValue(); //שמירת שם נהג
            var count = 0; // מונה ימי עבודה

            // איטרציה על מערך הקבלות
            for (var payment : this.payments) {
                var current = payment; //פויינטר לרשימת הקבלת בתא נוכחי

                //מעבר על רשימת הקבלות ביום נוכחי
                while (current != null) {
                    var temp = current.getValue(); //קבלת קבלה נוכחית
                    var driverName = temp.getDriverName(); //שמירת שם נהג נוכחי

                    //בדיקה אם שם נהג זה מופיע בקבלה נוכחית זו
                    if (driverName.equals(tName)) {
                        count++; // קידום מונה ימי עבודה
                        break; //מעבר ליום הבאה אין צורך להמשיך לבדוק עבור יום זה
                    }

                    current = current.getNext(); //מעבר לקבלה הבאה
                }
            }

            System.out.println("Driver: "+ tName +"fuel his car for "+ count + "days");
            pos = pos.getNext(); //מעבר לשם נהג הבא
        }

        System.out.println("end!");
    };
}
