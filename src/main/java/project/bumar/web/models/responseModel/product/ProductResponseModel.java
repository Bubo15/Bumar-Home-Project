package project.bumar.web.models.responseModel.product;

import com.google.gson.annotations.Expose;
import project.bumar.data.entities.Picture;

public class ProductResponseModel {

    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private double price;
    @Expose
    private Picture picture;
    @Expose
    private int countOfProduct;

    public ProductResponseModel() {
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

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCountOfProduct() {
        return countOfProduct;
    }

    public void setCountOfProduct(int countOfProduct) {
        this.countOfProduct = countOfProduct;
    }
}
