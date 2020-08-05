package project.bumar.service.services;

import project.bumar.service.models.coment.CommentServiceModel;
import project.bumar.web.models.bindingModels.comment.CommentBindingModel;

import java.util.List;

public interface CommentService {

    List<CommentServiceModel> getAllComment();

    CommentServiceModel create(CommentBindingModel commentBindingModel, String username);

    CommentServiceModel edit(CommentBindingModel commentBindingModel, long id);

    CommentServiceModel delete(long id);
}
