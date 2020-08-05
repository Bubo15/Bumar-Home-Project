package project.bumar.exeption;

import project.bumar.exeption.base.BaseCustomException;

public class AlreadyExistException extends BaseCustomException {

    public AlreadyExistException(String message) {
        super(message, 400);
    }
}
