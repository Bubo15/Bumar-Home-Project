package project.bumar.data.entities;

import project.bumar.data.entities.base.BaseProduct;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "category_products")
public class CategoryProduct extends BaseProduct {

    private Category category;

    public CategoryProduct() {
    }

    @ManyToOne()
    @JoinColumn(name="category_id", referencedColumnName = "id")
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
