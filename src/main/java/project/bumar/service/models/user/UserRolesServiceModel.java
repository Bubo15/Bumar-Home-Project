package project.bumar.service.models.user;

import project.bumar.data.entities.Role;

import java.util.Set;

public class UserRolesServiceModel {

    private Set<Role> authorities;

    public UserRolesServiceModel() {
    }

    public Set<Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }
}
