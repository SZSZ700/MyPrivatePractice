// Node.h
#ifndef NODE_H
#define NODE_H

//Generic Node struct
typedef struct Node {
    void* value;
    struct Node* next;
} Node;

Node* CreateNode(void* value);

void SetNodeValue(Node* node, void* value);

void* GetNodeValue(const Node* node);

void SetNextNode(Node* node, Node* next);

Node* GetNextNode(const Node* node);

void printIntNode(const Node* node);

void printDoubleNode(const Node* node);

void printStringNode(const Node* node);

void freeList(Node* node);

#endif



