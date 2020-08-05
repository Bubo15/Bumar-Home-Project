package project.bumar.exeption;

import project.bumar.exeption.base.BaseCustomException;

public class EmptyKeyOrValueException extends BaseCustomException {

    public EmptyKeyOrValueException(String message) {
        super(message, 400);
    }
}
