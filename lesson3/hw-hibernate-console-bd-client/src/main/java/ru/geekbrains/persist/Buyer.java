package ru.geekbrains.persist;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Buyer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String firstName;

    @Column(unique = true)
    private String lastName;

    //***из примера Unidirectional @OneToMany class Post поле List<PostComment> comments
    // https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/
    //Параметр orphanRemoval = true указывает, что все объекты Goods, которые
    // не имеют ссылки на Buyer, должны быть удалены из БД.
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "buyer_id")
    private List<Goods> goodsList = new ArrayList<>();

    public Buyer() {
    }

    public Buyer(String firstName, String lastName, List<Goods> goodsList) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.goodsList = goodsList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    @Override
    public String toString() {
        return "Buyer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", goodsList=" + goodsList +
                '}';
    }
}
