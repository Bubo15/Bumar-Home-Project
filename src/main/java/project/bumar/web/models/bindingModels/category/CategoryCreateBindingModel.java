package project.bumar.web.models.bindingModels.category;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class CategoryCreateBindingModel {

    private String name;

    public CategoryCreateBindingModel() {
    }

    @NotNull(message = "Field can not be null")
    @NotNull(message = "Field is required")
    @Length(min = 3, message = "Name must be least 3 symbols")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
