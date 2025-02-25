package com.userservice.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequest(
        @NotBlank
        String refreshToken
) {
}
