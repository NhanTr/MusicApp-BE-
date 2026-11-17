package nhantr.musicapp.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nhantr.musicapp.dto.request.UserUpdateRequest;
import nhantr.musicapp.dto.response.UserResponse;
import nhantr.musicapp.entity.User;
import nhantr.musicapp.enums.ErrorCode;
import nhantr.musicapp.enums.UserStatus;
import nhantr.musicapp.exception.AppException;
import nhantr.musicapp.mapper.UserMapper;
import nhantr.musicapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;
    final UserMapper userMapper;
    final CurrentUserService currentUserService;
    final PasswordEncoder passwordEncoder;


    @Override
    public UserResponse updateMe(UserUpdateRequest request) {
        User user = currentUserService.getCurrentUserEntity();
        log.info("Update current user id={}", user.getId());
        return userMapper.toResponse(applyUpdate(user, request));
    }

    @Override
    public UserResponse updateUser(UUID id, UserUpdateRequest request) {
        log.info("Admin update user id={}", id);
        User user = getUserEntity(id);
        return userMapper.toResponse(applyUpdate(user, request));
    }

    @Override
    public UserResponse updateStatus(UUID id, String status) {
        log.info("Update status userId={}, status={}", id, status);
        User user = getUserEntity(id);

        UserStatus userStatus;
        try {
            userStatus = UserStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_REQUEST.getCode(), "Invalid status: " + status);
        }

        user.setStatus(userStatus);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(UUID id) {
        log.info("Delete user id={}", id);
        userRepository.delete(getUserEntity(id));
    }

    // ── private helpers ───────────────────────────────────────────────────────

    private User getUserEntity(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(
                        ErrorCode.USER_NOT_FOUND.getCode(),
                        ErrorCode.USER_NOT_FOUND.getMessage()));
    }

    private User applyUpdate(User user, UserUpdateRequest request) {
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            // kiểm tra username chưa bị dùng bởi user khác
            userRepository.findByUsername(request.getUsername())
                    .filter(existing -> !existing.getId().equals(user.getId()))
                    .ifPresent(__ -> {
                        throw new AppException(
                                ErrorCode.USER_EXISTED.getCode(),
                                "Username already taken");
                    });
            user.setUsername(request.getUsername());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return userRepository.save(user);
    }
}
