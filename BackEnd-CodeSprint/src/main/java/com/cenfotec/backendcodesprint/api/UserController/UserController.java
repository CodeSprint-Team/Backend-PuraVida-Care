package com.cenfotec.backendcodesprint.api.UserController;

import com.cenfotec.backendcodesprint.logic.User.DTO.Request.RegisterUserRequestDto;
import com.cenfotec.backendcodesprint.logic.User.DTO.Response.UserResponseDto;
import com.cenfotec.backendcodesprint.logic.User.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody RegisterUserRequestDto requestDto) {
        UserResponseDto responseDto = userService.registerUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PatchMapping("/{userId}/role")
    public ResponseEntity<UserResponseDto> updateUserRole(
            @PathVariable Long userId,
            @RequestBody Map<String, Long> body) {
        Long roleId = body.get("roleId");
        if (roleId == null) {
            return ResponseEntity.badRequest().build();
        }
        UserResponseDto response = userService.updateUserRole(userId, roleId);
        return ResponseEntity.ok(response);
    }
}