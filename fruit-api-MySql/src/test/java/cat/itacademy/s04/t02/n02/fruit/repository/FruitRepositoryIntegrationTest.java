package cat.itacademy.s04.t02.n02.fruit.repository;

import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.model.Provider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class FruitRepositoryIntegrationTest {

    @Autowired
    private FruitRepository fruitRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Test
    void shouldFindFruitsByProviderId() {

        Provider provider = new Provider("Frutas del Sur", "España");
        Provider savedProvider = providerRepository.save(provider);

        Fruit apple = new Fruit(null, "Manzana", 2.5, savedProvider);
        Fruit pear = new Fruit(null, "Pera", 1.8, savedProvider);

        fruitRepository.save(apple);
        fruitRepository.save(pear);

        List<Fruit> result = fruitRepository.findByProviderId(savedProvider.getId());

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(fruit -> fruit.getName().equals("Manzana")));
        assertTrue(result.stream().anyMatch(fruit -> fruit.getName().equals("Pera")));
    }

    @Test
    void shouldDetectFruitsAssociatedWithProvider() {

        Provider provider = new Provider("Frutas del Sur", "España");
        Provider savedProvider = providerRepository.save(provider);

        Fruit apple = new Fruit(null, "Manzana", 2.5, savedProvider);
        fruitRepository.save(apple);

        boolean hasFruits = fruitRepository.existsByProviderId(savedProvider.getId());

        assertTrue(hasFruits);
    }

    @Test
    void shouldReturnFalseWhenProviderHasNoFruits() {

        Provider provider = new Provider("Frutas del Norte", "España");
        Provider savedProvider = providerRepository.save(provider);

        boolean hasFruits = fruitRepository.existsByProviderId(savedProvider.getId());

        assertFalse(hasFruits);
    }
}