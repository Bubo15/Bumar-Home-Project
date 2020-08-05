package project.bumar.web.models.responseModel.category;

import com.google.gson.annotations.Expose;
import org.springframework.hateoas.RepresentationModel;
import project.bumar.web.models.responseModel.subcategory.SubCategoryResponseModel;

import java.util.List;

public class CategoryResponseModel extends RepresentationModel<CategoryResponseModel> {

    @Expose
    private long id;
    @Expose
    private String name;
    @Expose
    private String url;
    @Expose
    private List<SubCategoryResponseModel> subCategory;

    public CategoryResponseModel() {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<SubCategoryResponseModel> getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(List<SubCategoryResponseModel> subCategory) {
        this.subCategory = subCategory;
    }
}
