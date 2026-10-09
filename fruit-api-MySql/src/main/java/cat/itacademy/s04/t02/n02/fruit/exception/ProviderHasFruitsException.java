package cat.itacademy.s04.t02.n02.fruit.exception;

public class ProviderHasFruitsException extends RuntimeException {

    public ProviderHasFruitsException(Long id) {

        super("Cannot delete provider with id " + id + " because it has associated fruits");
    }
}