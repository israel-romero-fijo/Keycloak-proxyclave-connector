package com.github.clave.keycloak;

import org.keycloak.broker.saml.SAMLIdentityProvider;
import org.keycloak.broker.saml.SAMLIdentityProviderConfig;
import org.keycloak.models.KeycloakSession;
import org.keycloak.saml.validators.DestinationValidator;

public class ClaveIdentityProvider extends SAMLIdentityProvider {

    public ClaveIdentityProvider(KeycloakSession session, SAMLIdentityProviderConfig config) {
        super(session, config, DestinationValidator.forProtocolMap(null));
    }

    // Example of where one might customize the AuthnRequest
    // Note: Keycloak's SAMLIdentityProvider is robust, but adding specific extensions
    // might require overriding buildAuthnRequest if exposed, or interacting with the builder.
    // In many Keycloak versions, one might need to subclass the Login logic.
}
