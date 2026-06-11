package com.github.clave.keycloak;

import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.broker.saml.mappers.UserAttributeMapper;
import org.keycloak.models.IdentityProviderMapperModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom attribute mapper for Cl@ve.
 * Extends the default SAML UserAttributeMapper to provide specific support for Cl@ve/eIDAS attributes.
 */
public class ClaveAttributeMapper extends UserAttributeMapper {

    public static final String PROVIDER_ID = "clave-user-attribute-mapper";

    // Standard eIDAS attributes for Cl@ve
    public static final String PERSON_IDENTIFIER = "http://eidas.europa.eu/attributes/naturalperson/PersonIdentifier";
    public static final String GIVEN_NAME = "http://eidas.europa.eu/attributes/naturalperson/CurrentGivenName";
    public static final String FAMILY_NAME = "http://eidas.europa.eu/attributes/naturalperson/CurrentFamilyName";
    public static final String DATE_OF_BIRTH = "http://eidas.europa.eu/attributes/naturalperson/DateOfBirth";

    public static final String EXTRACT_DNI = "extract.dni";

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        List<ProviderConfigProperty> configProperties = new ArrayList<>(super.getConfigProperties());

        ProviderConfigProperty property = new ProviderConfigProperty();
        property.setName(EXTRACT_DNI);
        property.setLabel("clave.extract.dni");
        property.setHelpText("clave.extract.dni.tooltip");
        property.setType(ProviderConfigProperty.BOOLEAN_TYPE);
        property.setDefaultValue("false");
        configProperties.add(property);

        return configProperties;
    }

    @Override
    public void preprocessFederatedIdentity(KeycloakSession session, org.keycloak.models.RealmModel realm, IdentityProviderMapperModel mapperModel, BrokeredIdentityContext context) {
        transformAttribute(mapperModel, context);
        super.preprocessFederatedIdentity(session, realm, mapperModel, context);
    }

    @Override
    public void updateBrokeredUser(KeycloakSession session, org.keycloak.models.RealmModel realm, UserModel user, IdentityProviderMapperModel mapperModel, BrokeredIdentityContext context) {
        transformAttribute(mapperModel, context);
        super.updateBrokeredUser(session, realm, user, mapperModel, context);
    }

    private void transformAttribute(IdentityProviderMapperModel mapperModel, BrokeredIdentityContext context) {
        if (Boolean.parseBoolean(mapperModel.getConfig().get(EXTRACT_DNI))) {
            String attributeName = mapperModel.getConfig().get(UserAttributeMapper.ATTRIBUTE_NAME);
            if (PERSON_IDENTIFIER.equals(attributeName)) {
                Object value = context.getContextData().get(attributeName);
                if (value instanceof String) {
                    String dni = extractDni((String) value);
                    context.getContextData().put(attributeName, dni);
                }
            }
        }
    }

    private String extractDni(String eidasIdentifier) {
        if (eidasIdentifier == null) return null;
        // eIDAS identifiers format: CountryCode/CountryCode/Identifier (e.g. ES/ES/12345678Z)
        // or sometimes: CountryCode/Other/Identifier
        String[] parts = eidasIdentifier.split("/");
        if (parts.length > 0) {
            return parts[parts.length - 1];
        }
        return eidasIdentifier;
    }

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
