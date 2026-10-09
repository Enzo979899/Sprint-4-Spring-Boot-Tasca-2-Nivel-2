package cat.itacademy.s04.t02.n02.fruit.controller;

import cat.itacademy.s04.t02.n02.fruit.dto.ProviderRequest;
import cat.itacademy.s04.t02.n02.fruit.dto.ProviderResponse;
import cat.itacademy.s04.t02.n02.fruit.model.Provider;
import cat.itacademy.s04.t02.n02.fruit.service.ProviderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/providers")
public class ProviderController {

    private final ProviderService providerService;

    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @PostMapping
    public ResponseEntity<ProviderResponse> create(@Valid @RequestBody ProviderRequest request) {

        Provider provider = new Provider(request.name(), request.country());

        Provider savedProvider = providerService.create(provider);

        return ResponseEntity.status(HttpStatus.CREATED).body(ProviderResponse.from(savedProvider));
    }

    @GetMapping
    public ResponseEntity<List<ProviderResponse>> findAll() {

        List<ProviderResponse> providers = providerService.findAll().stream().map(ProviderResponse::from).toList();

        return ResponseEntity.ok(providers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProviderResponse> update(@PathVariable Long id, @Valid @RequestBody ProviderRequest request) {

        Provider provider = new Provider(request.name(), request.country());

        Provider updatedProvider = providerService.update(id, provider);

        return ResponseEntity.ok(ProviderResponse.from(updatedProvider));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        providerService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
