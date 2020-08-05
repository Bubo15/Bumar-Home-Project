package project.bumar.web.models.bindingModels.comment;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CommentBindingModel {

    private String description;

    public CommentBindingModel() {
    }

    @NotNull(message = "Field can not be null")
    @NotEmpty(message = "Comment can not be empty")
    @Length(max = 250, message = "Description can be max 250 symbols")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
