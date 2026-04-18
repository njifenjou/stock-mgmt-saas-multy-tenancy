package com.jenkins.saas.config;

/**
 * TenantContext- stocke l'identifiant du tenant courant dans un ThreadLocal.
 * chaque requete HTTP est traité par un thread dedie.
 * Le ThreadLocal garantit que le tenant_id est isolé par le thread,
 * même en cas de requete simultanée de tenants differents
 * <p>
 * Flux:
 * 1. TenantFilter extrait le tenant_id de la requete HTTP
 * 2. TenantFilter appelle TenantContext.setCurrentTenant(tenant_id)
 * 3. le code metier (service, repositories) accede au tenant via TenantContext.getCurrentTenant
 * 4. TenantFilter appelle TenantContext.clear() après la reponse (nettoyage)
 **/
public class TenantContext {


    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    /**
     * definit l'identifiant du tenant pour le thread  courant
     **/
    public static void setCurrentTenant(final String tenant) {
        CURRENT_TENANT.set(tenant);
    }

    /**
     * Recupère l'identifiant du tenant pour le thread  courant
     **/
    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    /**
     * Nettoie l'identifiant du tenant pour le thread  courant
     **/

    public static void clearCurrentTenant() {
        CURRENT_TENANT.remove();
    }
}
