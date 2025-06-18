//
// Created by sharbel on 4/25/2025.
//

// Include JNI functions
#include <jni.h>

// Include standard libraries
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Include generated headers
#include "ProjectJni.h"
#include "ProjectJni_Person.h"
#include "ProjectJni_PeopleList.h"

// --------- STRUCT DEFINITIONS ---------

// Struct representing a Person
typedef struct {
    char *name;
    int age;
    char *id;
    char *phone;
    char *email;
} Person;

// Struct representing a PeopleList (dynamic array)
typedef struct {
    Person **people;  // Array of pointers to Person
    int size;         // Number of people
    int capacity;     // Allocated size
} PeopleList;

// --------- Free person HELPERS ---------

//free a Person structure safely
void freePerson(Person *p) {
    if (p != NULL) {
        free(p->name);
        free(p->id);
        free(p->phone);
        free(p->email);
        free(p);
    }
}

// --------- MATRIX FUNCTIONS ---------

// Create a 2D matrix of integers (n x n)
JNIEXPORT jlong JNICALL Java_ProjectJni_createMatrix(JNIEnv *env, jobject obj, jint n) {
    //create dynamic matrix
    jint **matrix = (jint **)malloc(n * sizeof(jint *));

    //create rows, and fill them as I wish
    for (jint i = 0; i < n; i++) {
        matrix[i] = (jint *)calloc(n, sizeof(jint));
        matrix[i][i] = 1;
    }

    //return "pointer" to the matrix
    return (jlong)matrix;
}

// Print the 2D matrix
JNIEXPORT void JNICALL Java_ProjectJni_printMatrix(JNIEnv *env, jobject obj, jlong ptr, jint n) {
    //create pointer for matrix
    jint **matrix = (jint **)ptr;

    //print its content
    for (jint i = 0; i < n; i++) {
        for (jint j = 0; j < n; j++) {
            printf("matrix[%d][%d] = %d\n", i, j, matrix[i][j]);
        }
    }
}

// Sum a 1D array
JNIEXPORT jint JNICALL Java_ProjectJni_sumArray(JNIEnv *env, jobject obj, jlong ptr, jint length) {
    //create pointer for array
    const jint *elements = (jint *)ptr;

    //var for sum up values from array
    jint sum = 0;

    //sum up all values in the array
    for (int i = 0; i < length; i++) {
        sum += elements[i];
    }

    //return sum
    return sum;
}

// Get a value from the matrix
JNIEXPORT jint JNICALL Java_ProjectJni_getMatrixValue(JNIEnv *env, jobject obj, jlong ptr, jint row, jint col) {
    //create pointer for matrix
    jint **matrix = (jint **)ptr;

    //return value in some specific index
    return matrix[row][col];
}

// Set a value in the matrix
JNIEXPORT void JNICALL Java_ProjectJni_setMatrixValue(JNIEnv *env, jobject obj, jlong ptr, jint row, jint col, jint value) {
    //create pointer for matrix
    jint **matrix = (jint **)ptr;

    //update value in some specific index
    matrix[row][col] = value;
}

// Free the entire matrix
JNIEXPORT void JNICALL Java_ProjectJni_freeMatrix(JNIEnv *env, jobject obj, jlong ptr, jint n) {
    //create pointer for matrix
    jint **matrix = (jint **)ptr;

    //free all rows from matrix
    for (jint i = 0; i < n; i++) {
        free(matrix[i]);
    }

    //free matrix itself
    free(matrix);
}

// --------- STRING HELPERS ---------

// Create a custom C string from Java string
JNIEXPORT jlong JNICALL Java_ProjectJni_createCustomString(JNIEnv *env, jobject obj, jstring jstr) {
    //create C String and pointer for it
    const char *temp = (*env)->GetStringUTFChars(env, jstr, NULL);

    //if string is null -> 0
    if (temp == NULL) return 0;

    //create copy string
    char *copy = (char *)calloc(strlen(temp) + 1, sizeof(char));

    //if copy-string contains null, garbage value, -> 0 release the C String "created" at first
    if (copy == NULL) {
        (*env)->ReleaseStringUTFChars(env, jstr, temp);
        return 0;
    }

    //copy C String content to copy-string
    strcpy(copy, temp);

    //release the C String "created" at first
    (*env)->ReleaseStringUTFChars(env, jstr, temp);

    //return "pointer" for copy String
    return (jlong)copy;
}

// Free a custom C string
JNIEXPORT void JNICALL Java_ProjectJni_freeCString(JNIEnv *env, jobject obj, jlong ptr) {
    if (ptr != 0) {
        free((void *)ptr);
    }
}

// --------- PERSON FUNCTIONS ---------

// Create a new Person(for java constructor method)
JNIEXPORT jlong JNICALL Java_ProjectJni_00024Person_createPerson(JNIEnv *env, jobject obj, jlong namePtr, jint age, jlong idPtr, jlong phonePtr, jlong emailPtr) {
    //create Person struct
    Person *p = (Person *)malloc(sizeof(Person));

    //if person not created for some reason -> 0
    if (p == NULL) return 0;

    //initialize its fields
    p->name = (char *)namePtr;
    p->age = age;
    p->id = (char *)idPtr;
    p->phone = (char *)phonePtr;
    p->email = (char *)emailPtr;

    //return "pointer" for this object [1edf#$%6 : long]
    return (jlong)p;
}

// Print Person information
JNIEXPORT void JNICALL Java_ProjectJni_00024Person_printPerson(JNIEnv *env, jobject obj, jlong ptr) {
    //cretae person pointer
    Person *p = (Person *)ptr;

    if (p != NULL) {
        printf("Name: %s\n", p->name);
        printf("Age: %d\n", p->age);
        printf("ID: %s\n", p->id);
        printf("Phone: %s\n", p->phone);
        printf("Email: %s\n", p->email);
    }
}

// Free a Person (JNI wrapper)
JNIEXPORT void JNICALL Java_ProjectJni_00024Person_freePerson(JNIEnv *env, jobject obj, jlong ptr) {
    freePerson((Person *)ptr);
}

// --------- PEOPLE LIST FUNCTIONS ---------

// Create a new empty PeopleList(for java constructor method)
JNIEXPORT jlong JNICALL Java_ProjectJni_00024PeopleList_createPeopleList(JNIEnv *env, jobject obj) {
    //create "people-list" object
    PeopleList *list = (PeopleList *)malloc(sizeof(PeopleList));

    //if list not created for some reason -> 0
    if (list == NULL) return 0;

    //initialize its fields
    list->size = 0;
    list->capacity = 10;
    //create space in memory for the list : Person
    list->people = (Person **)calloc(list->capacity, sizeof(Person *));

    //return "pointer" for this object [1edf#$%6 : long]
    return (jlong)list;
}

// Add a new person to the list
JNIEXPORT void JNICALL Java_ProjectJni_00024PeopleList_addPerson(JNIEnv *env, jobject obj, jlong listPtr, jlong personPtr) {
    //pointer for people-list : structs
    PeopleList *list = (PeopleList *)listPtr;

    //check if the array is full before adding person to it
    if (list->size == list->capacity) {
        //capacity * 2
        list->capacity *= 2;

        //enlarge the array
        list->people = (Person **)realloc(list->people, list->capacity * sizeof(Person *));
    }

    //add person to the array
    list->people[list->size++] = (Person *)personPtr;
}

// Remove a person from the list
JNIEXPORT void JNICALL Java_ProjectJni_00024PeopleList_removePerson(JNIEnv *env, jobject obj, jlong listPtr, jint index) {
    //pointer for people-list : structs
    PeopleList *list = (PeopleList *)listPtr;

    //check if index is legal
    if (index >= 0 && index < list->size) {
        //free this specific person from the array
        freePerson(list->people[index]);

        //mv all values in array backword
        for (int i = index; i < list->size - 1; i++) {
            list->people[i] = list->people[i + 1];
        }

        //update size(of list) field value
        list->size--;
    }
}

// Print all people in the list
JNIEXPORT void JNICALL Java_ProjectJni_00024PeopleList_printPeopleList(JNIEnv *env, jobject obj, jlong listPtr) {
    //pointer for people-list : structs
    PeopleList *list = (PeopleList *)listPtr;

    //iterate on list and print all people data
    for (int i = 0; i < list->size; i++) {
        printf("--- Person %d ---\n", i);
        printf("Name: %s\n", list->people[i]->name);
        printf("Age: %d\n", list->people[i]->age);
        printf("ID: %s\n", list->people[i]->id);
        printf("Phone: %s\n", list->people[i]->phone);
        printf("Email: %s\n", list->people[i]->email);
    }
}

// Find a person by their name
JNIEXPORT jint JNICALL Java_ProjectJni_00024PeopleList_findPersonByName(JNIEnv *env, jobject obj, jlong listPtr, jlong namePtr) {
    //pointer for people-list : structs
    PeopleList *list = (PeopleList *)listPtr;

    //create C String for name - by cast the long-number to be a pointer for it in memory
    char *searchName = (char *)namePtr;

    //iterate on list and search for specific person
    for (int i = 0; i < list->size; i++) {
        //call comparison function to check if the name of person matches
        //the name of the current person object

        if (strcmp(list->people[i]->name, searchName) == 0) {
            //where the person found -> index
            return i;
        }
    }

    return -1; // Not found -> -1
}

// Update a person at a given index
JNIEXPORT void JNICALL Java_ProjectJni_00024PeopleList_updatePerson(JNIEnv *env, jobject obj, jlong listPtr, jint index, jlong newNamePtr, jint newAge, jlong newIdPtr, jlong newPhonePtr, jlong newEmailPtr) {
    //pointer for people-list : structs
    PeopleList *list = (PeopleList *)listPtr;

    //if entered legal index value
    if (index >= 0 && index < list->size) {
        //pointer for this specific person
        Person *p = list->people[index];

        //clean old data of this person
        free(p->name);
        free(p->id);
        free(p->phone);
        free(p->email);

        //create new data for this person
        p->name = (char *)newNamePtr;
        p->age = newAge;
        p->id = (char *)newIdPtr;
        p->phone = (char *)newPhonePtr;
        p->email = (char *)newEmailPtr;
    }
}

// Free the entire PeopleList
JNIEXPORT void JNICALL Java_ProjectJni_00024PeopleList_freePeopleList(JNIEnv *env, jobject obj, jlong listPtr) {
    //pointer for people-list : structs
    PeopleList *list = (PeopleList *)listPtr;

    //iterate on list, free all persons structs from its
    for (int i = 0; i < list->size; i++) {
        //free current person : struct
        freePerson(list->people[i]);
    }

    //free the list
    free(list->people);

    //free the entire object from memory
    free(list);
}
