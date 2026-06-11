package com.github.clave.keycloak;

import org.keycloak.broker.saml.SAMLIdentityProviderConfig;
import org.keycloak.models.IdentityProviderModel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClaveSAMLIdentityProviderConfig extends SAMLIdentityProviderConfig {

    public ClaveSAMLIdentityProviderConfig(IdentityProviderModel model) {
        super(model);
    }

    public ClaveSAMLIdentityProviderConfig() {
        super();
    }

    public String getSpType() {
        return getConfig().getOrDefault(ClaveIdentityProviderFactory.CLAVE_SP_TYPE, "public");
    }

    public void setSpType(String spType) {
        getConfig().put(ClaveIdentityProviderFactory.CLAVE_SP_TYPE, spType);
    }

    public String getLoa() {
        return getConfig().getOrDefault(ClaveIdentityProviderFactory.CLAVE_LOA, ClaveIdentityProviderFactory.LOA_SUBSTANTIAL);
    }

    public void setLoa(String loa) {
        getConfig().put(ClaveIdentityProviderFactory.CLAVE_LOA, loa);
    }

    public List<String> getRequestedAttributes() {
        String attrStr = getConfig().get(ClaveIdentityProviderFactory.CLAVE_REQUESTED_ATTRIBUTES);
        if (attrStr == null || attrStr.trim().isEmpty()) {
            return List.of();
        }
        return Arrays.stream(attrStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    public void setRequestedAttributes(String attributes) {
        getConfig().put(ClaveIdentityProviderFactory.CLAVE_REQUESTED_ATTRIBUTES, attributes);
    }
}
