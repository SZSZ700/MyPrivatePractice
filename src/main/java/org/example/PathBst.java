package org.example;

public class PathBst {
    // הפנייה לשורש העץ
    private BinNode<Path> root;
    //מרחק אווירי
    private double airlen;

    //בנאי
    public PathBst(double airlen) {
        //אתחול הפנייה לשורש העץ
        this.root = null;
        //אתחול מרחק אווירי
        this.airlen = airlen;
    }

    // הוספת מסלול לעץ לפי שקלול  (מרחק וקפיצות)
    public void addPath(Path newPath) {
        BinNode<Path> toAdd = new BinNode<>(newPath);

        //אם עץ ריק נוסיף מסלול לעץ וניצא
        if (root == null) {
            root = toAdd;
            return;
        }

        //פויינטר לאיטרציה על העץ
        BinNode<Path> current = root;

        while (true) {
            //קבלת מסלול מחולייה נוכחית
            Path currPath = current.getValue();

            // אם המרחק היבשתי של המסלול להוספה קטן יותר מהמרחק היבשתי של המסלול הנוכחי
            // או מספר הקפיצות (שינויי מרחקים) במסלול להוספה קטן יותר ממספר הקפיצות של המסלול הנוכחי
            //נלך שמאלה
            if (newPath.getRealDistance() < currPath.getRealDistance() ||
                    (newPath.getRealDistance() == currPath.getRealDistance() &&
                            newPath.getJumps() < currPath.getJumps())) {

                //אם אנחנו בעלה נוסיף חולית מסלול להיות הבן השמאלי של העלה
                if (current.getLeft() == null) {
                    // הוספה
                    current.setLeft(toAdd);
                    // יציאה מהפונקציה
                    return;
                } else {
                    //נלך שמאלה
                    current = current.getLeft();
                }

                //אחרת נלך ימינה
            } else {
                // אם אנחנו בעלה נוסיף חולית מסלול להיות הבן הימני של העלה וניצא מהפונקצייה
                if (current.getRight() == null) {
                    // הוספה
                    current.setRight(toAdd);
                    //יציאה מהפונקציה
                    return;
                } else {
                    // אחרת נלך ימינה
                    current = current.getRight();
                }
            }
        }
    }

    //  פונקצייה עוטפת למחיקת דרך לפי מס מסלול
    public void removePathById(int id) {
        root = removeRecursive(root, id);
    }

    // פונקציית עיקרית למחיקה
    private BinNode<Path> removeRecursive(BinNode<Path> root, int id) {
        if (root == null) return null;

        if (id < root.getValue().getRoadId()) {
            //התקדמות לצד השמאלי של החולייה
            root.setLeft(removeRecursive(root.getLeft(), id));
        } else if (id > root.getValue().getRoadId()) {
            // התקדמות לצד הימני של החולייה
            root.setRight(removeRecursive(root.getRight(), id));
        } else {
            // צומת ללא בנים
            if (root.getLeft() == null && root.getRight() == null){ return null; }

            // צומת עם בן אחד
            if (root.getLeft() == null) return root.getRight();
            if (root.getRight() == null) return root.getLeft();

            // צומת עם שני בנים – מציאת מחליף
            BinNode<Path> successor = getMinNode(root.getRight());
            root.setValue(successor.getValue());
            root.setRight(removeRecursive(root.getRight(), successor.getValue().getRoadId()));
        }

        return root;
    }

    // מציאת הצומת השמאלי ביותר
    private BinNode<Path> getMinNode(BinNode<Path> node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    // החזרת הדרך היציבה והקצרה ביותר
    public Path findBestPath() {
        if (root == null) return null;

        BinNode<Path> current = root;

        // הדרך הכי טובה תהיה בצד שמאל של העץ לפי ההשוואות שלנו
        while (current.getLeft() != null) {
            current = current.getLeft();
        }

        return current.getValue();
    }

}
