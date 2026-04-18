package com.jenkins.saas.config;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Tenant - intercepte chaque requete HTTP pour identifier le tenant
 * ce filtre est le point d'entree du mecanuisme multi-tenant
 * il s'execute avant tous les controlers et services
 * Stratégie d'identification du tenant (par ordre de priorité)
 * 1- Header "X-Tenant-ID"
 * 2- (ptionnel) Sous-domaine : alpha.stockapp.com --"alpha"
 *
 * si aucun tenant n'est identifier , on envoi un 404 BAD REQUEST
 *
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)

public class TenantFilter implements Filter {

    private static final String TENANT_HEADER = "X-Tenant-ID";

    @Override
    public void doFilter
            (ServletRequest servletRequest,
             ServletResponse servletResponse,
             FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        final String tenantId = resolveTenant(request);
        if (tenantId == null || tenantId.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"error\":\"Invalid tenant ID, please add the header X-Tenant-ID attribute to the request parameter\"}"
            );
            //filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        try {
//stocker le tenant dans le ThreadLocal
            TenantContext.setCurrentTenant(tenantId);

            //continuer la chaine de filtre -- controller-- sevice
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            //CRITIQUE : toujours nettoyer le threadlocal après la requte
            //sans ce clear() le tenant pourrait "fuiter" vers la requete suivannte
            //si le thread est reutilliseé par le pool de threads du serveur
            TenantContext.clearCurrentTenant();
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }

    private String resolveTenant(final HttpServletRequest request) {
        final String tenantId = request.getHeader(TENANT_HEADER);
        if (tenantId != null && !tenantId.isBlank()) {
            return tenantId.trim().toLowerCase();
        }
        return null;
    }
}
