package ru.geekbrains;

import org.hibernate.cfg.Configuration;
import ru.geekbrains.persist.Buyer;
import ru.geekbrains.persist.Goods;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private EntityManager em;
    private Buyer buyer1;
    private Buyer buyer2;

    public Controller() {
        EntityManagerFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
        em = factory.createEntityManager();
    }

    void init() {
        buyer1 = newBuyer1();
        buyer2 = newBuyer2();
    }

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

    public void insertBuyer(Buyer buyer) {
        //избежать добавления дубликатов
        Long id = findBuyerId(buyer);
        System.out.println("Long id = findBuyerId(buyer): " + id);
        boolean res = isBuyerNotExist(id);
        System.out.println("boolean res = isBuyerNotExist(id): " + res);

        if(!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        try{
            //избежать добавления дубликатов
            if(res) {
                em.persist(buyer);
                em.getTransaction().commit();
            } else {
                return;
            }
        } catch(Exception e) {
            em.getTransaction().rollback();
        }
        //TODO только этим костылем удалось заполнить поле buyer_id в таблице goods
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
    //TODO сохраняет в БД, но в таблице goods поля buyer_id пустое
//    void insertBuyer(Buyer buyer) {
//        em.getTransaction().begin();
//        try{
//            em.persist(buyer);
//            em.getTransaction().commit();
//        } catch(Exception e) {
//            em.getTransaction().rollback();
//        }
//    }

    private List<Buyer> getBuyersList() {
        return em.createQuery("from Buyer").getResultList();
    }

    private List<Goods> getBuyerGoods(Buyer buyer) {
        Long id = findBuyerId(buyer);
        Query query = em.createQuery("from Goods g where g.buyer.id = :id");
        query.setParameter("id", id);
        return (List<Goods>)query.getResultList();
    }

    public void deleteBuyerGoods(Buyer buyer) {
        Long id = findBuyerId(buyer);
        Query query = em.createQuery("delete from Goods g where g.buyer.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    public Long findBuyerId(Buyer buyer){
        Query query = em.createQuery("from Buyer b " +
                "where b.firstName = :firstName and b.lastName = :lastName");
        query.setParameter("firstName", buyer.getFirstName());
        query.setParameter("lastName", buyer.getLastName());
        //если в таблице есть дубликаты
        List<Buyer> buyerList = (List<Buyer>)query.getResultList();
        Buyer buyer3;
        if(!buyerList.isEmpty()) {
            buyer3 = buyerList.get(0);
            return buyer3.getId();
        } else {
            return null;
        }
    }

    private boolean isBuyerNotExist(Long buyerId) {
        Query query = em.createQuery("from Buyer b where b.id = :id");
        query.setParameter("id", buyerId);
        List<Buyer> buyerList = (List<Buyer>)query.getResultList();
        return buyerList.isEmpty();
    }

    public void shutdown() {
        em.close();
    }

    public void printBuyersList() {
        List<Buyer> buyers = getBuyersList();
        buyers.forEach(System.out::println);
    }

    public void printBuyerGoods(Buyer buyer) {
        List<Goods> goodsList = getBuyerGoods(buyer);
        goodsList.forEach(System.out::println);
    }

    public Buyer getBuyer1() {
        return buyer1;
    }

    public Buyer getBuyer2() {
        return buyer2;
    }

}

//
//    void init() {
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
//        em.close();
//    }

//
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

//
//    private boolean isBuyerNotExist(Buyer buyer) {
//        return em.createQuery(
//                "from Buyer b where b.firstName = " + buyer.getFirstName() +
//                        " or b.lastName = " + buyer.getLastName())
//                .getResultList().isEmpty();
//    }
//    private boolean isBuyerNotExist(Buyer buyer) {
//        System.out.println("buyer.getFirstName(): " + buyer.getFirstName());
//        System.out.println("buyer.getLastName(): " + buyer.getLastName());
////        Query query = em.createQuery("from Buyer b where b.firstName = :firstName or " +
////                "b.lastName = :lastName");
//        Query query = em.createNativeQuery("from Buyer b where b.firstName = :firstName or " +
//                "b.lastName = :lastName");
//        query.setParameter("firstName", buyer.getFirstName());
//        query.setParameter("lastName", buyer.getLastName());
//
//        System.out.println("query: " + query.toString());
//        boolean res = query.getResultList().isEmpty();
//        System.out.println("res: " + res);
////        return query.getResultList().isEmpty();
//        return res;
//    }

//    private boolean isBuyerNotExist() {
//        return em.createQuery(
//                "from Buyer b where b.firstName = firstName or b.lastName = lastName")
//                .getResultList().isEmpty();
//    }