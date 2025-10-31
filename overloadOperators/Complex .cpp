#include "Complex .h"
// ------------------------------------------------------
// בנאי ברירת מחדל + בנאי עם פרמטרים
// יוצר מספר מרוכב עם חלק ממשי וחלק מדומה
// ------------------------------------------------------
Complex::Complex(const double r, const double i) : real(r), imag(i) {}

// ------------------------------------------------------
// בנאי העתקה - יוצר עותק חדש מאובייקט קיים
// ------------------------------------------------------
Complex::Complex(const Complex& other) = default;

// ------------------------------------------------------
// אופרטור השמה =
// משכפל ערכים מאובייקט אחד לשני
// ------------------------------------------------------
Complex& Complex::operator=(const Complex& other) {
    if (this != &other) {
        this->real = other.real;
        this->imag = other.imag;
    }

    return *this;
}

// ------------------------------------------------------
// אופרטור חיבור +
// מחבר שני מספרים מרוכבים
// ------------------------------------------------------
Complex Complex::operator+(const Complex& other) const {
    return Complex(this->real + other.real, this->imag + other.imag);
}

// ------------------------------------------------------
// אופרטור חיסור -
// מחסיר מספר מרוכב אחר
// ------------------------------------------------------
Complex Complex::operator-(const Complex& other) const {
    return Complex(this->real - other.real, this->imag - other.imag);
}

// ------------------------------------------------------
// אופרטור כפל *
// כפל של שני מספרים מרוכבים
// (a+bi)*(c+di) = (ac - bd) + (ad+bc)i
// ------------------------------------------------------
Complex Complex::operator*(const Complex& other) const {
    return Complex(this->real * other.real - this->imag * other.imag,
                   this->real * other.imag + this->imag * other.real);
}

// ------------------------------------------------------
// אופרטור חילוק /
// חילוק שני מספרים מרוכבים
// (a+bi)/(c+di) = [(ac+bd) + (bc-ad)i] / (c²+d²)
// ------------------------------------------------------
Complex Complex::operator/(const Complex& other) const {
    const double denom = other.real * other.real + other.imag * other.imag;

    if (denom == 0.0) { throw std::runtime_error("Division by zero in Complex"); }

    return Complex((this->real * other.real + this->imag * other.imag) / denom,
                   (this->imag * other.real - this->real * other.imag) / denom);
}

// ------------------------------------------------------
// אופרטור השוואה ==
// בודק אם שני מספרים מרוכבים שווים
// ------------------------------------------------------
bool Complex::operator==(const Complex& other) const {
    return this->real == other.real && this->imag == other.imag;
}

// ------------------------------------------------------
// אופרטור השוואה !=
// בודק אם שני מספרים מרוכבים שונים
// ------------------------------------------------------
bool Complex::operator!=(const Complex& other) const {
    return !(*this == other);
}

// ------------------------------------------------------
// אופרטור +=
// מוסיף ערך מרוכב ומחזיר את התוצאה לתוך האובייקט הנוכחי
// ------------------------------------------------------
Complex& Complex::operator+=(const Complex& other) {
    this->real += other.real;
    this->imag += other.imag;

    return *this;
}

// ------------------------------------------------------
// אופרטור -=
// מחסיר ערך מרוכב ומחזיר את התוצאה לאובייקט הנוכחי
// ------------------------------------------------------
Complex& Complex::operator-=(const Complex& other) {
    this->real -= other.real;
    this->imag -= other.imag;

    return *this;
}

// ------------------------------------------------------
// אופרטור *=
// כפל בתוך האובייקט הנוכחי
// ------------------------------------------------------
Complex& Complex::operator*=(const Complex& other) {
    const double r = this->real * other.real - this->imag * other.imag;
    const double i = this->real * other.imag + this->imag * other.real;

    this->real = r;
    this->imag = i;

    return *this;
}

// ------------------------------------------------------
// אופרטור /=
// חילוק בתוך האובייקט הנוכחי
// ------------------------------------------------------
Complex& Complex::operator/=(const Complex& other) {
    const double denom = other.real * other.real + other.imag * other.imag;

    if (denom == 0.0) { throw std::runtime_error("Division by zero in Complex"); }

    const double r = (this->real * other.real + this->imag * other.imag) / denom;
    const double i = (this->imag * other.real - this->real * other.imag) / denom;

    this->real = r;
    this->imag = i;

    return *this;
}

// ------------------------------------------------------
// Getter לחלק הממשי
// ------------------------------------------------------
double Complex::getReal() const { return this->real; }

// ------------------------------------------------------
// Getter לחלק המדומה
// ------------------------------------------------------
double Complex::getImag() const { return this->imag; }

// ------------------------------------------------------
// Setter - קובע ערכים חדשים
// ------------------------------------------------------
void Complex::set(const double r, const double i) {
    this->real = r;
    this->imag = i;
}

// ------------------------------------------------------
// פונקציה להדפסה (debug)
// ------------------------------------------------------
void Complex::print() const {
    std::cout << this->real << " + " << this->imag << "i\n";
}

// ------------------------------------------------------
// אופרטור << להדפסה
// ------------------------------------------------------
std::ostream& operator<<(std::ostream& os, const Complex& c) {
    os << c.real << " + " << c.imag << "i";
    return os;
}

// ------------------------------------------------------
// אופרטור >> לקלט
// ------------------------------------------------------
std::istream& operator>>(std::istream& is, Complex& c) {
    is >> c.real >> c.imag;
    return is;
}
