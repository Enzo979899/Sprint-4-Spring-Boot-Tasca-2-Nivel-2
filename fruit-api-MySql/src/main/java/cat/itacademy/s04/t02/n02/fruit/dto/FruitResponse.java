package cat.itacademy.s04.t02.n02.fruit.dto;


import cat.itacademy.s04.t02.n02.fruit.model.Fruit;

public record FruitResponse(Long id, String name, double weightInKilos, Long providerId) {

    public static FruitResponse from(Fruit fruit) {
        return new FruitResponse(fruit.getId(), fruit.getName(), fruit.getWeightInKilos(), fruit.getProvider().getId());
    }

}
