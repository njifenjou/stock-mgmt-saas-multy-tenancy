package com.jenkins.saas.service;

import com.jenkins.saas.common.PageResponse;
import com.jenkins.saas.entity.Tenant;
import com.jenkins.saas.requests.RegisterTenantRequest;
import com.jenkins.saas.responses.TenantResponse;

public interface TenantService {

    void registerTenant(final RegisterTenantRequest request);

    void approveTenant(final String tenantId);

    void activateTenant(final String tenantId);

    void deactivateTenant(final String tenantId);

    void suspendTenant(final String tenantId);

    PageResponse<TenantResponse> findAll(final int page, final int size);

}
