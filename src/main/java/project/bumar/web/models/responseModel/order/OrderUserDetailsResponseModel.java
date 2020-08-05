package project.bumar.web.models.responseModel.order;

import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;

public class OrderUserDetailsResponseModel {

    @Expose
    private long id;
    @Expose
    private String totalPrice;
    @Expose
    private LocalDateTime ordered;
    @Expose
    private int countOfProducts;

    public OrderUserDetailsResponseModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getOrdered() {
        return ordered;
    }

    public void setOrdered(LocalDateTime ordered) {
        this.ordered = ordered;
    }

    public int getProducts() {
        return countOfProducts;
    }

    public void setProducts(int countOfProducts) {
        this.countOfProducts = countOfProducts;
    }
}
