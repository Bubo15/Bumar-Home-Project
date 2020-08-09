package project.bumar.services;

import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.bumar.BaseTest;
import project.bumar.constant.ErrorConstants;
import project.bumar.data.entities.Role;
import project.bumar.data.entities.User;
import project.bumar.data.entities.UserProfile;
import project.bumar.data.repositories.RoleRepository;
import project.bumar.data.repositories.UserRepository;
import project.bumar.exeption.AlreadyExistException;
import project.bumar.exeption.NotFoundException;
import project.bumar.exeption.PasswordNonMatchException;
import project.bumar.exeption.base.BaseCustomException;
import project.bumar.services.models.user.UserAdminInfoServiceModel;
import project.bumar.services.models.user.UserInfoServiceModel;
import project.bumar.services.models.user.UserRolesServiceModel;
import project.bumar.services.models.user.UserServiceModel;
import project.bumar.services.services.RoleService;
import project.bumar.services.services.UserService;
import project.bumar.services.services.impl.RoleServiceImpl;
import project.bumar.services.services.impl.UserServiceImpl;
import project.bumar.web.models.bindingModels.changes.ChangeEmailBindingModel;
import project.bumar.web.models.bindingModels.changes.ChangePasswordBindingModel;
import project.bumar.web.models.bindingModels.changes.ChangeUsernameBindingModel;
import project.bumar.web.models.bindingModels.user.UserRegisterBindingModel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest extends BaseTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private RoleService roleService;

    private UserService userService;

    private PasswordEncoder passwordEncoder;

    @Before
    public void beforeEach() {
        passwordEncoder = new BCryptPasswordEncoder();

        roleService = new RoleServiceImpl(roleRepository);
        userService = new UserServiceImpl(userRepository, roleService, new ModelMapper(), passwordEncoder);

        roleRepository.saveAll
                (List.of(new Role("ROLE_USER"), new Role("ROLE_ADMIN"), new Role("ROLE_MODERATOR"), new Role("ROLE_OWNER")));
    }

    @Test
    public void register_shouldRegister_whenGiveValidData() {
        UserRegisterBindingModel userModel = new UserRegisterBindingModel();
        userModel.setUsername("Username");
        userModel.setFirstName("FirstName");
        userModel.setLastName("LastName");
        userModel.setEmail("email@abv.bg");
        userModel.setPassword("password");
        userModel.setConfirmPassword("password");

        Optional<UserServiceModel> userServiceModel = userService.register(userModel);

        assertEquals(1, userRepository.count());
        assertEquals(userModel.getUsername(), userServiceModel.get().getUsername());
    }

    @Test
    public void register_shouldThrowException_whenGiveAlreadyExistingUser() {
        User user = new User();
        user.setUsername("Username");

        userRepository.saveAndFlush(user);

        UserRegisterBindingModel userModel = new UserRegisterBindingModel();
        userModel.setUsername("Username");

        BaseCustomException exception = assertThrows(AlreadyExistException.class, () -> {
            userService.register(userModel);
        });

        String expectedMessage = ErrorConstants.USERNAME_ALREADY_EXIST;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getUserInfo_shouldReturnUserInfo_whenGiveValidData() {
        User user = new User();
        user.setUsername("Username");

        UserProfile userProfile = new UserProfile();

        userProfile.setEmail("email@abv.bg");

        user.setUserProfile(userProfile);

        userRepository.saveAndFlush(user);

        Optional<UserInfoServiceModel> userInfoServiceModel = userService.getUserInfo(user.getUsername());

        assertEquals(userInfoServiceModel.get().getUsername(), user.getUsername());
    }

    @Test
    public void getUserInfo_shouldThrowException_whenGiveNotExistUser() {
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            userService.getUserInfo("NotFoundUsername");
        });

        String expectedMessage = ErrorConstants.USERNAME_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void changePassword_shouldChangePassword_whenGiveValidData() {
        User user = new User();
        user.setUsername("Username");
        user.setPassword(passwordEncoder.encode("oldPassword"));

        UserProfile userProfile = new UserProfile();

        userProfile.setEmail("email@abv.bg");

        user.setUserProfile(userProfile);

        userRepository.saveAndFlush(user);

        ChangePasswordBindingModel changePassBindingModel = new ChangePasswordBindingModel();
        changePassBindingModel.setOldPassword("oldPassword");
        changePassBindingModel.setNewPassword("newPassword");

        Optional<UserServiceModel> userServiceModel = userService.changePassword(changePassBindingModel, user.getUsername());

        assertEquals(userServiceModel.get().getUsername(), user.getUsername());
    }

    @Test
    public void changePassword_shouldThrowException_whenGiveInvalidUsername() {
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            userService.changePassword(new ChangePasswordBindingModel(), "NotFoundUsername");
        });

        String expectedMessage = ErrorConstants.USERNAME_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void changePassword_shouldThrowException_whenGiveIncorrectOldPassword() {
        User user = new User();
        user.setUsername("Username");
        user.setPassword(passwordEncoder.encode("oldPassword"));

        userRepository.saveAndFlush(user);

        ChangePasswordBindingModel changePassBindingModel = new ChangePasswordBindingModel();
        changePassBindingModel.setOldPassword("newPassword");
        changePassBindingModel.setNewPassword("newPassword");

        BaseCustomException exception = assertThrows(PasswordNonMatchException.class, () -> {
            userService.changePassword(changePassBindingModel, user.getUsername());
        });

        String expectedMessage = ErrorConstants.INCORRECT_OLD_PASSWORD;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void changeUsername_shouldChangeUsername_whenGiveCorrectData() {
        User user = new User();
        user.setUsername("Username");

        userRepository.saveAndFlush(user);

        ChangeUsernameBindingModel changeUsernameBindingModel = new ChangeUsernameBindingModel();
        changeUsernameBindingModel.setNewUsername("NewUsername");

        Optional<UserServiceModel> userServiceModel = userService.changeUsername(changeUsernameBindingModel, user.getUsername());

        assertEquals(userServiceModel.get().getUsername(), user.getUsername());
    }

    @Test
    public void changeUsername_shouldThrowException_whenGiveAlreadyExistUsername() {
        User user = new User();
        user.setUsername("Username");

        userRepository.saveAndFlush(user);

        ChangeUsernameBindingModel changeUsernameBindingModel = new ChangeUsernameBindingModel();
        changeUsernameBindingModel.setNewUsername("Username");

        BaseCustomException exception = assertThrows(AlreadyExistException.class, () -> {
            userService.changeUsername(changeUsernameBindingModel, user.getUsername());
        });

        String expectedMessage = ErrorConstants.USERNAME_ALREADY_EXIST;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void changeEmail_shouldChangeEmail_whenGiveCorrectData() {
        User user = new User();
        user.setUsername("Username");

        UserProfile userProfile = new UserProfile();

        userProfile.setEmail("email@abv.bg");

        user.setUserProfile(userProfile);

        userRepository.saveAndFlush(user);

        ChangeEmailBindingModel changeEmailBindingModel = new ChangeEmailBindingModel();
        changeEmailBindingModel.setNewEmail("email@gmail.com");

        UserServiceModel userServiceModel = userService.changeEmail(changeEmailBindingModel, user.getUsername());

        assertEquals(userServiceModel.getUserProfile().getEmail(), user.getUserProfile().getEmail());
    }

    @Test
    public void loadUserByUsername_returnUserDetails_whenGiveCorrectData(){
        User user = new User();
        user.setUsername("Username");
        userRepository.saveAndFlush(user);

        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());

        assertEquals(user.getUsername(), userDetails.getUsername());
    }

    @Test
    public void loadUserByUsername_shouldThrowException_whenGiveIncorrectData(){
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("InvalidUsername");
        });

        String expectedMessage = ErrorConstants.USERNAME_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getCurrentUserRoles_returnCurrentUserRoles_whenGiveValidData(){
        User user = new User();

        user.setUsername("Username");
        user.setAuthorities(Set.of(roleService.getRole("USER")));

        userRepository.saveAndFlush(user);

        UserRolesServiceModel userRolesServiceModel = userService.getCurrentUserRoles(user.getUsername());

        assertEquals(1, userRolesServiceModel.getAuthorities().size());
    }

    @Test
    public void getCurrentUserRoles_throwException_whenGiveIncorrectUsername(){
        Exception exception = assertThrows(NotFoundException.class, () -> {
            userService.getCurrentUserRoles("InvalidUsername");
        });

        String expectedMessage = ErrorConstants.USERNAME_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getUserByUsername_returnUser_whenGiveValidData(){
        User user = new User();
        user.setUsername("Username");
        userRepository.saveAndFlush(user);

        assertEquals(user.getUsername(), userService.getUserByUsername(user.getUsername()).getUsername());
    }

    @Test
    public void getUserByUsername_throwException_whenGiveIncorrectUsername(){
        Exception exception = assertThrows(NotFoundException.class, () -> {
            userService.getUserByUsername("InvalidUsername");
        });

        String expectedMessage = ErrorConstants.USERNAME_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getAllUsersWithoutOwnerAndCurrentUser_shouldReturnAllUsersWithoutCurrentAndOwner(){
        User user = new User();

        user.setUsername("Username");
        user.setEnabled(true);
        user.setAuthorities(Set.of(roleService.getRole("OWNER")));

        userRepository.save(user);

        User user1 = new User();

        user1.setUsername("Username1");
        user1.setEnabled(true);
        user1.setAuthorities(Set.of(roleService.getRole("OWNER")));

        userRepository.save(user1);

        User user2 = new User();

        user2.setUsername("Username2");
        user2.setEnabled(true);
        user2.setAuthorities(Set.of(roleService.getRole("USER")));

        userRepository.save(user2);

        List<UserAdminInfoServiceModel> userAdminInfoServiceModels = userService.getAllUsersWithoutOwnerAndCurrentUser(user1.getUsername());

        assertEquals(1, userAdminInfoServiceModels.size());
    }

    @Test
    public void getAllUsers_shouldReturnAllUsers(){
        User user = new User();
        user.setUsername("Username");
        userRepository.save(user);

        List<UserServiceModel> userServiceModels = userService.getAllUsers();

        assertEquals(1, userServiceModels.size());
    }

    @Test
    public void getById_shouldReturnUser_whenGiveValidData(){
        User user = new User();
        user.setUsername("Username");
        userRepository.save(user);

        assertEquals(user.getUsername(), userService.getUserById(user.getId()).getUsername());
    }

    @Test
    public void getById_shouldThrowException_whenGiveInValidData(){
        Exception exception = assertThrows(NotFoundException.class, () -> {
            userService.getUserById(-1);
        });

        String expectedMessage = ErrorConstants.USER_ID_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void save_shouldSaveUser(){
        userService.saveUser(new User());
        assertEquals(1, userRepository.count());
    }
}
