package cat.itacademy.s04.t02.n02.fruit.service;

import cat.itacademy.s04.t02.n02.fruit.exception.FruitNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.exception.ProviderNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.model.Provider;
import cat.itacademy.s04.t02.n02.fruit.repository.FruitRepository;
import cat.itacademy.s04.t02.n02.fruit.repository.ProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FruitService {
    private final FruitRepository fruitRepository;
    private final ProviderRepository providerRepository;

    public FruitService(FruitRepository fruitRepository, ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
        this.fruitRepository = fruitRepository;
    }

    public Fruit create(String name, double weightInKilos, Long providerId) {

        Provider provider = providerRepository.findById(providerId).orElseThrow(() ->
                new ProviderNotFoundException(providerId));

        Fruit fruit = new Fruit(null, name, weightInKilos, provider);

        return fruitRepository.save(fruit);
    }

    public List<Fruit> findAll() {
        return fruitRepository.findAll();
    }

    public Fruit findById(Long id) {
        return fruitRepository.findById(id).orElseThrow(() -> new FruitNotFoundException(id));
    }

    public Fruit update(Long id, String name, double weightInKilos, Long providerId) {

        Fruit existingFruit = findById(id);

        Provider provider = providerRepository.findById(providerId).orElseThrow(() ->
                new ProviderNotFoundException(providerId));

        Fruit updatedFruit = new Fruit(existingFruit.getId(), name, weightInKilos, provider);

        return fruitRepository.save(updatedFruit);
    }

    public void delete(Long id) {
        Fruit fruit = findById(id);
        fruitRepository.delete(fruit);
    }
}
