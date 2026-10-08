package cat.itacademy.s04.t02.n02.fruit.repository;

import cat.itacademy.s04.t02.n01.fruitapih2.fruit.model.Fruit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FruitRepository extends JpaRepository<Fruit, Long> {

}
