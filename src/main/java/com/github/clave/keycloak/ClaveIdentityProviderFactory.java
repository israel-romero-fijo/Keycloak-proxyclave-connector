package com.github.clave.keycloak;

import org.keycloak.broker.provider.AbstractIdentityProviderFactory;
import org.keycloak.broker.saml.SAMLIdentityProviderConfig;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;

public class ClaveIdentityProviderFactory extends AbstractIdentityProviderFactory<ClaveIdentityProvider> {

    public static final String PROVIDER_ID = "clave-saml";
    public static final String NAME = "Cl@ve";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ClaveIdentityProvider create(KeycloakSession session, IdentityProviderModel model) {
        return new ClaveIdentityProvider(session, new SAMLIdentityProviderConfig(model));
    }

    @Override
    public IdentityProviderModel createConfig() {
        return new SAMLIdentityProviderConfig();
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
