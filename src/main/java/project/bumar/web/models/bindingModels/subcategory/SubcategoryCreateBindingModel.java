package project.bumar.web.models.bindingModels.subcategory;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class SubcategoryCreateBindingModel {

    private String name;
    private String categoryName;

    public SubcategoryCreateBindingModel() {
    }

    @NotNull(message = "Field can not be null")
    @NotEmpty(message = "Subcategory Name is required")
    @Length(min = 3, message = "Subcategory Name must be least 3 symbols")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = "Field can not be null")
    @NotEmpty(message = "Category Name is required")
    @Length(min = 3, message = "Category Name must be least 3 symbols")
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
