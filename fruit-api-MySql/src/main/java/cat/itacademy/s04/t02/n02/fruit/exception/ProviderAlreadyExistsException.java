package cat.itacademy.s04.t02.n02.fruit.exception;

public class ProviderAlreadyExistsException extends RuntimeException {

    public ProviderAlreadyExistsException(String name) {
        super("Provider already exists: " + name);
    }
}
