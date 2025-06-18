#ifndef ANIMAL_H
#define ANIMAL_H

// טיפוס של פונקציה שמדפיסה טקסט
typedef void (*SpeakFunc)();
typedef void (*EatFunc)(const char* food);

// מבנה "מחלקה" עם פוינטרים לפונקציות
typedef struct {
    SpeakFunc speak;
    EatFunc eat;
} Animal;

// פונקציות יצירה
Animal* CreateDog();
Animal* CreateCat();

#endif
