import command.CommandManager;
import shop.Product;
import shop.ShoppingCart;
import shop.ShoppingCartItem;
import strategies.*;

public class main {

    public static void main(String[] args) {
        CommandManager manager = new CommandManager();
        ShoppingCart shoppingCart = new ShoppingCart(manager);

        ShoppingCartItem item1 = new ShoppingCartItem(new Product("TV"), 123, 1, manager);
        ShoppingCartItem item2 = new ShoppingCartItem(new Product("Kola"), 1533, 3, manager);
        ShoppingCartItem item3 = new ShoppingCartItem(new Product("Mango"), 100, 1, manager);


        shoppingCart.addCartItem(item1);
        shoppingCart.addCartItem(item2);
        shoppingCart.addCartItem(item3);

        shoppingCart.addDiscount(new GetOneFree());
        shoppingCart.addDiscount(new ThreeForTwo());
        shoppingCart.addDiscount(new TotalSum());


        System.out.println(shoppingCart.receipt());
    }
}
