package project.bumar.exeption;

import project.bumar.exeption.base.BaseCustomException;

public class TimeIsUpException extends BaseCustomException {

    public TimeIsUpException(String message) {
        super(message, 400);
    }
}
