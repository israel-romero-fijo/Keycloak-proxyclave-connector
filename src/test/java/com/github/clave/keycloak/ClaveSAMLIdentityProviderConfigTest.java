package com.github.clave.keycloak;

import org.junit.jupiter.api.Test;
import org.keycloak.models.IdentityProviderModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClaveSAMLIdentityProviderConfigTest {

    @Test
    public void testGetRequestedAttributes() {
        IdentityProviderModel model = new IdentityProviderModel();
        ClaveSAMLIdentityProviderConfig config = new ClaveSAMLIdentityProviderConfig(model);

        // Test default (null)
        assertTrue(config.getRequestedAttributes().isEmpty());

        // Test empty string
        config.getConfig().put(ClaveIdentityProviderFactory.CLAVE_REQUESTED_ATTRIBUTES, "");
        assertTrue(config.getRequestedAttributes().isEmpty());

        // Test single attribute
        config.getConfig().put(ClaveIdentityProviderFactory.CLAVE_REQUESTED_ATTRIBUTES, "attr1");
        assertEquals(1, config.getRequestedAttributes().size());
        assertEquals("attr1", config.getRequestedAttributes().get(0));

        // Test multiple attributes with spaces
        config.getConfig().put(ClaveIdentityProviderFactory.CLAVE_REQUESTED_ATTRIBUTES, " attr1 ,  attr2,attr3 ");
        List<String> attrs = config.getRequestedAttributes();
        assertEquals(3, attrs.size());
        assertEquals("attr1", attrs.get(0));
        assertEquals("attr2", attrs.get(1));
        assertEquals("attr3", attrs.get(2));
    }

    @Test
    public void testGetSpTypeDefault() {
        ClaveSAMLIdentityProviderConfig config = new ClaveSAMLIdentityProviderConfig(new IdentityProviderModel());
        assertEquals("public", config.getSpType());
    }

    @Test
    public void testGetLoaDefault() {
        ClaveSAMLIdentityProviderConfig config = new ClaveSAMLIdentityProviderConfig(new IdentityProviderModel());
        assertEquals(ClaveIdentityProviderFactory.LOA_SUBSTANTIAL, config.getLoa());
    }
}
