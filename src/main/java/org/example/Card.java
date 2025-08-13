package org.example;

public class Card {
    private char color;//r,g,b,y
    private int digit;

    public Card(char color, int digit) {
        this.color = color;
        this.digit = digit;
    }

    public char getColor() {
        return color;
    }

    public void setColor(char color) {
        this.color = color;
    }

    public int getDigit() {
        return digit;
    }

    public void setDigit(int digit) {
        this.digit = digit;
    }

    public int compareTo(Card other) {
        // שלב 1: השוואת המספרים
        if (this.digit > other.digit) {
            return -1;  // הקלף הנוכחי חזק יותר
        }
        if (this.digit < other.digit) {
            return 1;   // הקלף הנוכחי חלש יותר
        }

        // שלב 2: אם המספרים שווים → משווים לפי צבע
        int thisColorRank = getColorRank(this.color);
        int otherColorRank = getColorRank(other.color);

        if (thisColorRank > otherColorRank) {
            return -1;  // הקלף הנוכחי חזק יותר לפי צבע
        }
        if (thisColorRank < otherColorRank) {
            return 1;   // הקלף הנוכחי חלש יותר לפי צבע
        }

        // שלב 3: זהים לגמרי
        return 0;
    }

    private int getColorRank(char color) {
        switch (color) {
            case 'R':
                return 0;
            case 'G':
                return 1;
            case 'B':
                return 2;
            case 'Y':
                return 3;
            default:
                return -1; // אופציונלי: לטיפול במקרה חריג (לא צפוי)
        }
    }

    @Override
    public String toString() {
        return "Card{" +
                "color=" + color +
                ", digit=" + digit +
                '}';
    }
}
