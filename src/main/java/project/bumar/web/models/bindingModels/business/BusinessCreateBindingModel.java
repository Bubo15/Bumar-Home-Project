package project.bumar.web.models.bindingModels.business;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class BusinessCreateBindingModel {

    @NotNull(message = "Field can not be null")
    @NotEmpty(message = "Name is required")
    @Length(min = 3, message = "Name must be least 3 symbols")
    private String name;

    public BusinessCreateBindingModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
