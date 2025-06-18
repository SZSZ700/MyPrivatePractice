#include "Entry.h"
#include <stdio.h>
#include <stdlib.h>

Entry* CreateEntry(void* key, void* value) {
    //create pointer to entry struct
    Entry* e = (Entry*)malloc(sizeof(Entry));

    //initialize its fields
    e->key = key;
    e->value = value;
    e->next = NULL;

    //return the pointer to the struct
    return e;
}

//set key
void SetKey(Entry* e, void* key) { e->key = key; }

//set value
void SetValue(Entry* e, void* value) { e->value = value; }

//get key
void* GetKey(const Entry* e) { return e->key; }

//get value
void* GetValue(const Entry* e) { return e->value; }

//set next
void SetNext(Entry* e, void* next) { e->next = next; }

//get next
void* GetNext(const Entry* e) { return e->next; }

//free entry from memory
void FreeEntry(Entry* e) {
    while (e != NULL) {
        //pointer for the next entry
        Entry* temp = e->next;
        //free value
        free(e->value);
        //free key
        free(e->key);
        //free pointer to struct entry
        free(e);
        //move to the next entry
        e = temp;
    }
}