package project.bumar.web.models.bindingModels.news;

import javax.validation.constraints.NotNull;

public class NewsEditBindingModel {

    private String title;
    private String text;

    public NewsEditBindingModel() {
    }

    @NotNull(message = "Field can not be null")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotNull(message = "Field can not be null")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
