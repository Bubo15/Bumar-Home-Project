package project.bumar.services;

import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.bumar.BaseTest;
import project.bumar.constant.ErrorConstants;
import project.bumar.data.entities.Comment;
import project.bumar.data.entities.Role;
import project.bumar.data.entities.User;
import project.bumar.data.entities.UserProfile;
import project.bumar.data.repositories.CommentRepository;
import project.bumar.data.repositories.RoleRepository;
import project.bumar.data.repositories.UserRepository;
import project.bumar.exeption.NotFoundException;
import project.bumar.exeption.base.BaseCustomException;
import project.bumar.services.models.coment.CommentServiceModel;
import project.bumar.services.services.CommentService;
import project.bumar.services.services.RoleService;
import project.bumar.services.services.UserService;
import project.bumar.services.services.impl.CommentServiceImpl;
import project.bumar.services.services.impl.RoleServiceImpl;
import project.bumar.services.services.impl.UserServiceImpl;
import project.bumar.web.models.bindingModels.comment.CommentBindingModel;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CommentServiceTest extends BaseTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private UserService userService;

    private CommentService commentService;

    private RoleService roleService;

    @Before
    public void beforeEach() {
        ModelMapper modelMapper = new ModelMapper();

        roleService = new RoleServiceImpl(roleRepository);
        userService = new UserServiceImpl(userRepository, roleService, modelMapper, new BCryptPasswordEncoder());
        commentService = new CommentServiceImpl(commentRepository, userService, modelMapper);

        roleRepository.saveAll
                (List.of(new Role("ROLE_USER"), new Role("ROLE_ADMIN"), new Role("ROLE_MODERATOR"), new Role("ROLE_OWNER")));
    }

    @Test
    public void getAllComment_shouldReturnAllComment(){
        commentRepository.saveAll(List.of(new Comment(), new Comment()));

        List<CommentServiceModel> commentServiceModels = commentService.getAllComment();

        assertEquals(2, commentServiceModels.size());
    }

    @Test
    public void create_shouldCreateComment_whenGiveValidData(){
        User user = new User();
        user.setUsername("Username");
        user.setPassword("password");
        user.setEnabled(true);
        user.setAuthorities(Set.of(roleService.getRole("USER")));

        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName("FirstName");
        userProfile.setLastName("LastName");
        userProfile.setEmail("email@abv.bg");

        user.setUserProfile(userProfile);

        userRepository.saveAndFlush(user);

        CommentBindingModel commentBindingModel = new CommentBindingModel();
        commentBindingModel.setDescription("asdasdasdasd");

        CommentServiceModel commentServiceModel = commentService.create(commentBindingModel, user.getUsername());

        assertEquals(commentBindingModel.getDescription(), commentServiceModel.getDescription());
    }

    @Test
    public void create_shouldThrowException_whenGiveNotExistUsername(){
        CommentBindingModel commentBindingModel = new CommentBindingModel();
        commentBindingModel.setDescription("aaaaaaaaaa");

        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            commentService.create(commentBindingModel, "Not Exist");
        });

        String expectedMessage = ErrorConstants.USERNAME_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void edit_shouldEditComment_whenGiveValidaData(){
        Comment comment = new Comment();
        comment.setDescription("aaaaaaaaaa");

        commentRepository.saveAndFlush(comment);

        CommentBindingModel commentBindingModel = new CommentBindingModel();
        commentBindingModel.setDescription("bbbbbbbbbb");

        CommentServiceModel commentServiceModel = commentService.edit(commentBindingModel, comment.getId());

        assertEquals(comment.getDescription(), commentServiceModel.getDescription());
    }

    @Test
    public void delete_shouldDeleteComment_whenGiveValidaData(){
        Comment comment = new Comment();
        comment.setDescription("aaaaaaaaaa");

        commentRepository.saveAndFlush(comment);

        CommentServiceModel commentServiceModel = commentService.delete(comment.getId());

        assertEquals("aaaaaaaaaa", commentServiceModel.getDescription());
        assertEquals(0, commentRepository.count());
    }

    @Test
    public void edit_shouldThrowException_whenGiveInValidaData(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            commentService.edit(new CommentBindingModel(), -1);
        });

        String expectedMessage = ErrorConstants.COMMENT_ID_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
