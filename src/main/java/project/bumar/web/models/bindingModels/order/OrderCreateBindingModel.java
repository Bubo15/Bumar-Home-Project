package project.bumar.web.models.bindingModels.order;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

public class OrderCreateBindingModel {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String postCode;
    private String totalPrice;
    private Set<Map<String, Integer>> products;

    public OrderCreateBindingModel() {
    }

    @NotNull(message = "Field can not be null")
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

    @Email(message = "Email must be valid")
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

    public Set<Map<String, Integer>> getProducts() {
        return products;
    }

    public void setProducts(Set<Map<String, Integer>> products) {
        this.products = products;
    }
}
