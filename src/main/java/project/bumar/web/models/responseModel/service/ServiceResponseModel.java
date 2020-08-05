package project.bumar.web.models.responseModel.service;

import com.google.gson.annotations.Expose;
import project.bumar.data.entities.Picture;

public class ServiceResponseModel {

    @Expose
    private long id;
    @Expose
    private String name;
    @Expose
    private String description;
    @Expose
    private Picture picture;

    public ServiceResponseModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }
}
