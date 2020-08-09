package project.bumar.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.bumar.BaseTest;
import project.bumar.constant.ErrorConstants;
import project.bumar.data.entities.Role;
import project.bumar.data.entities.User;
import project.bumar.data.entities.UserProfile;
import project.bumar.data.repositories.RoleRepository;
import project.bumar.data.repositories.UserRepository;
import project.bumar.exeption.AlreadyExistException;
import project.bumar.exeption.NotAllowedException;
import project.bumar.exeption.NotFoundException;
import project.bumar.exeption.base.BaseCustomException;
import project.bumar.services.models.user.UserAdminInfoServiceModel;
import project.bumar.services.services.AdminService;
import project.bumar.services.services.RoleService;
import project.bumar.services.services.UserService;
import project.bumar.services.services.impl.AdminServiceImpl;
import project.bumar.services.services.impl.RoleServiceImpl;
import project.bumar.services.services.impl.UserServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AdminServiceTest extends BaseTest {

    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    private AdminService adminService;

    @Before
    public void beforeEach() {
        ModelMapper modelMapper = new ModelMapper();

        roleService = new RoleServiceImpl(roleRepository);
        userService = new UserServiceImpl(userRepository, roleService, modelMapper, new BCryptPasswordEncoder());
        adminService = new AdminServiceImpl(userService, roleService, modelMapper);

        roleRepository.saveAll
                (List.of(new Role("ROLE_USER"), new Role("ROLE_ADMIN"), new Role("ROLE_MODERATOR"), new Role("ROLE_OWNER")));
    }

    @After
    public void afterEach(){
        userRepository.deleteAll();
    }

    @Test
    public void addRole_shouldAddRoleToUser_whenGiveValidData(){

        User user = saveUser();

        Optional<UserAdminInfoServiceModel> userAdminInfoServiceModel = adminService.addRoleToUser(user.getId(), "ADMIN");

        assertEquals(2, userAdminInfoServiceModel.get().getAuthorities().size());
        assertEquals(userAdminInfoServiceModel.get().getUsername(), user.getUsername());
    }

    @Test
    public void addRole_shouldNotAddRoleToUser_whenGiveInValidUserId(){

        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            adminService.addRoleToUser(1, "ADMIN");
        });

        String expectedMessage = ErrorConstants.USER_ID_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void addRole_shouldNotAddRoleToUser_whenGiveOwnerRole(){

        User user = saveUser();

        BaseCustomException exception = assertThrows(NotAllowedException.class, () -> {
            adminService.addRoleToUser(user.getId(), "OWNER");
        });

        String expectedMessage = ErrorConstants.NOT_ALLOWED_ROLE;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void addRole_shouldNotAddRoleToUser_whenGiveAlreadyExistRole(){

        User user = saveUser();

        BaseCustomException exception = assertThrows(AlreadyExistException.class, () -> {
            adminService.addRoleToUser(user.getId(), "USER");
        });

        String expectedMessage = ErrorConstants.USER_ROLE_ALREADY_EXIST;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void removeRoleFromUser_shouldRemoveRoleFromUser_whenGiveValidData(){

        User user = saveUser();

        User currentUser = new User();

        currentUser.setUsername("CurrentUsername");
        currentUser.setPassword("password");
        currentUser.setEnabled(true);
        currentUser.setAuthorities(Set.of(roleService.getRole("USER"), roleService.getRole("ADMIN")));

        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName("FirstName");
        userProfile.setLastName("LastName");
        userProfile.setEmail("email@abv.bg");

        currentUser.setUserProfile(userProfile);

        this.userRepository.saveAndFlush(currentUser);

        Optional<UserAdminInfoServiceModel> userAdminInfoServiceModel = adminService.removeRoleFromUser(user.getId(), "USER", currentUser.getUsername());

        assertEquals(0, userAdminInfoServiceModel.get().getAuthorities().size());
        assertEquals(userAdminInfoServiceModel.get().getUsername(), user.getUsername());
    }

    @Test
    public void removeRoleFromUser_shouldNotRemoveRoleFromUser_whenGiveInValidUserId(){

        User user = saveUser();

        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            adminService.removeRoleFromUser(-1, "ADMIN", user.getUsername());
        });

        String expectedMessage = ErrorConstants.USER_ID_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void removeRoleFromUser_shouldNotRemoveRoleFromUser_whenGiveInValidCurrentUserUsername(){

        User user = saveUser();

        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            adminService.removeRoleFromUser(user.getId(), "ADMIN", "NoExistUser");
        });

        String expectedMessage = ErrorConstants.USERNAME_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void removeRoleFromUser_shouldNotRemoveRoleFromUser_whenGiveOwnerRole(){

        User user = saveUser();

        BaseCustomException exception = assertThrows(NotAllowedException.class, () -> {
            adminService.removeRoleFromUser(user.getId(), "OWNER", user.getUsername());
        });

        String expectedMessage = ErrorConstants.NOT_ALLOWED_ROLE;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void removeRoleFromUser_shouldNotRemoveRoleFromUser_whenGiveAdminRole_and_currentUserRolesNotContainsRoleOwner(){

        User user = saveUser();

        BaseCustomException exception = assertThrows(NotAllowedException.class, () -> {
            adminService.removeRoleFromUser(user.getId(), "ADMIN", user.getUsername());
        });

        String expectedMessage = ErrorConstants.NOT_ALLOWED_ROLE;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void removeRoleFromUser_shouldNotRemoveRoleFromUser_whenGiveRoleWhichNotExistInUser(){

        User user = saveUser();

        User currentUser = new User();

        currentUser.setUsername("CurrentUsername");
        currentUser.setPassword("password");
        currentUser.setEnabled(true);
        currentUser.setAuthorities(Set.of(roleService.getRole("USER"), roleService.getRole("OWNER")));

        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName("FirstName");
        userProfile.setLastName("LastName");
        userProfile.setEmail("email@abv.bg");

        currentUser.setUserProfile(userProfile);

        this.userRepository.saveAndFlush(currentUser);

        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            adminService.removeRoleFromUser(user.getId(), "ADMIN", currentUser.getUsername());
        });

        String expectedMessage = "User role Not Found!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void blockUser_shouldBlockUser_whenGiveValidData(){

        User user = saveUser();

        User currentUser = new User();

        currentUser.setUsername("CurrentUsername");
        currentUser.setPassword("password");
        currentUser.setEnabled(true);
        currentUser.setAuthorities(Set.of(roleService.getRole("USER"), roleService.getRole("OWNER")));

        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName("FirstName");
        userProfile.setLastName("LastName");
        userProfile.setEmail("email@abv.bg");

        currentUser.setUserProfile(userProfile);

        this.userRepository.saveAndFlush(currentUser);

        Optional<UserAdminInfoServiceModel> userAdminInfoServiceModel = adminService.blockUser(user.getId(), currentUser.getUsername());

        assertFalse(userAdminInfoServiceModel.get().isEnabled());
    }

    @Test
    public void blockUser_shouldNotBlockUser_whenUserHasRoleAdmin_butCurrentUserHasNotRoleOwner(){

        User user = new User();

        user.setUsername("Username");
        user.setPassword("password");
        user.setEnabled(true);
        user.setAuthorities(Set.of(roleService.getRole("USER"), roleService.getRole("ADMIN")));

        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName("FirstName");
        userProfile.setLastName("LastName");
        userProfile.setEmail("email@abv.bg");

        user.setUserProfile(userProfile);

        this.userRepository.saveAndFlush(user);

        User currentUser = new User();

        currentUser.setUsername("CurrentUsername");
        currentUser.setPassword("password");
        currentUser.setEnabled(true);
        currentUser.setAuthorities(Set.of(roleService.getRole("USER"), roleService.getRole("ADMIN")));

        UserProfile userProfile1 = new UserProfile();
        userProfile1.setFirstName("FirstName");
        userProfile1.setLastName("LastName");
        userProfile1.setEmail("email@abv.bg");

        currentUser.setUserProfile(userProfile1);

        this.userRepository.saveAndFlush(currentUser);

        BaseCustomException exception = assertThrows(NotAllowedException.class, () -> {
            adminService.blockUser(user.getId(), currentUser.getUsername());
        });

        String expectedMessage = ErrorConstants.ONLY_OWNER_CAN_BLOCK_AND_UNBLOCK_ADMIN;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void unBlockUser_shouldUnBlockUser_whenGiveValidData(){

        User user = saveUser();

        User currentUser = new User();

        currentUser.setUsername("CurrentUsername");
        currentUser.setPassword("password");
        currentUser.setEnabled(true);
        currentUser.setAuthorities(Set.of(roleService.getRole("USER"), roleService.getRole("OWNER")));

        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName("FirstName");
        userProfile.setLastName("LastName");
        userProfile.setEmail("email@abv.bg");

        currentUser.setUserProfile(userProfile);

        this.userRepository.saveAndFlush(currentUser);

        Optional<UserAdminInfoServiceModel> userAdminInfoServiceModel = adminService.unBlockUser(user.getId(), currentUser.getUsername());

        assertTrue(userAdminInfoServiceModel.get().isEnabled());
    }

    @Test
    public void unBlockUser_shouldNotUnBlockUser_whenUserHasRoleAdmin_butCurrentUserHasNotRoleOwner(){

        User user = new User();

        user.setUsername("Username");
        user.setPassword("password");
        user.setEnabled(true);
        user.setAuthorities(Set.of(roleService.getRole("USER"), roleService.getRole("ADMIN")));

        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName("FirstName");
        userProfile.setLastName("LastName");
        userProfile.setEmail("email@abv.bg");

        user.setUserProfile(userProfile);

        this.userRepository.saveAndFlush(user);

        User currentUser = new User();

        currentUser.setUsername("CurrentUsername");
        currentUser.setPassword("password");
        currentUser.setEnabled(true);
        currentUser.setAuthorities(Set.of(roleService.getRole("USER"), roleService.getRole("ADMIN")));

        UserProfile userProfile1 = new UserProfile();
        userProfile1.setFirstName("FirstName");
        userProfile1.setLastName("LastName");
        userProfile1.setEmail("email@abv.bg");

        currentUser.setUserProfile(userProfile1);

        this.userRepository.saveAndFlush(currentUser);

        BaseCustomException exception = assertThrows(NotAllowedException.class, () -> {
            adminService.unBlockUser(user.getId(), currentUser.getUsername());
        });

        String expectedMessage = ErrorConstants.ONLY_OWNER_CAN_BLOCK_AND_UNBLOCK_ADMIN;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    private User saveUser(){
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

        this.userRepository.saveAndFlush(user);

        return user;
    }
}
