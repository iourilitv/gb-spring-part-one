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
        //!!! в учебных целях - реально так не делают
        //потокобезопасный объект - точка входа для источника данных
        EntityManagerFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
        //!!! в учебных целях - реально так не делают, но здесь можно управлять процессом em
        //объект для отдельной операции(сессии) с БД(em не доступны между потоками)
        EntityManager em = factory.createEntityManager();

        //создаем коллекцию объектов контактов пользователя
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("Mobile phone", "1112233"));
        contacts.add(new Contact("Address", "Universe street 123"));
        Person person = new Person("Ivan", "Ivanov", LocalDate.of(1995, 2, 12), contacts);

        //*** создаем новую сущность ***
        //начинаем транзакцию работы с БД
        em.getTransaction().begin();
        try {
            //сохраняем объект в БД
            em.persist(person);
            //выполняем транзакцию
            em.getTransaction().commit();
        } catch (Exception ex) {
            //откатываем назад, если транзакция не завершится корректно
            em.getTransaction().rollback();
        }
        System.out.println("PersonExample - person: " + person);

        //*** Изменить сущность в той же сессии ***
        //меняем имя сущности
//        person.setFirstName("Petr");

        //инициируем копию текущего объекта person по его id в БД при сохранении
        Person personCopy = new Person(person.getId());
        //добавляем копию в таблицу контактов
        contacts.forEach(contact -> {
            if(contact.getId() != null) {
                contact.setPerson(personCopy);
//                contact.setPerson(person); //вызывает зацикленность
            }
        });

//        System.out.println("***contacts***");
//        contacts.forEach(System.out::println);

        em.getTransaction().begin();
        try {
            //выполняем транзакцию
            em.getTransaction().commit();
        } catch (Exception ex) {
            //откатываем назад, если транзакция не завершится корректно
            em.getTransaction().rollback();
        }
        System.out.println("PersonExample - person(изм.): " + personCopy);
        //PersonExample - person(изм.): Person{id=20, firstName='null', lastName='null', birthday=null, contacts=null}
//        System.out.println("PersonExample - person(изм.): " + person);
        //Exception in thread "main" java.lang.StackOverflowError

        //*** Создать новую сущность ***
//        person.setFirstName("Petr");
//        em.getTransaction().begin();
//        try {
//            //изменяем объект в БД
//            em.merge(person);
//            //выполняем транзакцию
//            em.getTransaction().commit();
//        } catch (Exception ex) {
//            //откатываем назад, если транзакция не завершится корректно
//            em.getTransaction().rollback();
//        }
//        System.out.println("PersonExample - person(изм.): " + person);

        //*** Извлечение данных сущности из таблицы по ее id***
        //Person.class - класс сущности,
        // 2L - идентификатор сущности(первичный ключ), здесь объект Long
//        Person person1 = em.find(Person.class, 2L);
        //Позволяет изменить сущьность в рамках той же сессии,
        // т.к. она возвращается в состояние managed
//        System.out.println("PersonExample - person1: " + person1);

//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        System.out.println("PersonExample - persons: ");

        //***обращаемся к сущности, а не к таблице, т.к. язык JPQL***
        //получаем список всех сущностей Person(аналог select в sql)
        //связанные таблицы запрашиваются отдельно для каждой сущьности, т.к.
        //@OneToMany - FetchType fetch() по умолчанию LAZY - связанные сущности автоматически не извлекаются.
//        List<Person> persons = em.createQuery("from Person").getResultList();

        //в одном запросе запрашиваем данные из двух связанных таблиц
//        List<Person> persons = em.createQuery("from Person p join fetch p.contacts c").getResultList();

        //
//        List<Person> persons = em.createQuery("from Person p join fetch p.contacts c where p.id > 5").getResultList();

//        persons.forEach(System.out::println);

        em.close();
    }

}
