package com.github.clave.keycloak;

import org.junit.jupiter.api.Test;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.models.IdentityProviderMapperModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClaveAttributeMapperTest {

    @Test
    public void testMapperMetadata() {
        ClaveAttributeMapper mapper = new ClaveAttributeMapper();
        assertEquals(ClaveAttributeMapper.PROVIDER_ID, mapper.getId());
        assertEquals("Attribute Importer", mapper.getDisplayCategory());
        assertArrayEquals(new String[]{ClaveIdentityProviderFactory.PROVIDER_ID}, mapper.getCompatibleProviders());
    }

    @Test
    public void testUpdateBrokeredUserWithDniExtraction() {
        ClaveAttributeMapper mapper = new ClaveAttributeMapper();
        KeycloakSession session = mock(KeycloakSession.class);
        RealmModel realm = mock(RealmModel.class);
        UserModel user = mock(UserModel.class);
        IdentityProviderMapperModel mapperModel = new IdentityProviderMapperModel();
        Map<String, String> config = new HashMap<>();
        config.put(ClaveAttributeMapper.EXTRACT_DNI, "true");
        config.put("attribute.name", ClaveAttributeMapper.PERSON_IDENTIFIER);
        mapperModel.setConfig(config);

        BrokeredIdentityContext context = new BrokeredIdentityContext("id");
        context.getContextData().put(ClaveAttributeMapper.PERSON_IDENTIFIER, "ES/ES/12345678Z");

        mapper.updateBrokeredUser(session, realm, user, mapperModel, context);

        assertEquals("12345678Z", context.getContextData().get(ClaveAttributeMapper.PERSON_IDENTIFIER));
    }

    @Test
    public void testUpdateBrokeredUserWithoutDniExtraction() {
        ClaveAttributeMapper mapper = new ClaveAttributeMapper();
        KeycloakSession session = mock(KeycloakSession.class);
        RealmModel realm = mock(RealmModel.class);
        UserModel user = mock(UserModel.class);
        IdentityProviderMapperModel mapperModel = new IdentityProviderMapperModel();
        Map<String, String> config = new HashMap<>();
        config.put(ClaveAttributeMapper.EXTRACT_DNI, "false");
        config.put("attribute.name", ClaveAttributeMapper.PERSON_IDENTIFIER);
        mapperModel.setConfig(config);

        BrokeredIdentityContext context = new BrokeredIdentityContext("id");
        context.getContextData().put(ClaveAttributeMapper.PERSON_IDENTIFIER, "ES/ES/12345678Z");

        mapper.updateBrokeredUser(session, realm, user, mapperModel, context);

        assertEquals("ES/ES/12345678Z", context.getContextData().get(ClaveAttributeMapper.PERSON_IDENTIFIER));
    }
}
