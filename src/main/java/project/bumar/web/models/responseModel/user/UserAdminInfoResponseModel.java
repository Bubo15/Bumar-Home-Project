package project.bumar.web.models.responseModel.user;

import com.google.gson.annotations.Expose;
import project.bumar.data.entities.Role;

import java.util.Set;

public class UserAdminInfoResponseModel {

    @Expose
    private long id;
    @Expose
    private String username;
    @Expose
    private Set<Role> authorities;
    @Expose
    private boolean isEnabled;

    public UserAdminInfoResponseModel() {
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
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
}
