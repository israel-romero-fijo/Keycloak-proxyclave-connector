package com.github.clave.keycloak;

import org.junit.jupiter.api.Test;
import org.keycloak.broker.saml.SAMLIdentityProviderConfig;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClaveIdentityProviderFactoryTest {

    private final ClaveIdentityProviderFactory factory = new ClaveIdentityProviderFactory();

    @Test
    public void testGetId() {
        assertEquals(ClaveIdentityProviderFactory.PROVIDER_ID, factory.getId());
    }

    @Test
    public void testGetName() {
        assertEquals(ClaveIdentityProviderFactory.NAME, factory.getName());
    }

    @Test
    public void testCreateConfig() {
        SAMLIdentityProviderConfig config = factory.createConfig();
        assertTrue(config.isSignSpMetadata());
        assertTrue(config.isWantAuthnRequestsSigned());
        assertEquals("RSA_SHA256", config.getSignatureAlgorithm());
        assertEquals("urn:oasis:names:tc:SAML:2.0:nameid-format:persistent", config.getNameIDPolicyFormat());
        assertTrue(config.isForceAuthn());
    }

    @Test
    public void testGetConfigProperties() {
        List<ProviderConfigProperty> properties = factory.getConfigProperties();

        boolean foundSpType = false;
        boolean foundLoa = false;
        boolean foundAttributes = false;

        for (ProviderConfigProperty property : properties) {
            if (ClaveIdentityProviderFactory.CLAVE_SP_TYPE.equals(property.getName())) {
                foundSpType = true;
                assertEquals("clave.sp.type", property.getLabel());
            } else if (ClaveIdentityProviderFactory.CLAVE_LOA.equals(property.getName())) {
                foundLoa = true;
                assertEquals("clave.loa", property.getLabel());
            } else if (ClaveIdentityProviderFactory.CLAVE_REQUESTED_ATTRIBUTES.equals(property.getName())) {
                foundAttributes = true;
                assertEquals("clave.requested.attributes", property.getLabel());
            }
        }

        assertTrue(foundSpType, "CLAVE_SP_TYPE property not found");
        assertTrue(foundLoa, "CLAVE_LOA property not found");
        assertTrue(foundAttributes, "CLAVE_REQUESTED_ATTRIBUTES property not found");
    }
}
