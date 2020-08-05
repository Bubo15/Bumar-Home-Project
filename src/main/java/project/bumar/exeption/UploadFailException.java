package project.bumar.exeption;

import project.bumar.exeption.base.BaseCustomException;

public class UploadFailException extends BaseCustomException {

    public UploadFailException(String message) {
        super(message, 503);
    }
}
