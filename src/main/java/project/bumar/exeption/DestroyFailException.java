package project.bumar.exeption;

import project.bumar.exeption.base.BaseCustomException;

public class DestroyFailException extends BaseCustomException {

    public DestroyFailException(String message) {
        super(message, 503);
    }
}
