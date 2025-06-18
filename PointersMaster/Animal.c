#include <stdio.h>
#include <stdlib.h>
#include "Animal.h"

// === פונקציות הכלב ===
void DogSpeak() { printf("Woof!\n"); }

void DogEat(const char* food) { printf("The dog eats %s.\n", food); }

// === פונקציות החתול ===
void CatSpeak() { printf("Meow!\n"); }

void CatEat(const char* food) { printf("The cat eats %s.\n", food); }

// === יצירת אובייקט כלב ===
Animal* CreateDog() {
    Animal* dog = (Animal*)malloc(sizeof(Animal));
    dog->speak = DogSpeak;
    dog->eat = DogEat;
    return dog;
}

// === יצירת אובייקט חתול ===
Animal* CreateCat() {
    Animal* cat = (Animal*)malloc(sizeof(Animal));
    cat->speak = CatSpeak;
    cat->eat = CatEat;
    return cat;
}
