package project.bumar.data.entities;

import project.bumar.data.entities.base.BaseEntity;
import project.bumar.data.entities.base.BaseProduct;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.REMOVE;

@Entity
@Table(name = "businesses")
public class Business extends BaseEntity {

    private String name;
    private Set<BaseProduct> products;
    private Picture logo;

    public Business() {
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @OneToMany(fetch = FetchType.EAGER, cascade = {REMOVE})
    public Set<BaseProduct> getProducts() {
        return products;
    }

    public void setProducts(Set<BaseProduct> products) {
        this.products = products;
    }

    @OneToOne(cascade = ALL)
    @JoinColumn(name = "logo_id", referencedColumnName = "id")
    public Picture getLogo() {
        return logo;
    }

    public void setLogo(Picture logo) {
        this.logo = logo;
    }
}
