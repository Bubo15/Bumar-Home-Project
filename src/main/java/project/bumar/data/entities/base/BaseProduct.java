package project.bumar.data.entities.base;

import project.bumar.data.entities.Business;
import project.bumar.data.entities.Picture;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

import static javax.persistence.CascadeType.ALL;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class BaseProduct extends BaseEntity {

    private String name;
    private String mainDescription;
    private double price;
    private Map<String, String> description;
    private int countOfProduct;
    private Picture picture;
    private Business business;
    private boolean isNew;
    private LocalDateTime created;

    protected BaseProduct() {
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "main_description")
    public String getMainDescription() {
        return mainDescription;
    }

    public void setMainDescription(String mainDescription) {
        this.mainDescription = mainDescription;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    public Map<String, String> getDescription() {
        return description;
    }

    public void setDescription(Map<String, String> description) {
        this.description = description;
    }

    @Column(name = "price")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Column(name = "count_of_product")
    public int getCountOfProduct() {
        return countOfProduct;
    }

    public void setCountOfProduct(int countOfProduct) {
        this.countOfProduct = countOfProduct;
    }

    @OneToOne(cascade = ALL)
    @JoinColumn(name = "picture_id", referencedColumnName = "id")
    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    @ManyToOne
    @JoinColumn(name="business_id", referencedColumnName = "id")
    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    @Column(name = "is_new")
    public boolean isNew() {
        return isNew;
    }


    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    @Column(name = "created")
    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
