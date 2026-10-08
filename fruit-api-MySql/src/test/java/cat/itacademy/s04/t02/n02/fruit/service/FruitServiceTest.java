package cat.itacademy.s04.t02.n02.fruit.service;

import cat.itacademy.s04.t02.n02.fruit.exception.FruitNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.repository.FruitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FruitServiceTest {

    @Mock
    private FruitRepository fruitRepository;

    @InjectMocks
    private FruitService fruitService;

    @Test
    void shouldCreateFruit() {
        Fruit fruit = new Fruit(null, "Manzana", 1.8);
        Fruit savedFruit = new Fruit(1L, "Manzana", 2.4);

        when(fruitRepository.save(any(Fruit.class))).thenReturn(savedFruit);

        Fruit result = fruitService.create(fruit);

        assertEquals(1L, result.getId());
        assertEquals("Manzana", result.getName());
        assertEquals(2.4, result.getWeightInKilos());
    }

    @Test
    void shouldReturnAllFruits() {
        Fruit uva = new Fruit(1L, "Uva", 1.4);
        Fruit melon = new Fruit(2L, "Melon", 3.7);

        when(fruitRepository.findAll()).thenReturn(List.of(uva, melon));

        List<Fruit> result = fruitService.findAll();

        assertEquals(2, result.size());
        assertEquals("Uva", result.get(0).getName());
        assertEquals("Melon", result.get(1).getName());
    }

    @Test
    void shouldReturnFruitById() {
        Fruit uva = new Fruit(1L, "Uva", 2.6);

        when(fruitRepository.findById(1L)).thenReturn(Optional.of(uva));

        Fruit result = fruitService.findById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Uva", result.getName());
        assertEquals(2.6, result.getWeightInKilos());
    }

    @Test
    void shouldThrowExceptionWhenFruitDoesNotExist() {
        when(fruitRepository.findById(888L)).thenReturn(Optional.empty());

        assertThrows(FruitNotFoundException.class, () -> fruitService.findById(888L));
    }

    @Test
    void shouldUpdateFruit() {
        Fruit existingFruit = new Fruit(1L, "Uva", 1.4);
        Fruit updatedFruit = new Fruit(1L, "Melon", 3.2);

        when(fruitRepository.findById(1L)).thenReturn(Optional.of(existingFruit));

        when(fruitRepository.save(any(Fruit.class))).thenReturn(updatedFruit);

        Fruit result = fruitService.update(1L, new Fruit(null, "Melon", 3.2));

        assertEquals(1L, result.getId());
        assertEquals("Melon", result.getName());
        assertEquals(3.2, result.getWeightInKilos());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingFruit() {
        when(fruitRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(FruitNotFoundException.class, () -> fruitService.update(999L, new Fruit(null, "Pera",
                        2.1)));
    }

    @Test
    void shouldDeleteFruit() {
        Fruit fruit = new Fruit(1L, "Pera", 1.1);

        when(fruitRepository.findById(1L)).thenReturn(Optional.of(fruit));

        fruitService.delete(1L);

        verify(fruitRepository).delete(fruit);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingFruit() {
        when(fruitRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(FruitNotFoundException.class, () -> fruitService.delete(999L));
    }
}
