package com.github.clave.keycloak;

import org.keycloak.broker.saml.mappers.UserAttributeMapper;

public class ClaveAttributeMapper extends UserAttributeMapper {

    public static final String PROVIDER_ID = "clave-user-attribute-mapper";

    @Override
    public String[] getCompatibleProviders() {
        return new String[]{ClaveIdentityProviderFactory.PROVIDER_ID};
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getDisplayCategory() {
        return "Attribute Importer";
    }

    @Override
    public String getDisplayType() {
        return "Cl@ve User Attribute Mapper";
    }

    @Override
    public String getHelpText() {
        return "Import specific Cl@ve/eIDAS attributes from the SAML assertion.";
    }
}
