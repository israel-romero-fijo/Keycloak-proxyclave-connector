package com.github.clave.keycloak;

import org.keycloak.broker.provider.AuthenticationRequest;
import org.keycloak.broker.saml.SAMLIdentityProvider;
import org.keycloak.broker.saml.SAMLIdentityProviderConfig;
import org.keycloak.dom.saml.v2.protocol.AuthnContextComparisonType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.protocol.saml.JaxrsSAML2BindingBuilder;
import org.keycloak.saml.SAML2AuthnRequestBuilder;
import org.keycloak.saml.SAML2RequestedAuthnContextBuilder;
import org.keycloak.saml.SAML2NameIDPolicyBuilder;
import org.keycloak.crypto.KeyWrapper;
import org.keycloak.crypto.KeyUse;
import org.keycloak.saml.SignatureAlgorithm;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import org.jboss.logging.Logger;

public class ClaveIdentityProvider extends SAMLIdentityProvider {

    protected static final Logger logger = Logger.getLogger(ClaveIdentityProvider.class);

    public ClaveIdentityProvider(KeycloakSession session, SAMLIdentityProviderConfig config) {
        super(session, config, org.keycloak.saml.validators.DestinationValidator.forProtocolMap(null));
    }

    @Override
    public Response performLogin(AuthenticationRequest request) {
        try {
            UriInfo uriInfo = request.getUriInfo();
            RealmModel realm = request.getRealm();
            String issuerURL = getEntityId(uriInfo, realm);
            String destinationUrl = getConfig().getSingleSignOnServiceUrl();

            SAML2AuthnRequestBuilder build = new SAML2AuthnRequestBuilder()
                .destination(destinationUrl)
                .issuer(issuerURL)
                .forceAuthn(getConfig().isForceAuthn())
                .isPassive(false)
                .nameIdPolicy(SAML2NameIDPolicyBuilder.format(getConfig().getNameIDPolicyFormat()));

            // Add SPType extension for eIDAS
            String spType = getConfig().getConfig().getOrDefault(ClaveIdentityProviderFactory.CLAVE_SP_TYPE, "public");
            build.addExtension(new EidasNodeGenerator(spType));

            // Add RequestedAuthnContext (LoA) for Cl@ve 2.0
            String loa = getConfig().getConfig().get(ClaveIdentityProviderFactory.CLAVE_LOA);
            if (loa != null && !loa.isEmpty()) {
                build.requestedAuthnContext(new SAML2RequestedAuthnContextBuilder()
                    .setComparison(AuthnContextComparisonType.MINIMUM)
                    .addAuthnContextClassRef(loa));
            }

            // Create Binding Builder
            JaxrsSAML2BindingBuilder binding = new JaxrsSAML2BindingBuilder(session)
                    .relayState(request.getState().getEncoded());

            if (getConfig().isWantAuthnRequestsSigned()) {
                SignatureAlgorithm sa = getSignatureAlgorithm();
                String algorithm = getAlgorithmName(sa);
                KeyWrapper key = session.keys().getActiveKey(realm, KeyUse.SIG, algorithm);

                if (key != null) {
                     binding.signWith(key.getKid(), (PrivateKey) key.getPrivateKey(), (PublicKey) key.getPublicKey(), (X509Certificate) key.getCertificate())
                            .signatureAlgorithm(sa)
                            .signDocument();
                } else {
                    logger.warnf("No active key found for algorithm %s in realm %s", algorithm, realm.getName());
                }
            }

            if (getConfig().isPostBindingAuthnRequest()) {
                return binding.postBinding(build.toDocument()).request(destinationUrl);
            } else {
                return binding.redirectBinding(build.toDocument()).request(destinationUrl);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error preparing Cl@ve SAML request", e);
        }
    }

    @Override
    public SignatureAlgorithm getSignatureAlgorithm() {
        String alg = getConfig().getSignatureAlgorithm();
        if (alg != null && !alg.isEmpty()) {
            try {
                return SignatureAlgorithm.valueOf(alg);
            } catch (IllegalArgumentException e) {
                logger.warnf("Invalid signature algorithm %s, defaulting to RSA_SHA256", alg);
            }
        }
        return SignatureAlgorithm.RSA_SHA256;
    }

    private String getAlgorithmName(SignatureAlgorithm sa) {
        String name = sa.name();
        if (name.equalsIgnoreCase("RSA_SHA256")) return "RS256";
        if (name.equalsIgnoreCase("RSA_SHA384")) return "RS384";
        if (name.equalsIgnoreCase("RSA_SHA512")) return "RS512";
        if (name.equalsIgnoreCase("ECDSA_SHA256")) return "ES256";
        if (name.equalsIgnoreCase("ECDSA_SHA384")) return "ES384";
        if (name.equalsIgnoreCase("ECDSA_SHA512")) return "ES512";
        return "RS256";
    }

    private String getEntityId(UriInfo uriInfo, RealmModel realm) {
        if (getConfig().getEntityId() != null && !getConfig().getEntityId().isEmpty()) {
            return getConfig().getEntityId();
        }
        return uriInfo.getBaseUriBuilder()
                .path("realms").path(realm.getName())
                .path("broker")
                .path(getConfig().getAlias())
                .path("endpoint")
                .build().toString();
    }
}
