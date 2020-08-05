package project.bumar.web.models.responseModel.user;

import com.google.gson.annotations.Expose;

public class UserInfoResponseModel {

    @Expose
    private String username;

    @Expose
    private String email;

    public UserInfoResponseModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
