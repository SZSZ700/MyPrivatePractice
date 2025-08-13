package org.example;

public class Service {
    private Node<Caller> callers;//שרשרת ממתינים
    private int num;//מס עמדה
    private int numOfCallers;//מס ממתינים בעמדה
    private int countAll;

    //בנאי עמדה
    public Service(Node<Caller> callers, int num) {
        this.callers = callers;
        this.num = num;
        this.numOfCallers = 0;
        this.countAll = 0;
    }

    //החזרת שרשרת הממתינים
    public Node<Caller> getCallers() {
        return callers;
    }

    //קביעת שרשרת הממתינים
    public void setCallers(Node<Caller> callers) {
        this.callers = callers;
    }

    //החזרת מס עמדה
    public int getNum() {
        return num;
    }

    //קביעת מס עמדה
    public void setNum(int num) {
        this.num = num;
    }

    //הדפסת פרטי עמדה
    @Override
    public String toString(){
        System.out.print("stand number: "+this.num);//הדפסת מס עמדה
        System.out.print("num of callers: "+this.numOfCallers);//הדפסת מס הממתינים לעמדה

        //פויינטר לריצה על שרשרת הממתינים לעמדה, והדפסת פרטיהם
        Node<Caller> pos = this.callers;
        while (pos != null){
            System.out.print(pos.getValue());//הדפסת הממתין הנוכחי
            pos = pos.getNext();//התקדם לממתין הבא
        }

        return "end";
    }

    //פונק המקבלת אובייקט ממתין ומוסיפה אותו לסוף שרשרת הממתינים
    public void addCaller(Caller c1){
        Node<Caller> toAdd = new Node<>(c1);//יצירת חוליית אובייקט להוספה
        Node<Caller> pos = this.callers;//פויינטר לשרשרת הממתיינים לעמדה

        //חיפוש זנב השרשרת
        while (pos.getNext() != null){
            pos = pos.getNext();
        }

        pos.setNext(toAdd);//הוספת חוליית אובייקט הממתין לסוף השרשרת

        this.numOfCallers += 1;//הגדלת מונה הממתינים באחד
    }

    //פונק המחזירה את הממתין הראשון בעמדה, ומסירה אותו
    public Caller callBack(){
        Caller temp = this.callers.getValue();//שמירת הממתין הראשון באובייקט זמני
        this.callers = this.callers.getNext();//קישור הפויינטר של השרשרת לחוליית האובייקט ממתין הבאה בשרשרת
        this.numOfCallers --;
        this.countAll ++;
        return temp;//החזרת הממתין
    }

    //החזרת מס הממתינים בעמדה נוכחית זו
    public int getNumOfCallers(){
        return this.numOfCallers;
    }


    public void setNumOfCallers(int numOfCallers) {
        this.numOfCallers = numOfCallers;
    }

    public int getCountAll() {
        return countAll;
    }

    public void setCountAll(int countAll) {
        this.countAll = countAll;
    }
}
