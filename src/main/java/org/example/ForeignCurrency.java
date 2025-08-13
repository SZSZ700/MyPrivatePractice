package org.example;

public class ForeignCurrency {
    private String name; // שם המטבע
    // רשימה של שווי
    //המטבע בשקלים )זהו שער המטבע( במשך תקופה מסוימת. לכל יום בתקופה יש שער משלו. האיבר בראש
    //הרשימה מציין את שער היום הנוכחי.
    private Node<Double> rates;

    public ForeignCurrency(String name) {
        this.name = name;
        this.rates = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node<Double> getRates() {
        return rates;
    }

    public void setRates(Node<Double> rates) {
        this.rates = rates;
    }

    @Override
    public String toString() {
        return "ForeignCurrency{" +
                "name='" + name + '\'' +
                ", rates=" + rates +
                '}';
    }

    // המוסיפה שער מטבע ביום הנוכחי
    public void addRate(double rate){ this.rates = new Node<>(rate, this.rates.getNext()); }

    // מטבע נחשב בשיא שלו אם השער הנוכחי שלו גבוה מכל השערים הקודמים שלו.
    public boolean firstIsMax(){
        var first = this.rates.getValue(); // שמירת שער נוכחי
        var pos = this.rates.getNext(); // פויינטר לחולייה הבאה ברשימה

        // איטרציה על הרשימה
        while (pos != null){
            // אם הערך הנוכחי גדול משער המטבע נחזיר שקר
            if (pos.getValue() >= first) return false;
            pos = pos.getNext(); // התקדמות לחולייה הבאה
        }

        return true; // יוחזר אמת משמע המטבע בשיאו
    }

    // מטבע נחשב "כדאי להשקעה" אם השער שלו עלה לפחות בחמישה הימים האחרונים אבל לא נמצא בשיא.
    public boolean kedaiLehashkaa(){
        if (this.firstIsMax()) return false; // אם המטבע בשיאו יוחזר שקר

        // בדיקת עלייה ב 5 ימים האחרונים
        var pos = this.rates; // פויינטר לרשימה
        var count = 0; // בדיקת עלייה רק ב 5 חוליות ראשונות לכן דרוש מונה עד זקיף - 5

        // איטרציה על הרשימה
        while (pos.getNext() != null && count < 4){
            // אם הערך הנוכחי קטן מהאיבר הבא יוחזר שקר
            if (pos.getValue() < pos.getNext().getValue()) return false;
            pos = pos.getNext(); // התקדמות לחולייה הבאה
            count++; // קידום מונה
        }

        // אם לא נספרו 5 ימים יוחזר שקר
        if (count != 5) return false;

        return true; // יוחזר אמת כלומר מטבע כדאי להשקעה
    }
}
