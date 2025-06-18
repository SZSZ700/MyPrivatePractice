// Stack.c
#include "Stack.h"
#include <stdlib.h>

//O(1)
//method to create pointer to stack struct and initialize its fields
Stack* CreateStack() {
    //allocate memory
    Stack* stk = (Stack*)malloc(sizeof(Stack));

    //initialize its fields
    stk->top = NULL;

    //initialize size to 0
    stk->size = 0;

    //initialize bottom of the stack to be 0
    stk->bottom = NULL;

    //return pointer to the stack struct
    return stk;
}

//O(1)
//is stuck empty
int Empty(const Stack* stack) {
    return stack->top == NULL;
}

//O(1)
//push node to stuck
void Push(Stack* stack, void* value) {
    //Keep bottom of stack
    if (Empty(stack)) { stack->bottom = value; }

    //create new node to push to stack
    Node* n1 = CreateNode(value);

    //connect the new node in left side of the head
    SetNextNode(n1, stack->top);//n1->next = stk->top;

    //make the head pointer point to it
    stack->top = n1;

    //increase the size of the stack
    stack->size++;
}

//O(1)
//pop node from the stack
void* Pop(Stack* stack) {
    //is stk is empty return null
    if (Empty(stack)) return NULL;

    //pointer for the head
    Node* temp = stack->top;

    //keep value of the current node
    void* val = GetNodeValue(temp);////void* val = stk->top->value;

    //make the head pointer point to the next node in the list
    stack->top = GetNextNode(temp);

    //free it from memory
    free(temp);

    //decrease the size of the stack
    stack->size--;

    //if stack is empty free bottom
    if (stack->top == NULL) { stack->bottom = NULL; }

    //return the value
    return val;
}

//O(1)
//review what hide in top node in stack
void* Peek(const Stack* stack) {
    if (stack->top == NULL) return NULL;

    return GetNodeValue(stack->top);
}

//O(n) //***can be O(1) only if stack contain 1 val
//free stack from memory
void FreeStack(Stack* stack) {
    //iteration on the stack
    while (!Empty(stack)) {
        //pop out value from stack
        void* val = Pop(stack);

        //free it from memory
        free(val);
    }

    //free the stack struct from memory
    free(stack);
}

//O(1)
//return size of stack
int StackSize(const Stack* stack) {
    return stack->size;
}

//O(1)
//return bottom value in stack
void* GetBottom(const Stack *stack) {
    //if stack is empty
    if (stack == NULL || stack->size == 0 || stack->top == NULL) { return NULL; }

    //else return bottom
    return stack->bottom;
}
