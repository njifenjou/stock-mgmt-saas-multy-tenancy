package com.jenkins.saas.repositories;

import com.jenkins.saas.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, String> {
    boolean existsByCompanyCode(String companyCode);
    boolean existsByEmail(String email);

}
