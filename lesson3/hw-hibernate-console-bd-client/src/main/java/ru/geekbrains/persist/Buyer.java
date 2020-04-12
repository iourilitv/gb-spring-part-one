package ru.geekbrains.persist;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Buyer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @OneToMany(
            mappedBy = "buyer",
            cascade = CascadeType.ALL
    )
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
