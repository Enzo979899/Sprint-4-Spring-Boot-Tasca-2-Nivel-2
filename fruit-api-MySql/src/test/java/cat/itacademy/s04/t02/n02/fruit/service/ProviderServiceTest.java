package cat.itacademy.s04.t02.n02.fruit.service;

import cat.itacademy.s04.t02.n02.fruit.exception.ProviderAlreadyExistsException;
import cat.itacademy.s04.t02.n02.fruit.exception.ProviderHasFruitsException;
import cat.itacademy.s04.t02.n02.fruit.exception.ProviderNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.model.Provider;
import cat.itacademy.s04.t02.n02.fruit.repository.FruitRepository;
import cat.itacademy.s04.t02.n02.fruit.repository.ProviderRepository;
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
class ProviderServiceTest {

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private FruitRepository fruitRepository;

    @InjectMocks
    private ProviderService providerService;

    @Test
    void shouldCreateProvider() {
        Provider provider = new Provider("Frutas del Sur", "España");

        when(providerRepository.save(any(Provider.class))).thenReturn(provider);

        Provider result = providerService.create(provider);

        assertEquals("Frutas del Sur", result.getName());
        assertEquals("España", result.getCountry());
        verify(providerRepository).save(provider);
    }

    @Test
    void shouldThrowExceptionWhenProviderAlreadyExists() {
        Provider provider = new Provider("Frutas del Sur", "España");

        when(providerRepository.existsByName("Frutas del Sur")).thenReturn(true);

        assertThrows(ProviderAlreadyExistsException.class, () -> providerService.create(provider));

        verify(providerRepository, never()).save(any(Provider.class));
    }

    @Test
    void shouldReturnAllProviders() {
        Provider provider1 = new Provider("Frutas del Sur", "España");
        Provider provider2 = new Provider("Tropical Fruits", "Brasil");

        when(providerRepository.findAll()).thenReturn(List.of(provider1, provider2));

        List<Provider> result = providerService.findAll();

        assertEquals(2, result.size());
        assertEquals("Frutas del Sur", result.get(0).getName());
        assertEquals("Tropical Fruits", result.get(1).getName());
    }

    @Test
    void shouldUpdateExistingProvider() {

        Provider existingProvider = new Provider("Frutas del Sur", "España");

        Provider updatedData = new Provider("Frutas del Norte", "España");

        when(providerRepository.findById(1L)).thenReturn(Optional.of(existingProvider));

        when(providerRepository.save(existingProvider)).thenReturn(existingProvider);

        Provider result = providerService.update(1L, updatedData);

        assertEquals("Frutas del Norte", result.getName());
        assertEquals("España", result.getCountry());

        verify(providerRepository).save(existingProvider);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingProvider() {

        Provider updatedData = new Provider("Frutas del Norte", "España");

        when(providerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProviderNotFoundException.class, () -> providerService.update(999L, updatedData));

        verify(providerRepository, never()).save(any(Provider.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingProviderWithDuplicateName() {

        Provider existingProvider = new Provider("Tropical Fruits", "Brasil");
        Provider updatedData = new Provider("Frutas del Sur", "España");

        when(providerRepository.findById(2L)).thenReturn(Optional.of(existingProvider));

        when(providerRepository.existsByNameAndIdNot("Frutas del Sur", 2L)).thenReturn(true);

        assertThrows(ProviderAlreadyExistsException.class, () -> providerService.update(2L, updatedData));

        verify(providerRepository, never()).save(any(Provider.class));
    }

    @Test
    void shouldAllowUpdatingProviderWithoutChangingItsName() {

        Provider existingProvider = new Provider("Frutas del Sur", "España");
        Provider updatedData = new Provider("Frutas del Sur", "Portugal");

        when(providerRepository.findById(1L)).thenReturn(Optional.of(existingProvider));

        when(providerRepository.existsByNameAndIdNot("Frutas del Sur", 1L)).thenReturn(false);

        when(providerRepository.save(existingProvider)).thenReturn(existingProvider);

        Provider result = providerService.update(1L, updatedData);

        assertEquals("Frutas del Sur", result.getName());
        assertEquals("Portugal", result.getCountry());

        verify(providerRepository).save(existingProvider);
    }

    @Test
    void shouldDeleteProviderWithoutFruits() {

        Provider provider = new Provider("Frutas del Sur", "España");

        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));

        providerService.delete(1L);

        verify(providerRepository).delete(provider);
    }

    @Test
    void shouldNotDeleteProviderWithFruits() {

        Provider provider = new Provider("Frutas del Sur", "España");

        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));

        when(fruitRepository.existsByProviderId(1L)).thenReturn(true);

        assertThrows(ProviderHasFruitsException.class, () -> providerService.delete(1L));

        verify(providerRepository, never()).delete(any(Provider.class));
    }


}
