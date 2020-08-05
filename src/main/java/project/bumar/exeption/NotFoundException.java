package project.bumar.exeption;

import project.bumar.exeption.base.BaseCustomException;

public class NotFoundException extends BaseCustomException {

    public NotFoundException(String message) {
        super(message, 404);
    }
}
