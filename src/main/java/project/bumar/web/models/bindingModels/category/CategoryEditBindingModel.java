package project.bumar.web.models.bindingModels.category;

import javax.validation.constraints.NotNull;

public class CategoryEditBindingModel {

    private String name;

    public CategoryEditBindingModel() {
    }

    @NotNull(message = "Field can not be null")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
