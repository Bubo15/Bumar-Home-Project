package project.bumar.web.models.bindingModels.product;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public class ProductCreateBindingModel {

    private String name;
    private double price;
    private List<Map<String, String>> description;
    private String mainDescription;
    private String businessName;
    private int countOfProduct;

    public ProductCreateBindingModel() {
    }

    @NotNull(message = "Field can not be null")
    @Length(min = 3, message = "Name must be least 3 symbols")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Min(value = 0,message = "Price must be positive")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public List<Map<String, String>> getDescription() {
        return description;
    }

    public void setDescription(List<Map<String, String>> description) {
        this.description = description;
    }

    @NotNull(message = "Field can not be null")
    @Length(min = 4, message = "Main Description must be least 4 symbols")
    public String getMainDescription() {
        return mainDescription;
    }

    public void setMainDescription(String mainDescription) {
        this.mainDescription = mainDescription;
    }

    @NotNull(message = "Field can not be null")
    @Length(min = 3, message = "Business name must be least 3 symbols")
    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    @NotNull(message = "Field can not be null")
    @Min(value = 0,message = "Count of product must be positive")
    public int getCountOfProduct() {
        return countOfProduct;
    }

    public void setCountOfProduct(int countOfProduct) {
        this.countOfProduct = countOfProduct;
    }
}
