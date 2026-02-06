package com.github.clave.keycloak;

import org.keycloak.broker.provider.AbstractIdentityProviderMapper;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.broker.saml.mappers.UserAttributeMapper;
import org.keycloak.models.IdentityProviderMapperModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

public class ClaveAttributeMapper extends UserAttributeMapper {

    public static final String PROVIDER_ID = "clave-user-attribute-mapper";
    public static final String[] COMPATIBLE_PROVIDERS = {ClaveIdentityProviderFactory.PROVIDER_ID};

    @Override
    public String[] getCompatibleProviders() {
        return COMPATIBLE_PROVIDERS;
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    // Keycloak's UserAttributeMapper handles the "import" logic (SAML Attribute -> User Attribute)
    // We just need to register it with a specific ID to associate it easily with our provider.
    // However, for eIDAS, attributes often come with complex names or FriendlyNames.
    // This mapper is a convenience wrapper.
}
