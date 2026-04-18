package com.jenkins.saas.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

/**
 * TenantHibernateFilter- Active automatiquement le filtre Hibernate
 * avant chaque appel au repositories spring Data JPA
 * <p>
 * FONCTIONNEMENT:
 * 1- TenantFilter(Http) a deja stocké le tenantId sans TenantContext
 * 2- cet aspect intercepte tout appel à un Repository
 * 3- il active le filtre Hibernate "tenantFilter" avec le tenantId courant
 * 4- Hibernate ajoute automatiquement WHERE tenant_id = tenatId
 * <p>
 * POURQUOI UN ASPECT?
 * sans cet aspect, il faudrait activer le filtre manuellement dans chaque
 * méthode de service. l'aspect le fait automatiquement et de manière transversale.
 * <p>
 * ALTERNATIVE:
 * on ourrait aussi utiliser un HandlerIntercepter ou un @EventListener.
 * L'aspect est plus propre car il s'execute au plus proche de la couche données
 */


@Aspect
@Component
public class TenantHibernateFilter {
    @PersistenceContext
    private EntityManager entityManager;

    @Before("execution(* com.jenkins.saas.service.*.*(..))")
    public void activateTenantFilter() {
        final String tenantId = TenantContext.getCurrentTenant();
        if (tenantId != null) {
            final Session session = this.entityManager.unwrap(Session.class);
            //active le filter et injecte le parametre tenantId
            session.enableFilter("tenantFilter")
                    .setParameter("tenantId", tenantId);

        }
    }
}
