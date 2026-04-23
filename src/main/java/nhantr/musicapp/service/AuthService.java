package nhantr.musicapp.service;

import nhantr.musicapp.dto.request.LoginRequest;
import nhantr.musicapp.dto.request.RegisterRequest;
import nhantr.musicapp.dto.response.LoginResponse;
import nhantr.musicapp.dto.response.UserResponse;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    void logout(String token);

    LoginResponse refreshToken(String token);

    UserResponse getCurrentUser();
}
