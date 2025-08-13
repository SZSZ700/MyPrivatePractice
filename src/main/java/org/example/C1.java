package org.example;

public class C1 extends A {

    public C1 (int n) {
        super(n);
        System.out.println("F in C");
    }

    @Override
    public boolean f(A a) {
        return a instanceof C1 && num == a.num;
    }
}
