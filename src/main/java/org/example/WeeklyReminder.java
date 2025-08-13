package org.example;
import java.util.Arrays;

public class WeeklyReminder {
    private DailyReminder [] arr = new DailyReminder[7];

    public WeeklyReminder(DailyReminder[] arr) {
        this.arr = arr;
    }

    public DailyReminder[] getArr() {
        return arr;
    }

    public void setArr(DailyReminder[] arr) {
        this.arr = arr;
    }

    @Override
    public String toString() {
        return "weeklyReminder{" +
                "arr=" + Arrays.toString(arr) +
                '}';
    }

    //הוספת תזכורת חדשה לרשימת התזכורות ביום מסויים
    //O(1)
    public void addReminder(String cust, String tel, String inst, String date, int hour, int dayReminder){
        //יצירת תזכורת חדשה
        Reminder r1 = new Reminder(cust, tel, inst, date, hour);

        //אם רשימת התזכורות ריקה ביום הספציפי
        if (this.arr[dayReminder].getChain() == null){
            //נוסיף חוליית תזכורת חדשה בראש הרשימה
            this.arr[dayReminder].setChain(new Node<>(r1));
            //יציאה מהפונקציה
            return;
        }else {
            //אחרת אם רשימת התזכורות לא ריקה ביום הספציפי
            //ניצור חוליית תזכורת חדשה
            Node<Reminder> toAdd = new Node<>(r1);
            //נוסיף אותה בתחילת הרשימה
            toAdd.setNext(this.arr[dayReminder].getChain());
            //הפויינטר של ראש הרשימה יצביע על חולייה זו
            this.arr[dayReminder].setChain(toAdd);
            //יציאה מהפונקציה
            return;
        }
    }

    //O(n) - עדכון תזכורת עם סטטוס מענה המטופל, או הוספתה לאינדקס 0 במערך ימי-תזכורת לשם יצירת קשר בשבוע הבא
    public void updateReminder(String cust, String inst, int dayReminder, int answer){
        //אם הלקוח לא ענה יעבור לרשימת התזכורות לשבוע הבא
        if (answer == 0){
            //פוינטר לרשימת התזכורות ביום מסויים
            Node<Reminder> pos = this.arr[dayReminder].getChain();

            //אם הלקוח להסרה נמצא בהתחלה
            if (pos.getValue().getCust().equals(cust) && pos.getValue().getInst().equals(inst)){
                //הסרתו מתחילת הרשימת תזכורות ביום הנוכחי והעברתו לתא באינדקס 0
                this.arr[dayReminder].setChain(pos.getNext());

                //ההעברה לתא במיקום 0
                //פויינטר לרשימה בתא 0 במערך ימים
                Node<Reminder> nex = this.arr[0].getChain();

                //אם הרשימה ריקה באותו תא
                //נוסיף תזכורת דרושה לתחילת הרשימה כך שהפויינטר לרשימה יצביע עלייה
                if (nex == null){
                    this.arr[0].setChain(pos);
                    //יציאה מהפונקצייה
                    return;
                } else {
                    //אם הרשימה לא ריקה באותו תא
                    //נוסיף תזכורת דרושה לתחילת הרשימה כך שהפויינטר לרשימה יצביע עלייה
                    pos.setNext(nex);
                    this.arr[0].setChain(pos);
                    //יציאה מהפונקצייה
                    return;
                }
            }

            //עבור כל מיקרה אחר
            //יצביע על החולית תזכורת להסרה
            Node<Reminder> toMove = new Node<>(null);

            //הסרה מכל מקום אחר ברשימת התזכורות ביום הנוכחי
            while (pos.getNext() != null){
                //אם החולייה תזכורת הבאה היא החולייה תזכורת להסרה
                if (pos.getNext().getValue().getCust().equals(cust) && pos.getNext().getValue().getInst().equals(inst)){
                    //חולייה חדשה שתכיל את התזכורת להסרה
                    toMove.setValue(pos.getNext().getValue());
                    //הסרה בפועל מהרשימת תזכורות ביום הנוכחי
                    pos.setNext(pos.getNext());
                    //יציאה מהלולאה הסרה בוצעה בהצלחה
                    break;
                }

                //התקדמות לחוליית תזכורת הבאה
                pos = pos.getNext();
            }

            //אם ההסרה אכן בוצעה כלומר החוליית עזר החדשה מכילה את התזכורת שהוסרה
            if (toMove.getValue() != null){
                //ההעברה לתא במיקום 0
                //פויינטר לרשימה בתא 0 במערך ימים
                Node<Reminder> nex = this.arr[0].getChain();

                //אם הרשימה ריקה באותו תא
                //נוסיף תזכורת דרושה לתחילת הרשימה כך שהפויינטר לרשימה יצביע עלייה
                if (nex == null){
                    this.arr[0].setChain(toMove);
                    //יציאה מהפונקצייה
                    return;
                } else {
                    //אם הרשימה לא ריקה באותו תא
                    //נוסיף תזכורת דרושה לתחילת הרשימה כך שהפויינטר לרשימה יצביע עלייה
                    toMove.setNext(nex);
                    this.arr[0].setChain(toMove);
                    //יציאה מהפונקצייה
                    return;
                }
            }

        } else if (answer == 1 || answer == 2) {
            //אם הלקוח ענה או אישר או ביטל
            //פוינטר לרשימת התזכורות ביום מסויים
            Node<Reminder> pos = this.arr[dayReminder].getChain();

            //איטרציה על הרשימה
            while (pos != null){
                //תזכורת נוכחית
                Reminder current = pos.getValue();
                //שם מטופל
                String currentName = current.getCust();
                //שם המכון
                String currentInst = current.getInst();

                //אם נמצא המטופל המבוקש נשנה את סטטוס התזכורת בהתאם (אישר תור-1, ביטל-2, לא ענה-0)
                if (currentName.equals(cust) && currentInst.equals(inst)){
                    current.setStatus(answer);
                    return;
                }

                //נתקדם לחוליית תזכורת הבאה
                pos = pos.getNext();
            }
        }else {
            return;
        }
    }

    //פעולה המקבלת מערך מכונים של המרכז הרפואי "כל
    //הבריאות". הפעולה תדפיס עבור כל מכון את פרטי התורים שהתפנו עקב ביטול מצד המטופל
    //O(n)^2
    public void printCancelledAppointments(String[] clinics) {
        // עבור כל מכון ברשימה
        for (String inst : clinics) {
            System.out.println("Cancelled appointments in " + inst + ":");

            //מעבר על כל ימות השבוע
            for (int i = 1; i < this.arr.length; i++) {

                Node<Reminder> pos = this.arr[i].getChain();
                while (pos != null) {
                    Reminder r = pos.getValue();
                    if (r.getInst().equals(inst) && r.getStatus() == 2) {
                        System.out.println(r);
                    }
                    pos = pos.getNext();
                }
            }
            System.out.println();
        }
    }
}
