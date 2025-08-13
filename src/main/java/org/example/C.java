package org.example;

public class C extends B{
    public C (int num){
        super(num);
    }

    @Override
    public boolean equals(Object other){
        return (other != null) && (other instanceof C) && (num == ((C) other).num);
    }
}
