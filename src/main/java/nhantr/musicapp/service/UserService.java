package nhantr.musicapp.service;

import java.util.UUID;
import nhantr.musicapp.dto.request.UserUpdateRequest;
import nhantr.musicapp.dto.response.UserResponse;

public interface UserService {

    UserResponse updateMe(UserUpdateRequest request);

    UserResponse updateUser(UUID id, UserUpdateRequest request);

    UserResponse updateStatus(UUID id, String status);

    void deleteUser(UUID id);
}
