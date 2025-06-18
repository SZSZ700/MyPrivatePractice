// Stack.h
#ifndef STACK_H
#define STACK_H

#include "../Node/Node.h"

//Generic Stack struct
typedef struct Stack {
    Node* top;
    int size;
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
