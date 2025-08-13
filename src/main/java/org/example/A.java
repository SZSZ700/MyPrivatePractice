package org.example;

public class A extends Object {
    protected int num;

    public A(int n) { num = n; }

    public int getNum() { return num; }

    public boolean f(A a) {
        System.out.println("F in A");
        return num == a.num * 2;
    }
}
