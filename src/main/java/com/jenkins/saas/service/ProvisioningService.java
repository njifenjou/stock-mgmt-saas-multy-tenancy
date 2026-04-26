package com.jenkins.saas.service;

import com.jenkins.saas.entity.Tenant;

public interface ProvisioningService {

    void provisionTenant(final Tenant tenant);
}
