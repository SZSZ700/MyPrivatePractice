package org.example;

public class Entry<K, V> {
    private K key;//מפתח
    private V value;//ערך
    private Entry<K, V> next;//מצביע לחוליה הבאה ברשימה מקושרת

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
        this.next = null;
    }

    // מחזיר את המפתח
    public K getKey() {
        return key;
    }

    // מחזיר את הערך
    public V getValue() {
        return value;
    }

    // מעדכן את הערך
    public void setValue(V value) {
        this.value = value;
    }

    // מחזיר את החוליה הבאה ברשימה
    public Entry<K, V> getNext() {
        return next;
    }

    // מעדכן את החוליה הבאה ברשימה
    public void setNext(Entry<K, V> next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "Entry [key=" + key + ", value=" + value + ", next=" + next + "]";
    }
}
