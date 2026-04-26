package com.jenkins.saas.service.impl;

import com.jenkins.saas.common.PageResponse;
import com.jenkins.saas.entity.Tenant;
import com.jenkins.saas.entity.TenantStatus;
import com.jenkins.saas.entity.User;
import com.jenkins.saas.entity.UserRole;
import com.jenkins.saas.exceptions.DuplicateResourceException;
import com.jenkins.saas.exceptions.InvalidRequestException;
import com.jenkins.saas.mappers.TenantMapper;
import com.jenkins.saas.repositories.TenantRepository;
import com.jenkins.saas.repositories.UserRepository;
import com.jenkins.saas.requests.RegisterTenantRequest;
import com.jenkins.saas.responses.TenantResponse;
import com.jenkins.saas.service.ProvisioningService;
import com.jenkins.saas.service.TenantService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TenantServiceImpl implements TenantService {
    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ProvisioningService provisioningService;
    ;

    @Override
    public void registerTenant(final RegisterTenantRequest request) {
        if (this.tenantRepository.existsByCompanyCode(request.getCompanyCode())) {
            throw new DuplicateResourceException("Tenant already exists");
        }

        if (this.tenantRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("TenantEmail already exists");
        }
        final Tenant tenant = this.tenantMapper.toEntity(request);
        tenant.setAdminPassword(this.passwordEncoder.encode(request.getAdminPassword()));
        tenant.setStatus(TenantStatus.PENDING);
        this.tenantRepository.save(tenant);

    }

    @Override
    public void approveTenant(final String tenantId) {
        //activate tenant
        final Tenant tenant = this.tenantRepository.findById(tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Tenant not found"));
        tenant.setStatus(TenantStatus.ACTIVE);
        this.tenantRepository.save(tenant);

        try {
            //provision the schema for the tenant

        this.provisioningService.provisionTenant(tenant);
            //create initial admin user
            createInitialAdminUser(tenant);

        } catch (final Exception e) {

            rollBackTenantStatus(tenant);


        }


    }


    @Override
    public void activateTenant(final String tenantId) {
        final Tenant tenant = this.tenantRepository.findById(tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Tenant does not exist"));
        if (tenant.getStatus() != TenantStatus.PENDING) {
            throw new InvalidRequestException("Tenant status is not PENDING");
        }
        tenant.setStatus(TenantStatus.ACTIVE);
        this.tenantRepository.save(tenant);
    }

    @Override
    public void deactivateTenant(final String tenantId) {
        final Tenant tenant = this.tenantRepository.findById(tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Tenant does not exist"));
        if (tenant.getStatus() != TenantStatus.ACTIVE) {
            throw new InvalidRequestException("Tenant status is not PENDING");
        }
        tenant.setStatus(TenantStatus.INACTIVE);
        this.tenantRepository.save(tenant);
    }


    @Override
    public void suspendTenant(String tenantId) {
        final Tenant tenant = this.tenantRepository.findById(tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Tenant does not exist"));
        if (tenant.getStatus() != TenantStatus.ACTIVE) {
            throw new InvalidRequestException("Tenant status is not PENDING");
        }
        tenant.setStatus(TenantStatus.SUSPENDED);
        this.tenantRepository.save(tenant);
    }


    @Override
    public PageResponse<TenantResponse> findAll(final int page, final int size) {
        final PageRequest pageRequest = PageRequest.of(page, size);
        final Page<Tenant> tenants = this.tenantRepository.findAll(pageRequest);
        final Page<TenantResponse> tenantResponse = tenants.map(this.tenantMapper::toResponse);
        return PageResponse.of(tenantResponse);
    }


    private void rollBackTenantStatus(final Tenant tenant) {
        tenant.setStatus(TenantStatus.PENDING);
        this.tenantRepository.save(tenant);
    }


    private void createInitialAdminUser(final Tenant tenant) {
        if (this.userRepository.existsByUsername(tenant.getAdminUsername())) {
            throw new DuplicateResourceException("Admin user already exists");
        }
        final User adminUser = User.builder()
                .username(tenant.getAdminUsername())
                .email(tenant.getAdminEmail())
                .firstName(extractFirstName(tenant.getAdminFullName()))
                .lastName(extractLastName(tenant.getAdminFullName()))
                .password(passwordEncoder.encode(tenant.getAdminPassword()))
                .role(UserRole.ROLE_COMPANY_ADMIN)
                .tenant(tenant)
                .enabled(true)
                .build();

        this.userRepository.save(adminUser);
        log.info("Created initial admin user for tenant: {}", tenant.getId());
    }

    private String extractFirstName(final String fullName) {
        return fullName.split(" ")[0];

    }

    private String extractLastName(final String fullName) {
        return fullName.split(" ")[1].length() > 0 ? fullName.split(" ")[1] : fullName;

    }
}
