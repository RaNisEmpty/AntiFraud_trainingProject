package antifraud.presentation;

import antifraud.business.user.User;
import antifraud.business.user.UserService;
import antifraud.business.user.userdto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/api/auth/user")
    public ResponseEntity<UserResponse> postUser(@Validated @RequestBody UserRequest userRequest) {


        Optional<User> user = userService.save(userRequest); //--id--(generated) User(name, username, password, role(USER))
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        UserResponse userResponse = new UserResponse(user.get().getId(), user.get().getName(), user.get().getUsername(), user.get().getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PutMapping("/api/auth/role")
    public ResponseEntity<UserResponse> putRole(@Valid @RequestBody UserRoleRequest userRoleRequest) {

        Optional<User> user = userService.findUserByUsername(userRoleRequest.getUsername());

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!(userRoleRequest.getRole().equals("MERCHANT") || userRoleRequest.getRole().equals("SUPPORT"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (userRoleRequest.getRole().equals("ADMINISTRATOR")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (userRoleRequest.getRole().equals(user.get().getRole())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        user.get().setRole(userRoleRequest.getRole());

        userService.setRole(user.get());

        UserResponse userResponse = new UserResponse(user.get().getId(), user.get().getName(), user.get().getUsername(), user.get().getRole());
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/api/auth/access")
    public ResponseEntity<?> putLock(@Valid @RequestBody UserStatusRequest userStatusRequest) {

        Optional<User> user = userService.findUserByUsername(userStatusRequest.getUsername());

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!(userStatusRequest.getOperation().equals("LOCK") || userStatusRequest.getOperation().equals("UNLOCK"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (user.get().getRole().equals("ADMINISTRATOR")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        user.get().setStatus(userStatusRequest.getOperation() + "ED");

        userService.setLock(user.get());

        return ResponseEntity.ok(Map.of("status","User " + user.get().getUsername() + " " + userStatusRequest.getOperation().toLowerCase() + "ed!"));
    }

    @GetMapping("/api/auth/list")
    public List<UserResponse> getList() {
        return userService.findAllByOrderByIdAsc();
    }

    @DeleteMapping("/api/auth/user/{username}")
    public ResponseEntity<?> deletePost(@PathVariable String username) {

        boolean isDeleted = userService.deleteUser(username);

        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }



        return ResponseEntity.ok(new UserDeleteResponse(username, "Deleted successfully!"));
    }
}