package project.bumar.service.models.product;

import project.bumar.data.entities.Picture;
import project.bumar.service.models.business.BusinessServiceModel;

import java.util.Map;

public class ProductDetailsServiceModel {

    private long id;
    private String name;
    private String mainDescription;
    private double price;
    private Map<String, String> description;
    private int countOfProduct;
    private Picture picture;
    private BusinessServiceModel business;

    public ProductDetailsServiceModel() {
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

    public BusinessServiceModel getBusiness() {
        return business;
    }

    public void setBusiness(BusinessServiceModel business) {
        this.business = business;
    }

}
