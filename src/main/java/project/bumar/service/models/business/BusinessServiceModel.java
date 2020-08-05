package project.bumar.service.models.business;

import project.bumar.data.entities.Picture;
import project.bumar.data.entities.base.BaseProduct;

import java.util.Set;

public class BusinessServiceModel {

    private long id;
    private String name;
    private Set<BaseProduct> products;
    private Picture logo;

    public BusinessServiceModel() {
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

    public Set<BaseProduct> getProducts() {
        return products;
    }

    public void setProducts(Set<BaseProduct> products) {
        this.products = products;
    }

    public Picture getLogo() {
        return logo;
    }

    public void setLogo(Picture logo) {
        this.logo = logo;
    }
}
