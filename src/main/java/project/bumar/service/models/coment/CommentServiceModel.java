package project.bumar.service.models.coment;

import project.bumar.service.models.user.UserInfoServiceModel;

public class CommentServiceModel {

    private long id;
    private String description;
    private UserInfoServiceModel user;

    public CommentServiceModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserInfoServiceModel getUser() {
        return user;
    }

    public void setUser(UserInfoServiceModel user) {
        this.user = user;
    }
}
