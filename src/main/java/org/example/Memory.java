package org.example;

public class Memory {
    private final Node<Data> start;

    public Memory(int totalSize) {
        this.start = new Node<>(new Data(totalSize));
    }

    private int totalMemorySize(){
        Node<Data> pos = this.start;
        int sum = 0;
        while (pos != null){
            sum += pos.getValue().getSize();
            pos = pos.getNext();
        }
        return sum;
    }

    public boolean isInDanger(){
        Node<Data> pos = this.start;
        int sum = 0;
        while (pos != null){
            if (pos.getValue().isFree()) {
                sum += pos.getValue().getSize();
            }
            pos = pos.getNext();
        }

        int totalMemory = totalMemorySize();

        return sum < totalMemory * 0.10;
    }

    public boolean first(int num){
        if (isInDanger()){return false;}
        Node<Data> pos = this.start;

        while (pos.getNext() != null){
            if (pos.getNext().getValue().getSize() >= num){
                Node<Data> newey = new Node<>(new Data(num));//נוסיף חוליה חדשה
                newey.getValue().setFree(false);//ונשנה אותה ללא פנוייה

                //חיבור חולית מקטע הזיכרון החדשה התפוסה ביניהם
                newey.setNext(pos.getNext());
                pos.setNext(newey);

                //עדכון מצב מקטע הזיכרון של החולייה הבאה אחרי החולית מקטע תפוס שהוספתי
                pos.getNext().getNext().getValue().setSize(pos.getNext().getNext().getValue().getSize()-num);
                //קביעת מצב החולייה הבאה אחרי החוליית מקטע תפוס החדשה שהוספנו, להיות נוייה או לא בהתאם לבדיקה הבאה
                if (pos.getNext().getNext().getValue().getSize() == 0){
                    pos.getNext().getNext().getValue().setFree(false);
                }
                return true;
            }
            pos = pos.getNext();
        }
        return false;
    }
}
