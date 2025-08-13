package org.example;

public class Date {
    private int day;   // משתנה לאחסון היום
    private int month; // משתנה לאחסון החודש
    private int year;  // משתנה לאחסון השנה

    // בנאי המקבל יום, חודש ושנה ומוודא את תקינותם
    public Date(int day, int month, int year) {
        validateDate(day, month, year); // קריאה לפונקציה לבדוק אם התאריך חוקי
        this.day = day;   // הגדרת היום
        this.month = month; // הגדרת החודש
        this.year = year;   // הגדרת השנה
    }

    public Date() {
        this.day = 00;
        this.month = 00;
        this.year = 00;
    }

    // מתודת גישה המחזירה את היום
    public int getDay() {
        return day;
    }

    // מתודת גישה המחזירה את החודש
    public int getMonth() {
        return month;
    }

    // מתודת גישה המחזירה את השנה
    public int getYear() {
        return year;
    }

    // מתודת עדכון לשינוי היום בתאריך
    public void setDay(int day) {
        validateDate(day, this.month, this.year); // בדיקת התאריך לאחר השינוי
        this.day = day; // עדכון המשתנה
    }

    // מתודת עדכון לשינוי החודש בתאריך
    public void setMonth(int month) {
        validateDate(this.day, month, this.year); // בדיקת התאריך לאחר השינוי
        this.month = month; // עדכון המשתנה
    }

    // מתודת עדכון לשינוי השנה בתאריך
    public void setYear(int year) {
        validateDate(this.day, this.month, year); // בדיקת התאריך לאחר השינוי
        this.year = year; // עדכון המשתנה
    }

    // פונקציה לבדיקה האם השנה היא שנה מעוברת
    private boolean isLeapYear(int year) {
        // שנה מעוברת מתחלקת ב-4 ולא מתחלקת ב-100, או שמתחלקת ב-400
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    // פונקציה המחזירה את מספר הימים בחודש מסוים
    private int getMaxDaysInMonth(int month, int year) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            // חודשים עם 31 ימים
            return 31;
        }

        if (month == 4 || month == 6 || month == 9 || month == 11) {
            // חודשים עם 30 ימים
            return 30;
        }
        if (month == 2) {
            // חודש פברואר, בודקים אם השנה מעוברת
            if (isLeapYear(year)) {
                return 29; // פברואר בשנה מעוברת
            } else {
                return 28; // פברואר בשנה רגילה
            }
        }
        // במקרה שחודש לא חוקי הוזן
        throw new IllegalArgumentException("חודש לא חוקי.");
    }

    // פונקציה לבדיקה אם התאריך חוקי
    private void validateDate(int day, int month, int year) {
        if (month < 1 || month > 12) {
            // בדיקה אם החודש נמצא בטווח החוקי
            throw new IllegalArgumentException("החודש חייב להיות בין 1 ל-12.");
        }
        int maxDays = getMaxDaysInMonth(month, year); // חישוב מספר הימים האפשרי בחודש
        if (day < 1 || day > maxDays) {
            // בדיקה אם היום בטווח החוקי
            throw new IllegalArgumentException("היום חייב להיות בין 1 ל-" + maxDays + ".");
        }
    }

    // מתודה להצגת התאריך כטקסט
    @Override
    public String toString() {
        // פורמט הצגה: DD/MM/YYYY
        return String.format("%02d/%02d/%04d", day, month, year);
    }

    public int compareTo(Date other) {
        // בדיקה אם השנה שונה
        if (this.year > other.year) {
            return 1; // השנה של התאריך הנוכחי מאוחרת
        }
        if (this.year < other.year) {
            return -1; // השנה של התאריך הנוכחי מוקדמת
        }

        // בדיקה אם החודש שונה
        if (this.month > other.month) {
            return 1; // החודש של התאריך הנוכחי מאוחר
        }
        if (this.month < other.month) {
            return -1; // החודש של התאריך הנוכחי מוקדם
        }

        // בדיקה אם היום שונה
        if (this.day > other.day) {
            return 1; // היום של התאריך הנוכחי מאוחר
        }
        if (this.day < other.day) {
            return -1; // היום של התאריך הנוכחי מוקדם
        }

        // כל הרכיבים שווים
        return 0;
    }

}

