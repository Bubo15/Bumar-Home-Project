package project.bumar.data.entities;

import project.bumar.data.entities.base.BaseEntity;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.CascadeType.REMOVE;

@Entity
@Table(name = "subcategories")
public class SubCategory extends BaseEntity {

    private String name;
    private String url;
    private Category category;
    private Set<SubcategoryProduct> products;

    public SubCategory() {
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

    @ManyToOne
    @JoinColumn(name="category_id", referencedColumnName = "id")
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @OneToMany(mappedBy = "subcategory", fetch = FetchType.EAGER, cascade = {REMOVE})
    public Set<SubcategoryProduct> getProducts() {
        return products;
    }

    public void setProducts(Set<SubcategoryProduct> products) {
        this.products = products;
    }
}
