package project.bumar.services.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.bumar.services.services.ValidationService;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@Service
public class ValidationServiceImpl implements ValidationService {

    private final Validator validator;

    @Autowired
    public ValidationServiceImpl(Validator validator) {
        this.validator = validator;
    }

    @Override
    public <T> void validate(T model) {

        Set<ConstraintViolation<T>> violations = validator.validate(model);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
