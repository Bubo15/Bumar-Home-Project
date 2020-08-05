package project.bumar.web.models.bindingModels.subcategory;

import javax.validation.constraints.NotNull;

public class SubcategoryEditBindingModel {

    private String name;
    private String categoryName;

    public SubcategoryEditBindingModel() {
    }

    @NotNull(message = "Field can not be null")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = "Field can not be null")
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
