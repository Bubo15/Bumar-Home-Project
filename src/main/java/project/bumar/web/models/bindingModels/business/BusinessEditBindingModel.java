package project.bumar.web.models.bindingModels.business;

import javax.validation.constraints.NotNull;

public class BusinessEditBindingModel {


    private String name;

    public BusinessEditBindingModel() {
    }

    @NotNull(message = "Field can not be null")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
