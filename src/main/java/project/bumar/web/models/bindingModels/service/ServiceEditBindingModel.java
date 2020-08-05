package project.bumar.web.models.bindingModels.service;

import javax.validation.constraints.NotNull;

public class ServiceEditBindingModel {

    private String name;
    private String description;

    public ServiceEditBindingModel() {
    }

    @NotNull(message = "Field can not be null")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = "Field can not be null")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
