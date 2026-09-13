package com.etec.tourtripapi.user.service;

import com.etec.tourtripapi.user.dto.request.CreateUserRequest;
import com.etec.tourtripapi.user.dto.request.UpdateAdminUserRequest;
import com.etec.tourtripapi.user.dto.request.UpdateCustomerRequest;
import com.etec.tourtripapi.user.dto.response.CustomerResponse;
import com.etec.tourtripapi.user.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    // Administrative user operations
    UserResponse createAdminUser(CreateUserRequest request);

    UserResponse getUserById(Long id);

    List<UserResponse> searchAdminUsers(String name, String email, String status, String role);

    UserResponse updateAdminUser(Long id, UpdateAdminUserRequest request);

    List<UserResponse> getUsersByRole(String role);

    // Customer management operations
    CustomerResponse getCustomerById(Long id);

    List<CustomerResponse> searchCustomers(String name, String email, String status);

    CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request);

    CustomerResponse activateCustomer(Long id);

    CustomerResponse deactivateCustomer(Long id);

    CustomerResponse updateCustomerStatus(Long id, String status);

    void deleteCustomer(Long id);
}
