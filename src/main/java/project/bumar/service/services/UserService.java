package project.bumar.service.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import project.bumar.data.entities.User;
import project.bumar.service.models.user.UserAdminInfoServiceModel;
import project.bumar.service.models.user.UserInfoServiceModel;
import project.bumar.service.models.user.UserRolesServiceModel;
import project.bumar.service.models.user.UserServiceModel;
import project.bumar.web.models.bindingModels.changes.ChangeEmailBindingModel;
import project.bumar.web.models.bindingModels.changes.ChangePasswordBindingModel;
import project.bumar.web.models.bindingModels.changes.ChangeUsernameBindingModel;
import project.bumar.web.models.bindingModels.user.UserRegisterBindingModel;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    Optional<UserServiceModel> register(UserRegisterBindingModel userModel);

    Optional<UserInfoServiceModel> getUserInfo(String username);

    Optional<UserServiceModel> changePassword(ChangePasswordBindingModel changePassBindingModel, String username);

    Optional<UserServiceModel> changeUsername(ChangeUsernameBindingModel changeUsernameBindingModel, String username);

    UserServiceModel changeEmail(ChangeEmailBindingModel changeEmailBindingModel, String username);

    UserRolesServiceModel getCurrentUserRoles(String username);

    User getUserByUsername(String username);

    List<UserAdminInfoServiceModel> getAllUsersWithoutOwnerAndCurrentUser(String username);

    List<UserServiceModel> getAllUsers();

    User getUserById(long userId);

    void saveUser(User user);
}

