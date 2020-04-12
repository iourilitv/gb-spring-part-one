package ru.geekbrains;

import org.hibernate.cfg.Configuration;
import ru.geekbrains.persist.Buyer;
import ru.geekbrains.persist.Goods;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller {
    private Scanner scanner = new Scanner(System.in);
    private EntityManager em;

    public Controller() {
        EntityManagerFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
        em = factory.createEntityManager();
    }

    void run() {
        Buyer buyer1 = newBuyer1();
        insertBuyer(buyer1);

        Buyer buyer2 = newBuyer2();
        insertBuyer(buyer2);

        List<Buyer> buyers = getBuyersList();
        buyers.forEach(System.out::println);

        List<Goods> goodsList1 = getBuyerGoods(buyer1);
        goodsList1.forEach(System.out::println);

        List<Goods> goodsList2 = getBuyerGoods(buyer2);
        goodsList2.forEach(System.out::println);

        em.close();
    }

//    void run() {
//        Buyer buyer1 = newBuyer1();
//        insertBuyer(buyer1);
//
//        Buyer buyer2 = newBuyer2();
//        insertBuyer(buyer2);
//
//        List<Buyer> buyers = getBuyersList();
//        buyers.forEach(System.out::println);
//
//        List<Goods> goodsList1 = getBuyerGoods(buyer1);
//        goodsList1.forEach(System.out::println);
//
//        List<Goods> goodsList2 = getBuyerGoods(buyer2);
//        goodsList2.forEach(System.out::println);
//
////        deleteBuyerGoods(buyer1);
//
//        em.close();
//    }

    Buyer newBuyer1() {
        List<Goods> goods = new ArrayList<>();
        goods.add(new Goods("good11", 11));
        goods.add(new Goods("good12", 12));
        goods.add(new Goods("good13", 13));
        return new Buyer("Ivan", "Ivanov", goods);
    }

    Buyer newBuyer2() {
        List<Goods> goods = new ArrayList<>();
        goods.add(new Goods("good21", 21));
        goods.add(new Goods("good22", 22));
        goods.add(new Goods("good23", 23));
        return new Buyer("Petr", "Petrov", goods);
    }

    void insertBuyer(Buyer buyer) {
        em.getTransaction().begin();
        try{
//            if(!em.contains(buyer)) { //не находит и создает дубликат
            if(isBuyerNotExist(buyer)) { //true и не создает даже, если нет
//            if(isBuyerNotExist()) {
                em.persist(buyer);
                em.getTransaction().commit();
            }
        } catch(Exception e) {
            em.getTransaction().rollback();
        }

        Buyer buyerCopy = new Buyer();
        buyerCopy.setId(buyer.getId());
        //добавляем копию в таблицу контактов
        buyer.getGoodsList().forEach(goods -> goods.setBuyer(buyerCopy));
        em.getTransaction().begin();
        try{
            em.getTransaction().commit();
        } catch(Exception e) {
            em.getTransaction().rollback();
        }
    }

    private List<Buyer> getBuyersList() {
        return em.createQuery("from Buyer").getResultList();
    }

    private List<Goods> getBuyerGoods(Buyer buyer) {
        return em.createQuery("from Goods g where g.buyer = buyer").getResultList();
    }

//    private boolean isBuyerNotExist(Buyer buyer) {
//        return em.createQuery(
//                "from Buyer b where b.firstName = " + buyer.getFirstName() +
//                        " or b.lastName = " + buyer.getLastName())
//                .getResultList().isEmpty();
//    }
    private boolean isBuyerNotExist(Buyer buyer) {
        System.out.println("buyer.getFirstName(): " + buyer.getFirstName());
        System.out.println("buyer.getLastName(): " + buyer.getLastName());
//        Query query = em.createQuery("from Buyer b where b.firstName = :firstName or " +
//                "b.lastName = :lastName");
        Query query = em.createNativeQuery("from Buyer b where b.firstName = :firstName or " +
                "b.lastName = :lastName");
        query.setParameter("firstName", buyer.getFirstName());
        query.setParameter("lastName", buyer.getLastName());

        System.out.println("query: " + query.toString());
        boolean res = query.getResultList().isEmpty();
        System.out.println("res: " + res);
//        return query.getResultList().isEmpty();
        return res;
    }

//    private boolean isBuyerNotExist() {
//        return em.createQuery(
//                "from Buyer b where b.firstName = firstName or b.lastName = lastName")
//                .getResultList().isEmpty();
//    }

}
