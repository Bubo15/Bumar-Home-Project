package project.bumar.web.models.responseModel.news;

import com.google.gson.annotations.Expose;
import project.bumar.data.entities.Picture;

public class NewsResponseModel {

    @Expose
    private long id;
    @Expose
    private String title;
    @Expose
    private String text;
    @Expose
    private Picture picture;

    public NewsResponseModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }
}
