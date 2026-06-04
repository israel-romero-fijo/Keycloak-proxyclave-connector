package com.github.clave.keycloak;

import org.keycloak.broker.saml.mappers.UserAttributeMapper;

/**
 * Custom Attribute Mapper for Cl@ve.
 * Facilitates mapping typical Cl@ve/eIDAS attributes.
 */
public class ClaveAttributeMapper extends UserAttributeMapper {

    public static final String[] COMPATIBLE_PROVIDERS = {ClaveIdentityProviderFactory.PROVIDER_ID};

    @Override
    public String[] getCompatibleProviders() {
        return COMPATIBLE_PROVIDERS;
    }

    @Override
    public String getId() {
        return "clave-user-attribute-mapper";
    }

    @Override
    public String getDisplayCategory() {
        return "Attribute Importer";
    }

    @Override
    public String getDisplayType() {
        return "Cl@ve Attribute Importer";
    }

    @Override
    public String getHelpText() {
        return "Import attributes from Cl@ve SAML assertions into user attributes or properties.";
    }
}
