package project.bumar.web.models.bindingModels.changes;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ChangeUsernameBindingModel {

    private String newUsername;

    public ChangeUsernameBindingModel() {
    }

    @NotNull(message = "Field can not be null")
    @NotEmpty(message = "Username name is required")
    @Length(min = 2, message = "Username must be least 2 symbols")
    @Length(min = 8, message = "Username must be at most 8 symbols")
    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }
}
