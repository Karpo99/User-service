package com.userservice.service.impl;

import com.userservice.exception.UserNotFoundException;
import com.userservice.exception.UserStatusNotValidException;
import com.userservice.model.Token;
import com.userservice.model.dto.request.TokenRefreshRequest;
import com.userservice.model.entity.UserEntity;
import com.userservice.model.enums.TokenClaims;
import com.userservice.model.enums.UserStatus;
import com.userservice.repository.UserRepository;
import com.userservice.service.RefreshTokenService;
import com.userservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    public Token refreshToken(TokenRefreshRequest request) {
        tokenService.verifyAndValidate(request.refreshToken());

        final String userId = tokenService
                .getPayload(request.refreshToken())
                .get(TokenClaims.USER_ID.getValue())
                .toString();

        final UserEntity userEntityFromDb = userRepository
                .findById(userId)
                .orElseThrow(UserNotFoundException::new);

        validateUserStatus(userEntityFromDb);

        return tokenService
                .generateToken(userEntityFromDb.getUserClaims(), request.refreshToken());
    }

    private void validateUserStatus(UserEntity userEntityFromDb) {
        if (!(UserStatus.ACTIVE.equals(userEntityFromDb.getUserStatus()))) {
            throw new UserStatusNotValidException("User status: " + userEntityFromDb.getUserStatus());
        }
    }
}
