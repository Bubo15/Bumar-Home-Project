package project.bumar.exeption;

import project.bumar.exeption.base.BaseCustomException;

public class NotAllowedException extends BaseCustomException {

    public NotAllowedException(String message) {
        super(message, 405);
    }
}
