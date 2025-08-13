package org.example;

public class MailItem {
    protected String name;
    protected String address;
    protected Double price;

    public MailItem(String name, String address, double price) {
        this.name = name;
        this.address = address;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getRealPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "MailItem{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", price=" + price +
                '}';
    }
}
