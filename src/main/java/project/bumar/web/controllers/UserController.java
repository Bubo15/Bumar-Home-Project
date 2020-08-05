package project.bumar.web.controllers;

import com.auth0.jwt.JWT;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.bumar.data.entities.Role;
import project.bumar.service.services.UserService;
import project.bumar.web.jwtFilters.JwtProperties;
import project.bumar.web.models.bindingModels.changes.ChangeEmailBindingModel;
import project.bumar.web.models.bindingModels.changes.ChangePasswordBindingModel;
import project.bumar.web.models.bindingModels.changes.ChangeUsernameBindingModel;
import project.bumar.web.models.bindingModels.user.UserRegisterBindingModel;
import project.bumar.web.models.responseModel.user.UserAdminInfoResponseModel;
import project.bumar.web.models.responseModel.user.UserInfoResponseModel;
import project.bumar.web.models.responseModel.user.UserResponseModel;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:3000"})
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseModel> register(@Valid @RequestBody UserRegisterBindingModel model,
                                                      BindingResult result) throws URISyntaxException {

        if (result.hasErrors()) { return ResponseEntity.badRequest().build(); }
        return ResponseEntity.created(new URI("/user/register")).body(this.modelMapper.map(this.userService.register(model).orElseThrow(), UserResponseModel.class));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all/users")
    public ResponseEntity<List<UserAdminInfoResponseModel>> getAllUsers(Principal principal) {
        return ResponseEntity.ok(
                this.userService
                        .getAllUsersWithoutOwnerAndCurrentUser(principal.getName())
                        .stream()
                        .map(userServiceModel -> this.modelMapper.map(userServiceModel, UserAdminInfoResponseModel.class))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponseModel> getUserInfo(Principal principal) {
        return ResponseEntity.ok(this.modelMapper.map(this.userService.getUserInfo(principal.getName()).orElseThrow(), UserInfoResponseModel.class));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/roles")
    public ResponseEntity<List<String>> getCurrentUserRoles(Principal principal) {
        return ResponseEntity.ok(
                this.userService
                        .getCurrentUserRoles(principal.getName())
                        .getAuthorities()
                        .stream()
                        .map(Role::getAuthority)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/change/password")
    public ResponseEntity<UserResponseModel> changePassword(Principal principal,
                                            @Valid @RequestBody ChangePasswordBindingModel changePassBindingModel,
                                            BindingResult result) {
        if (result.hasErrors()) { return ResponseEntity.badRequest().build(); }
        return ResponseEntity.ok(this.modelMapper.map(userService.changePassword(changePassBindingModel, principal.getName()).orElseThrow(), UserResponseModel.class));
    }

    @PostMapping("/change/username")
    public ResponseEntity<?> changeUsername(Principal principal,
                                            @Valid @RequestBody ChangeUsernameBindingModel changeUsernameBindingModel,
                                            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }

        String token = createToken(changeUsernameBindingModel);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);

        return ResponseEntity.ok().headers(responseHeaders)
                .body(this.modelMapper.map(this.userService.changeUsername(changeUsernameBindingModel, principal.getName()).orElseThrow(), UserResponseModel.class));
    }

    @PostMapping("/change/email")
    public ResponseEntity<UserResponseModel> changeEmail(Principal principal,
                                                         @Valid @RequestBody ChangeEmailBindingModel changeEmailBindingModel,
                                                         BindingResult result) {

        if (result.hasErrors()) { return ResponseEntity.badRequest().build(); }
        return ResponseEntity.ok(this.modelMapper.map(userService.changeEmail(changeEmailBindingModel, principal.getName()), UserResponseModel.class));
    }

    private String createToken(ChangeUsernameBindingModel changeUsernameBindingModel) {
        return JWT.create()
                .withSubject(changeUsernameBindingModel.getNewUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .sign(HMAC512(JwtProperties.SECRET.getBytes()));
    }
}
