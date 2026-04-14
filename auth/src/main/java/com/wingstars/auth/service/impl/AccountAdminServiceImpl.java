package com.wingstars.auth.service.impl;

import com.wingstars.auth.dto.request.AccountCreateRequest;
import com.wingstars.auth.dto.request.AccountUpdateRequest;
import com.wingstars.auth.dto.response.UserResponse;
import com.wingstars.auth.entity.Role;
import com.wingstars.auth.entity.User;
import com.wingstars.auth.repository.RefreshTokenRepository;
import com.wingstars.auth.repository.RoleRepository;
import com.wingstars.auth.repository.UserRepository;
import com.wingstars.auth.service.AccountAdminService;
import com.wingstars.core.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountAdminServiceImpl implements AccountAdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllAccounts(Pageable pageable, Boolean includeDeleted) {
        Page<User> users;
        if (Boolean.TRUE.equals(includeDeleted)) {
            users = userRepository.findAll(pageable);
        } else {
            users = userRepository.findByDeletedFalse(pageable);
        }
        return users.map(this::convertToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getAccountById(Long id) {
        User user = userRepository.findByIdWithRole(id)
                .orElseThrow(() -> new BusinessException("User not found"));
        return convertToResponse(user);
    }

    @Override
    @Transactional
    public UserResponse createAccount(AccountCreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        Role role = roleRepository.findByCode(request.getRoleCode())
                .orElseThrow(() -> new BusinessException("Role not found"));

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .gender(request.getGender())
                .address(request.getAddress())
                .role(role)
                .deleted(false)
                .build();

        return convertToResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse updateAccount(Long id, AccountUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found"));

        if (!user.getEmail().equalsIgnoreCase(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        Role role = roleRepository.findByCode(request.getRoleCode())
                .orElseThrow(() -> new BusinessException("Role not found"));

        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setGender(request.getGender());
        user.setAddress(request.getAddress());
        user.setRole(role);
        
        if (request.getDeleted() != null) {
            if (Boolean.TRUE.equals(request.getDeleted()) && !Boolean.TRUE.equals(user.getDeleted())) {
                // Lock account
                user.setDeleted(true);
                refreshTokenRepository.revokeAllByUser(user);
            } else if (Boolean.FALSE.equals(request.getDeleted()) && Boolean.TRUE.equals(user.getDeleted())) {
                // Unlock account
                user.setDeleted(false);
            }
        }

        return convertToResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void lockAccount(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found"));
        user.setDeleted(true);
        userRepository.save(user);
        refreshTokenRepository.revokeAllByUser(user);
    }

    @Override
    @Transactional
    public void unlockAccount(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found"));
        user.setDeleted(false);
        userRepository.save(user);
    }

    private UserResponse convertToResponse(User user) {
        UserResponse response = modelMapper.map(user, UserResponse.class);
        if (user.getRole() != null) {
            response.setRoleName(user.getRole().getName());
            response.setRoleCode(user.getRole().getCode());
        }
        return response;
    }
}
