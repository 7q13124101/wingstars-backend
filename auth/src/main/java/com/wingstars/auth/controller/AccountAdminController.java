package com.wingstars.auth.controller;

import com.wingstars.auth.dto.request.AccountCreateRequest;
import com.wingstars.auth.dto.request.AccountUpdateRequest;
import com.wingstars.auth.dto.response.UserResponse;
import com.wingstars.auth.service.AccountAdminService;
import com.wingstars.core.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin/accounts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AccountAdminController {

    private final AccountAdminService accountAdminService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "true") Boolean includeDeleted) {
        
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<UserResponse> data = accountAdminService.getAllAccounts(pageable, includeDeleted);
        
        ApiResponse<Page<UserResponse>> response = ApiResponse.<Page<UserResponse>>builder()
                .status(200)
                .message("Accounts retrieved successfully")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getAccountById(@PathVariable Long id) {
        UserResponse data = accountAdminService.getAccountById(id);
        
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .status(200)
                .message("Account retrieved successfully")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createAccount(@RequestBody @Valid AccountCreateRequest request) {
        UserResponse data = accountAdminService.createAccount(request);
        
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .status(201)
                .message("Account created successfully")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateAccount(@PathVariable Long id, @RequestBody @Valid AccountUpdateRequest request) {
        UserResponse data = accountAdminService.updateAccount(id, request);
        
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .status(200)
                .message("Account updated successfully")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/lock")
    public ResponseEntity<ApiResponse<Void>> lockAccount(@PathVariable Long id) {
        accountAdminService.lockAccount(id);
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(200)
                .message("Account locked successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/unlock")
    public ResponseEntity<ApiResponse<Void>> unlockAccount(@PathVariable Long id) {
        accountAdminService.unlockAccount(id);
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(200)
                .message("Account unlocked successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.ok(response);
    }
}
