package project.bumar.web.models.responseModel.order;

import com.google.gson.annotations.Expose;
import project.bumar.web.models.responseModel.product.ProductDetailsResponseModel;

import java.time.LocalDateTime;
import java.util.Map;

public class OrderResponseModel {

    @Expose
    private String username;
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private String email;
    @Expose
    private String phone;
    @Expose
    private String address;
    @Expose
    private String city;
    @Expose
    private String postCode;
    @Expose
    private String totalPrice;
    @Expose
    private LocalDateTime ordered;
    @Expose
    private Map<ProductDetailsResponseModel, Integer> products;

    public OrderResponseModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public LocalDateTime getOrdered() {
        return ordered;
    }

    public void setOrdered(LocalDateTime ordered) {
        this.ordered = ordered;
    }

    public Map<ProductDetailsResponseModel, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<ProductDetailsResponseModel, Integer> products) {
        this.products = products;
    }
}
