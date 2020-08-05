package project.bumar.web.models.bindingModels.product;

import project.bumar.data.entities.Picture;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public class ProductEditBindingModel {


    private String name;
    private String mainDescription;
    private double price;
    private List<Map<String, String>> description;
    private int countOfProduct;
    private Picture picture;
    private String businessName;

    public ProductEditBindingModel() {
    }

    @NotNull(message = "Field can not be null")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = "Field can not be null")
    public String getMainDescription() {
        return mainDescription;
    }

    public void setMainDescription(String mainDescription) {
        this.mainDescription = mainDescription;
    }

    @NotNull(message = "Field can not be null")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @NotNull(message = "Field can not be null")
    public List<Map<String, String>> getDescription() {
        return description;
    }

    public void setDescription(List<Map<String, String>> description) {
        this.description = description;
    }

    @NotNull(message = "Field can not be null")
    public int getCountOfProduct() {
        return countOfProduct;
    }

    public void setCountOfProduct(int countOfProduct) {
        this.countOfProduct = countOfProduct;
    }

    @NotNull(message = "Field can not be null")
    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    @NotNull(message = "Field can not be null")
    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
}
