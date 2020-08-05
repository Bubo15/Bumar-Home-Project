package project.bumar.service.models.user;

import project.bumar.data.entities.Role;
import project.bumar.data.entities.UserProfile;

import java.util.Set;

public class UserServiceModel {

    private long id;
    private String username;
    private UserProfile userProfile;
    private Set<Role> authorities;

    public UserServiceModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
