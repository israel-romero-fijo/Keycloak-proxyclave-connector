package com.github.clave.keycloak;

import org.jboss.logging.Logger;
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
import org.keycloak.saml.SignatureAlgorithm;
import org.keycloak.crypto.KeyWrapper;
import org.keycloak.crypto.KeyUse;
import org.keycloak.crypto.Algorithm;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

/**
 * Identity Provider implementation for Spanish Cl@ve system.
 */
public class ClaveIdentityProvider extends SAMLIdentityProvider {

    private static final Logger logger = Logger.getLogger(ClaveIdentityProvider.class);

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

            // Add SPType extension
            String spType = getConfig().getConfig().getOrDefault(ClaveIdentityProviderFactory.CLAVE_SP_TYPE, "public");
            build.addExtension(new EidasNodeGenerator(spType));

            // Add RequestedAuthnContext (LoA)
            String loa = getConfig().getConfig().get(ClaveIdentityProviderFactory.CLAVE_LOA);
            if (loa == null || loa.isEmpty()) {
                loa = ClaveIdentityProviderFactory.LOA_SUBSTANTIAL;
            }

            build.requestedAuthnContext(new SAML2RequestedAuthnContextBuilder()
                .setComparison(AuthnContextComparisonType.MINIMUM)
                .addAuthnContextClassRef(loa));

            // Create Binding Builder
            JaxrsSAML2BindingBuilder binding = new JaxrsSAML2BindingBuilder(session)
                    .relayState(request.getState().getEncoded());

            if (getConfig().isWantAuthnRequestsSigned()) {
                KeyWrapper key = session.keys().getActiveKey(realm, KeyUse.SIG, Algorithm.RS256);

                if (key != null) {
                     SignatureAlgorithm sa;
                     try {
                         sa = SignatureAlgorithm.valueOf(getConfig().getSignatureAlgorithm());
                     } catch (IllegalArgumentException | NullPointerException e) {
                         logger.warnf("Invalid or missing signature algorithm: %s. Defaulting to RSA_SHA256.", getConfig().getSignatureAlgorithm());
                         sa = SignatureAlgorithm.RSA_SHA256;
                     }

                     binding.signWith(key.getKid(), (PrivateKey) key.getPrivateKey(), (PublicKey) key.getPublicKey(), (X509Certificate) key.getCertificate())
                            .signatureAlgorithm(sa)
                            .signDocument();
                }
            }

            return binding.redirectBinding(build.toDocument()).request(destinationUrl);

        } catch (Exception e) {
            logger.error("Error preparing Cl@ve SAML request", e);
            throw new RuntimeException("Error preparing Cl@ve SAML request", e);
        }
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
