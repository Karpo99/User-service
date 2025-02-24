package com.userservice.service.impl;

import com.userservice.exception.UserAlreadyExistException;
import com.userservice.model.dto.request.UserRegisterRequest;
import com.userservice.model.entity.UserEntity;
import com.userservice.model.enums.UserType;
import com.userservice.repository.UserRepository;
import com.userservice.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {
    @Value("${admin.email}")
    private String adminEmail;
    private static final String EXCEPTION_MESSAGE = "The email are already in use!";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(UserRegisterRequest request) {
        isEmailTaken(request.getEmail());

        UserEntity userEntity = UserEntity.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .userType(isAdminEmail(request.getEmail()))
                .build();

        userRepository.save(userEntity);

    }

    private void isEmailTaken(final String email) {
        if (userRepository.existsUserEntityByEmail(email)) {
            throw new UserAlreadyExistException(EXCEPTION_MESSAGE);
        }
    }

    private UserType isAdminEmail(final String email) {
        if (email.equals(adminEmail)) {
            return UserType.ADMIN;
        }
        return UserType.USER;
    }
}
