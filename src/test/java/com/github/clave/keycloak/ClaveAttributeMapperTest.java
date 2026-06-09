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

    @Test
    public void testConstants() {
        assertEquals("http://eidas.europa.eu/attributes/naturalperson/PersonIdentifier", ClaveAttributeMapper.PERSON_IDENTIFIER);
        assertEquals("http://eidas.europa.eu/attributes/naturalperson/CurrentGivenName", ClaveAttributeMapper.GIVEN_NAME);
        assertEquals("http://eidas.europa.eu/attributes/naturalperson/CurrentFamilyName", ClaveAttributeMapper.FAMILY_NAME);
        assertEquals("http://eidas.europa.eu/attributes/naturalperson/DateOfBirth", ClaveAttributeMapper.DATE_OF_BIRTH);
    }
}
