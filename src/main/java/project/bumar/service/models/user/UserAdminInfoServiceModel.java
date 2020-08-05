package project.bumar.service.models.user;

import project.bumar.data.entities.Role;

import java.util.Set;

public class UserAdminInfoServiceModel {

    private long id;
    private String username;
    private Set<Role> authorities;
    private boolean isEnabled;

    public UserAdminInfoServiceModel() {
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

    public Set<Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
