package project.bumar.web.models.responseModel.product;

import com.google.gson.annotations.Expose;
import project.bumar.data.entities.Picture;

public class ProductOrderResponseModel {

    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private double price;
    @Expose
    private Picture picture;

    public ProductOrderResponseModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }
}
