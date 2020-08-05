package project.bumar.web.models.responseModel.subcategory;

import com.google.gson.annotations.Expose;

public class SubCategoryResponseModel {

    @Expose
    private String name;
    @Expose
    private String url;

    public SubCategoryResponseModel() {
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
}
