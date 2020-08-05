package project.bumar.web.models.responseModel.category;

import com.google.gson.annotations.Expose;

public class CategoryNameResponseModel {

    @Expose
    private long id;
    @Expose
    private String name;

    public CategoryNameResponseModel() {
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
}
