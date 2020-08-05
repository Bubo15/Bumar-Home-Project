package project.bumar.exeption;

import project.bumar.exeption.base.BaseCustomException;

public class PasswordNonMatchException extends BaseCustomException {


    public PasswordNonMatchException(String message) {
        super(message, 400);
    }
}
