package ru.geekbrains;

import org.hibernate.cfg.Configuration;
import ru.geekbrains.persist.Contact;
import ru.geekbrains.persist.Person;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersonExample {

    public static void main(String[] args) {
        EntityManagerFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();

        EntityManager em = factory.createEntityManager();

        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("Mobile phone", "1112233"));
        contacts.add(new Contact("Address", "Universe street 123"));
        Person person = new Person("Ivan", "Ivanov", LocalDate.of(1995, 2, 12), contacts);
        em.getTransaction().begin();

        try {
            em.persist(person);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
        System.out.println(person);

        Person person1 = em.find(Person.class, 2L);
        System.out.println(person1);

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        List<Person> persons = em.createQuery("from Person p join fetch p.contacts c where p.id > 5").getResultList();
        persons.forEach(System.out::println);

        em.close();
    }
}
