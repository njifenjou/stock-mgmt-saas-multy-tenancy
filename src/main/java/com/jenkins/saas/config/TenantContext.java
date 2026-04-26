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
    private static final ThreadLocal<String> CURRENT_SCHEMA = new ThreadLocal<>();

    /**
     * definit l'identifiant du tenant pour le thread  courant
     **/
    public static void setCurrentTenant(final String tenant) {
        CURRENT_TENANT.set(tenant);
    }
    public static void setCurrentSchema(final String schema) {
        CURRENT_SCHEMA.set(schema);
    }

    /**
     * Recupère l'identifiant du tenant pour le thread  courant
     **/
    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }
    public static String getCurrentSchema() {
        return CURRENT_SCHEMA.get();
    }

    /**
     * Nettoie l'identifiant du tenant pour le thread  courant
     **/

    public static void clearCurrentTenant() {

        CURRENT_TENANT.remove();
        CURRENT_SCHEMA.remove();
    }
}
