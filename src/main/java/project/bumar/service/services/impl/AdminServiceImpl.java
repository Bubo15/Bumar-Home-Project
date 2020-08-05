package project.bumar.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.bumar.constant.ErrorConstants;
import project.bumar.data.entities.Role;
import project.bumar.data.entities.User;
import project.bumar.exeption.AlreadyExistException;
import project.bumar.exeption.NotAllowedException;
import project.bumar.service.models.user.UserAdminInfoServiceModel;
import project.bumar.service.services.AdminService;
import project.bumar.service.services.RoleService;
import project.bumar.service.services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserService userService;
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminServiceImpl(UserService userService, RoleService roleService, ModelMapper modelMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Optional<UserAdminInfoServiceModel> addRoleToUser(long userId, String role) {
        return addRole(role, userId);
    }

    @Override
    public Optional<UserAdminInfoServiceModel> removeRoleFromUser(long userId, String role, String currentUserUsername){
        return removeRole(role, userId, currentUserUsername);
    }

    @Override
    public Optional<UserAdminInfoServiceModel> blockUser(long id, String username) {
        return this.mainInBlockAndUnBlock(id, false, username);
    }

    @Override
    public Optional<UserAdminInfoServiceModel> unBlockUser(long id, String username) {
       return this.mainInBlockAndUnBlock(id, true, username);
    }

    private Optional<UserAdminInfoServiceModel> mainInBlockAndUnBlock(long id, boolean block, String currentUserUsername){
        User currentUser = this.userService.getUserByUsername(currentUserUsername);
        User user = this.userService.getUserById(id);

        List<String> userRoles = getUserRoles(user);
        List<String> currentUserRoles = getUserRoles(currentUser);

        if (userRoles.contains("ADMIN") && !currentUserRoles.contains("ROLE_OWNER")){
            throw new NotAllowedException(ErrorConstants.ONLY_OWNER_CAN_BLOCK_AND_UNBLOCK_ADMIN);
        }

        user.setEnabled(block);
        this.userService.saveUser(user);

        return Optional.of(this.modelMapper.map(user, UserAdminInfoServiceModel.class));
    }

    private Optional<UserAdminInfoServiceModel> removeRole(String givenRole, long userId, String currentUserUsername){
        User currentUser = this.userService.getUserByUsername(currentUserUsername);
        User user = this.userService.getUserById(userId);

        List<String> userRoles = getUserRoles(user);
        List<String> currentUserRoles = getUserRoles(currentUser);

        if (givenRole.equalsIgnoreCase("ADMIN") && !currentUserRoles.contains("ROLE_OWNER")){
            throw new NotAllowedException(ErrorConstants.NOT_ALLOWED_ROLE);
        }

        if (givenRole.equalsIgnoreCase("OWNER")){
            throw new NotAllowedException(ErrorConstants.NOT_ALLOWED_ROLE);
        }

        if (!userRoles.contains("ROLE_" + givenRole)) {
            throw new AlreadyExistException(ErrorConstants.USER_ROLE_NOT_FOUND);
        }

        Role role = this.roleService.getRole(givenRole);

        user.getAuthorities().remove(role);

        user.setAuthorities(user.getAuthorities()
                .stream()
                .filter(r -> !r.getAuthority().equals("ROLE_" + givenRole))
                .collect(Collectors.toSet()));

        this.userService.saveUser(user);

        return Optional.of(this.modelMapper.map(user, UserAdminInfoServiceModel.class));
    }

    private Optional<UserAdminInfoServiceModel> addRole(String givenRole, long userId){
        User user = this.userService.getUserById(userId);

        if (givenRole.equalsIgnoreCase("OWNER")){
            throw new NotAllowedException(ErrorConstants.NOT_ALLOWED_ROLE);
        }

        List<String> userRoles = user.getAuthorities().stream().map(Role::getAuthority).collect(Collectors.toList());

        if (userRoles.contains("ROLE_" + givenRole)) {
            throw new AlreadyExistException(ErrorConstants.USER_ROLE_ALREADY_EXIST);
        }

        Role role = this.roleService.getRole(givenRole);

        user.getAuthorities().add(role);

        this.userService.saveUser(user);

        return Optional.of(this.modelMapper.map(user, UserAdminInfoServiceModel.class));
    }

    private List<String> getUserRoles(User user){
        return user.getAuthorities().stream().map(Role::getAuthority).collect(Collectors.toList());
    }
}
