package project.bumar.service.models.category;

import project.bumar.data.entities.CategoryProduct;
import project.bumar.data.entities.SubCategory;

import java.util.Set;

public class CategoryServiceModel {

    private long id;
    private String name;
    private String url;
    private Set<CategoryProduct> products;
    private Set<SubCategory> subCategories;

    public CategoryServiceModel() {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(Set<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    public Set<CategoryProduct> getProducts() {
        return products;
    }

    public void setProducts(Set<CategoryProduct> products) {
        this.products = products;
    }
}
