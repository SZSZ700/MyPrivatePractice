// Main.c
#include <stdio.h>
#include <stdlib.h>
#include "Node/Node.h"
#include "BinNode/BinNode.h"
#include "Stack.h"
#include "Queue.h"
#include "PointersMaster//Animal.h"

typedef struct {
    char *name;
    int age;
} Person;

//function to print lists (Nodes)
//caution: you may need change casting in function
void printNode(const Node *node) {
    const Node *pos = node;

    while (pos != NULL) {
        printf("node1: %s\n", (char*)GetNodeValue(pos));
        pos = GetNextNode(pos);
    }
}

//function to print tree (BinNode)
//caution: you may need change casting in function
void printTree(const BinNode* root) {

    if (root != NULL) {
        printf("Root: %s\n", (char*)GetBinNodeValue(root));
        printTree(GetLeft(root));
        printTree(GetRight(root));
    }
}

int* evenStk(Stack* stk) {
    //create assist stack for restoration
    Stack *temp = CreateStack();

    //counter for even values
    int *count = (int*)malloc(sizeof(int));
    *count = 0;

    //iteration
    while (!Empty(stk)) {
        //pop current value
        int *x = (int*)Pop(stk);

        //chek if it even, count it
        if (*x % 2 == 0) {(*count) ++;}

        //push it to the original stack
        Push(temp, x);
    }

    //restoration
    while (!Empty(temp)) {
        Push(stk, Pop(temp));
    }
    free(temp);

    return count;
}

int* lastInStack(Stack* stk) {
    if (Empty(stk)) {return NULL;}

    //stack assist
    Stack *temp = CreateStack();

    //iteration
    while (!Empty(stk)) {
        Push(temp, Pop(stk));
    }

    //last value in stack
    const int *top = (int*)Peek(temp);

    //create copy of last
    int *last = (int*)malloc(sizeof(int));
    *last = *top;

    //restoration
    while (!Empty(temp)) {
        Push(stk, Pop(temp));
    }
    FreeStack(temp);

    //return last value in stack
    return last;
}

// Copy a stack without modifying the original
//Deep-copy
Stack* CopyIntStack(Stack* stk) {
    //create two stacks
    Stack *copy = CreateStack();
    Stack *temp = CreateStack();

    // emptying original stack into temp
    while (!Empty(stk)) {
        Push(temp, Pop(stk));
    }

    // Restore original and build the copy
    while (!Empty(temp)) {
        //pointer for the value
        int* val = Pop(temp);

        //create copy of the value
        int* copiedVal = malloc(sizeof(int));
        *copiedVal = *val;

        Push(stk, copiedVal);   // Restore original
        Push(copy, val);        // Keep val from temp
    }

    free(temp);
    return copy;
}

// Check if stack is sorted in ascending order
int* StkSortUp(Stack* stk) {
    //if stack is empty return null
    if (Empty(stk)) return NULL;

    //create copy of the original stack
    Stack* copy = CopyIntStack(stk);

    //boolean flag for detection if stack not sorted up
    int* flag = (int*)malloc(sizeof(int));
    //initialize the flag
    *flag = 0;

    //pop the current value in stack
    int* prev = (int*)Pop(copy);

    //iteration
    while (!Empty(copy)) {
        //pop current value in top of stack
        int* curr = (int*)Pop(copy);

        //if current value smaller than previous value -> flag = true
        if (*curr < *prev) {
            *flag = 1;
        }

        *prev = *curr;
        //free value
        free(curr);
    }

    //free first value that got out from stack
    free(prev);
    //free copy stack
    free(copy);

    //if 1 means stack not ordered, else ordered
    return flag;
}

//function to calculate size of stack
int stackSize(Stack* stk) {
    //counter for calculate size of stack
    int count = 0;

    //check if stack is empty
    if (Empty(stk)){ return count;}

    //copy stack
    Stack *temp = CopyIntStack(stk);

    //iteration
    while (!Empty(temp)) {
        count++;
        int* x = Pop(temp);
        free(x);
    }

    //free stack pointer
    free(temp);

    //return counter
    return count;
}

//function to convert stack to array
int* stackToIntArray(Stack* stk) {
    if (Empty(stk)) return NULL;

    //calc size of stack
    const int size = stackSize(stk);
    //create new array and initialize it in zeros
    int *arr = (int*)calloc(size + 1,sizeof(int));
    //create index for the array, and initialize it to zero
    int index = 0;

    //create copy of stack
    Stack *copy = CopyIntStack(stk);

    //iteration
    while (!Empty(copy)) {
        //pop current value from stack
        int *x = Pop(copy);

        //fill the array
        *(arr + index) = *x;
        index++;

        //free the value from the stack
        free(x);
    }

    //free stack
    free(copy);

    //put size of array
    *(arr + index) = index;

    return arr;
}

int sortDownStk(Stack* stk) {
    //check if stack is empty
    if (Empty(stk)) return 0;

    //create cloning of original stack
    Stack *copy = CopyIntStack(stk);

    //boolean flag to detect if stack not sorted
    int flag = 0;

    //prev value from stack
    int* prev = Pop(copy);

    while (Empty(copy)) {
        int* current = Pop(copy);

        if (*current > *prev) {
            flag = 1;
        }

        *prev = *current;
        free(current);
    }

    free(prev);
    free(copy);

    return flag ? 0 : 1;
}

//declaration of the sort function even-odd
void sort_even_odd_private(Node** chain, const Node* pos, Node* even, Node* even_tail, Node* odd, Node* odd_tail);

void sort_even_odd(Node** chain) {
    //pointer for the list
    const Node* pos = *chain;

    //pointers for even list
    Node* even = NULL;
    Node* even_tail = NULL;

    //pointers for odd list
    Node* odd = NULL;
    Node* odd_tail = NULL;

    sort_even_odd_private(chain,pos,even,even_tail,odd,odd_tail);
}

void sort_even_odd_private(Node** chain, const Node* pos, Node* even, Node* even_tail, Node* odd, Node* odd_tail) {
    if (pos == NULL) {
        if (even != NULL && odd != NULL) {
            //connect between even list and odd list
            SetNextNode(even_tail, odd);
            //change head of original chain to be the head of even list
            *chain = even;
        }else if (even == NULL && odd != NULL) {
            //change head of original chain to be the head of odd list
            *chain = odd;
        }else if (even != NULL && odd == NULL) {
            *chain = even_tail;
        }
    }else {
        int* x = (int*)GetNodeValue(pos);
        Node* toAdd = CreateNode(x);

        //if its even value
        if (*x % 2 == 0) {
            //if even list is empty
            if (even == NULL) {
                even = toAdd;
                even_tail = even;
            }else {//if not
                SetNextNode(even_tail, toAdd);
                even_tail = GetNextNode(even_tail);
            }
        }else {//if its odd value
            //if odd list is empty
            if (odd == NULL) {
                odd = toAdd;
                odd_tail = odd;
            }else {//if not
                SetNextNode(odd_tail, toAdd);
                odd_tail = GetNextNode(odd_tail);
            }
        }

        //recursive call
        sort_even_odd_private(chain,GetNextNode(pos),even,even_tail,odd,odd_tail);
    }
}

void sumList_andUpdateTheList(Node* chain) {
    //dynamic sum var
    int* sum = (int*)malloc(sizeof(int));
    //initialize its value
    *sum = 0;
    //temporary pointer for iteration
    const Node* pos = chain;

    //iteration process
    while (pos != NULL) {
        //sum current node value
        *sum = *sum + *(int*)GetNodeValue(pos);
        //mv to the next node
        pos = GetNextNode(pos);
    }

    //free rest of the list
    freeList(GetNextNode(chain));

    // detach head from the rest (prevent dangling pointer)
    SetNextNode(chain, NULL);

    //update the head of the list to contain the sum var val
    SetNodeValue(chain,sum);
}

//function that calc size of list
int sizeOfList(const Node* chain) {
    //counter -> calc size
    int size = 0;
    //pointer for the head of the list
    const Node* pos = chain;

    //iteration
    while (pos != NULL) {
        size++;
        //mv to the next node
        pos = GetNextNode(pos);
    }

    //return the counter
    return size;
}

int* listToArray(const Node* chain) {
    //size of list
    const int size = sizeOfList(chain);

    //create new array
    int *arr = malloc(sizeof(int) * (size + 1));
    //index for the array
    int index = 0;

    //pointer for the list
    const Node* pos = chain;

    //iteration
    while (pos != NULL) {
        //fill the array
        *(arr + index) = *(int*)GetNodeValue(pos);
        index++;

        //mv to the next node
        pos = GetNextNode(pos);
    }

    //add the size of the array to the end
    *(arr + 0) = size;

    //return the array
    return arr;
}

int isListPalindrome(const Node* chain) {
    //create array from list and add a pointer to it
    int* arr = listToArray(chain);
    //size of array
    const int size = arr[0];
    //boolean flag to detect if list is not palindrome
    int flag = 0;

    //iteration
    for (int i = 1; i < size / 2; i++) {
        if (*(arr + i) != *(arr + size - i - 1)) {
            flag = 1;
        }
    }

    //free array
    free(arr);

    return flag ? 0 : 1;
}

//O(n) -> n = number of nodes in queue, worst case scenario full iteration
int sortQsmlBig_and_returnIndex(Queue *q, int* num) {
    //create q for big val
    Queue *big = CreateQueue();
    //create q for sml vals
    Queue *small = CreateQueue();
    //index for num
    int index = 0;

    //iteration
    while (!QIsEmpty(q)) {
        //catch current number
        int *current = Poll(q);
        //if the current value bigger than num add it to the big-numbers queue,
        //else to the small-numbers queue
        if (*current >= *num) {
            Offer(big, current);
        }else if (*current < *num) {
            Offer(small, current);
        }
    }

    //restoration - empty the small vals q into original queue
    while (!QIsEmpty(small)) {
        Offer(q,Poll(small));
        index++;
    }

    //offer the number the function received to the q
    Offer(q,num);

    //restoration - empty the big vals q into original queue
    while (!QIsEmpty(big)) {
        Offer(q,Poll(big));
    }

    //return index of num
    return index;
}

int AllStacksInListAreEven(const Node* chain) {
    //pointer for the chain
    const Node* pos = chain;

    //iteration
    while (pos != NULL) {
        //temp stack
        Stack* copy = CopyIntStack(GetNodeValue(pos));

        //iteration on copy-stack
        while (!Empty(copy)) {
            //pop current value from stack
            int* val = Pop(copy);

            if (*val % 2 != 0) { return 0; }

            //free it from memory
            free(val);
        }

        //free copy-stack from memory
        free(copy);
        //mv to the next node in list
        pos = GetNextNode(pos);
    }
    return 1;
}

int listOfPerfectEvenQueue(const Node* chain) {
    //boolean flag for detect not valid stack
    int flag = 0;
    //pointer to the list
    const Node* pos = chain;

    //iteration
    while (pos != NULL) {
        //pointer for current queue
        Queue *q = GetNodeValue(pos);
        //temporary queue for restoration
        Queue *temp = CreateQueue();

        //iteration on current queue
        while (!QIsEmpty(q)) {
            //poll out value from current queue
            int* val = Poll(q);

            //check if current val is even
            if (*val % 2 != 0) {
                flag = 1;
            }

            //offer it to the temporary queue for future restoration
            Offer(temp, val);
        }

        //restore current queue
        while (!QIsEmpty(temp)) {
            Offer(q,Poll(temp));
        }

        //free assist queue
        free(temp);

        //mv to next node
        pos = GetNextNode(pos);
    }

    //if flag == 1 none valid stack detected return false else return true
    return flag? 0 : 1;
}

int bFirstSearch(BinNode *root) {
    //Queue for iteration on the tree
    Queue *q = CreateQueue();
    //offer the root of the tree to the queue
    Offer(q,root);

    //iteration
    while (!QIsEmpty(q)) {
        //poll out current value from the current BinNode in the queue
        const BinNode *current = Poll(q);
        //current->value
        const int num = *(int*)GetBinNodeValue(current);

        if (num % 2 != 0) {
            //free all queue BinNodes
            FreeQueue(q);
            //return false
            return 0;
        }

        //if there are sons offer them too, to the queue
        //current->left != NULL
        if (HasLeft(current)) { Offer(q, GetLeft(current)); }

        //current->right != NULL
        if (HasRight(current)) { Offer(q, GetRight(current)); }
    }

    //free queue
    free(q);

    //return true
    return 1;
}

//O(n)
//check if queue is sorted according to its stacks sizes
int QueueOfStacksSortedUp(Queue *q) {
    //queue for restoration
    Queue *temp = CreateQueue();
    //boolean flag for detect if queue is not sorted according to its stacks sizes
    int flag = 0;

    //poll out current stack from queue
    Stack *prevStack = Poll(q);
    //calc its size
    int prevSize = StackSize(prevStack);
    //offer current stack to the temporary assist queue
    Offer(temp, prevStack);

    while (!QIsEmpty(q)) {
        //poll out current stack from queue
        Stack *currStack = Poll(q);
        //calc its size
        const int currSize = StackSize(currStack);

        //if current size of stack smaller than previous stack size -> flag = true -> queue of stack not sorted
        if (currSize < prevSize) { flag = 1; }

        //offer current stack to the temporary assist queue
        Offer(temp, currStack);
        prevSize = currSize;
    }

    //restoration
    while (!QIsEmpty(temp))
        Offer(q,Poll(temp));

    //if flag equals to 1 , queue is not sorted -> false, else if flag equals to 0, queue is sorted -> true
    return flag? 0 : 1;
}

//check if stuck is sorted
int IsStkSorted(Stack *stk) {
    //stack assist for future restoration
    Stack *temp = CreateStack();
    //boolean flag to detect unsorted stack
    int flag = 0;

    //pop out val from stack
    int *prev = Pop(stk);

    //push it to assist stack
    Push(temp,prev);

    //iteration
    while (!Empty(stk)) {
        //pop out val from stack
        int *curr = Pop(stk);

        //check the condition
        if (*curr < *prev) { flag = 1; }

        //prev value = current value
        *prev = *curr;

        //push it to assist stack
        Push(temp, curr);
    }

    //restoration
    while (!Empty(temp)) { Push(temp, Pop(temp)); }

    //free stack assist
    free(temp);

    //if flag == 1 means it detected stack that not sorted
    return flag? 0 : 1;
}

//last in stack
void* LastInStack(const Stack *stk) { return GetBottom(stk); }

int HowManyNumFoundInList(const Node *chain, const int num) {
    //pointer to the list
    const Node *pos = chain;
    //counter
    int count = 0;

    //iteration
    while (pos != NULL) {
        //if num found count it
        if (*(int*)GetNodeValue(pos) == num) { count++; }
        //mv to the next node
       pos =  GetNextNode(pos);
    }

    //return count
    return count;
}

int AllNegFoundOnce(const Node *chain) {
    //pointer to the list
    const Node *temp = chain;

    //iteration
    while (temp != NULL) {
        //current num
        const int *num = GetNodeValue(temp);

        //if number is negative
        if (*num < 0) {
            //calc how many times num found in list
            const int how = HowManyNumFoundInList(chain,*num);
            //if its found more than once return false
            if (how > 1) { return 0; }
        }

        //mv to the next node
        temp = GetNextNode(temp);
    }

    //return true
    return 1;
}

//O(n) or *O(log n) - only if sorted bst, function that calculate the average of binary tree of numbers
double CalcTreeAvg(BinNode *root) {
    //counter for the BinNodes
    double count = 0;
    //sum
    double sum = 0;

    //temporary queue for iteration
    Queue *q = CreateQueue();
    //offer the root of the tree to the queue
    Offer(q,root);

    //iteration on the tree
    while (!QIsEmpty(q)) {
        //poll current binnode from the queue
        const BinNode *curr = Poll(q);
        //count it
        count++;
        //sum up its value
        sum += *(double*)GetBinNodeValue(curr);

        //offer sons to the queue
        //offer left son
        if (HasLeft(curr)) { Offer(q, GetLeft(curr)); }
        //offer right son
        if (HasRight(curr)) { Offer(q, GetRight(curr)); }
    }

    //free the queue
    free(q);

    //return the average
    return sum / count;
}

//O(n) - function that calc how many BinNodes there values are above the average
int HowManyBinAboveAvg(BinNode *root) {
    //counter for the BinNodes that their values above average
    int count = 0;
    //calc avg
    const double avg = CalcTreeAvg(root);

    //temporary queue for iteration
    Queue *q = CreateQueue();
    //offer the root of the tree to the queue
    Offer(q,root);

    //iteration on the tree
    while (!QIsEmpty(q)) {
        //poll current binnode from the queue
        const BinNode *curr = Poll(q);

        //if current BinNode value above average count it
        if (*(int*)GetBinNodeValue(curr) > avg) { count++; }

        //offer sons to the queue
        //offer left son
        if (HasLeft(curr)) { Offer(q, GetLeft(curr)); }
        //offer right son
        if (HasRight(curr)) { Offer(q, GetRight(curr)); }
    }

    //free the queue
    free(q);

    //return the num of bins that their vals above avg
    return count;
}

//O(n)^2 - function that return pointer to Node in list, that points to tree with the highest
//number of BinNodes that there values are above the average
Node* TreesWithMaxBins(Node *chain) {
    //initialize max var to be zero
    int max = 0;

    //pointer for the list of trees
    Node *pos = chain;

    //pointer that will point to the node that points to tree with maximum BinNodes that their vals above avg
    Node *final = NULL;

    //iteration
    while (pos != NULL) {
        //pointer : BinNode, to current tree
        BinNode *tempTree = GetNodeValue(pos);
        //calc num of BinNodes that their vals above avg
        const int howMany = HowManyBinAboveAvg(tempTree);

        //save the node that points to the tree with the maximum BinNodes that their vals above avg
        if (howMany > max) { max = howMany; final = pos; }

        //mv to the next node
        pos = GetNextNode(pos);
    }

    //return pointer that will point to the node that points to
    //the tree with the maximum BinNodes that their vals above avg
    return final;
}

//Generic function for adding Node to list
int AddVoidVal3Cases(Node **chain, void *val, const int position) {
    //if position of adding is not valid , -> false
    if ( position < 1 || position > sizeOfList(*chain) + 1) { return 0; }

    //Create Node for future adding
    Node *toAdd = CreateNode(val);

    //if is the position of adding equals to one, add it to the start of the list toAdd will be the new head
    if (position == 1 || *chain == NULL) {
        //connect them
        SetNextNode(toAdd, *chain);
        //update the head pointer, to point to toAdd
        *chain = toAdd;
        //exit -> return true
        return 1;
    }

    //Create pointer for iteration
    Node *pos = *chain;
    //counter, till position
    int count = 0;

    //iteration
    while (pos != NULL) {
        //if I add 1 to the counter and its value will be equals to position
        //its mean that the while loop reach the correct place for adding the new node
        if (count + 1 == position) {
            //firstly: connect toAdd to next node
            SetNextNode(toAdd, GetNextNode(pos));
            //Secondly: connect current node to toAdd
            SetNextNode(pos, toAdd);
            //exit -> return true
            return 1;
        }

        //count + 1
        count++;
        //mv to the next node
        pos = GetNextNode(pos);
    }
    //exit -> return false
    return 0;
}

//remove value from list
void RemoveFirstOccurrence0fNumberFromList(Node **chain, const int number) {
    //if the number attend for removal found first in the list
    //change the head pointer to point to the next node
    if (*(int*)GetNodeValue(*chain) == number) {
        //pointer to the first node in list that chain points to it
        Node *temp = *chain;
        //make The Pointer to the list points to the next node
        *chain = GetNextNode(*chain);
        //free the number in node
        free(GetNodeValue(temp));
        //free the node that removed from the list
        free(temp);
        //exit
        return;
    }

    //pointer for the head of the list
    Node *pos = *chain;

    //iteration on list searching for the value to remove
    while (pos != NULL) {
        //if the next node contains the value, connect this current node to the next node after the node
        //I want to remove
        if (GetNextNode(pos) != NULL && *(int*)GetNodeValue(GetNextNode(pos)) == number) {
            //point to the node the function wants to remove
            Node *temp = GetNextNode(pos);
            //connect this current node to the next node after the node the function wants to remove
            SetNextNode(pos, GetNextNode(GetNextNode(pos)));
            //free the number in node
            free(GetNodeValue(temp));
            //free node from memory
            free(temp);
            //exit
            return;
        }

        //mv to the next node
        pos = GetNextNode(pos);
    }
}


int main() {

    // ---------------- Oop Test ----------------
    printf("=== 'OOP' Example ===\n");
    // יצירת "אובייקטים"
    Animal* dog = CreateDog();
    Animal* cat = CreateCat();

    // שימוש כמו ב-OOP
    dog->speak();          // Woof!
    dog->eat("bones");     // The dog eats bones.

    cat->speak();          // Meow!
    cat->eat("fish");      // The cat eats fish.

    // שחרור זיכרון
    free(dog);
    free(cat);

    // ---------------- Node Test ----------------
    printf("=== Node Example ===\n");
    Node* node1 = CreateNode("First");
    Node* node2 = CreateNode("Second");
    SetNextNode(node1, node2);

    printNode(node1);

    free(node2);
    free(node1);

    // ---------------- BinNode Test ----------------
    printf("\n=== BinNode Example ===\n");
    BinNode* root = CreateBinNode("Root");
    BinNode* left = CreateBinNode("Left");
    BinNode* right = CreateBinNode("Right");

    SetLeft(root, left);
    SetRight(root, right);

    printTree(root);

    free(left);
    free(right);
    free(root);

    // ---------------- Stack Test ----------------
    printf("\n=== Stack Example ===\n");
    Stack* stk = CreateStack();
    int* num1 = (int*)malloc(sizeof(int));
    *num1 = 0;
    int* num2 = (int*)malloc(sizeof(int));
    *num2 = 2;
    int* num3 = (int*)malloc(sizeof(int));
    *num3 = 3;
    int* num4 = (int*)malloc(sizeof(int));
    *num4 = 6;
    Push(stk, num1);
    Push(stk, num2);
    Push(stk, num3);
    Push(stk, num4);

    int* evenCount = evenStk(stk);
    printf("Number of even numbers: %d\n", *evenCount);

    int* last = lastInStack(stk);
    printf("Last in stack: %d\n", *last);

    free(evenCount);
    free(last);
    free(num1);
    free(num2);
    free(num3);
    free(stk);

    // ---------------- Queue Test ----------------
    printf("\n=== Queue Example ===\n");
    Queue* q = CreateQueue();
    Offer(q, "1");
    Offer(q, "2");
    Offer(q, "3");

    while (!QIsEmpty(q)) {
        char* val = (char*)Poll(q);
        printf("Polled: %s\n", val);
    }
    free(q);

    // ---------------- Node Test ----------------
    printf("=== Node Example n.2 ===\n");
    int* one = (int*)malloc(sizeof(int));
    *one = 1;
    int* two = (int*)malloc(sizeof(int));
    *two = 2;
    int* three = (int*)malloc(sizeof(int));
    *three = 3;
    int* four = (int*)malloc(sizeof(int));
    *four = 4;
    Node* node = CreateNode(one);
    Node* n2 = CreateNode(two);
    Node* n3 = CreateNode(three);
    Node* n4 = CreateNode(four);
    SetNextNode(node, n2);
    SetNextNode(n2, n3);
    SetNextNode(n3, n4);
    SetNextNode(n4, NULL);

    printf("list pre: %s\n","");
    printIntNode(node);
    //sumList_andUpdateTheList(node);
    //printIntNode(node);
    printf("list post: %s\n","");
    sort_even_odd(&node);
    printIntNode(node);
    printf("size of list is: %d\n",sizeOfList(node));
    freeList(node);

    // ---------------- Pointer to function Test ----------------
    //create numbers: firstNum = 17, secondNum = 18, thirdNum = 19, fourthNum = 25
    int *firstNum = (int*)malloc(sizeof(int));
    *firstNum = 17;
    int *secondNum = (int*)malloc(sizeof(int));
    *secondNum = 18;
    int *thirdNum = (int*)malloc(sizeof(int));
    *thirdNum = 19;
    int *fourthNum = (int*)malloc(sizeof(int));
    *fourthNum = 25;

    /*
    {       17 <-- [root1] <-- [first node of chain], ... [sec node of chain] --> (another tree) };
           /  \
          18   19 <--[root3]
         /
        25 <--[root4]

     */

    //create tree:
    BinNode* root1 = CreateBinNode(firstNum);
    BinNode* root2 = CreateBinNode(secondNum);
    BinNode* root3 = CreateBinNode(thirdNum);
    BinNode* root4 = CreateBinNode(fourthNum);
    SetLeft(root1, root2);
    SetRight(root1, root3);
    SetLeft(root2, root4);

    //create list
    Node* chain = CreateNode(root1);

    //pointer to function
    Node * (*func_ptr)(Node *) = TreesWithMaxBins;

    //call the function
    Node *final = func_ptr(chain);


    return 0;
}
