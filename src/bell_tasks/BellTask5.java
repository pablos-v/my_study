package bell_tasks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BellTask5 implements Basket, Comparable<BellTask5> {

    private final Map<String, Integer> basket;

    public BellTask5() {
        this.basket = new HashMap<>();
    }

    @Override
    public void addProduct(String product, int quantity) {
        basket.put(product, quantity);
    }

    @Override
    public void removeProduct(String product) {
        basket.remove(product);
    }

    @Override
    public void updateProductQuantity(String product, int quantity) {
        basket.put(product, quantity);
    }

    @Override
    public void clear() {
        basket.clear();
    }

    @Override
    public List<String> getProducts() {
        return basket.keySet().stream().toList();
    }

    @Override
    public int getProductQuantity(String product) {
        return basket.get(product);
    }

    public Map<String, Integer> getBasket() {
        return basket;
    }

    @Override
    public int compareTo(BellTask5 o) {
        return o.getBasket().values().stream().mapToInt(i->i).sum() - this.getBasket().values().stream().mapToInt(i->i).sum();
    }


}
