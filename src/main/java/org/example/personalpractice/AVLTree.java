package org.example.personalpractice;

import java.util.Stack;

public class AVLTree<T extends Comparable<T>> {
    private BinNode<T> root; // שורש העץ

    // פעולה להוספת ערך לעץ
    public void insert(T value) {
        root = insert(root, value); // מתחילים להכניס מהשורש
    }

    // פעולה להוספת ערך לעץ בתנאים של AVL
    //using bst algoritem + call the function for balncing the tree after adding the new binnode
    private BinNode<T> insert(BinNode<T> root, T value) {

        if (root == null) {
            return new BinNode<>(value); // אם הגענו למקום ריק, יוצרים את הצומת החדש
        }

        BinNode<T> current = root;

        while (current != null) {
            int compareResult = value.compareTo(current.getValue());

            if (compareResult < 0) { // אם הערך קטן מהערך של הצומת הנוכחי
                if (current.getLeft() == null) {
                    current.setLeft(new BinNode<>(value)); // מוסיפים את הצומת החדש
                    break;
                }
                current = current.getLeft();
            } else if (compareResult > 0) { // אם הערך גדול מהערך של הצומת הנוכחי
                if (current.getRight() == null) {
                    current.setRight(new BinNode<>(value)); // מוסיפים את הצומת החדש
                    break;
                }
                current = current.getRight();
            } else {
                return root; // אם הערך כבר קיים, אין צורך להוסיף
            }
        }

        return balance(root); // אחרי ההוספה, מבצעים איזון של העץ
    }

    // פעולה לאיזון העץ אחרי כל הוספה או מחיקה
    private BinNode<T> balance(BinNode<T> root) {
        int balance = getBalanceFactor(root); // מחשבים את האיזון של הצומת

        if (balance > 1) { // עץ גבוה מדי בצד שמאל
            if (getBalanceFactor(root.getLeft()) < 0) {
                root.setLeft(leftRotate(root.getLeft())); // סיבוב שמאלי
            }
            return rightRotate(root); // סיבוב ימני
        }

        if (balance < -1) { // עץ גבוה מדי בצד ימין
            if (getBalanceFactor(root.getRight()) > 0) {
                root.setRight(rightRotate(root.getRight())); // סיבוב ימני
            }
            return leftRotate(root); // סיבוב שמאלי
        }

        return root; // אם אין צורך בשום סיבוב
    }

    // פעולת סיבוב ימני
    private BinNode<T> rightRotate(BinNode<T> y) {
        BinNode<T> x = y.getLeft();
        BinNode<T> T2 = x.getRight();

        x.setRight(y);
        y.setLeft(T2);

        return x; // מחזירים את הצומת החדש שהפך לשורש
    }

    // פעולת סיבוב שמאלי
    private BinNode<T> leftRotate(BinNode<T> x) {
        BinNode<T> y = x.getRight();
        BinNode<T> T2 = y.getLeft();

        y.setLeft(x);
        x.setRight(T2);

        return y; // מחזירים את הצומת החדש שהפך לשורש
    }

    // חישוב מדד האיזון של הצומת
    private int getBalanceFactor(BinNode<T> node) {
        if (node == null) {
            return 0;
        }
        return height(node.getLeft()) - height(node.getRight()); // גובה הצד השמאלי פחות גובה הצד הימני
    }

    // חישוב הגובה של הצומת
    private int height(BinNode<T> node) {
        // אם הצומת ריק, הגובה הוא 0
        int height = 0;

        // ניצור pointer חדש כדי לנוע בעץ מבלי לשנות את הצומת המקורי
        BinNode<T> currentNode = node;

        // נמשיך כל עוד יש צומת לא ריק
        while (currentNode != null) {
            // אם יש צומת שמאלי, נזוז לשם
            if (currentNode.hasLeft()) {
                currentNode = currentNode.getLeft();
            }
            // אם אין שמאלי, נזוז לימין
            else {
                currentNode = currentNode.getRight();
            }
            height++; // כל פעם שהצלחנו לעבור לצומת, נגדיל את הגובה
        }
        return height;
    }

    // פעולה להדפסת העץ בסדר-על (in-order)
    public void printInOrder() {
        printInOrder(root); // מתחילים מהשורש
    }

    // פעולת InOrder ללא רקורסיה (באמצעות מחסנית)
    private void printInOrder(BinNode<T> root) {
        Stack<BinNode<T>> stack = new Stack<>();
        BinNode<T> currentNode = root;

        // כל עוד יש צמתים לעבור עליהם
        while (currentNode != null || !stack.isEmpty()) {
            // עובר שמאלה עד שאין עוד צומת שמאלי
            while (currentNode != null) {
                stack.push(currentNode);  // שומר את הצמתים במחסנית
                currentNode = currentNode.getLeft();  // עובר שמאלה
            }

            // עכשיו, הצומת הנוכחי הוא הצומת שמחייב הדפסה
            currentNode = stack.pop();  // שולף את הצומת הכי אחרון שנשמר במחסנית
            System.out.println(currentNode.getValue());  // הדפסה של הערך

            // אחרי שהדפסנו, נעבור לימין
            currentNode = currentNode.getRight();
        }
    }
}
