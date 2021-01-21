package shop;

import command.Command;
import command.CommandManager;
import strategies.Discount;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ShoppingCart {

    private ArrayList<ShoppingCartItem> items = new ArrayList<>();
    private ArrayList<Discount> discounts = new ArrayList<>();
    private CommandManager commandManager;
    BigDecimal bestDiscount = BigDecimal.ZERO;

    public ShoppingCart(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void addCartItem(ShoppingCartItem item) {
        items.add(item);

        Command undoCommand = new Command() {
            @Override
            public void execute() {
                items.remove(item);
            }

            @Override
            public void redo() {
                items.add(item);
            }
        };
        commandManager.addToUndo(undoCommand);
    }

    public void removeCartItem(ShoppingCartItem item) {
        items.remove(item);
        Command addCommand = new Command() {
            @Override
            public void execute() {
                items.add(item);
            }

            @Override
            public void redo() {
                items.remove(item);
            }
        };
        commandManager.addToUndo(addCommand);
    }

    public ArrayList<ShoppingCartItem> getItems() {
        return items;
    }

    public BigDecimal calculatePrice() {
        var sum = BigDecimal.ZERO;
        bestDiscount = BigDecimal.ZERO;

        for (var item : items) {
            sum = item.itemCost().multiply(BigDecimal.valueOf(item.quantity())).add(sum);
        }
        for (var discount : discounts) {
            var totalDiscount = discount.discount(items);
            if (sum.subtract(totalDiscount).intValue() > bestDiscount.intValue()) {
                bestDiscount = sum.subtract(totalDiscount);
            }
        }
        return sum.subtract(bestDiscount);
    }

    public void addDiscount(Discount discount) {
        discounts.add(discount);
    }

    public String receipt() {
        String line = "--------------------------------\n";
        StringBuilder sb = new StringBuilder();
        sb.append(line);
        var list = items.stream()
                .sorted(Comparator.comparing(item -> item.product().name()))
                .collect(Collectors.toList());
        for (var each : list) {
            sb.append(String.format("%-24s % 7.2f \nquantity: %d%n\n", each.product().name(), each.itemCost(), each.quantity()));
        }
        sb.append(line);
        sb.append(String.format("%24s% 8.2f", "TOTAL:", calculatePrice()));
        sb.append(String.format("\n%24s% 8.2f", "DISCOUNT:", bestDiscount.multiply(BigDecimal.valueOf(-1))));
        return sb.toString();
    }

    public void undo() {
        int index = commandManager.getUndoList().size() - 1;
        commandManager.getUndoList().get(index).execute();
        commandManager.getRedoList().push(commandManager.getUndoList().peek());
        commandManager.getUndoList().pop();
    }

    public void redo() {
        if (commandManager.getRedoList().size() > 0) {
            commandManager.getRedoList().get(0).redo();
        }
    }
}
