package org.example;

import java.util.*;
public class MyHashMap<K, V> {

    private static final int CAPACITY = 16; // קיבולת התחלתית של המערך
    private static final float LOAD = 0.75f; // הסף לעומס יתר
    private int size = 0; // מספר הזוגות מפתח-ערך במפה
    private Entry<K, V>[] buckets; // מערך של רשימות מקושרות

    //בנאי
    @SuppressWarnings("unchecked")
    public MyHashMap() {
        buckets = new Entry[CAPACITY];
    }

    // פונקציה לחישוב האינדקס במערך עבור המפתח
    private int getBucketIndex(K key) {
        return Math.abs(key.hashCode() % this.buckets.length);
    }

    // פונקציה להוספת מפתח-ערך או עדכון ערך קיים
    //O(1) / O(log(n))
    public void put(K key, V value) {

        // אם היחס בין הגודל לקיבולת עובר את הסף, מבצעים rehashing
        if ((float) size / this.buckets.length > LOAD) {rehash();}

        int index = getBucketIndex(key);//פונקציה המחזירה באיזה אינדקס נמצאת הרשימה או איפה ניתן להוסיף
        Entry<K, V> pos = this.buckets[index];//פוינטר לרשימה המקושרת באותו תא ספציפי

        // אם הרשימה ריקה הוסף רשומה חדשה
        if (pos == null) {
            this.buckets[index] = new Entry<>(key, value);
            this.size++; //הגדלת מונה הגודל
        } else { //אם השרשרת לא ריקה
            while (pos != null) { //נעבור על השרשרת
                //אם מצאנו חוליה שהמפתח בה שווה למפתח שהתקבל בפונקציה
                if (pos.getKey().equals(key)) {
                    // עדכון ערך קיים
                    pos.setValue(value);
                    //נצא
                    return;
                }

                //אם הגענו לחולייה האחרונה(כולל) והיא מפנה לריק,נצא
                if (pos.getNext() == null) {break;}

                //התקדמות לרשומה הבאה
                pos = pos.getNext();
            }

            //הוסף רשומה חדשה בסוף השרשרת הנוכחית כמובן
            pos.setNext(new Entry<>(key, value));
            this.size++; // הגדלת מונה
        }
    }

    // פונקציה לשליפת ערך על פי מפתח
    //O(1) / O(log(n))
    public V get(K key) {
        int index = getBucketIndex(key);//מציאת מיקום רשומה בתא במערך
        Entry<K, V> pos = this.buckets[index];//פויינטר לרשימה המקושרת בתא הספציפי שמצאנו את מיקומו

        while (pos != null) {//כל עוד הפויינטר לא מפנה לריק
            if (pos.getKey().equals(key)) {//אם נמצאה רשומה שערך המפתח שלה שווה למפתח שהתקבל בפונקצייה
                return pos.getValue(); // החזרת הערך אם המפתח נמצא
            }
            pos = pos.getNext();//התקדם לחוליה הבאה
        }

        return null; // אם המפתח לא נמצא, מחזירים null
    }

    // מתודת keySet להחזרת סט של המפתחות
    //O(n**2)
    public Set<K> keySet() {
        //יצירת סט להחזרה של מפתחות
        Set<K> keySet = new HashSet<>();

        for (int i = 0; i < this.buckets.length; i++) {//מעבר על המפה

            Entry<K, V> pos = this.buckets[i];//פוינטר לתא(דלי) נוכחי

            while (pos != null) {
                keySet.add(pos.getKey()); // הוספת המפתחות לסט
                pos = pos.getNext();
            }
        }

        // מחזירה את הסט של המפתחות
        return keySet;
    }

    // מתודת values להחזרת רשימה של הערכים
    //O(n**2)
    public Collection<V> values() {
        Collection<V> values = new ArrayList<>();//החזרת מערך דינאמי-אפשר לבחור מימוש של מבנה נתונים אחר שחלק מאוסף

        for (int i = 0; i < buckets.length; i++) {//מעבר על המפה(מערך רשומות) אנטריס

            Entry<K, V> current = buckets[i];//פויינטר לרשימת רשומות

            while (current != null) {//מעבר על הרשימה עם הפויינטר
                values.add(current.getValue()); // הוספת הערכים לרשימה
                current = current.getNext();
            }
        }

        return values; // מחזירה את הרשימה של הערכים
    }

    // מתודת entrySet להחזרת סט של הקלטות
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entrySet = new HashSet<>();
        for (int i = 0; i < this.buckets.length; i++) {
            Entry<K, V> current = this.buckets[i];
            while (current != null) {
                entrySet.add(new AbstractMap.SimpleEntry<>(current.getKey(), current.getValue())); // הוספת הקלטות לסט
                current = current.getNext();
            }
        }
        return entrySet; // מחזירה את הסט של הקלטות
    }

    // פונקציה להרחבת המערך וביצוע rehashing
    @SuppressWarnings("unchecked")
    private void rehash() {
        //פויינטר נוסף למערך נוכחי
        Entry<K, V>[] oldBuckets = this.buckets;
        // הכפלת הקיבולת-הפוינטר המקורי הישן מפנה למערך חדש גדול פי 2
        this.buckets = new Entry[oldBuckets.length * 2];
        size = 0; // איפוס הגודל והוספת הערכים מחדש

        for (int i = 0; i < oldBuckets.length; i++) { //מעבר על המערך הישן

            Entry<K, V> pos = oldBuckets[i]; //פוינטר לראש השרשרת היישנה

            while (pos != null) {
                put(pos.getKey(), pos.getValue()); // הכנסת כל ערך מחדש
                pos = pos.getNext();//התקדם לחולייה הבאה
            }
        }
    }
}
