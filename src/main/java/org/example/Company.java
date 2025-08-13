package org.example;
import java.util.Arrays;

public class Company {
    private Clientc [] clients; // מערך לקוחות
    private  int numClients; // מס לקוחות בפועל

    // בנאי
    public Company() {
        this.clients = new Clientc[1000];
        this.numClients = 0;
    }

    public Clientc[] getClients() { return clients; } // קבלת מערך הלקוחות

    public void setClients(Clientc[] clients) { this.clients = clients; } // קביעת מערך הלקוחות

    public int getNumClients() { return numClients; } // קבלת מס הלקוחות בפועל

    public void setNumClients(int numClients) { this.numClients = numClients; } // קביעת מס הלקוחות

    // מתודה המוסיפה לקוח למערכת
    public void addClient(Clientc c) {
        if (this.numClients < this.clients.length) { this.clients[this.numClients++] = c; }
    }

    @Override
    public String toString() {
        return "Company{" +
                "clients=" + Arrays.toString(clients) +
                ", numClients=" + numClients +
                '}';
    }

    // הפעולה קוראת לפונקצית למבדה המדפיסה את שמות הלקוחות שיש להם ערוץ "ספורט"
    public void printAllClientsThatHaveSportChannel(){ printAllClientsThatHaveSportChannel.run(); }

    //  הפעולה מדפיסה את שמות הלקוחות שיש להם ערוץ "ספורט"
    private final Runnable printAllClientsThatHaveSportChannel = () -> {

        // איטרציה על מערך הלקוחות
        for (var client : this.clients){
            // אם התא מפנה ללקוח
            if (client != null) {
                var clientName = client.getClientName(); // שמירת שם לקוח נוכחי
                var cable = client.getCable(); // קבלת חבילת כבלים של לקוח נוכחי

                // אם זו חבילה משולבת
                if (cable instanceof ExtendedPackage) {
                    var exp = (ExtendedPackage) cable; // המרה מפורשת לחבילה משולבת
                    var names = exp.getNamesOfChannels(); // קבלת מערך שמות הערוצים

                    // איטרציה על מערך שמות הערוצים
                    for (var channelName : names) {
                        // אם ישנו ערוץ ספורט במערך שמות הערוצים, אזי נדפיס את שם הלקוח
                        if (channelName != null && channelName.equalsIgnoreCase("sport")) {
                            System.out.println(clientName); // הדפסת שם לקוח נוכחי
                            // יציאה מלולאת חיפוש ערוץ הספורט, לשם יעול
                            break;
                        }
                    }
                }
            }else {
                // מכיוון והמערך ללא חורים, אם הגענו לכאן משמע שהלולאה עברה על כל הלקוחות בפועל בחברה
                // ולכן הפעולה תיעצר
                break;
            }
        }
    };

    // הפעולה המחזירה את כמות הלקוחות שיש להם חבילה בסיסית בלבד.
    public int onlyBasicClients(){
        var count = 0;

        for (var client : this.clients){
            // אם התא מפנה ללקוח
            if (client != null) {
                var cable = client.getCable(); // קבלת חבילת כבלים של לקוח נוכחי

                // אם זו חבילה בסיסית בלבד, נמנה לקוח זה
                if (
                        // אם אין לו חבילה מורחבת
                        !(cable instanceof ExtendedPackage)
                        // ואם אין לו חבילת טלפון
                        && !(cable instanceof Phone)
                        // ואם אין לו חבילה משולבת - טריפל
                        && !(cable instanceof Triple)
                        // ,אבל כן יש לו חבילה בסיסית ביותר
                        && cable  instanceof BasicPackage
                ) {count++;}
            }else {
                break;
            }
        }

        return count; // החזרת כמות הלקוחות שיש להם חבילה בסיסית בלבד
    }

}
