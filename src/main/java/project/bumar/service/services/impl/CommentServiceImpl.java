package project.bumar.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.bumar.constant.ErrorConstants;
import project.bumar.data.entities.Comment;
import project.bumar.data.entities.User;
import project.bumar.data.repositories.CommentRepository;
import project.bumar.exeption.NotFoundException;
import project.bumar.service.models.coment.CommentServiceModel;
import project.bumar.service.services.CommentService;
import project.bumar.service.services.UserService;
import project.bumar.web.models.bindingModels.comment.CommentBindingModel;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserService userService, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CommentServiceModel> getAllComment(){
        return this
                .commentRepository
                .findAll()
                .stream()
                .map(comment -> this.modelMapper.map(comment, CommentServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public CommentServiceModel create(CommentBindingModel commentBindingModel, String username) {

        User user = this.userService.getUserByUsername(username);

        Comment comment = this.modelMapper.map(commentBindingModel, Comment.class);
        comment.setUser(user);

        this.commentRepository.saveAndFlush(comment);

        return this.modelMapper.map(comment, CommentServiceModel.class);
    }

    @Override
    public CommentServiceModel edit(CommentBindingModel commentBindingModel, long id) {

        Comment comment = this.getNativeCommentByIs(id);

        comment.setDescription(commentBindingModel.getDescription());

        this.commentRepository.saveAndFlush(comment);

        return this.modelMapper.map(comment, CommentServiceModel.class);
    }

    @Override
    public CommentServiceModel delete(long id) {

        Comment comment = this.getNativeCommentByIs(id);

        this.commentRepository.deleteById(id);

        return this.modelMapper.map(comment, CommentServiceModel.class);
    }

    private Comment getNativeCommentByIs(long id){
        return this.commentRepository.getCommentById(id)
                .orElseThrow(() -> new NotFoundException(ErrorConstants.COMMENT_ID_NOT_FOUND));
    }
}
