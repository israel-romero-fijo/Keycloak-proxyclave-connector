package com.github.clave.keycloak;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ClaveAttributeMapperTest {

    @Test
    public void testMapperConfiguration() {
        ClaveAttributeMapper mapper = new ClaveAttributeMapper();
        assertEquals("clave-user-attribute-mapper", mapper.getId());
        assertArrayEquals(new String[]{"clave-saml"}, mapper.getCompatibleProviders());
    }
}
