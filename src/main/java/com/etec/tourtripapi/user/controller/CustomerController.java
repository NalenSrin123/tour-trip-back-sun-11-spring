package com.etec.tourtripapi.user.controller;

import com.etec.tourtripapi.common.response.ApiResponse;
import com.etec.tourtripapi.user.dto.request.UpdateCustomerRequest;
import com.etec.tourtripapi.user.dto.request.UpdateStatusRequest;
import com.etec.tourtripapi.user.dto.response.CustomerResponse;
import com.etec.tourtripapi.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final UserService userService;

    /**
     * GET /api/v1/customers/{id}
     * Retrieves customer account details by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(@PathVariable Long id) {
        CustomerResponse customer = userService.getCustomerById(id);
        ApiResponse<CustomerResponse> response = ApiResponse.success(
                "Customer retrieved successfully",
                customer
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/customers
     * Searches or lists customer accounts filtered by name, email, or status.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> searchCustomers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String status) {

        List<CustomerResponse> customers = userService.searchCustomers(name, email, status);
        ApiResponse<List<CustomerResponse>> response = ApiResponse.success(
                "Customers retrieved successfully",
                customers
        );
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/v1/customers/{id}
     * Updates customer profile information (full name, bio, avatar).
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerRequest request) {

        CustomerResponse updatedCustomer = userService.updateCustomer(id, request);
        ApiResponse<CustomerResponse> response = ApiResponse.success(
                "Customer updated successfully",
                updatedCustomer
        );
        return ResponseEntity.ok(response);
    }

    /**
     * PATCH /api/v1/customers/{id}/activate
     * Directly activates a customer account.
     */
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<CustomerResponse>> activateCustomer(@PathVariable Long id) {
        CustomerResponse activatedCustomer = userService.activateCustomer(id);
        ApiResponse<CustomerResponse> response = ApiResponse.success(
                "Customer account activated successfully",
                activatedCustomer
        );
        return ResponseEntity.ok(response);
    }

    /**
     * PATCH /api/v1/customers/{id}/deactivate
     * Directly deactivates a customer account.
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<CustomerResponse>> deactivateCustomer(@PathVariable Long id) {
        CustomerResponse deactivatedCustomer = userService.deactivateCustomer(id);
        ApiResponse<CustomerResponse> response = ApiResponse.success(
                "Customer account deactivated successfully",
                deactivatedCustomer
        );
        return ResponseEntity.ok(response);
    }

    /**
     * PATCH /api/v1/customers/{id}/status
     * Updates customer status to a specific value (ACTIVE, INACTIVE, SUSPENDED).
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomerStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request) {

        CustomerResponse updatedCustomer = userService.updateCustomerStatus(id, request.getStatus());
        ApiResponse<CustomerResponse> response = ApiResponse.success(
                "Customer status updated successfully",
                updatedCustomer
        );
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/v1/customers/{id}
     * Deletes a customer account by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id) {
        userService.deleteCustomer(id);
        ApiResponse<Void> response = ApiResponse.success("Customer deleted successfully");
        return ResponseEntity.ok(response);
    }
}
