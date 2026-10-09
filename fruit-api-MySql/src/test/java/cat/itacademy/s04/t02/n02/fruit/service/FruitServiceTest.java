package cat.itacademy.s04.t02.n02.fruit.service;

import cat.itacademy.s04.t02.n02.fruit.exception.FruitNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.exception.ProviderNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.model.Provider;
import cat.itacademy.s04.t02.n02.fruit.repository.ProviderRepository;
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

    @Mock
    private ProviderRepository providerRepository;

    @InjectMocks
    private FruitService fruitService;

    @Test
    void shouldCreateFruit() {
        Provider provider = new Provider("Frutas del Sur", "España");
        Fruit savedFruit = new Fruit(1L, "Manzana", 2.4, provider);

        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));

        when(fruitRepository.save(any(Fruit.class))).thenReturn(savedFruit);

        Fruit result = fruitService.create("Manzana", 1.8, 1L);

        assertEquals(1L, result.getId());
        assertEquals("Manzana", result.getName());
        assertEquals(2.4, result.getWeightInKilos());
        assertSame(provider, result.getProvider());

        verify(fruitRepository).save(any(Fruit.class));
    }

    @Test
    void shouldReturnAllFruits() {
        Provider provider = new Provider("Frutas del Sur", "España");

        Fruit uva = new Fruit(1L, "Uva", 1.4, provider);
        Fruit melon = new Fruit(2L, "Melon", 3.7, provider);

        when(fruitRepository.findAll()).thenReturn(List.of(uva, melon));

        List<Fruit> result = fruitService.findAll();

        assertEquals(2, result.size());
        assertEquals("Uva", result.get(0).getName());
        assertEquals(1.4, result.get(0).getWeightInKilos());
        assertEquals("Melon", result.get(1).getName());
        assertEquals(3.7, result.get(1).getWeightInKilos());

        assertSame(provider, result.get(0).getProvider());
        assertSame(provider, result.get(1).getProvider());
    }

    @Test
    void shouldReturnFruitById() {
        Provider provider = new Provider("Frutas del Sur", "España");

        Fruit uva = new Fruit(1L, "Uva", 2.6, provider);

        when(fruitRepository.findById(1L)).thenReturn(Optional.of(uva));

        Fruit result = fruitService.findById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Uva", result.getName());
        assertEquals(2.6, result.getWeightInKilos());
        assertSame(provider, result.getProvider());
    }

    @Test
    void shouldThrowExceptionWhenFruitDoesNotExist() {
        when(fruitRepository.findById(888L)).thenReturn(Optional.empty());

        assertThrows(FruitNotFoundException.class, () -> fruitService.findById(888L));
    }

    @Test
    void shouldUpdateFruit() {
        Provider provider = new Provider("Frutas del Sur", "España");

        Fruit existingFruit = new Fruit(1L, "Uva", 1.4, provider);
        Fruit updatedFruit = new Fruit(1L, "Melon", 3.2, provider);

        when(fruitRepository.findById(1L)).thenReturn(Optional.of(existingFruit));

        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));

        when(fruitRepository.save(any(Fruit.class))).thenReturn(updatedFruit);

        Fruit result = fruitService.update(1L, "Melon", 3.2, 1L);

        assertEquals(1L, result.getId());
        assertEquals("Melon", result.getName());
        assertEquals(3.2, result.getWeightInKilos());
        assertSame(provider, result.getProvider());

        verify(fruitRepository).save(any(Fruit.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingFruit() {

        when(fruitRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(FruitNotFoundException.class, () -> fruitService.update(999L, "Pera", 2.1,
                1L));

        verify(providerRepository, never()).findById(any(Long.class));
        verify(fruitRepository, never()).save(any(Fruit.class));
    }

    @Test
    void shouldDeleteFruit() {
        Provider provider = new Provider("Frutas del Sur", "España");

        Fruit fruit = new Fruit(1L, "Pera", 1.1, provider);

        when(fruitRepository.findById(1L)).thenReturn(Optional.of(fruit));

        fruitService.delete(1L);

        verify(fruitRepository).delete(fruit);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingFruit() {
        when(fruitRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(FruitNotFoundException.class, () -> fruitService.delete(999L));
    }

    @Test
    void shouldThrowExceptionWhenCreatingFruitWithNonExistingProvider() {

        when(providerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProviderNotFoundException.class, () -> fruitService.create("Manzana", 1.8,
                999L));

        verify(fruitRepository, never()).save(any(Fruit.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingFruitWithNonExistingProvider() {

        Provider provider = new Provider("Frutas del Sur", "España");

        Fruit existingFruit = new Fruit(1L, "Uva", 1.4, provider);

        when(fruitRepository.findById(1L)).thenReturn(Optional.of(existingFruit));

        when(providerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProviderNotFoundException.class, () -> fruitService.update(1L, "Melon", 3.2,
                999L));

        verify(fruitRepository, never()).save(any(Fruit.class));
    }

    @Test
    void shouldFindFruitsByProviderId() {

        Provider provider = new Provider("Frutas del Sur", "España");

        Fruit manzana = new Fruit(1L, "Manzana", 2.5, provider);
        Fruit pera = new Fruit(2L, "Pera", 1.8, provider);

        when(providerRepository.existsById(1L)).thenReturn(true);

        when(fruitRepository.findByProviderId(1L)).thenReturn(List.of(manzana, pera));

        List<Fruit> result = fruitService.findByProviderId(1L);

        assertEquals(2, result.size());
        assertEquals("Manzana", result.get(0).getName());
        assertEquals("Pera", result.get(1).getName());

        verify(fruitRepository).findByProviderId(1L);
    }

    @Test
    void shouldThrowExceptionWhenFilteringByNonExistingProvider() {

        when(providerRepository.existsById(999L)).thenReturn(false);

        assertThrows(ProviderNotFoundException.class, () -> fruitService.findByProviderId(999L));

        verify(fruitRepository, never()).findByProviderId(anyLong());
    }
}
