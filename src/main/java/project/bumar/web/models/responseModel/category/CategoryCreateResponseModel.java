package project.bumar.web.models.responseModel.category;

import com.google.gson.annotations.Expose;

public class CategoryCreateResponseModel {

    @Expose
    private String name;

    public CategoryCreateResponseModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
