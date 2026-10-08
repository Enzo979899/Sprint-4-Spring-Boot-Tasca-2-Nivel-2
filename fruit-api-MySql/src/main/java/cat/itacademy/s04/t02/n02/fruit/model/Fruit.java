package cat.itacademy.s04.t02.n02.fruit.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Fruit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double weightInKilos;
    private Provider provider;

    public Fruit() {

    }

    public Fruit (Long id, String name, double weightInKilos, Provider provider) {
        this.id = id;
        this.name = name;
        this.weightInKilos = weightInKilos;
        this.provider = provider;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getWeightInKilos() {
        return weightInKilos;
    }

    public Provider getProvider() {
        return provider;
    }
}
