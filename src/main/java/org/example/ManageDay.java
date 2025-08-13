package org.example;
import java.util.Arrays;

public class ManageDay {
    private AdvertHour [] advHrr;

    public ManageDay(AdvertHour[] advHrr) {
        this.advHrr = advHrr;
    }

    public AdvertHour[] getAdvHrr() {
        return advHrr;
    }

    public void setAdvHrr(AdvertHour[] advHrr) {
        this.advHrr = advHrr;
    }

    @Override
    public String toString() {
        return "ManageDay{" +
                "advHrr=" + Arrays.toString(advHrr) +
                '}';
    }

    //מתודה לחישוב רווח משידורי פרסומות ביום
    public double totalMax(){
        //צובר מחירים עבור שידור פרסומות - רווח בפועל
        double sum = 0;

        //איטרציה על מערך שעות שידור
        for (AdvertHour temp : this.advHrr) {
            //קבלת רשימת פרסומת בשעת שידור נוכחית, וקישור פויינטר לאיטרציה עלייה
            Node<Advert> pos = temp.getChain();

            //איטרציה על רשימת הפרסומות
            while (pos != null) {
                //צבירת מחיר פרסומת נוכחית
                sum += pos.getValue().getPrice();
                //מעבר לפרסומת הבאה ברשימה הפרסומות בשעת השידור הנוכחית
                pos = pos.getNext();
            }
        }

        //החזרת הרווח הכולל ביום שידור מלא כהלכתו
        return sum;
    }
}
