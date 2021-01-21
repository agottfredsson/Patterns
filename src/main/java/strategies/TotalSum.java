package strategies;

import shop.ShoppingCartItem;
import java.math.BigDecimal;
import java.util.ArrayList;

public class TotalSum implements Discount {
    @Override
    public BigDecimal discount(ArrayList<ShoppingCartItem> items) {

        var sum = BigDecimal.ZERO;
        for (var item : items) {
            sum = item.itemCost().multiply(BigDecimal.valueOf(item.quantity())).add(sum);
        }

        if (sum.intValue() > 500) {
            sum = sum.multiply(BigDecimal.valueOf(0.9));
        }

        return sum;
    }
}
