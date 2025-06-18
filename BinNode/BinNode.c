// BinNode.c
#include "BinNode.h"

#include <stdio.h>
#include <stdlib.h>

//method to create pointer to binnode struct and initialize its fields
BinNode* CreateBinNode(void* value) {
    //allocate memory
    BinNode* node = (BinNode*)malloc(sizeof(BinNode));

    //initialize its fields
    node->value = value;
    node->left = NULL;
    node->right = NULL;

    //return the pointer
    return node;
}

//set value
void SetBinNodeValue(BinNode* node, void* value) { node->value = value; }

//get value
void* GetBinNodeValue(const BinNode* node) { return node->value; }

//get left son
BinNode* GetLeft(const BinNode* node) { return node->left; }

//get right son
BinNode* GetRight(const BinNode* node) { return node->right; }

//set left son
void SetLeft(BinNode* node, BinNode* left) { node->left = left; }

//set right son
void SetRight(BinNode* node, BinNode* right) { node->right = right; }

//is parent has left son
int HasLeft(const BinNode* node) { return node->left != NULL; }

//is parent has right son
int HasRight(const BinNode* node) { return node->right != NULL; }

//is binnode is leaf
int IsLeaf(const BinNode* node) { return node->left == NULL && node->right == NULL; }

//print binnode
void printIntBin(const BinNode* node) { if (node != NULL) { printf("%d ", *(int*)node->value); } }
