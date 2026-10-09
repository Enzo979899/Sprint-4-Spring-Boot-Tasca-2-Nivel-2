package cat.itacademy.s04.t02.n02.fruit.repository;

import cat.itacademy.s04.t02.n02.fruit.model.Provider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ProviderRepositoryIntegrationTest {

    @Autowired
    private ProviderRepository providerRepository;

    @Test
    void shouldSaveAndFindProvider() {

        Provider provider = new Provider("Frutas del Sur", "España");

        Provider savedProvider = providerRepository.save(provider);

        assertNotNull(savedProvider.getId());

        Provider foundProvider = providerRepository.findById(savedProvider.getId()).orElseThrow();

        assertEquals("Frutas del Sur", foundProvider.getName());
        assertEquals("España", foundProvider.getCountry());
    }



}
