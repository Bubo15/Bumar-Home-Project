package project.bumar.web.models.responseModel.comment;

import com.google.gson.annotations.Expose;
import project.bumar.web.models.responseModel.user.UserCommentResponseModel;

public class CommentResponseModel {

    @Expose
    private long id;
    @Expose
    private String description;
    @Expose
    private UserCommentResponseModel user;

    public CommentResponseModel() {
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

    public UserCommentResponseModel getUser() {
        return user;
    }

    public void setUser(UserCommentResponseModel user) {
        this.user = user;
    }
}
