package ru.geekbrains.persist;

public class Product {

    private int id;
    private String title;
//    private float price;
    private String price;

    public Product() {
    }

    public Product(String title, String price) {
        this.title = title;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
