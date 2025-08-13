package org.example;

public class B1 extends A {

    public B1(int n) { super(n); }

    //OverLoad
    public boolean f(B b) {
        System.out.println("F in B");
        return num == b.num;
    }
}
