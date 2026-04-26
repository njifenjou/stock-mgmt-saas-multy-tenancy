package com.jenkins.saas.security;

import com.jenkins.saas.config.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().contains("/api/v1/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            final String jwt = JwtFromRequest(request);

            if (StringUtils.hasText(jwt) && this.jwtService.validateToken(jwt))
            {
                final String userId = this.jwtService.getUserIdFromToken(jwt);
                final String tenantId = this.jwtService.getTenantIdFromToken(jwt);
                final String role = this.jwtService.getRoleFromToken(jwt);

                if (tenantId != null) {
                    TenantContext.setCurrentTenant(tenantId);
                    final String schemaName = "Not_yet_defined";
                    TenantContext.setCurrentSchema(schemaName);

                }

                //create authentication token

                final SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
                final UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId,
                                null,
                                Collections.singleton(authority));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Authenticated user: {}, tenant: {}, role: {}", userId, tenantId, role);
            }

        } catch (final Exception e) {
            log.error("Error authenticating", e);
        }
        filterChain.doFilter(request, response);
        TenantContext.clearCurrentTenant();


    }

    private String JwtFromRequest(final HttpServletRequest request) {
        final String authorization = request.getHeader("Authorization");
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }
}
