// Queue.h
#ifndef QUEUE_H
#define QUEUE_H

#include "../Node/Node.h"

//Generic Queue struct
typedef struct Queue {
    Node* head;
    Node* tail;
} Queue;

Queue* CreateQueue();

int QIsEmpty(const Queue* q);

void Offer(Queue* q, void* value);

void* Poll(Queue* q);

void* Head(const Queue* q);

void FreeQueue(Queue* q);

#endif
