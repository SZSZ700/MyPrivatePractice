package org.example;
import java.util.ArrayList;
import java.util.List;

// מחלקה שמייצגת את החנות
public class Store {
    // רשימת כל הקניות שבוצעו
    private Node<Buy> buys;

    // בנאי שמאתחל את רשימת הקניות
    public Store() { buys = null; }

    // הוספת קנייה חדשה
    public void addBuy(Buy b) { this.buys = new Node<>(b, this.buys.getNext()); }

    // פעולה שמחזירה מספר הקניות שבוצעו
    public int getBuyCount() {
        var count = 0; // מונה מס רכישות
        var pos = this.buys;   // פויינטר לרשימת הקניות

        // איטרציה על הרשימה
        while (pos != null){
            count++;
            // התקדמות לחולייה הבאה
            pos = pos.getNext();
        }

        return count; // החזרת כמות הקניות שבוצעו בחנות
    }

    // הדפסת כל הקניות ששולמו באשראי מסוים
    public void printBuysWithCard(String cardNumber) {
        var pos = this.buys;   // פויינטר לרשימת הקניות

        // איטרציה על הרשימה
        while (pos != null) {
            // אם קנייה נוכחית אחד מהאמצעי תשלום שלה היה הכרטיס אשראי שהיתקבל בפונק כפרמטר, יודפסו פרטי הקנייה
            if (pos.getValue().containsCard(cardNumber)) { pos.getValue().printDetails(); }

            // התקדמות לחולייה הבאה
            pos = pos.getNext();
        }
    }

    // החזרת מספר הקניות ששולמו במזומן בלבד
    public int countCashOnlyBuys() {
        var pos = this.buys;   // פויינטר לרשימת הקניות
        var count = 0; // מונה קניות ששולמו במזומן בלבד

        // איטרציה על הרשימה
        while (pos != null) {
            // אם הקנייה בוצעה במזומן בלבד, נמנה קנייה זו
            if (pos.getValue().isCashOnly()) { count++; }

            // התקדמות לחולייה הבאה
            pos = pos.getNext();
        }

        return count; // נחזיר את כמות הקניות שבוצעו במזומן בלבד
    }
}

