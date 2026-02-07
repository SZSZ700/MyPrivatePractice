package org.example;

public class NodeInfo {
    private int num;
    private String info;

    public NodeInfo(int num, String info) {
        this.num = num;
        this.info = info;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "NodeInfo{" +
                "num=" + num +
                ", info='" + info + '\'' +
                '}';
    }
}
