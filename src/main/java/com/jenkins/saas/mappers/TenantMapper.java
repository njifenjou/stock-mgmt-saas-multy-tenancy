package com.jenkins.saas.mappers;

import com.jenkins.saas.entity.Tenant;
import com.jenkins.saas.requests.RegisterTenantRequest;
import com.jenkins.saas.responses.TenantResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TenantMapper {

    public Tenant toEntity(final RegisterTenantRequest request){
        return Tenant.builder()
                .companyName(request.getCompanyName())
                .companyCode(request.getCompanyCode())
                .createdAt(LocalDateTime.now())
                .email(request.getEmail())
                .adminFullName(request.getAdminFullName())
                .adminEmail(request.getAdminEmail())
                .adminUsername(request.getAdminUsername())
                .build();
    }

    public TenantResponse toResponse(final Tenant tenant){
        return TenantResponse.builder()
                .companyName(tenant.getCompanyName())
                .companyCode(tenant.getCompanyCode())
                .createdAt(tenant.getCreatedAt())
                .email(tenant.getEmail())
                .adminFullName(tenant.getAdminFullName())
                .adminEmail(tenant.getAdminEmail())
                .adminUsername(tenant.getAdminUsername())
                .build();
    }
}
