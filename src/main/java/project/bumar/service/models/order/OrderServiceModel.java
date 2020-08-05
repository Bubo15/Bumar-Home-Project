package project.bumar.service.models.order;

import project.bumar.data.entities.base.BaseProduct;

import java.time.LocalDateTime;
import java.util.Map;

public class OrderServiceModel {

    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String postCode;
    private String totalPrice;
    private LocalDateTime ordered;
    private Map<BaseProduct, Integer> products;

    public OrderServiceModel() {
    }

    public LocalDateTime getOrdered() {
        return ordered;
    }

    public void setOrdered(LocalDateTime ordered) {
        this.ordered = ordered;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Map<BaseProduct, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<BaseProduct, Integer> products) {
        this.products = products;
    }
}
