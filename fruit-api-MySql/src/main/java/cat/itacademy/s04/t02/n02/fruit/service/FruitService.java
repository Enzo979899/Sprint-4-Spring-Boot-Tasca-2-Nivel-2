package cat.itacademy.s04.t02.n02.fruit.service;

import cat.itacademy.s04.t02.n02.fruit.exception.FruitNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.repository.FruitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FruitService {
    private final FruitRepository fruitRepository;

    public FruitService(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;
    }

    public Fruit create(Fruit fruit) {
        return fruitRepository.save(fruit);
    }

    public List<Fruit> findAll() {
        return fruitRepository.findAll();
    }

    public Fruit findById(Long id) {
        return fruitRepository.findById(id).orElseThrow(() -> new FruitNotFoundException(id));
    }

    public Fruit update(Long id, Fruit fruit) {
        Fruit existingFruit = findById(id);

        Fruit updatedFruit = new Fruit(existingFruit.getId(), fruit.getName(), fruit.getWeightInKilos());
        return fruitRepository.save(updatedFruit);
    }

    public void delete(Long id) {
        Fruit fruit = findById(id);
        fruitRepository.delete(fruit);
    }
}
