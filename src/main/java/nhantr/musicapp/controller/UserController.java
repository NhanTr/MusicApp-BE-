package nhantr.musicapp.controller;

import jakarta.validation.Valid;
import java.util.UUID;
import nhantr.musicapp.dto.request.UserUpdateRequest;
import nhantr.musicapp.dto.response.APIResponse;
import nhantr.musicapp.dto.response.UserResponse;
import nhantr.musicapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }



    // Cập nhật thông tin user đang đăng nhập
    @PutMapping("/me")
    public ResponseEntity<APIResponse<UserResponse>> updateMe(
            @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(APIResponse.success(userService.updateMe(request)));
    }
    // Admin cập nhật thông tin bất kỳ user
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<UserResponse>> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(APIResponse.success(userService.updateUser(id, request)));
    }

    // Admin đổi status user (ACTIVE / BANNED)
    @PatchMapping("/{id}/status")
    public ResponseEntity<APIResponse<UserResponse>> updateStatus(
            @PathVariable UUID id,
            @RequestParam String status) {
        return ResponseEntity.ok(APIResponse.success(userService.updateStatus(id, status)));
    }

    // Xóa user (admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(APIResponse.success("User deleted"));
    }
}
