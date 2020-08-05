package project.bumar.service.models.subcategory;

import project.bumar.data.entities.Category;
import project.bumar.data.entities.SubcategoryProduct;

import java.util.Set;

public class SubcategoryServiceModel {

    private long id;
    private String name;
    private String url;
    private Category category;
    private Set<SubcategoryProduct> products;

    public SubcategoryServiceModel() {
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<SubcategoryProduct> getProducts() {
        return products;
    }

    public void setProducts(Set<SubcategoryProduct> products) {
        this.products = products;
    }
}
