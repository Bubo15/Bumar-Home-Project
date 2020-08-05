package project.bumar.web.models.responseModel.business;

import com.google.gson.annotations.Expose;
import org.springframework.hateoas.RepresentationModel;
import project.bumar.data.entities.Picture;

public class BusinessResponseModel extends RepresentationModel<BusinessResponseModel> {

    @Expose
    private long id;
    @Expose
    private String name;
    @Expose
    private Picture logo;

    public BusinessResponseModel() {
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

    public Picture getLogo() {
        return logo;
    }

    public void setLogo(Picture logo) {
        this.logo = logo;
    }
}
