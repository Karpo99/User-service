package com.userservice.service.impl;

import com.userservice.model.dto.request.TokenInvalidateRequest;
import com.userservice.service.InvalidTokenService;
import com.userservice.service.TokenService;
import com.userservice.service.UserLogoutService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class UserLogoutServiceImpl implements UserLogoutService {
    private final TokenService tokenService;
    private final InvalidTokenService invalidTokenService;

    @Override
    public void logout(TokenInvalidateRequest request) {
        tokenService.verifyAndValidate(
                Set.of(
                        request.accessToken(),
                        request.refreshToken()
                )
        );

        final String accessTokenId = tokenService
                .getPayload(request.accessToken())
                .getId();

        invalidTokenService.checkForInvalidityOfToken(accessTokenId);

        final String refreshTokenId = tokenService
                .getPayload(request.refreshToken())
                .getId();

        invalidTokenService.checkForInvalidityOfToken(refreshTokenId);

        invalidTokenService.invalidateTokens(Set.of(accessTokenId, refreshTokenId));
    }
}
