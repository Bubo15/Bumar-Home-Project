package project.bumar.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.bumar.constant.ErrorConstants;
import project.bumar.data.entities.User;
import project.bumar.data.entities.UserProfile;
import project.bumar.data.repositories.UserRepository;
import project.bumar.exeption.AlreadyExistException;
import project.bumar.exeption.NotFoundException;
import project.bumar.exeption.PasswordNonMatchException;
import project.bumar.service.models.user.UserAdminInfoServiceModel;
import project.bumar.service.models.user.UserInfoServiceModel;
import project.bumar.service.models.user.UserRolesServiceModel;
import project.bumar.service.models.user.UserServiceModel;
import project.bumar.service.services.RoleService;
import project.bumar.service.services.UserService;
import project.bumar.web.models.bindingModels.changes.ChangeEmailBindingModel;
import project.bumar.web.models.bindingModels.changes.ChangePasswordBindingModel;
import project.bumar.web.models.bindingModels.changes.ChangeUsernameBindingModel;
import project.bumar.web.models.bindingModels.user.UserRegisterBindingModel;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<UserServiceModel> register(UserRegisterBindingModel userModel) {

        if (this.userRepository.getUserByUsername(userModel.getUsername()).isPresent()){
            throw new AlreadyExistException(ErrorConstants.USERNAME_ALREADY_EXIST);
        }

        User user = this.modelMapper.map(userModel, User.class);
        UserProfile userProfile = this.modelMapper.map(userModel, UserProfile.class);

        user.setPassword(this.passwordEncoder.encode(userModel.getPassword()));
        user.setUserProfile(userProfile);

        user.setAuthorities(new HashSet<>(Set.of(this.roleService.getRole("USER"))));

        if (userRepository.findAll().size() == 0) {
            user.getAuthorities().add(this.roleService.getRole("OWNER"));
            user.getAuthorities().add(this.roleService.getRole("ADMIN"));
            user.getAuthorities().add(this.roleService.getRole("MODERATOR"));
        }

        this.userRepository.saveAndFlush(user);
        return Optional.of(this.modelMapper.map(user, UserServiceModel.class));
    }

    @Override
    public Optional<UserInfoServiceModel> getUserInfo(String username) {
        User user = this.getUserByUsername(username);

        UserInfoServiceModel userInfoServiceModel = this.modelMapper.map(user, UserInfoServiceModel.class);

        userInfoServiceModel.setEmail(user.getUserProfile().getEmail());

        return Optional.of(userInfoServiceModel);
    }

    @Override
    public Optional<UserServiceModel> changePassword(ChangePasswordBindingModel changePassBindingModel, String username) {

        User user = getUserByUsername(username);

        if (!this.passwordEncoder.matches(changePassBindingModel.getOldPassword(), user.getPassword())){
            throw new PasswordNonMatchException(ErrorConstants.INCORRECT_OLD_PASSWORD);
        }

        user.setPassword(this.passwordEncoder.encode(changePassBindingModel.getNewPassword()));

        this.saveUser(user);

        return Optional.of(this.modelMapper.map(user, UserServiceModel.class));
    }

    @Override
    public Optional<UserServiceModel> changeUsername(ChangeUsernameBindingModel changeUsernameBindingModel, String username) {

        if (this.userRepository.getUserByUsername(changeUsernameBindingModel.getNewUsername()).isPresent()){
            throw new AlreadyExistException(ErrorConstants.USERNAME_ALREADY_EXIST);
        }

        User user = getUserByUsername(username);
        user.setUsername(changeUsernameBindingModel.getNewUsername());

        this.saveUser(user);
        return Optional.of(this.modelMapper.map(user, UserServiceModel.class));
    }

    @Override
    public UserServiceModel changeEmail(ChangeEmailBindingModel changeEmailBindingModel, String username) {

        User user = getUserByUsername(username);

        user.getUserProfile().setEmail(changeEmailBindingModel.getNewEmail());

        this.saveUser(user);
        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorConstants.USERNAME_NOT_FOUND));
    }

    @Override
    public UserRolesServiceModel getCurrentUserRoles(String username){
        return this.modelMapper.map(this.getUserByUsername(username), UserRolesServiceModel.class);
    }

    @Override
    public User getUserByUsername(String username){
        return this.userRepository.getUserByUsername(username).orElseThrow(() -> new NotFoundException(ErrorConstants.USERNAME_NOT_FOUND));
    }

    @Override
    public List<UserAdminInfoServiceModel> getAllUsersWithoutOwnerAndCurrentUser(String username) {
        return this.userRepository
                .findAll()
                .stream()
                .skip(1)
                .filter(user -> !user.getUsername().equals(username))
                .map(user -> this.modelMapper.map(user, UserAdminInfoServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserServiceModel> getAllUsers() {
        return this.userRepository
                .findAll()
                .stream()
                .map(user -> this.modelMapper.map(user, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public User getUserById(long userId) {
        return this.userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException(ErrorConstants.USER_ID_NOT_FOUND));
    }

    @Override
    public void saveUser(User user) {
        this.userRepository.saveAndFlush(user);
    }
}
