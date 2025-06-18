// Queue.c
#include "Queue.h"
#include <stdlib.h>

//O(1)
//method to create pointer to queue struct and initialize its fields
Queue* CreateQueue() {

    //allocate memory
    Queue* q = (Queue*)malloc(sizeof(Queue));

    //initialize its fields
    q->head = NULL;
    q->tail = NULL;

    //return pointer to the queue struct
    return q;
}

//O(1)
//check if queue is empty
int QIsEmpty(const Queue* q) {
    return q->head == NULL;
}

//O(1)
//add value to the queue
void Offer(Queue* q, void* value) {
    //Create new Node to add to the queue
    Node* toAdd = CreateNode(value);

    //check if the head of the list inside the queue struct is points to NULL
    if (QIsEmpty(q)) {
        q->head = toAdd;
        q->tail = toAdd;
    } else {
        SetNextNode(q->tail, toAdd);
        q->tail = toAdd;
    }
}

//O(1)
//poll value from the queue (Caution: Generic Function, needs casting)
void* Poll(Queue* q) {
    //check if the head of the list inside the queue struct is points to NULL
    if (QIsEmpty(q)) return NULL;

    //temporary pointer to the list
    Node* temp = q->head;

    //keep value of the head of the list(inside the Queue struct)
    void* val = GetNodeValue(temp);

    //point the header-pointer to the next node in the list(sec one)
    q->head = GetNextNode(temp);

    if (q->head == NULL) q->tail = NULL;

    //free the temporary pointer to the list
    free(temp);

    //return the value
    return val;
}

//O(1)
//function that return the head of the queue
void* Head(const Queue* q) {
    //check if it's not point to null
    if (q->head == NULL) return NULL;

    //returns the value of the head
    return GetNodeValue(q->head);
}

//O(n)
//function to free the queue from memory
void FreeQueue(Queue* q) {
    //iteration
    while (!QIsEmpty(q)) {
        //poll value
        void* val = Poll(q);
        //free it from heap memory
        free(val);
    }
    //free the pointer to struct from memory
    free(q);
}


