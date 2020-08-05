package project.bumar.data.entities;

import project.bumar.data.entities.base.BaseEntity;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.CascadeType.REMOVE;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    private String name;
    private String url;
    private Set<CategoryProduct> products;
    private Set<SubCategory> subCategories;

    public Category() {
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @OneToMany(mappedBy = "category", targetEntity = SubCategory.class,
            fetch = FetchType.EAGER, cascade = {REMOVE})
    public Set<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(Set<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER, cascade = {REMOVE})
    public Set<CategoryProduct> getProducts() {
        return products;
    }

    public void setProducts(Set<CategoryProduct> products) {
        this.products = products;
    }
}
