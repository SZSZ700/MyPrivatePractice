package org.example;
import java.util.Stack;

public class Tstack {
    private Stack<Integer> s0;
    private Stack<Integer> s1;
    private Stack<Integer> s2;

    //O(1)
    //0-1 1-2 2-0
    public void move(int from, int to){
        Stack<Integer> temp = new Stack<>();

        if (from == 0 && to == 1){
            if(!this.isEmpty(0)){
                s1.push(s0.pop());
                return;
            }
        } else if (from == 1 && to == 2) {
            if (!this.isEmpty(1)){
                s2.push(s1.pop());
                return;
            }
        }else if (from == 2 && to == 0){
            if (!this.s2.isEmpty()){
                s0.push(s2.pop());
                return;
            }
        }
        return;
    }

    //O(1)
    public boolean bigOrEqual(int from, int toCompare){
        if (from == 0 && toCompare == 1){
            if (s0.peek() > s1.peek()){
                return true;
            }
        } else if (from == 1 && toCompare == 2) {
            if (s1.peek() > s2.peek()){
                return true;
            }
        } else if (from == 2 && toCompare == 0) {
            if (s2.peek() > s0.peek()){
                return true;
            }
        }

        return false;
    }

    //O(1)
    public boolean isEmpty(int stackId){
        if (stackId == 0){
            return this.s0.isEmpty();
        } else if (stackId == 1) {
            return this.s1.isEmpty();
        } else if (stackId == 2) {
            return this.s2.isEmpty();
        }
        return false;
    }

    //O(n) -> move max val to s1
    public void maximum(){
        move(0,1);
        while (!this.isEmpty(0)){
            if (this.bigOrEqual(0,1)){
                move(1,2);
                move(0,1);
            }else {
                move(0,1);
                move(1,2);
            }
        }

        while (!this.isEmpty(2)){
            move(2,0);
        }
    }

    public void sort(){
        while (!this.isEmpty(0)){
            maximum();
        }

        while (!this.isEmpty(1)){
            move(1,2);
        }
    }
}
