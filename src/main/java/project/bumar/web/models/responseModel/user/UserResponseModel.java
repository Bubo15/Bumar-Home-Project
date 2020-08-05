package project.bumar.web.models.responseModel.user;

import com.google.gson.annotations.Expose;
import project.bumar.data.entities.Role;
import project.bumar.data.entities.UserProfile;

import java.util.Set;

public class UserResponseModel {

    @Expose
    private String username;
    @Expose
    private UserProfile userProfile;
    @Expose
    private Set<Role> authorities;

    public UserResponseModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public Set<Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }
}
