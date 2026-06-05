package com.github.clave.keycloak;

import org.junit.jupiter.api.Test;
import org.keycloak.broker.saml.SAMLIdentityProviderConfig;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClaveIdentityProviderFactoryTest {

    private final ClaveIdentityProviderFactory factory = new ClaveIdentityProviderFactory();

    @Test
    public void testFactoryId() {
        assertEquals("clave-saml", factory.getId());
    }

    @Test
    public void testFactoryName() {
        assertEquals("Cl@ve", factory.getName());
    }

    @Test
    public void testCreateConfig() {
        SAMLIdentityProviderConfig config = factory.createConfig();
        assertTrue(config.isWantAuthnRequestsSigned());
        assertEquals("RSA_SHA256", config.getSignatureAlgorithm());
        assertEquals("urn:oasis:names:tc:SAML:2.0:nameid-format:persistent", config.getNameIDPolicyFormat());
        assertTrue(config.isForceAuthn());
    }

    @Test
    public void testGetConfigProperties() {
        List<ProviderConfigProperty> properties = factory.getConfigProperties();

        boolean hasSpType = properties.stream().anyMatch(p -> p.getName().equals(ClaveIdentityProviderFactory.CLAVE_SP_TYPE));
        boolean hasLoa = properties.stream().anyMatch(p -> p.getName().equals(ClaveIdentityProviderFactory.CLAVE_LOA));

        assertTrue(hasSpType, "Missing eIDAS SP Type property");
        assertTrue(hasLoa, "Missing eIDAS LoA property");
    }
}
