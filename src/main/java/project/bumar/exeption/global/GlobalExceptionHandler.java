package project.bumar.exeption.global;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import project.bumar.exeption.*;
import project.bumar.exeption.base.BaseCustomException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            AlreadyExistException.class,
            NotFoundException.class,
            DestroyFailException.class,
            PasswordNonMatchException.class,
            UploadFailException.class,
            NotAllowedException.class,
            EmptyKeyOrValueException.class,
            TimeIsUpException.class})
    @ResponseBody

    public ResponseEntity handleException(BaseCustomException exception) {
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
    }

//    @ExceptionHandler(BaseCustomException.class)
//    @ResponseBody
//    public ResponseEntity handleGlobalException(BaseCustomException exception) {
//        return ResponseEntity.status(exception.getStatusCode()).build();
//    }
}
