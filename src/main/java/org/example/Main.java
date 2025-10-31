package org.example;
import sun.misc.Unsafe;
import java.awt.*;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.crypto.Cipher;
import javax.swing.Timer;
import javax.swing.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
/*
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
*/

public class Main {

    public static boolean listIsPali(LinkedList<Integer> lst) {

        if (lst.size() <= 1) {
            return true;
        }

        for (int i = 0; i < lst.size(); i++) {
            if (lst.get(i) != lst.get(lst.size() - i - 1)) {
                return false;
            }
        }
        return true;
    }

    public static LinkedList<Integer> why(LinkedList<Integer> lst) {
        LinkedList<Integer> newey = new LinkedList<>();

        if (lst.get(0) % 2 == 0 && !lst.isEmpty()) {

            for (int i = 0; i < lst.size(); i++) {
                if (lst.get(i) % 2 == 0) {
                    newey.add(lst.get(i));
                } else {
                    lst.remove(i);
                    i--;
                }
            }
        } else if (lst.get(0) % 2 == 1 && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); i++) {
                if (lst.get(i) % 2 == 1) {
                    newey.add(lst.get(i));
                } else {
                    lst.remove(i);
                    i--;
                }
            }
        }
        return newey;
    }

    public static boolean ispaliq(Queue<Integer> q) {
        Queue<Integer> temp = new LinkedList<>();
        int length = q.size();
        int count = 0;

        if (length == 1) {
            return true;
        }

        while (length > 1) {
            //poll first node
            int first = q.poll();
            length--;

            while (!q.isEmpty() && count < length - 1) {
                temp.offer(q.poll());
                count++;
            }

            //poll last node
            int last = q.poll();
            length--;
            count = 0;

            //if not equal return false
            if (first != last) {
                return false;
            }

            //restore the original q
            while (!temp.isEmpty()) {
                q.offer(temp.poll());
            }
        }
        return true;
    }

    public static Queue<Integer> cloneQueue(Queue<Integer> q) {
        Queue<Integer> temp = new LinkedList<>();
        Queue<Integer> copy = new LinkedList<>();

        while (!q.isEmpty()) {
            int x = q.poll();
            temp.offer(x);
            copy.offer(x);
        }

        //restore q
        while (!temp.isEmpty()) {
            q.offer(temp.poll());
        }

        //return copy of the queue
        return copy;
    }

    public static boolean perfectlyS(Stack<Integer> s4) {
        Stack<Integer> copy = cloneStack(s4);

        while (!copy.empty()) {
            int x = copy.pop();

            if (x % 2 == 1) {
                return false;
            }
        }
        return true;
    }

    // פונק המחזירה את המיקום של מס במחסנית מתחילת המחסנית
    public static int firstPlace(Stack<Integer> s4, int num) {
        Stack<Integer> copy = cloneStack(s4);
        int count = 0;

        while (!copy.empty()) {
            int x = copy.pop();
            count++;
            if (x == num) {
                return count; // מחזיר את המיקום (מתחיל מ-1)
            }
        }
        return -1; // אם לא נמצא
    }

    // פונק המחזירה את המיקום של מס במחסנית מסופה
    public static int lastPlace(Stack<Integer> s4, int num) {
        Stack<Integer> copy = cloneStack(s4);
        Stack<Integer> temp = new Stack<>();
        int count = 0;

        // הפיכת סדר המחסנית כדי להתחיל מהסוף
        while (!copy.empty()) {
            temp.push(copy.pop());
        }

        while (!temp.empty()) {
            int x = temp.pop();
            count++;
            if (x == num) {
                return count; // מחזיר את המיקום (מתחיל מ-1)
            }
        }
        return -1; // אם לא נמצא
    }

    // פונקציה המחזירה את המרחק הכי קטן במחסנית בין שני מרחקים
    public static int dis(Stack<Integer> s4) {
        Stack<Integer> c = cloneStack(s4); // שיבוט המחסנית

        int dis1 = 0;
        int dis2 = 0;
        int totalDis = 0;
        int minDis = Integer.MAX_VALUE; // מתחילים מהערך הגדול ביותר למינימום
        int length = c.size(); // אורך המחסנית המקורית
        int x = 0;

        // מעבר על שאר האלמנטים במחסנית
        while (!c.empty()) {
            x = c.pop(); // שליפת אלמנט נוסף
            if (c.contains(x)) {
                dis1 = firstPlace(s4, x);
                dis2 = lastPlace(s4, x);
                // חישוב המרחק בין המופע הראשון למופע האחרון
                totalDis = length - dis1 - dis2;
            }

            // עדכון המרחק המינימלי במידת הצורך
            if (totalDis < minDis) {
                minDis = totalDis;
            }
        }
        return minDis; // החזרת המרחק הקטן ביותר
    }

    //פונקציה הסורקת עץ מחסניות ומחזירה כמה מחסניות הן מחסניות זוגיות
    public static int maxStackUsingBFS(BinNode<Stack<Integer>> root) {
        Queue<BinNode<Stack<Integer>>> q = new LinkedList<>();//תור עזר לסריקה לרוחב
        q.offer(root);//הכנסת שורש לתור
        int c = 0;//מונה מחסניות זוגיות
        int count = 0;//מונה כמות ערכים זוגית

        while (!q.isEmpty()) {//לולאה שרצה כל עוד התור שבאמצעותו מבצעים סריקה לרוחב לא ריק
            BinNode<Stack<Integer>> current = q.poll();//שליפת הצומת הנוכחי בתור
            Stack<Integer> numy = new Stack<>();//מחסנית עזר
            int length = current.getValue().size();//שמירת אורך מחסנית

            //מעבר על המחסנית בצומת הנוכחית
            while (!current.getValue().empty()) {
                //שליפת ערך ממחסנית
                int x = current.getValue().pop();
                //עם הערך הנוכחי זוגי מנה אותו
                if (x % 2 == 0) {
                    count++;
                }
                numy.push(x);//הכנס אותו בכל מיקרה למחסנית עזר
            }

            //שיחזור מחסנית נוכחית
            while (!numy.empty()) {
                current.getValue().push(numy.pop());
            }

            //אם כמות החוליות הזוגיות שווה לאורך המחסנית משמע כל המחסנית מכילה ערכים זוגיים בלבד
            if (count == length) {
                c++;
            }
            count = 0;//איפוס מונה
            // הוספת  בנים לתור
            //אם להורה יש בן שמאלי הוסף אותו לתור
            if (current.getLeft() != null) {
                q.offer(current.getLeft());
            }
            //אם להורה יש בן ימני הוסף אותו לתור
            if (current.getRight() != null) {
                q.offer(current.getRight());
            }
        }
        return c;//החזר כמות מחסניות זוגיות
    }

    //פונקציה הבודקת אם צד שמאלי בעץ יחודי וצד ימני בעץ גם יחודי
    //תחילה פונקציית עזר לבדיקה אם ערך מסויים נמצא בעץ
    public static boolean foundy(BinNode<Integer> root, int num) {
        Queue<BinNode<Integer>> q = new LinkedList<>();//תור עזר לסריקה לרוחב
        q.offer(root);//הכנסת שורש לתור

        while (!q.isEmpty()) {//לולאה שרצה כל עוד התור שבאמצעותו מבצעים סריקה לרוחב לא ריק
            BinNode<Integer> current = q.poll();//שליפת הצומת הנוכחי בתור

            if (current.getValue() == num) {
                return true;
            }

            // הוספת  בנים לתור
            //אם להורה יש בן שמאלי הוסף אותו לתור
            if (current.getLeft() != null) {
                q.offer(current.getLeft());
            }
            //אם להורה יש בן ימני הוסף אותו לתור
            if (current.getRight() != null) {
                q.offer(current.getRight());
            }
        }
        return false;
    }

    public static boolean specialTree(BinNode<Integer> root) {
        //הצהרה על פוינטרים לצדדי העץ/בני העץ
        BinNode<Integer> leftRoot = null;
        BinNode<Integer> rightRoot = null;
        //חיבור הפוינטרים לבנים בהתאם
        //אם יש בן שמאלי, פוינטר עץ-שמאל יצביע עליו
        if (root.hasLeft()) {
            leftRoot = root.getLeft();
        }
        //אם יש בן ימני, פוינטר עץ ימין יצביע עליו
        if (root.hasRight()) {
            rightRoot = root.getRight();
        }
        //ניתוק שורש העץ מבניו
        root.setLeft(null);
        root.setRight(null);

        Queue<BinNode<Integer>> q = new LinkedList<>();//תור עזר לסריקה לרוחב
        q.offer(leftRoot);//הכנסת שורש לתור

        while (!q.isEmpty()) {//לולאה שרצה כל עוד התור שבאמצעותו מבצעים סריקה לרוחב לא ריק
            BinNode<Integer> current = q.poll();//שליפת הצומת הנוכחי בתור
            int x = current.getValue();//קבלת ערך איבר מעץ שמאל
            if (foundy(rightRoot, x)) {//בדיקה אם ערך זה נמצא בעץ ימין, אם כן החזר שקר
                return false;
            }

            // הוספת  בנים לתור
            //אם להורה יש בן שמאלי הוסף אותו לתור
            if (current.getLeft() != null) {
                q.offer(current.getLeft());
            }
            //אם להורה יש בן ימני הוסף אותו לתור
            if (current.getRight() != null) {
                q.offer(current.getRight());
            }
        }
        //קישור שורש העץ לבניו בחזרה
        root.setLeft(leftRoot);
        root.setRight(rightRoot);

        //החזר אמת כלומר עץ שמאל שונה מעץ ימין בערכיו
        return true;
    }

    //פונקציה שבודקת אם כל התורים בשרשרת החוליות הן תוריים זוגיים כלומר כל ערכי איבריהן הם זוגיים
    public static boolean perfectlyList(LinkedList<Queue<Integer>> lst) {
        //תור עזר להסרה ושיחזור
        Queue<Integer> temp = new LinkedList<>();

        int count = 0;//מונה חוליות זוגיות
        for (int i = 0; i < lst.size(); i++) {//מעבר על שרשרת החוליות

            int length = lst.get(i).size();//שמירת גודל תור נוכחי

            while (lst.get(i).isEmpty()) {//מעבר על התור הנוכחי
                int x = lst.get(i).poll();//שמירת ערך חוליה בתור נוכחי
                if (x % 2 == 0) {//בדיקה עם הערך השמור בחוליה זו הינו זוגי
                    count++;//אם ערך זה הוא זוגי מנה אותו
                }
                temp.offer(x);//הכנס בכל מיקרה את האיבר הוסר לתור עזר
            }

            //שחזר את כל החוליות מהתור עזר לתור הרגיל
            while (!temp.isEmpty()) {
                lst.get(i).offer(temp.poll());
            }

            //אם מונה הערכים הזוגיים בתור שונה מאורך התור כלומר לא כל חוליותיו הן זוגיות החזר שקר מיד
            if (count != length) {
                return false;
            }
            count = 0;//איפוס מונה ערכים זוגיים
        }
        return true;//אם לא חזר שקר, משמע שכל התורים בשרשרת חוליות הן זוגיים ולכן נחזיר אמת
    }

    //פונקציה המקבלת שרשרת חוליות של תורים ומחזירה שרשרת חווליות כאשר כל חוליה מכילה מחלקה המכילה מינימום ומקסימום בתור
    public static LinkedList<RangeNode> Range_N(LinkedList<Queue<Integer>> lst) {
        //crete the list we gonna return at the end of the proccess
        LinkedList<RangeNode> newey = new LinkedList<>();

        //iterate through the original list
        for (int i = 0; i < lst.size(); i++) {

            Queue<Integer> temp = new LinkedList<>();//queue assist
            //initialize min mav variables
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;

            //itirate throgh the current queue
            while (!lst.get(i).isEmpty()) {
                int x = lst.get(i).poll();
                if (x < min) {
                    min = x;
                }
                if (x > max) {
                    max = x;
                }
                temp.offer(x);//move it for the temp queue anyway
            }

            //restore current queue
            while (!temp.isEmpty()) {
                lst.get(i).offer(temp.poll());
            }

            RangeNode tempy = new RangeNode(min, max);//create the rangenode object
            newey.add(tempy);//insert it to the new list
        }
        return newey;//return the new list
    }

    //על אותו עיקרון כמו פונקצייה קודמת רק שהפעם זו היא שרשרת מחסניות
    public static LinkedList<RangeNode> Range_N2(LinkedList<Stack<Integer>> lst) {
        LinkedList<RangeNode> newey = new LinkedList<>();//create the new list we gonna return

        //iterate through the recived list
        for (int i = 0; i < lst.size(); i++) {
            //create assist stack
            Stack<Integer> temp = new Stack<Integer>();
            //initialize min to max-value and max to min-value
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            //itereate through the current stack
            while (!lst.get(i).empty()) {
                int x = lst.get(i).pop();
                if (x < min) {
                    min = x;
                }
                if (x > max) {
                    max = x;
                }
                temp.push(x);
            }
            //restore current stack
            while (!temp.empty()) {
                lst.get(i).push(temp.pop());
            }
            //create new rangenode object
            RangeNode tempy = new RangeNode(min, max);
            newey.add(tempy);
        }
        return newey;//return the new rangenode min-max list
    }

    public static int putInPlace(Queue<Integer> q, int num) {
        Queue<Integer> gdolim = new LinkedList<>();
        Queue<Integer> ktanim = new LinkedList<>();

        int place = 0;
        int count = 0;

        while (!q.isEmpty()) {
            int x = q.poll();

            if (x < num) {
                ktanim.offer(x);
                count++;
            } else {
                gdolim.offer(x);
            }
        }
        ktanim.offer(num);
        count++;
        place = count;//שמירת מיקומו של המס שהתווסף

        //העברת הקטנים כולל המס שהתווסף לתור
        while (!ktanim.isEmpty()) {
            q.offer(ktanim.poll());
        }
        //ולאחר מיכן העברת כל הגדולים
        while (!gdolim.isEmpty()) {
            q.offer(gdolim.poll());
        }
        return place;
    }

    public static void moveToFront(Queue<Integer> q, int k) {
        Queue<Integer> temp0 = new LinkedList<>();
        Queue<Integer> temp1 = new LinkedList<>();

        int count = 0;
        int length = q.size();
        while (!q.isEmpty() && count <= length - k) {
            int x = q.poll();
            count++;
            temp0.offer(x);
        }
        //המשך ריקון
        while (!q.isEmpty()) {
            temp1.offer(q.poll());
        }

        //שיחזור התור מקור כך שקיי האיברים מהסוף יהיו בהתחלה
        while (!temp1.isEmpty()) {
            q.offer(temp1.poll());
        }
        while (!temp0.isEmpty()) {
            q.offer(temp0.poll());
        }

    }

    public static void order(BinNode<Integer> chain) {

        // ראשי שרשראות עזר לזוגיים ולאי-זוגיים
        BinNode<Integer> evenChain = null;
        BinNode<Integer> oddChain = null;

        // משתנים לשמירת קצה השרשראות
        BinNode<Integer> lastEven = null;
        BinNode<Integer> lastOdd = null;

        // מעבר על השרשרת המקורית
        BinNode<Integer> current = chain;
        while (current != null) {
            if (current.getValue() % 2 == 0) {
                // אם הערך זוגי, נוסיף אותו לשרשרת הזוגיים
                if (evenChain == null) {
                    evenChain = new BinNode<>(current.getValue()); // תחילת השרשרת
                    lastEven = evenChain;
                } else {
                    lastEven.setRight(new BinNode<>(current.getValue()));
                    lastEven.getRight().setLeft(lastEven); // שמירת קשר דו-כיווני
                    lastEven = lastEven.getRight();
                }
            } else {
                // אם הערך אי-זוגי, נוסיף אותו לשרשרת האי-זוגיים
                if (oddChain == null) {
                    oddChain = new BinNode<>(current.getValue()); // תחילת השרשרת
                    lastOdd = oddChain;
                } else {
                    lastOdd.setRight(new BinNode<>(current.getValue()));
                    lastOdd.getRight().setLeft(lastOdd); // שמירת קשר דו-כיווני
                    lastOdd = lastOdd.getRight();
                }
            }
            current = current.getRight();
        }

        // חיבור שרשרת הזוגיים לשרשרת האי-זוגיים
        if (lastEven != null) {
            lastEven.setRight(oddChain); // חיבור האי-זוגיים לסוף של הזוגיים
            if (oddChain != null) {
                oddChain.setLeft(lastEven); // שמירה על הקשר הדו-כיווני
            }
        }

        //שינוי שרשרת מקורית
        BinNode<Integer> help = evenChain;
        BinNode<Integer> bdika = chain;
        while (bdika != null && help != null) {
            //שינוי ערכי שרשרת מקורית לערכי שרשרת זוגיים(שחוברה כבר לשרשרת האי זוגיים)
            bdika.setValue(help.getValue());
            //התקדם לחוליה הבאה בשרשרת הזוגיים-אי זוגיים
            if (help != null) {
                help = help.getRight();
            }
            //התקדם לחוליה הבאה בשרשרת המקוריים
            bdika = bdika.getRight();
        }
    }

    public static int countNodes(Node<Integer> list) {
        Node<Integer> pos = list;
        int count = 0;
        while (pos != null) {
            count++;
            pos = pos.getNext();
        }
        return count;
    }

    public static void removeAlly(Node<Integer> chain, int num) {
        Node<Integer> newHead = null; // מצביע לחוליה הראשונה בשרשרת החדשה
        Node<Integer> last = null; // מצביע לחוליה האחרונה בשרשרת החדשה
        Node<Integer> pos = chain; // מצביע לחוליה הנוכחית בשרשרת המקורית

        // יצירת השרשרת החדשה
        while (pos != null) {
            int x = pos.getValue(); // קבלת הערך הנוכחי
            if (x != num) { // אם הערך שונה מהמס המועבר
                // יצירת חוליה חדשה
                Node<Integer> newNode = new Node<>(x);
                if (newHead == null) { // אם זו החוליה הראשונה בשרשרת החדשה
                    newHead = newNode; // עדכון ראש השרשרת החדשה
                    last = newHead; // עדכון זנב השרשרת החדשה
                } else {
                    last.setNext(newNode); // קישור החוליה החדשה
                    last = newNode; // עדכון זנב השרשרת החדשה
                }
            }
            pos = pos.getNext(); // העברת המצביע לחוליה הבאה
        }

        // עדכון הרשימה המקורית כך שתשקף את השרשרת החדשה
        pos = newHead;
        Node<Integer> current = chain;
        Node<Integer> prev = null; // שמירה על החוליה הקודמת

        while (pos != null) {
            current.setValue(pos.getValue()); // העבר את הערכים מהשרשרת החדשה
            prev = current; // שמור את החוליה האחרונה שנשארה
            pos = pos.getNext();
            current = current.getNext();
        }

        // ניתוק שאר החוליות מהרשימה המקורית
        if (prev != null) {
            prev.setNext(null); // ניתוק כל החוליות המיותרות
        }


    }

    //פעולת עזר א- אמת או שקר אם מס נמצא בשרשרת סטטית
    public static boolean wasNot(Node<Integer> lst, int num) {
        Node<Integer> pos = lst;
        while (pos != null) {
            int x = pos.getValue();
            if (x == num) {
                return true; // num already exists in the list
            }
            pos = pos.getNext();
        }
        return false; // num not found in the list
    }

    //יצירת שרשרת סטטית לבקרה על מס שכבר היו כדי שלא תהיי כפילותיות
    private static Node<Integer> n = null;  // Static variable for new list
    private static Node<Integer> l = null;   // Static variable for the end of the new list

    //הזנה לתוך השרשרת הסטטית פעולת עזר ב
    public static boolean found(Node<Integer> lst, int num) {
        // If n is null, create the first node
        if (n == null) {
            n = new Node<>(num);
            l = n;  // Update last to the first node
            return true;  // Successfully added
        }
        // If n is not null, check if the number does not already exist
        else if (!wasNot(n, num)) {
            l.setNext(new Node<>(num)); // Add a new node
            l = l.getNext();            // Update last
            return true;                // Successfully added
        }
        return false; // Number already exists in the new list
    }

    //הפעולה העיקרית
    public static void buildFreqList(Node<Integer> chain) {
        //pointer for new temp list
        Node<Integer> newey = null;
        Node<Integer> last = null;

        Node<Integer> pos = chain;
        int count = 0;

        while (pos != null) {

            int x = pos.getValue();// Get current value

            Node<Integer> check = pos;
            if (found(n, x)) {//הכנסת המס למערכת הבקרה כדי לבדוק אם המס כבר היה
                // Count occurrences of x in the original chain
                while (check != null) {
                    if (check.getValue() == x) {
                        count++;
                    }
                    check = check.getNext();
                }

                // Add to the new list
                if (newey == null) {// New list is empty
                    newey = new Node<>(x);
                    last = newey;
                    last.setNext(new Node<>(count));// Add the count
                    last = last.getNext();// Move last pointer
                } else {// New list has elements
                    last.setNext(new Node<>(x));
                    last = last.getNext();
                    last.setNext(new Node<>(count));
                    last = last.getNext();
                }
            }

            count = 0;// Reset count for this number
            pos = pos.getNext();// Move to the next node in the original chain
            check = chain;// Pointer to count occurrences
        }

        // Update the original list with the new frequencies
        Node<Integer> prev = null;// Keep track of the last processed node
        Node<Integer> current = chain;// Start from the original chain
        Node<Integer> re = newey;
        while (current != null && re != null) {
            if (re != null) {
                current.setValue(re.getValue());//change the original list values to the newes one
            }
            prev = current;
            current = current.getNext();
            re = re.getNext();
        }

        if (prev != null) {
            //put the next of the last node to be null for disconnecting unwanted nodes
            prev.setNext(null);
        }

    }

    //פתירת תרגיל 5 אך תוך שימוש בספריות מוכנות
    private static LinkedList<Integer> e = new LinkedList<>();

    public static boolean find(LinkedList<Integer> ls, int num) {
        //if the static list e doesnt contain the num, add it and return true
        if (!e.contains(num)) {
            e.add(num);
            return true;
        }
        return false;
    }

    public static void buildFreq(LinkedList<Integer> lst) {
        LinkedList<Integer> li = new LinkedList<>();
        int count = 0;

        for (int i = 0; i < lst.size(); i++) {
            int x = lst.get(i);

            if (find(e, x)) {
                for (int j = 0; j < lst.size(); j++) {
                    if (lst.get(j) == x) {
                        count++;
                    }
                }
                //add to the new list
                li.add(x);
                li.add(count);
                count = 0;// initialize count to zero
            }
        }

        //update the original list
        for (int a = 0; a < li.size(); a++) {
            lst.set(a, li.get(a));
        }
        // מחיקת תאים עודפים אם הרשימה המקורית גדולה מהרשימה החדשה
        while (lst.size() > li.size()) {
            lst.removeLast();
        }
    }

    public static int mostpop(LinkedList<Integer> lst) {
        //buildFreq(lst);
        int max = Integer.MIN_VALUE;
        int numy = 0;

        for (int i = 0; i < lst.size(); i += 2) {
            int num = lst.get(i);
            int howMany = lst.get(i + 1);

            if (howMany > max) {
                max = howMany;
                numy = num;
            }
        }
        return numy;
    }

    public static int mostPopularNumber(Node<Integer> chain) {

        //buildFreqList(chain);

        Node<Integer> pos = chain;//poiner for new orderd list
        int numy = 0;// var for most popular num
        int max = Integer.MIN_VALUE;//initialize max to lower integer number

        //iterate through the list post order
        while (pos != null) {
            int num = pos.getValue();//get the value of the current node
            int howMany = pos.getNext().getValue();//get the counter(value) of num in the list

            if (howMany > max) {
                max = howMany;
                numy = num;
            }

            pos = pos.getNext().getNext();//get next next node
        }
        return numy;//return most popular number
    }

    public static void printListBin(BinNode<Integer> head) {
        BinNode<Integer> current = head;
        while (current != null) {
            System.out.print(current.getValue() + " ");
            current = current.getRight();
        }
        System.out.println();
    }

    public static <T> void printListNod(Node<T> temp) {
        Node<T> current = temp;
        System.out.print("chain->");
        while (current != null) {
            System.out.print(current.getValue() + "->");
            current = current.getNext();
        }
        System.out.print("null");
    }

    public static int what(Queue<Integer> q, int x) {

        if (q.isEmpty() || x != q.peek()) {
            return 0;
        }

        x = q.remove();

        return 1 + what(q, x);
    }

    public static int secret(Queue<Integer> q) {

        if (q.isEmpty()) {
            return 0;
        }

        int x = q.peek();
        int z = x * what(q, x);
        int y = 1 + secret(q);
        q.offer(z);

        return y;
    }

    //create new static list
    //its the main part of the system to check if num has been used
    private static Node<Integer> e2 = null;
    private static Node<Integer> f2 = null;

    public static boolean nimtsa(Node<Integer> lst, int num) {
        Node<Integer> pos = lst;// pointer to the chain
        while (pos != null) {//iterate throgh the chain
            if (pos.getValue() == num) {// if the current value of the binnode equals to num
                return true;
            }
            pos = pos.getNext();//get to the next node
        }
        return false;//else if we  found the number return false
    }

    public static boolean fo(Node<Integer> lst, int num) {
        if (e2 == null) {//if the static list is empty
            e2 = new Node<>(num);//add new node and point e2 to it
            f2 = e2;//point the tail f2 to the head e2
            return true;//return true that the number wasnt found
        } else if (!nimtsa(e2, num)) {//check if the number found in the static list
            f2.setNext(new Node<>(num));//if its not, create new node that contain the number(value)
            f2 = f2.getNext();//move forward with the tail pointer
            return true;//return true that the number wasnt found
        }
        return false;//it means that the number was found
    }

    public static void miyun(BinNode<Integer> chain) {
        //בהנחה שקיבלנו הפנייה לחולייה הכי שמאלית
        //create new doubly linkedlist
        BinNode<Integer> temp = null;
        BinNode<Integer> t1 = null;
        //pointer for iteration through the original list
        BinNode<Integer> pos = chain;
        int count = 0;// counter for how many the same number showing in the list
        while (pos != null) {
            int x = pos.getValue();//get the value of the binnode

            BinNode<Integer> check = chain;//pointer for the check while-loop

            if (fo(e2, x)) {
                //iterate throgh the chain searching for value that matches the number in x
                while (check != null) {
                    int k = check.getValue();//get the value of the binnode
                    if (k == x) {//if its equals to x count it
                        count++;
                    }
                    check = check.getRight();//move the pointer to the right side
                }

                //building the assist list
                BinNode<Integer> newnode1 = new BinNode<>(x);//binnode for the number
                BinNode<Integer> newnode2 = new BinNode<>(count);//binnode for the counter of the number

                if (temp == null) {//if the list is empty add new nodes then connect them
                    temp = newnode1;//create new node and point/atach temp to it
                    t1 = temp;//point the tail to the head
                    t1.setRight(newnode2);
                    t1.getRight().setLeft(temp);//connect between the two nodes
                    t1 = t1.getRight();//move the pointer to the right side
                } else {//if its not, add new nodes and connect them
                    t1.setRight(newnode1);//create new binnode and poin the tail pointer tp it
                    t1.getRight().setLeft(newnode1);
                    t1 = t1.getRight();
                    t1.setRight(newnode2);
                    t1.getRight().setLeft(newnode2);
                    t1 = t1.getRight();
                }
            }
            count = 0;// initialize count to 0
            pos = pos.getRight();// get to the next binnode
            check = chain;
        }

        //updateing the original doubly list
        BinNode<Integer> a = chain;//pointer for original list
        BinNode<Integer> b = temp;//pointer for the assist list
        BinNode<Integer> prev = null;//pointer for the rightest binnode in the list

        while (a != null && b != null) {
            if (b != null) {
                a.setValue(b.getValue());//updating the values of the original list
            }
            prev = a;
            //get to the right sides of both lists
            a = a.getRight();
            b = b.getRight();
        }
        //if the assist list shorter than the original one
        //it prevent from put nulls in their values
        //so it disconect the unwanted ones
        if (prev != null) {
            prev.setRight(null);
        }
    }

    //  func for finding the most showing number in a list
    public static int mostp(BinNode<Integer> lst) {
        miyun(lst);// ordet the list to be freqlist

        int max = Integer.MIN_VALUE;// initialize max to the lowest int number
        int numy = 0;//initialize numy to 0

        BinNode<Integer> pos = lst;//pointer for the original list so we not lose the  head ofc

        while (pos != null) {//iterate throgh the doubly linkedlist
            int x = pos.getValue();//get the value of the current binnode

            if (x > max) {//check if this value grater than max
                max = x;//if so it become the maximun/highest value showing in list
                numy = x;//save the highest showing number
            }
            pos = pos.getRight().getRight();// get to the next next binnode
        }
        return numy;// return the highest showing number
    }

    public static Queue<Integer> arrangeData(Queue<Integer> marks) {
        Queue<Integer> testN = new LinkedList<>();//תור מספרי בחינות עבור כל תלמיד
        Queue<Integer> temp = new LinkedList<>();//תור עזר לציונים

        int count = 0;

        while (!marks.isEmpty()) {

            int x = marks.poll();

            if (x != -1) {
                temp.offer(x);
                while (x != -1 && !marks.isEmpty()) {
                    x = marks.poll();
                    count++;//מנה את הציון
                    if (x != -1)
                        temp.offer(x);//הכנס אותו לתור עזר
                }
                //הגענו ל -1
                testN.offer(count);//נזין את המונה לתור המונים
                count = 0;//נאפס את המונה
            }

            if (!marks.isEmpty() && marks.peek() == -1) {
                testN.offer(count);//נזין את המונה לתור המונים
                marks.poll();
            }

        }

        //restore marks
        while (!temp.isEmpty()) {
            marks.offer(temp.poll());
        }
        return testN;
    }

    //פונק עזר 1-לבדיקה אם סכום כל הערכים שווים ל 0
    public static boolean allsumzero(Node<Integer> chain) {
        Node<Integer> pos = chain;
        int sum = 0;
        while (pos != null) {
            int x = pos.getValue();
            sum += x;

            pos = pos.getNext();
        }
        return sum == 0;
    }

    //פונק' עזר 2- מציאת איבר אחרון
    public static Node<Integer> getLast(Node<Integer> chain) {
        Node<Integer> last = chain;

        while (last.getNext() != null) {
            last = last.getNext();
        }

        return last;
    }

    //פונק' עזר 3- החזרת סכום כל הערכים
    public static int sumy(Node<Integer> chain) {
        int sum = 0;
        Node<Integer> pos = chain;

        while (pos != null) {
            int x = pos.getValue();
            sum += x;
        }
        return sum;
    }

    public static void balance(Node<Integer> chain) {
        int length = countNodes(chain);//size of list
        int d = sumy(chain);//new nodes to add for the total sum be 0
        Node<Integer> end = getLast(chain);//pointer to the last node

        Node<Integer> t1 = new Node<Integer>(d);
        Node<Integer> t2 = new Node<Integer>(-d);
        Node<Integer> t3 = new Node<Integer>(0);

        //if the length of the list is odd and total sum != 0
        if (length % 2 != 0 && !allsumzero(chain)) {
            if (d < 0) {
                end.setNext(t1);
                end = end.getNext();
            } else {
                end.setNext(t2);
                end = end.getNext();
            }
            return;
        } else if (length % 2 != 0 && allsumzero(chain)) {//אם אורך אי זוגי וסכום כל הספרות שווה ל 0 נוסיף חוליה עם ערך 0 לאיזון אורך
            end.setNext(t3);
            end = end.getNext();
            return;
        } else if (length % 2 == 0 && !allsumzero(chain)) {//אם אורך זוגי אך הסכום לא שווה 0 ניצטרך להוסיף שני חוליות כי אם נוסיף אחת הרסנו את הזוגיות של אורך השרשרת
            if (d < 0) {
                end.setNext(t1);
                end = end.getNext();
            } else {
                end.setNext(t2);
                end = end.getNext();
            }
            end.setNext(t3);
            end = end.getNext();
            return;
        }
    }

    public static boolean isSuper(Node<Integer> n) {
        int sum = 0;
        Node<Integer> pos = n;

        while (pos != null) {
            int x = pos.getValue();

            if (x <= sum) {
                return false;
            }

            sum += x;

            pos = pos.getNext();
        }
        return true;
    }

    public static boolean addToSuper(Node<Integer> n, int num) {
        if (!isSuper(n)) {
            return false;
        }

        int sum = 0;
        Node<Integer> pos = n;
        int next = 0;

        while (pos != null) {

            int current = pos.getValue();

            if (pos.getNext() != null) {
                next = pos.getNext().getValue();
            }

            if (num > current && num < next && current > sum) {
                Node<Integer> newey = new Node<Integer>(num);
                newey.setNext(pos.getNext());
                pos.setNext(newey);
                return true;
            }
            sum += current;

            pos = pos.getNext();
        }
        return false;
    }

    private static Node<Integer> Alist = null;
    private static Node<Integer> Alast = null;

    //check if num found in list
    public static boolean foundI(Node<Integer> chain, int num) {

        Node<Integer> pos = Alist;
        while (pos != null) {
            int x = pos.getValue();
            if (x == num) {
                return true;
            }
            pos = pos.getNext();
        }
        return false;
    }

    //add to static list only if the num is uniqe
    public static boolean addToStaticLi(Node<Integer> chain, int num) {
        Node<Integer> newey = new Node<>(num);
        if (Alist == null) {//if the static list is empty add new node and return true that num wasnt found in the list
            Alist = newey;
            Alast = Alist;
            return true;
        } else if (!foundI(Alist, num)) {//if the static list is not empty &  add new node and return true that num wasnt found in the list
            Alast.setNext(newey);
            Alast = Alast.getNext();
            return true;
        }
        return false;//the func couldnt add num cause it exist in the list the list should be uniqe
    }

    //recursion stack
    public static int secret(Stack<Integer> s1, Stack<Integer> s2) {
        if (s1.empty() || s2.empty()) {
            return 0;
        }

        if (s1.peek() > s2.peek()) {
            return s1.pop() + s2.peek() + secret(s1, s2);
        } else {
            return s1.peek() + s2.pop() + secret(s1, s2);
        }
    }

    public static Node<Integer> differenceList(Node<Integer> chain) {
        Node<Integer> newey = null;
        Node<Integer> last = null;

        Node<Integer> pos = chain;

        while (pos.getNext() != null) {
            int x1 = pos.getValue();
            int x2 = pos.getNext().getValue();
            int d = Math.abs(x1 - x2);

            if (newey == null) {
                newey = new Node<>(d);
                last = newey;
            } else {
                last.setNext(new Node<>(d));
                last = last.getNext();
            }
            pos = pos.getNext();
        }
        return newey;
    }

    public static void theSurvives(Node<Integer> chain) {
        int length = countNodes(chain);
        Node<Integer> result = chain;

        while (length > 1) {
            result = differenceList(result);
            printListNod(result);
            length--;
        }
    }

    //פונק עזר לחישוב סכום במחסנית
    public static int sumy(Stack<Integer> stk) {
        Stack<Integer> copy = cloneStack(stk);
        int sum = 0;

        while (!copy.empty()) {
            int x = copy.pop();
            sum += x;
        }
        return sum;
    }

    public static boolean some(Stack<Integer> stk) {
        //int sum = 0;
        Stack<Integer> copy = cloneStack(stk);
        copy.pop();
        while (!copy.empty()) {
            int x = copy.pop();//שליפת ערך ושמירה באיקס
            int d = sumy(copy);//סכימת מחסנית ללא איבר נוכחי שנישלף

            if (x < d) {
                return false;
            }
        }
        return true;
    }

    //סכום איברי תור
    public static int sumQueue(Queue<Integer> q) {
        Queue<Integer> temp = new LinkedList<>();//queuq assist
        int sum = 0;//var for sum up all values

        while (!q.isEmpty()) {//while the queue is not empty
            int x = q.poll();//poll cuurent value
            sum += x;//sum it up
            temp.offer(x);//offer the value to the assite queue
        }
        //restore original queue
        while (!temp.isEmpty()) {
            q.offer(temp.poll());
        }
        return sum;//return sum
    }

    public static int lastValue(Queue<Integer> q) {
        Queue<Integer> temp = new LinkedList<>();//queue assist
        int length = q.size();//save the length of the queue
        int count = 0;//counter till reach last value

        while (!q.isEmpty() && count < length - 1) {
            temp.offer(q.poll());
            count++;
        }
        int last = q.poll();//save last node value

        //restore original queue than add the last node for keeping the same order of the queue
        while (!temp.isEmpty()) {
            q.offer(temp.poll());
        }
        q.offer(last);

        return last;//return last queue node value
    }

    public static Queue<Integer> ny(Node<Queue<Integer>> chain) {
        Queue<Integer> newey = new LinkedList<>();//create new queue to return at the end of the procces
        Node<Queue<Integer>> pos = chain;//pointer for iteration through the linkedlist

        while (pos != null) {//whie the pointer didnt reach null(point to null)
            Queue<Integer> temp = pos.getValue();//assist pointer for the current queue
            int sum = sumQueue(temp);//call the function for summing up the queue nodes value

            if (!temp.isEmpty()) {
                int top = temp.peek();
                if (top % 2 != 0) {//if the first queue node value is odd
                    newey.offer(sum);//add to the new queue the sum of the current queue nodes values
                } else {//else if the first queue node value is even
                    newey.offer(lastValue(temp));//add the last queue node value to the new queue
                }
            }

            pos = pos.getNext();//get to the next node
        }
        return newey;//return new queue
    }

    public static Queue<Integer> avgQ(Queue<Integer> tests, Queue<Integer> marks) {
        //clone both recived queues for not harming them
        Queue<Integer> temp0 = cloneQueue(tests);
        Queue<Integer> temp1 = cloneQueue(marks);
        Queue<Integer> avgQueue = new LinkedList<>();//create the queue we gonna return

        //variabels for calculate average marks per student
        int count = 0;
        int sum = 0;
        int avg = 0;

        while (!temp0.isEmpty()) {//iterate through tests queue

            int x = temp0.poll();//poll current value
            //initialize these variable to zero
            count = 0;
            sum = 0;
            avg = 0;

            while (!temp1.isEmpty() && count < x) {//iterate through marks queue till reach num of tests per one student
                int t = temp1.poll();//poll curent grade
                sum += t;//sum it up
            }

            avg = sum / x;//initialize average variable
            avgQueue.offer(avg);//offer it to the new returned queue

        }
        return avgQueue;
    }

    //תור מושלם-תור מחסניות אשר כל מחסנית ממויינת בסדר עולה,התור ממויין לפי אורכיהן של המחסניות בסדר עולה
    //והאיבר האחרון של כל מחסנית הוא האיבר הראשון של המחסנית הבא
    //פונק עזר 1-בדיקה אם מחסנית ממויינת בסדר עולה
    public static boolean isSort(Stack<Integer> stk) {
        Stack<Integer> copy = cloneStack(stk);//copy the original stack

        int prev = copy.pop();//pop the first value from the stack

        while (!copy.empty()) {//while the stack is not empty
            int curr = copy.pop();//pop the current value

            if (prev > curr) {//if the current value smaller than previous value return false
                return false;
            }

            prev = curr;//initialize prev var to the curr var
        }
        return true;//if the func didnt return false its mean the stack is sorted so we can return true
    }

    //פונק עזר 2-מציאת איבר אחרון במחסנית
    public static int getLastInStack(Stack<Integer> stk) {
        Stack<Integer> copy = cloneStack(stk);//copy original stack
        Stack<Integer> temp = new Stack<>();//create new assist stack

        while (!copy.empty()) {//while stack is not empty
            temp.push(copy.pop());//insert all its values to the assist stack
        }

        int last = temp.peek();//save the first value of the assist stack cause its the last value of the copy-original stack

        return last;//return last value
    }

    //פונק עזר 3-אורך מחסנית
    public static int lengthStk(Stack<Integer> stk) {
        Stack<Integer> copy = cloneStack(stk);//copy original stack
        int count = 0;//counter for counting poped nodes from stack

        while (!copy.empty()) {//iterate through clone stack
            count++;//count nodes
            copy.pop();//pop current node
        }
        return count;//return stack length
    }

    //פונק עזר 4-בדיקה אם תור מחסניות ממויין בסדר עולה כלומר לפי אורכיהן של המחסניות
    public static boolean lengthSortedStackQueue(Queue<Stack<Integer>> q) {
        Queue<Stack<Integer>> temp = new LinkedList<>();//assist queue for restoring the original one

        Stack<Integer> pre = q.poll();//poll current stack from the queue
        int prev = lengthStk(pre);//save stack size in prev var
        temp.offer(pre);//for restoration

        boolean flag = false;//for knowing if queue is sorted

        while (!q.isEmpty()) {//while the q is not empty

            Stack<Integer> cur = q.poll();//poll current stack from the queue
            int curr = lengthStk(cur);//save stack size in prev var
            temp.offer(cur);//for restoration

            if (prev > curr) {//if prev stk length higher than curr stk length its mean that the queue doesnt sorted
                flag = true;
            }

            prev = curr;//initialize the prev length var to curr stk length
        }
        //restore original queue
        while (!temp.isEmpty()) {
            q.offer(temp.poll());
        }

        if (flag) {//if flag is true its mean queue not sorted so we return false
            return false;
        } else {//else if its false its mean the queue is sorted so we return true
            return true;
        }
    }

    //פונק 5-פונק עיקרית לבדיקה אם תור הוא מושלם
    public static boolean isPerfectQueue(Queue<Stack<Integer>> q) {

        if (q.isEmpty()) {
            return false;
        }

        Queue<Stack<Integer>> temp = new LinkedList<>();//for restoration

        if (lengthSortedStackQueue(q)) {//if the queue is sorted

            Stack<Integer> prev = q.poll();
            temp.offer(prev);//for restoration

            if (!isSort(prev)) {//if the current stack isnt sorted return false
                return false;
            }
            int last = getLastInStack(prev);//save last value in stack

            while (!q.isEmpty()) {

                Stack<Integer> curr = q.poll();
                temp.offer(curr);//for restoration

                if (!isSort(curr)) {//if the current stack isnt sorted return false
                    return false;
                }
                if (curr.peek() != last) {//if the head of the current stack diffrent from previous stack last value return false
                    return false;
                }
                last = getLastInStack(curr);//initialize last to current stk last value
            }

            //restore original queue
            while (!temp.isEmpty()) {
                q.offer(temp.poll());
            }
            return true;//the queue is for sure sorted return true
        }
        return false;//if the queue didnt pass the first condition
    }

    public static boolean equalSums(Stack<Integer> stk) {
        Stack<Integer> copy = cloneStack(stk);//copy stack
        int length = stk.size();//size of stk for building array
        int[] arr = new int[length];
        int i = 0;

        while (!copy.empty()) {
            int x = copy.pop();
            arr[i] = x;
            i++;
        }

        int midele = arr[arr.length / 2];

        for (int j = 0; j < arr.length / 2; j++) {
            if ((arr[j] + arr[arr.length - j - 1]) != midele) {
                return false;
            }
        }
        return true;
    }

    public static int numNodesFollowing(Node<Integer> after) {
        int count = 0;
        Node<Integer> pos = after;

        pos = pos.getNext();

        while (pos != null) {
            count++;
            pos = pos.getNext();
        }

        return count;
    }

    public static boolean isSection(Node<Integer> chain) {
        Node<Integer> pos = chain;

        while (pos != null) {
            int x = pos.getValue();
            int count_after = numNodesFollowing(pos);

            if (x > count_after) {
                return false;
            }

            pos = pos.getNext();
        }
        return true;
    }

    public static boolean blueWhiteTree(BinNode<Character> root) {

        Queue<BinNode<Character>> q = new LinkedList<>();//תור עזר לסריקה רוחבית
        q.offer(root);//הכנסת  השורש

        while (!q.isEmpty()) {//כל עוד התור לא ריק

            BinNode<Character> current = q.poll();//שליפת החולייה הדו כיוונית מהתור וחיבור פוינטר אלייה
            //אם ערך החולייה שונה משני התווים הנל החזק שקר
            if (current.getValue() != 'w' && current.getValue() != 'b') {
                return false;
            }
            // אם ערך החוליה שווה לתו בי כלומר כחול נבדוק ששני בנייה הם לבנים
            if (current.getValue() == 'b') {

                char color1 = 'd';
                char color2 = 'd';
                //אם יש לו בן שמאלי נשמור את הצבע של הבן השמאלי
                if (current.hasLeft()) {
                    color1 = current.getLeft().getValue();

                }
                //אם יש לו בן ימני נשמור את הצבע של הבן הימני
                if (current.hasRight()) {
                    color2 = current.getRight().getValue();
                }
                //אם שניהם אינם לסנים נחזיר שקר
                if (color1 != 'w' && color2 != 'w') {
                    return false;
                }
            }

            //הכנסת הבנים
            if (current.hasLeft()) {
                q.offer(current.getLeft());
            } else {//משמע הוא עלה ונבדוק אם ערכו שווה לבי
                if (current.getValue() != 'b') {
                    return false;// אם לא, נחזיר שקר
                }
            }

            if (current.hasRight()) {
                q.offer(current.getRight());
            } else {//משמע הוא עלה ונבדוק אם ערכו שווה לבי
                if (current.getValue() != 'b') {
                    return false;//אם הוא אינו בצבע כחול והוא עלה נחזיר שקר
                }
            }
        }
        return true;//נחזיר אמת כלומר העץ הוא עץ "כחול לבן"א לפי התנאים!
    }

    public static Node<Double> arrangeList(Node<Integer> chain) {
        Node<Double> newey = null;
        Node<Double> last = null;
        Node<Integer> pos = chain;

        int sum = 0;
        int count = 0;

        while (pos != null) {
            int x = pos.getValue();

            //מעבר על שרשרת הציונים פר החלק של תלמיד ספציפי וכל עוד לא נתקלנו בציון שערכו מינוס 1
            while (pos != null && x != -1) {
                sum += x;//צבור ציון
                count++;//קידום מונה כמות ציונים לצורך חישוב ממוצע
                pos = pos.getNext();//התקדם לציון הבא
                x = pos.getValue();  //שמור את הציון
            }

            if (count > 0) {
                // כדי שלא נכניס בטעות ערך 0 לשרשרת הממוצעים אם לא נכנסנו ללולאת הציונים הקודמת במידה והיא הייתה מתחילה במינוס 1
                double avg = (double) sum / count;//חישוב הציון הממוצע
                Node<Double> newNode = new Node<>(avg);//יצירת חוליה המכילה את הממוצע
                //הוספה לשרשרת הממוצעים
                if (newey == null) {//אם השרשרת ריקה הוסף חוליה חדשה וקשר את פויינטר הזנב אלייה
                    newey = newNode;
                    last = newey;
                } else {//אם השרשרת לא ריקה הוסף בעזרת פויינטר הזנב חוליית ממוצע חדשה וקדם את פויינטר הזנב שיצביע עלייה
                    last.setNext(newNode);
                    last = last.getNext();
                }
            }

            pos = pos.getNext(); // קפיצה לתלמיד- הבא
            // אתחול מחדש לסיבוב הבא
            sum = 0;
            count = 0;
        }

        return newey;
    }

    public static boolean wa_1(Node<Integer> chain) {
        //variables for calculating average
        int sum = 0;
        int avg = 0;
        int count = 0;
        //flag for checking if all values on list are even
        boolean flag = false;

        Node<Integer> pos = chain;//pointer for the linked list

        while (pos != null) {//iterate through the list
            int x = pos.getValue();//get and save value of the current node
            sum += x;//sum it up
            count++;//count node
            if (x % 2 != 0) {//check if the value isodd
                flag = true;
            }
            pos = pos.getNext();// get to the next node
        }

        avg = sum / count;//calculate avg

        return ((avg % 2 == 0) && (!flag));// if avg is even and all values of the list are even return true
    }

    //תרגיל סיכום של מבנה נתונים:מערך, מטריצה, שרשרת חוליות חד כיוונית, דו כיוונית, תור, מחסנית, עץ בינארי, עץ רגיל
    //פונק עזר 1-העתק מחסנית
    public static Stack<Integer> cloneStack(Stack<Integer> stk) {
        Stack<Integer> temp0 = new Stack<>();
        Stack<Integer> temp1 = new Stack<>();
        Stack<Integer> copy = new Stack<>();

        while (!stk.empty()) {
            int x = stk.pop();
            temp0.push(x);
            temp1.push(x);
        }

        //restore original stack
        while (!temp0.empty()) {
            stk.push(temp0.pop());
        }

        //build the copy stack
        while (!temp1.empty()) {
            copy.push(temp1.pop());
        }
        return copy;
    }

    //פונקציית עזר 2- איבר אחרון במחסנית
    public static int endy(Stack<Integer> stk) {
        Stack<Integer> copy = cloneStack(stk);//שימוש במחסנית העתק
        Stack<Integer> temp = new Stack<>();//מחסנית עזר

        while (!copy.empty()) {//כל עוד מחסנית ההעתק לא ריקה
            temp.push(copy.pop());//דחוף למחסנית העזר את כל החוליות הנשלפות
        }
        int last = temp.peek();//שמירת ערך חוליה עליונה במחסנית עזר מכיוון וחוליה עליונה במחסנית עזר היא החולייה התחתונה במחסנית המקור
        return last;//הדפסת ערכה
    }

    //פונקציית עזר 3-אורך מחסנית
    public static int length(Stack<Integer> stk) {
        Stack<Integer> copy = cloneStack(stk);//הפניה למחסנית העתק
        int count = 0;//מונה חוליות-כמות חוליות שווה לארוך מחסנית

        while (!copy.empty()) {//כל עוד המחסנית העתק לא ריקה
            count++;//מנה
            copy.pop();//שליפת חולייה הבאה ממחסנית
        }
        return count;//החזר מונה
    }

    //פונקציית עזר 4- אם מחסנית ממויינת בסדר עולה
    public static boolean isSorted(Stack<Integer> stk) {
        Stack<Integer> copy = cloneStack(stk);//העתק מחסנית עזר
        int prev = copy.pop();//שמירת ערך ראשון
        while (!copy.empty()) {//כל עוד המחסנית לא ריקה
            int curr = copy.pop();//שמור ערך חולייה 2

            if (prev > curr) {//אם חולייה קודמת ערכה גדול יותר מערך חוליה הבאה החזר שקר מכיוון וזה מעיד על מחסנית שלא ממוינת בסדר עולה
                return false;
            }
            prev = curr;//אתחל משתנה "קודם" בערכו של משתנה נוכחי לשם המשך השוואה מול ערך החוליה הבאה
        }
        return true;//אם לא חזר שקר כלל משמע שהמחסנית ממוינת בסדר עולה לכן נחזיר אמת
    }

    //פונקציית עזר 5- אם תור ממויין לפי אורך מחסניות(בסדר עולה )
    public static boolean KLSR(Queue<Stack<Integer>> q) {
        Queue<Stack<Integer>> temp = new LinkedList<>();//תור מחסניות עזר
        boolean flag = false;//אתחול משתנה בוליאני בשקר משתנה זה מעיד אם תור ממויין לפי אורכיהן של המחסניות בסדר עולה

        Stack<Integer> tempi = q.poll();//הפנייה למחסנית הראשונה בתור
        int prev = length(tempi);//שמירת אורכה של מחסנית נוכחית זו
        temp.offer(tempi);//הכנסת הפנייה של מחסנית זו לתור עזר רק לשם שיחזור ושמירה על התור המקורי

        while (!q.isEmpty()) {//כל עוד לא ריק

            tempi = q.remove();//הפניה לחוליה הוכחית בתור המכילה הפניה למחסנית
            int curr = length(tempi);//שמירת אורכה של מחסנית נוכחית זו
            temp.offer(tempi);//והכנסת הפניה זו לתור עזר רק לשם שיחזור התור מקור

            if (prev > curr) {//אם אורכה של מחסנית קודמת גדול יותר ממחסנית נוכחית משמע שתור לא ממויין לפי אורכיהן של מחסניותיו בסדר עולה
                flag = true;//עדכן משתנה בולליאני זה
            }
            prev = curr;//עדכן משתנה "קודם" בערך הנוכחי(אורך) לשם המשך השוואה עם המחסנית הבאה
        }

        //restoration
        while (!temp.isEmpty()) {
            q.offer(temp.remove());
        }

        if (flag) {//אם משתנה זה ערכו אמת משמע שבתור אין שמירה על מחסניות לפי אורכן בסדר עולה ונחזיר שקר
            return false;
        } else {//אחרת נחזיר אמת
            return true;
        }
    }

    //פונקצייה עזר 6 לבדיקה אם תור מושלם
    public static boolean isPerfect(Queue<Stack<Integer>> q) {
        Queue<Stack<Integer>> restore = new LinkedList<>();

        if (KLSR(q)) {//אם תור ממויין לפי אורכי מחסניות בסדר עולה
            Stack<Integer> temp = q.poll();//הפנייה למחסנית נוכחית

            if (!isSorted(temp)) {//אם מחסנית לא ממוינת בסדר עולה החזר שקר
                return false;
            }
            int prev = endy(temp);//שמירת ערך חוליה אחרונה במחסנית

            while (!q.isEmpty()) {//מעבר על התור כל עוד הוא לא ריק

                temp = q.poll();//הפנייה למחסנית נוכחית
                restore.offer(temp);//הזנה לתור עזר רק לשם שיחזור

                if (!isSorted(temp)) {//אם מחסנית לא ממוינת בסדר עולה החזר שקר
                    return false;
                }
                if (temp.peek() != prev) {//אם ערך החוליה האחרונה במחסנית קודמת לא שווה לערך חולייה ראשונה במחסנית נוכחית זו החזר שקר
                    return false;
                }
                prev = endy(temp);//שמירת ערך חוליה אחרונה
            }
            //שיזחור תור מקור
            while (!restore.isEmpty()) {
                q.offer(restore.poll());
            }
            return true;//עברנו את כל הבדיקות בשלום לכן נחזיר אמת
        } else {//אם לא נכנסנו לתנאי בכלל משמע שאחד התנאים לתור "מושלם" הופר(במיקרה זה מדובר בתנאי מיון מחסניות לפי אורכיהן בסדר עולה) ונחזיר שקר
            return false;
        }
    }

    //פונק עזר 7 לבדיקה האם שרשרת היא שרשרת תורים מושלמים
    public static boolean howmen(LinkedList<Queue<Stack<Integer>>> chain) {
        for (int i = 0; i < chain.size(); i++) {//מעבר על שרשרת החוליות של התורים המושלמים
            if (!isPerfect(chain.get(i))) {//אם התור הנוכחי אינו תור מחסניות מושלם החזר שקר מכיוון והמטרה שכל חוליות השרשרת יפנו לתור מחסניות מושלמים
                return false;
            }
        }
        return true;//אם לא חזר שקר משמע שהחוליות מפנות לתורים מושלמים(כולן) ולכן נחזיר אמת
    }

    //פונק עזר 8 לבדיקה אם עץ הוא עץ שרשראות של תורים של מחסניות מושלם
    public static boolean perfectTree(BinNode<LinkedList<Queue<Stack<Integer>>>> root) {
        Queue<BinNode<LinkedList<Queue<Stack<Integer>>>>> q = new LinkedList<>();//תור עזר למעבר בסריקה רוחבית על עץ
        q.offer(root);//נתחיל מלהכניס את שורש העץ

        while (!q.isEmpty()) {//כל עוד תור העזר אינו ריק
            BinNode<LinkedList<Queue<Stack<Integer>>>> current = q.poll();//פויינטר המצביע על החוליה הדוכיוונית הנשלפה מהתור
            //אם חוליית העץ הנוכחית לא מפנה לשרשרת של תורים של מחסניות מושלמים, נחזיר שקר
            if (!howmen(current.getValue())) {
                return false;
            }
            //הכנסת הבנים לתור עזר
            //אם חולייה נוכחית לא מפנה לריק בצד השמאלי שלה, משמע שיש בן שמאלי ולכן נכניס אותו לתור
            if (current.hasLeft()) {
                q.offer(current.getLeft());
            }
            //כנל לגבי הבן הימני
            if (current.hasRight()) {
                q.offer(current.getRight());
            }
        }
        return true;//החזר אמת משמע: עץ זה הוא עץ של שרשראות של תורים של מחסניות מושלם!
    }

    //פונק עזר 9 לבדיקת מטריצת עצים של שרשראות של תורים של מחסניות מושלם
    public static boolean matrix_2D_perfect_Trees(BinNode<LinkedList<Queue<Stack<Integer>>>> mat[][]) {
        //מעבר על מערך דו מימדי ובדיקה שכל התאים מפנים לעצים מושלמים
        for (int i = 0; i < mat.length; i++) {//מעבר על השורות
            for (int j = 0; j < mat[j].length; j++) {//מעבר על העמודות
                //פוינטר לעץ בתא הנוכחי במטריצה
                BinNode<LinkedList<Queue<Stack<Integer>>>> current = mat[i][j];
                //אם העץ אינו עץ של שרשראות של תורים של מחסניות מושלם, נחזיר שקר
                if (!perfectTree(current)) {
                    return false;
                }
            }
        }
        return true;//כלומר מטריצה זו היא מושלמת
    }

    //פונק עיקרית 10 לבדיקה האם שרשרת דו כיוונית של מערך דו מימדי של עצים של שרשראות חד כיוונית של תורים של מחסניות מושלמת
    public static boolean Perfect_doubly_Linky(BinNode<BinNode<LinkedList<Queue<Stack<Integer>>>>[][]> double_chain) {
        BinNode<BinNode<LinkedList<Queue<Stack<Integer>>>>[][]> pos = double_chain;

        while (pos.hasLeft()) {
            pos = pos.getLeft();
        }

        while (pos != null) {
            BinNode<LinkedList<Queue<Stack<Integer>>>> mat[][] = pos.getValue();
            if (!matrix_2D_perfect_Trees(mat)) {
                return false;
            }
            pos = pos.getRight();
        }
        return true;
    }

    public static Queue<Integer> tillN(int num) {
        Queue<Integer> tiN = new LinkedList<>();

        for (int i = 1; i <= num; i++) {
            int currNum = i;
            int count = 0;

            while (count < currNum) {
                tiN.offer(currNum);
                count++;
            }
        }
        return tiN;
    }

    public static boolean DargaNqueue(Queue<Integer> q, int num) {
        Queue<Integer> temp = tillN(num);
        if (q.size() != temp.size()) {
            return false;
        }

        while (!q.isEmpty() && !temp.isEmpty()) {
            if (q.poll() != temp.poll()) {
                return false;
            }
        }
        return true;
    }

    //שרשרת סכומים
    public static boolean chainSumy(Node<Integer> chain) {
        Node<Integer> pos = chain;
        int sum = 0;

        while (pos.getNext() != null) {
            int curr = pos.getValue();
            sum += curr;
            int after = pos.getNext().getValue();

            if (after != sum) {
                return false;
            }

            pos = pos.getNext();
        }
        return true;
    }

    //שרשרת סכומים הפוכה
    public static boolean reverseChainSumy(Node<Integer> chain) {
        Node<Integer> pos = chain;
        int sum = 0;

        while (pos != null) {
            int curr = pos.getValue();

            Node<Integer> miniPos = pos.getNext();
            sum = 0;

            while (miniPos != null) {
                int t = miniPos.getValue();
                sum += t;
                miniPos = miniPos.getNext();
            }

            if (curr != sum) {
                return false;
            }

            pos = pos.getNext();
        }
        return true;
    }

    //function that order even list at the farest left side of doubly linkedlist, and odd numbers at the farest right side
    public static void LINKY(BinNode<Integer> chain) {

        //pointer for even Doubly LinkedList
        BinNode<Integer> newey = null;
        BinNode<Integer> last = null;

        //pointer for odd Doubly LinkedList
        BinNode<Integer> secnewey = null;
        BinNode<Integer> lsec = null;

        //pointer for original Doubly LinkedList
        BinNode<Integer> pos = chain;

        //reach farest left side of the list
        while (pos.hasLeft()) {
            pos = pos.getLeft();
        }

        //iterate through original list to create two new Doubly linkedlists
        while (pos != null) {
            //save val in var
            int x = pos.getValue();
            //if the current node value is even create new even list add new binnode to it
            if (x % 2 == 0) {
                BinNode<Integer> toaddz = new BinNode<>(x);//create new binnode
                //if the new even list is null
                if (newey == null) {
                    newey = toaddz;//attach the pointer to the new binnode
                    last = newey;//attach the tail pointer to the new binnode
                } else {//if its not null
                    last.setRight(toaddz);//attach the tail pointer to the new binnode
                    last.getRight().setLeft(last);//attach the new binnode to the tail
                    last = last.getRight();//move the tail pointer to the last binnode created
                }
            } else {//if the current node value is odd create new odd list add new binnode to it
                BinNode<Integer> toaddz = new BinNode<>(x);//create new binnode
                //if the new odd list is null
                if (secnewey == null) {
                    secnewey = toaddz;
                    lsec = secnewey;
                } else {//if its not null
                    lsec.setRight(toaddz);//attach the tail pointer to the new binnode
                    lsec.getRight().setLeft(lsec);//attach the new binnode to the tail
                    lsec = lsec.getRight();//move the tail pointer to the last binnode created
                }
            }
            pos = pos.getRight();//get to the next(right->) binnode
        }

        //connect the two new seperated lists
        last.setRight(secnewey);//connect the even list tail pointer to the odd list head pointer
        last.getRight().setLeft(last);//crate doubly connection

        pos = chain;//move the iteration binnode of the original list to the start(leftest side)
        BinNode<Integer> from_start = newey;//create new pointer for the new list created
        BinNode<Integer> prev = pos; // Track the previous node

        //iterate through both lists and update the original one
        while (pos != null && from_start != null) {
            pos.setValue(from_start.getValue());//update to the new values
            prev = pos; // Keep track of the current node before moving on
            pos = pos.getRight();//get to the next binnode
            from_start = from_start.getRight();//get to the next binnode
        }

        //if the new list is longer than the original list
        if (from_start != null) {
            while (from_start != null) {// iterate through the new longer list
                pos.setRight(new BinNode<>(from_start.getValue()));//create new nodes and add them to the original list
                pos.getRight().setLeft(pos);//create doubly connection
                pos = pos.getRight();//get to the next binnode
                from_start = from_start.getRight();//get to the next binnode
            }
        }

        //if the new list is shorter than the original
        if (pos != null) {
            prev.setRight(null);
        }

    }

    //same as the func before but using build-in library
    public static void LINKY_LIST(LinkedList<Integer> chain) {
        LinkedList<Integer> newey = new LinkedList<>();

        for (int i = 0; i < chain.size(); i++) {
            int curr_num = chain.get(i);
            if (curr_num % 2 == 0) {
                newey.add(curr_num);
            }
        }

        for (int i = 0; i < chain.size(); i++) {
            int curr_num = chain.get(i);
            if (curr_num % 2 != 0) {
                newey.add(curr_num);
            }
        }

        for (int i = 0; i < newey.size(); i++) {
            chain.set(i, newey.get(i));
        }
    }

    public static Queue<Integer> NEW_QUEUE(Queue<Integer> q) {
        Queue<Integer> temp = new LinkedList<>();
        Queue<Integer> returned_one = new LinkedList<>();
        int count = 0;
        while (!q.isEmpty()) {

            int num = q.poll();
            count++;

            while (!q.isEmpty()) {
                int x = q.poll();
                if (x == num) {
                    count++;
                } else {
                    temp.offer(x);
                }
            }

            if (count >= 2) {
                returned_one.offer(num);
            }

            while (!temp.isEmpty()) {
                q.offer(temp.poll());
            }
            count = 0;
        }
        return returned_one;
    }

    //פונק הממיינת שרשרת חוליות דו כיוונית כך שהחוליות המחזיקות ערך זוגי יהיו בהתחלה והחוליות המחזיקות ערך אי זוגי יהיו לאחריהן
    public static void change_to_evenOdd_Dlist(BinNode<Integer> chain) {

        //if original chain is null return (dont do anything)
        if (chain == null) {
            return;
        }

        //pointers for new even Doubly LinkedList
        BinNode<Integer> even_chain = null;//pointer for the head of a list
        BinNode<Integer> last1 = null;//pointer for the tail of the list

        //pointers for new odd Doubly LinkedList
        BinNode<Integer> odd_chain = null;//pointer for the head of a list
        BinNode<Integer> last2 = null;//pointer for the tail of the list

        BinNode<Integer> pos = chain;//pointer for the original Doubly LinkedList

        //reach the far left side
        while (pos.getLeft() != null) {
            pos = pos.getLeft();//get to the left binnode
        }

        while (pos != null) {//while the pointer of the original doubly linkedlist doesn't point to null

            int num = pos.getValue();//get the value of the current binnode
            BinNode<Integer> newey = new BinNode<>(num);//create new binnode that contain num

            if (num % 2 == 0) {//if the value is even add it to even chain
                if (even_chain == null) {//if even chain is null
                    even_chain = newey;//connect the first binnode with the current value, so even_chain point to this first binnode
                    last1 = even_chain;//attach the tail pointer to the new binnode
                } else {//if even chain not null
                    last1.setRight(newey);//attach the tail pointer to the new binnode
                    last1.getRight().setLeft(last1);//create doubly connection
                    last1 = last1.getRight();//tail pointer now pointing to the latest added binnode
                }
            } else {
                if (odd_chain == null) {//if odd chain is null
                    odd_chain = newey;//connect the first binnode with the current value, so even_chain point to this first binnode
                    last2 = odd_chain;//attach the tail pointer to the new binnode
                } else {//if odd chain not null
                    last2.setRight(newey);//attach the tail pointer to the new binnode
                    last2.getRight().setLeft(last2);//create doubly connection
                    last2 = last2.getRight();//tail pointer now pointing to the latest added binnode
                }
            }
        }
        //connect the two doubly linkedlists into one doubly linkedlist
        if (even_chain != null && odd_chain != null) {
            last1.setRight(odd_chain);//connect the tail pointer of the even chain to the first pointer of the odd chain
            last1.getRight().setLeft(last1);
        }

        BinNode<Integer> NEW_LIST = even_chain;//new pointer for the new list that created
        BinNode<Integer> prev = null;//pointer for the last binnode of the original linkedlist
        pos = chain;//reset pos to the start of the original chain

        while (pos != null && NEW_LIST != null) {//iterate through both lists
            pos.setValue(NEW_LIST.getValue());//changing all the valus of the original chain to be exaxctly like the created list
            prev = pos;//point prev pointer to pos
            pos = pos.getRight();//get to the right binnode(ori chain)
            NEW_LIST = NEW_LIST.getRight();//get to the right binnode(ev chain)
        }

        if (pos != null) {//if new list is shorter
            prev.setRight(null);//put the right of the "new tail" pointer to null, disconnect all unwanted binnodes from original chain
        }

        if (pos == null) {//if the new list is longer
            while (NEW_LIST != null) {
                prev.setRight(new BinNode<Integer>(NEW_LIST.getValue()));//create new binnodes and connect them to the original doubly linkedlist
                prev.getRight().setLeft(prev);
                prev = prev.getRight();
            }
        }
    }

    //מחסנית שרשראות דו כיווניות זוגיות
    public static int stk_Bin(Stack<BinNode<Integer>> stk) {
        Stack<BinNode<Integer>> temp = new Stack<>();//assist stk for restoration
        boolean flag = false;//boolean var that tell if there is list that is not even

        while (!stk.empty()) {//iterate through the stack

            BinNode<Integer> curr_chain = stk.pop();//pointer for current list
            temp.push(curr_chain);//push the first stk_node to the assist-stk for restoration

            while (curr_chain != null) {//iterate through the chain
                int x = curr_chain.getValue();//save the current binnode value
                if (x % 2 != 0) {//check if it is odd val
                    flag = true;
                }
                curr_chain = curr_chain.getRight();//get to the next binnode(left->right)
            }
        }

        //resore original stk
        while (!temp.empty()) {
            stk.push(temp.pop());
        }

        if (flag) {//means notall the nodes in the stk points to "even-doubly-linkedlists"
            return 0;
        } else {
            return 1;//means all the nodes in the stk points to "even-doubly-linkedlists"
        }
    }

    //כל הצד השמאלי של העץ הוא עץ מחסניות של שרשראות זוגיות, וכל הצד הימני ההיפך
    public static boolean con(BinNode<Stack<BinNode<Integer>>> root) {

        //if tree empty return false
        if (root == null) {
            return false;
        }

        //create two pointer for the two sides of the tree
        BinNode<Stack<BinNode<Integer>>> lefty_tree = null;
        BinNode<Stack<BinNode<Integer>>> righty_tree = null;

        //connect the pointers to the sides
        if (root.hasLeft()) {
            lefty_tree = root.getLeft();
        }

        if (root.hasRight()) {
            righty_tree = root.getRight();
        }

        //disconect the root of the tree
        root.setLeft(null);
        root.setRight(null);

        //create two assist queues- one for left side and the other for right side
        Queue<BinNode<Stack<BinNode<Integer>>>> leftQueue = new LinkedList<>();//create new q assist for bfs
        leftQueue.offer(lefty_tree);

        Queue<BinNode<Stack<BinNode<Integer>>>> rightQueue = new LinkedList<>();//create new q assist for bfs
        rightQueue.offer(righty_tree);

        while (!leftQueue.isEmpty()) {

            BinNode<Stack<BinNode<Integer>>> current = leftQueue.poll();//pointer for current binnode_tree that polled out of the queue

            if (stk_Bin(current.getValue()) == 0) {//if stk_Bin fuc' return 0 means that the stk-list is not even, so we return false
                //connect the root to its childrens
                root.setLeft(lefty_tree);
                root.setRight(righty_tree);
                return false;
            }

            //offers sons
            //if parent have left son offer it to the queue
            if (current.hasLeft()) {
                leftQueue.offer(current.getLeft());
            }

            //if parent have right son offer it to the queue
            if (current.hasRight()) {
                leftQueue.offer(current.getRight());
            }
        }

        while (!rightQueue.isEmpty()) {

            BinNode<Stack<BinNode<Integer>>> current = rightQueue.poll();//pointer for current binnode_tree that polled out of the queue

            if (stk_Bin(current.getValue()) == 1) {//if stk_Bin fuc' return 0 means that the stk-list is not odd, so we return false
                //connect the root to its childrens
                root.setLeft(lefty_tree);
                root.setRight(righty_tree);
                return false;
            }

            //offers sons
            //if parent have left son offer it to the queue
            if (current.hasLeft()) {
                rightQueue.offer(current.getLeft());
            }

            //if parent have right son offer it to the queue
            if (current.hasRight()) {
                rightQueue.offer(current.getRight());
            }
        }

        //connect the root to its childrens
        root.setLeft(lefty_tree);
        root.setRight(righty_tree);

        return true;
    }

    //פונק הממיינת שרשרת חוליות חד כיוונית כך שהחוליות המחזיקות ערך זוגי יהיו בהתחלה והחוליות המחזיקות ערך אי זוגי יהיו לאחריהן
    public static void orderListy(Node<Integer> chain) {

        Node<Integer> even = null;
        Node<Integer> le = null;

        Node<Integer> odd = null;
        Node<Integer> lo = null;

        Node<Integer> pos = chain;

        while (pos != null) {

            int num = pos.getValue();
            Node<Integer> newey = new Node<>(num);

            if (num % 2 == 0) {
                if (even == null) {
                    even = newey;
                    le = even;
                } else {
                    le.setNext(newey);
                    le = le.getNext();
                }
            } else {
                if (odd == null) {
                    odd = newey;
                    lo = odd;
                } else {
                    lo.setNext(newey);
                    lo = lo.getNext();
                }
            }
            pos = pos.getNext();
        }

        if (even != null && odd != null) {
            le.setNext(odd);
        }

        Node<Integer> start = even;
        Node<Integer> prev = null;
        pos = chain;

        while (pos != null && start != null) {
            pos.setValue(start.getValue());
            prev = pos;
            pos = pos.getNext();
            start = start.getNext();
        }

        if (start == null) {
            prev.setNext(null);
        }

        if (pos == null) {
            while (start != null) {
                pos.setNext(new Node<>(start.getValue()));
                pos = pos.getNext();
                start = start.getNext();
            }
        }
    }

    //func that check if all stacks in queue are perfectly even stacks
    public static boolean queuePer(Queue<Stack<Integer>> q) {

        Queue<Stack<Integer>> temp = new LinkedList<>();//create assist queue for restoration
        boolean flag = false;//boolean var for catch case thet q contain stk that contain odd num

        while (!q.isEmpty()) {//iterate through the queue

            Stack<Integer> curr_stk = q.poll();//poll current stk
            Stack<Integer> temp_stk = new Stack<>();//assist stack for restoration

            while (!curr_stk.empty()) {//iterate through the stack

                int x = curr_stk.pop();//pop current val

                if (x % 2 != 0) {//if curr val is odd
                    flag = true;//change bool var value -> true
                }

                temp_stk.push(x);//push cuur integer val to assist stack
            }

            //restore original_curr stk
            while (!temp_stk.empty()) {
                curr_stk.push(temp_stk.pop());
            }

            temp.offer(curr_stk);//push current stk to the assist queue for restoration

        }

        //restore original queue
        while (!temp.isEmpty()) {
            q.offer(temp.poll());
        }

        //check the val of the bool var
        if (flag) {//if -> true, odd num founded
            return false;
        } else {//else if -> false, no odd num founded, return true
            return true;
        }
    }

    //מערכת בקרה- אם מס קיים או לא
    //רשימת בקרה
    public static Node<Integer> listyy = null;
    public static Node<Integer> las = null;

    //אם מס קיים ברשימה?
    public static boolean find_in_list(Node<Integer> chain, int num) {
        Node<Integer> pos = chain;

        while (pos != null) {
            int x = pos.getValue();

            if (x == num) {
                return true;
            }

            pos = pos.getNext();
        }
        return false;
    }

    //אם מס הוסף לרשימה סימן שהוא לא קיים-משמע מס יחודי-> משמע רשימה יחודית
    public boolean added_to_list(Node<Integer> chain, int num) {
        Node<Integer> pos = listyy;

        if (listyy == null) {
            listyy = new Node<>(num);
            las = listyy;
            return true;
        } else if (!find_in_list(listyy, num)) {
            las.setNext(new Node<>(num));
            las = las.getNext();
            return true;
        }
        return false;
    }

    //מוצאת את המינימום בתור
    public static int findMinQ(Queue<Integer> q) {
        Queue<Integer> temp = new LinkedList<>();
        int min = Integer.MAX_VALUE;

        while (!q.isEmpty()) {
            int x = q.poll();

            if (x < min) {
                min = x;
            }
            temp.offer(x);
        }
        while (!temp.isEmpty()) {
            q.offer(temp.poll());
        }

        return min;
    }

    //מסירה כל מופעים של מס מתור
    public static void removeFromQueue(Queue<Integer> q, int num) {
        Queue<Integer> temp = new LinkedList<>();

        while (!q.isEmpty()) {
            int x = q.poll();

            if (x != num) {
                temp.offer(x);
            }
        }
        while (!temp.isEmpty()) {
            q.offer(temp.poll());
        }
    }

    //מסירה מופע ראשון של מס מתור
    public static void refirst(Queue<Integer> q, int num) {
        Queue<Integer> temp = new LinkedList<>();

        while (!q.isEmpty()) {
            int x = q.poll();

            if (x == num) {//אם המס נמצא
                while (!q.isEmpty()) {//הוסף בלי הבחנה את כל המס הבאים אחריו בתור (גם אם הם שווים לו)
                    temp.offer(q.poll());
                }
            } else {//הוסף את כל המס עד שמגיעים למופע הראשון של המס המבוקש
                temp.offer(x);
            }

        }

        while (!temp.isEmpty()) {
            q.offer(temp.poll());
        }
    }

    public static void soQ(Queue<Integer> q) {
        Queue<Integer> returned = new LinkedList<>();//תור מסודר בסדר עולה
        Queue<Integer> temp = new LinkedList<>();//תור עזר לשיחזורים

        while (!q.isEmpty()) {
            int min = findMinQ(q);
            refirst(q, min);
            returned.offer(min);
        }

        //שפיכת תוכן התור "המסודר" לתור מקור
        while (!returned.isEmpty()) {
            q.offer(returned.poll());
        }
    }

    //ממיינת תור בסדר עולה ללא כפילויות
    public static void order_queue(Queue<Integer> q) {
        Queue<Integer> temp = new LinkedList<>();

        while (!q.isEmpty()) {
            int min = findMinQ(q);//find min
            temp.offer(min);
            removeFromQueue(q, min);//remove all ocuurance of this min num
        }
        while (!temp.isEmpty()) {
            q.offer(temp.poll());
        }
    }

    //מיון שרשרת בסדר עולה ללא הסרת כפילויות
    //בדיקה אם שרשרת ממויינת בסדר עולה
    public static boolean sorted(Node<Integer> chain) {
        Node<Integer> pos = chain;

        while (pos.getNext() != null) {
            if (pos.getValue() > pos.getNext().getValue()) {
                return false;
            }
            pos = pos.getNext();
        }
        return true;
    }

    //מיון שרשרת בפועל(מיון בועות-החלפה בין תאים עוקבים לא עולים נונ סטופ-עד השגת מיון מלא)
    public static void sort(Node<Integer> chain) {

        while (!sorted(chain)) {//כל עוד השרשרת לא ממויינת מיין אותה
            Node<Integer> pos = chain;//הפנייה מחדש לשרשרת המקורית

            //מיון-החלפה בין שני איברים עוקבים
            while (pos.getNext() != null) {
                int curr = pos.getValue();//ערך נוכחי
                int nex = pos.getNext().getValue();//ערך הבא

                if (curr > nex) {//אם ערך החולייה הנוכחית גדול מערך החולייה הבאה נחליף ביניהם את ערכיהם
                    pos.setValue(nex);
                    pos.getNext().setValue(curr);
                }

                pos = pos.getNext();//התקדמות לחולייה הבאה
            }
        }
    }

    //הסרת כפילויות בשרשרת שמויינה בסדר עולה(העברת ערך תא השונה מערך התא הבא אחריו)
    public static void remove_doublys(Node<Integer> chain) {
        Node<Integer> pos = chain;//פויינטר לשרשרת מקור
        //פויינטרים לשרשרת חדשה
        Node<Integer> newey = null;//פוינטר לראש השרשרת החדשה
        Node<Integer> last = null;//פויינטרים לזנבה

        while (pos.getNext() != null) {//איטרצית מעבר על השרשרת מקור

            //אם הער הנוכחי שונה מהערך של החולייה העוקבת
            if (pos.getValue() != pos.getNext().getValue()) {

                //הוסף אותו לשרשרת החדשה
                //אם השרשרת החדשה ריקה..
                if (newey == null) {
                    newey = new Node<>(pos.getValue());
                    last = newey;
                } else {//אם לא..
                    last.setNext(new Node<>(pos.getValue()));
                    last = last.getNext();
                }
            }
            pos = pos.getNext();//כל עוד הם שווים התקדם לחוליה הבאה
        }

        //טיפול באיבר אחרון
        last.setNext(new Node<>(pos.getValue()));
        last = last.getNext();

        //עדכון שרשרת מקור
        Node<Integer> start = newey;//פויינטר חדש לראש השרשרת החדשה
        Node<Integer> prev = null;//פויינטר לחוליה אחרונה בשרשרת מקור
        pos = chain;//אתחול פויינטר שרשרת מקור לתחילתה של שרשרת המקור כמובן

        while (pos != null && start != null) {
            pos.setValue(start.getValue());
            prev = pos;
            pos = pos.getNext();
            start = start.getNext();
        }

        //במידה והשרשרת מקור קצרה יותר ניתוק חוליות מיותרות
        if (start == null) {
            prev.setNext(null);
        }
    }

    public static boolean sortedlinky(LinkedList<Integer> chain) {
        for (int i = 0; i < chain.size() - 1; i++) {
            if (chain.get(i) > chain.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    public static void sortlinky(LinkedList<Integer> chain) {
        //Collections.sort(chain);

        while (!sortedlinky(chain)) {

            for (int i = 0; i < chain.size() - 1; i++) {
                int current = chain.get(i);
                int next = chain.get(i + 1);

                if (current > next) {
                    chain.set(i, next);
                    chain.set(i + 1, current);
                }
            }
        }
    }

    public static void sort_d_link(BinNode<Integer> chain) {
        //שרשרת זוגיים
        BinNode<Integer> even = null;//פויינטר לראש
        BinNode<Integer> le = null;//פויינטר לזנב

        //שרשרת אי זוגיים
        BinNode<Integer> odd = null;//פויינטר לראש
        BinNode<Integer> lo = null;//פויינטר לזנב

        //פויינטר לשרשרת מקור
        BinNode<Integer> pos = chain;

        //נעבור על שרשרת המקור
        while (pos != null) {
            int x = pos.getValue();//נשלוף ערך
            BinNode<Integer> newey = new BinNode<>(x);//ניצור לו חוליה משלו

            if (x % 2 == 0) {//אם הערך הוא זוגי,נוסיף אותו לשרשרת הזוגיים
                if (even == null) {//אם השרשרת ריקה נוסיף חוליה חדשה ונקשר את הפויינטרים ראש וזנב בהתאם
                    even = newey;
                    le = even;
                } else {//אם השרשרת לא ריקה, נחבר חוליה חדשה לפויינטר זנב השרשרת,ונקדם את פויינטר הזנב
                    le.setRight(newey);
                    le.getRight().setLeft(le);
                    le = le.getRight();
                }
            } else {//אם הערך הוא אי זוגי,נוסיף אותו לשרשרת האי זוגיים
                if (odd == null) {//אם השרשרת ריקה נוסיף חוליה חדשה ונקשר את הפויינטרים ראש וזנב בהתאם
                    odd = newey;
                    lo = odd;
                } else {//אם השרשרת לא ריקה, נחבר חוליה חדשה לפויינטר זנב השרשרת,ונקדם את פויינטר הזנב
                    lo.setRight(newey);
                    lo.getRight().setLeft(lo);
                    lo = lo.getRight();
                }
            }
        }

        //אם שני השרשראות לא ריקות נחבר ביניהם(יש לקחת בחשבון עוד שני מקרים)
        if (even != null && odd != null) {
            le.setRight(odd);//נחבר את זנב שרשרת הזוגיים לראש שרשרת הזוגיים
            le.getRight().setLeft(le);//נשמור על קשר דו כיווני
        }

        BinNode<Integer> start = even;//פויינטר חדש לשרשרת המאוחדת
        pos = chain;//נאפס את פויינטר שרשרת המקור לתחילתה
        BinNode<Integer> prev = null;//פויינטר לחוליה אחרונה בשרשרת המקור למקרים ובהם היא יותר קצרה או ארוכה

        while (pos != null && start != null) {//כל עוד הפויינטרים של שני השרשראות לא מצביעות על ריק
            pos.setValue(start.getValue());//נעתיק את תוכנה של השרשרת המאוחדת לתוך השרשרת מקור
            prev = pos;//נקדם את פויינטר הזנב
            pos = pos.getRight();//נקדם את פויינטר שרשרת המקור
            start = start.getRight();//נקדם את פויינטר השרשרת המאוחדת
        }

        if (pos == null) {//במידה והשרשרת המקורית קצרה יותר נוסיף חוליות בהתאם לשרשרת מקור וכמובן המשך העתקת התוכן מהשרשרת המאוחדת
            while (start != null) {//כל עוד השרשרת המאוחדת לא ריקה
                prev.setRight(new BinNode<>(start.getValue()));//נוסיף לשרשרת המקור חוליה חדשה עם הערך הנוכחי עליו מצביע פויינטר השרשרת איחוד
                prev.getRight().setLeft(prev);//שמירת קשר דו כיווני
                prev = prev.getRight();//קידום פויינטר הזנב של שרשרת המקור לחוליה האחרונה שנוספה
            }
        }

        if (start == null) {//אם שרשרת האיחוד קצרה יותר
            prev.setRight(null);//ננתק משרשרת המקור את החוליות העודפות
        }

    }

    //a
    public static boolean perfecto(BinNode<Queue<Stack<Integer>>> chain) {
        BinNode<Queue<Stack<Integer>>> pos = chain;//pointer for the list

        while (pos.hasLeft()) {//take the pointer to the left far side
            pos = pos.getLeft();
        }

        boolean oddFlag = true;//flag that show if odd number was founded

        while (pos != null) {//iterate through the list

            Queue<Stack<Integer>> currQ = pos.getValue();//get the queue of the current binnode
            Queue<Stack<Integer>> temp = new LinkedList<>();//assist queue for restoration

            while (!currQ.isEmpty()) {

                Stack<Integer> currentStack = currQ.poll();//get the stack of the current node from the current queue
                Stack<Integer> te = new Stack<>();//assist stack for restoration

                while (!currentStack.empty()) {//iterate through the stack

                    int x = currentStack.pop();//pop the first value from the stack
                    if (x % 2 != 0) {//if the value is odd num change the odd-flag to true,"odd num founded"
                        oddFlag = true;
                    }
                    te.push(x);//push current value to the assist stack
                }

                //restore current stack
                while (!te.empty()) {
                    currentStack.push(te.pop());
                }

                temp.offer(currentStack);//push the current stack to the assist queue
            }

            //restore current queue
            while (!temp.isEmpty()) {
                currQ.offer(temp.poll());
            }

            pos = pos.getRight();//move to the next(right) binnode
        }

        if (oddFlag) {//if odd-flag->true, return false-odd num was found
            return false;
        } else {//else if odd-flag->false, return true-odd num wasnt found
            return true;
        }
    }

    //b
    public static boolean zustk(Stack<Integer> stk) {
        Stack<Integer> temp = new Stack<>();
        boolean flag = false;

        while (!stk.empty()) {
            int x = stk.pop();
            if (x % 2 != 0) {
                flag = true;
            }
            temp.push(x);
        }

        while (!temp.empty()) {
            stk.push(temp.pop());
        }

        if (flag) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean zoq(Queue<Stack<Integer>> q) {
        Queue<Stack<Integer>> temp = new LinkedList<>();
        boolean flag = false;

        while (!q.isEmpty()) {
            Stack<Integer> te = q.poll();

            if (!zustk(te)) {
                flag = true;
            }
            temp.offer(te);
        }

        while (!temp.isEmpty()) {
            q.offer(temp.poll());
        }

        if (flag) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean zol(BinNode<Queue<Stack<Integer>>> chain) {
        BinNode<Queue<Stack<Integer>>> pos = chain;

        while (pos.getLeft() != null) {
            pos = pos.getLeft();
        }

        while (pos != null) {

            Queue<Stack<Integer>> curr = pos.getValue();
            if (!zoq(curr)) {
                return false;
            }
            pos = pos.getRight();
        }
        return true;
    }

    public static boolean zot(BinNode<BinNode<Queue<Stack<Integer>>>> root) {
        Queue<BinNode<BinNode<Queue<Stack<Integer>>>>> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            BinNode<BinNode<Queue<Stack<Integer>>>> current = q.poll();
            if (!zol(current.getValue())) {
                return false;
            }

            if (current.hasLeft()) {
                q.offer(current.getLeft());
            }

            if (current.hasRight()) {
                q.offer(current.getRight());
            }
        }
        return true;
    }

    public static int factorial(int num) {
        if (num < 2) {
            return 1;
        }
        return factorial(num - 1) * num;
    }

    //פונקציה רקורסיבית להדפסת מערך חד מימדי
    private static void prinarr(int[] arr, int i) {
        if (i < arr.length) {
            System.out.println(arr[i]);
            prinarr(arr, i + 1);
        }
    }

    public static void prinarr(int[] arr) {
        prinarr(arr, 0);
    }

    //פונק רקורסיבית המונה כמה ספרות זוגיות יש במספר
    public static int zugcou(int num) {
        if (num == 0) {
            return 0;
        }
        if (((num % 10) % 2) == 0) {
            return 1 + zugcou(num / 10);
        } else {
            return zugcou(num / 10);
        }
    }

    //חישוב סכום ספרות של מספר רקורסיבית
    public static int recsum(int num) {
        if (num == 0) {//תנאי העצירה אם לא נותרו ספרות לסכום
            return 0;
        }
        return (num % 10) + recsum(num / 10);//קריאה לפונק עם הספרה המפורקת+שאר המס
    }

    // פונקציה עוטפת שמתחילה את החישוב מ-0
    public static int sumtilnum(int num) {
        return sumtilnum(num, 0); // קריאה לפונקציה הפרטית
    }

    // פונקציה פרטית שמבצעת את החישוב הרקורסיבי
    private static int sumtilnum(int num, int start) {
        if (start == num) { // תנאי קצה: אם הגענו לסוף
            return 0; // אין עוד מספרים להוסיף
        }
        return start + sumtilnum(num, start + 1); // הוספת המספר הנוכחי והתקדמות לרקורסיה
    }

    //פונקצייה להדפסת מחרוזת הפוכה רקורסיבית
    public static String reverStRec(String str) {
        int end = str.length() - 1; // קובע את האינדקס של התו האחרון
        return reverStRec(str, end); // קריאה לפונקציה הפנימית
    }

    private static String reverStRec(String str, int i) {
        if (i == -1) { // תנאי העצירה: סיימנו את כל התווים
            return ""; // מחזירים מחרוזת ריקה
        }
        // חיבור התו הנוכחי לתוצאה של הקריאה הרקורסיבית
        return str.charAt(i) + reverStRec(str, i - 1);
    }

    public static int maxinarRec(int[] arr) {
        // קריאה לפונקציה רקורסיבית עם אינדקס 0 והמספר הקטן ביותר כערך התחלתי
        return maxinarRec(arr, 0, Integer.MIN_VALUE);
    }

    private static int maxinarRec(int[] arr, int i, int max) {
        // תנאי עצירה: הגעה לסוף המערך
        if (i == arr.length) {
            return max;
        }

        // עדכון הערך המקסימלי אם האיבר הנוכחי גדול יותר
        if (arr[i] > max) {
            max = arr[i];
        }

        // קריאה לפונקציה עבור האינדקס הבא
        return maxinarRec(arr, i + 1, max);
    }

    // פונקציה עוטפת להדפסת הערכים ברשימה
    public static void printList(Node<Integer> chain) {
        Node<Integer> pos = chain;
        printListRecursively(pos); // קריאה לפונקציה רקורסיבית עם פויינטר עזר לראש הרשימה
    }

    // פונקציה רקורסיבית להדפסת הערכים ברשימה
    private static void printListRecursively(Node<Integer> pos) {
        // אם הגענו לסוף הרשימה, לא עושים כלום
        if (pos == null) {
            return;
        }
        // הדפסת הערך של החוליה הנוכחית
        System.out.println(pos.getValue());
        // קריאה רקורסיבית לחוליה הבאה
        printListRecursively(pos.getNext());
    }

    public static void reArrange_Dlist_Even_Odd(BinNode<Integer> chain) {

        //if cahin is null return without do anything
        if (chain == null) {
            return;
        }

        //pointers for the new even list
        BinNode<Integer> even = null;//pointer for head
        BinNode<Integer> le = null;//pointer for tail

        //pointers for the new odd list
        BinNode<Integer> odd = null;//pointer for head
        BinNode<Integer> lo = null;//pointer for tail

        BinNode<Integer> pos = chain;//pointer for original list

        //point the assist pointer for the left far side
        while (pos.getLeft() != null) {
            pos = pos.getLeft();//get to next left binnode
        }

        while (pos != null) {//iterate through the original list
            int x = pos.getValue();//keep the current binnode value
            BinNode<Integer> newey = new BinNode<>(x);//create new binnode to add
            if (x % 2 == 0) {//if the value is even
                if (even == null) {//if the event list is null
                    even = newey;//point the head pointer to the first added binnode in the list
                    le = even;//point the tail pointer to the first added binnode in the list
                } else {//if the event list is not null
                    le.setRight(newey);//connect new binnode
                    le.getRight().setLeft(le);//create doubly connection
                    le = le.getRight();//point the tail pointer to the last added binnode
                }
            } else {//if the value is odd
                if (odd == null) {//if the odd list is null
                    odd = newey;//point the head pointer to the first added binnode in the list
                    lo = odd;//point the tail pointer to the first added binnode in the list
                } else {//if the odd list is not null
                    lo.setRight(newey);//connect new binnode
                    lo.getRight().setLeft(odd);//create doubly connection
                    lo = lo.getRight();//point the tail pointer to the last added binnode
                }
            }
            pos = pos.getRight();//move the assist pointer to the next binnode
        }

        if (even != null && odd != null) {//connect both lists
            le.setRight(odd);//connect the even-list tail pointer to the odd-list head pointer
            le.getRight().setLeft(le);//create doubly connection
        }

        BinNode<Integer> start = null;//new binnode for the new united* list

        if (even != null) {//if even list created, point the assist pointer to the head of the even list
            start = even;
        } else if (even == null && odd != null) {//if odd list created, point the assist pointer to the head of the odd list
            start = odd;
        }

        pos = chain;//restart the assist pointer of the original list to the start(left side)
        BinNode<Integer> prev = null;//pointer for the tail of the original list

        while (pos != null && start != null) {//iterate through both lists(original and united)
            pos.setValue(start.getValue());
            prev = pos;//point the tail pointer (prev) to the assist pointer (pos)
            pos = pos.getRight();//move the assist pointer to the next binnode
            start = start.getRight();//move the assist pointer to the next binnode
        }

        //if the united assist pointer point to null, and the original list pointer not pointing to null
        if (start == null && prev != null) {
            prev.setRight(null);//disconnect the unnecessarily binnodes
        } else if (start != null && pos == null) {//if the united assist pointer not point to null, and the original list pointer pointing to null
            while (start != null) {//iterate through the united list and added new binnodes to the original list
                int x = start.getValue();//save the current value into x
                BinNode<Integer> toadd = new BinNode<>(x);//create new binnode to added
                prev.setRight(toadd);//connect the new binnode
                prev.getRight().setLeft(prev);//create doubly connection
                prev = prev.getRight();//move the assist pointer to the next binnode
                start = start.getRight();//move the assist pointer to the next binnode
            }
        }
    }

    //פונקציה רקורסיבית לבדיקה אם מערך הוא פלינדרום
    public static boolean palArRec(int[] arr) {//wrapper function
        int start = 0;//index of the first cell in the array
        int end = arr.length - 1;//index of the last cell in the array
        return palArRec(arr, start, end);//return call for the private function(palArRec)
    }

    //private func for checking pal in array
    private static boolean palArRec(int[] arr, int start, int end) {
        //if both arr pointers reach the middele
        if (start == end) {
            return true;
        }
        // if arr[i] != arr[arr.length-1} -> false
        if (arr[start] != arr[end]) {
            return false;
        }
        //recursive call -- call the function with: arr ,start++, end--
        return palArRec(arr, start + 1, end - 1);
    }

    //פונק רקורסיבית מונה כמה ערכים זוגיים במערך
    public static int countyz(int[] arr) {//פונק עוטפת
        int i = 0;//אינדקס למערך
        int count = 0;//מונה מופעים זוגיים
        return countyz(arr, i, count);//קריאה לפונק רקורסיבית למניית זוגיים
    }

    private static int countyz(int[] arr, int i, int count) {//פונק עיקרית
        if (i == arr.length) {//אם הגענו לאינדקס האחרון במערך נחזיר את המונה
            return count;
        }
        if (arr[i] % 2 == 0) {//אם המס זוגי
            count++;//מנה אותו
        }
        //קריאה רקורסיבית לפונקציה עם הפנייה למערך,אינדקס הבא והמונה
        return countyz(arr, i + 1, count);
    }

    //פונק המחזירה מערך זוגיים(הנחה arr not null)
    public static int[] arzug(int[] arr) {//פונק עוטפת
        int[] evenArray = new int[countyz(arr)];//יצירת מערך זוגיים בגודל המתאים
        return arzug(arr, evenArray, 0, 0);//קריאה לפונק הרקור' להחזרת מערך זוגיים
    }

    private static int[] arzug(int[] arr, int[] evenArray, int i, int j) {//פונק עיקרית
        if (i == arr.length) {//אם הגענו לאינדקס האחרון במערך נחזיר את מערך הזוגיים
            return evenArray;
        }
        if (arr[i] % 2 == 0) {//אם ערך התא הנוכחי במערך הינו זוגי "נעביר" אותו למערך הזוגיים
            evenArray[j] = arr[i];
            j++;//קידום אינדקס מערך הזוגיים
        }
        //קריאה רקורסיבית עם שני המערכים ושני האינדקסים שלהם
        // כאשר רק האינדקס של המערך המקורי מקודם
        // -כי אין אנו רוצים שהאינדקס של המערך השני יקודם ללא סיבה מוצדקת
        return arzug(arr, evenArray, i + 1, j);
    }

    //עץ מושלם הוא עץ אשר הצד השמאלי שלו גדול מהצד הימני שלו והמס יחודיים בשני תתי העצים
    //בדיקה אם מס נמצא בתת עץ (הימני) או קטן מהערכים(בעץ הימני במיקרה זה)
    public static boolean uniq_num_and_bigger(BinNode<Integer> root, int num) {
        //the point: if small and not uniq -> false
        Queue<BinNode<Integer>> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            BinNode<Integer> current = q.poll();
            if (current.getValue() == num && current.getValue() > num) {
                return false;
            }

            if (current.hasLeft()) {
                q.offer(current.getLeft());
            }

            if (current.hasRight()) {
                q.offer(current.getRight());
            }
        }
        return true;
    }

    public static boolean lr_big_rr(BinNode<Integer> root) {

        //if the three is empty -> return false
        if (root == null) {
            return false;
        }

        //create two pointers for the two sides of the three
        BinNode<Integer> leftRoot = null;
        BinNode<Integer> rightRoot = null;

        //if the parrent root have left child, pointer leftroot will point on it
        if (root.hasLeft()) {
            leftRoot = root.getLeft();
        }

        //if the parrent root have right child, pointer rightroot will point on it
        if (root.hasRight()) {
            rightRoot = root.getRight();
        }

        //split the tree for two main sides(left - right)
        //disconnect the parrent from it's childrens
        root.setLeft(null);
        root.setLeft(null);

        //create new queue for breadth first search
        Queue<BinNode<Integer>> q = new LinkedList<>();
        q.offer(leftRoot);//offer the left-child side to it

        while (!q.isEmpty()) {//while the queue isnt empty->still have binnodes
            //poll current binnode from the queue and create new pointer(current) that points to it
            BinNode<Integer> current = q.poll();
            //if the current binnode value (numeric) smaller or equal to any value from the second three
            //return false imidiatly
            if (!uniq_num_and_bigger(rightRoot, current.getValue())) {
                return false;
            }

            //check if the current binnode is a parent or not
            //if it's have left child, offer it to the queue
            if (current.hasLeft()) {
                q.offer(current.getLeft());
            }

            //if it's have right child, offer it to the queue
            if (current.hasRight()) {
                q.offer(current.getRight());
            }
        }

        //connect the main two sides to the root of the tree
        root.setLeft(leftRoot);
        root.setLeft(rightRoot);
        //if we reach here it's mean everything is fine -> return true
        return true;
    }

    public static void reverse(int n) {
        if (n < 10) {
            System.out.println(n);
        } else {
            System.out.print(n % 10);
            reverse(n / 10);
        }
    }
    //פלט -> 7 2 4 6

    /*
    סדרת פיבונאצי היא סידרה שכל איבר בה הוא סכום שני איברים הקודמים לו
    כתוב פונקציה המקבלת K ומחזירה את המס ש K מצביע עליו
     */
    public static int fibonachi(int k) {
        if (k == 1) {
            return 0;
        }

        if (k == 2) {
            return 1;
        }

        return fibonachi(k - 1) + fibonachi(k - 2);
    }

    public static int mysterySum(int n) {
        if (n == 1) {
            return 1;
        }

        int sum = n;
        for (int i = 0; i < n; i++) {
            sum = sum + mysterySum(i);
        }

        return sum;
    }

    public static boolean isPali(String str) {//wrapper function
        int start = 0;//אינדקס לתחילת המחרוזת
        int end = str.length() - 1;//אינדקס לסוף המחרוזת
        return isPali(str, start, end);//החזר קריאה לפונק עם המחרוזת,אינדקס התחלה,אינדקס סוף
    }

    //פונק פרטית לבדיקה אם מחרוזת היא מחרוזת פלינדרום
    private static boolean isPali(String str, int start, int end) {
        //אם האינדקס הראשון שווה לאינדקס האחרון
        // כלומר הגענו לאמצע המחרוזת נחזיר אמת
        if (start == end) {
            return true;
        }

        //אם ערך האיבר הראשון שונה מהאיבר האחרון המקביל לו, נחזיר שקר
        if (str.charAt(start) != str.charAt(end)) {
            return false;
        }

        //נחזיר קריאה לפונק עם המחרוזת,קידום אינדקס התחלתי,קידום אינדקס מהסוף
        return isPali(str, start + 1, end - 1);
    }


    public static int mystery5(int num) {
        int num1 = 10;//save copy of num
        int count = 0;//create counter

        while (num > 0) {//iterate through num and count
            count++;
            num /= 10;
        }

        int n = 0;//create num
        while (num1 > 0) {
            n += num1 % 10 * Math.pow(10, count - 1);
        }

        return n;
    }

    public static int revmystery5(int num) {
        String n = Integer.toString(num);
        String newN = "";

        for (int i = n.length() - 1; i >= 0; i--) {
            newN += n.charAt(i);
        }

        int reNum = Integer.parseInt(newN);

        return reNum;
    }


    public static boolean stkPal(Stack<Integer> stk) {

        Stack<Integer> copyStk = cloneStack(stk);//use copy stack
        Stack<Integer> temp = new Stack<>();//for partly restoration
        int length = copyStk.size();//save the size of the stack

        if (length == 1) {//if the size of the stack equals to one its palindrome -> return true
            return true;
        }

        while (length > 1) {//while the copy stack is not empty
            int first = copyStk.pop();//save first node value in the copy stack
            length--;
            int count = 0;//counter for poping out the currently unwanted nodes

            while (!copyStk.isEmpty() && count < length - 1) {
                temp.push(copyStk.pop());//push nodes to assist stack for restoration
                count++;//count + 1
            }

            int last = copyStk.pop();//save last node value in the copy stack
            length--;

            //if the first node val not equal to the last node val -> return false
            if (first != last) {
                return false;
            }

            //partly restoration
            while (!temp.isEmpty()) {
                copyStk.push(temp.pop());
            }
        }
        //if the func didnt return false its mean the stack is palindrome -> return true
        return true;
    }

    public static Node<Student>[] arLiMonth(School s1) {
        Node<Student>[] arr = new Node[12];//יצירת מערך רשימות התלמידים ממויין לפי חודשי לידה

        //מעבר על מערך השכבות (שרשראות התלמידים-ממויין לפי מס שכבה)
        for (int i = 0; i < s1.getGrades().length; i++) {

            if (s1.getGrades()[i] != null) {//אם השכבה לא ריקה מתלמידים

                Node<Student> pos = s1.getGrades()[i];//פויינטר לראש השרשרת בתא הנוכחי של השכבה הנוכחית

                while (pos != null) {//כל עוד לא הדענו לסוף שרשרת חוליות התלמידים

                    int month = pos.getValue().getDate().getMonth();//שמירת חודש של תלמיד נוכחי
                    Student temp = pos.getValue();//פויינטר לסטודנט נוכחי

                    //הוספה למערך שרשראות החדש(עפ חודש לידה בלבד)
                    if (arr[month - 1] == null) {//אם התא הנוכחי ריק
                        arr[month - 1] = new Node<>(temp);//תא זה יפנה לחוליית תלמיד חדשה
                    } else {//אחרת אם התא הנוכחי לא ריק
                        Node<Student> last = arr[month - 1];//פויינטר למציאת זנב שרשרת
                        while (last.getNext() != null) {//כל עוד לא הגענו לזנב
                            last = last.getNext();//התקדמות לחולייה הבאה
                        }
                        last.setNext(new Node<>(temp));//הוספת חולייה חדשה בסוף השרשרת
                    }

                    pos = pos.getNext();//התקדמות לחוליית התלמיד הבאה
                }
            }
        }
        return arr;// החזרת מערך שרשראות התלמידים(ממויין ע"פ חודשי לידה)
    }

    /*
      0   1   2   3   4   5...12
    -------------------------
    |   |   |   |   |   |   |
    --|----------------------
      |
     \|/
    --------------
    |*value|*next|
    -----|--------
         |     --------------
         |---->|*value|*next| --> null
               ---|----------
                  |
                 \|/
              |--------|
              |Student |
              ----------
              | *name  |
              | *Date d|
              -------|--
                     |
                    \|/
                  |------|
                  | Date |
                  --------
                  |*day  |
                  |*month|
                  |*year |
                  --------
     */

    public static int same(int n1, int n2) {//פונקציה עוטפת
        int count = 0;//הגדרת מונה
        String num1 = Integer.toString(n1);//המרת המס הראשון למחרוזת
        String num2 = Integer.toString(n2);//המרת המס הראשון למחרוזת
        int i = 0;//אינדקס למחרוזת המס הראשון
        int j = 0;//אינדקס למחרוזת המס השני
        return same(num1, num2, i, j, count);//קריאה לפונקציה הפרטית שמחזירה את כמות הספרות הזהות
    }

    private static int same(String num1, String num2, int i, int j, int count) {
        if (i == num1.length() && j == num2.length()) {//אם הגענו לקצוות המחרוזת החזר מונה
            return count;
        }

        if (num1.charAt(i) == num2.charAt(j)) {//אם הספרות שוות במיקומן ובערכן
            count++;//קדם מונה
        }

        //קריאה רקורסיבית לפונקציה , קידום שני המונים ,שליחת שני המס ושליחת המונה
        return same(num1, num2, i + 1, j + 1, count);
    }

    //דרך א -ממוצע סכום ספרות
    //פונקציה רקורסיבית לכמות ספרות
    public static double lengthRec(int num) {
        if (num == 0) {
            return 0;
        }
        return 1 + lengthRec(num / 10);
    }

    //פונקציה רקורסיבית לסכום ספרות
    public static double sumrec(int num) {
        if (num == 0) {
            return 0;
        }
        return (num % 10) + sumrec(num / 10);
    }

    //פונקציה המחזירה ממוצע
    public static double avgrec(int num) {
        if (num < 10) {
            return num;
        }
        double avg = (sumrec(num)) / (lengthRec(num));
        return avg;
    }

    //דרך ב
    public static double avgDigits(int n) {
        if (n < 10) {
            return n;
        }
        return (((n % 10) + Math.round(avgDigits(n / 10)))) / 2.0;
    }

    /*
    n = 251; |1+round(avgDigits(25))/2| |5+round(avgDigits(2))/2| |2| -> |(5+2)/2=3.5| ->|(1+3.5)/2.7
     */
    //פונקצייה הממירה מס עשרוני לבינארי
    public static void printToBin(int n) {
        if (n != 0) {
            printToBin(n / 2);
            System.out.println(n % 2);
        }
    }

    //----------------------------------------------------------------------------------------------------------------//
    //find index(from top) of number in stk (1)
    public static int firsp(Stack<Integer> stk, int num) {
        Stack<Integer> copyStk = cloneStack(stk);//copy original stack
        int count = 0;//counter for the index

        while (!copyStk.isEmpty()) {//empty the copy stack
            int x = copyStk.pop();//get current stack node value
            count++;//counter+1

            if (x == num) {//if current node value equals to num, return the index of it
                return count;
            }
        }

        return -1;//if was'nt found return -1
    }

    //find index(from bottom) of number in stk (2)
    public static int lasp(Stack<Integer> stk, int num) {
        Stack<Integer> copyStk = cloneStack(stk);//copy original stack
        Stack<Integer> temp = new Stack<>();//assist stack

        //while copy stk is not empty, push its nodes to the assist stack so we can start iterate from the bottom of the copy stk
        while (!copyStk.isEmpty()) {
            temp.push(copyStk.pop());
        }

        int count = 0;//counter for the index

        while (!temp.isEmpty()) {
            int x = temp.pop();
            count++;//counter+1

            if (x == num) {//if current node value equals to num, return the index of it
                return count;
            }
        }

        return -1;//if was'nt found return -1
    }

    //func to check how many times number shown in stack (3)
    public static int howocc(Stack<Integer> stk, int num) {
        Stack<Integer> copyStk = cloneStack(stk);//clone the original stack
        int count = 0;//counter for the occurances of num

        while (!copyStk.isEmpty()) {//while clone stack is not empty
            int x = copyStk.pop();// x = value
            if (x == num) {//if this value equals to num count + 1
                count++;
            }
        }
        return count;//return number of occurances of num
    }

    //func to check if every value in stack not found twice (4)
    public static boolean twoXoccu(Stack<Integer> stk) {
        Stack<Integer> copyStk = cloneStack(stk);//clone the original stack

        while (!copyStk.isEmpty()) {//while clone stack is not empty
            int x = copyStk.pop();// x = value

            //call func that check number of occurance of number in stack,
            // if it returns number that higher than the num 2,
            // its mean that number shown more than 2 times in stack -> return false
            if (howocc(stk, x) > 2) {
                return false;
            }
        }
        return true;//return true <-> all numbers in stack shown only twice
    }

    //func that find out the min dis between two occurances of some number (5)
    public static int miniDis(Stack<Integer> stk) {
        // if it returns false its mean that there is at least one number that shown more than 2 times in stack
        // -> return false
        if (!twoXoccu(stk)) {
            return -1;
        }

        Stack<Integer> copyStk = cloneStack(stk);
        int length = stk.size();//size of stack
        int distance = 0;//var for "distance"-> between [first occurance and last occurance] of number
        int firsOcuur = -1;//first index
        int lasOcuur = -1;//last index
        int minDis = Integer.MAX_VALUE;//put minDis var to the highest integer value

        while (!copyStk.isEmpty()) {//while copyStk is not empty
            int num = copyStk.pop();//pop out current node, and keep the value

            if (copyStk.contains(num)) {//if num found more than once in stack

                firsOcuur = firsp(stk, num);//save index (from top) of num
                lasOcuur = lasp(stk, num);//save index (from bottom) of num
                distance = length - firsOcuur - lasOcuur;//calculate distance between them

                //found the minimum distance between two occurance of number(from top and from bottom)
                if (distance < minDis) {
                    minDis = distance;
                }
            }
        }

        //if minimum distance variable value diffrent from the highest integer value,
        // its mean that there are at least one number that found twice in stack, and its distance saved.
        if (minDis != Integer.MAX_VALUE) {
            //return the minimum distance between two occurance of number(from top and from bottom)
            return minDis;
        } else {//if there is no any doubly occurance numbers return -1
            return -1;
        }
    }

    /*

 stk--|
     \'/

   |  6  | first_index = 1 } func 1 - firsp() // מוצאת מיקום מספר מתחילת המחסנית
   -------                 } func 2 - lasp() // מוצאת מיקום מס מסוף המחסנית
   |  2  |                 } func 3 - howocc() // כמה פעמים מס מופיע במחסנית
   -------                 } func 4 - twoXoccu() // בודקת אם מס מופיע לא יותר פעמיים במחסנית
   |  6  | last_index = 1  } func 5 - miniDis() [int distance = stk.size() - first_index - last_index;]//מרחק הכי קטן בין שני מופעים של מס
   -------

     */

    //func that find out the min dis between two occurances of some number in some stack in the three(6)
    public static int lowMinStkInThree(BinNode<Stack<Integer>> root) {
        Queue<BinNode<Stack<Integer>>> q = new LinkedList<>();//queue for bfs
        q.offer(root);//offer the root of the three to the bfs queue
        int low = Integer.MAX_VALUE;//intialize low (minimum variable) to the highest integer value

        while (!q.isEmpty()) {//while the queue is not empty

            BinNode<Stack<Integer>> current = q.poll();//create pointer for the node who polled out of the queue
            Stack<Integer> temp = current.getValue();//stack pointer to the stack "in" the node
            int currentMinLow = miniDis(temp);//calc min distance between two occurances of number in stack

            if (currentMinLow < low) {
                low = currentMinLow;//keep the shortest distance "in" the min var (low)
            }

            if (current.hasLeft()) {//if there is "left son" offer it to the queue
                q.offer(current.getLeft());
            }

            if (current.hasRight()) {//if there is "right son" offer it to the queue
                q.offer(current.getRight());
            }
        }

        //if min var contain other value than highest integer value
        // means there is at least one number that seen in some stack twice
        // and distance between the occurances is saved in min var (low)
        if (low != Integer.MAX_VALUE) {
            return low;// -> return the lowest dis between two ocuurances of some number
        } else {//there is no any num that seen twice in any stack , -> return -1
            return -1;
        }
    }

    //private static final Object lock = new Object();//אובייקט לעיקוב

    public static Node<RangeNode> tvachim(Node<Integer> chain) {
        // אם הרשימה ריקה או חוליה אחת בלבד
        if (chain == null || chain.getNext() == null) {
            return null;
        }

        Node<RangeNode> reu = null;// ראש הרשימה החדשה
        Node<RangeNode> leu = null;// סוף הרשימה החדשה
        Node<Integer> pos = chain;// מצביע לרשימה המקורית

        while (pos != null && pos.getNext() != null) {
            int curr = pos.getValue();
            int nex = pos.getNext().getValue();

            if (curr < nex) {// התחלת טווח עולה
                int min = curr;
                while (pos.getNext() != null && pos.getValue() < pos.getNext().getValue()) {
                    pos = pos.getNext();
                }
                // סיום הטווח
                int max = pos.getValue();

                // יצירת RangeNode
                RangeNode temp = new RangeNode(min, max);
                Node<RangeNode> newey = new Node<>(temp);
                if (reu == null) {// עדכון ראש הרשימה
                    reu = newey;
                    leu = reu;
                } else {// הוספה לסוף הרשימה
                    leu.setNext(newey);
                    leu = leu.getNext();
                }
            } else if (curr == nex) { // טיפול באיברים זהים
                int min = curr;
                int max = min;// min ו-max שווים במקרה זה
                RangeNode temp = new RangeNode(min, max);
                Node<RangeNode> newey = new Node<>(temp);
                if (reu == null) {// עדכון ראש הרשימה
                    reu = newey;
                    leu = reu;
                } else {// הוספה לסוף הרשימה
                    leu.setNext(newey);
                    leu = leu.getNext();
                }
            }
            pos = pos.getNext();// קידום מצביע
        }
        // טיפול באיבר האחרון ברשימה אם הוא לא נכלל
        if (pos != null) {
            int last = pos.getValue();
            RangeNode range = new RangeNode(last, last); // יצירת טווח עם ערך יחיד
            Node<RangeNode> newNode = new Node<>(range);

            if (reu == null) { // אם הרשימה החדשה עדיין ריקה
                reu = newNode;
                leu = reu;
            } else {
                leu.setNext(newNode); // הוספה לסוף הרשימה
                leu = leu.getNext();
            }
        }
        return reu;
    }

    //פונקצייה עוטפת
    public static int evNumInArray(int[] arr) {
        int i = 0;//אינדקס למערך
        int count = 0;//מונה ערכים זוגיים
        return evNumInArray(arr, i, count);//קריאה לפונקציה הפרטית למציאת כמות ערכים זוגיים במערך
    }

    //פונקציה עיקרית רקורסיבית לחישוב כמות ערכים זוגיים במערך
    private static int evNumInArray(int[] arr, int i, int count) {
        if (i == arr.length) {
            return count;
        }//אם הגענו לסוף המערך, נחזיר את המונה
        if (arr[i] % 2 == 0) {
            count++;
        }//אם הערך בתא הנוכחי זוגי, נמנה אותו
        return evNumInArray(arr, i + 1, count);//קריאה חוזרת לפונקציה עם אינדקס מקודם ב1
    }

    //פונקצייה עוטפת
    public static int[] evAr(int[] arr) {
        int i = 0;//אינדקס עבור מערך מקור
        int j = 0;//אינדקס עבור מערך הזוגיים
        int[] evenArray = new int[evNumInArray(arr)];//יצירת מערך הזוגיים בגודל המתאים
        return evAr(arr, evenArray, i, j);//קריאה לפונק הפרטית שתחזיר מערך זוגיים
    }

    //פונקצייה פרטית להחזרת מערך ערכים זוגיים
    private static int[] evAr(int[] arr, int[] evenArray, int i, int j) {
        if (i == arr.length) {
            return evenArray;
        }//אם הגענו לסוף המערך מקור נחזיר את מערך הזוגיים
        //אם הערך בתא הנוכחי זוגי, נוסיף העתק שלו למערך הזוגיים
        // ונקדם את אינדקס מערך הזוגיים ב 1
        if (arr[i] % 2 == 0) {
            evenArray[j++] = arr[i];
        }
        return evAr(arr, evenArray, i + 1, j);//קריאה חוזרת לפונקציה עם מערך מקור ומע' זוגיים,אינדקס מע' ראשון ואחרון+1
    }

    public static int my(int num) {
        if (num == 1) {
            return 1;
        }
        int sum = num;
        for (int i = 1; i < num; i++) {
            sum += my(i);
        }
        return sum;
    }
    /*
  מעקב:

    num = 4; sum = 4;     4  +    my(1)   +      my(2)       +     my(3)                = 15
    -------------------------------------------------------------------------------------------
                         |4| + |my(1)->1| + |2+my(1)=1+2->3| + |3+my(1)+my(2)=3+1+3->7| = 15
     */

    public static int[] whrI(Node<Integer> chain) {
        Node<Integer> temp = chain;//פויינטר עזר לריצה על השרשרת
        int count = 0;//מונה
        while (temp != null) {//מעבר על השרשרת ומניית החוליות
            count++;
            temp = temp.getNext();
        }

        int[] arr = new int[count];//יצירת מערך בגודל אורך השרשרת חוליות
        int i = 0;//אינדקס למערך החדש

        Node<Integer> pos = chain;//פויינטר עזר לריצה על השרשרת
        while (pos != null) {//מעבר על השרשרת ובניית המערך
            int x = pos.getValue();//ערך חוליה
            arr[i++] = x;//הצבת הערכים למערך
            pos = pos.getNext();//מעבר לחולייה הבאה
        }
        return arr;//החזרת המערך
    }//O(n)

    //----------------------------------------------------------------------------------------------------------------//
    public static int countyr(Node<Integer> chain) {//פונקצייה עוטפת
        Node<Integer> pos = chain;
        return countyr(chain, pos);
    }

    private static int countyr(Node<Integer> chain, Node<Integer> pos) {//פונקציה רקורסיבית למניית חוליות בשרשרת
        if (pos == null) {
            return 0;
        }
        return 1 + countyr(chain, pos.getNext());
    }

    public static int[] NodeToarr(Node<Integer> chain) {//פונקציה עוטפת להחזרת מערך המכיל את תוכן השרשרת
        int[] arr = new int[countyr(chain)];
        int i = 0;
        Node<Integer> pos = chain;
        return NodeToarr(chain, pos, arr, i);
    }

    private static int[] NodeToarr(Node<Integer> chain, Node<Integer> pos, int[] arr, int i) {
        if (pos == null) {
            return arr;
        }
        arr[i++] = pos.getValue();
        return NodeToarr(chain, pos.getNext(), arr, i);
    }

    //-----------------------------------------------------------------------------------------------------------------//
    private static Integer[] arrayDinamic = {1, 2, 3, 4, 5, 6, 7};//מערך סטטי
    //פונקציה להרחבת מערך והוספת ערך
    public static void toAdd(Integer[] arr, int num) {
        //מעבר על המערך הריק נבדוק קודם אם יש תא ריק ונוסיף את הערך שם
        for (int j = 0; j < arrayDinamic.length; j++) {
            if (arrayDinamic[j] == null) {                                       //!!//
                arrayDinamic[j] = num;//נוסיף ערך חדש
                return;//נחזיר-אין טעם להמשיך הלאה
            }
        }
        //אחרת אם הגעתי לכאן משמע שאין תא ריק להוסיף אליו את הערך החדש,
        // ולכן אצטרך ליצור מערך עזר בגודל המתאים כמובן היכיל גם את האיבר להוספה והערכים הרגילים שהיו במערך המקור
        Integer[] temp = new Integer[arrayDinamic.length + 1];//מערך עזר
        for (int i = 0; i < arrayDinamic.length; i++) {//מעבר על המערך מקור
            temp[i] = arrayDinamic[i];//העתקת תוכן מערך מקור למערך עזר
        }
        temp[temp.length - 1] = num;//הוספת הערך החדש לסוף מערך העזר
        arrayDinamic = temp;//הפניית פוינטר מערך מקור למערך עזר
    }
    //----------------------------------------------------------------------------------------------------------------//
    //4
    public static Node<Integer> createRandomChain(int numNodes) {
        if (numNodes <= 0) {
            return null;
        }
        Random rand = new Random();//זימון מחלקת ראנדום
        Node<Integer> newey = null;//חולית ראש לשרשרת
        Node<Integer> le = null;//חוליית זנב לשרשרת

        for (int i = 0; i < numNodes; i++) {
            int num = rand.nextInt(100);
            Node<Integer> toAdd = new Node<>(i);//יצירת חוליה חדשה
            if (newey == null) {//אם הרשימה ריקה נקשר פויינטר ראש אלייה
                newey = toAdd;
                le = newey;//נחבר פויינטר זנב בהתאם
            } else {//אחרת אם הרשימה לא ריקה
                le.setNext(toAdd);//נוסיף חוליה זו לסוף השרשרת
                le = le.getNext();//נקדם פויינטר זנב אלייה
            }
        }
        return newey;
    }

    //5
    //נתונה הפעולה הבאה המקבלת שני שרשראות חוליות
    public static void change(Node<Integer> chain1, Node<Integer> chain2) {
        Node<Integer> pos = chain1;
        while (pos.getNext() != null) {
            pos = pos.getNext();
        }
        pos.setNext(chain2);
    }
    /*
    א
    chain1 = [1 -> 2 -> 3 -> 4 -> 5 -> 6]
    chain2 = [4 -> 5 -> 6]
    ב
    טענת יציאה:שרשרת אחד מחוברת לשרשרת שתיים
    ג
    יעילות
    O(n)
     */

    //6
    //נתונה הפונקציה הרקורסיבית המקבלת שרשרת חוליות
    public static boolean secret(Node<Integer> chain) {
        if (chain.getNext() == null) {
            return true;
        }
        int x = chain.getValue();
        int y = chain.getNext().getValue();
        if (x * y > 0) {
            return false;
        }
        return secret(chain.getNext());
    }

    /*
    א
    דוגמא לשרשרת עבורה זימון הפעולה יחזיר "אמת"
    1 -> -2 -> 3 -> -4

   דוגמא לשרשרת עבורה זימון הפעולה יחזיר "אמת"
    1 -> 2 -> 3 -> 4

    ב
    טענה הכניסה:אם השרשרת ריקה או מכפלת ערכי חוליות צמודות נותנות תוצאה שלישית(יוחזר אמת)
    טענת היצאה:אם השרשרת אכן עונה על התנאים(יוחזר אמת)
    הנחת היסוד היא ששרשרת חוליות חייבת להיות בנוייה מערך חיובי ערך שלילי בהתאמה לאורכה
    כך שמכפלת שני איברים צמודים תיתן הוצאה שלילית
     */

    //9
    //נתונה הפעולה הרקורסיבית הבאה
    public static String mystery(Node<Character> chain) {
        if (chain.getNext() == null) {
            return chain.getValue().toString();
        }
        return mystery(chain.getNext()) + "," + chain.getValue();
    }

    /*
    א
    mystery(chain.getNext()) -> + ,x
    -> mystery(chain.getNext()) + ,b
    -> mystery(chain.getNext()) + ,k
    -> mystery(chain.getNext()) + ,z
    -> mystery(chain.getNext()) + ,e
    -> mystery(chain.getNext()) + ,z
    returned string = z,e,z,k,b,x
    ב
    טענת היציאה - הפונקציה מחזירה את תוכן השרשרת הפוך
    כאשר פויינטר השרשרת מפנה ל"ריק"
    מתחילים לבנות את מחרוזת תוכן השרשרת הפוך
     */
    //ג-מימוש הפעולה ללא שימוש ברקורסיה
    public static String recPrint(Node<Character> chain) {
        String str = "";//מחרוזת ריקה לקליטת תוכן השרשרת
        Node<Character> pos = chain;//פויינטר עזר לשרשרת מקור
        while (pos != null) {//כל עוד פויינטר העזר לא מפנה ל "ריק"
            str += pos.getValue();//נצבור את התו בחוליה הנוכחית למחרוזת
            pos = pos.getNext();//נתקדם לחוליה הבאה
        }
        String rec = "";//מחרוזת ריקה שתכיל את מחרוזת התווים בצורה הפוכה
        for (int i = str.length() - 1; i > 0; i--) {
            rec += str.charAt(i) + ",";
        }
        return rec;//נחזיר מחרוזת הפוכה זו
    }

    //10
    //מהי טענת היציאה של הפונקציה הבאה:
    public static void mys(Node<Integer> chain) {
        int temp = 0;
        Node<Integer> pos1 = chain;
        Node<Integer> pos2 = null;

        while (pos1.getNext() != null) {
            pos2 = pos1.getNext();
            while (pos2 != null) {
                if (pos1.getValue() > pos2.getValue()) {
                    temp = pos1.getValue();
                    pos1.setValue(pos2.getValue());
                    pos2.setValue(temp);
                }
                pos2 = pos2.getNext();
            }
            pos1 = pos1.getNext();
        }
    }
    //פונקציה זו ממינת שרשרת בסדר עולה

    //11
    //הפעולה מקבלת שתי שרשראות של חוליות מס שלמים,ממויינות בסדר עולה.
    //הפעולה מבצעת מיזוג של שני השרשראות, עליכם לממש את הפעולה בשני צורות שונות
    //א-הפעולה תחזיר הפניה לשרשרת חוליות חדשה שהיא מיזוג של שני השרשראות
    public static Node<Integer> merge1(Node<Integer> chain1, Node<Integer> chain2) {
        if (chain1 == null) return chain2; // אם השרשרת הראשונה ריקה
        if (chain2 == null) return chain1; // אם השרשרת השנייה ריקה
        Node<Integer> star = null;//פוינטר ראש לשרשרת איחוד החדשה
        Node<Integer> tail = null;//פויינטר זנב לשרשרת איחוד החדשה
        Node<Integer> pos1 = chain1;//פויינטר עזר לשרשרת 1
        Node<Integer> pos2 = chain2;//פויינטר עזר לשרשרת 2

        while (pos1 != null) {//כל עוד פויינטר עזר לשרשרת 1 לא מפנה ל"ריק" נתקדם
            int x = pos1.getValue();//שמירת ערך חולייה נוכחית
            Node<Integer> current = new Node<>(x);//יצירת חולייה חדשה המכילה ערך זה שאותה נירצה להוסיף לשרשרת האיחוד החדשה
            if (star == null) {//אם השרשרת איחוד ריקה
                star = current;//נקשר פויינטר ראש אל חוליה חדשה זו
                tail = star;//נקשר פויינטר זנב אל חולייה זו
            } else {//אחרת אם השרשרת איחוד לא ריקה
                tail.setNext(current);//נחבר חוליה חדשה זו לסוף שרשרת האיחוד
                tail = tail.getNext();//ונקדם את פויינטר הזנב של שרשרת האיחוד אלייה
            }
            pos1 = pos1.getNext();//נתקדם לחולייה הבאה בשרשרת 1 שהתקבלה בפונקציה כפרמטר
        }
        while (pos2 != null) {//כל עוד פויינטר עזר לשרשרת 2 לא מפנה ל"ריק" נתקדם
            int x = pos2.getValue();//שמירת ערך חולייה נוכחית
            Node<Integer> current = new Node<>(x);//יצירת חולייה חדשה המכילה ערך זה שאותה נירצה להוסיף לשרשרת האיחוד החדשה
            if (star == null) {//אם השרשרת איחוד ריקה
                star = current;//נקשר פויינטר ראש אל חוליה חדשה זו
                tail = star;//נקשר פויינטר זנב אל חולייה זו
            } else {//אחרת אם השרשרת איחוד לא ריקה
                tail.setNext(current);//נחבר חוליה חדשה זו לסוף שרשרת האיחוד
                tail = tail.getNext();//ונקדם את פויינטר הזנב של שרשרת האיחוד אלייה
            }
            pos2 = pos2.getNext();//נתקדם לחולייה הבאה בשרשרת 2 שהתקבלה בפונקציה כפרמטר
        }
        mys(star);//נקרא לפונקציה הממיינת את שרשרת האיחוד בסדר עולה(מסעיף 10)
        return star;//נחזיר הפניה לשרשרת איחוד החדשה
    }

    //ב-הפונק תחזיר הפניה לשרשרת חוליות שהיא מיזוג של שני שרשראות תוך שימוש בחוליות הקיימות של שתי השרשראות ללא יצירת חוליות חדשות
    public static Node<Integer> merge2(Node<Integer> chain1, Node<Integer> chain2) {
        Node<Integer> tail1 = chain1;//פויינטר זנב לשרשרת 1
        while (tail1.getNext() != null) {//נחפש זנב שרשרת זו
            tail1 = tail1.getNext();
        }
        tail1.setNext(chain2);//נאחד בין השרשראות
        Node<Integer> start = chain1;//ניצור הפניה חדשה לשרשרת מקור
        mys(start);//נקרא לפונקציה הממיינת את שרשרת האיחוד בסדר עולה(מסעיף 10)
        return start;//נחזיר הפניה זו
    }

    //----------------------------------------------------------------------------------------------------------------//

    //להעתיק למחברת
    //8
    public static void compressSequences(Node<Character> chain) {
        Node<Character> pos = chain;   // מצביע לשרשרת המקורית
        while (pos != null) {
            char current = pos.getValue();
            Node<Character> start = pos;
            // דילוג על כל הרצף של אותו הערך
            while (pos.getNext() != null && pos.getNext().getValue() == current) {
                pos = pos.getNext();
            }
            pos = pos.getNext(); // מעבר לחוליה הבאה
            Node<Character> end = pos;
            start.setNext(end);
        }
    }

    //12
    public static void disconnect(Node<Integer> chain1, Node<Integer> chain2) {
        Node<Integer> pos1 = chain1;//פויינטר עזר לשרשרת 1
        Node<Integer> pos2 = chain2;//פןיינטר עזר לשרשרת 2
        //כל עוד שני פויינטרי העזר של שני השרשראות לא מפנים ל"ריק"
        while (pos1 != null) {
            while (pos2 != null) {
                if (pos1.getNext() != null && pos2.getNext() != null && pos1.getNext() == pos2.getNext()) {
                    pos1 = pos1.getNext();
                    while (pos1 != null) {
                        pos2.setNext(new Node<>(pos1.getValue()));
                        pos2 = pos2.getNext();
                        pos1 = pos1.getNext();
                    }
                    return;
                }
                pos2 = pos2.getNext();//נקדם פויינטר עזר שרשרת שניה
            }//while(2) ends here
            pos2 = chain2;//איפוס חוזר של פויינטר שרשרת 2 לתחילתה
            pos1 = pos1.getNext();
        }//while(1) ends here
    }

    //שאלה מבוחן 1-מיון שלושה שרשראות (גדול-בינוני-קטן)
    public static Node<Integer> sorty2(Node<Integer> chain1, Node<Integer> chain2, Node<Integer> chain3){
        int len1 = countNodes(chain1);
        int len2 = countNodes(chain2);
        int len3 = countNodes(chain3);

        Node<Integer> big = null;
        Node<Integer> mid = null;
        Node<Integer> small = null;

        if (len1 > len2 && len1 > len3){
            big = chain1;
            if (len2 > len3){
                mid = chain2;
                small = chain3;
            }else{
                mid = chain3;
                small = chain2;
            }
        } else if (len2 > len1 && len2 > len3) {
            big = chain2;
            if (len1 > len3){
                mid = chain1;
                small = chain3;
            }else{
                mid = chain3;
                small = chain1;
            }
        } else if (len3 > len1 && len3 > len2) {
            big = chain3;
            if (len1 > len2){
                mid = chain1;
                small = chain2;
            }else{
                mid = chain2;
                small = chain1;
            }
        }

        Node<Integer> tail1 = big;
        Node<Integer> tail2 = mid;

        while (tail1.getNext() != null){
            tail1 = tail1.getNext();
        }
        tail1.setNext(mid);

        while (tail2.getNext() != null){
            tail2 = tail2.getNext();
        }
        tail2.setNext(small);

        return big;
    }

    public static Node<Integer> sorty1(Node<Integer> chain1, Node<Integer> chain2, Node<Integer> chain3){
        int len1 = countNodes(chain1);
        int len2 = countNodes(chain2);
        int len3 = countNodes(chain3);

        Node<Integer> pos1 = chain1;
        Node<Integer> pos2 = chain2;
        Node<Integer> pos3 = chain3;
        while (pos1.getNext() != null){
            pos1 = pos1.getNext();
        }
        while (pos2.getNext() != null){
            pos2 = pos2.getNext();
        }
        while (pos3.getNext() != null){
            pos3 = pos3.getNext();
        }

        int [] arr = new int[3];
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        arr[0] = len1;
        arr[1] = len2;
        arr[2] = len3;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max){
                max = arr[i];
            }
            if (arr[i] < min){
                min = arr[i];
            }
        }
        //אם 1 הכי גדול 2קטן 3 בינוני
        if (len1 == max && len2 == min && len3 != min && len3 != max){
            pos1.setNext(chain3);
            pos3.setNext(chain2);
            return chain1;
        }
        //אם 1 גדול 2 בינוני 3 קטן
        if (len1 == max && len2 != min && len2 != max && len3 == min){
            pos1.setNext(chain2);
            pos2.setNext(chain3);
            return chain1;
        }
        //אם 2 גדול 3 בינוני 1 קטן
        if (len2 == max && len1 == min && len3 != min && len3 != max){
            pos2.setNext(chain3);
            pos3.setNext(chain1);
            return chain2;
        }
        //אם 2 גדול 1 בינוני 3 קטן
        if (len2 == max && len1 != min && len1 != max && len3 == min){
            pos2.setNext(chain1);
            pos1.setNext(chain3);
            return chain2;
        }
        //אם 3 גדול 2 בינוני 1 קטן
        if (len3 == max && len1 == min && len2 != min && len2 != max){
            pos3.setNext(chain2);
            pos2.setNext(chain1);
            return chain3;
        }
        //אם 3 גדול 1 בינוני 2 קטן
        if (len3 == max && len1 != min && len1 != max && len2 == min){
            pos3.setNext(chain1);
            pos1.setNext(chain2);
            return chain3;
        }
        return null;
    }

    //--------------------------------------------------------------------------------------------------------------//
    //פונקצייה עוטפת-מיון רקורסיבי זוגיים-אי זוגיים שרשרת חוליות חד כיוונית
    public static void reEvenOdd(Node<Integer> chain) {
        if (chain == null) {return;}//אם הרשימה ריקה החזר
        Node<Integer> even = null;//פוינטר ראש שרשרת זוגיים
        Node<Integer> le = null;//פויינטר זנב שרשרת אי זוגיים
        Node<Integer> odd = null;//פויינטר ראש שרשרת אי זוגיים
        Node<Integer> lo = null;//פויינטר זנב שרשרת אי זוגיים
        Node<Integer> pos = chain;//פויינטר עזר למעבר על שרשרת מקור
        //קריאה לפונקצייה פרטית שתיצור שרשרת איחוד
        // כך שזוגיים תחילה ולאחריהן חוליות אי זוגיות
        //ואז תעדכן שרשרת מקור בתוכן השרשרת איחוד
        reEvenOdd(chain, pos, even, le, odd, lo);
    }
    private static void reEvenOdd(Node<Integer> chain, Node<Integer> pos, Node<Integer> even, Node<Integer> le, Node<Integer> odd, Node<Integer> lo) {
        //מיקרה בסיס אם הגענו לסוף שרשרת המקור(ניסרקה כולה)
        if (pos == null) {
            if (even != null && odd != null) {
                le.setNext(odd);
                chain.setValue(even.getValue());
                chain.setNext(even.getNext());
            } else if (even == null && odd != null) {
                chain.setValue(odd.getValue());
                chain.setNext(odd.getNext());
            }
        }

        //כל עוד לא הגענו לסוף השרשרת הנוכחית המשך בבניית השרשראות(שרשרת זוגיים && שרשרת אי זוגיים)
        if (pos != null) {
            int x = pos.getValue();//ערך חוליה נוכחית
            Node<Integer> newey = new Node<>(x);//יצירת חוליה להוספה
            if (x % 2 == 0) {//אם הערך זוגי נוסיף אותו לשרשרת הזוגיים
                if (even == null) {//מיקרה ראשון אם שרשרת הזוגיים ריקה
                    even = newey;
                    le = even;
                } else {//מיקרה שני אם היא לא ריקה,נחבר אותה לסוף שרשרת הזוגיים
                    le.setNext(newey);
                    le = le.getNext();
                }
            } else {//אם הערך אי זוגי נוסיף אותו לשרשרת האי זוגיים
                if (odd == null) {//מיקרה ראשון אם שרשרת האי זוגיים ריקה
                    odd = newey;
                    lo = odd;
                } else {//מיקרה שני אם היא לא ריקה,נחבר אותה לסוף שרשרת האי זוגיים
                    lo.setNext(newey);
                    lo = lo.getNext();
                }
            }
            reEvenOdd(chain, pos.getNext(), even, le, odd, lo);//קריאה רקורסיבית עם כל ההפניות הנדרשות
        }
    }

    //מיון בועות זוגיים-אי זוגיים שרשרת חוליות חד כיוונית
    public static void bSortEvOdd(Node<Integer> chain){
        Node<Integer> pos = chain; // לולאה חיצונית - נועלת על חוליה מסוימת
        while (pos != null) {
            Node<Integer> current = chain; // לולאה פנימית - מבצעת את ההחלפות עבור החוליה שנבחרה בלולאה החיצונית
            while (current.getNext() != null) {
                if (current.getValue() % 2 == 1 && current.getNext().getValue() % 2 == 0) {
                    // החלפת הערכים בין החוליה הנוכחית לחוליה הבאה
                    int temp = current.getValue();
                    current.setValue(current.getNext().getValue());
                    current.getNext().setValue(temp);
                }
                current = current.getNext(); // התקדמות בלולאה הפנימית
            }
            pos = pos.getNext(); // התקדמות בלולאה החיצונית
        }
    }
    //-----------------------------------------------------------------------------------------------------------//
    //יצירת פלינדרום משרשרת
    //כמה פעמים מס מופיע בשרשרת
    public static int howMuchFoundInList(Node<Integer> chain, int num){
        Node<Integer> pos = chain;
        int count = 0;

        while (pos != null){
            int x = pos.getValue();
            if (x == num){
                count++;
            }
            pos = pos.getNext();
        }
        return count;
    }

    //פונקציה הבודקת אם כל המספרים מופיעים מספר זוגי של פעמים למעט מספר אחד המופיע פעם אחת בלבד
    public static int Most_exp_one_foundTwiceInList(Node<Integer> chain){
        bSortAsc(chain); //מיון השרשרת
        Node<Integer> pos = chain;
        int countOther = 0;
        int countOnlyOne = 0;
        int middle = -1;

        while (pos != null){
            int cur = pos.getValue();
            int count = 1;
            while (pos.getNext() != null && pos.getNext().getValue() == cur){
                count++;
                pos = pos.getNext();
            }
            if (count % 2 == 0){
                countOther++;
            } else if (count == 1) {
                middle = cur;
                countOnlyOne++;
            } else if (count % 2 == 1){
                return -1;
            }
            pos = pos.getNext();
        }

        if ((countOnlyOne == 1) && ((countOther * 2) == ((countNodes(chain)- 1)))) {
            return middle;
        }else {
            return -1;
        }
    }

    //מיון שרשרת בסדר עולה
    public static void bSortAsc(Node<Integer> chain) {
        Node<Integer> pos = chain; // לולאה חיצונית - נועלת על חוליה מסוימת
        while (pos != null) {
            Node<Integer> current = chain; // לולאה פנימית - מבצעת את ההחלפות עבור החוליה שנבחרה בלולאה החיצונית
            while (current.getNext() != null) {
                if (current.getValue() > current.getNext().getValue()) {
                    // החלפת הערכים בין החוליה הנוכחית לחוליה הבאה
                    int temp = current.getValue();
                    current.setValue(current.getNext().getValue());
                    current.getNext().setValue(temp);
                }
                current = current.getNext(); // התקדמות בלולאה הפנימית
            }
            pos = pos.getNext(); // התקדמות בלולאה החיצונית
        }
    }

    //מיון שרשרת בסדר יורד
    public static void bSortDesc(Node<Integer> chain) {
        Node<Integer> pos = chain; // לולאה חיצונית - נועלת על חוליה מסוימת
        while (pos != null) {
            Node<Integer> current = chain; // לולאה פנימית - מבצעת את ההחלפות עבור החוליה שנבחרה בלולאה החיצונית
            while (current.getNext() != null) {
                if (current.getValue() < current.getNext().getValue()) {
                    // החלפת הערכים בין החוליה הנוכחית לחוליה הבאה
                    int temp = current.getValue();
                    current.setValue(current.getNext().getValue());
                    current.getNext().setValue(temp);
                }
                current = current.getNext(); // התקדמות בלולאה הפנימית
            }
            pos = pos.getNext(); // התקדמות בלולאה החיצונית
        }
    }

    //מציאת זנב שרשרת והחזרתו
    public static Node<Integer> findTail(Node<Integer> chain) {
        if (chain == null) {
            return null; // במקרה של שרשרת ריקה
        }
        Node<Integer> current = chain; // אתחול פוינטר שמתחיל בראש השרשרת
        while (current.getNext() != null) {
            current = current.getNext(); // התקדמות לחוליה הבאה
        }
        return current; // החוליה האחרונה (זנב)
    }

    //פונקציה להסרת כפילויות
    public static void compressList(Node<Integer> chain) {
        if (chain == null) {
            return;
        }

        Node<Integer> pos = chain;   // מצביע לשרשרת המקורית
        while (pos != null) {
            int current = pos.getValue();
            Node<Integer> start = pos;
            // דילוג על כל הרצף של אותו הערך
            while (pos.getNext() != null && pos.getNext().getValue() == current) {
                pos = pos.getNext();
            }
            pos = pos.getNext(); // מעבר לחוליה הבאה
            Node<Integer> end = pos;
            start.setNext(end);
        }
    }

    //פונקציה להסרת ערך משרשרת
    public static void removeValue(Node<Integer> chain, int value) {
        if (chain == null) {
            return; // במקרה של שרשרת ריקה
        }

        // טיפול במקרים שבהם הערך מופיע בראש השרשרת
        while (chain.getValue() == value) {
            chain.setValue(chain.getNext().getValue());
            chain.setNext(chain.getNext().getNext());
            if (chain.getNext() == null) {
                return; // אם הגענו לסוף השרשרת
            }
        }

        // משתנה עזר למעבר על שאר השרשרת
        Node<Integer> pos = chain;

        // מעבר על השרשרת והסרת חוליות עם הערך
        while (pos.getNext() != null) {
            if (pos.getNext().getValue() == value) {
                // דילוג על החוליה עם הערך המתאים
                pos.setNext(pos.getNext().getNext());
            } else {
                pos = pos.getNext(); // התקדמות לחוליה הבאה
            }
        }
    }

    //פונקציה עיקרית לבניית שרשרת פלינדרום
    public static void buildPal(Node<Integer> chain){
        //שמירת ערך בודד המופיע רק פעם אחת בשרשרת שהתקבלה מהמשתמש
        //והוא יהיה באמצע
        //ואם כל הערכים נמצאים פעמיים וערך אחד מופיע פעם אחת משמע שנוכל לבנות פלינדרום
        //בנוסף בשלב זה פונקציה כבר ממויינת בסדר עולה
        int who = Most_exp_one_foundTwiceInList(chain);

        //אם הוזנו ערכים תקינים
        if (who != -1){
            compressList(chain);//הסרת כפילויות משרשרת מקור
            removeValue(chain,who);//הסרת הערך שמופיע פעם אחת
            //פויינטרים עבור שרשרת העתק
            Node<Integer> temp = null;
            Node<Integer> t = null;
            //פויינטר עזר עבור שרשרת מקור
            Node<Integer> pos = chain;

            while (pos != null){//מעבר על שרשרת מקור
                int x = pos.getValue();//שמירת ערך נוכחי
                Node<Integer> newey = new Node<>(x);//יצירת חולייה מערך נוכחי והוספתו לשרשרת עזר
                    if (temp == null){
                        temp = newey;
                        t = temp;
                    }else{
                        t.setNext(newey);
                        t = t.getNext();
                    }
                pos = pos.getNext();
            }

            Node<Integer> tail = findTail(chain);
            tail.setNext(new Node<>(who));
            tail = tail.getNext();
            bSortDesc(temp);
            tail.setNext(temp);
        }else {
            System.out.println("create legal list");//אחרת נדפיס הודעה
        }
    }
    //----------------------------------------------------------------------------------------------------------------//
    //last value in stack
    private static int lstStk(Stack<Integer> stk) {
        Stack<Integer> copyStk = cloneStack(stk); // שכפול הסטאק המקורי כדי לא לשנות את הנתונים המקוריים
        Stack<Integer> temp = new Stack<>(); // סטאק זמני להעברת הנתונים מסטאק השכפול

        while (!copyStk.isEmpty()) { // לולאה להעברת כל הנתונים לסטאק הזמני
            temp.push(copyStk.pop()); // העברת ערכים מהסטאק המועתק לסטאק הזמני
        }

        int last = temp.peek(); // הערך האחרון בסטאק המקורי (ראש הסטאק הזמני)

        return last; // החזרת הערך האחרון
    }

    //size of stack
    private static int stkLen(Stack<Integer> stk) {
        int count = 0; // מונה החוליות בסטאק
        Stack<Integer> copyStk = cloneStack(stk); // שכפול הסטאק המקורי כדי לשמר את הנתונים

        while (!copyStk.isEmpty()) { // ספירה עד לריקון הסטאק המועתק
            count++; // הגדלת המונה בכל איטרציה
            copyStk.pop(); // הוצאת ערך מהסטאק
        }

        return count; // החזרת אורך הסטאק
    }

    //check if stack ordered (min-high)
    private static boolean stkOrUp(Stack<Integer> stk) {
        Stack<Integer> copyStk = cloneStack(stk); // שכפול הסטאק המקורי
        int prev = copyStk.pop(); // קבלת הערך הראשון (תחתית הסטאק)

        while (!copyStk.isEmpty()) { // בדיקה לכל הערכים בסטאק
            int curr = copyStk.pop(); // קבלת הערך הנוכחי
            if (curr < prev) { // בדיקה אם הסדר נשבר
                return false; // אם כן, החזרה שקר
            }
            prev = curr; // עדכון הערך הקודם
        }

        return true; // אם הלולאה הסתיימה בלי החזרת שקר, הסדר נכון
    }

    //check if queue of stack order up (min length - high length)
    private static boolean QstkOrUp(Queue<Stack<Integer>> q) {
        Queue<Stack<Integer>> temp = new LinkedList<>(); // תור זמני לשימור הנתונים המקוריים
        Stack<Integer> tp = q.poll(); // הוצאת הסטאק הראשון מהתור
        int prev = stkLen(tp); // אורך הסטאק הראשון
        temp.offer(tp); // החזרת הסטאק הראשון לתור הזמני

        while (!q.isEmpty()) { // מעבר על כל הסטאקים בתור
            Stack<Integer> tc = q.poll(); // הוצאת הסטאק הנוכחי
            int curr = stkLen(tc); // אורך הסטאק הנוכחי
            temp.offer(tc); // החזרת הסטאק לתור הזמני
            if (curr < prev) { // בדיקת סדר האורכים
                return false; // אם הסדר נשבר, החזרת שקר
            }
            prev = curr; // עדכון האורך הקודם
        }

        while (!temp.isEmpty()) { // שיחזור התור המקורי
            q.offer(temp.poll()); // החזרת כל הסטאקים לתור המקורי
        }

        return true; // אם הלולאה הסתיימה בלי שקר, התור מסודר
    }

    //check if queue of stack is perfect
    public static boolean PerfectQs(Queue<Stack<Integer>> q) {
        Queue<Stack<Integer>> temp = new LinkedList<>(); // תור זמני לשמירת הסטאקים המקוריים

        if (QstkOrUp(q)) { // בדיקה אם כל הסטאקים בתור מסודרים בסדר עולה מבחינת אורכם

            Stack<Integer> tp = q.poll(); // הוצאת הסטאק הראשון מהתור
            int lastPrev = lstStk(tp); // הערך האחרון בסטאק הראשון
            temp.offer(tp); // החזרת הסטאק הראשון לתור הזמני

            if (!stkOrUp(tp)) { // בדיקה אם הסטאק הראשון מסודר בסדר עולה
                return false; // אם לא, החזרת שקר
            }

            while (!q.isEmpty()) { // מעבר על שאר הסטאקים בתור
                Stack<Integer> tc = q.poll(); // הוצאת הסטאק הנוכחי
                if (!stkOrUp(tc)) { // בדיקה אם הסטאק מסודר בסדר עולה
                    return false; // אם לא, החזרת שקר
                }
                int lastCurr = lstStk(tc); // הערך האחרון בסטאק הנוכחי
                temp.offer(tc); // החזרת הסטאק לתור הזמני

                if (tc != null && tc.peek() != lastPrev) { // בדיקה אם הערך העליון של הסטאק שונה מהערך האחרון בסטאק הקודם
                    return false; // אם כן, החזרת שקר
                }

                lastPrev = lastCurr; // עדכון הערך האחרון הקודם
            }

            return true; // אם כל התנאים מתקיימים, התור מושלם
        } else {
            return false; // אם התנאי הראשון לא מתקיים, החזרת שקר
        }
    }
    //----------------------------------------------------------------------------------------------------------------//
    public static Node<Integer> bubbleLenS(Node<Integer> chain1, Node<Integer> chain2, Node<Integer> chain3){
        //check for exceptines
        if (chain1 == null || chain2 == null || chain3 == null){
            throw new IllegalArgumentException("chains can't be null.");
        }
        
        Node<Integer> big = null;//pointer for longest list
        Node<Integer> mid = null;//poinder for meddium list
        Node<Integer> sml = null;//pointer for shortest list

        Node<Integer> [] lrr = new Node[3];//create array of linked lists
        lrr[0] = chain1;//push first chain to it
        lrr[1] = chain2;//push sec chain to it
        lrr[2] = chain3;//push third chain to it

        //prform bubble sort - (short - long)
        for (int i = 0; i < lrr.length - 1; i++) {
            for (int j = 0; j < lrr.length - i - 1; j++) {
                //perform swapp
                if (countNodes(lrr[j]) > countNodes(lrr[j+1])){
                    Node<Integer> temp = lrr[j];
                    lrr[j] = lrr[j+1];
                    lrr[j+1] = temp;
                }
            }
        }
         
        //connect pointers for chains
        big = lrr[2];//connect it to the longest one
        mid = lrr[1];//connect it to the meddium one
        sml = lrr[0];//connect it to the shortest one

        //find tails of each list, so we can be able to connect them
        Node<Integer> tail1 = findTail(big);//tail of the biggest list
        Node<Integer> tail2 = findTail(mid);//tail of the meddium list
        tail1.setNext(mid);//create connection
        tail2.setNext(sml);//same here

        return big;//returne new united list
    }

    public static Node<RangeNode> minMaxList(Node<Integer> chain){
        // אם הרשימה ריקה או חוליה אחת בלבד
        if (chain == null || chain.getNext() == null) {return null;}

        Node<RangeNode> start = null;// ראש הרשימה החדשה
        Node<RangeNode> tail = null;// סוף הרשימה החדשה
        Node<Integer> pos = chain;// מצביע לרשימה המקורית

        while (pos != null && pos.getNext() != null) {
            int curr = pos.getValue();
            int nex = pos.getNext().getValue();

            if (curr < nex) {// התחלת טווח עולה
                int min = curr;
                while (pos.getNext() != null && pos.getValue() < pos.getNext().getValue()) {
                    pos = pos.getNext();
                }
                // סיום הטווח
                int max = pos.getValue();
                //-//
                // יצירת RangeNode
                RangeNode temp = new RangeNode(min, max);
                Node<RangeNode> newey = new Node<>(temp);
                if (start == null) {// עדכון ראש הרשימה
                    start = newey;
                    tail = start;
                } else {// הוספה לסוף הרשימה
                    tail.setNext(newey);
                    tail = tail.getNext();
                }
            } else if (curr == nex) { // טיפול באיברים זהים
                int min = curr;
                int max = min;// min ו-max שווים במקרה זה
                RangeNode temp = new RangeNode(min, max);
                Node<RangeNode> newey = new Node<>(temp);
                if (start == null) {// עדכון ראש הרשימה
                    start = newey;
                    tail = start;
                } else {// הוספה לסוף הרשימה
                    tail.setNext(newey);
                    tail = tail.getNext();
                }
            }
            pos = pos.getNext();// קידום מצביע
        }

        // טיפול באיבר האחרון ברשימה אם הוא לא נכלל
        if (pos != null) {
            int last = pos.getValue();
            RangeNode range = new RangeNode(last, last); // יצירת טווח עם ערך יחיד
            Node<RangeNode> newNode = new Node<>(range);

            if (start == null) { // אם הרשימה החדשה עדיין ריקה
                start = newNode;
                tail = start;
            } else {
                tail.setNext(newNode); // הוספה לסוף הרשימה
                tail = tail.getNext();
            }
        }
        return start;
    }

    //func to check how many times num found in queue
    public static int howInQueue(Queue<Integer> q, int num){
        Queue<Integer> copyQ = cloneQueue(q);//תור העתק מקור
        int count = 0;//מונה מס מופעים של ערך מסויים

        //ריקון תור העתק מקור
        while (!copyQ.isEmpty()){
            int x = copyQ.poll();//שליפת ערך נוכחי מתור
            if (x == num){//אם הערך הנוכחי שווה למספר המבוקש,נקדם את המונה ב1
                count++;//קידום מונה מופעים
            }
        }

        return count;//החזרת המונה
    }

    //func to check if num found in queue
    public static boolean contain(Queue<Integer> q, int num){
        Queue<Integer> copyQ = cloneQueue(q);//תור העתק מקור

        //ריקון תור העתק מקור
        while (!copyQ.isEmpty()){
            int x = copyQ.poll();//שליפת ערך נוכחי
            if (x == num){//אם המס נמצא נחזיר אמת
                return true;
            }
        }

        return false;//אם הגענו לכאן משמע שהמס לא נמצא ולכן נחזיר שקר
    }

    //func to return queue that contains numbers that found twice in original queue
    public static Queue<Integer> doublevals(Queue<Integer> q){
        Queue<Integer> copyQ = cloneQueue(q);//תור העתק מקור
        Queue<Integer> temp = new LinkedList<>();//תור להחזרה

        //ריקון תור העתק מקור
        while (!copyQ.isEmpty()){
            int x = copyQ.poll();//שליפת ערך נוכחי
            //אם המס נמצא בדיוק פעמיים בתור מקור נכניס אותו לתור המוחזר
            //contain-נועדה לכך שבתור המוחזר המס הכפולים יופיעו פעם אחת בלבד במקום פעמיים
            if (howInQueue(q,x) == 2 && !contain(copyQ,x)){
                temp.offer(x);//הכנסה לתור המוחזר
            }
        }

        return temp;//החזרת התור החדש שנוצר
    }

    //func to remove once value from list
    public static void remo(Node<Integer> chain, int num){
        if (chain.getValue() == num){
            chain.setValue(chain.getNext().getValue());
            chain.setNext(chain.getNext().getNext());
            return;
        }

        Node<Integer> pos = chain;

        while (pos.getNext() != null){
            int x = pos.getNext().getValue();
            if (x == num){
                pos.setNext(pos.getNext().getNext());
                return;
            }
            pos = pos.getNext();
        }
    }

    public static boolean twoSum(Queue<Integer>q,int x){
        Queue<Integer> temp = new LinkedList<>();//תור עזר לשיחזור מתמשך

        while (!q.isEmpty()){
            int curr = q.poll();//שליפת ערך נוכחי

            //שליפת כל הערכים ובדיקה אם המס הראשון שנשלף
            // תוצאת הסכום שלו עם מספר אחר כלשהו בתור שווה ל איקס
            while (!q.isEmpty()){
                int nex = q.poll();
                if (curr + nex == x){
                    return true;
                }
                temp.offer(nex);
            }

            //שיחזור
            while (!temp.isEmpty()){
                q.offer(temp.poll());
            }
        }
        return false;
    }

    public static int smlKbig(Queue<Integer>q,int num){
        Queue<Integer> big = new LinkedList<>();//תור גדולים
        Queue<Integer> small = new LinkedList<>();//תור קטנים
        int count = 0;//מונה עד המס
        int index = -1;//שומר מיקום(מונה)שיוחזר בסוף התהליך

        //ריקון תור המקור לתור קטנים מנאם ולתור גדולים מנאם בהתאמה
        while (!q.isEmpty()){
            int x = q.poll();//שליפת ערך נוכחי
            count++;//קידום מונה
            if (x == num){
                index = count;
            } else if (x < num) {
                small.offer(x);
            } else if (x > num) {
                big.offer(x);
            }
        }

        //ריקון תוכן תור קטנים לתור מקור
        while (!small.isEmpty()){
            q.offer(small.poll());
        }

        q.offer(num);//הכנסת המס נאם

        //ריקון תוכן תור גדולים לתור מקור
        while (!big.isEmpty()){
            q.offer(big.poll());
        }

        return index;//החזרת המיקום של המס נאם שנוסף לתור מחדש
    }

    //תור להזזת קיי איברים מסוף התור קדימה
    public static void befKaf(Queue<Integer> q, int k){
        int length = q.size()-k;//חישוב כמות החוליות שיש להעביר לסוף התור

        for (int i = 0; i < length; i++) {
            q.offer(q.remove());//הוצאה והכנסה לאותו תור בו זמנית
        }
    }

    public static void sortStudentGlists(GradesFile gd){
        Node<StudentG> [] arr = new Node[gd.getGrades().length];

        for (int i = 0; i < gd.getGrades().length; i++) {
            Node<StudentG> pos = gd.getGrades()[i];

            while (pos != null){
                int code = pos.getValue().getCode();//קבלת ספרות אמצעיות במזהה הסטודנט
                Node<StudentG> toAdd = new Node<>(pos.getValue());//יצירת חולייה חדשה להוספה

                if (arr[code] == null){//אם האוסף ריק בתא במערך החדש,נפנה תא זה לחוליית סטודנט החדשה
                    arr[code] = toAdd;
                }else {//אם האוסף אינו ריק בתא הנוכחי
                    toAdd.setNext(arr[code]);// מחבר את הסטודנט הנוכחי לראש הרשימה
                    arr[code] = toAdd;// מעדכן את הראש החדש
                }

                pos = pos.getNext();//התקדמות לחולייה הבאה
            }
        }
        gd.setGrades(arr);//שינוי מערך המחלקה שיכיל נתוני מערך זה
    }

    public static int High(BinNode<Integer> root){
        int count = -1;
        int max = Integer.MIN_VALUE;
        return High(root,count,max);
    }

    private static int High(BinNode<Integer> root, int count,int maxCount){
        if (root == null) {return maxCount;}
        
        count++; // העלאת הרמה הנוכחית

        if (count > maxCount) {maxCount = count;}// עדכון הרמה הגבוהה ביותר שנמצאה

        maxCount = High(root.getLeft(), count, maxCount);
        maxCount = High(root.getRight(), count, maxCount);

        return maxCount;
    }

    //2021 מועד ב
    //1-א
    public static Queue<Integer> tiln(int n){
        Queue<Integer> temp = new LinkedList<>();
        for (int i = 1; i <= n; i++) {
            int j = 0;
            while (j < i){
                temp.offer(i);
                j++;
            }
        }
        return temp;
    }
    //1-ב
    public static boolean istiln(Queue<Integer> q, int n){
        Queue<Integer> temp = tiln(n);
        Queue<Integer> clone = cloneQueue(q);

        while (!clone.isEmpty() && !temp.isEmpty()){
            int cloneVal = clone.poll();
            int tempVal = temp.poll();
            if (cloneVal != tempVal){
                return false;
            }
        }
        return true;
    }
    //2-א
    public static boolean sumli(Node<Integer>chain){
        int sum = 0;
        Node<Integer> pos = chain;
        sum = pos.getValue();
        pos = pos.getNext();

        while (pos != null){
            int x = pos.getValue();
            if (x != sum){
                return false;
            }
            sum += x;
            pos = pos.getNext();
        }
        return true;
    }
    //2-ב
    public static boolean revsumli(Node<Integer> chain){
        Node<Integer> pos = chain;

        while (pos.getNext() != null){
            int x = pos.getValue();
            Node<Integer> others = pos.getNext();
            int sum = 0;

            while (others != null){
                sum += others.getValue();
                others = others.getNext();
            }

            if (sum != x){return false;}

            pos = pos.getNext();
        }
        return true;
    }
    //2022 אביב
    //1-א
    public static void first(Node<Integer>chain){
        Node<Integer> pos = chain;

        while (pos.getNext() != null){
            int x = pos.getValue();

            Node<Integer> toAdd = new Node<>(x);
            toAdd.setNext(pos.getNext());
            pos.setNext(toAdd);

            pos = pos.getNext().getNext();
        }
        int last = pos.getValue();
        pos.setNext(new Node<>(last));
    }
    //1-ב
    public static void second(Node<Integer> chain){
        Node<Integer> newey = null;
        Node<Integer> tail = null;
        Node<Integer> pos = chain;
        Node<Integer> prev = null;

        while (pos != null){
            Node<Integer> toAdd = new Node<>(pos.getValue());
            if (newey == null){
                newey = toAdd;
                tail = newey;
            }else {
                tail.setNext(toAdd);
                tail = tail.getNext();
            }
            prev = pos;
            pos = pos.getNext();
        }

        prev.setNext(newey);
    }
    //2-א
    public static int avgInStk(Stack<Integer>stk){
        Stack<Integer>clone = cloneStack(stk);
        int sum = 0;
        int count = 0;
        int avg = 0;

        while (!clone.isEmpty()){
            count++;
            sum += clone.pop();
        }
        avg = sum / count;

        return avg;
    }

    public static void sortStkAvg(Stack<Integer>stk){
        Stack<Integer> lower = new Stack<>();
        Stack<Integer> bigger = new Stack<>();
        int avgNum = avgInStk(stk);

        while (!stk.isEmpty()){
            if (stk.peek() > avgNum){
                bigger.push(stk.peek());
            }else {
                lower.push(stk.peek());
            }
            stk.pop();
        }

        while (!lower.isEmpty()){
            stk.push(lower.pop());
        }

        stk.push(avgNum);

        while (!bigger.isEmpty()){
            stk.push(bigger.pop());
        }
    }

    public static Stack<Integer> sumss(Stack<Integer> nums){
        Stack<Integer> sums = new Stack<>();//create new stack to return
        Stack<Integer> cloneNums = cloneStack(nums);//copy original stack
        Stack<Integer> cnum = new Stack<>();

        while (!cloneNums.isEmpty()){
            cnum.push(cloneNums.pop());
        }

        int sum = 0;//sum values from copy stacks

        while (!cnum.isEmpty()){
            sum += cnum.pop();//sum up current value
            sums.push(sum);//push it to the new stack
        }

        return sums;//return the new stack
    }

    public static boolean goodDoublyStk(Stack<Integer> nums, Stack<Integer> sums){
        Stack<Integer> cloneNums = cloneStack(nums);//copy original of nums stack
        Stack<Integer> cloneSums = cloneStack(sums);//copy original of sums stack

        //move copies of stacks for new two stucks
        Stack<Integer> num = new Stack<>();
        Stack<Integer> sumy = new Stack<>();

        while (!cloneNums.isEmpty()){
            num.push(cloneNums.pop());
        }

        while (!cloneSums.isEmpty()){
            sumy.push(cloneSums.pop());
        }

        int sum = 0;//for summing up values from copy of nums stacks

        while (!num.isEmpty() && !sumy.isEmpty()){
            sum += num.pop();//sum up current value from nums stack
            //if value from sums stack diffrent from -> the sum of values(till now) of nums stack,return false
            if (sumy.pop() != sum){
                return false;
            }
        }

        return true;//else stacks are perfect
    }

    public static int lengthN(Node<Integer> chain){
        int count = 0;
        Node<Integer> pos = chain;

        while (pos != null){
            count++;
            pos = pos.getNext();
        }

        return count;
    }

    public static int getLastNval(Node<Integer> chain){
        int last = 0;
        Node<Integer> pos = chain;

        while (pos.getNext() != null){
            pos = pos.getNext();
        }

        return pos.getValue();
    }

    public static int minNval(Node<Integer> chain){
        int min = Integer.MAX_VALUE;
        Node<Integer> pos = chain;

        while (pos != null){
            if (pos.getValue() < min){
                min = pos.getValue();
            }
            pos = pos.getNext();
        }

        return min;
    }

    public static boolean isMirc(Node<Integer> chain){
        int len = lengthN(chain);//size of chain
        int last = getLastNval(chain);//last value in list
        int min = minNval(chain);//minimum value in list
        Node<Integer> pos = chain;//assist pointer for the list

        //if first val of list not equals to the last value of list return false
        if (pos.getValue() != last){return false;}

        if (len % 2 != 1){return false;}//if length of list is even, return false

        int count = 0;//counter to assure we reach the middle of a list

        //iterate through the chain till we reach the middle
        while (pos != null && count < len/2){
            pos = pos.getNext();//get to the next node
            count++;//count + 1
        }

        //if middle node value isn't the minimmum value in the list, return false
        if (pos.getValue() != min){return false;}

        return true;//return true this chains follows all rules
    }

    public static boolean isFibonachiList(Node<Integer> chain){
        if (lengthN(chain) < 3){return false;}//if length of chain lower than 3 return false
        
        Node<Integer> pos = chain;//pointer to iterate through the linkedlist
        int [] arr = new int[lengthN(chain)];//create new array
        int index = 0;//index for the array

        //mv all list content to the new array
        while (pos != null){
            arr[index++] = pos.getValue();//mv current val
            pos = pos.getNext();//get to the next node
        }

        //iterate through the array to see if its obay fibonachi rules
        for (int i = 0; i < arr.length - 2; i++){
            int sum = 0;//sum
            sum += arr[i];
            sum += arr[i+1];
            if (arr[i+2] != sum){
                return false;//not fibo arr
            }
        }

        return true;//its folowing fibonachi rules
    }

    public static Node<Integer> buildFibonachiList(int num){
        int [] arr = new int[num];//create array in size of num
        arr[0] = 1;//put 1 in first array index
        arr[1] = 1;//put 1 in sec array index

        //build array following fibonachi rules
        for (int i = 2; i < arr.length; i++) {
            arr[i] = arr[i-1] + arr[i - 2];
        }

        //new pointers for the fibonachi list i gonna build
        Node<Integer> newey = null;//pointer for head of the new list
        Node<Integer> tail = null;//pointer for the new tail of the list

        //build the new list
        for (int i = 0; i < arr.length; i++) {
            Node<Integer> toAdd = new Node<>(arr[i]);//create new node that contains cuurent array index value

            if (newey == null){//case 1 if chain is empty
                newey = toAdd;//connect the head pointer to the new node
                tail = newey;//connect tail pointer to it
            }else {//case 2 if it's not
                tail.setNext(toAdd);//connect the tail to new node
                tail = tail.getNext();//mv the tail pointer to the new node that have been added
            }
        }

        return newey;//return fibonachi chain
    }

    public static void removeMiddle(Node<Integer> chain){
        Node<Integer> pos = chain;//pointer fr the list
        int len = lengthN(chain);//length of list

        if(len % 2 == 1){//if length is odd,remove middle node

            //if length == 1, put original chain created in main to be null
            if(len == 1){
                chain.setNext(chain.getNext().getNext());
                return;//get out of the function
            }

            int count = 0;//counter till reaching the  middle of list

            while (pos != null && count < (len / 2) - 1){
                pos = pos.getNext();
                count++;
            }

            pos.setNext(pos.getNext().getNext());

        }else {//else if length is even remove two middle nodes

            if (len == 2){
                chain.setNext(chain.getNext().getNext().getNext());
                return;
            }

            int count = 0;
            while (pos != null && count < (len / 2) - 2){
                pos = pos.getNext();
                count++;
            }

            pos.setNext(pos.getNext().getNext().getNext());
        }
    }


    public static void change(Queue<Integer>q, int x, int y){
        Random random = new Random();//create new instance of random class
        int size = q.size();//size of queue
        Queue<Integer> temp = new LinkedList<>();//create new queue for restoration

        if (size % 2 == 1){//if size of queue is odd
            int num = random.nextInt(Math.abs(x-y)+1)+x;//randomize num between x and y
            int count = 0;//counter for polling

            if (size == 1){//if size of queue is 1 insert num, return
                q.poll();
                q.offer(num);
                return;
            }

            //while we didnt reach the middle of the queue , count + 1, offer values to the restoration queue
            while (!q.isEmpty() && count < size / 2){
                count++;
                temp.offer(q.remove());
            }

            q.poll();//poll out middle number
            temp.offer(num);//offer num in the middle of the assist queue

            //keep polling values and offer them to the new assist queue
            while (!q.isEmpty()){
                temp.offer(q.poll());
            }

            //restore original queue
            while (!temp.isEmpty()){
                q.offer(temp.poll());
            }
        }else{//if size of queue is even

            if (size == 2){//if size of queue is 2,poll out two nodes, and offer x and y to it,return
                q.poll();
                q.poll();
                q.offer(x);
                q.offer(y);
                return;
            }

            int count = 0;//counter for polling
            //while we didnt reach the middle of the queue , count + 1, offer values to the restoration queue
            while (!q.isEmpty() && count < size / 2 - 1){
                count++;
                temp.offer(q.poll());
            }

            //poll out middle numbers
            q.poll();
            q.poll();

            //offer x and y to it
            temp.offer(x);
            temp.offer(y);

            //keep polling values and offer them to the new assist queue
            while (!q.isEmpty()){
                temp.offer(q.poll());
            }

            //restore original queue
            while (!temp.isEmpty()){
                q.offer(temp.poll());
            }
        }
    }

    //func that recive two numbers, and return new down sorted list
    public static Node<Integer> buildDownList(int num, int a, int b){
        //two new pointers for the new "down" list
        Node<Integer> newey = null;
        Node<Integer> tail = null;
        //build array, that its size is num
        int [] arr = new int[num];
        arr[0] = a;//put first number in first index in the array
        arr[1] = b;//put first number in first index in the array

        //build "down array"
        for (int i = 2; i < arr.length; i++) {
            arr[i] = Math.abs(arr[i - 1] - arr[i - 2]);
        }

        //build "down list" from "down array"
        for (int i = 0; i < arr.length; i++) {
            Node<Integer> toAdd = new Node<>(arr[i]);
            if (newey == null){
                newey = toAdd;
                tail = newey;
            }else{
                tail.setNext(toAdd);
                tail = tail.getNext();
            }
        }

        //return the new list we've created
        return newey;
    }

    //פונקציה המקבלת תור ומיקום ומחזירה ערך במיקום זה
    public static int valueAt(Queue<Integer> q, int pos){
        Queue<Integer> temp = new LinkedList<>();//תור עזר
        int count = 0;//מונה
        int num = 0;//משתנה לשמירת המס במיקום שהתקבל בפונק כפרמטר

        while (!q.isEmpty()){//מעבר על התור
            int x = q.poll();//שליפת ערך מתור זה
            count++;//קידום מונה

            if (count == pos){//אם המונה שווה למיקום שהוזן
                num = x;//נשמור מס במיקום זה
            }

            temp.offer(x);//נמשיך בהעברת תוכן תור זה לתור עזר שבעזרתו בסוף נשחזר תור מקור
        }

        //שיחזור תור עזר
        while (!temp.isEmpty()){
            q.offer(temp.poll());
        }
        //אם משתנה המס אכן מכיל ערך מספרי השונה מ0 נחזיר את המס, אחרת  אם מיקום לא קיים נחזיר -1
        return num != 0? num : -1;
    }

    //פונק הבונה תור חדש משני תורים משלבת בין ערכי התורים
    public static Queue<Integer> merge(Queue<Integer> q1, Queue<Integer> q2){
        Queue<Integer> q3 = new LinkedList<>();//תור מוחזר המשלב ערכים משני התורים
        int i = 0;//משתנה "העוזר" למצוא מס במיקום מסויים מהתור השני
        int size = q1.size();//אורך תור ראשון

        while (!q1.isEmpty()){
            int first = q1.poll();//שליפת ערך מתור
            int last = valueAt(q2,size - i);//שליפת ערך "מסוף" תור 2
            q3.offer(first);//הכנסת ערך מתור 1
            q3.offer(last);//הכנסת ערך מתור 2
            i++;//קידום אינדקס
        }

        return q3;//החזרת התור המבוקש
    }

    //-----everthing to Array-----------//
    public static <T> Object[] stackToArray(Stack<T> stk){
        Stack<T> temp = new Stack<>();//create new stack assist for restoration
        Object [] arr = new Object[stk.size()];//create new array
        int index = 0;//index for array

        //push all values in original stak to the assist stack
        while (!stk.isEmpty()){
            temp.push(stk.pop());
        }

        //restore original stack and build the new arraylist
        while (!temp.isEmpty()){
            stk.push(temp.peek());
            arr[index++] = temp.peek();
            temp.pop();
        }

        return arr;//return the new array we've created
    }

    public static <T> Object[] queueToArray(Queue<T> q){
        Queue<T> temp = new LinkedList<>();//create new queue assist for restoration
        int len = q.size();//size of queue
        Object [] arr = new Object[len];//create new array
        int index = 0;

        //push all values in original queue to the assist queue
        while (!q.isEmpty()){
            T x = q.poll();//poll the value from the queue
            temp.offer(x);//offer it to the queue assist
            arr[index++] = x;//put it in the array
        }

        return arr;//return the new array
    }

    public static <T> Object[] nodeToArray(Node<T> chain){
        int len = countNodes((Node<Integer>) chain);//calculate the length of chain
        Object [] arr = new Object[len];//create new objects array
        int index = 0;//index for the array
        Node<T> pos = chain;//pointer for the head of the list

        while (pos != null){//while the pointer not pointing to null, get to the next node
            arr[index++] = pos.getValue();//build the array
            pos = pos.getNext();//get to the next pointer
        }
        return arr;//return the new array
    }

    private static <T> int countBinnodes(BinNode<T> chain){
        int count = 0;//counter that will contain the length of the binnode
        BinNode<T> pos  = chain;//pointer for the chain

        while (pos.hasLeft()){//mv the pointer to the far lrft side of the chain
            pos = pos.getLeft();
        }

        while (pos != null){//iterate over the list, whilr the pointer not pointing to null
            count++;//count + 1
            pos = pos.getRight();// mv to the next right binnode
        }

        return count;//return the length of the chain
    }

    public static <T> Object[] binnodeToArray(BinNode<T> chain){
        int len = countBinnodes(chain);//size of the chain
        Object [] arr = new Object[len];//create new array that will contains all the values in the chain
        int index = 0;//index for the new array
        BinNode<T> pos = chain;//pointer for the binnode list

        //mv the pointer to the far left side of the list
        while (pos.getLeft() != null){
            pos = pos.getLeft();
        }

        while (pos != null){//while the pointer not pointing to null
            arr[index++] = pos.getValue();//build the new array
            pos = pos.getRight();//get to the next right binnode
        }

        return arr;//return the new array
    }

    private static <T> int countBinNodes(BinNode<T> root) {
        if (root == null) {
            return 0;
        }

        // חישוב גודל תת-העץ השמאלי ותת-העץ הימני בסדר חיפוש תחילי
        int leftCount = countBinNodes(root.getLeft());
        int rightCount = countBinNodes(root.getRight());

        return 1 + leftCount + rightCount; // סופרים את הצומת הנוכחי + הצאצאים
    }

    public static <T> Object[] BfsArray(BinNode<T> root){
        int len = countBinnodes(root);//length of the tree
        Object [] arr = new Object[len];//build new array
        int index = 0;//index for the array
        Queue<BinNode<T>> q = new LinkedList<>();//new queue assist for breadth first search
        q.offer(root);//offer the root of the tree

        while(!q.isEmpty()){
            BinNode<T> current = q.poll();//poll the current binnode value of the tree
            arr[index++] = current;//build the array

            //offer left son to the queue
            if (current.hasLeft()){
                q.offer(current.getLeft());
            }

            //offer the right son to the queue
            if (current.hasRight()){
                q.offer(current.getRight());
            }
        }

        return arr;// return the new bfs array
    }
    //----------end----------------------//

    //---------------------------------------------------------------------------------------//
    //1-a
    //private func to check if all values int stack are positive
    private static boolean allPosS(Stack<Integer> stk){
        Stack<Integer> clone = cloneStack(stk);//copy original stack
        //iterate through the stack
        while (!clone.isEmpty()){
            //if negative number found return false
            if (clone.pop() < 0){
                return false;
            }
        }
        //return true all values in stack are positive
        return true;
    }

    //private func to check if the number of even num equals to the number of od nums
    //and if sum of even nums equals to sum of odd nums
    private static boolean countAndSumEquals(Stack<Integer> stk){
        int Ecount = 0;//counter for even nums
        int Ocount = 0;//counter for odd nums
        int Esum = 0;//sum for even nums
        int Osum = 0;//sum for odd nums
        Stack<Integer> clone = cloneStack(stk);//copy stack

        //iterate through the stack
        while (!clone.isEmpty()){
            int x = clone.pop();//pop out value from the stack

            if (x % 2 == 0){//if value is even
                Ecount++;//count it
                Esum += x;//sum it up
            } else if (x % 2 == 1) {//same
                Ocount++;
                Osum += x;
            }
        }
        //return true if the number of even num equals to the number of od nums
        //and if sum of even nums equals to sum of odd nums
        return Esum == Osum && Ecount == Ocount;
    }

    //main func to check if stack is "Shivyon"
    public static boolean isParityStack(Stack<Integer> st){
        if (!allPosS(st)){return false;}
        if (!countAndSumEquals(st)){return false;}
        return true;
    }
    //1-b O(n)

    //2//
    public static boolean inQ(Queue<Integer>q, int x){
        Queue<Integer> temp = new LinkedList<>();//assist queue for restoration
        boolean found = false;//flag if number founded or not

        while (!q.isEmpty()){//iterate through the queue
            int t = q.poll();//poll value from it
            if (t == x){//if num found, change flag to true
                found = true;
            }
            temp.offer(t);//offer the value to the assist queue
        }

        //restoration
        while (!temp.isEmpty()){
            q.offer(temp.poll());
        }

        return found;//return flag -> found or not
    }

    public static int distanceQ(Queue<Integer> q, int x, int y){
        //if x not in queue or y not in queue,-> -1
        if (!inQ(q, x) || !inQ(q, y)){
            return -1;
        }

        int count = 0;//counter for the
        Object [] arr = queueToArray(q);//array that contains all values in the queue
        int len = arr.length;//length of the array

        int i = 0;//index to iterate through the array
        //while we didnt reach x & y , mv to the next cell
        while (i < len && !arr[i].equals(x) && !arr[i].equals(y)){
            i++;
        }

        i++;//mv to the next cell

        //while we didnt reach x & y , mv to the next cell
        while (i < len && !arr[i].equals(y) && !arr[i].equals(x)){
            count++;//count the values between them
            i++;
        }

        return count;//return the number of values between them
    }

    //3//
    //דרך א
    //function to check how many times value found in list
    private static int howmi(Node<Character> chain, int x){
        Node<Character> pos = chain;//pointer for the list
        int count = 0;//counter for num of occurances of value
        //iterate through the chain
        while (pos != null){
            //if we found the number,count it
            if (pos.getValue() == x){
                count++;
            }
            pos = pos.getNext();//get to the next node
        }
        return count;//return the counter
    }

    //check if all the values in chain found twice
    public static boolean repeatList(Node<Character> chain){
        Node<Character> pos = chain;//pointer for the chain

        //iterate through the list
        while (pos != null){
            int num = pos.getValue();//get the value of current node
            //if the current node value found more than twice, -> false
            if (howmi(chain, num) != 2){
                return false;
            }
            pos = pos.getNext();//get to the next node
        }
        //-> true, all values in list found twice
        return true;
    }

    //דרך ב
    //bubble sort - char chain
    public static void bsortchain(Node<Character> chain){
        Node<Character> pos = chain;
        while (pos != null){
            Node<Character> current = chain;
            while (current.getNext() != null){
                if (current.getValue() > current.getNext().getValue()){
                    Character temp = current.getValue();
                    current.setValue(current.getNext().getValue());
                    current.getNext().setValue(temp);
                }
                current = current.getNext();
            }
            pos = pos.getNext();
        }
    }

    public static boolean repeatlist2(Node<Character>chain){
        bsortchain(chain);//sort chain
        Node<Character> pos = chain;//pointer for the chain

        while (pos.getNext() != null){
            int count = 0;//counter for num of occurances of char in list
            int first = pos.getValue();//current value
            count++;//count the first occurance
            Node<Character> others = pos.getNext();//pointer for next node

            //count the occurances of current value(char)
            while (others != null && others.getValue().equals(first)){
                //if current val found,count it
                count++;
                //if its found more than twice, -> fals
                if (count != 2){return false;}
                others = others.getNext();//get to the next node
            }

            pos = others;
        }
        return true;
    }

    //6-a
    public static void one(Queue<Integer> q, int k){
        int size = q.size();//size of queue
        int i = 0;//var assist for iteration
        while (i < size){//iterate through the queue
            int x = q.poll();//poll current value
            int j = 0;//var assist for updating the queue
            while (j < k){
                q.offer(x);
                j++;
            }
            i++;
        }
    }
    //6-b-1
    //fun that expend the queue
    private static void addqToq(Queue<Integer> q, Queue<Integer> t){
        Queue<Integer> clo = cloneQueue(t);
        while (!clo.isEmpty()){
            q.offer(clo.poll());
        }
    }
    //6-b-2
    public static void two(Queue<Integer> q, int k){
        Queue<Integer> clone = cloneQueue(q);

        //how many times to expand it
        for (int i = 1; i < k; i++) {
            addqToq(q, clone);
        }
    }
    //6-b-3 O(n)

    //7 Domino Deck

    //8 - perfect tree
    public static boolean orderdTree(BinNode<Integer> root){
        //queue for bfs
        Queue<BinNode<Integer>> q = new LinkedList<>();
        //offer the root of the tree
        q.offer(root);

        while (!q.isEmpty()){
            BinNode<Integer> current = q.poll();

            if (!current.hasLeft() && !current.hasRight()){
                if (current.getValue() % 2 == 0){
                    return false;
                }
            }else if (current.hasLeft() || current.hasRight()){
                if (current.getValue() % 2 == 1){
                    return false;
                }

                if (current.hasRight() && current.getValue() > current.getRight().getValue()){
                    return false;
                }

                if (current.hasLeft() && current.getValue() > current.getLeft().getValue()){
                    return false;
                }
            }

            if (current.hasLeft()){
                q.offer(current.getLeft());
            }

            if (current.hasRight()){
                q.offer(current.getRight());
            }
        }

        return true;
    }

    //9
    //מחסנית "פירמידה n "היא מחסנית של מספרים שלמים וחיוביים שהמספר הראשון (מספר שנימצא בראש
    //המחסנית) הוא 1 וכל מספר נוסף מופיע בו ברצף, מספר פעמים השווה לערכו, עד N
    public static Stack<Integer> Pyramid(int n){
        Stack<Integer> pyramid1 = new Stack<>();
        Stack<Integer> pyramid2 = new Stack<>();
        for (int i = 1; i <= n; i++) {
            int j = 0;
            while (j < i){
                pyramid1.push(i);
                j++;
            }
        }
        while (!pyramid1.isEmpty()){
            pyramid2.push(pyramid1.pop());
        }

        return pyramid2;
    }

    public static boolean isPyramid(Stack<Integer> stk){
        if (!stkOrUp(stk)){
            return false;
        }
        Stack<Integer> clone = cloneStack(stk);

        while (!clone.isEmpty()){
            int num = clone.pop();
            int j = 1;
            while (j < num && !clone.isEmpty()){
                if (clone.pop() != num){
                    return false;
                }
                j++;
            }
        }
        return true;
    }
    //--------------------------------------------------------------------//
    private static int[] revColMat(int[][] arr, int col){
        int [] temp = new int[arr.length];//create new array

        //build array of some culomn in the matrix
        for (int i = 0; i < arr.length; i++){
            temp[i] = arr[i][col];
        }

        //reverse the contents of the array
        for (int i = 0; i < temp.length/2; i++) {
            int t = temp[i];
            temp[i] = temp[temp.length-1-i];
            temp[temp.length-1-i] = t;
        }

        return temp;//return the new array(row)
    }

    private static int[][] rotatem(int[][] mat){
        //create new matrix for the new rotated content of the original matrix
        int [][] temp = new int[mat.length][mat[0].length];

        //iterate through the first row in the original matrix
        for (int j = 0; j < mat[0].length; j++) {
            int [] buildRows = revColMat(mat,j);//create new row of the current coulmn contents

            //fill the row of the new matrix
            for (int i = 0; i < buildRows.length; i++) {
                temp[j][i] = buildRows[i];
            }
        }

        return temp;
    }

    public static int[][] rotate(int[][] mat, int angle){
        //if the given angle is equals to 0 or 360
        if (angle == 0 || angle == 360) {
            return mat;
        } else if (angle == 90) { //if the given angle is equals to 90
            return rotatem(mat);
        } else if (angle == 180) { //if the given angle is equals to 180
            return rotatem(rotatem(mat));
        } else if (angle == 270) { //if the given angle is equals to 270
            return rotatem(rotatem(rotatem(mat)));
        } else { //if given angle is not 0,90,180,270,360 throw exception
            throw new IllegalArgumentException("Angle must be 0, 90, 180, 270, or 360");
        }
    }

    public static void printM(int[][] mat){
        for (int i = 0; i < mat.length; i++) {
            System.out.print("[");
            for (int j = 0; j < mat[0].length; j++) {
                System.out.print(mat[i][j]+",");
            }
            System.out.print("]");
            System.out.println();
        }
    }

    //@wrapper function
    public static void sortListNode(Node<Integer> chain){
        //pointers for the new even chain
        Node<Integer> even = null;
        Node<Integer> le = null;

        //pointers for the new odd chain
        Node<Integer> odd = null;
        Node<Integer> lo = null;

        //pointers for the original chain
        Node<Integer> pos = chain;

        //call the main function for sorting
        sortListNode(chain, pos, even, le, odd, lo);
    }

    //main function for sorting chain even values first, after them will come the odd values
    private static void sortListNode(Node<Integer> chain, Node<Integer> pos,Node<Integer> even, Node<Integer> le, Node<Integer> odd, Node<Integer> lo){
        //if we reach the end of the chain
        if (pos == null){
            if (even != null && odd != null){
                le.setNext(odd);
                chain.setValue(even.getValue());
                chain.setNext(even.getNext());
            } else if (even != null && odd == null) {
                chain.setValue(even.getValue());
                chain.setNext(even.getNext());
            } else if (odd != null && even == null) {
                chain.setValue(odd.getValue());
                chain.setNext(odd.getNext());
            }

        }else {//if we didnt reach the end of the chain
            //create new node to add
            Node<Integer> toAdd = new Node<>(pos.getValue());

            //if the number is even, add it to the even list
            if (pos.getValue() % 2 == 0){
                if (even == null){//if the chain is null
                    even = toAdd;
                    le = even;
                }else{//if it allready have nodes
                    le.setNext(toAdd);
                    le = le.getNext();
                }
            }else {//if the number is odd, add it to the odd list
                if (odd == null){//if the chain is null
                    odd = toAdd;
                    lo = odd;
                }else{//if it allready have nodes
                    lo.setNext(toAdd);
                    lo = lo.getNext();
                }
            }

            //recursive call
            sortListNode(chain, pos.getNext(), even, le.getNext(), odd, lo.getNext());
        }
    }


    public static String bigSender(MailItem [] arr){
        double maxPrice = Double.MIN_VALUE;
        String sender = "";

        for (int i = 0; i < arr.length; i++) {

            if (arr[i] instanceof Package){
                double price = ((Package)arr[i]).getRealPrice();

                if (price > maxPrice){
                    maxPrice = price;
                    sender = ((Package)arr[i]).getName();
                }
            }
        }
        if (sender != "") {
            return sender;
        }
        return null;
    }

    /*
                T
              /   \
             R     Q
              \   /
               F A
              /   \
             G     B
              \      \
               H      D
              /      / \
             I      L   C
            / \    /   /
           J   K  O   E
                \  \
                 N  P

     */

    public static BinNode<Integer> buildBst(int n){
        Scanner scanner = new Scanner(System.in);

        System.out.print("enter a number");
        int num = scanner.nextInt();
        BinNode<Integer> root = new BinNode<>(num);

        for (int i = 1; i < n; i++) {
            System.out.print("enter next number");
            num = scanner.nextInt();
            root = insertRec(root, num);
        }

        return root;
    }
    
    private static BinNode<Integer> insertRec(BinNode<Integer> root, int num) {
        if (root == null) {
            return new BinNode<>(num);
        }

        if (num < root.getValue()) {
            root.setLeft(insertRec(root.getLeft(), num));
        } else if (num > root.getValue()) {
            root.setRight(insertRec(root.getRight(), num));
        }
        return root;
    }

    //function to check if num is prime
    private static boolean isPrime(int num){
        for (int i = 2; i < num; i++) {
            if (num % i == 0){
                return false;
            }
        }
        return true;
    }

    //function to add binnodes to the three(left son, right son)
    private static boolean addBinNodes(BinNode<Integer> root){
        //if binnode have sons return false
        if (root.hasRight() || root.hasLeft()){
            return false;
        }

        //if its value equals to 0 return false
        if (root.getValue() == 0){
            return false;
        }

        //if current binnode value is prime number
        if (isPrime(root.getValue())){
            return false;
        }

        for (int i = 2; i < root.getValue() / 2; i++) {
            if (root.getValue() % i == 0){
                BinNode<Integer> leftySon = new BinNode<>(root.getValue() / i);
                BinNode<Integer> rightySon = new BinNode<>(i);
                root.setLeft(leftySon);
                root.setRight(rightySon);
                return true;
            }
        }
        return true;
    }

    public static BinNode<Integer> build2(int n){
        Scanner scanner = new Scanner(System.in);
        int num = 0;
        BinNode<Integer> f = null;
        BinNode<Integer> s = null;

        System.out.print("input the first number in the list");
        num = scanner.nextInt();

        BinNode<Integer> root = new BinNode<>(num);

        for (int i = 2; i <= n; n++) {
            num = scanner.nextInt();
            s = root;

            while (s != null){
                f = s;
                if (num > f.getValue()){
                    s = f.getRight();
                }else{
                    s = f.getLeft();
                }
            }

            if (num >= f.getValue()){
                f.setRight(new BinNode<>(num));
            }else{
                f.setLeft(new BinNode<>(num));
            }
        }

        return root;
    }

    private static int sumQ(Queue<Integer>q){
        int sum = 0;
        Queue<Integer> copyQ = cloneQueue(q);

        while (!copyQ.isEmpty()){
            sum += copyQ.poll();
        }
        return sum;
    }

    private static int lastInQueue(Queue<Integer>q){
        Queue<Integer> copyQ = cloneQueue(q);
        int last = 0;
        Stack<Integer> temp = new Stack<>();

        while (!copyQ.isEmpty()){
            temp.push(copyQ.poll());
        }

        last = temp.peek();

        return last;
    }

    public static Queue<Integer> whyn(Node<Queue<Integer>> chain){
        Queue<Integer> q = new LinkedList<>();

        Node<Queue<Integer>> pos = chain;

        while (pos != null){
            if (pos.getValue().peek() % 2  == 1){
                q.offer(sumQueue(pos.getValue()));

            }else{
                q.offer(lastInQueue(pos.getValue()));
            }

            pos = pos.getNext();
        }
        return q;
    }

    //--------------------------------------------------------------------------//
    private static boolean sykuPuP(Stack<Integer> stk){
        Stack<Integer> copyStk = cloneStack(stk);
        int prev = copyStk.pop();

        while (!copyStk.isEmpty()){
            int current = copyStk.pop();

            if (prev > current){
                return false;
            }

            prev = current;
        }

        return true;
    }

    private static int lstInStack(Stack<Integer> stk){
        Stack<Integer> copyStk = cloneStack(stk);
        Stack<Integer> temp = new Stack<>();

        while (!copyStk.isEmpty()){
            temp.push(copyStk.pop());
        }

        return temp.peek();
    }

    private static <T> int sizeStK(Stack<T> stk){
        Stack<T> copyStk = copyStack(stk);
        int len = 0;

        while (!copyStk.isEmpty()){
            len++;
            copyStk.pop();
        }

        return len;
    }

    private static <T> boolean Qsorted(Queue<Stack<T>> q){
        Queue<Stack<T>> temp = new LinkedList<>();
        boolean flag = false;

        Stack<T> prevStk = q.poll();
        int prevLen = sizeStK(prevStk);
        temp.offer(prevStk);

        while (!q.isEmpty()){
            Stack<T> currStk = q.poll();
            int currLen = sizeStK(prevStk);
            temp.offer(prevStk);

            if (prevLen > currLen){
                flag = true;
            }

            prevLen = currLen;
        }

        return flag ? false : true;
    }

    private static <T> Stack<T>  copyStack(Stack<T> stk){
        Stack<T> temp = new Stack<>();
        Stack<T> newey = new Stack<>();

        while (!stk.isEmpty()){
            temp.push(stk.pop());
        }

        while (!temp.isEmpty()){
            stk.push(temp.peek());
            newey.push(temp.peek());
            temp.pop();
        }

        return newey;
    }

    private static <T> Queue<T>  copyQueue(Queue<T> q){
        Queue<T> temp = new LinkedList<>();
        Queue<T> newey = new LinkedList<>();

        while (!q.isEmpty()){
            temp.offer(q.poll());
        }

        while (!temp.isEmpty()){
            q.offer(temp.peek());
            newey.offer(temp.peek());
            temp.poll();
        }

        return newey;
    }

    private static boolean isPerfixQstk(Queue<Stack<Integer>>q){
        //אם תור לא ממויין לפי אורך מחסניותיו
        if (!Qsorted(q)){return false;}

        //העתק תור מקור
        Queue<Stack<Integer>> temp = copyQueue(q);

        //שליפת מחסנית ראשונה מתור העתק
        Stack<Integer> prevStk = temp.poll();

        //איבר אחרון במחסנית ראשונה
        int prevLast = lstInStack(prevStk);

        //אם מחסנית לא ממויינת בסדר עולה החזר שקר
        if (!sykuPuP(prevStk)){return false;}

        //המשך ריקון תור
        while (!q.isEmpty()){

            //שליפת מחסנית נוכחית מתור העתק
            Stack<Integer> currStk = temp.poll();

            //איבר אחרון במחסנית נוכחית
            int currLast = lstInStack(currStk);

            //אם המחסנית הנוכחית לא ממויינת בסדר עולה החזר שקר
            if (!sykuPuP(prevStk)){return false;}

            //אם איבר ראש במחסנית נוכחית שונה מאיבר אחרון במחסנית קודמת נחזיר שקר
            if (currStk.peek() != prevLast){return false;}

            //נעדכן את ערך משתנה איבר אחרון במחסנית קודמת להיות האיבר האחרון במחסנית נוכחית
            prevLast = currLast;
        }

        //נחזיר אמת, משמע שהתור ממויין לפי אורך מחסניותיו
        //והאיבר האחרון בכל מחסנית שווה לאיבר הראשון במחסית הבאה אחריו בתור
        return true;
    }

    private static boolean isPerfixQstkTree(BinNode<Queue<Stack<Integer>>> root){
        Queue<BinNode<Queue<Stack<Integer>>>> q = new LinkedList<>();
        q.offer(root);
        boolean flag = false;

        while (!q.isEmpty()){
            BinNode<Queue<Stack<Integer>>> current = q.poll();

            if (!isPerfixQstk(current.getValue())){
                flag = true;
            }

            if (current.hasLeft()){
                q.offer(current.getLeft());
            }

            if (current.hasRight()){
                q.offer(current.getRight());
            }
        }


        return flag ? false : true;
    }
    //--------------------------------------------------------------------------//
    //שאלה מרעיון עבודה
    //function that build string from tree using preOrder recursive function
    public static String buildStringFromTree(BinNode<Character> root){
        if (root == null) {
            return "";
        }

        return root.getValue()
                + buildStringFromTree(root.getLeft())
                + buildStringFromTree(root.getRight());
    }

    //function return the longest string in chain of trees
    public static String maxString(Node<BinNode<Character>> chain){

        //pointer for iteration through the linkedlist
        Node<BinNode<Character>> pos = chain;

        //empty string
        String longest = "";

        //iteration proccess
        while (pos != null){

            //call function that build string from the tree that the current node point to it
            String stTemp = buildStringFromTree(pos.getValue());

            //length of the string
            int len = stTemp.length();

            //check if the current string longest than the previous longest string
            if (len > longest.length()){
                longest = stTemp;
            }

            //move the pointer to the next node
            pos = pos.getNext();
        }

        //return the longest string
        return longest;
    }

    //O(n)  instead of O(n**2)
    public static boolean anyStringRepeat(Node<BinNode<Character>> chain){
        //create hash map
        HashMap<String, Integer> TreesList = new HashMap<>();

        //create pointer for the chain
        Node<BinNode<Character>> pos = chain;

        //iteration proccess
        while (pos != null){

            //call the function that build String from tree
            String current = buildStringFromTree(pos.getValue());

            //put the string in the hashmap, with the counter of the occurancess of this string
            TreesList.put(current, TreesList.getOrDefault(current, 0) + 1);

            //get the value of the current entry
            int count = TreesList.getOrDefault(current, 0);

            //if the value(counter of occurances) of the current entry bigger than 1
            //it means that this string found at least twice in the linkedlist of trees
            if (count > 1){
                return true;
            }

            //move the pointer to the next Node in the LinkedList
            pos = pos.getNext();
        }

        return false;
    }

    public static boolean repeat(Node<String> chain){
        Node<String> pos = chain;
        int count = 0;

        while (pos != null){
            String temp = pos.getValue();
            Node<String> check = pos.getNext();

            while (check != null){
                String te = check.getValue();

                if (te.equals(temp)){count++;}

                if (count > 1){return true;}

                check = check.getNext();
            }
            pos = pos.getNext();
        }
        return false;
    }

    public static boolean isWBTree(BinNode<Character> root){
        if (root == null){
            return true;
        }

        if(root.getValue() != 'w' && root.getValue() != 'b'){
            return false;
        }

        if(root.getLeft() == null && root.getRight() == null){
            if (root.getValue() != 'b'){
                return false;
            }
        }

        if (root.getValue() == 'b'){
            if (root.getLeft() == null || root.getRight() == null){
                return false;
            }

            if (root.getLeft().getValue() != 'w' || root.getRight().getValue() != 'w'){
                return false;
            }
        }

        return isWBTree(root.getLeft()) && isWBTree(root.getRight());
    }

    private static int hownChar(String str, char ch){
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ch){
                count++;
            }
        }
        return count;
    }

    //e.g for good string: aaabccdd
    public static boolean uniqStr(String str){
        String innotin = "";//new str

        for (int i = 0; i < str.length(); i++) {
            char current = str.charAt(i);//save current char
            int howMany = hownChar(str,current);//how many times char found in string
            innotin += current;//add current char to the new string i created

            //if char shown at least twice in string and the charcter find once in string
            if (howMany > 1 && hownChar(innotin,current) == 1){
                //create sub-string to check if all ocurances of char shown together
                String temp = str.substring(i,i+howMany);

                for (int j = 0; j < temp.length(); j++) {
                    if (temp.charAt(j) != current){
                        return false;
                    }
                }

                i = i+howMany - 1;//כדי לבדוק את הגוש הצפוי הבא
            }
        }
        return true;
    }

    //פונקצייה עוטפת-מיון רקורסיבי זוגיים-אי זוגיים שרשרת חוליות חד כיוונית
    public static void reEvenOddn(Node<Integer> chain) {
        if (chain == null) {return;}//אם הרשימה ריקה החזר
        Node<Integer> even = null;//פוינטר ראש שרשרת זוגיים
        Node<Integer> le = null;//פויינטר זנב שרשרת אי זוגיים
        Node<Integer> odd = null;//פויינטר ראש שרשרת אי זוגיים
        Node<Integer> lo = null;//פויינטר זנב שרשרת אי זוגיים
        Node<Integer> pos = chain;//פויינטר עזר למעבר על שרשרת מקור
        //קריאה לפונקצייה פרטית שתיצור שרשרת איחוד
        // כך שזוגיים תחילה ולאחריהן חוליות אי זוגיות
        //ואז תעדכן שרשרת מקור בתוכן השרשרת איחוד
        reEvenOddn(chain, pos, even, le, odd, lo);
    }

    private static void reEvenOddn(Node<Integer> chain, Node<Integer> pos, Node<Integer> even, Node<Integer> le, Node<Integer> odd, Node<Integer> lo) {
        //מיקרה בסיס אם הגענו לסוף שרשרת המקור(ניסרקה כולה)
        if (pos == null) {
            if (even != null && odd != null) {
                le.setNext(odd);
                chain.setValue(even.getValue());
                chain.setNext(even.getNext());
            } else if (even == null && odd != null) {
                chain.setValue(odd.getValue());
                chain.setNext(odd.getNext());
            }
        }

        //כל עוד לא הגענו לסוף השרשרת הנוכחית המשך בבניית השרשראות(שרשרת זוגיים && שרשרת אי זוגיים)
        if (pos != null) {
            int x = pos.getValue();//ערך חוליה נוכחית
            Node<Integer> newey = new Node<>(x);//יצירת חוליה להוספה
            if (x % 2 == 0) {//אם הערך זוגי נוסיף אותו לשרשרת הזוגיים
                if (even == null) {//מיקרה ראשון אם שרשרת הזוגיים ריקה
                    even = newey;
                    le = even;
                } else {//מיקרה שני אם היא לא ריקה,נחבר אותה לסוף שרשרת הזוגיים
                    le.setNext(newey);
                    le = le.getNext();
                }
            } else {//אם הערך אי זוגי נוסיף אותו לשרשרת האי זוגיים
                if (odd == null) {//מיקרה ראשון אם שרשרת האי זוגיים ריקה
                    odd = newey;
                    lo = odd;
                } else {//מיקרה שני אם היא לא ריקה,נחבר אותה לסוף שרשרת האי זוגיים
                    lo.setNext(newey);
                    lo = lo.getNext();
                }
            }
            reEvenOddn(chain, pos.getNext(), even, le, odd, lo);//קריאה רקורסיבית עם כל ההפניות הנדרשות
        }
    }

    private static int last(Stack<Integer> stk){
        int last = 0;
        Stack<Integer> temp = new Stack<>();

        while (!stk.isEmpty()){
            temp.push(stk.pop());
        }

        last = temp.peek();

        while (!temp.isEmpty()){
            stk.push(temp.pop());
        }

        return last;
    }

    private static boolean isStackSortedUp(Stack<Integer> stk){
        Stack<Integer> temp = new Stack<>();

        boolean flag = false;
        int prev = stk.pop();
        temp.push(prev);

        while (!stk.isEmpty()){
            int current = stk.pop();
            temp.push(current);

            if (current < prev){
                flag = true;
            }

            prev = current;
        }

        while (!temp.isEmpty()){
            stk.push(temp.pop());
        }

        return flag? false : true;
    }

    private static int sizeOfStack(Stack<Integer> stk){
        Stack<Integer> temp = new Stack<>();
        int count = 0;

        while(!stk.isEmpty()){
            count++;
            temp.push(stk.pop());
        }

        while (!temp.isEmpty()){
            stk.push(temp.pop());
        }

        return count;
    }

    private static boolean isQueueOfStacksSortedUp(Queue<Stack<Integer>> q){
        Queue<Stack<Integer>> temp = new LinkedList<>();
        boolean flag = false;

        Stack<Integer> prevStk = q.poll();
        int prevLen = sizeOfStack(prevStk);
        temp.offer(prevStk);

        while (!q.isEmpty()){
            Stack<Integer> currStk = q.poll();
            int currLen = sizeOfStack(currStk);
            temp.offer(currStk);

            if (currLen < prevLen){
                flag = true;
            }
        }

        while (!temp.isEmpty()){
            q.offer(temp.poll());
        }

        return flag ? false : true;
    }

    private static boolean isPerfectQueueOfStacks(Queue<Stack<Integer>> q){
        if (!isQueueOfStacksSortedUp(q)){
            return false;
        }

        Queue<Stack<Integer>> temp = new LinkedList<>();
        boolean StacknotSortedUp = false;
        boolean lastNotEqualsToFirst = false;

        Stack<Integer> prevStk = q.poll();
        if (!isStackSortedUp(prevStk)){
            StacknotSortedUp  = true;
        }

        int prevlast = last(prevStk);

        while (!q.isEmpty()){
            Stack<Integer> currStk = q.poll();
            temp.offer(currStk);

            if (!isStackSortedUp(currStk)){
                StacknotSortedUp  = true;
            }

            if (currStk.peek() != prevlast){
                lastNotEqualsToFirst = true;
            }

            prevlast = last(currStk);
        }

        while (!temp.isEmpty()){
            q.offer(temp.poll());
        }

        return StacknotSortedUp || lastNotEqualsToFirst ? false : true;
    }

    public static boolean isPerefectTreeOfQueueOfStacks(BinNode<Queue<Stack<Integer>>> root){
        Queue<BinNode<Queue<Stack<Integer>>>> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()){
            BinNode<Queue<Stack<Integer>>> current = q.poll();

            if (!isPerfectQueueOfStacks(current.getValue())){
                return false;
            }

            if (current.hasLeft()){
                q.offer(current.getLeft());
            }

            if (current.hasRight()){
                q.offer(current.getRight());
            }
        }

        return true;
    }

    public static void resetGfile(GradesFile f1){
        //מערך שרשראות סטודנטים מקורי-מבולגן
        Node<StudentG> [] grr = f1.getGrades();

        //יצירת מערך שרשראות סטודנטים חדש
        Node<StudentG> [] arr = new Node[100];

        //מעבר על מערך מבולגן
        for (int i = 0; i < grr.length; i++) {
            //פויינטר לשרשרת-סטודנטים לראש שלה
            Node<StudentG> pos = grr[i];

            //מעבר על השרשרת בתא הנוכחי במערך
            while (pos != null){
                //קבלת סטודנט נוכחי
                StudentG tempSt = pos.getValue();
                //קבלת קוד סטודנט
                int code = tempSt.getCode();
                //יצירת חולייה עם סטודנט נוכחי כדי שנוסיף אותו למערך החדש לשרשרת החדשה בו
                Node<StudentG> toAdd = new Node<>(tempSt);

                //מקרה ראשון אם השרשרת במיקום ההוא במערך ריקה, מקרה שני אם לא ריקה כלומר ישנם חוליות
                if (arr[code] == null){
                    arr[code] = toAdd;
                }else{
                    toAdd.setNext(arr[code]);
                    arr[code] = toAdd;
                }

                //התקדמות לחולייה הבאה
                pos = pos.getNext();
            }
        }

        //שינוי המערך במחלקה שבה המערך היה מבולגן,
        // להיות המערך החדש
        f1.setGrades(arr);
    }

    //O(n)
    public static boolean isParity(Stack<Integer> st){
        //stack for restoration
        Stack<Integer> temp = new Stack<>();

        //initialize
        int even = 0;
        int odd = 0;
        int esum = 0;
        int osum = 0;
        boolean flag = false;

        //iteration
        while (!st.isEmpty()){
            int x = st.pop();
            //push it to the sec stk
            temp.push(x);

            if (x < 0){ flag = true;}

            if (x % 2 == 0){
                even++;
                esum += x;
            }else {
                odd++;
                osum += x;
            }
        }

        //restoration
        while (!temp.isEmpty()){st.push(temp.pop());}

        return !flag && even == odd && esum == osum ? true : false;
    }

    private static boolean isinq(Queue<Integer> q, int num){
        Queue<Integer> temp = cloneQueue(q);

        while (!temp.isEmpty()){
            if (temp.poll() == num){
                return true;
            }
        }
        return false;
    }

    public static int distance(Queue<Integer>q, int x, int y){
        if (!isinq(q,x) || !isinq(q,y)){return -1;}

        //תור העתק מקור
        Queue<Integer> copyQ = cloneQueue(q);
        //מונה
        int count = 0;
        boolean flag = false;

        //איטרציה על התור
        while (!copyQ.isEmpty()){
            //שליפת ערך נוכחי מתור
            int temp = copyQ.poll();

            //אם נמצא X או Y
            if (temp == x || temp == y){
                //נמשיך לרוקן את התור
                while (!copyQ.isEmpty()){
                    //שליפת ערך נוכחי מתור
                    int temp2 = copyQ.poll();
                    // ומניית כל הערכים בין איקס לוואי
                    if (temp2 != x && temp2 != y){
                        count++;
                    }else {
                        flag = true;
                        //הגענו בפעם השנייה לאיקס או וואי, נעצור
                        break;
                    }
                }
            }
            if (flag){break;}
        }

        //החזרת מונה
        return count;
    }//O(n)

    //דרך פתרון א
    private static int howManyTimesFoundInList(Node<Character> chain, char num){
        if (chain == null){return -1;}

        int count = 0 ;

        while(chain != null){
            if (chain.getValue() == num){count++;}
            chain = chain.getNext();
        }
        return count;
    }

    public static boolean allOccurTwice(Node<Character> chain){
        if (chain == null){return false;}

        Node<Character> pos = chain;

        while (pos != null){
            Character currentChar = pos.getValue();
            int howmany = howManyTimesFoundInList(chain, currentChar);
            if (howmany != 2){return false;}
        }
        return true;
    }

    //דרך פתרון ב
    private static void bubblesortnode(Node<Character> chain){
        if (chain == null){return;}

        Node<Character> pos = chain;

        while (pos != null){
            Node<Character> current = chain;

            while (current.getNext() != null){

                if (current.getValue() > current.getNext().getValue()){
                    char temp = current.getValue();
                    current.setValue(current.getNext().getValue());
                    current.getNext().setValue(temp);
                }
            }
        }
    }

    public static boolean allt(Node<Character> chain){
        bubblesortnode(chain);

        Node<Character> pos = chain;

        while (pos != null){
            int current = pos.getValue();

            Node<Character> con = pos;
            int count = 0;

            while (con != null){

                if (con.getValue() == current){count++;}

                if (count != 2){return false;}

                con = con.getNext();
            }

            pos = con;
        }

        return true;
    }

    public static String bigSenderr(MailItem[] items) {
        //אתחול משתנה לשמירת מחיר החבילה הגבוה ביותר
        double maxPrice = -1;
        //מחרוזת לשמירת שם השולח של החבילה היקרה ביותר
        String maxSender = null;

        //איטרציה על המערך פריטים
        for (int i = 0; i < items.length; i++) {
            //אם פריט זה הינו "חבילה למשלוח"
            if (items[i] instanceof Package) {
                //המרה מפורשת לסוג הפריט "חבילה"
                Package pack = (Package) items[i];
                //חישוב המחיר של החבילה
                double price = pack.getRealPrice();
                //אם נמצא מחיר חבילה הגבוה יותר ממחיר השמור במשתנה המקסימום
                //נעדכן את המחיר השמור במשתנה המקסימום להיות המחיר של חבילה זו
                if (price > maxPrice) {
                    maxPrice = price;
                    maxSender = pack.getName();
                }
            }
        }
        //החזרת שם השולח
        return maxSender;
    }

    public static void one1(Queue<Integer>q, int k){
        //תור עזר
        Queue<Integer> temp = new LinkedList<>();

        //איטרציה על התור מקור
        while (!q.isEmpty()){
            //נדחוף את הערך הנוכחי בתור מקור(ראש התור) K פעמים לתור עזר
            int i = 0;
            while (i < k){
                temp.offer(q.peek());
                i++;
            }
            //שליפת הראש מהתור מקור
            q.poll();
        }

        //שיחזור תור מקור
        while (!temp.isEmpty()){q.offer(temp.poll());}
    }

    public static void two2(Queue<Integer>q, int k){
        //תור העתק מקור
        Queue<Integer> copy = copyQueue(q);
        int i = 0;//משתנה עזר הרץ עד זקיף

        while (i < k){
            //תור עזר לשחזורי ביניים
            Queue<Integer> temp = new LinkedList<>();

            //נרוקן את התור העתק מקור
            while (!copy.isEmpty()){
                //נכניס ערך לתור מקור
                q.offer(copy.peek());
                //נכניס ערך לתור שיחזור
                temp.offer(copy.peek());
                //נשלוף אותו מתור "העתק מקור"
                copy.poll();
            }

            //נשחזר תור "העתק מקור"
            while (!temp.isEmpty()){copy.offer(temp.poll());}
            //קידום ה I
            i++;
        }
    }

    public static boolean MesudarTree(BinNode<Integer> root){
        //תור לסריקה לרוחב
        Queue<BinNode<Integer>> q = new LinkedList<>();
        //הכנסת השורש לתור סריקה
        q.offer(root);

        //סריקה ע"יי תור
        while (!q.isEmpty()){
            //שליפת חולייה נוכחית מתור והצבעה עלייה
            BinNode<Integer> current = q.poll();

            //אם עלה-נבדוק אם מכיל ערך זוגי נחזיר שקר
            if (!current.hasLeft() && !current.hasRight()){
                if (current.getValue() % 2 == 0){
                    return false;
                }
            }

            //אם יש לו בן אחד לפחות כלומר הורה
            //אם ההורה מכיל ערך אי זוגי נחזיר שקר
            if (current.hasLeft() || current.hasRight()){
                if (current.getValue() % 2 == 1){
                    return false;
                }
            }

            //אסור לאב להכיל ערך הגדול מבניו-אם כן נחזיר שקר
            //בדיקה לגבי בן ימני
            if (current.hasRight()){
                if (current.getValue() > current.getRight().getValue()){
                    return false;
                }
            }
            //בדיקה לגבי בן שמאלי
            if (current.hasLeft()){
                if (current.getValue() > current.getLeft().getValue()){
                    return false;
                }
            }

            //הכנסת בנים לתור להמשך סריקה
            if (current.hasLeft()){q.offer(current.getLeft());}
            if (current.hasRight()){q.offer(current.getRight());}
        }
        return true;
    }

    public static Stack<Integer> nst(int n){
        Stack<Integer> stk = new Stack<>();

        for (int i = n; i >= 1; i--) {
            int num = i;
            int j = 0;

            while (j < num){
                stk.push(num);
                j++;
            }
        }

        return stk;
    }

    public static boolean isPyramidN(Stack<Integer> stk) {
        Stack<Integer> copyStk = cloneStack(stk);

        while (!copyStk.isEmpty()) {
            int current = copyStk.pop();
            int count = 1;

            while (!copyStk.isEmpty() && copyStk.peek() == current) {
                copyStk.pop();
                count++;
            }

            if (count != current) {
                return false;
            }
        }

        return true;
    }

    public ListNode swapPairs(ListNode A) {
        // ניצור stam node שיקל על הטיפול בראש הרשימה
        ListNode stam = new ListNode(0);
        stam.next = A;

        // מצביע זמני שיעבור בזוגות
        ListNode prev = stam;

        while (prev.next != null && prev.next.next != null) {
            ListNode first = prev.next;
            ListNode second = first.next;

            //החלפת חוליות
            first.next = second.next;
            second.next = first;
            prev.next = second;

            //הזזת prev קדימה
            prev = first;
        }

        // ראש הרשימה החדש
        return stam.next;
    }

    //שאלה מ"ס 1
    //סעיף א'
    public static Queue<Integer> QDN(int num){
        //יצירת תור להחזרה
        Queue<Integer> q = new LinkedList<>();

        //מעבר בלולאה עד המס שהתקבל בפונקצייה
        for (int i = 1; i <= num; i++) {
            int j = 0;
            //הזנת אותו מס(i) נוכחי, i פעמים לתור
            while (j < i){
                q.offer(i);
                j++;
            }
        }

        //החזרת התור מדרגה N
        return q;
    }

    //סעיף ב'
    public static boolean isQDN(Queue<Integer> q, int num){
        //בניית תור עזר מדרגה N , לפי המספר שהתקבל בפונקצייה
        Queue<Integer> temp = QDN(num);

        //איטרציה-בדיקה שהתור שהתקבל בפונקצייה זהה לתור עזר שבנינו מדרגה N
        while (!temp.isEmpty() && !q.isEmpty()){
            //השוואה בין ערכים משני התורים מקור, ועזר
            //אם נמצא שוני החזר שקר
            if (temp.poll() != q.poll()){
                return false;
            }
        }
        //אם אחד התורים נשאר מלא נחזיר שקר, אחרת אמת
        return !temp.isEmpty() && !q.isEmpty()? false : true;
    }

    //שאלה מ"ס 2
    //סעיף א'
    public static boolean isGlist(Node<Integer> chain){
        //פויינטר לשרשרת
        Node<Integer> pos = chain;

        if (pos.getNext() != null) {
            if (pos.getValue() != pos.getNext().getValue()) {return false;}
        }

        //הגדרת צובר ואתחולו בערך החולייה הראשונה
        int sum = pos.getValue();

        //מעבר על השרשרת
        while (pos != null){
            //שמירת ערך נוכחי
            int currentNum = pos.getValue();
            //אם ערכו שונה מסכום האיברים שלפניו נחזיר שקר
            if (currentNum != sum){return false;}
            //נצבור ערך נוכחי זה
            sum += currentNum;
            //נעבור לחולייה הבאה
            pos = pos.getNext();
        }

        //נחזיר אמת שרשרת סכומים תקינה
        return true;
    }

    //סעיף ב'
    //פונקציית עזר
    private static int sumList(Node<Integer> chain){
        //אם השרשרת ריקה נחזיר -1
        if (chain == null){return -1;}
        //אתחול צובר
        int sum = 0;

        //מעבר על השרשרת, וצבירת ערך החוליות לצובר
        while (chain != null){
            //צבירה
            sum += chain.getValue();
            //התקדמות לחולייה הבאה
            chain = chain.getNext();
        }

        //החזרת צובר
        return sum;
    }

    //פונקציה עיקרית
    public static boolean isGlistReverse(Node<Integer> chain){
        //פויינטר לשרשרת
        Node<Integer> pos = chain;

        //מעבר על השרשרת
        while (pos.getNext() != null){
            //שמירת מס נוכחי
            int currentNum = pos.getValue();
            //חישוב סכום ערכי החוליות הבאים אחריו עד סוף השרשרת
            int sumNexts = sumList(pos.getNext());
            //אם ערך איבר נוכחי שונה מסכום האיברים הבאים אחריו, נחזיר שקר
            if (currentNum != sumNexts){return false;}
            //נתקדם לחולייה הבאה
            pos = pos.getNext();
        }

        //נחזיר אמת שרשרת סכומים הפוכה תקינה
        return true;
    }

    //שאלה מ"ס 3
    //א D -> B -> A -> C
    //ב  B -> A -> D -> C

    //wrong
    public static void changeHeadIntegerNode(Node<Integer> chain){
        Node<Integer> toAdd = new Node<>(9);
        toAdd.setNext(chain);
        chain = toAdd;//לא חוקי
    }

    //right
    //O(1)
    public static void changeHead(NodeWrapper<Integer> wrapper) {
        Node<Integer> toAdd = new Node<>(9);
        toAdd.setNext(wrapper.getHead());
        wrapper.setHead(toAdd); // שינוי אמיתי
    }

    //O(n)
    public static int putInPlaceT(Queue<Integer>q, int num){
        //index for num
        int index = 0;

        //small numbers queue
        Queue<Integer> small = new LinkedList<>();
        //big number queue
        Queue<Integer> big = new LinkedList<>();

        //iteration on original queue
        while (!q.isEmpty()){
            //poll out current value from queue
            int current = q.poll();

            //if it's small offer it to the small-numbers-queue, else to the big-num-queue
            if (current < num){
                small.offer(current);
            }else if (current >= num){
                big.offer(current);
            }
        }

        //restoration
        while (!small.isEmpty()){
            q.offer(small.poll());
            index++;
        }
        //offer num to the queue
        q.offer(num);
        index++;

        //restoration
        while (!big.isEmpty()){ q.offer(big.poll()); }

        //return index of num
        return index;
    }

    //O(n)
    public static void moveToFrontC(Queue<Integer>q, int k){
        //size of queue
        int size = q.size();
        //counter how many nodes have been moved to the back of the queue
        int toSize = 0;
        //how many nodes to mv to the bck side of the queue
        int howManyToRemove = size - k;

        //poll it out and offer it to the end of the queue
        while (toSize < howManyToRemove){
            q.offer(q.poll());
            toSize++;
        }
    }

    //O(n^2)
    //order binnode list using bubble sort algorithm
    public static void orderBin(BinNode<Integer>chain){
        //pointer for the list
        BinNode<Integer> pos = chain;

        //mv the ptr to the far left side of the list
        while (chain.getLeft() != null){
            chain = chain.getLeft();
        }

        //buble sort
        while (pos != null){
            //pointer for the pointer of the list
            BinNode<Integer> current = pos;
            while (current.getLeft() != null){
                //if current val is odd and the next val is even swap
                if (current.getValue() % 2 == 1 && current.getLeft().getValue() % 2 == 0){
                    //swap proccess
                    int temp = current.getValue();
                    current.setValue(current.getLeft().getValue());
                    current.getLeft().setValue(temp);
                }
            }
            pos = pos.getLeft();
        }
    }

    //O(n)
    //if number in list -> true
    private static boolean Inlist(Node<Integer> chain, int num){
        if (chain == null){
            return false;
        }
        //pointer for the list
        Node<Integer> pos = chain;

        //iteration
        while (pos != null){
            //if num found return true
            if (pos.getValue() == num){
                return true;
            }
            //mv to the next node
            pos = pos.getNext();
        }

        //return false num not found in list
        return false;
    }

    //O(n)
    //how manny times number shown in list -> int
    private static int hmilist(Node<Integer> chain, int num){
        //pointer for the head of the list
        Node<Integer> pos = chain;
        //counter num of occurances of number in list
        int count = 0;

        //iteration
        while (pos != null){
            //if num found count it
            if (pos.getValue() == num){
                count++;
            }

            //mv to the next node
            pos = pos.getNext();
        }

        //return the counter
        return count;
    }

    //O(n^2)
    //build frequently list
    private static void buildFreqList3(Node<Integer> chain){
        //pointers for the new list
        Node<Integer> newey = null;
        Node<Integer> tail = null;

        //pointer for the head of the original list
        Node<Integer> pos = chain;

        while (pos != null){
            //current number
            int currentNumber = pos.getValue();
            //how many times num shown in the new list
            int howManyTimeFoundInList = hmilist(chain, currentNumber);

            //if the current number not in the new list add it, and its counter
            if (! Inlist(newey, currentNumber)){
                //create node with current number
                Node<Integer> numberToAdd = new Node<>(currentNumber);
                //create node with the number of occurances of current number
                Node<Integer> counterToAdd = new Node<>(howManyTimeFoundInList);
                //connect them
                numberToAdd.setNext(counterToAdd);

                //if new list is empty
                if (newey == null){
                    //add two nodes to the list
                    newey = numberToAdd;
                    //point the tail pointer to the sec node(that conected to the first node)
                    tail = numberToAdd.getNext();
                }else{
                    //if list not empty add them to the end of the list
                    tail.setNext(numberToAdd);
                    //point the tail pointer to the sec node(that conected to the first node)
                    tail = tail.getNext().getNext();
                }
            }
            //mv to the next node
            pos = pos.getNext();
        }

        //update original list
        //mv pos to start
        pos = chain;

        //update the original list
        while (newey != null){
            pos.setValue(newey.getValue());
            newey = newey.getNext();
            pos = pos.getNext();
        }

        //remove unwanted nodes
        pos.setNext(null);
    }

    public static int mostPopularNumberMax(Node<Integer> chain){
        //call function to build freqlist
        buildFreqList3(chain);
        //pointer for the head of list
        Node<Integer> pos = chain;

        //will contain: how many times a number shown in list
        int MaxOccur = Integer.MIN_VALUE;
        //will contain the number with the highest number of occurances in list
        int theNumber = -1;

        //iteration
        while (pos.getNext() != null){
            //keep current node val in "num" var
            int num = pos.getValue();
            //keep the number of occurances of num in "howMany" var
            int howMany = pos.getNext().getValue();

            //check if this number is more popular
            if (howMany > MaxOccur){
                MaxOccur = howMany;
                theNumber = num;
            }

            //get to the next node in list
            pos = pos.getNext();
        }

        //if number not equals to -1, return the number, else return -1
        return theNumber != -1 ? theNumber : -1;
    }

    //O(n)
    public static int midInStk(Stack<Integer>stk){
        Stack<Integer> copy = cloneStack(stk);
        int [] arr = new int[copy.size()];
        int index = 0;

        while (!copy.isEmpty()){
            arr[index++] = copy.pop();
        }

        return arr[arr.length / 2];
    }

    //O(n)
    public static boolean equalSumst(Stack<Integer> stk){
        //מחסנית העתק
        Stack<Integer> copy = cloneStack(stk);

        //אורך מחסנית
        int size = copy.size();

        //אם אורך זוגי נחזיר שקר
        if (size % 2 == 0){ return false; }

        //איבר אמצעי במחסנית
        int mid = midInStk(copy);

        while (size > 1){
            //ערך ראשון
            int first = copy.pop();
            size--;

            //משתנה עזר לריצה עד איבר לפני אחרון(כולל) והכנסתם למחסנית עזר לשיחזור
            int toEnd = 0;
            //מחסנית עזר
            Stack<Integer> temp = new Stack<>();

            while (!copy.isEmpty() && toEnd < size - 1){
                temp.push(copy.pop());
                toEnd++;
            }

            //ערך אחרון
            int last = stk.pop();
            size--;


            if (last + first != mid){
                return false;
            }

            //שיחזור ביניים של מחסנית מקור ללא האיבר הראשון ואחרון שנשלפו
            while (!temp.isEmpty()){
                copy.push(temp.pop());
            }
        }

        return true;
    }

    //O(n)
    public static boolean equalSumst2(Stack<Integer> stk){
        //מערך בגודל המחסנית
        int [] arr = new int[stk.size()];
        //אינדקס למעבר על המערך ולמילוי המערך
        int index = 0;

        //ריקון המחסנית למערך
        while (!stk.isEmpty()){
            arr[index++] = stk.pop();
        }

        //שמירת איבר אמצעי
        int mid = arr[arr.length / 2];

        //מעבר עד חצי המערך ובדיקת קצוות
        for (int i = 0; i < arr.length/2; i++) {
            //אם הראשון ועוד האחרון שונה מהאיבר האמצעי נחזיר שקר
            if (arr[i] + arr[arr.length - i - 1] != mid){
                return false;
            }
        }

        //לא חזר שקר, משמע כל זוג איברים מקצוות המערך סכומם שווה לאיבר האמצעי ולכן נחזיר אמת
        return true;
    }

    private static int numNodesFollowing2(Node<Integer> chain){
        int count = 0;

        while (chain != null){
            count++;
            chain = chain.getNext();
        }

        return count;
    }


    //2022 summer b' - 8
    //assist functioin
    public static void updateAllMynodes(MyNode chain){
        //pointer for the head of the list
        MyNode pos = chain;

        //iteration
        while (pos != null){
            //current value from list
            int current = pos.getValue();
            //pointer for the rest of the list
            MyNode pos2 = pos.getNext();
            //counter for counting the nodes that contain higher value than current value
            int  count = 0;

            //continue the iteration on the rest of the list searching for nodes with higher value than current
            while (pos2 != null){
                //if value like that found, count it
                if (pos2.getValue() > current){
                    count++;
                }
                //mv to the next MyNode
                pos2 = pos2.getNext();
            }

            //update current node "howManyBig" property
            pos.setHowManyBig(count);
            //mv to the next MyNode
            pos = pos.getNext();
        }
    }

    public static MyNode addNumber(MyNode list, int val, int position){
        //create MyNode for adding it later to the list
        MyNode toAdd = new MyNode(val);

        //add at start of the list
        if (position == 1){
            //attach it before the head
            toAdd.setNext(list);
            //update "howManyBig" property in every MyNode
            updateAllMynodes(list);
            //return the new head
            return toAdd;
        }

        //pointer for the head
        MyNode pos = list;
        int count = 0;

        //add it everywhere else
        while (pos != null && pos.getNext() != null){
            if (count + 1 == position){
                //add the new node between the current two nodes
                toAdd.setNext(pos.getNext());
                pos.setNext(toAdd);
                //update "howManyBig" property in every MyNode
                updateAllMynodes(list);
                //return the head
                return list;
            }

            //keep counting till reach the position
            count++;
            //mv to the next MyNode
            pos = pos.getNext();
        }

        //add it at the end
        if (count == position){
            //add it to the end
            pos.setNext(toAdd);
            //change the "howManyBig" property of all the nodes in the list
            updateAllMynodes(list);
            //return the updated head
            return list;
        }

        return null;
    }


    //2022 summer a' - 1
    public static void HefreshListy(Node<Integer> chain){
        //פוינטר עזר לשרשרת
        Node<Integer> pos = chain;
        //שמירת לפני איבר אחרון
        Node<Integer> prev = pos;

        //איטרציה
        while (pos != null){
            //משתנה שיחזיק את ההפרש בין שני חוליות צמודות
            int d = 0;
            //אם האיבר הבא אינו NULL
            if (pos.getNext() != null){
                //נחשב את ההפרש
                d = Math.abs(pos.getValue() - pos.getNext().getValue());
                // נשנה את ערך החולייה הנוכחית להיות ההפרש
                pos.setValue((d));
            }else{//ניתוק חוליה אחרונה, היא מיותרת
                prev.setNext(null);
            }

            prev = pos;
            //נתקשם לחוליה הבאה
            pos = pos.getNext();
        }

    }

    public static int lastHefreshList(Node<Integer> chain){
        while (lengthN(chain) > 1){
            HefreshListy(chain);
            System.out.println();
            printListNod(chain);
            System.out.println();
        }
        return chain.getValue();
    }

    //2 O(n)
    public static boolean stkSumUp(Stack<Integer> stk){
        //create copy stack
        Stack<Integer> temp = cloneStack(stk);
        //initialize sum variable to zero
        int sum = 0;

        //iteration
        while (!temp.isEmpty()){
            //pop current value from stack
            int currentNum = temp.pop();

            //if current value smaller than sum, return false
            if (currentNum < sum){ return false; }

            //sum up current value
            sum += currentNum;
        }

        //all values in stack bigger than the sum of the previous values before them
        return true;
    }

    //8
    //a
    public static int sumQueue2(Queue<Integer> q){
        //copy queue
        Queue<Integer> copy = cloneQueue(q);
        //sum
        int sum = 0;

        //iteration
        while (!q.isEmpty()){ sum += q.poll(); }

        //return sum
        return sum;
    }

    //b
    public static int lastValueQ(Queue<Integer> q){
        //copy queue
        Queue<Integer> copy = cloneQueue(q);
        //assist stack for find last val in queue
        Stack<Integer> stk = new Stack<>();

        //iteration mv all queue vals into the assist stack
        while (!copy.isEmpty()) { stk.push(copy.poll()); }

        //return the first value in stack (is the last value in queue)
        return stk.peek();
    }

    public static Queue<Integer> lsOfQu(Node<Queue<Integer>> chain){
        //create new queue
        Queue<Integer> q = new LinkedList<>();

        //create pointer for chain
        Node<Queue<Integer>> pos = chain;

        //iteration
        while (pos != null){
            //the current value first in the current queue
            int currentTop = pos.getValue().peek();

            //if this val is odd
            if (currentTop % 2 == 1){
                //calc sum of this current queue
                int sum = sumQueue2(pos.getValue());
                //offer the sum to the new queue
                q.offer(sum);
            }else {
                //found last val in current queue
                int last = lastValueQ(pos.getValue());
                //offer it to the new queue
            }

            //mv to the next node that contains the next queue
            pos = pos.getNext();
        }

        //return the new queue
        return q;
    }

    //9
    public static void printAffordableApartments(Appartment[] building, double budget) {
        System.out.println("Apartments available within budget " + budget + ":");

        for (Appartment app : building) {
            // נניח null פירושו דירה לא קיימת או לא רלוונטית
            if (app != null && app.getPrice() <= budget) {
                System.out.println(app);
            }
        }
    }

    //7
    public static void what1(BinNode<Integer> bt) {
        if(bt!=null){
            int x=bt.getValue();
            if(what2(bt.getLeft(),x) && what2(bt.getRight(),x))
                System.out.println(x);
            what1(bt.getLeft());
            what1(bt.getRight());
        }
    }

    public static boolean what2(BinNode<Integer> bt, int x) {
        if(bt==null)return true;
        if(bt.getValue()==x)return false;
        return what2(bt.getLeft(), x) && what2(bt.getRight(), x);
    }

    //משתנה צובר סטטי גלובלי
    private static int sum = 0;
    //משתנה מונה סטטי גלובלי
    private static int count = 0;

    //פונקציה עוטפת
    public static int avgt(BinNode<Integer> root) {
        //אתחול צובר
        sum = 0;
        //אתחול מונה
        count = 0;
        //קריאה לפונק הרקורסיבית המחשבת את הממוצע של העץ
        traverse(root);
        //החזרת הממוצע
        return sum / count;
    }

    // פונקצייה רקורסיבית שמחשבת סכום וכמות
    private static void traverse(BinNode<Integer> node) {
        if (node == null) return;
        //צבירת ערך נוכחי
        sum += node.getValue();
        //מניית חוליה נוכחית
        count++;
        //קריאות רקורסיביות להמשך איטרציה
        traverse(node.getLeft());
        traverse(node.getRight());
    }

    //פונקציה עוטפת
    public static void allBigThanAvg(BinNode<Integer> root){
        //קריאה לפונקציה להדפסת ערכים הגדולים מהממוצע
        allBigThanAvg(root,avgt(root));
    }

    //פונק רקורסיבית להדפסת כל הערכים בעץ שגדולים מהממוצע
    private static void allBigThanAvg(BinNode<Integer> root, int avg) {
        //אם הגענו לסוף
        if (root == null) return;

        //אם ערך נוכחי גדול מהממוצע נדפיס ערך זה
        if (root.getValue() > avg) { System.out.println(root.getValue()); }

        //קריאות רקורסיביות להמשך סריקת העץ
        traverse(root.getLeft());
        traverse(root.getRight());
    }

    //שני מחסניות
    public static boolean isBottom(Stack<Integer> stk1, Stack<Integer> stk2){
        Stack<Integer> temp1 = new Stack<>();
        Stack<Integer> temp2 = new Stack<>();

        while (!stk1.isEmpty()){ temp1.push(stk1.pop()); }

        while (!stk2.isEmpty()){ temp2.push(stk2.pop()); }

        while (!temp2.isEmpty() && !temp1.isEmpty()){
            if (temp2.pop() != temp1.pop()){
                return false;
            }
        }

        return true;
    }

    public static Stack<Integer> commonButtom(Stack<Integer> stk1, Stack<Integer> stk2){
        Stack<Integer> temp1 = new Stack<>();
        Stack<Integer> temp2 = new Stack<>();
        Stack<Integer> finalstk = new Stack<>() ;

        while (!stk1.isEmpty()){ temp1.push(stk1.pop()); }

        while (!stk2.isEmpty()){ temp2.push(stk2.pop()); }

        while (!temp2.isEmpty() && !temp1.isEmpty() && temp2.peek() == temp1.peek()){
            finalstk.push(temp1.peek());
            temp2.pop();
            temp1.pop();
        }

        return finalstk;
    }

    public static boolean isFiboxyz(Node<Integer> chain){
        if (chain == null || chain.getNext() == null || chain.getValue() != 1 && chain.getNext().getValue() != 1){
            return false;
        }

        int x = 0;
        int y = 0;
        int z = 0;
        Node<Integer> pos = chain;

        while (pos.getNext().getNext().getNext() != null){
            x = pos.getValue();
            y = pos.getNext().getValue();
            z = pos.getNext().getNext().getValue();

            if (x + y != z){ return false; }

            pos = pos.getNext();
        }

        x = pos.getValue();
        y = pos.getNext().getValue();
        z = pos.getNext().getNext().getValue();

        if (x + y != z){ return false; }

        return true;
    }

    public static Node<Integer> buildFibonachi2(int num){
        //יצירת מערך
        int [] arr = new int[num];
        arr[0] = 1;
        arr[1] = 1;

        //בניית מערך
        for (int i = 2; i <= num; i++) { arr [i+2] = arr[i+1] + arr[i]; }

        //יצירת שרשרת חדשה
        //פויינטר לראש
        Node<Integer> chain = null;
        //פויינטר לזנב
        Node<Integer> tail = null;

        //איטרציה על המערך ובניית השרשרת
        for (int i = 0; i < arr.length; i++) {
            //יצירת החולייה להוספה
            Node<Integer> toAdd = new Node<>(arr[i]);

            //מיקרה: השרשרת ריקה
            if (chain == null){
                chain = toAdd;
                tail = chain;
            }else{ //מיקרה: השרשרת לא ריקה
                tail.setNext(toAdd);
                tail = tail.getNext();
            }
        }

        //החזרת השרשרת
        return chain;
    }

    public static boolean isGoDown(BinNode<Integer> root){
        //if we reach the end of the tree
        if (root == null){ return true; }

        //if current is parent in genral
        if (root.getLeft() != null || root.getRight() != null){

            //if this parrent dont have two childrens return false
            if (root.getLeft() == null || root.getRight() == null){ return false; }

            //if this parrent contains value that smaller than its childrens, return false
            if (root.getValue() < root.getLeft().getValue() || root.getValue() < root.getRight().getValue()){
                return false;
            }
        }

        //recursive call
        return isGoDown(root.getLeft()) && isGoDown(root.getRight());
    }

    //O(n)
    public static boolean PerfectList(Node<Integer> chain){
        //create hashmap
        HashMap<Integer, Integer> map = new HashMap<>();
        //create pointer for the list
        Node<Integer> pos = chain;

        //iteration on the list and build the map
        while (pos != null){
            //keep current value
            int num = pos.getValue();
            //put it in map the value and how many times this val found in the list
            map.put(num, map.getOrDefault(num, 0) + 1);
            //mv to the next node
            pos = pos.getNext();
        }

        //iteration on the map
        for (Map.Entry<Integer, Integer> entry : map.entrySet()){
            //if current entry value is positive
            if (entry.getKey() > 0 && entry.getValue() % 2 != 0){ return false;}
            //if current entry value is negative
            else if (entry.getKey() > 0 && entry.getValue() % 2 != 1){ return false; }
        }

        return true;
    }


    public static boolean IsPerfectN(Queue<Integer> q, int n){
        int [] arr = new int[n];//יצירת מערך מונים בגודל N
        Queue<Integer> copyQ = cloneQueue(q);//תור עזר לשחזור

        while (!copyQ.isEmpty()) {
            //שליפת ערך נוכחי
            int current = copyQ.poll();
            //בניית מערך המונים
            if (current >= 1 && current <= n) { arr[current - 1] += 1; }
        }

        //מעבר על מערך המונים אםם יש תא שמכיל את הערך 0 משמע אחד המס מ 1 עד N לא מופיע
        for (int i = 0; i < arr.length; i++) { if (arr[i] == 0){ return false; } }

        return true;
    }

    public static void bQperfect(Queue<Integer> q, int n){
        int [] arr = new int[n];//יצירת מערך מונים בגודל N
        Queue<Integer> finalq = new LinkedList<>();

        while (!q.isEmpty()){
            int num = q.poll();

            if (num >= 1 && num <= n){
                arr[num - 1] += 1;
                finalq.offer(num);
            }
        }

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 0){
                finalq.offer(i);
            }
        }

        while (!finalq.isEmpty()){
            q.offer(finalq.poll());
        }
    }

    public static Queue<String> bQfromLenStr(BinNode<String> root){
        //queue for iteration
        Queue<BinNode<String>> q = new LinkedList<>();
        //offer the root
        q.offer(root);

        //create new queue
        Queue<String> finalq = new LinkedList<>();
        //size of tree
        int size = 0;

        //iteration on the tree
        while (!q.isEmpty()){
            //poll current binnode from the queue
            BinNode<String> current = q.poll();
            //add 1 to the size of the tree
            size++;
            //offer the string to the new queue created
            finalq.offer(current.getValue());

            //offer the childrens of the current parrent to the queue
            if (current.hasLeft()){ q.offer(current.getLeft()); }
            if (current.hasRight()){ q.offer(current.getRight()); }
        }

        //build strings array from queue
        String [] arr = new String[size];
        int index = 0;
        while (!finalq.isEmpty()){ arr[index++] = finalq.poll(); }

        //sort the array acordings to its strings lengthes
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j].length() > arr[j+1].length()){
                    String temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }

        //build the sorted queue
        for (int i = 0; i < arr.length; i++) { finalq.offer(arr[i]); }

        //return the new queue
        return finalq;
    }

    public static boolean where(BinNode<Integer> bt, int num, int count){
        if (bt == null){ return false; }

        if (num < 1 || count < 1){ return false; }

        if (bt.getLeft() == null && bt.getRight() == null){ return bt.getValue() == num && count == 1; }

        return where(bt.getLeft(), num - bt.getValue(), count - 1) || where(bt.getRight(), num - bt.getValue(), count - 1);
    }

    public static void One(Stack<Integer> s, int x){
        if(s.isEmpty())
            s.push(x);
        else
        {
            int temp=s.pop();
            One(s,x);
            s.push(temp);
        }
    }
    public static void Two(Stack<Integer> s){
        if(!s.isEmpty())
        {
            int x = s.pop();
            Two(s);
            One(s,x);
        }
    }

    //פונק הסוכמת את כל הערכים הראשוניים בעץ
    public static int atree(BinNode<Integer> root){
        //מקרה בסיס: אם הגענו לסוף נחזיר 0
        if (root == null){ return 0; }

        //עבור כל ערך חולייה בדוק אם הוא ראשוני
        for (int i = 2; i < root.getValue(); i++){
            //אם הוא ראשוני נתקדם לחולייה הבאה
            if (root.getValue() % i == 0){
                return atree(root.getLeft()) + atree(root.getRight());
            }
        }

        //אם לא ראשוני נסכום אותו
        return root.getValue() + atree(root.getLeft()) + atree(root.getRight());
    }

    public static void bsBin(BinNode<Integer> chain){
        BinNode<Integer> pos = chain;

        while (pos != null){

            BinNode<Integer> current = pos.getRight();

            while (current.getRight() != null){

                if (current.getValue() % 2 == 1 && current.getRight().getValue() % 2 == 0){
                    int temp = current.getValue();
                    current.setValue(current.getRight().getValue());
                    current.getRight().setValue(temp);
                }

                current = current.getRight();
            }

            pos = pos.getRight();
        }
    }

    //2025 אביב מועד ב
    //1
    public static boolean divallstk(Stack<Integer> stk){
        Stack<Integer> temp = new Stack<>();

        while (!stk.isEmpty()){
            //שליפת מס נוכחי
            int n = stk.pop();

            //מעבר על שאר המחסנית לבדיקת האם יש חלוקה
            while (!stk.isEmpty()) {
                if (n % stk.peek() == 0 || n % stk.peek() % n == 0){
                    return false;
                }
                //שליפת הערך שעת עתה נבדק והכנסתו למחסנית שיחזור
                temp.push(stk.pop());
            }

            //שיחזור המחסנית ללא המס הנוכחי שנשלף
            while (!temp.isEmpty()){ stk.push(temp.pop()); }
        }

        return true;
    }

    //2
    //א
    public static boolean allKtsavotSumEquals(Queue<Integer> q){
        Queue<Integer> temp = new LinkedList<>();
        int size = q.size();
        int sum = 0;

        while (!q.isEmpty()){
            int first = q.poll();
            size--;

            int i = 0;
            while (!q.isEmpty() && i < size - 1){
                temp.offer(q.poll());
                i++;
            }

            int last = q.poll();

            if (sum == 0) { sum = first + last; }

            if (first + last != sum){ return false; }

            while(!temp.isEmpty()){q.offer(temp.poll());}
        }

        return true;
    }

    //ב
    //O(n)^2

    //3
    //א
    public static boolean perfectListK(Node<Integer> chain, int k){
        HashMap<Integer,Integer> map = new HashMap<>();
        Node<Integer> pos = chain;

        while (pos != null){
            map.put(pos.getValue(), map.getOrDefault(pos.getValue(), 0) + 1);
            pos = pos.getNext();
        }

        for (Map.Entry<Integer,Integer> entry : map.entrySet()){
            if (entry.getValue() != k){
                return false;
            }
        }

        return true;
    }

    //ב
    //O(n)

    //6
    //א
    //O(n)
    public static int firstPosition(Queue<Integer>q, int num){
        int count = 0;

        while (!q.isEmpty()){
            if (q.poll() != num){
                count++;
            }else {
                count++;
                return count;
            }
        }

        return -1;
    }

    //ב
    //O(n)
    public static int lastPosition(Queue<Integer>q, int num){
        Stack<Integer> stk = new Stack<>();
        Queue<Integer>copy = cloneQueue(q);
        int count = 0;

        while(!copy.isEmpty()){ stk.push(copy.poll()); }

        while (!stk.isEmpty()){
            int n = stk.pop();

            if (n != num){
                count++;
            }else {
                count++;
                return count;
            }
        }

        return -1;
    }

    //ג
    //O(n)^2
    public static boolean isDistanceK(Queue<Integer> q, int k){
        Queue<Integer> copy = cloneQueue(q);
        int sizeQ = q.size();

        while (!copy.isEmpty()){
            int n = copy.poll();
            int first = firstPosition(q, n);
            int last = lastPosition(q, n);
            int dis = Math.abs(first - last);
            if (dis == k){ return true; }
        }

        return false;
    }

    //7
    //א
    //O(n)
    public static int getSum(Node<Integer> p1, Node<Integer> p2){
        int sum = 0;

        while (p1 != p2.getNext()) {
            sum += p1.getValue();
            p1 = p1.getNext();
        }

        return sum;
    }

    //ב
    //O(n)^2
    public static boolean isAmount(Node<Integer> ch, int num){
        int sum = 0;

        while (ch != null){
            Node<Integer> pos = ch;

            while (pos != null && sum <= num){
                sum += pos.getValue();
                pos = pos.getNext();
            }

            if (sum == num){ return true; }

            ch = ch.getNext();
        }

        return false;
    }

    //8
    //א
    //O(n)
    public static int getSumOfStack(Stack<Integer> st){
        Stack<Integer> copyStk = cloneStack(st);
        int sum = 0;

        while (!copyStk.isEmpty()){ sum += copyStk.pop(); }

        return sum;
    }

    //ב
    //O(n)
    public static int getStackLastValue(Stack<Integer> st){
        Stack<Integer> copyStk = cloneStack(st);
        Stack<Integer> temp = new Stack<>();

        while (!copyStk.isEmpty()){ temp.push(copyStk.pop()); }

        return temp.peek();
    }

    //ג
    //O(n)^2
    public static Queue<Integer> doIt(Node<Stack<Integer>> chain){
        Node<Stack<Integer>> pos = chain;
        Queue<Integer> rt = new LinkedList<>();

        while (pos != null){
            Stack<Integer> tempStk = pos.getValue();
            int size = tempStk.size();

            if (size % 2 == 0){
                rt.offer(getSumOfStack(tempStk));
            }else {
                rt.offer(getStackLastValue(tempStk));
            }

            pos = pos.getNext();
        }

        return rt;
    }

    //9 במחברת


    public static int one(Node<Integer> ch, int num) {
        if(ch.getNext()!= null) {
            if(ch.getNext().getValue() == num) {
                ch.setNext(ch.getNext().getNext());
                return 1 + one(ch, num);
            } else {
                ch = ch.getNext();
                return one(ch, num);
            }
        } else {
            return 0;
        }
    }

    public static void two(Node<Integer> ch) {
        if(ch != null) {
            int x = one(ch, ch.getValue());
            ch.setNext(new Node<Integer> (x+1, ch.getNext()));
            ch = ch.getNext();
            two(ch.getNext());
        }
    }

    public static void changeMidQueue(Queue<Integer> q, int x, int y){
        var rnd = new Random();

        var temp = new LinkedList<Integer>();
        var size = q.size();

        if (size % 2 == 0){
            //מונה עד זקיף
            var count = 0;
            //מיקום איבר אמצעי
            var mid = (size / 2) - 1;

            // אם יש רק שני איברים
            if (q.size() == 2){
                //שליפת איבר ראשון
                q.poll();
                //שליפת איבר שני
                q.poll();
                q.offer(x);
                q.offer(y);
            }

            //ריקון הערכים לתור עזר עד האמצע
            while (!q.isEmpty() && count < mid){
                //הכנסה לתור עזר
                temp.offer(q.poll());
                //מנייה כדי לא להכניס את האיבר האמצעי
                count++;
            }

            //שליפת איבר ראשון
            q.poll();
            //שליפת איבר שני
            q.poll();
            //הכנסת X לתור עזר לשיחזור
            temp.offer(x);
            //הכנסת Y לתור עזר לשיחזור
            temp.offer(y);

            //המשך ריקון תור מקור לתור עזר
            while (!q.isEmpty()){ temp.offer(q.poll()); }

            //שיחזור תור מקור
            while (!temp.isEmpty()){ q.offer(temp.poll()); }
        }else {
            //7
            //1 2 3 4 5 6 7
            //הגרלת מס רנדומלי
            var randNum = rnd.nextInt(y - x, 1);
            //מונה עד זקיף
            var count = 0;
            //מיקום איבר אמצעי
            var mid = size / 2;

            //אם יש רק איבר אחד
            if (q.size() == 1){
                //שליפתו
                q.poll();
                //הכנסת מס רנדומלי
                q.offer(randNum);
                //יציאה מהפונקצייה
                return;
            }

            //ריקון הערכים לתור עזר עד האמצע
            while (!q.isEmpty() && count < mid){
                //הכנסה לתור עזר
                temp.offer(q.poll());
                //מנייה כדי לא להכניס את האיבר האמצעי
                count++;
            }

            //הכנסת מס רנדומלי שיחליף את המס האמצעי לתור עזר לשיחזור
            temp.offer(randNum);
            //שליפת איבר אמצעי
            if (!q.isEmpty()){ temp.poll(); }

            //המשך ריקון תור מקור לתור עזר
            while (!q.isEmpty()){ temp.offer(q.poll()); }

            //שיחזור תור מקור
            while (!temp.isEmpty()){ q.offer(temp.poll()); }
        }
    }


    public static void bubbleSortDown(Node<Integer> chain){
        var pos = chain;

        while(pos != null){
            var current = chain;

            while (current.getNext() != null){
                if (current.getValue() < current.getNext().getValue()){
                    var temp = current.getValue();
                    current.setValue(current.getNext().getValue());
                    current.getNext().setValue(temp);
                }

                current = current.getNext();
            }

            pos = pos.getNext();
        }
    }

    public static void buildFreqListn(Node<Integer> chain){
        bubbleSortDown(chain);

        var pos = chain;

        while (pos != null){
            //שמירת ערך נוכחי
            var num = pos.getValue();

            var nex = pos;
            int count = 0;

            while (nex != null && nex.getValue() == num){
                count++;
                nex = nex.getNext();
            }

            //דילוג על ערכים זהים
            pos.setNext(nex);

            //יצירת חולייה המכילה כמה פעים ערך מסויים מופיע בשרשרת
            var toAdd2 = new Node<>(count);
            //חיבור החולייה שמכילה כמה ערך מופיע לחולייה הבאה ברשימה
            toAdd2.setNext(nex);
            pos.setNext(toAdd2);

            pos = pos.getNext();
        }
    }

    public static int mostPopularNumber2(Node<Integer> chain){
        var max = Integer.MIN_VALUE;
        var finalNumber = 0;

        var pos = chain;

        while (pos.getNext() != null){
            var num = pos.getValue();
            var how = pos.getNext().getValue();

            if (how > max){
                max = how;
                finalNumber = num;
            }

            pos = pos.getNext();
        }

        return finalNumber;
    }

    public static void buildFreqListn1(Node<Integer> chain){
        //pointer for iteration
        var pos = chain;

        //iteration on list
        while (pos != null){
            //keep current value
            var n = pos.getValue();
            //another pointer for the current node
            var current = pos;
            //count current value, -> means found at least once
            var count = 1;

            //iteratetion
            while (current.getNext() != null){
                //keep next value
                var nNext = current.getNext().getValue();

                //if next value equals to the current value
                if (nNext == n){
                    //count val
                    count++;
                    //set current node to the sec next node
                    current.setNext(current.getNext().getNext());
                }else {
                    //move to next node
                    current = current.getNext();
                }
            }

            //create new node contains how many current number found in list
            var toAdd = new Node<>(count);
            //connect current new node to next node
            toAdd.setNext(pos.getNext());
            //connect pos the new node
            pos.setNext(toAdd);
            //mv pos twice
            pos = pos.getNext().getNext();
        }
    }

    public static void qOfStr(Queue<String> q){
        var str = ""; //מחרוזת ריקה לבנייה
        var i = 0; //משתנה למיקום תו

        //איטרציה על התור
        while (!q.isEmpty()){
            //אם האינדקס בטווח
            if (i < q.peek().length()){
                //בניית המחרוזת
                str += q.peek().charAt(i);
                //הכנסה מעגלית רק מחרוזות שיש להן תו באינקס I
                q.offer(q.poll());
            }

            else { q.poll(); }

            //קידום האינדקס
            i++;
        }
    }

    //O(n)
    BiFunction<Stack<Integer>, Integer, Integer> fstart = (stk, n) -> {
        //מחסנית עזר לשיחזור מחסנית מקור
        var temp = new Stack<Integer>();
        //מונה עד זקיף
        var count = 0;

        //איטרציה על המחסנית
        while (!stk.isEmpty()){
            //שליפת איבר נוכחי מהמחסנית ושמירת ערכו
            var num = stk.pop();

            //כל עוד לא הגענו למס שמחפשים
            if (num != n){
                //נמנה איבר זה
                count++;
                //נכניס אותו למחסנית שיחזור
                temp.push(num);
            }else {
                //אחרת אם הגענו למס שמחפשים
                //נמנה גם אותו
                count++;
                //נכניס אותו למחסנית שיחזור
                temp.push(num);

                //הנשיך לרוקן מחסנית מקורית
                while (!stk.isEmpty()){ temp.push(stk.pop()); }

                //נשחזר מחסנית מקורית
                while (!temp.isEmpty()){ stk.push(temp.pop()); }

                //נחזיר מיקום המס שחיפשנו
                return count;
            }
        }

        //אם הגענו לכאן משמע שהמס לא נמצא
        //נשחזר מחסנית מקור ונחזיר -1
        while (!temp.isEmpty()){ stk.push(temp.pop()); }

        return -1;
    };

    //O(n)
    BiFunction<Stack<Integer>, Integer, Integer> fend = (stk, n) -> {
        //for each loop iterarte on stack from the bottom
            for (var number : stk){
                if (number != n){
                    count++;
                }else {
                    count++;
                    return count;
                }
            }
        return -1;
    };

    //O(n)^2
    Function<Stack<Integer>, Integer> disBstks = (stk) -> {
        //מחסנית העתק מקור
        var clone = cloneStack(stk);
        //אורך מחסנית מקור
        var size = stk.size();
        //משתנה שיכיל את המרחק הגדול ביותר במחסנית בין שני ערכים זהים
        var max = Integer.MIN_VALUE;

        //איטרציה על המחסנית העתק
        while (!clone.isEmpty()){
            //מס נוכחי
            var n = clone.pop();
            //אינדקס של ערך זה מתחילת המחסנית
            var startIndex = fstart.apply(stk, n);
            //אינדקס של ערך זה מסוף המחסנית
            var endIndex = fend.apply(stk, n);
            //חישוב מרחק בין שני ערכים זהים
            var dis = size - startIndex - endIndex;
            //רק אם ערך מופיע פעמיים נבדוק אם המרחק בין שני ערכים זהים הוא המרחק הגדול ביותר
            if (startIndex != -1 && endIndex != -1){ if (dis > max){ max = dis; } }
        }

        //אם נמצא מרחק שכזה נחזיר אותו, אחרת נחזיר -1
        return max != Integer.MIN_VALUE ? max : -1;
    };

    //O(n)^3
    Function<Queue<Stack<Integer>>, Integer> disBstksInQu = (q) -> {
        //תור לשיחזורים
        var temp = new LinkedList<Stack<Integer>>();
        //משתנה שיכיל את המרחק הגדול ביותר בין שני איברים באחת מהמחסניות שבתור
        var max = Integer.MIN_VALUE;

        //איטרציה על התור
        while (!q.isEmpty()){
            //שליפת מחסנית נוכחית
            var currentStack = q.poll();
            //שמירת מרחק בין שני איברים זהים (אם קיים)
            var maxDisInCurrentStack = disBstks.apply(currentStack);
            //עדכון משתנה מרחק מקסימלי
            if (maxDisInCurrentStack > max){ max = maxDisInCurrentStack; }
            //הכנסת מחסנית נוכחית זו לתור שיחזורים
            temp.offer(currentStack);
        }

        //שיחזור תור מקור
        while (!temp.isEmpty()){ q.offer(temp.poll()); }

        //אם נמצא מרחק שכזה נחזירו, אחרת נחזיר -1
        return max != Integer.MIN_VALUE ? max : -1;
    };

    //O(n)^4
    Function<BinNode<Queue<Stack<Integer>>>, Integer> maxdisBstksInTreeOfQu = (root) -> {
        //queue for breadth first search
        var q = new LinkedList<BinNode<Queue<Stack<Integer>>>>();
        //offer the root to the queue
        q.offer(root);
        //maximum var for keep max distance between two identical vals
        //in BinNode -> Queue -> Stack<Integer>
        var max = Integer.MIN_VALUE;

        while (!q.isEmpty()){
            //remove current BinNode from the queue
            var current = q.poll();// current : BinNode<Queue<Stack<Integer>>>
            //keep pointer to current q
            var cq = current.getValue();// cq : Queue<Stack<Integer>>
            //calc distance between two identical values
            var cdis = disBstksInQu.apply(cq);// cdis : int
            //update max var
            if (cdis > max){ max = cdis; }
            //offer the sons of the current parent to the queue
            //offer left son
            if (current.hasLeft()){ q.offer(current.getLeft()); }
            //offer right son
            if (current.hasRight()){ q.offer(current.getRight()); }
        }

        //return max if exist
        return max != Integer.MIN_VALUE ? max : -1;
    };

    //O(n)
    public static void my(BinNode<Character> root){
        if (root != null){
            System.out.print(root.getValue() + " ");
            my(root.getLeft());
            System.out.print(root.getValue() + " ");
            my(root.getRight());
            System.out.print(root.getValue() + " ");
        }
    }

    //O(n)^2
    Function<Queue<Integer>, Queue<Integer>> sidq = (q) -> {
        //temp queue ffor restoration
        var temp = new LinkedList<Integer>();
        //final queue
        var finalq = new LinkedList<Integer>();

        //iteration on original queue
        while (!q.isEmpty()){
            //poll out current value from queue
            int n = q.poll();
            //offer current value to the final queue
            finalq.offer(n);

            //keep iterate and remove vals from the queue
            while (!q.isEmpty()){
                //poll out current value from queue
                int next = q.poll();

                //if current value equals to the prev value offer it to the final queue
                if (next == n){
                    finalq.offer(next);
                }else {
                    //else offer it to the restoration queue
                    temp.offer(next);
                }
            }

            //restoration
            while (!temp.isEmpty()){
                q.offer(temp.poll());
            }
        }

        //return final queue
        return finalq;
    };

    //O(n)
    BiFunction<Stack<Integer>, Integer, Integer> distBetweenTwoIdenticalVals = (stk, num) -> {
        //מחסנית עזר לשיחזור
        var temp = new Stack<Integer>();
        //חישוב מרחק בין שני מופעים של מס
        var count = 0;
        //משתנה שיגיד אם ערך נמצא פעם ראשונה
        var first = false;
        //משתנה שיגיד עם ערך נמצא פעם שנייה
        var sec = false;

        //איטרציה על מחסנית נוכחית
        while (!stk.empty()){
            int current = stk.pop();
            //כל עוד זה שונה מהערך שהפונקצייה מחפשת
            if (current != num){
                temp.push(current);
            }else{
                first = true;
                //הכנסת המופע הראשון של המס למחסנית עזר לשיחזור
                temp.push(current);

                //הכנסת כל האיברים בין המופע הראשון לשני למחסנית עזר לשיחזור
                while (!stk.empty() && stk.peek() != current){
                    count++;
                    temp.push(stk.pop());
                }

                //הכנסת המופע השני של המספר
                if (!stk.isEmpty()){ temp.push(stk.pop()); sec = true; }

                //המשך ריקון שאר האיברים אחרי המופע השני למחסנית עזר לשיחזור
                while (!stk.isEmpty()){ temp.push(stk.pop()); }

                // שיחזור
                while (!temp.isEmpty()){ stk.push(temp.pop()); }

                // יציאה מהלולאה למציאת מרחק
                break;
            }
        }

        // אם המס נמצא פעמיים
        return first && sec ? count : -1;
    };

    //O(n)^2
    Function<Stack<Integer>, Integer> maxdisinstk = (stk) -> {
        //יכיל את המרחק הגדול ביותר
        var max = Integer.MIN_VALUE;
        var temp = cloneStack(stk);

        while (!temp.isEmpty()){
            // שליפת ערך נוכחי
            var n = temp.pop();
            // מרחק בין שני מופעים של מס
            var dis = distBetweenTwoIdenticalVals.apply(stk, n);

            if (dis != -1) {
                // עדכון המרחק המקסימלי
                if (dis > max) { max = dis; }
            }
        }

        // אם נמצא מרחק שכזה נחזיר אותו, אחרת נחזיר -1
        return max != Integer.MIN_VALUE ? max : -1;
    };

    //O(n)
    Function<Node<Integer> ,Boolean>PerfectList = (chain) -> {
        //יצירת טבלת גיבוב
        var map = new HashMap<Integer, Integer>();
        // פויינטר לשרשרת
        var pos = chain;

        //איטרציה על השרשרת
        while (pos != null){
            //שמירת ערך ראשון
            var currentNum = pos.getValue();

            //הכנסת רשומה המכילה את ה-> המס : כמה פעמים המס מופיע לטבלת גיבוב
            map.put(currentNum, map.getOrDefault(currentNum, 0) + 1);

            //התקדמות לחולייה הבאה
            pos = pos.getNext();
        }

        //מעבר על הטבלת גיבוב וביצוע בדיקה
        for (var entry : map.entrySet()){
            // שמירת ערך נוכחי
            var num = entry.getKey();
            //חישוב כמה פעמים ערך מופיע
            var howMany = entry.getValue();

            //אם הערך חיובי ולא נמצא מס זוגי של פעמים נחזיר שקר,
            // אחרת נבדוק אם הערך שלילי אם הוא לא מופיע מס אי זוגי של פעמים נחזיר שקר
            if (num > 0 && howMany % 2 == 1){
                return false;
            } else if (num < 0 && howMany % 2 == 0) {
                return false;
            }
        }

        // נחזיר אמת הרשימה מקיימת את התנאים
        return true;
    };

    BiFunction<Queue<Integer>, Integer ,Boolean> IsPerfectN = (queue, num) -> {
        //counters array
        var monim = new int[num];
        //restoration queue
        var temp = new LinkedList<Integer>();

        //iteration on the queue
        while (!queue.isEmpty()){
            //remove current value from queue
            var n = queue.poll();
            //add 1 to the array in index: n - 1, in the end it will determine tal all
            //value between 1 to n, found in queue
            monim[n - 1]++;
            //offer it to the temp queue
            temp.offer(n);
        }

        //restoration
        while (!temp.isEmpty()){
            queue.offer(temp.poll());
        }

        //iteration on the counters array
        for(var currentNumber : monim){
            //if there is number that not found at least once in the array return false
            if (currentNumber < 1){
                return false;
            }
        }

        //return true -> valid queue
        return true;
    };

    BiConsumer<Queue<Integer>, Integer> DoItSuper = (q, n) -> {
        //counters array
        var monim = new int[n];
        //restoration queue
        var temp = new LinkedList<Integer>();

        //iteration
        while(!q.isEmpty()){
            //remove current value from queue
            var num = q.poll();

            //if num between 1 and n add 1to the monim array at index[n-1]
            if (num >= 1 && num <= n) {
                monim[num - 1]++;
                //offer it to the restoration queue
                temp.offer(num);
            }
        }

        //restoration
        while (!temp.isEmpty()){
            q.offer(temp.poll());
        }

        //iteration on the counters array
        for(var currentNumber : monim){
            //if there is number that not found at least once in the array return false
            if (currentNumber < 1){
                q.offer(currentNumber);
            }
        }
    };

    //O(n)
    Function<Node<Integer>, Boolean> isSumSortUp = (chain) -> {
        var sum = 0; // צובר ערכים
        var pos = chain; // פויינטר למעבר על השרשרת

        // איטרציה על השרשרת
        while (pos != null){
            var current = pos.getValue(); // שמירת ערך נוכחי

            if (current <= sum){ return false; } // אם הערך הנוכחי קטן מסכום האיברים שלפניו נחזיר שקר

            sum += current; // אחרת נצבור אותו

            pos = pos.getNext(); // נתקדם לחולייה הבאה
        }

        return true; // החזר אמת, שרשרת ממויינת לפי התנאי
    };

    //O(n)^2
    BiFunction<Node<Integer>, Integer, Boolean> buildSumSortUpList = (chain, num) -> {
        //הוספה להתחלה
        if (num < chain.getValue()){
            //ננסה להוסיף נבדוק אם היא עדיין ממויינת אחר כך אם לא נבטל הוספה ונחזיר שקר
            // הוספה להתחלה
            var temp = chain.getValue(); //שמירת ערך של חולייה ראשונה
            chain.setValue(num); //החולייה הראשונה תקבל את הערך להוספה

            //הוספת החולייה החדשה עם הערך של הראשונה, בין הראשונה לשלישית
            chain.setNext(new Node<Integer>(temp, chain.getNext()));

            // אם הרשימה עדיין ממויינת, נחזיר אמת
            if (isSumSortUp.apply(chain)){
                return true;
            }else {
                //אחרת נבטל את הוספת החולייה השנייה
                chain.setValue(temp);
                chain.setNext(chain.getNext());
            }
        }

        var pos = chain; // פויינטר לאיטרציה על השרשרת

        //הוספה לאמצע
        while (pos.getNext() != null){
            pos.setNext(new Node<Integer>(num, pos.getNext())); // חיבור חולייה חדשה באמצע בין שני איברים

            // אם הרשימה עדיין ממויינת, נחזיר אמת
            if (isSumSortUp.apply(chain)){
                return true;
            }else {
                //אחרת נבטל את הוספת החולייה החדשה
                pos.setNext(pos.getNext().getNext());
            }

            pos = pos.getNext(); // נתקדם לחולייה הבאה
        }

        pos.setNext(new Node<Integer>(num)); //הוספה לסוף

        // אם הרשימה עדיין ממויינת, נחזיר אמת
        if (isSumSortUp.apply(chain)){
            return true;
        }else {
            pos.setNext(null); //אחרת נבטל את הוספת החולייה החדשה
        }

        return false; //החזר שקר לא ניתן להוסיף איבר חדש לשרשרת
    };

    // summer 2021 b
    // 1
    //"הוא תור של מספרים שלמים חיוביים שהמספר הראשון בו הוא 1- וכל מספר מופיע בו ברצף,
    //N מספר פעמים השווה לערכו, עד
    Function<Integer, Queue<Integer>> torNq = (num) -> {
        var q = new LinkedList<Integer>(); // תור לבנייה

        // איטרציה מ 1 עד N
        for (int i = 1; i <= num; i++) {
            var j = 0; // משתנה עזר לריצה עד זקיף

            while (j < i){
                q.offer(i); // הכנסה לתור את הערך I
                j++; // קידום תוכן משתנה עזר
            }
        }

        return q; // החזרת התור החדש
    };

    // האם תור הוא תור מדרגה N
    BiFunction<Queue<Integer>, Integer, Boolean> istorNq = (q,num) -> {
        // אם הערך N
        //נמצא בפנים
        var numInsideQueue = false;

        // איטרציה על התור שהתקבל בפונקציה כפרמטר
        while (!q.isEmpty()){
            var number = q.poll(); // שליפת ערך נוכחי
            var j = 0; // משתנה עזר לריצה עד זקיף

            // אם הערך N
            //נמצא בפנים, אזי ערך המשתנה הבוליאני ישתנה לאמת
            if (number == num) { numInsideQueue = true; }

            // המשך איטרציה וריקון התור כל עוד הערך הנוכחי שווה ל NUMBER
            while (!q.isEmpty() && j < number){
                // אם האיבר הנוכחי לא שווה לנאמבר משמע תור זה הוא לא תור מדרגה אן ולכן נחזיר שקר
                if (q.poll() != number){ return false; }
                j++;
            }

            // אם משתנה עזר עד זקיף בסופו של דבר ערכו לא הגיע לנאמבר, משמע זה הוא לא תור מדרגה אן ולכן יוחזר שקר
            if (q.isEmpty() && j != number){ return false; }
        }

        return numInsideQueue; // יחזור אמת אם גם הערך נאמבר נמצא בפנים וגם התור מדרגה אן
    };

    // 2
    // "שרשרת סכומים" היא שרשרת של מספרים שלמים שכל מספר בה )פרט לראשון( הוא סכום של המספרים אשר
    //מופיעים לפניו.
    Function<Node<Integer>, Boolean> suml = (chain) -> {
        var sum = 1; // צובר איברים

        chain = chain.getNext(); // דילוג על האיבר הראשון

        // איטרציה על הרשימה
        while (chain != null){
            var current = chain.getValue(); // שמירת ערך נוכחי

            // אם ערך האיבר הנוכחי שונה מסכום האיברים הקודמים, יוחזר שקר
            if (current != sum) { return false; }

            sum += current; // צבירת ערך נוכחי

            chain = chain.getNext(); // מעבר לחוליה הבאה ברשימה
        }

        return true; // אם לא חזר שקר עד כו משמע הרשימה עונה על התנאים ולכן יוחזר אמת
    };

    // "שרשרת סכומים הפוכה" היא שרשרת של מספרים שלמים שכל מספר בה )פרט לאחרון( הוא סכום של
    //המספרים אשר מופיעים אחריו
    Function<Node<Integer>, Boolean> suma = (chain) -> {
        // איטרציה על הרשימה
        while (chain.getNext() != null){
            var current = chain.getValue(); // שמירת ערך נוכחי
            var sum = 1; // צובר איברים
            var pos = chain.getNext(); // פוינטר לחולייה הבאה ברשימה

            // איטרציה על שאר הרשימה וסכימת כל האיברים שאחרי האיהר הנוכחי
            while (pos != null){
                sum += pos.getValue(); // צבירת ערך נוכחי
                pos = pos.getNext(); // התקדמות לחולייה הבאה
            }

            // אם ערך האיבר הנוכחי שונה מסכום האיברים הבאים אחריו, יוחזר שקר
            if (current != sum) { return false; }

            chain = chain.getNext(); // מעבר לחוליה הבאה ברשימה
        }

        return true; // יוחזר אמת משמע עבור כל איבר ברשימה יבואו אחריו איברים שסכומם שווה לסכום האיבר הנוכחי
    };

    // 2022 spring a
    // 1
    Consumer<Node<Integer>> setF2 = (chain) -> {
        // שיכפול רשימה באופן הבא
        // [1,2,3] -> [1,1,2,2,3,3]
        while (chain != null){
            chain.setNext(new Node<>(chain.getValue(), chain.getNext())); // הוספת חולייה באמצע בין שני חוליות
            chain = chain.getNext().getNext(); // התקדמות לחולייה אחרי החולייה החדשה שנוספה
        }
    };

    Consumer<Node<Integer>> setF2sec = (chain) -> {
        // שיכפול רשימה באופן הבא
        // [1,2,3] -> [1,2,3,1,2,3]
        Node<Integer> newey = null; // פויינטר לרשימה החדשה
        Node<Integer> tail = null; // פויינטר לזנב הרשימה החדשה
        var prev = chain; // פויינטר לזנב השרשרת המקורית

        // איטרציה על הרשימה המקורית
        while (chain != null){
            var toAdd = new Node<>(chain.getValue()); // יצירת חולייה חדשה

            // אם רשימה חדשה ריקה, ראש הרשימה יהיה החולייה החדשה שנוצרה כרגע
            if (newey == null){
                newey = toAdd; // פויינטר הראש של הרשימה החדשה יצביע על חולייה זו
                tail = newey; // קישור פויינטר הזנב אלייה
            }else {
                tail.setNext(toAdd); // תתווסף חולייה דרך זנב השרשרת החדשה
                tail = tail.getNext(); // יקודם פויינטר הזנב של הרשימה החדשה אלייה
            }

            prev = chain; // פויינטר הזנב של רשימת המקור יצביע על החולייה הנוכחית
            chain = chain.getNext(); // קידום הפויינטר לחולייה הבאה
        }

        prev.setNext(newey); // חיבור פויינטר זנב השרשרת המקורית לראש הרשימה החדשה
    };

    // 2
    Function<Stack<Integer>, Integer> calcAvgOfStack = (stk) -> {
        // כשצריך לצבור/לשנות ערך מחוץ ל־lambda
        // לא ניתן להשתמש במשתנה int בתוך למבדא, ולכן משתמשים במחלקה עוטפת למס שלם
        AtomicInteger sum = new AtomicInteger(); // צובר

        stk.forEach( (num) -> {
            sum.addAndGet(num);
        } ); // צבירת ערכים במחסנית

        return sum.get() / stk.size(); // החזרת ממוצע הערכים במחסנית
    };

    Consumer<Stack<Integer>> sortByAvgSplitStack  = (stk) -> {
        var bigThanAvg = new Stack<Integer>(); // מחסנית עזר עבור הערכים הגדולים מהממוצע
        var smlThanAvg = new Stack<Integer>(); // מחסנית עזר עבור הערכים הקטנים מהממוצע
        var avg = calcAvgOfStack.apply(stk); // חישוב ממוצע מחסנית

        // איטרציה על מחסנית מקור
        stk.forEach((num) -> {
            if (num > avg) bigThanAvg.push(num); // אם ערך גדול מהממוצע הוא יוכנס למחסנית הערכים הגדולים מהממוצע
            else smlThanAvg.push(num);// אחרת יוכנס למחסנית המכילה ערכים הקטנים מהממוצע
        });

        stk.clear(); // ניקוי תוכן מחסנית מקור

        //העברת תוכן מחסנית עזר של הערכים הקטנים מהממוצע למחסנית מקור
        smlThanAvg.forEach((num ) -> {
            stk.push(num);
        } );

        smlThanAvg.clear(); // ניקוי תוכן מחסנית העזר המכילה ערכים קטנים מהממוצע

        // ולאחריהם העברת תוכן המחסנית עזר של הערכים הקטנים מהממוצע למחסנית מקור
        bigThanAvg.forEach((num) -> {
            stk.push(num);
        } );

        bigThanAvg.clear(); // ניקוי תוכן מחסנית העזר המכילה ערכים גדולים מהממוצע
    };

    // 6
    Function<Node<Integer>, Node<Double> > averageList = (lst) -> {
        Node<Double> avglst = null; // pointer for head of the new list
        Node<Double> tail = null; // pointer for tail of the new list
        var pos = lst; // pointer for iteration on list
        var count = 0.0; // count nodes
        var sum = 0.0; // sum up the values

        //       pos  pos   pos   pos   pos   pos   pos   pos   pos   pos
        // lst-> 80 -> 40 -> 90 -> -1 -> 95 -> -1 -> 85 -> 80 -> -1 -> null

        //                           tail
        // avglst -> 70.0 -> 95.0 -> 82.5 -> null

        // iteration
        while (pos != null){
            var current = pos.getValue(); // current value

            // if current node value != -1
            if (current != -1){
                sum += current; // sum it
                count++; // count current node
            }else {
                if (sum == 0 || count == 0){ continue; } // for not putting 0 avg in the new list

                // else if current node value != -1
                //create new avg node
                var toAdd = new Node<Double>(sum / count);

                // add it to the list
                if (avglst == null){
                    avglst = toAdd;
                    tail = avglst;
                }else {
                    tail.setNext(toAdd);
                    tail = tail.getNext();
                }

                count = 0; // reset the counter
                sum = 0; // reset sum
            }

            pos = pos.getNext(); // mv to the next node
        }

        // return the new average list
        return avglst;
    };

    Consumer<Node<Integer>> print = (lst) -> {
        var avglist = averageList.apply(lst);

        Node<Double> newavglst = null; // pointer for head of the new list
        Node<Double> newtail = null; // pointer for tail of the new list
        var pos = lst; // pointer for iteration on list
        var count = 0.0; // count nodes
        var sum = 0.0; // sum up the values
        var min = Integer.MAX_VALUE;

        // iteration
        while (pos != null){
            var current = pos.getValue(); // current value

            // if current node value != -1
            if (current != -1){

                if (current < min){ min = current; } // keep the minimun value between (-1) -> ... -> (-1)

                sum += current; // sum it
                count++; // count current node
            }else {

                //if there is more than 1 value between (-1) -> ... -> (-1),so it can comlete this specific procidure
                if (count > 1){
                    sum -= min; // remove the minimun grade from the sum var
                    count--; // dicrease the counter by 1
                }

                // else if current node value != -1
                //create new avg node
                var toAdd = new Node<Double>(sum / count);

                // add it to the list
                if (newavglst == null){
                    newavglst = toAdd;
                    newtail = newavglst;
                }else {
                    newtail.setNext(toAdd);
                    newtail = newtail.getNext();
                }

                count = 0; // reset the counter
                sum = 0; // reset sum
                min = Integer.MAX_VALUE; // reset min var
            }

            pos = pos.getNext(); // mv to the next node
        }

        // counter for the student number
        var studentIndex = 1;
        // iterate on both avglist (the old one and the new one) and print the diffrencess
        while (avglist != null && newavglst != null){
            var avgBefore = avglist.getValue(); // old avg
            var avgAfter = newavglst.getValue(); // new avg
            // print the results
            System.out.println(
                    "student number: " + studentIndex +
                    ", before: " + avgBefore +
                    ", after: " + avgAfter
            );
            studentIndex++; // add 1 to the students counter
            avglist = avglist.getNext(); // mv to the next old student avg
            newavglst = newavglst.getNext(); // mv to the next new student avg
        }
    };

    Runnable rprintres = () -> {
        // יצירת שרשרת: 80 -> 40 -> 90 -> -1 -> 95 -> -1 -> 85 -> 80 -> -1 -> null
        var lst = new Node<>(80,
                new Node<>(40,
                        new Node<>(90,
                                new Node<>(-1,
                                        new Node<>(95,
                                                new Node<>(-1,
                                                        new Node<>(85,
                                                                new Node<>(80,
                                                                        new Node<>(-1, null)))))))));

        // הרצת הפונקציה
        print.accept(lst);
    };

    //2022 summer a
    //2 O(n)
    Function<Stack<Integer>, Boolean> bigThanBefore = (stack) -> {
        var clone = copyStack(stack);
        var sum = 0;

        while (!clone.empty()){
            if (clone.peek() <= sum){ return false; }
            sum += clone.pop();
        }

        return true;
    };

    public static void addly(Node<Integer> chain, int n, int pos) {
        // case: add at the start of the chain
        if (pos == 1) {
            // save the original first value
            var firstOriginal = chain.getValue();
            // replace the value of the first node with the new value
            chain.setValue(n);
            // create a new node with the old value and set it as next
            chain.setNext(new Node<>(firstOriginal, chain.getNext()));
            return;
        }

        // case: add in between and at the end
        var current = chain;   // pointer to traverse the chain
        var tail = current;    // pointer to remember the last node visited
        var count = 0;         // counter for the current position
        while (current != null) {
            // if the next position is the desired one
            if (count + 1 == pos) {
                // insert new node after the current node
                current.setNext(new Node<>(n, current.getNext()));
                return;
            }
            // otherwise, move to the next node
            count++;
            current = current.getNext();
        }

        // case: add at the end (if pos is larger than the chain length)
        tail.setNext(new Node<>(n));
    }

    //---------------------------------------------------------------------------------------------------------------//

    //מפתח הצפנה
    private static final String secret = "1234567890123456";

    public static void main (String[]args)throws Exception{
        var scanner = new Scanner(System.in);

        System.out.println("do you want run main function? Y/N");
        var ansyM1 = scanner.next();

        if (ansyM1.equalsIgnoreCase("y")) {
        /*
         Runnable (interface) -  ("ניתנת להרצה").
         Thread (class) - משמש להרצת המשימה (Runnable) בשירשור נפרד.
         synchronized (methoud)- משמש למנוע התנגשויות בגישה לנתונים משותפים (ספריות למשל) כשהמשימות רצות בשירשורים.
         */
            var q = new LinkedList<Runnable>();//תור מטיפוס (פונקציות [להרצה])
            //swing
            q.offer(() -> {
                System.out.println("Swing tool");
                System.out.println("Do you want to see visual Java in action? Y/N");
                String ansy = scanner.next();

                if (ansy.equalsIgnoreCase("y")) {
                    SwingUtilities.invokeLater(() -> {
                        //create frame
                        JFrame frame = new JFrame("change background color");
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.setSize(500, 500);
                        frame.setLocationRelativeTo(null);
                        //--------------------------------------------------------//

                        //colors array
                        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE, Color.CYAN};
                        final int[] colorIndex = {0}; // שימוש במערך כדי לאפשר שינוי ערך בתוך ה-Lambda
                        //--------------------------------------------------------//

                        //create button, listener
                        JButton button = new JButton("change color");
                        button.addActionListener(e -> {
                            frame.getContentPane().setBackground(colors[colorIndex[0]]); // change background color
                            colorIndex[0] = (colorIndex[0] + 1) % colors.length; // update the index
                        });
                        frame.setLayout(new BorderLayout());
                        frame.add(button, BorderLayout.SOUTH); // הוספת הכפתור בתחתית החלון
                        frame.setVisible(true);
                        //--------------------------------------------------------//

                        //lable for timer
                        JLabel till = new JLabel();
                        till.setFont(new Font("Arial", Font.BOLD, 60));
                        till.setHorizontalAlignment(SwingConstants.CENTER);
                        //add lable-timer to the frame
                        frame.add(till);
                        frame.setVisible(true);
                        //create new Time object, put it in the lable
                        new Timer(1000, e -> {
                            String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
                            till.setText(time);
                            till.setForeground(till.getForeground() == Color.PINK ? Color.GRAY : Color.PINK);
                        }).start();
                        //--------------------------------------------------------//
                    });
                }
                System.out.println("-------------------------------------------------------");
            });
            //file
            q.offer(() -> {
                //some os / user details
                System.out.println("System details: ");
                System.out.println("OS: " + System.getProperty("os.name"));
                System.out.println("OS Version: " + System.getProperty("os.version"));
                System.out.println("Architecture: " + System.getProperty("os.arch"));
                System.out.println("User: " + System.getProperty("user.name"));
                System.out.println("Java Version: " + System.getProperty("java.version"));
                long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
                System.out.println("JVM Uptime: " + uptime + " ms");
                System.out.println("----------------------------------------------------------");

                System.out.println("⚠️do you want to add this current run-details to the logger? Y/N");
                String Ans = scanner.next();
                if (Ans.equalsIgnoreCase("Y")) {
                    System.out.println("Logger:");

                    try {
                        //פויינטר לאובייקט קובץ
                        //File file = new File("C:\\Users\\sharb\\Desktop\\myfile.txt");
                        String userHome = System.getProperty("user.home");
                        File file = new File(userHome + File.separator + "Desktop" + File.separator + "myfile.txt");

                        //בדיקה אם הקובץ קיים
                        if (file.exists()) {
                            System.out.println("✅ File exists!");
                            System.out.println("do you want to see the logger file details? Y/N: ");
                            String ans = scanner.next();

                            if (ans.equalsIgnoreCase("Y")) {
                                System.out.println("📂 File name: " + file.getName() + ";");
                                System.out.println("📍 Absolute path: " + file.getAbsolutePath() + ";");
                                System.out.println("📏 Size (bytes): " + file.length() + ";");
                                System.out.println("✍️ Writable? " + file.canWrite() + ";");
                                System.out.println("📖 Readable? " + file.canRead() + ";");
                                System.out.println("⚙️ can be executed? " + file.canExecute() + ";");
                                Date date = new Date(file.lastModified());
                                System.out.println("✍️ last modified? " + date + ";");
                                System.out.println("📍 is file hidden? " + Files.isHidden(Paths.get(file.getAbsolutePath())) + ";");
                                System.out.println("📏 is this file a regular file? " + Files.isRegularFile(Paths.get(file.getAbsolutePath())) + ";");
                                System.out.println("📂 is it folder maybe? " + Files.isDirectory(Paths.get(file.getAbsolutePath())) + ";");
                            }
                        } else {
                            // Create the file if it does not exist
                            if (file.createNewFile()) {
                                System.out.println("📁 File successfully created.");
                            }
                        }

                        // קביעת הרשאות
                        System.out.println();
                        System.out.println("do you want to change the accesbility of this file? Y/N: ");
                        String ans3 = scanner.next();

                        if (ans3.equalsIgnoreCase("Y")) {
                            System.out.println("you can try enter your password only 5 times!");
                            System.out.println("enter your uniqe pass (ID): ");
                            String pass = scanner.next();

                            int i = 1;
                            while (!pass.equals("321518987") && i < 5) {
                                i++;
                                System.out.println("try again!!!");
                                System.out.println("enter your uniqe pass (ID): ");
                                pass = scanner.next();
                            }

                            if (pass.equals("321518987")) {

                                System.out.println("e -> enable || d -> disable");

                                System.out.println("⚠️do you want to disable/enable 'read-file' feature?: ");
                                String anst1 = scanner.next();
                                if (anst1.equalsIgnoreCase("e")) {
                                    file.setReadable(true);
                                } else if (anst1.equalsIgnoreCase("d")) {
                                    file.setReadable(false);
                                }

                                System.out.println("⚠️do you want to disable/enable 'write-file' feature?: ");
                                String anst2 = scanner.next();
                                if (anst2.equalsIgnoreCase("e")) {
                                    file.setWritable(true);
                                } else if (anst2.equalsIgnoreCase("d")) {
                                    file.setWritable(false);
                                }

                                System.out.println("⚠️do you want to disable/enable 'execute-file' feature?: ");
                                String anst3 = scanner.next();
                                if (anst3.equalsIgnoreCase("e")) {
                                    file.setExecutable(true);
                                } else if (anst3.equalsIgnoreCase("d")) {
                                    file.setExecutable(false);
                                }

                                // הצגת ההרשאות החדשות
                                System.out.println("\n✅ File permissions updated:");
                                System.out.println("📖 Readable: " + file.canRead());
                                System.out.println("✍️ Writable: " + file.canWrite());
                                System.out.println("⚙️ Executable: " + file.canExecute());

                            } else {
                                System.out.println("⚠️ wrong password, your access to this feature denied!!");
                            }
                        }

                        //קבלת תאריך ושעה נוכחים
                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String formattedDateTime = now.format(formatter);

                        //כתיבה ללוגר הרצות
                        FileWriter w1 = new FileWriter(file, true);
                        w1.write("Ran in: " + formattedDateTime + "\n");
                        w1.close();

                        //בניית רשימת תאריכים מהקובץ תאריכים
                        List<String> dates = new ArrayList<>();
                        Scanner reader = new Scanner(file);
                        // קריאה והוצאת התאריכים
                        while (reader.hasNextLine()) {
                            String line = reader.nextLine();
                            if (line.startsWith("Ran in: ")) {
                                dates.add(line.substring(8, 18)); // חיתוך התאריך בלבד (YYYY-MM-DD)
                            }
                        }
                        reader.close();

                        //הצגת תאריך אחרון ששונה הקובץ
                        System.out.println();
                        System.out.print("do you want to see the last date-time that the file got updated? Y/N: ");
                        String ans4 = scanner.next();

                        if (ans4.equalsIgnoreCase("Y")) {
                            List<String> temp = Files.readAllLines(Paths.get(file.getAbsolutePath()));
                            if (temp.size() >= 2) {
                                System.out.println("🕒 Last recorded date: " + temp.get(temp.size() - 2));
                            } else {
                                System.out.println("⚠️ Not enough data!");
                            }
                        }

                        // קריאה מקובץ
                        System.out.println();
                        System.out.print("do you want to see the exec logger content? Y/N: ");
                        String ans = scanner.next();

                        if (ans.equalsIgnoreCase("Y")) {
                            System.out.println();
                            Scanner reader1 = new Scanner(file);
                            while (reader1.hasNextLine()) {
                                String line = reader1.nextLine();
                                System.out.println(line);
                            }
                            reader1.close();
                        }

                        //מציאת התאריך הפופולרי ביותר
                        System.out.println();
                        System.out.print("do you want to see the most frequent date? Y/N: ");
                        String ans2 = scanner.next();

                        if (ans2.equalsIgnoreCase("Y")) {
                            int maxCount = 0;
                            String popularDate = null;

                            for (int i = 0; i < dates.size(); i++) {
                                String currentDate = dates.get(i);
                                int count = 0;

                                // Count occurrences of the current date
                                for (int j = 0; j < dates.size(); j++) {
                                    if (dates.get(j).equals(currentDate)) {
                                        count++;
                                    }
                                }

                                // Check if this date is the most frequent one
                                if (count > maxCount) {
                                    maxCount = count;
                                    popularDate = currentDate;
                                }
                            }

                            if (popularDate != null) {
                                //הצגת המידע המבוקש למשתמש
                                System.out.println("📌 Most frequent date: " + popularDate + ", Occurred " + maxCount + " times.");
                            }
                        }

                        System.out.println();
                        System.out.println("want more info about class File? Y/N");
                        String an = scanner.next();
                        if (an.equalsIgnoreCase("Y")) {
                            Desktop desktop = Desktop.getDesktop();
                            desktop.browse(new URI("https://docs.oracle.com/javase/8/docs/api/java/io/File.html"));
                        }

                    } catch (IOException e) {
                        System.out.println("❌ Error: " + e.getMessage());
                    } catch (URISyntaxException ex) {
                        throw new RuntimeException(ex);
                    }

                    System.out.println("⚠️Do you want encrypt this logger file? Y/N⚠️");
                    String ans = scanner.next();

                    if (ans.equalsIgnoreCase("y")) {
                        // קובץ המקור להצפנה
                        String userHome = System.getProperty("user.home");
                        File inputFile = new File(userHome + File.separator + "Desktop" + File.separator + "myfile.txt");

                        // קובץ מוצפן שייווצר
                        File encryptedFile = new File(userHome + File.separator + "Desktop" + File.separator + "encrypted.dat");

                        //קובץ שיווצר לאחר הפענוח
                        File decryptedFile = new File(userHome + File.separator + "Desktop" + File.separator + "decrypted.txt");

                        try {
                            // שלב הצפנה
                            MyEncrypt.EncryptOrDecryptAnyFileUsingCipher(Cipher.ENCRYPT_MODE, secret, inputFile, encryptedFile);
                            // שלב הפענוח
                            MyEncrypt.EncryptOrDecryptAnyFileUsingCipher(Cipher.DECRYPT_MODE, secret, encryptedFile, decryptedFile);

                            System.out.println("finished encryption-decryption proccess!");
                        } catch (Exception e) {
                            System.err.println("error: " + e.getMessage());
                        }
                    }
                }


                System.out.println("⚠️do you to blast a Folder in your desktop with a lot of files?⚠️");
                String ans = scanner.next();

                if (ans.equalsIgnoreCase("Y")) {
                    File dir = new File("C:/Users/sharb/Desktop/myTestDir");
                    dir.mkdirs(); // יוצר את התיקייה אם לא קיימת

                    // יצירת 20 קבצי טקסט עם תוכן לדוגמה
                    for (int i = 1; i <= 20; i++) {
                        File file = new File(dir, "file" + i + ".txt");
                        try {
                            file.createNewFile();
                            FileWriter w2 = new FileWriter(file, true);
                            w2.write("hello" + "\n");
                            w2.close();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    System.out.println("✅files created in folder: " + dir.getAbsolutePath());

                    // רשימת כל קבצי הטקסט מהתיקייה
                    File[] txtFiles = dir.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File pathname) {
                            return pathname.isFile() && pathname.getName().endsWith(".txt");
                        }
                    });

                    for (File f : txtFiles) {
                        System.out.println("• " + f.getName());
                    }

                    System.out.println();
                    System.out.println("⚠️do you want to delete all these files?⚠️");
                    String dele = scanner.next();

                    if (dele.equalsIgnoreCase("Y")) {
                        File[] txtFls = dir.listFiles(f -> f.isFile() && f.getName().endsWith(".txt"));

                        for (File f : txtFls) {
                            f.delete();
                        }
                    }
                }
                System.out.println("----------------------------------------------------------");
            });
            //hashmap
            q.offer(() -> {
                System.out.println("HashMap practice:");
                System.out.println("function 1: build string from tree using preOrder recursive function");
                // עץ 1: A -> B, C
                BinNode<Character> t1 = new BinNode<>('A');
                t1.setLeft(new BinNode<>('B'));
                t1.setRight(new BinNode<>('C'));

                // עץ 2: D -> E, F
                BinNode<Character> t2 = new BinNode<>('D');
                t2.setLeft(new BinNode<>('E'));
                t2.setRight(new BinNode<>('F'));

                // עץ 3 - העתק של עץ 1
                BinNode<Character> t3 = new BinNode<>('A');
                t3.setLeft(new BinNode<>('B'));
                t3.setRight(new BinNode<>('C'));

                // בניית שרשרת
                Node<BinNode<Character>> n1 = new Node<>(t1);
                Node<BinNode<Character>> n2 = new Node<>(t2);
                Node<BinNode<Character>> n3 = new Node<>(t3);
                n1.setNext(n2);
                n2.setNext(n3);

                System.out.println("function 2: return the longest string in chain of trees");
                System.out.println("Longest string: " + maxString(n1));  // צפוי: ABC או DEF

                System.out.println("function 3: check if there is tree in linkedlist that shown twice");
                System.out.println("(hashmap solution) Any repeated tree? " + anyStringRepeat(n1));  // צפוי: true כי t1 == t3 במבנה ובערכים
                System.out.println("----------------------------------------------------------");
            });
            //Threads
            q.offer(() -> {
                System.out.println("Active threads section?");
                String answer = scanner.next();

                if (answer.equalsIgnoreCase("y")) {
                    //how many threads active now in main
                    System.out.println("how many threads active now in main" + Thread.activeCount());

                    //change current thread main
                    Thread.currentThread().setName("main");

                    //update current thread priority
                    Thread.currentThread().setPriority(10);

                    //check if this thread is alive
                    boolean alive = Thread.currentThread().isAlive();

                    //create second mini thread inside main thread
                    MyThread t2 = new MyThread();

                    //start second thread inside of thread main
                    t2.start();

                    //print if second thread is daemon
                    System.out.print("if this thread is a diamond" + t2.isDaemon());

                    //put the second thread as daemon
                    t2.setDaemon(true);

                    System.out.println("create and run two more threads - caution code not completed can cause errors?");
                    String ttread = scanner.next();

                    if (ttread.equalsIgnoreCase("y")) {

                        Thread a1 = new Thread() {
                            @Override
                            public void run() {
                                super.run();
                            }
                        };

                        Thread a2 = new Thread() {
                            @Override
                            public void run() {
                                super.run();
                            }
                        };

                        a1.start();
                        a2.start();

                        try {
                            a2.join();
                            a1.join();
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
                System.out.println("----------------------------------------------------------");
            });
            //Test for resetGfile function
            q.offer(() -> {
                System.out.println("Test for resetGfile function: ");
                // יצירת 5 סטודנטים עם קודים שונים
                StudentG s1 = new StudentG(123456789, 90); // קוד: 45
                StudentG s2 = new StudentG(987654321, 65); // קוד: 54
                StudentG s3 = new StudentG(112233445, 75); // קוד: 33
                StudentG s4 = new StudentG(556677889, 82); // קוד: 77
                StudentG s5 = new StudentG(999888777, 95); // קוד: 88

                // הכנסנו את כולם לאותו תא [0] – כלומר מקום לא נכון
                Node<StudentG> chain = new Node<>(s1, new Node<>(s2, new Node<>(s3, new Node<>(s4, new Node<>(s5)))));

                // יצירת מערך עם תא אחד לא תקני
                Node<StudentG>[] grades = new Node[100];
                grades[0] = chain;

                // יצירת מחלקת GradesFile עם המערך
                GradesFile file = new GradesFile(grades);

                System.out.println("📦 לפני reset:");
                for (int i = 0; i < grades.length; i++) {
                    if (grades[i] != null) {
                        System.out.println("Index " + i + ": " + grades[i]);
                    }
                }

                // קריאה לפונקציה שמארגנת מחדש לפי קוד
                resetGfile(file);

                // הדפסת המצב החדש
                System.out.println("\n📦 אחרי reset:");
                Node<StudentG>[] fixed = file.getGrades();
                for (int i = 0; i < fixed.length; i++) {
                    if (fixed[i] != null) {
                        System.out.print("Index " + i + ": ");
                        Node<StudentG> p = fixed[i];
                        while (p != null) {
                            System.out.print(p.getValue() + " → ");
                            p = p.getNext();
                        }
                        System.out.println("null");
                    }
                }
                System.out.println("----------------------------------------------------------");
            });
            //BinNode question...
            q.offer(() -> {
                BinNode<Integer> a = new BinNode<>(3);
                BinNode<Integer> b = new BinNode<>(3);
                a.setLeft(b);
                BinNode<Integer> c = new BinNode<>(1);
                b.setLeft(c);
                BinNode<Integer> d = new BinNode<>(3);
                b.setRight(d);
                BinNode<Integer> e = new BinNode<>(2);
                d.setLeft(e);


                BinNode<Integer> f = new BinNode<>(10);
                a.setRight(f);
                BinNode<Integer> g = new BinNode<>(9);
                f.setLeft(g);
                BinNode<Integer> h = new BinNode<>(7);
                g.setLeft(h);

                BinNode<Integer> i = new BinNode<>(7);
                f.setRight(i);
                BinNode<Integer> j = new BinNode<>(10);
                i.setRight(j);
                what1(a);
                System.out.println("----------------------------------------------------------");
            });
            //class Doctor...
            q.offer(() -> {
                var d = new Doctor[9];
                d[0] = new Doctor("Dr. Cohen", "Cardiology",12);
                d[1] = new Doctor("Dr. Levy", "Neurology");
                d[2] = new Doctor("Dr. Sharon", "Pediatrics",8);
                d[3] = new Intern("Dani", "Cardiology", new Doctor(d[0]));
                d[4] = new Intern("Yael", "Surgery", d[0]);
                d[5] = new Intern("Avi", "Pediatrics", new Doctor(d[2]));
                d[6] = new Intern("Ruth", "Oncology", d[2]);
                d[7] = new Intern("Noam", "Cardiology", new Doctor(d[1]));
                d[8] = new Intern("Maya", "Neurology", new Doctor(d[0]));

                for (int i=0; i<d.length; i++) { System.out.println(d [i]); }

                d[0] = new Doctor("Dr. Goldman","Neurology",20);
                d[2].setName("Dr. Galper");
                d[2].addPatients(100);
                d[3].addPatients(200);
                d[5].addPatients(100);

                System.out.println("After change:");
                for (int i=0; i<d.length; i++) { System.out.println(d[i]); }

                System.out.println("----------------------------------------------------------");
            });
            //upgrade Object
            q.offer(() -> {
                System.out.println("add speed field to local Object var: ");
                var b = new Object() {
                    //field
                    volatile int speed;
                    //setter
                    public void setSpeed(int val) { this.speed = val; }
                    //getter
                    public int getSpeed() { return this.speed; }
                };

                // עכשיו משתנה b מוגדר לפי טיפוס אנונימי (לא Object)
                b.setSpeed(100);
                System.out.println("speed of class ;) is: " + b.getSpeed() + "!");
                System.out.println("----------------------------------------------------------");
            });
            //.forEach()
            q.offer(() -> {
                List<Integer> numbers = Arrays.asList(80, 90, 95, 100, 105);

                numbers.forEach((n) -> {
                    if (n % 2 == 0) {
                        System.out.println(n + " is even");
                    }
                });
                System.out.println("----------------------------------------------------------");
            });
            //rec calls on charc tree
            q.offer(() -> {
                var a = new BinNode<>('a');
                var b = new BinNode<>('b');
                a.setLeft(b);
                var c = new BinNode<>('c');
                a.setRight(c);
                var d = new BinNode<>('d');
                b.setRight(d);
                var e = new BinNode<>('e');
                c.setLeft(e);
                var f = new BinNode<>('f');
                e.setLeft(f);
                my(a);
                System.out.println();
                System.out.println("----------------------------------------------------------");
            });
            //bsortQu
            q.offer(() -> {
                Function<Queue<Integer>, Integer> minInQueue = (queue) -> {
                    //temp queue for restoration
                    var temp = new LinkedList<Integer>();
                    //will contain the min val in queue
                    var min = Integer.MAX_VALUE;

                    //iteration and find min
                    while (!queue.isEmpty()){
                        //current val
                        var num = queue.poll();
                        //offer it to the temp queue
                        temp.offer(num);

                        if (num < min){ min = num; }
                    }

                    //restoration
                    while (!temp.isEmpty()){ queue.offer(temp.poll()); }

                    //remove min val from queue
                    while (!queue.isEmpty()){
                        //keep current val
                        var num = queue.poll();

                        if (num == min){
                            //num here get lost//
                            //keep iteration mv all vals from riginal queue to the restoration queue
                            while (!queue.isEmpty()){ temp.offer(queue.poll()); }

                            //restoration
                            while (!temp.isEmpty()){ queue.offer(temp.poll()); }

                            break;
                        }else{
                            // num != min ? offer it to the temp queue
                            queue.offer(num);
                        }
                    }

                    //return min val
                    return min;
                };

                Consumer<Queue<Integer>> bsortQueueByMin = (queue) -> {
                    var finalq = new LinkedList<Integer>();

                    while (!queue.isEmpty()){
                        //call function that find the min val in queue remove it and return it
                        var minNum = minInQueue.apply(queue);
                        finalq.offer(minNum);
                    }

                    //restoration
                    while (!finalq.isEmpty()){ queue.offer(finalq.poll()); }
                };

                Consumer<Queue<Integer>> bsortQueue = (queue) -> {
                    var arr = new LinkedList<Integer>(); //copy queue to list
                    var i = 0; //index for the list
                    while (!queue.isEmpty()){ arr.add(i++, queue.poll()); } //empty queue fill the list

                    //bubble sort
                    for (var j = 0; j < arr.size() - 1; j++) {
                        for (var k = 0; k < arr.size() - j - 1; k++) {
                            if (arr.get(k) > arr.get(k+1)){
                                var temp = arr.get(k);
                                arr.set(k, arr.get(k+1));
                                arr.set(k+1, temp);
                            }
                        }
                    }

                    arr.forEach( (num) -> { queue.offer(num); } ); //restore qriginal queue
                };

                Runnable r = () -> {
                    var temp = new LinkedList<>((List.of(2, 3, 7, 5, 4)));
                    bsortQueue.accept(temp);
                };
                r.run();//run the operation
            });
            //Calculate best root from A to B
            q.offer(() -> {

                // פיתרון יותר יעיל במחלקות path , pathBst

                //פונקציה שמחזירה את המרחק האווירי בין התחלה לסוף
                Function<Queue<Double>, Double> qdis = (queue) -> {
                    var temp = new LinkedList<Double>(); // תור עזר לשיחזור
                    var start = queue.peek(); // האיבר הראשון
                    var end = 0.0; // יכיל את האיבר האחרון

                    //איטרציה על התור
                    while (!queue.isEmpty()) {
                        end = queue.poll(); // שומר את האיבר האחרון
                        temp.offer(end); // הכנסה לתור עזר
                    }

                    // שחזור תור מקור
                    while (!temp.isEmpty()) { queue.offer(temp.poll()); }

                    //מרחק אווירי בין התחלה לסוף
                    return Math.abs(end - start);
                };

                //פונקציה שמחזירה את המרחק האמיתי
                Function<Queue<Double>, Double> realDistance = (queue) -> {
                    //תור עזר לשחזור תור מקור
                    var temp = new LinkedList<Double>();
                    // סכום כל הקפיצות, כלומר המרחק בפועל עם הקפיצות
                    var sum = 0.0;

                    // קורדינצייה ראשונה
                    var first = queue.poll();
                    // הכנסתה לתור עזר
                    temp.offer(first);

                    while (!queue.isEmpty()) {
                        // שמירת קורדינציה נוכחית
                        var next = queue.poll();
                        // סכום המרחק בין זוגות קורדינציות עוקבות
                        sum += Math.abs(next - first);
                        //הכנסתה לתור עזר לשיחזור
                        temp.offer(next);
                        //יקבל קורדינציה הבאה העוקבת המחוברת לקורדינציה נוכחית
                        first = next;
                    }

                    // שחזור תור מקור
                    while (!temp.isEmpty()) { queue.offer(temp.poll()); }

                    //החזרת מרחק בפועל
                    return sum;
                };

                //פונקציה שמחזירה את הדרך שהכי קרובה למרחק האווירי וגם עם הכי מעט קפיצות
                Function<BinNode<LinkedList<Double>>, Queue<Double>> bestPath = (root) -> {
                    // תור סריקה לרוחב
                    var temp = new LinkedList<BinNode<LinkedList<Double>>>();
                    // הכנסת שורש העץ לתור
                    temp.offer(root);
                    // יכיל את הדרך היעילה ביותר (תור)
                    Queue<Double> bestQueue = null;
                    // יכיל סטיית מרחק בין "מרחק אווירי" ל"מרחק בפועל"
                    double minDeviation = Double.MAX_VALUE;
                    //מס קפיצות הכי קטן
                    int minJumps = Integer.MAX_VALUE;

                    // סריקת התור
                    while (!temp.isEmpty()) {
                        //שליפת חולייה נוכחית - המפנה לתור (מסלול)
                        var current = temp.poll();
                        // קבלת תור (מסלול עצמו) מהחולייה הנוכחית
                        var currentQueue = current.getValue();
                        // חישוב מדדים
                        //מרחק אווירי של מסלול נוכחי
                        var len = qdis.apply(currentQueue);
                        //מרחק יבשתי ריאלי של מסלול זה
                        var realLen = realDistance.apply(currentQueue);
                        // כמה סטינו מהמרחק האווירי - כמובן שתמיד המרחק האווירי הוא הקצר ביותר
                        var deviation = realLen - len;
                        // כמות הקפיצות בעקומת המסלול
                        var jumps = currentQueue.size();

                        // נעדכן אם מצאנו מסלול עם סטייה קטנה יותר
                        if (deviation < minDeviation || (deviation == minDeviation && jumps < minJumps)) {
                            //שמירת מסלול נוכחי כמועדף ביותר בעל המרחק הקטן ביותר והיציב ביותר
                            bestQueue = currentQueue;
                            //שמירת הסטייה במרחק
                            minDeviation = deviation;
                            // כמות הקפיצות בעקומת המסלול
                            minJumps = jumps;
                        }

                        //הכנסת בנים (מסלולים) לתור סריקה לרוחב
                        //הכנסת בן שמאלי לתור
                        if (current.hasLeft()) temp.offer(current.getLeft());
                        // הכנסת בן ימני לתור
                        if (current.hasRight()) temp.offer(current.getRight());
                    }

                    //החזרת התור, כלומר המסלול הקצר ביותר והיציב ביותר
                    return bestQueue;
                };

                //מתודה לבניית עץ מסלולים ומציאת המסלול הקצר ביותר בין שני נקודות והיציב ביותר עם כמה שפחות קפיצות
                Runnable r = () -> {
                    // עץ שמכיל תורים עם קורדינטות:
                    var a = new BinNode<>(
                            //[2.0, 10.0] → מרחק אווירי: 8, מרחק אמיתי: 8, סטייה: 0
                            new BinNode<>(new LinkedList<Double>(List.of(10.0, 2.0))),
                            //[2.0, 3.0, 5.0, 6.0] → מרחק אווירי: 4, מרחק אמיתי: 4, סטייה: 0
                            new LinkedList<Double>(List.of(6.0, 5.0, 3.0, 2.0)),
                            //[2.0, 4.0, 6.0, 8.0, 10.0] → מרחק אווירי: 8, מרחק אמיתי: 8, סטייה: 0 אבל יותר קפיצות
                            new BinNode<>(new LinkedList<Double>(List.of(10.0, 8.0, 6.0, 4.0, 2.0)))
                    );

                    //קבלת המסלול הטוב ביותר
                    var finalq = bestPath.apply(a);
                    //הדפסתו
                    //הוא יבחר את [2, 10] כי גם סטייה 0 וגם הכי מעט קפיצות.
                    System.out.println(finalq);
                };

                //הרצת פונקציית הלמבדה לבדיקת הלמבדות העיקריות
                r.run();
                System.out.println("----------------------------------------------------------");
            });
            // test some lambdas function
            q.offer(() -> {
                // a
                BiFunction<Stack<Integer>, Integer, Boolean> ExistSum = (stk, num) -> {
                    var equalsToNum = false;
                    var temp = new Stack<Integer>();

                    while (!stk.isEmpty()){
                        var first = stk.pop();
                        temp.push(first);

                        while (!stk.isEmpty()){
                            var sec = stk.pop();
                            temp.push(sec);
                            if (first + sec == num){ equalsToNum = true; }
                        }
                    }

                    while (!temp.isEmpty()){ stk.push(temp.pop()); }

                    return equalsToNum;
                };

                // b
                Function<Stack<Integer>, Integer> highSumOfTwoVals = (stk) -> {
                    var max = Integer.MIN_VALUE;
                    var temp = new Stack<Integer>();

                    while (!stk.isEmpty()){
                        var first = stk.pop();
                        temp.push(first);

                        while (!stk.isEmpty()){
                            var sec = stk.pop();
                            temp.push(sec);
                            if (first + sec > max){ max = first + sec; }
                        }
                    }

                    while (!temp.isEmpty()){ stk.push(temp.pop()); }

                    return max;
                };

                // 1
                //O(n)
                // all values in stack should be odd or even
                Function<Stack<Integer>, Boolean> isUnifi = (stack) -> {
                    var zougiut = stack.peek() % 2; // if first num is odd or even
                    var temp = new Stack<Integer>(); // restoration stack
                    var flag = false; // flag to detect if this stack is not valid according to the task

                    // iteration on stack
                    while (!stack.isEmpty()){
                        var current = stack.pop(); // remove current top value from stack
                        var evOd = current % 2; // this num may be odd or even

                        // if this value not identical to the first value
                        // in matter if its even or odd
                        // so we marks this stack as not valid
                        if (evOd != zougiut){ flag = true; }

                        temp.push(current); // push current value to the restoration stack
                    }

                    while (!temp.isEmpty()){ stack.push(temp.pop()); } // restoration proccess

                    return flag ? false : true; // return true if it's valid, else return false
                };

                // 2
                //O(n)
                Function<Queue<Integer>, Integer> lastValueInQueueToRemove = (queue) -> {
                    var size = queue.size(); // size of queue
                    var i = 0; // for keep the last value in the queue

                    // iteration
                    while (i < size){
                        queue.offer(queue.poll()); // remove it from the front of the queue and mv it to the back
                        i++;
                    }

                    return queue.poll(); // return last value
                };

                // 3
                //O(n)^2 ,  can be O(n) if i used hashmap
                Function<Node<Integer>, Boolean> uniqNeg = (chain) -> {
                    var pos = chain; // pointer for itertion on the list

                    // iteration
                    while (pos != null){
                        var num = pos.getValue(); // keep current value

                        // if its negative number
                        if (num < 0) {
                            var count = 1; // counter, for number of occurances of value in list
                            var next = pos.getNext(); // pointer for iteration on the rest of the list

                            // iteration, to check if current negative value found somewhere else in the list
                            // if cuurent value find one more time return false
                            while (next != null) {
                                if (next.getValue() == num){ return false; }
                                next = next.getNext();
                            }
                        }

                        pos = pos.getNext(); // mv the pointer to the next node
                    }

                    return true; // means every negative number found once in the list
                };

                // 6 a
                //O(n)
                BiFunction<Queue<Integer>, Queue<Integer>, Boolean> startWith = (q1, q2) -> {
                    if (q2.size() < q1.size()) { return false; }

                    // restoration queues
                    var temp1 = new LinkedList<Integer>();
                    var temp2 = new LinkedList<Integer>();

                    var flag = false; // flag to detect not valid stacks

                    // iteration
                    while (!q1.isEmpty()){

                        if (q1.peek() != q2.peek()){ flag = true; }

                        temp1.offer(q1.poll());
                        temp2.offer(q2.poll());
                    }

                    // remove all the rest of the values from the second queue
                    while (!q2.isEmpty()){ temp2.offer(q2.poll()); }

                    // restoration
                    while (!temp1.isEmpty()){ q1.offer(temp1.poll()); }
                    while (!temp2.isEmpty()){ q2.offer(temp2.poll()); }

                    return flag ? false : true;
                };

                // 6 b
                //O(n)
                // check if secon queue is duplication of first queue
                BiFunction<Queue<Integer>, Queue<Integer>, Boolean> duplication = (q1, q2) -> {
                    var flag = false; // flag to detect not valid stacks
                    var size2 = q2.size(); // size of second queue
                    var i = 0;

                    // iteration on the second queue and first queue
                    while (i <= size2){
                        // if values are diffrent its means thats queues are not valid
                        if (q2.peek() != q1.peek()){ flag = true; }
                        // remove values from the front of both queues and move them to the back
                        q2.offer(q2.remove());
                        q1.offer(q1.remove());
                        i++;
                    }

                    return flag ? false : true;
                };

                // 7
                // check if the values in the list goes up then down
                // O(n)
                Function<Node<Integer>, Boolean> isUpDown = (chain) -> {
                    var up = false; // for detect if list went up in some point
                    var down = false; // for detect if list went down in some point

                    // detection if the values sorted from smaller to bigger
                    while (chain.getNext() != null && chain.getValue() < chain.getNext().getValue()){
                        chain = chain.getNext();
                        if (!up){ up = true; }
                    }

                    // detection if the values sorted from bigger to smaller
                    while (chain.getNext() != null && chain.getValue() > chain.getNext().getValue()){
                        chain = chain.getNext();
                        if (!down){ down = true; }
                    }

                    // if up == true and down == true and we reach the end of the list, return true else return false
                    return chain.getNext() == null && up && down;
                };

                // 8
                // calc avg of tree
                Function<BinNode<Integer>, Integer> cavgTree = (root) -> {
                    var queue = new LinkedList<BinNode<Integer>>(); // iteration queue
                    queue.offer(root); // offer the root of the tree to the queue
                    var sum = 0; // sum var
                    var count = 0; // count var

                    // iteration
                    while (!queue.isEmpty()){
                        var current = queue.poll(); // remove current BinNode from the queue
                        sum += current.getValue(); // sum up current value
                        count++; // count it

                        // offer the sons of the current binnode to the iteration queue
                        if (current.hasLeft()){ queue.offer(current.getLeft()); }
                        if (current.hasRight()){ queue.offer(current.getRight()); }
                    }

                    return sum / count; // calc avg and return it
                };

                // print values smaller than the avg
                Consumer<BinNode<Integer>> printSmlcavgTree = (root) -> {
                    var queue = new LinkedList<BinNode<Integer>>(); // iteration queue
                    queue.offer(root); // offer the root of the tree to the queue
                    var avg = cavgTree.apply(root); // call the lambda function that cal the avg of a tree

                    // iteration
                    while (!queue.isEmpty()){
                        var current = queue.poll(); // remove current BinNode from the queue
                        // print current value if its smaller than the avg
                        if (current.getValue() < avg) { System.out.println(current.getValue()); }

                        // offer the sons of the current binnode to the iteration queue
                        if (current.hasLeft()){ queue.offer(current.getLeft()); }
                        if (current.hasRight()){ queue.offer(current.getRight()); }
                    }
                };
            });
            // מתוך בחינת מה"ט 2025 קיץ מועד א', חלק ג' - שאלה 9
            q.offer(() -> {
                // דרך א'
                // פונקציית עזר להפיכת מחסנית למערך
                Function<Stack<Integer>, int[]> stkToArray = (stk) -> {
                    var arr = new int[stk.size()]; // יצירת מערך בגודל המחסנית
                    var index = 0; // אינדקס למערך

                    while (!stk.isEmpty()){ arr[index++] = stk.pop(); } // בניית המערך

                    return arr; // החזרת המערך
                };

                // פונקציה הבודקת שכל הערכים במחסנית 1 מופיעים לפי הסדר במחסנית 2 אך לא ברצף
                BiFunction<Stack<Integer>, Stack<Integer>, Boolean> allFoundInorder = (stk1, stk2) -> {
                    // יצירת מערך ממחסנית 1
                    var arr1 = stkToArray.apply(stk1);
                    // יצירת מערך ממחסנית 2
                    var arr2 = stkToArray.apply(stk2);
                    // שמירת מיקום איבר קודם במערך 2
                    var prev = -1;
                    // שמירת מיקום איבר נוכחי במערך 2
                    var current = -1;

                    // מעבר על מערך 1 המייצג את מחסנית 1
                    for (int i = 0; i < arr1.length; i++) {
                        // שמירת ערך נוכחי
                        var num = arr1[i];
                        // משתנה לבדיקה אם ערך נמצא
                        var found = false;

                        // מעבר על מערך 2 המייצג מחסנית 2
                        for (int j = 0; j < arr2.length; j++) {
                            // אם המס נמצא שוב
                            if (arr2[j] == num){
                                // סימון שנמצא
                                found = true;
                                //שמירת מיקומו
                                current = j;
                                // יתבצע פעם אחת בלבד כדי שלא יחזור שקר ישר על ההתחלה
                                if (prev == -1){ prev = current; }
                                // יציאה מהלולאה השנייה אין צורך להמשיך לחפש
                                break;
                            }
                        }

                        // אם ערך לא נמצא יוחזר שקר
                        if (!found){ return false; }

                        // אם המיקום של איבר נוכחי קטן ממיקום של האיבר הקודם יחזור שקר
                        // כי בעצם התבקשתי בתרגיל לבדוק שכל האיברים במחסנית 1
                        // נמצאים לפי הסדר (לא חייב ברצף - יכול להיות שיהיו איברים ביניהם וזה תקין) במחסנית 2
                        if (current < prev){ return false; }

                        // משתנה "קודם" יכיל את המיקום של המס שחופש הרגע
                        prev = current;
                    }

                    // נחזיר אמת משמע כל האיברין ממחסנית אחת נמצאים לפי הסדר במחסנית 2
                    return true;
                };

                // פונקציית למבדה לבדיקת המתודה לעיל
                Runnable r = () -> {
                    // יצירת מחסנית ראשונה
                    var stk1 = new Stack<Integer>();
                    stk1.push(3);
                    stk1.push(7);
                    stk1.push(2);

                    // יצירת מחסנית שנייה
                    var stk2 = new Stack<Integer>();
                    stk2.push(9);
                    stk2.push(3);
                    stk2.push(5);
                    stk2.push(7);
                    stk2.push(4);
                    stk2.push(2);
                    stk2.push(1);

                    // משתנה בוליאני המפעיל מתודה ומקבל תוצאה בחזרה אמת/שקר

                    var res = allFoundInorder.apply(stk1, stk2);
                    // הדפסת התוצאה
                    System.out.println(res);
                };

                // הפעלת הלמבדה
                r.run();

                // דרך ב'
                BiFunction<Stack<Integer>, Integer, Integer> numIndexInStk = (stk, num) -> {
                    var temp = new Stack<Integer>(); // temporary stack for restoration
                    var count = 0; // for index of num in stack

                    // iteration
                    while (!stk.isEmpty()){
                        var cnum = stk.pop(); // remove current value from stack

                        // if current value not equals to num
                        if (cnum != num){
                            count++; // add 1 to count
                            temp.push(cnum); // push it to the temporary stack
                        }else {
                            // else if current value is actually equals to num
                            count++; // add 1 to count
                            temp.push(cnum); // push it to the temporary stack

                            while (!stk.isEmpty()){ temp.push(stk.pop()); } // keep iteration

                            while (!temp.isEmpty()){ stk.push(temp.pop()); } // restoration - restore original stack

                            return count; // return index of num in the stack
                        }
                    }

                    while (!temp.isEmpty()){ stk.push(temp.pop()); } // restoration - restore original stack

                    return -1; // not found
                };

                BiFunction<Stack<Integer>, Stack<Integer>, Boolean> allFinSecStkInOrder = (stk1, stk2) -> {
                    var temp = new Stack<Integer>(); // temporary stack for restoration
                    var notValid = false; // for detect if stack 2 is not valid
                    var prev  = -1; // index of previous value from stack 2

                    // iteration
                    while (!stk1.isEmpty()){
                        var num = stk1.pop(); // remove current value from stack
                        temp.push(num); // push it to the restoration stack
                        var current = numIndexInStk.apply(stk2, num); // calc index of current value in second stack

                        if (prev == -1){ prev = current; } // for not returning false on first iteration

                        // if previous value index bigger than current value index, sec stack not valid
                        if (prev > current){ notValid = true; }

                        prev = current; // current value index become previous
                    }

                    while (!temp.isEmpty()){ stk1.push(temp.pop()); } // restoration

                    // if it detect that sec stk not valid return false, else return true
                    return notValid ? false : true;
                };

                System.out.println("----------------------------------------------------------");
            });
            // classe: Client, Package, BasicPackage, ExtendedPackage, Phone, Company
            q.offer(() -> {
                var company = new Company();

                // לקוח עם ערוץ "Sport"
                var c1 = new Clientc(
                        "David",
                        new ExtendedPackage(
                                10, 30, 5, 2,
                                new String[]{"Kids", "Sport"}
                        )
                );

                // לקוח עם BasicPackage
                var c3 = new Clientc(
                        "Maya",
                        new BasicPackage(
                                15, 20
                        )
                );

                // הוספה למערכת
                company.addClient(c1);
                company.addClient(c3);

                // הפעלת הפונקציה שדורשת להדפיס לקוחות עם ערוץ ספורט
                System.out.println("Clients with Sport channel:");
                company.printAllClientsThatHaveSportChannel();
                System.out.println();
                var basicClients = company.onlyBasicClients();
                System.out.println("num of clients that have only the basic package: " + basicClients);
                System.out.println("----------------------------------------------------------");
            });
            // differenceList + theSurvives
            q.offer(() -> {
                //2022 summer a
                System.out.println("differenceList + theSurvives: ");
                //1
                Function<Node<Integer>, Node<Integer>>differenceList = (chain) -> {
                    var pos = chain;
                    Node<Integer> newey = null;
                    Node<Integer> tail = null;

                    while (pos.getNext() != null){
                        var toAdd = new Node<Integer>(
                                Math.abs(
                                        pos.getValue() - pos.getNext().getValue()
                                )
                        );

                        if (newey == null){
                            newey = toAdd;
                            tail = newey;
                        }else {
                            tail.setNext(toAdd);
                            tail = tail.getNext();
                        }

                        pos = pos.getNext();
                    }

                    return newey;
                };

                //2
                Consumer<Node<Integer>> theSurvives = (chain) -> {

                    while (lengthN(chain) != 1){
                        chain = differenceList.apply(chain);
                        System.out.println("");
                        printListNod(chain);

                    }
                };

                Runnable printmj = () -> {
                    //5 20 9 6 5 8 2
                    var chain = new Node<>(5,
                            new Node<>(20,
                                    new Node<>(9,
                                            new Node<>(6,
                                                    new Node<>(5,
                                                            new Node<>(8,
                                                                    new Node<>(2)))))));

                    theSurvives.accept(chain);
                };

                printmj.run();

                System.out.println("");
                System.out.println("----------------------------------------------------------");
            });
            //2022 summer b, 2023 spring a, 2023 summer a, 2023 summer b
            q.offer(() -> {
                //1 O(n)
                // מחסנית "שוות סכומים", הי א מחסנית של מספרים שלמים המכילה מספר אי זוגי של איברים.
                //כך שכל שני איברים קיצוניים )ראשון + אחרון, שני + לפני אחרון וכו'( שווים בסכומם לאיבר האמצעי מחסנית.
                Function<Stack<Integer>, Boolean> equalSums = (stk)-> {
                    var size = stk.size();
                    if (size % 2 == 0){ return false; }

                    var arr = new int[size];
                    var index = 0;

                    while (!stk.empty()){ arr[index++] = stk.pop(); }

                    for (var i = 0; i < arr.length / 2; i++) {
                        if (arr[i] + arr[arr.length - 1 - i] != arr[arr.length / 2]){
                            return false;
                        }
                    }

                    return true;
                };

                //2 O(n)^2
                // נגדיר: "רשימת מקטעים" כשרשרת חוליות של מספרים שלמים ריקה או כשרשרת חוליות אשר כל אחת
                //מהחוליות שלה מכילה ערך קטן או שווה למספר החוליות העוקבות לה )נמצאות אחריה בשרשרת(.
                Function<Node<Integer>, Boolean> numNodesFollowingPLUSisSection = (chain) -> {

                    while (chain != null){
                        var current = chain.getValue();
                        var count = 0;
                        var pos = chain.getNext();

                        while (pos != null){
                            count++;
                            pos = pos.getNext();
                        }

                        if (current > count){ return false; }

                        chain = chain.getNext();
                    }

                    return true;
                };

                // בעזרת המחלקה MyNode נבנית שרשרת חוליות לפי הכלל הבא:
                //כל חוליה היא מסוג MyNode. בכל איבר התכונה value מאחסנת מספר שלם, התכונה howManyBig
                //מאחסנת מספר שלם ששווה למספר האיברים בשרשרת שנמצאים אחרי האיבר הנוכחי, שערך התכונה value
                //שלהם גדול מערך value של האיבר הנוכחי. התכונה next מחזיקה מצביע על האיבר הבא.
                BiFunction<MyNode, Integer, Boolean> addNumber = (list, val) -> {
                    //position
                    var position = 6;

                    // add at start
                    if (position == 1){
                        // mini manipulation,
                        // instead of adding new mynode to the start of the list
                        var toAdd =  new MyNode(list.getValue()); // create new mynode
                        toAdd.setHowManyBig(list.getHowManyBig());

                        // connect it between the first and the sec mynode
                        toAdd.setNext(list.getNext());
                        list.setNext(toAdd);

                        // update the first mynode fields
                        list.setValue(val); // update value field to contains val
                        var fromStart = list;// pointer to iterate on the rest mynodes
                        var count = 0; // counter

                        // iteration
                        while (fromStart != null){
                            // if current value smaller than val
                            // update the field "howmanybig", add 1 to it
                            if (fromStart.getValue() < val ){ count++; }
                            fromStart = fromStart.getNext(); // mv to the next mynode
                        }
                        // update howmanybig field to contains count
                        list.setHowManyBig(count);

                        return true; // new mynode added successfully to the list
                    }

                    var pos = list; // pointer for the mynodes list
                    var toAdd = new MyNode(val); // create new mynode to add in the future
                    var prev = pos; // pointer for the last mynode in the list
                    var count = 1; // counter till the position

                    // iteration
                    while (pos != null && count < position){
                        // if current value smaller than val
                        // update the field "howmanybig", add 1 to it
                        if (pos.getValue() < val){pos.setHowManyBig(pos.getHowManyBig() + 1);}
                        count++;// add 1 to the counter
                        prev = pos; // point the tail pointer to current mynode
                        pos = pos.getNext(); // mv to the next mynode
                    }

                    // add between
                    if (pos != null){
                        toAdd.setNext(pos.getNext());
                        pos.setNext(toAdd);
                        var count2 = 0;
                        var next = pos.getNext();

                        while (next != null){
                            if (next.getValue() > val ){ count2++; }
                            next = next.getNext();
                        }
                        pos.setHowManyBig(count2);

                        // new mynode added successfully to the list
                        return true;
                    }

                    // add in the end
                    prev.setNext(new MyNode(val));
                    // new mynode added successfully to the list
                    return true;
                };

                //11 -> עץ כחול לבן
                Function<BinNode<Character>, Boolean> bwt = (root) -> {
                    var searchQueeu = new LinkedList<BinNode<Character>>();
                    searchQueeu.offer(root);

                    while (!searchQueeu.isEmpty()){
                        var current = searchQueeu.poll();
                        char temp = current.getValue();

                        // כל צומת חייבת להיות או כחולה או לבנה
                        if (temp != 'w' || temp != 'b'){ return false; }

                        // עלה חייב להיות בצבע כחול
                        if (!current.hasLeft() && !current.hasRight()
                                && current.getValue() != 'b'){
                            return false;
                        }

                        // אם אב הוא כחול, חייב שיהיה לו שני בנים לבנים
                        // אם הוא הורה
                        if (current.hasLeft() || current.hasRight()){
                            // אם יש לו רק בין אחד, יוחזר שקר
                            if (!current.hasLeft() || !current.hasRight()){ return false; }

                            // אם שני הבנים שלו אינם לבנים יוחזר שקר
                            if (current.getLeft().getValue() != 'w'
                                    || current.getRight().getValue() != 'w'){
                                return false;
                            }
                        }

                        // הכנסת הבנים לתור להמשך סריקה
                        if (current.hasLeft()){ searchQueeu.offer(current.getLeft()); }
                        if (current.hasRight()){ searchQueeu.offer(current.getRight()); }
                    }

                    return true; // עץ זה הוא אכן עץ כחול לבן
                };

                //2023 spring a
                // 1
                BiFunction<Queue<Integer>, Queue<Integer>, Queue<Double>> avgQueu = (marks, tests) -> {
                    var newey = new LinkedList<Double>(); // תור ממוצעים

                    // מעבר על תור מספרי הבחינות עבור כל תלמיד
                    while (!tests.isEmpty()){
                        var i = 0; // משתנה עד זקיף
                        var sum = 0.0; // צובר

                        // מעבר על תור ציוני הבחינות
                        while (!marks.isEmpty() && i < tests.peek()){
                            sum += marks.poll(); // צבירת ציון
                            i++; // קידום משתנה עד זקיף
                        }

                        // הכנסת ממוצע תלמיד נוכחי לתור הממוצעים
                        newey.offer(sum / tests.peek());
                        tests.poll(); // שליפת מס הבחינה מהתור
                    }

                    return newey; // החזרת תור הממוצעים
                };

                // 2
                // סעיף א
                Function<Node<Integer>, Integer> numDigits = (chain) -> {
                    var c = 0; // מונה

                    // איטרציה על שרשרת החוליות
                    while (chain != null){
                        c++; // קידום מונה
                        chain = chain.getNext(); // התקדמות לחולייה הבאה
                    }

                    return c; // החזרת מונה
                };

                // סעיף ב
                BiFunction<Node<Integer>, Node<Integer>, Integer> compare = (n1, n2) -> {
                    var first = 0; // מס ראשון לבנייה מהרשימה הראשונה
                    var sec = 0; // מס שני לבנייה מהרשימה השנייה

                    // בניית מס ראשון
                    while (n1 != null){
                        first = first * 10 + n1.getValue();
                        n1 = n1.getNext(); // התקדמות לחולייה הבאה
                    }

                    // בניית מס שני
                    while (n2 != null){
                        sec = sec * 10 + n2.getValue();
                        n2 = n2.getNext(); // התקדמות לחולייה הבאה
                    }

                    // אם המס הראשון גדול מהמס השני יוחזר 1, אחרת יוחזר 2
                    // אם שני המס שווים יוחזר 0
                    return first > sec ? 1 : first < sec? 2 : 0;
                };

                // 5 a O(n)
                BiFunction<Node<Integer>, Integer, Integer> distance = (chain, num) -> {
                    // build temporary doubly linkedList
                    BinNode<Integer> head = null; // head pointer
                    BinNode<Integer> tail = null; // tail pointer

                    //from [] -> [] to [] <-> []
                    // iteration on original list
                    while (chain != null){
                        var current = chain.getValue(); // catch current value
                        var toAdd = new BinNode<>(current); // create new binnode

                        //build the new list
                        // if the head pointer points to null
                        if (head == null){
                            head = toAdd; // point the head pointer to the new binnode
                            tail = toAdd; // also point the tail pointer to it
                        }else {
                            // else if the head pointer not pointing to null
                            // add the new binnode on the right side of the tail pointer
                            tail.setRight(toAdd);
                            // create doubly connection
                            tail.getRight().setLeft(tail);
                            // move the tail pointer to the new added binnode
                            tail = tail.getRight();
                        }

                        chain = chain.getNext(); // move to the next node on the original list
                    }

                    // distance from start __ -> __ ● __
                    var start = 0;
                    while (head != null){
                        // if num found, return the distance from the start
                        if (head.getValue() == num){ break; }
                        start++; // add 1 to start var
                        head = head.getRight(); // move to the right binnode
                    }

                    // distance from the end   __ ● __ <- __
                    var end = 0;
                    while (tail != null){
                        // if num found return the distance from the end
                        if (tail.getValue() == num){ break; }
                        end++; // add 1 to end var
                        tail = tail.getLeft(); // move to the left binnode
                    }

                    //__ -> __ ● ____ ● __ <- __
                    // return distance of num from both sides
                    return start != 0 && end != 0 ? start + end : -1;
                };
                // 5 b O(n)^2
                Function<Node<Integer>, Integer> minDistanceValue = (chain) -> {
                    var minDistance = Integer.MAX_VALUE; // will contain the minimum distance

                    // iteration on original list
                    while (chain != null){
                        var currentNumber = chain.getValue(); // catch current value
                        //__ -> __ ● ____ ● __ <- __
                        // distance of num from both sides
                        var currentDistance = distance.apply(chain, currentNumber);

                        // *** it will always pass this first if statement ***
                        // if this current distance != -1,
                        // num found at list once on the original list
                        if (currentDistance != -1){
                            // if the current distance smaller than the minimum distance
                            if (currentDistance < minDistance){
                                // the minimum distance will contain this distance
                                minDistance = currentDistance;
                            }
                        }

                        chain = chain.getNext();  // move to the next node on the original list
                    }

                    // if there is a number that have the most smaller distance,
                    // return its distance, else return -1
                    return minDistance != Integer.MAX_VALUE ? minDistance : -1;
                };

                //2023 summer a
                // 1 a O(n)
                BiFunction<Queue<Integer>, Integer, Integer> putInPlace = (queue, num) -> {
                    // temporary queue for all numbers that smaller than num
                    var sml = new LinkedList<Integer>();
                    // temporary queue for all numbers that bigger than num
                    var big = new LinkedList<Integer>();
                    // temporary queue for all numbers equals to num
                    var eq = new LinkedList<Integer>();

                    // build the tree temporary queues
                    queue.forEach((current) -> {
                        // if current value is bigger than num, offer it to the "biggers queue" -> sml
                        if (current > num){ big.offer(current); }
                        // if current value is smaller than num, offer it to the "smallers queue" -> big
                        else if (current < num){ sml.offer(current); }
                        // if current value is equals to num, offer it to the "equals queue" -> eq
                        else { eq.offer(current); }
                    });
                    queue.clear(); // clear original queue content

                    AtomicInteger index = new AtomicInteger(); // synced index var

                    // move all content from "smallers queue" to the original queue
                    sml.forEach((current) -> {
                        queue.offer(current);
                        index.getAndAdd(1); // increment index var by one
                    });
                    sml.clear(); // clear "smallers queue" content

                    // move all content from "equals queue" to the original queue
                    eq.forEach((current) -> {
                        queue.offer(current);
                        index.getAndAdd(1); // increment index var by one
                    });
                    eq.clear(); // clear "equals queue" content

                    queue.offer(num); // offer the new num to the original queue
                    index.getAndAdd(1); // increment index var by one
                    var cindex = index.get(); // keep the value of the index var

                    // move all content from "biggers queue" to the original queue
                    big.forEach( (current) -> { queue.offer(big.poll()); } );
                    big.clear(); // clear "biggers queue" content

                    return cindex; // return the index of the new num
                };

                // 1 b O(n)
                BiConsumer<Queue<Integer>, Integer> moveToFront = (queue, k) -> {
                    var size = queue.size(); // size of queue
                    var tillK = 0; // times of iteration
                    // how many numbers from the beggining of the queue, should the function,
                    // relocate to be at the end of the queue
                    var howMany = size - k;
                    // iteration, remove from start add to the end
                    while (tillK < howMany){ queue.offer(queue.poll()); tillK++; }
                };

                System.out.println("buildFreqList: ");
                // 5 - a O(n)^2
                Consumer<Node<Integer>> buildFreqList = (chain) -> {
                    var pos = chain; // pointer to the list

                    // iteration on the original list
                    while (pos != null){
                        var current = pos.getValue(); // keep current value
                        // count the num of occurrences of this current value, in the list
                        var count = 1;

                        var rest = pos;// pointer for iteration on the rest nodes

                        // iteration - for deleting all occurrences of this current value,
                        // in the list
                        while (rest.getNext() != null){
                            if (rest.getNext().getValue() == current){
                                rest.setNext(rest.getNext().getNext());
                                // count the num of occurrences of this current value
                                count++;
                            }else {
                                rest = rest.getNext(); // move to the next node
                            }
                        }

                        // add new node after the current number,
                        // that represent num of occurrences of current number in the list
                        pos.setNext(new Node<>(count, pos.getNext()));

                        pos = pos.getNext().getNext(); // move to the next next node
                    }
                };

                // 5 - b O(n)
                Function<Node<Integer>, Integer> mostPopularNumber = (chain) -> {
                    // the number that have the maximun num of occurrences in the list
                    var theNumber = 0;
                    // max num of occurrences of some value in the list
                    var max = Integer.MIN_VALUE;
                    var pos = chain; // pointer for iteration on the list

                    // itertaion
                    while (pos != null && pos.getNext() != null){
                        var current = pos.getValue(); // keep current value
                        // keep num of occurrences of current value
                        var next = pos.getNext().getValue();

                        // check if this value found in list more than other values
                        if (next > max){
                            max = next;
                            theNumber = current; // keep this current value
                        }

                        pos = pos.getNext().getNext(); // move to the next next node
                    }

                    return theNumber; // return the most shown value in the list
                };

                // check the function above ⤴️
                Runnable rt = () -> {
                    //before: 100,40,20,100,100,20,30
                    //after: 100,3,40,1,20,2,30,1
                    var list = new Node<>(100,
                            new Node<>(40,
                                    new Node<>(20,
                                            new Node<>(100,
                                                    new Node<>(100,
                                                            new Node<>(20,
                                                                    new Node<>(30)))))));
                    System.out.println("before: ");
                    printListNod(list);
                    System.out.println("");
                    buildFreqList.accept(list);
                    System.out.println("after: ");
                    printListNod(list);
                    System.out.println("");
                    var n = mostPopularNumber.apply(list);
                    System.out.println("and the most popular num is: " + n);
                };

                rt.run();

                // 2023 summer b
                // 1 - a O(n)
                BiFunction<Integer, Integer, Boolean> strangers = (num1, num2) -> {
                    // if both num are strangers -> return true
                    // iteration on both numbers
                    for (var i = 2; i < num1; i++){
                        // if thery are not become zero yet
                        if (num1 > 0 && num2 > 0){
                            // peek the last digit from both numbers
                            // and divide them by i, and keep the results
                            var first = (num1 % 10) % i;
                            var sec = (num2 % 10) % i;

                            // if we got the same results, return false
                            if (first == sec){ return false; }

                            // remove the last digit from both numbers
                            num1 /= 10;
                            num2 /= 10;
                        }
                        // one of the numbers become zero, get out of the loop
                        else { break; }
                    }

                    return true; // they are strangers
                };

                //1 - b - O(n)^2
                BiConsumer<Queue<Integer>,Integer> change = (queue, num) ->{
                    // before-queue: all the starngers
                    var before = new LinkedList<Integer>();
                    // after-queue: all the strangers
                    var after = new LinkedList<Integer>();

                    //iteration
                    queue.forEach((current) -> {
                        // if the current value and num are strangers
                        // offer current value to the "before-queue"
                        if (strangers.apply(current, num)){ before.offer(current); }

                        // else offer it to the "after-queue"
                        else { after.offer(current); }
                    });
                    queue.clear(); // clear original queue

                    //move all "before-queue" content to the original queue
                    before.forEach( (current) -> { queue.offer(current); } );
                    before.clear(); // clear "before-queue"

                    //move all "after-queue" content to the original queue
                    after.forEach( (current) -> { queue.offer(current); } );
                    after.clear(); // clear "after-queue"
                };

                // 2 - O(n)
                Function<Node<Integer>, Node<Integer>> split = (chain) -> {
                    if (chain == null) return null;
                    //A function that receives a reference to the first node of a linked list
                    // containing integers and splits the list into two
                    // according to the following rule:
                    // if the value of the first node in the list is even,
                    // a new list is created containing only the odd values
                    // while removing them from the original list
                    // so that the original list will contain only the even values,
                    // and if the value of the first node in the list is odd,
                    // a new list is created containing only the even values
                    // while removing them from the original list
                    // so that the original list will contain only the odd values.
                    // The function returns a reference to the first node of the new list.
                    Node<Integer> head = null; // head pointer
                    Node<Integer> tail = null; // tail pointer

                    var current = chain.getValue();
                    var div = Math.abs(current % 2); // calculate the parity of the first number

                    var pos = chain;
                    // iteration
                    while (pos.getNext() != null){
                        var num = pos.getNext().getValue(); // keep next value
                        // calculate the parity of the next number
                        var tempDiv = Math.abs(num % 2);

                        // if the next number have the same parity
                        // as the first value in the list
                        // keep it in the original list
                        if (tempDiv == div){ pos = pos.getNext(); }

                        // else if the next number don't have the same parity
                        // as the first value in the list
                        else {
                            // remove the next number from the original list
                            pos.setNext(pos.getNext().getNext());

                            // create new node that contains the current value
                            var toAdd = new Node<>(num);

                            // add it to the new list
                            // if the new list is empty
                            if (head == null){
                                // make the head pointer, to point to the new node
                                head = toAdd;
                                // make the tail pointer, to point to the new node
                                tail = head;
                            }else {
                                // if its not empty
                                // connect the new node to the last node in the list
                                tail.setNext(toAdd);
                                // and move the tail pointer to it
                                tail = tail.getNext();
                            }
                        }
                    }

                    // returns a reference to the first node of the new list.
                    return head;

                };

                // 7
                // עץ בינארי של מספרים שלמים נקרא "עץ מיוחד" אם הוא עונה על כלל הבא: עבור כל צומת בעץ כל הערכים
                //הנמצאים בתת - העץ השמאלי שלו שונים מכל הערכים הנמצאים בתת -העץ הימני שלו.
                // פונקציית עזר
                BiFunction<BinNode<Integer>, Integer, Boolean> isdfr = (root, num) -> {
                    // create search queue for iteration
                    var queue = new LinkedList<BinNode<Integer>>();
                    queue.offer(root); // offer the root to the queue

                    // iteration
                    while (!queue.isEmpty()){
                        var current = queue.poll(); // remove current binnode from the queue
                        var tnum = current.getValue(); // keep current value

                        // if current value equals to num, return false
                        if (tnum == num){ return false; }

                        // offer the childrens to the search queue
                        // offer the left son to the search queue
                        if (current.hasLeft()){ queue.offer(current.getLeft()); }

                        // offer the right son to the search queue
                        if (current.hasRight()){ queue.offer(current.getRight()); }
                    }

                    return true; // num not found at all at the tree
                };

                // פונקצייה עיקרית
                Function<BinNode<Integer>, Boolean> isdfrAll = (root) -> {
                    // create search queue for iteration ONLY on the left side of the tree
                    var queue = new LinkedList<BinNode<Integer>>();
                    queue.offer(root.getLeft()); // offer the root to the queue
                    var secSide = root.getRight(); // pointer to the right side of the tree

                    // iteration
                    while (!queue.isEmpty()){
                        var current = queue.poll(); // remove current binnode from the queue
                        var tnum = current.getValue(); // keep current value

                        // if the current num found in the rightest side of the tree,
                        // return false
                        if (!isdfr.apply(secSide, tnum)){ return false; }

                        // offer the childrens to the search queue
                        // offer the left son to the search queue
                        if (current.hasLeft()){ queue.offer(current.getLeft()); }

                        // offer the right son to the search queue
                        if (current.hasRight()){ queue.offer(current.getRight()); }
                    }

                    return true; // num not found at all at the tree
                };

                System.out.println("----------------------------------------------------------");
            });
            // 2025 - my test
            q.offer(() -> {

                // 1
                // Definition?
                // A valid queue of strings is a queue where all strings
                // that start with the same letter are located only at the beginning of the queue

                // 1 - a
                // Write an operation that receives a valid queue of strings and a new string.
                // The operation inserts the string into the queue so that the queue remains valid.
                BiConsumer<Queue<String>, String> addToProperQueue = (queue, st) -> {
                    // Temporary queue for valid strings (those starting with the same first char)
                    var valid = new LinkedList<String>();
                    // Temporary queue for all other strings
                    var notValid = new LinkedList<String>();
                    // Determine the first character of the first string in the queue
                    char first = queue.peek().charAt(0);

                    // Iteration: move all valid strings from the original queue to 'valid'
                    while (!queue.isEmpty() && queue.peek().charAt(0) == first) {
                        valid.offer(queue.poll());
                    }

                    // Move the rest of the strings to 'notValid'
                    while (!queue.isEmpty()) { notValid.offer(queue.poll()); }

                    // Decide where to put the new string, depending on its starting character
                    if (st.charAt(0) == first) { valid.offer(st); }
                    else { notValid.offer(st); }

                    // Restoration: rebuild the original queue
                    while (!valid.isEmpty()) { queue.offer(valid.poll()); }
                    while (!notValid.isEmpty()) { queue.offer(notValid.poll()); }
                };

                // 1 - b
                // Write a function that receives a queue of strings.
                // The function checks if the queue is a "valid queue".
                // A valid queue is defined as: all strings starting with the same letter appear only at the beginning of the queue.
                // If valid → return true, otherwise → return false.
                Function<Queue<String>, Boolean> isProperQueue = (queue) -> {
                    // Get the first character of the first string in the queue
                    char first = queue.peek().charAt(0);

                    // Temporary queues for restoring the original queue later
                    var rest1 = new LinkedList<String>();
                    var rest2 = new LinkedList<String>();

                    // Flag indicating if the queue is not valid
                    boolean notvalid = false;

                    // Move all strings that start with the same first char into rest1
                    while (!queue.isEmpty() && queue.peek().charAt(0) == first) {
                        rest1.offer(queue.poll());
                    }

                    // Move the rest of the strings into rest2, checking validity
                    while (!queue.isEmpty()) {
                        if (queue.peek().charAt(0) == first) {
                            // If we find another string starting with 'first' after other strings → invalid
                            notvalid = true;
                        }
                        rest2.offer(queue.poll());
                    }

                    // Restoration: rebuild the original queue
                    while (!rest1.isEmpty()) { queue.offer(rest1.poll()); }
                    while (!rest2.isEmpty()) { queue.offer(rest2.poll()); }

                    // Return true if valid, false if invalid
                    return !notvalid;
                };

                // 1 - c
                // Write a function that receives a queue of strings.
                // The function should check if the queue is a "valid queue".
                // If it is valid – the function does nothing.
                // If it is not valid – the function rearranges the order of the strings
                // in the queue so that it becomes valid.
                Consumer<Queue<String>> fixit = (queue) -> {
                    // Define a local function to find the most popular first character in the queue
                    Function<Queue<String>, Character> mostPopularChar = (ququ) -> {
                        // Create a map to count how many times each first character appears
                        var map = new HashMap<Character, Integer>();
                        // Temporary queue to hold elements while counting
                        var rest = new LinkedList<String>();

                        // Iterate through the queue
                        while (!ququ.isEmpty()) {
                            // Take out the string at the front of the queue
                            String cstr = ququ.poll();
                            // Get the first character of the string
                            char current = cstr.charAt(0);
                            // Update its count in the map (default 0 if not yet present)
                            map.put(current, map.getOrDefault(current, 0) + 1);
                            // Store the string in the temporary queue for restoration
                            rest.offer(cstr);
                        }

                        // Restore the queue by moving items back from temporary queue
                        while (!rest.isEmpty()) { ququ.offer(rest.poll()); }

                        // Initialize variables to find the max occurrence
                        var max = Integer.MIN_VALUE;
                        var most = ' ';

                        // Go through all entries in the map
                        for (var entry : map.entrySet()) {
                            // Current character being checked
                            var current = entry.getKey();
                            // Number of times this character appeared
                            var count = entry.getValue();

                            // If this character occurs more than the current max
                            if (count > max) {
                                // Update max occurrence
                                max = count;
                                // Save this character as the most popular
                                most = current;
                            }
                        }

                        // Return the most frequent first character
                        return most;
                    };

                    // Find the most popular first character in the given queue
                    var most = mostPopularChar.apply(queue);

                    // Temporary queue for strings that do not start with 'most'
                    var rest = new LinkedList<String>();
                    // Temporary queue for strings that start with 'most'
                    var finalq = new LinkedList<String>();

                    // Iterate through the original queue
                    while (!queue.isEmpty()) {
                        // Remove the current string
                        var currentString = queue.poll();
                        // Get its first character
                        var currentCharcter = currentString.charAt(0);

                        // If the first character matches the most popular one
                        if (currentCharcter == most) {
                            // Add it to the 'finalq' list
                            finalq.offer(currentString);
                        } else {
                            // Otherwise, add it to the 'rest' list
                            rest.offer(currentString);
                        }
                    }

                    // Rebuild the queue: add all strings with the most popular char first
                    while (!finalq.isEmpty()) {
                        queue.offer(finalq.poll());
                    }

                    // Then add all other strings afterward
                    while (!rest.isEmpty()) {
                        queue.offer(rest.poll());
                    }
                };

                // 1 - d
                // Summary: In all three parts (A–C), the operations require a linear scan of the queue and use temporary data structures of size proportional to the input.
                // Thus, time complexity is O(n) and space complexity is O(n).

                // 2-3 בע"פ

                // 6 - a
                // Lambda that receives a binary tree of integers and returns the sum of all leaf values
                Function<BinNode<Integer>, Integer> sumLeaves = (root) -> {
                    // If the tree is empty → return 0
                    if (root == null) return 0;

                    // Initialize sum
                    var sum = 0;

                    // Queue for BFS traversal
                    var qu = new LinkedList<BinNode<Integer>>();
                    qu.offer(root);

                    // Iterate through the tree
                    while (!qu.isEmpty()) {
                        // Current node
                        var current = qu.poll();

                        // If it is a leaf → add its value to sum
                        if (!current.hasLeft() && !current.hasRight()) {
                            sum += current.getValue();
                        }

                        // Add children if they exist
                        if (current.hasLeft()) qu.offer(current.getLeft());
                        if (current.hasRight()) qu.offer(current.getRight());
                    }

                    // Return total sum of leaf values
                    return sum;
                };


                // 6 - b
                // A function that creates a queue of NodeInfo objects from a binary tree
                Function<BinNode<Integer>, Queue<NodeInfo>> createQueueOfNodesInfo = (root) -> {
                    // Final queue that will contain the NodeInfo objects
                    var finalQueue = new LinkedList<NodeInfo>();

                    // Queue used for traversing the binary tree (BFS)
                    var searchQueue = new LinkedList<BinNode<Integer>>();
                    searchQueue.offer(root);

                    // Iterate while there are nodes in the search queue
                    while (!searchQueue.isEmpty()) {
                        // Get the current binary node
                        var currentBinNode = searchQueue.poll();
                        // Get the value of the current node
                        var currentBinNodeValue = currentBinNode.getValue();
                        // String describing the type of node
                        var whatIsIt = " ";

                        // Check if the node is a leaf
                        if (!currentBinNode.hasLeft() && !currentBinNode.hasRight()) {
                            whatIsIt = "leaf";
                        }
                        // Otherwise, it's an internal node
                        else if (currentBinNode.hasRight() || currentBinNode.hasLeft()) {
                            whatIsIt = "internal";
                        }

                        // Add a new NodeInfo object to the final queue
                        finalQueue.offer(new NodeInfo(currentBinNodeValue, whatIsIt));

                        // Add the left child to the search queue if it exists
                        if (currentBinNode.hasLeft()) {
                            searchQueue.offer(currentBinNode.getLeft());
                        }

                        // Add the right child to the search queue if it exists
                        if (currentBinNode.hasRight()) {
                            searchQueue.offer(currentBinNode.getRight());
                        }
                    }

                    // Return the queue of NodeInfo objects
                    return finalQueue;
                };

                // 6 - c
                // Lambda that receives a binary tree of integers and returns the maximum leaf value
                Function<BinNode<Integer>, Integer> maxLeafValue = (root) -> {
                    // If the tree is empty → return smallest possible value
                    if (root == null) return Integer.MIN_VALUE;

                    // Track maximum leaf value
                    var max = Integer.MIN_VALUE;

                    // Queue for BFS traversal
                    var qu = new LinkedList<BinNode<Integer>>();
                    qu.offer(root);

                    // Iterate through the tree
                    while (!qu.isEmpty()) {
                        // Current node
                        var current = qu.poll();

                        // If it is a leaf → check for maximum
                        if (!current.hasLeft() && !current.hasRight()) {
                            if (current.getValue() > max) {
                                max = current.getValue();
                            }
                        }

                        // Add children if they exist
                        if (current.hasLeft()) qu.offer(current.getLeft());
                        if (current.hasRight()) qu.offer(current.getRight());
                    }

                    // Return maximum leaf value found
                    return max;
                };

                // 7 - a
                // Lambda that checks if all values in q1 are smaller than all values in q2
                BiFunction<Queue<Integer>, Queue<Integer>, Boolean> isSmaller = (q1, q2) -> {
                    // Temporary queues for restoration
                    var restore1 = new LinkedList<Integer>();
                    var restore2 = new LinkedList<Integer>();

                    // Track maximum of q1 and minimum of q2
                    var maxQ1 = Integer.MIN_VALUE;
                    var minQ2 = Integer.MAX_VALUE;

                    // Iterate through q1 to find maximum
                    while (!q1.isEmpty()) {
                        var current = q1.poll();
                        if (current > maxQ1) maxQ1 = current;
                        restore1.offer(current);
                    }

                    // Iterate through q2 to find minimum
                    while (!q2.isEmpty()) {
                        var current = q2.poll();
                        if (current < minQ2) minQ2 = current;
                        restore2.offer(current);
                    }

                    // Restore q1
                    while (!restore1.isEmpty()) q1.offer(restore1.poll());
                    // Restore q2
                    while (!restore2.isEmpty()) q2.offer(restore2.poll());

                    // Return true if max of q1 < min of q2
                    return maxQ1 < minQ2;
                };

                // 7 - b
                // Lambda that checks if a binary tree of queues of integers is an "excellent tree"
                Function<BinNode<Queue<Integer>>, Boolean> isExcellentTree = (root) -> {
                    // Empty tree is considered excellent
                    if (root == null) return true;

                    // Queue for BFS traversal
                    var searchQueue = new LinkedList<BinNode<Queue<Integer>>>();
                    searchQueue.offer(root);

                    // Iterate through all nodes
                    while (!searchQueue.isEmpty()) {
                        var current = searchQueue.poll();

                        // If current is not a leaf → must have 2 children
                        if (current.hasLeft() || current.hasRight()) {
                            if (!current.hasLeft() || !current.hasRight()){ return false; }

                            var parentQ = current.getValue();
                            var leftQ = current.getLeft().getValue();
                            var rightQ = current.getRight().getValue();

                            // Use previous lambda isSmaller to check order constraints
                            if (!isSmaller.apply(leftQ, parentQ)
                                    || !isSmaller.apply(parentQ, rightQ)){
                                        return false;
                            }

                            // offer the children to the search queue for further checking
                            if (current.hasLeft()) { searchQueue.offer(current.getLeft()); }
                            if (current.hasRight()) { searchQueue.offer(current.getRight()); }
                        }
                    }

                    // If all checks passed → tree is excellent
                    return true;
                };

                // c
                // Statement: "The smallest value in an excellent tree is found in the leftmost leaf."
                // Explanation: This statement is true.
                // Reason: By definition, every parent has values greater than all values in its left child.
                // This property guarantees that the smallest values are always pushed down leftwards,
                // and the absolute smallest is located in the leftmost leaf node.

                // d
                // Complexity analysis:
                // - isSmaller: Each queue is scanned fully once → O(n + m) where n = |q1|, m = |q2|.
                // - isExcellentTree: Each node is visited once, and for each node we may check its queues with isSmaller.
                //   If each queue length is k, total cost = O(N * k) where N = number of nodes.
                // Overall, both solutions are linear in terms of input size (number of elements processed).


                // 9
                // a
                // Explanation of the given functions:
                //
                // isExist1: Checks if element x exists in the stack st by popping elements.
                //           - Problem: it returns after the first comparison (inside while), so it only checks the top element.
                //           - Complexity: O(1), but incorrect logic.
                //
                // isExist2: Checks if element x exists in the stack st by using a temporary stack to restore elements.
                //           - Iterates through all elements, sets flag f = true if x is found.
                //           - Restores st completely.
                //           - Complexity: O(n), correct logic.


                // b
                // Lambda that checks if all elements of st1 exist in st2 (ignoring order)
                BiFunction<Stack<Integer>, Stack<Integer>, Boolean> allElementsExist = (st1, st2) -> {
                    // Temporary stacks for restoration
                    var restore1 = new Stack<Integer>();
                    var restore2 = new Stack<Integer>();

                    // Copy all elements from st2 into restore2
                    while (!st2.isEmpty()) restore2.push(st2.pop());

                    // Flag for overall check
                    boolean result = true;

                    // Check each element in st1
                    while (!st1.isEmpty()) {
                        var current = st1.pop();
                        restore1.push(current);

                        boolean found = false;
                        // Traverse restore2 to check for existence
                        var temp = new Stack<Integer>();
                        while (!restore2.isEmpty()) {
                            var c2 = restore2.pop();
                            if (c2.equals(current)) found = true;
                            temp.push(c2);
                        }
                        // Restore restore2
                        while (!temp.isEmpty()) restore2.push(temp.pop());

                        if (!found) result = false;
                    }

                    // Restore st1
                    while (!restore1.isEmpty()) st1.push(restore1.pop());
                    // Restore st2
                    while (!restore2.isEmpty()) st2.push(restore2.pop());

                    return result;
                };


                // c
                // Lambda that checks if all elements of st1 appear in st2 in the same order
                BiFunction<Stack<Integer>, Stack<Integer>, Boolean> sameOrder = (st1, st2) -> {
                    // Temporary copies for safe traversal
                    var temp1 = new Stack<Integer>();
                    var temp2 = new Stack<Integer>();
                    var restore1 = new Stack<Integer>();
                    var restore2 = new Stack<Integer>();

                    // Reverse st1 into temp1
                    while (!st1.isEmpty()) temp1.push(st1.pop());
                    // Reverse st2 into temp2
                    while (!st2.isEmpty()) temp2.push(st2.pop());

                    boolean result = true;
                    while (!temp1.isEmpty()) {
                        var c1 = temp1.pop();
                        restore1.push(c1);

                        if (temp2.isEmpty()) { result = false; break; }
                        var c2 = temp2.pop();
                        restore2.push(c2);

                        if (!c1.equals(c2)) { result = false; break; }
                    }

                    // Restore st1
                    while (!restore1.isEmpty()) st1.push(restore1.pop());
                    // Restore st2
                    while (!restore2.isEmpty()) st2.push(restore2.pop());
                    while (!temp2.isEmpty()) st2.push(temp2.pop());

                    return result;
                };


                // d
                // Lambda that checks if st1 is a subsequence (sub-stack) of st2 in consecutive order
                BiFunction<Stack<Integer>, Stack<Integer>, Boolean> isSubStack = (st1, st2) -> {
                    // Convert st1 into list for easier handling
                    var temp1 = new Stack<Integer>();
                    var restore1 = new Stack<Integer>();
                    var list1 = new LinkedList<Integer>();

                    while (!st1.isEmpty()) {
                        var c = st1.pop();
                        temp1.push(c);
                    }
                    while (!temp1.isEmpty()) {
                        var c = temp1.pop();
                        list1.add(c);
                        st1.push(c);
                    }

                    // Convert st2 into list
                    var temp2 = new Stack<Integer>();
                    var restore2 = new Stack<Integer>();
                    var list2 = new LinkedList<Integer>();

                    while (!st2.isEmpty()) {
                        var c = st2.pop();
                        temp2.push(c);
                    }
                    while (!temp2.isEmpty()) {
                        var c = temp2.pop();
                        list2.add(c);
                        st2.push(c);
                    }

                    // Sliding window search
                    boolean found = false;
                    for (int i = 0; i <= list2.size() - list1.size(); i++) {
                        boolean match = true;
                        for (int j = 0; j < list1.size(); j++) {
                            if (!list2.get(i + j).equals(list1.get(j))) {
                                match = false;
                                break;
                            }
                        }
                        if (match) { found = true; break; }
                    }

                    return found;
                };


                // e
                // Complexity analysis:
                // - allElementsExist: For each element in st1, we scan st2 fully → O(n * m).
                // - sameOrder: Linear, O(min(n,m)).
                // - isSubStack: Sliding window search in lists → O(n * m).

            });
            //The End
            q.offer(() -> {
                System.out.println("Java <--> JVM <--> ByteCode(mechine code) /or/ JNI(Bridge interface) <--> native code (c,c++) <--> Assembly(mechine code)? ");
                System.out.println("want more information? Y/N");
                var an = scanner.next();
                if (an.equalsIgnoreCase("Y")) {
                    var desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(new URI("https://github.com/openjdk/jdk/blob/master/src/java.base/windows/native/libjava/io_util_md.c"));
                    }
                    catch (Exception e) { System.out.println(e.getCause()); }
                }
                System.out.println("🚀💻 bye for now, have a good day!");
                System.out.println("!----------------------------------------------------------!");
            });

            //excute all the tasks in the queue
            while (!q.isEmpty()) {
                //poll out the function from the queue
                Runnable task = q.poll();
                //invoke the task
                if (task != null) { task.run(); }
            }

            //--------------------------------------------------------//
        }
        scanner.close();
    }
}

//uwu(happy)    u_u(sad)    u..u(sleep)    u*u(mad)    u0u(third eye)    u$u(rich)    u^u(excited)    u``u(emo)
// (●'◡'●)   (❁´◡`❁)   ^_~   ^0^   o(*^＠^*)o   o(*^▽^*)┛   o(*￣︶￣*)o   ヾ(＠⌒ー⌒＠)ノ
