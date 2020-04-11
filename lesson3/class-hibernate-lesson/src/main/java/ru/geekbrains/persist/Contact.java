package ru.geekbrains.persist;

import javax.persistence.*;

@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String type;

    @Column
    private String info;

    @ManyToOne //чтобы hibernate создал связь между таблицами, иначе создаст отдельную таблицу для связей
//    @JoinColumn (name = "person_id" )
    private Person person;

    public Contact() {
    }

    public Contact(String type, String info) {
        this.type = type;
        this.info = info;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override //вызывает зацикленность person<->contacts при печати contacts
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", info='" + info + '\'' +
                ", person=" + person +
                '}';
    }
//    @Override //Exception in thread "main" java.lang.NullPointerException
//    public String toString() {
//        return "Contact{" +
//                "id=" + id +
//                ", type='" + type + '\'' +
//                ", info='" + info + '\'' +
//                ", person_id=" + person.getId() +
//                '}';
//    }
}
