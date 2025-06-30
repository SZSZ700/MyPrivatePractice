// Stack.h
#ifndef STACK_H
#define STACK_H

#include "../Node/Node.h"

//Generic Stack struct
typedef struct Stack {
    //pointer to the head of the list
    Node* top;
    //length of the list = size of stack
    int size;
    //pointer to the bottom value in the stack
    void* bottom;
} Stack;

Stack* CreateStack();

int Empty(const Stack* stack);

void Push(Stack* stack, void* value);

void* Pop(Stack* stack);

void* Peek(const Stack* stack);

void FreeStack(Stack* stack);

int StackSize(const Stack* stack);

void* GetBottom(const Stack *stack);

#endif
