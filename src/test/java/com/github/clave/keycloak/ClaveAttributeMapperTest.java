package com.github.clave.keycloak;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClaveAttributeMapperTest {

    @Test
    public void testMapperMetadata() {
        ClaveAttributeMapper mapper = new ClaveAttributeMapper();
        assertEquals(ClaveAttributeMapper.PROVIDER_ID, mapper.getId());
        assertEquals("Attribute Importer", mapper.getDisplayCategory());
        assertArrayEquals(new String[]{ClaveIdentityProviderFactory.PROVIDER_ID}, mapper.getCompatibleProviders());
    }
}
