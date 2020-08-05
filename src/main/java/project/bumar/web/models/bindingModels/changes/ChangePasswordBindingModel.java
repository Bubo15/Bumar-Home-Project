package project.bumar.web.models.bindingModels.changes;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ChangePasswordBindingModel {

    private String oldPassword;
    private String newPassword;

    public ChangePasswordBindingModel() {
    }

    @NotNull(message = "Field can not be null")
    @NotEmpty(message = "Old Password is required")
    @Length(min = 6, message = "Old Password must be least 6 symbols")
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @NotNull(message = "Field can not be null")
    @NotEmpty(message = "News Password is required")
    @Length(min = 6, message = "News Password must be least 6 symbols")
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
