package org.example;

public class AdvertHour {
    private Node<Advert> chain;
    public static final int MAX_ADS = 15;
    public static final int MAX_TIME_PER_ALL_ADS = 5 * 60;

    public AdvertHour(Node<Advert> chain) {
        this.chain = chain;
    }

    public Node<Advert> getChain() {
        return chain;
    }

    public void setChain(Node<Advert> chain) {
        this.chain = chain;
    }

    @Override
    public String toString() {
        return "AdvertHour{" +
                "chain=" + chain +
                '}';
    }

    //מתודה המחשבת את הזמן הפנוי הנותר כדי לדעת אם ניתן להוסיף פרסומת
    public int freeTime(){
        //פויינטר למעבר על השרשרת
        Node<Advert> pos = this.chain;
        //משתנה שיצבור את כמות השניות שהפרסומות תפסו כבר, אסור לעקוף את ה 5 דקות כמובן
        int sumSec = 0;

        //איטרציה על השרשרת פרסומות
        while (pos != null){

            //שצירת פרסומת נוכחית
            Advert temp = pos.getValue();
            //קבלת אורך פרסומת נוכחית בשניות
            int currentLen = temp.getLength();
            //צבירת אורך הפרסומת (בשניות) למשתנה צובר
            sumSec += currentLen;

            //מעבר לפרסומת הבאה ברשימה
            pos = pos.getNext();
        }

        //החזרת הזמן שנותר לפרסומת נוספת
        return Math.max(AdvertHour.MAX_TIME_PER_ALL_ADS - sumSec, 0);
    }

    //מתודת עזר לחישוב כמה פרסומות יש כבר ברשימה
    private int sizeOfAdvList(){
        //פויינטר לשרשרת הראש
        Node<Advert> pos = this.chain;
        //מונה מס פרסומות ברשימה
        int count = 0;

        //איטרציה על הרשימה
        while (pos != null){
            //מנייה חוליה נוכחית
            count++;
            //התקדמות לחוליה הבאה
            pos = pos.getNext();
        }

        //החזרת מונה
        return count;
    }

    //מתודה הבודקת אם זה אפשרי בכלל להוסיף פרסומת
    public boolean isPossible(Advert adv){
        //אם הזמן הנותר הוא 0 אז מן הסתם שלא ניתן להוסיף פרסומת ולכן נחזיר שקר
        if (this.freeTime() == 0){ return false; }

        //אם הגענו למגבלת ה 15 פרסומות לשעה נחזיר שקר
        if (this.sizeOfAdvList() == AdvertHour.MAX_ADS || this.sizeOfAdvList() > AdvertHour.MAX_ADS){ return false; }

        //אם מס הזמן הפנוי + זמן הפרסומת להוספה, חורגת ממס הדקות המקסימלי המותר בשעת שידור אחת, נחזיר שקר
        if (this.freeTime() + adv.getLength() > AdvertHour.MAX_TIME_PER_ALL_ADS){ return false; }

        //אחרת נחזיר אמת, משמע ניתן להוסיף פרסומת
        return true;
    }

    //מתודה המוסיפה פרסומת לרשימת פרסומות בשעת שידור
    public boolean addAdvert(Advert adv){
        //בדיקה אם בכלל ניתן להוסיף פרסומת
        if (!isPossible(adv)){ return false; }

        //פויינטר למציאת סוף רשימת הפרסומות
        Node<Advert> tail = this.chain;

        //איטרציה עד הגעה לזנב
        while (tail.getNext() != null){
            tail = tail.getNext();
        }

        //הוספת פרסומת חדשה לסוף הרשימה
        tail.setNext(new Node<>(adv));

        //פרסומת נוספה בהצלחה
        return true;
    }
}
