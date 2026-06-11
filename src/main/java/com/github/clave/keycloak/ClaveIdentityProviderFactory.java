package com.github.clave.keycloak;

import org.keycloak.broker.saml.SAMLIdentityProviderFactory;
import org.keycloak.broker.saml.SAMLIdentityProviderConfig;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.RealmModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for the Cl@ve SAML Identity Provider.
 * <p>
 * This factory provides default configuration suitable for Cl@ve and eIDAS,
 * including SPType and LoA settings.
 */
public class ClaveIdentityProviderFactory extends SAMLIdentityProviderFactory {

    public static final String PROVIDER_ID = "clave-saml";
    public static final String NAME = "Cl@ve";
    public static final String CLAVE_SP_TYPE = "clave.sp.type";
    public static final String CLAVE_LOA = "clave.loa";
    public static final String CLAVE_REQUESTED_ATTRIBUTES = "clave.requested.attributes";

    public static final String LOA_LOW = "http://eidas.europa.eu/LoA/low";
    public static final String LOA_SUBSTANTIAL = "http://eidas.europa.eu/LoA/substantial";
    public static final String LOA_HIGH = "http://eidas.europa.eu/LoA/high";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ClaveIdentityProvider create(KeycloakSession session, IdentityProviderModel model) {
        return new ClaveIdentityProvider(session, new ClaveSAMLIdentityProviderConfig(model));
    }

    @Override
    public ClaveSAMLIdentityProviderConfig createConfig() {
        ClaveSAMLIdentityProviderConfig config = new ClaveSAMLIdentityProviderConfig();
        // Set defaults for Cl@ve
        config.setSignSpMetadata(true);
        config.setWantAuthnRequestsSigned(true);
        config.setSignatureAlgorithm("RSA_SHA256");
        config.setNameIDPolicyFormat("urn:oasis:names:tc:SAML:2.0:nameid-format:persistent");
        config.setForceAuthn(true);
        config.getConfig().put(SAMLIdentityProviderConfig.POST_BINDING_AUTHN_REQUEST, "true");
        config.getConfig().put(CLAVE_LOA, LOA_SUBSTANTIAL);
        config.getConfig().put(CLAVE_SP_TYPE, "public");
        config.getConfig().put(CLAVE_REQUESTED_ATTRIBUTES, "http://eidas.europa.eu/attributes/naturalperson/PersonIdentifier,http://eidas.europa.eu/attributes/naturalperson/CurrentGivenName,http://eidas.europa.eu/attributes/naturalperson/CurrentFamilyName");
        return config;
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        List<ProviderConfigProperty> properties = new ArrayList<>(super.getConfigProperties());

        properties.addAll(ProviderConfigurationBuilder.create()
                .property()
                .name(CLAVE_SP_TYPE)
                .label("clave.sp.type")
                .helpText("clave.sp.type.tooltip")
                .type(ProviderConfigProperty.LIST_TYPE)
                .options("public", "private")
                .defaultValue("public")
                .add()
                .property()
                .name(CLAVE_LOA)
                .label("clave.loa")
                .helpText("clave.loa.tooltip")
                .type(ProviderConfigProperty.LIST_TYPE)
                .options(LOA_LOW, LOA_SUBSTANTIAL, LOA_HIGH)
                .defaultValue(LOA_SUBSTANTIAL)
                .add()
                .property()
                .name(CLAVE_REQUESTED_ATTRIBUTES)
                .label("clave.requested.attributes")
                .helpText("clave.requested.attributes.tooltip")
                .type(ProviderConfigProperty.STRING_TYPE)
                .defaultValue("http://eidas.europa.eu/attributes/naturalperson/PersonIdentifier,http://eidas.europa.eu/attributes/naturalperson/CurrentGivenName,http://eidas.europa.eu/attributes/naturalperson/CurrentFamilyName")
                .add()
                .build());

        return properties;
    }

}
