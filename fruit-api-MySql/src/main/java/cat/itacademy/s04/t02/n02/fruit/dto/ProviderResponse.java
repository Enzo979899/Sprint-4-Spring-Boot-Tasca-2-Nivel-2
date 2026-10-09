package cat.itacademy.s04.t02.n02.fruit.dto;

import cat.itacademy.s04.t02.n02.fruit.model.Provider;

public record ProviderResponse(Long id, String name, String country) {

    public static ProviderResponse from(Provider provider) {
        return new ProviderResponse(provider.getId(), provider.getName(), provider.getCountry());
    }
}

