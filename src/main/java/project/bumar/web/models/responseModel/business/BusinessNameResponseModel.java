package project.bumar.web.models.responseModel.business;

import com.google.gson.annotations.Expose;

public class BusinessNameResponseModel {

    @Expose
    private String name;

    public BusinessNameResponseModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
