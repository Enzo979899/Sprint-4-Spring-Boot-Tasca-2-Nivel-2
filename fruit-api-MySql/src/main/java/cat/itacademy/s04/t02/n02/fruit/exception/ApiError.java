package cat.itacademy.s04.t02.n02.fruit.exception;

public record ApiError(int status, String error, String message) {

}
