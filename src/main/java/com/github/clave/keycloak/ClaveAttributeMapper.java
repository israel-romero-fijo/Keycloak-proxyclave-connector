package com.github.clave.keycloak;

import org.keycloak.broker.saml.mappers.UserAttributeMapper;

/**
 * Custom attribute mapper for Cl@ve.
 * Extends the default SAML UserAttributeMapper to provide specific support for Cl@ve/eIDAS attributes.
 */
public class ClaveAttributeMapper extends UserAttributeMapper {

    public static final String PROVIDER_ID = "clave-user-attribute-mapper";

    // eIDAS Attribute URIs
    public static final String PERSON_IDENTIFIER = "http://eidas.europa.eu/attributes/naturalperson/PersonIdentifier";
    public static final String GIVEN_NAME = "http://eidas.europa.eu/attributes/naturalperson/CurrentGivenName";
    public static final String FAMILY_NAME = "http://eidas.europa.eu/attributes/naturalperson/CurrentFamilyName";
    public static final String DATE_OF_BIRTH = "http://eidas.europa.eu/attributes/naturalperson/DateOfBirth";

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
        return "Cl@ve Attribute Importer";
    }

    @Override
    public String getHelpText() {
        return "Import attributes from Cl@ve SAML assertions into user attributes or properties.";
    }
}
