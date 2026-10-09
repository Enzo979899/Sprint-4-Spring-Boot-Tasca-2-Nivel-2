package cat.itacademy.s04.t02.n02.fruit.service;

import cat.itacademy.s04.t02.n02.fruit.exception.ProviderAlreadyExistsException;
import cat.itacademy.s04.t02.n02.fruit.exception.ProviderHasFruitsException;
import cat.itacademy.s04.t02.n02.fruit.exception.ProviderNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.model.Provider;
import cat.itacademy.s04.t02.n02.fruit.repository.FruitRepository;
import cat.itacademy.s04.t02.n02.fruit.repository.ProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderService {
    private final ProviderRepository providerRepository;
    private final FruitRepository fruitRepository;

    public ProviderService(ProviderRepository providerRepository, FruitRepository fruitRepository) {
        this.providerRepository = providerRepository;
        this.fruitRepository = fruitRepository;
    }

    public Provider create(Provider provider) {

        if (providerRepository.existsByName(provider.getName())) {
            throw new ProviderAlreadyExistsException(provider.getName());
        }

        return providerRepository.save(provider);
    }

    public List<Provider> findAll() {
        return providerRepository.findAll();
    }

    public Provider update(Long id, Provider provider) {

        Provider existingProvider = providerRepository.findById(id).orElseThrow(() ->
                new ProviderNotFoundException(id));

        if (providerRepository.existsByNameAndIdNot(provider.getName(), id)) {
            throw new ProviderAlreadyExistsException(provider.getName());
        }

        existingProvider.setName(provider.getName());
        existingProvider.setCountry(provider.getCountry());

        return providerRepository.save(existingProvider);
    }

    public void delete(Long id) {

        Provider provider = providerRepository.findById(id).orElseThrow(() -> new ProviderNotFoundException(id));

        if (fruitRepository.existsByProviderId(id)) {
            throw new ProviderHasFruitsException(id);
        }

        providerRepository.delete(provider);
    }
}
