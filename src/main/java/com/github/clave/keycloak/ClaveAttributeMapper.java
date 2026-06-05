package com.github.clave.keycloak;

import org.keycloak.broker.saml.mappers.UserAttributeMapper;

/**
 * Custom attribute mapper for Cl@ve.
 * Extends the default SAML UserAttributeMapper to provide specific support for Cl@ve/eIDAS attributes.
 */
public class ClaveAttributeMapper extends UserAttributeMapper {

    public static final String PROVIDER_ID = "clave-user-attribute-mapper";
    private static final String[] COMPATIBLE_PROVIDERS = {ClaveIdentityProviderFactory.PROVIDER_ID};

    @Override
    public String[] getCompatibleProviders() {
        return COMPATIBLE_PROVIDERS;
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getHelpText() {
        return "Import specific Cl@ve/eIDAS attributes into user attributes.";
    }

    @Override
    public String getDisplayCategory() {
        return "Attribute Importer";
    }

    @Override
    public String getDisplayType() {
        return "Cl@ve User Attribute";
    }
}
