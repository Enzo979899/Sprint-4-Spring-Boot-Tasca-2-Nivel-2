package cat.itacademy.s04.t02.n02.fruit.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

class FruitTest {

    @Test
    void shouldAssociateFruitWithProvider() {

        Provider provider = new Provider("Frutas del Sur", "España");

        Fruit fruit = new Fruit(null, "Manzana", 1.8, provider);

        assertSame(provider, fruit.getProvider());
    }
}
