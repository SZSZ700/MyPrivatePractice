#ifndef UNTITLED1_COMPLEX_H
#define UNTITLED1_COMPLEX_H
#include <iostream>
// ======================================================
// Complex
// מחלקה שמייצגת מספר מרוכב (a + bi)
// כוללת העמסת אופרטורים: +, -, *, /, ==, <<, >> ועוד
// ======================================================
class Complex {
    double real;   // החלק הממשי
    double imag;   // החלק המדומה

public:
    // בנאי ברירת מחדל
    explicit Complex(double r = 0.0, double i = 0.0);

    // בנאי העתקה
    Complex(const Complex& other);

    // אופרטור השמה
    Complex& operator=(const Complex& other);

    // אופרטור חיבור
    Complex operator+(const Complex& other) const;

    // אופרטור חיסור
    Complex operator-(const Complex& other) const;

    // אופרטור כפל
    Complex operator*(const Complex& other) const;

    // אופרטור חילוק
    Complex operator/(const Complex& other) const;

    // אופרטור השוואה ==
    bool operator==(const Complex& other) const;

    // אופרטור השוואה !=
    bool operator!=(const Complex& other) const;

    // אופרטור +=
    Complex& operator+=(const Complex& other);

    // אופרטור -=
    Complex& operator-=(const Complex& other);

    // אופרטור *=
    Complex& operator*=(const Complex& other);

    // אופרטור /=
    Complex& operator/=(const Complex& other);

    // Getter לחלק הממשי
    [[nodiscard]] double getReal() const;

    // Getter לחלק המדומה
    [[nodiscard]] double getImag() const;

    // Setter
    void set(double r, double i);

    // הצגת מספר מרוכב
    void print() const;

    // אופרטור << להדפסה
    friend std::ostream& operator<<(std::ostream& os, const Complex& c);

    // אופרטור >> לקלט
    friend std::istream& operator>>(std::istream& is, Complex& c);
};

#endif // UNTITLED1_COMPLEX_H
