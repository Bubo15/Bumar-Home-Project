package project.bumar.web.models.bindingModels.user;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ScriptAssert(lang = "javascript",
        script = "_this.confirmPassword !== null && _this.password === _this.confirmPassword",
        reportOn = "confirmPassword",
        message = "Passwords don\'t match")
public class UserRegisterBindingModel {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;

    public UserRegisterBindingModel() {
    }

    @NotNull(message = "Field can not be null")
    @Length(min = 2, message = "First name must be least 2 symbols")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @NotNull(message = "Field can not be null")
    @Length(min = 2, message = "Last name must be least 2 symbols")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @NotNull(message = "Field can not be null")
    @NotEmpty(message = "Username name is required")
    @Length(min = 2, message = "Username must be least 2 symbols")
    @Length(max = 8, message = "Username must be at most 8 symbols")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotNull(message = "Field can not be null")
    @NotEmpty(message = "Email is required")
    @Email(message = "Email must be valid")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotNull(message = "Field can not be null")
    @NotEmpty(message = "Password is required")
    @Length(min = 6, message = "Password must be least 6 symbols")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotNull(message = "Field can not be null")
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
