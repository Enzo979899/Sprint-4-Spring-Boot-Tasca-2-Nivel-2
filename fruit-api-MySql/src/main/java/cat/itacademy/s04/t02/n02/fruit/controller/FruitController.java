package cat.itacademy.s04.t02.n02.fruit.controller;


import cat.itacademy.s04.t02.n02.fruit.dto.FruitRequest;
import cat.itacademy.s04.t02.n02.fruit.dto.FruitResponse;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.service.FruitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fruits")
public class FruitController {

    private final FruitService fruitService;

    public FruitController(FruitService fruitService) {
        this.fruitService = fruitService;
    }

    @PostMapping
    public ResponseEntity<FruitResponse> create(@Valid @RequestBody FruitRequest request) {

        Fruit savedFruit = fruitService.create(request.name(), request.weightInKilos(), request.providerId());

        return ResponseEntity.status(HttpStatus.CREATED).body(FruitResponse.from(savedFruit));
    }

    @GetMapping
    public List<FruitResponse> findAll() {
        return fruitService.findAll().stream().map(FruitResponse::from).toList();
    }

    @GetMapping("/{id}")
    public FruitResponse findById(@PathVariable Long id) {
        Fruit fruit = fruitService.findById(id);
        return FruitResponse.from(fruit);
    }

    @PutMapping("/{id}")
    public FruitResponse update(@PathVariable Long id, @Valid @RequestBody FruitRequest request) {

        Fruit updatedFruit = fruitService.update(id, request.name(), request.weightInKilos(), request.providerId());

        return FruitResponse.from(updatedFruit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fruitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
