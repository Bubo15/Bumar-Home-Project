package project.bumar.web.models.bindingModels.news;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class NewsCreateBindingModel {


    private String title;
    private String text;

    public NewsCreateBindingModel() {
    }

    @NotNull(message = "Field can not be null")
    @NotEmpty(message = "Field is required")
    @Length(min = 3, message = "Title must be least 3 symbols")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotNull(message = "Field can not be null")
    @NotEmpty(message = "Field is required")
    @Length(min = 10, message = "Title must be least 10 symbols")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
