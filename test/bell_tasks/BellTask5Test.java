package bell_tasks;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Objects;

class BellTask5Test {

    BellTask5 b = new BellTask5();

    @Order(1)
    @Test
    void addProduct() {
        b.addProduct("juice", 1);

        assert Objects.equals(b.getProducts(), java.util.List.of("juice"));
    }

    @Order(2)
    @Test
    void updateProductQuantity() {
        b.updateProductQuantity("juice", 2);

        assert b.getProductQuantity("juice") == 2;
    }

    @Order(5)
    @Test
    void removeProduct() {
        b.removeProduct("juice");

        assert b.getBasket().isEmpty();

    }

    @Order(6)
    @Test
    void clear() {
        b.addProduct("juice", 1);
        b.clear();

        assert b.getBasket().isEmpty();
    }

}