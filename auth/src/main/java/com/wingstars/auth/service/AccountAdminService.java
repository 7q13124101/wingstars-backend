package com.wingstars.auth.service;

import com.wingstars.auth.dto.request.AccountCreateRequest;
import com.wingstars.auth.dto.request.AccountUpdateRequest;
import com.wingstars.auth.dto.response.UserResponse;
import com.wingstars.core.payload.PageResponse;
import org.springframework.data.domain.Pageable;

public interface AccountAdminService {
    PageResponse<UserResponse> getAllAccounts(Pageable pageable, Boolean includeDeleted);
    UserResponse getAccountById(Long id);
    UserResponse createAccount(AccountCreateRequest request);
    UserResponse updateAccount(Long id, AccountUpdateRequest request);
    void lockAccount(Long id);
    void unlockAccount(Long id);
}
