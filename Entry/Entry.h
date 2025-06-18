#ifndef ENTRY_H
#define ENTRY_H

typedef struct Entry {
    void* key;
    void* value;
    struct Entry *next;
}Entry;

Entry* CreateEntry(void* key, void* value);

//set key
void SetKey(Entry* e, void* key);

//set value
void SetValue(Entry* e, void* value);

//get key
void* GetKey(const Entry* e);

//get value
void* GetValue(const Entry* e);

//set next
void SetNext(Entry* e, void* next);

//get next
void* GetNext(const Entry* e);

//free entry from memory
void FreeEntry(Entry* e);

#endif //ENTRY_H
