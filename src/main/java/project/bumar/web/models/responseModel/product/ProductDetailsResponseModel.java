package project.bumar.web.models.responseModel.product;

import com.google.gson.annotations.Expose;
import project.bumar.data.entities.Picture;
import project.bumar.web.models.responseModel.business.BusinessResponseModel;

import java.util.Map;

public class ProductDetailsResponseModel {

    @Expose
    private long id;
    @Expose
    private String name;
    @Expose
    private String mainDescription;
    @Expose
    private double price;
    @Expose
    private Map<String, String> description;
    @Expose
    private int countOfProduct;
    @Expose
    private Picture picture;
    @Expose
    private BusinessResponseModel business;

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

    public String getMainDescription() {
        return mainDescription;
    }

    public void setMainDescription(String mainDescription) {
        this.mainDescription = mainDescription;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Map<String, String> getDescription() {
        return description;
    }

    public void setDescription(Map<String, String> description) {
        this.description = description;
    }

    public int getCountOfProduct() {
        return countOfProduct;
    }

    public void setCountOfProduct(int countOfProduct) {
        this.countOfProduct = countOfProduct;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public BusinessResponseModel getBusiness() {
        return business;
    }

    public void setBusiness(BusinessResponseModel business) {
        this.business = business;
    }

}
