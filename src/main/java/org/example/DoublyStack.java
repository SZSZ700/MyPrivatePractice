package org.example;
import java.util.Stack;

public class DoublyStack {
    private Stack<Integer> numbers;
    private Stack<Integer> sums;

    public DoublyStack(){
        this.numbers = new Stack<>();
        numbers.push(2);
        numbers.push(-1);
        numbers.push(4);
        numbers.push(3);
        numbers.push(-9);
        this.sums = new Stack<>();
        sums.push(2);
        sums.push(1);
        sums.push(5);
        sums.push(8);
        sums.push(-1);
    }

    public Stack<Integer> getNumbers() {
        return numbers;
    }

    public void setNumbers(Stack<Integer> numbers) {
        this.numbers = numbers;
    }

    public Stack<Integer> getSums() {
        return sums;
    }

    public void setSums(Stack<Integer> sums) {
        this.sums = sums;
    }

    public Stack<Integer> getNums(int x){
        Stack<Integer> stk = new Stack<>();
        Stack<Integer> temp = new Stack<>();

        while (!this.numbers.isEmpty()){
            stk.push(numbers.pop());
        }

        int sum = 0;

        while (!stk.isEmpty()){
            int a = stk.pop();//שלוף ערך נוכחי
            sum += a;//צבור אותו
            numbers.push(a);//שיחזור מחסנית מקור
            //אם הסכום שונה מX שהוא ערך ממחסנית הסכומים הכנס ערך נוכחי למחסנית
            if (sum <= x){
                temp.push(a);
            }
            if (sum == x){
                break;
            }
        }
        while (!stk.isEmpty()){
            numbers.push(stk.pop());
        }
        return temp;
    }

    public void removeFromNumbers(int num){
        Stack<Integer> temp = new Stack<>();

        while (!this.numbers.isEmpty()){
            int x = numbers.pop();
            if (x != num){
                temp.push(x);
            }
        }

        while (!temp.isEmpty()){
            this.numbers.push(temp.pop());
        }
    }

    public void eraseNum(int num){
        removeFromNumbers(num);
        Stack<Integer> temp = new Stack<>();

        while (!this.numbers.isEmpty()){
            temp.push(numbers.pop());
        }
        
        int sum = 0;//צובר
        this.sums = new Stack<>();//ריקון מחסנית סכומים

        int n = temp.pop();
        sum += n;
        sums.push(n);//בניית מחסנית סכומים מחדש
        numbers.push(n);//שיחזור מחסנית מקור

        while (!temp.isEmpty()){
            int x = temp.pop();
            sum += x;
            numbers.push(x);//שיחזור מחסנית מקור
            sums.push(sum);//בניית מחסנית סכומים מחדש
        }
    }

}
