package cat.itacademy.s04.t02.n02.fruit.dto;

import jakarta.validation.constraints.NotBlank;

public record ProviderRequest(@NotBlank String name, @NotBlank String country) {

}
