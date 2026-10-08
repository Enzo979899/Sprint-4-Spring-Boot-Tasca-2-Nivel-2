package cat.itacademy.s04.t02.n02.fruit.service;

import cat.itacademy.s04.t02.n02.fruit.exception.ProviderAlreadyExistsException;
import cat.itacademy.s04.t02.n02.fruit.model.Provider;
import cat.itacademy.s04.t02.n02.fruit.repository.ProviderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProviderServiceTest {

    @Mock
    private ProviderRepository providerRepository;

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
}
