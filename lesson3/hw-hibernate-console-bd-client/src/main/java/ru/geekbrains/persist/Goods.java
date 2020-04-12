package ru.geekbrains.persist;

import javax.persistence.*;

@Entity
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private int price;

    @ManyToOne
    private Buyer buyer;

    public Goods() {
    }

    public Goods(String title, int price) {
        this.title = title;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

//    @Override //вызывает зацикливание пары buyer<->goods
//    public String toString() {
//        return "Goods{" +
//                "id=" + id +
//                ", title='" + title + '\'' +
//                ", price=" + price +
//                ", buyer=" + buyer +
//                '}';
//    }
    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", buyer_id=" + buyer.getId() +
                '}';
    }
    //Goods{id=31, title='good11', price=11, buyer_id=11}
    //Goods{id=32, title='good12', price=12, buyer_id=11}
    //Goods{id=33, title='good13', price=13, buyer_id=11}
    //Goods{id=34, title='good21', price=21, buyer_id=12}
    //Goods{id=35, title='good22', price=22, buyer_id=12}
    //Goods{id=36, title='good23', price=23, buyer_id=12}
}
