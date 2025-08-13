package org.example;

//מחלקת ניהול שירות
public class Support {
    public static final int len = 15;
    private Service [] emdot = new Service[len];//מערך עמדות

    //פונק פרטית המחזירה את העמדה עם הכי פחות ממתינים
    private Service lessCallers(){
        Service temp = null;//אובייקט עמדה זמני שיכיל בהמשך את העמדה בעלת מס הממתינים הנמוך ביותר
        int min = Integer.MAX_VALUE;//משתנה מינימום לשמירת מס הממתינים לעמדה הקטן ביותר

        //מעבר על מערך העמדות
        for (int i = 0; i < this.emdot.length; i++) {
            //אם מס הממתינים בעמדה נוכחית זו קטן מהערך השמור במישתנה המינימום
            if (this.emdot[i].getNumOfCallers() < min){
                //נקבע ערך חדש במשתנה המינימום והוא מס הממתינים בעמדה נוכחית זו
                min = emdot[i].getNumOfCallers();
                temp = emdot[i];//נשמור את אובייקט העמדה
            }
        }

        return temp;//נחזיר את אובייקט העמדה
    }

    //פונק המחפשת את העמדה עם מס הממתינים הנמוך ביותר ומוסיפה אלייה ממתין חדש
    public void startSupport(String name, String phoneNumber){
        Caller c1 = new Caller(name, phoneNumber);//יצירת ממתין חדש

        Service whichOne = lessCallers();//איזה עמדה יש בה הכי פחות ממתינים

        whichOne.addCaller(c1);//קריאה לפונק להוספת ממתין לעמדה

        int numOfStand = whichOne.getNum();//מס עמדה

        System.out.print("stand n.o: "+numOfStand);//הדפס מס עמדה
    }

    //פונק המחזירה את העמדה שנתנה הכי הרבה שירות לממתינים-לקוחות
    public int theBest(){
        int max = Integer.MIN_VALUE;//משתנה לשמירת כמות הממתינים בעמדה שנתנה הכי הרבה שירות
        int whoIsTheBest = -1;//משתנה שיכיל את מס העמדה הטובה ביותר

        //מעבר על מערך העמדות
        for (int i = 0; i < this.emdot.length; i++) {
            //אם נמצאה עמדה בה מס הממתינים לרגע זה הוא הגבוהה יותר
            if (emdot[i].getCountAll() > max){
                max = emdot[i].getCountAll();//שמירת מס הממתינים בעמדה זו
                whoIsTheBest = emdot[i].getNum();//שמירת מס עמדה
            }
        }

        return whoIsTheBest;//החזרת מס העמדה הטובה ביותר
    }

    public Service[] getEmdot() {
        return emdot;
    }

    public void setEmdot(Service[] emdot) {
        this.emdot = emdot;
    }
}
