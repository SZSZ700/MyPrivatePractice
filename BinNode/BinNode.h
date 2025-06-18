// BinNode.h
#ifndef BINNODE_H
#define BINNODE_H

//Generic BinNode struct
typedef struct BinNode {
    void* value;
    struct BinNode* left;
    struct BinNode* right;
} BinNode;

BinNode* CreateBinNode(void* value);

void SetBinNodeValue(BinNode* node, void* value);

void* GetBinNodeValue(const BinNode* node);

BinNode* GetLeft(const BinNode* node);

BinNode* GetRight(const BinNode* node);

void SetLeft(BinNode* node, BinNode* left);

void SetRight(BinNode* node, BinNode* right);

int HasLeft(const BinNode* node);

int HasRight(const BinNode* node);

int IsLeaf(const BinNode* node);

#endif



