// Node.c
#include "Node.h"

#include <stdio.h>
#include <stdlib.h>

//method to create pointer to node struct and initialize its fields
Node* CreateNode(void* value) {
    //allocate memory
    Node* n1 = (Node*)malloc(sizeof(Node));

    //initialize its fields
    n1->value = value;
    n1->next = NULL;

    //return the pointer
    return n1;
}

//set Value
void SetNodeValue(Node* node, void* value) { node->value = value; }

//get Value
void* GetNodeValue(const Node* node) { return node->value; }

//set next node
void SetNextNode(Node* node, Node* next) { node->next = next; }

//get next node
Node* GetNextNode(const Node* node) { return node->next; }

void printIntNode(const Node* node) {
    while (node != NULL) {
        printf("Node value: %d\n", *(int*)node->value);
        node = node->next;
    }
    printf("\n");
}

void printDoubleNode(const Node* node) {

    while (node != NULL) {
        printf("Node value: %f\n", *(double*)node->value);
        node = node->next;
    }
    printf("\n");
}

void printStringNode(const Node* node) {
    while (node != NULL) {
        printf("Node value: %s\n", (char*)node->value);
        node = node->next;
    }
    printf("\n");
}

void freeList(Node* node) {
    while (node != NULL) {
        //pointer for the next node
        Node* next = node->next;
        //free value
        free(node->value);
        //free pointer to struct node
        free(node);
        node = next;
    }
}