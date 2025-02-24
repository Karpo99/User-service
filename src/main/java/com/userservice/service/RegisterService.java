package com.userservice.service;

import com.userservice.model.dto.request.UserRegisterRequest;

public interface RegisterService {
    void registerUser(final UserRegisterRequest request);
}
