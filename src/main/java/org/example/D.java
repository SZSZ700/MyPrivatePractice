package org.example;

public class D extends B{
    public D(int num){
        super(num + 1);
    }

    //OverLoad
    public boolean equals(D other){
        return (other != null) && (num == other.num);
    }
}
