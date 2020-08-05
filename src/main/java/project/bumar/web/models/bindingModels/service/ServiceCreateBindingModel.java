package project.bumar.web.models.bindingModels.service;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ServiceCreateBindingModel {

    private String name;
    private String description;

    public ServiceCreateBindingModel() {
    }

    @NotNull(message = "Field can not be null")
    @NotEmpty(message = "Field is required")
    @Length(min = 3, max = 20, message = "Name must be least 3 and smaller then 20 symbols")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = "Field can not be null")
    @NotEmpty(message = "Field is required")
    @Length(min = 10, message = "Description must be least 10 symbols")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
