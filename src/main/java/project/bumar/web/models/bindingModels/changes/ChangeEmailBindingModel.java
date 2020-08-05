package project.bumar.web.models.bindingModels.changes;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ChangeEmailBindingModel {

    private String newEmail;

    public ChangeEmailBindingModel() {
    }

    @NotNull(message = "Field can not be null")
    @NotEmpty(message = "Email is required")
    @Email(message = "Email must be valid")
    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }
}
