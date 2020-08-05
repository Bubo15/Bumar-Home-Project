package project.bumar.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.bumar.service.services.AdminService;
import project.bumar.web.models.responseModel.user.UserAdminInfoResponseModel;

import java.security.Principal;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = {"http://localhost:3000"})
public class AdminController {

    private final ModelMapper modelMapper;
    private final AdminService adminService;

    @Autowired
    public AdminController(ModelMapper modelMapper, AdminService adminService) {
        this.modelMapper = modelMapper;
        this.adminService = adminService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add/user/{id}/{role}")
    public ResponseEntity<UserAdminInfoResponseModel> addRoleToUser(@PathVariable long id, @PathVariable String role) {
        return ResponseEntity.ok(this.modelMapper.map(this.adminService.addRoleToUser(id, role).orElseThrow(), UserAdminInfoResponseModel.class));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/remove/user/{id}/{role}")
    public ResponseEntity<UserAdminInfoResponseModel> removeRoleFromUser(@PathVariable long id, @PathVariable String role, Principal principal) {
        return ResponseEntity
                .ok(this.modelMapper.map(this.adminService.removeRoleFromUser(id, role, principal.getName()).orElseThrow(), UserAdminInfoResponseModel.class));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/block/user/{id}")
    public ResponseEntity<UserAdminInfoResponseModel> blockUser(@PathVariable long id, Principal principal) {
        return ResponseEntity.ok(this.modelMapper.map(this.adminService.blockUser(id, principal.getName()).orElseThrow(), UserAdminInfoResponseModel.class));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/unblock/user/{id}")
    public ResponseEntity<UserAdminInfoResponseModel> unBlockUser(@PathVariable long id, Principal principal) {
        return ResponseEntity.ok(this.modelMapper.map(this.adminService.unBlockUser(id, principal.getName()).orElseThrow(), UserAdminInfoResponseModel.class));
    }

}
