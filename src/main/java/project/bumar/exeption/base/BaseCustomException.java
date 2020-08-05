package project.bumar.exeption.base;

public class BaseCustomException extends RuntimeException {

    private Integer statusCode;

    public BaseCustomException(String message, Integer statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}

