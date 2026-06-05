package com.github.clave.keycloak;

import org.keycloak.broker.saml.SAMLIdentityProviderFactory;
import org.keycloak.broker.saml.SAMLIdentityProviderConfig;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;

public class ClaveIdentityProviderFactory extends SAMLIdentityProviderFactory {

    public static final String PROVIDER_ID = "clave-saml";
    public static final String NAME = "Cl@ve";
    public static final String CLAVE_SP_TYPE = "clave.sp.type";
    public static final String CLAVE_LOA = "clave.loa";

    public static final String LOA_LOW = "http://eidas.europa.eu/LoA/low";
    public static final String LOA_SUBSTANTIAL = "http://eidas.europa.eu/LoA/substantial";
    public static final String LOA_HIGH = "http://eidas.europa.eu/LoA/high";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ClaveIdentityProvider create(KeycloakSession session, IdentityProviderModel model) {
        return new ClaveIdentityProvider(session, new SAMLIdentityProviderConfig(model));
    }

    @Override
    public SAMLIdentityProviderConfig createConfig() {
        SAMLIdentityProviderConfig config = new SAMLIdentityProviderConfig();
        // Set defaults for Cl@ve
        config.setSignSpMetadata(true);
        config.setWantAuthnRequestsSigned(true);
        config.setSignatureAlgorithm("RSA_SHA256");
        config.setNameIDPolicyFormat("urn:oasis:names:tc:SAML:2.0:nameid-format:persistent");
        config.setForceAuthn(true);
        config.getConfig().put(CLAVE_LOA, LOA_SUBSTANTIAL);
        config.getConfig().put(CLAVE_SP_TYPE, "public");
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
                .label("clave.sp.type.label")
                .helpText("clave.sp.type.tooltip")
                .type(ProviderConfigProperty.LIST_TYPE)
                .options("public", "private")
                .defaultValue("public")
                .add()
                .property()
                .name(CLAVE_LOA)
                .label("clave.loa.label")
                .helpText("clave.loa.tooltip")
                .type(ProviderConfigProperty.LIST_TYPE)
                .options(LOA_LOW, LOA_SUBSTANTIAL, LOA_HIGH)
                .defaultValue(LOA_SUBSTANTIAL)
                .add()
                .build());

        return properties;
    }
}
