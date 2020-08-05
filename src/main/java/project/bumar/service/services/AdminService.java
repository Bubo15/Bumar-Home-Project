package project.bumar.service.services;

import project.bumar.service.models.user.UserAdminInfoServiceModel;
import java.util.Optional;

public interface AdminService{

    Optional<UserAdminInfoServiceModel> addRoleToUser(long userId, String role);

    Optional<UserAdminInfoServiceModel> removeRoleFromUser(long userId, String role, String currentUserUsername);

    Optional<UserAdminInfoServiceModel> blockUser(long id, String username);

    Optional<UserAdminInfoServiceModel> unBlockUser(long id, String username);
}
