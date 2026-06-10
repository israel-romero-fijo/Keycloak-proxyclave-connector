package com.github.clave.keycloak;

import org.junit.jupiter.api.Test;
import org.keycloak.broker.saml.SAMLIdentityProviderConfig;
import org.keycloak.models.IdentityProviderModel;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClaveIdentityProviderTest {

    @Test
    public void testProviderInitialization() {
        IdentityProviderModel model = new IdentityProviderModel();
        model.getConfig().put(SAMLIdentityProviderConfig.SINGLE_SIGN_ON_SERVICE_URL, "http://localhost/sso");
        model.getConfig().put(ClaveIdentityProviderFactory.CLAVE_REQUESTED_ATTRIBUTES, ClaveIdentityProviderFactory.DEFAULT_REQUESTED_ATTRIBUTES);

        SAMLIdentityProviderConfig config = new SAMLIdentityProviderConfig(model);
        ClaveIdentityProvider provider = new ClaveIdentityProvider(null, config);

        assertNotNull(provider);
    }
}
