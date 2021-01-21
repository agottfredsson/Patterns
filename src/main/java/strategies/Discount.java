package strategies;

import shop.ShoppingCartItem;

import java.math.BigDecimal;
import java.util.ArrayList;

public interface Discount {
    BigDecimal discount(ArrayList<ShoppingCartItem> items);
}
