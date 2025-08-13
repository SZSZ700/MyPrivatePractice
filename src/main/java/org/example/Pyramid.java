package org.example;

public class Pyramid {
    Node<Clown> pyramid;

    public Pyramid(Node<Clown> pyramid) {
        this.pyramid = pyramid;
    }

    public Node<Clown> getPyramid() {
        return pyramid;
    }

    public void setPyramid(Node<Clown> pyramid) {
        this.pyramid = pyramid;
    }

    //פונקציה הבודקת אם פירמידה היא יציבה
    public boolean isStable(){
        //פויינטר לראש השרשרת
        Node<Clown> pos = this.pyramid;

        //איטרציה
        while (pos.getNext() != null){
            //אם המשקל של הליצן הנוכחי גדול מהמשקל של הליצן הבא נחזיר שקר
            if (pos.getValue().getWeight() > pos.getNext().getValue().getWeight()){ return false; }

            //נתקדם לחולייה הבאה
            pos = pos.getNext();
        }

        //נחזיר אמת השרשרת ליצנים ממויינת בסדר עולה
        return true;
    }

    //כתבו פעולה בוליאנית המקבלת ליצן. על הפעולה להוסיף את הליצן לפירמידה כך שהיא תישאר יציבה
    public boolean addClown(Clown c){
        //אם הפירמידה אינה יציבה נחזיר שקר
        if (!isStable()){ return false; }

        //שמירת משקל של ליצן נוכחי
        int cWeight = c.getWeight();

        //יצירת חוליית ליצן חדש להוספה
        Node<Clown> toAdd = new Node<>(c);

        Node<Clown> pos = this.pyramid;

        //הוספה בהתחלה
        //אם המשקל של הליצן הנוכחי קטן ממשקל הליצן ההתחלתי, נוסיף אותו להתחלה ונחזיר אמת
        if (cWeight < this.pyramid.getValue().getWeight()){
            //הוספה בהתחלה
            toAdd.setNext(this.pyramid);
            //שינוי מצביע הראש
            this.pyramid = toAdd;
            //החזר אמת - הוספה בוצעה בהצלחה
            return true;
        }

        //הוספה באמצע
        while (pos.getNext() != null){
            //אם המשקל של הליצן להוספה גדול ממשקל הליצן הנוכחי וקטן ממשקל הליצן הבא אחריו נוסיף ונחזיר אמת
            if (cWeight > pos.getValue().getWeight() && cWeight < pos.getNext().getValue().getWeight()){
                //חיבור חולייה באמצע
                toAdd.setNext(pos.getNext());
                pos.setNext(toAdd);
                //החזר אמת - הוספה בוצעה בהצלחה
                return true;
            }
        }

        //הוספה בסוף
        //אם משקל הליצן החדש להוספה גבוה יותר מהליצן שבתחתית/בסוף נחזיר נוסיף ליצן חדש זה ונחזיר אמת
        if (cWeight > pos.getValue().getWeight()){
            //הוספה לסוף
            pos.setNext(toAdd);
            //החזר אמת - הוספה בוצעה בהצלחה
            return true;
        }

        //נחזיר שקר לא צלח הניסיון להוסיף ליצן לשרשרת הליצנים
        return false;
    }

}
