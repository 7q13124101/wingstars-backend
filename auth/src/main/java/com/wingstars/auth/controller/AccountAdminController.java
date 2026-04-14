package com.wingstars.auth.controller;

import com.wingstars.auth.dto.request.AccountCreateRequest;
import com.wingstars.auth.dto.request.AccountUpdateRequest;
import com.wingstars.auth.dto.response.UserResponse;
import com.wingstars.auth.service.AccountAdminService;
import com.wingstars.core.payload.ApiResponse;
import com.wingstars.core.payload.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/accounts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
@Tag(name = "Admin Account Management", description = "Management APIs for user accounts (Super Admin only)")
public class AccountAdminController {

    private final AccountAdminService accountAdminService;

    @GetMapping
    @Operation(summary = "Get all accounts", description = "Retrieve a paginated list of all user accounts.")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @Parameter(description = "Include locked/soft-deleted accounts")
            @RequestParam(defaultValue = "true") Boolean includeDeleted) {
        
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(ApiResponse.success(accountAdminService.getAllAccounts(pageable, includeDeleted)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account detail", description = "Retrieve full information of a specific user account by ID.")
    public ResponseEntity<ApiResponse<UserResponse>> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(accountAdminService.getAccountById(id)));
    }

    @PostMapping
    @Operation(summary = "Create internal account", description = "Create a new internal account with specific role (ADMIN, SUPER_ADMIN, etc.)")
    public ResponseEntity<ApiResponse<UserResponse>> createAccount(@RequestBody @Valid AccountCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(accountAdminService.createAccount(request)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update account", description = "Update existing user information and role.")
    public ResponseEntity<ApiResponse<UserResponse>> updateAccount(@PathVariable Long id, @RequestBody @Valid AccountUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(accountAdminService.updateAccount(id, request)));
    }

    @DeleteMapping("/{id}/lock")
    @Operation(summary = "Lock account", description = "Soft delete/lock a user account and revoke all their refresh tokens.")
    public ResponseEntity<ApiResponse<Void>> lockAccount(@PathVariable Long id) {
        accountAdminService.lockAccount(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{id}/unlock")
    @Operation(summary = "Unlock account", description = "Restore a locked user account.")
    public ResponseEntity<ApiResponse<Void>> unlockAccount(@PathVariable Long id) {
        accountAdminService.unlockAccount(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
