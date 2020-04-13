package ru.geekbrains;

public class ConsoleApp {

    public static void main(String[] args) {
        Controller controller = new Controller();
        System.out.println("***initialising***");
        controller.init();
        controller.insertBuyer(controller.getBuyer1());
        controller.insertBuyer(controller.getBuyer2());
        controller.printBuyersList();

        System.out.println("***trying to make a duplicate of Buyer1***");
        controller.insertBuyer(controller.getBuyer1());
        controller.printBuyersList();

        System.out.println("***printing a goods list of Buyer1***");
        controller.printBuyerGoods(controller.getBuyer1());
        System.out.println("***printing a goods list of Buyer2***");
        controller.printBuyerGoods(controller.getBuyer2());

        System.out.println("***deleting a goods list of Buyer1***");
        controller.deleteBuyerGoods(controller.getBuyer1());
        System.out.println("***After deleting. printing a goods list of Buyer1***");
        controller.printBuyerGoods(controller.getBuyer1());
        System.out.println("***After deleting. printing a goods list of buyers***");
        controller.printBuyersList();

        controller.shutdown();
    }
}
//result
//***initialising***
//Buyer{id=1, firstName='Ivan', lastName='Ivanov', goodsList=[Goods{id=1, title='good11', price=11, buyer_id=1}, Goods{id=2, title='good12', price=12, buyer_id=1}, Goods{id=3, title='good13', price=13, buyer_id=1}]}
//Buyer{id=2, firstName='Petr', lastName='Petrov', goodsList=[Goods{id=4, title='good21', price=21, buyer_id=2}, Goods{id=5, title='good22', price=22, buyer_id=2}, Goods{id=6, title='good23', price=23, buyer_id=2}]}
//***printing a goods list of Buyer1***
//Goods{id=1, title='good11', price=11, buyer_id=1}
//Goods{id=2, title='good12', price=12, buyer_id=1}
//Goods{id=3, title='good13', price=13, buyer_id=1}
//***printing a goods list of Buyer2***
//Goods{id=4, title='good21', price=21, buyer_id=2}
//Goods{id=5, title='good22', price=22, buyer_id=2}
//Goods{id=6, title='good23', price=23, buyer_id=2}
//***deleting a goods list of Buyer1***
//***After deleting. printing a goods list of buyers***
//Buyer{id=1, firstName='Ivan', lastName='Ivanov', goodsList=[Goods{id=1, title='good11', price=11, buyer_id=1}, Goods{id=2, title='good12', price=12, buyer_id=1}, Goods{id=3, title='good13', price=13, buyer_id=1}]}
//Buyer{id=2, firstName='Petr', lastName='Petrov', goodsList=[Goods{id=4, title='good21', price=21, buyer_id=2}, Goods{id=5, title='good22', price=22, buyer_id=2}, Goods{id=6, title='good23', price=23, buyer_id=2}]}