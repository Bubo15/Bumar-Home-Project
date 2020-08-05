package project.bumar.data.entities;

import project.bumar.data.entities.base.BaseProduct;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "subcategory_products")
public class SubcategoryProduct extends BaseProduct {

    private SubCategory subcategory;

    public SubcategoryProduct() {
    }

    @ManyToOne()
    @JoinColumn(name="subcategory_id", referencedColumnName = "id")
    public SubCategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(SubCategory subcategory) {
        this.subcategory = subcategory;
    }
}
