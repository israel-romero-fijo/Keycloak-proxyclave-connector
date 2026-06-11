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
import org.keycloak.crypto.KeyWrapper;
import org.keycloak.crypto.KeyUse;
import org.keycloak.crypto.Algorithm;
import org.keycloak.saml.SignatureAlgorithm;
import org.keycloak.dom.saml.v2.metadata.EntityDescriptorType;
import org.keycloak.dom.saml.v2.metadata.ExtensionsType;
import org.keycloak.dom.saml.v2.metadata.SPSSODescriptorType;
import org.keycloak.dom.saml.v2.protocol.AuthnRequestType;
import org.keycloak.saml.common.constants.JBossSAMLURIConstants;
import org.keycloak.saml.processing.api.saml.v2.request.SAML2Request;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

/**
 * Identity Provider implementation for Cl@ve (Spanish Government SAML Gateway).
 * <p>
 * This provider extends the standard SAML Identity Provider to include eIDAS specific extensions
 * like SPType and RequestedAuthnContext for Level of Assurance (LoA).
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

            ClaveSAMLIdentityProviderConfig config = (ClaveSAMLIdentityProviderConfig) getConfig();

            if (logger.isDebugEnabled()) {
                logger.debugf("Preparing Cl@ve SAML request. Issuer: %s, Destination: %s, Locale: %s",
                    issuerURL, destinationUrl, session.getContext().resolveLocale(null).getLanguage());
            }

            SAML2AuthnRequestBuilder build = new SAML2AuthnRequestBuilder()
                .destination(destinationUrl)
                .issuer(issuerURL)
                .forceAuthn(config.isForceAuthn())
                .isPassive(false)
                .nameIdPolicy(SAML2NameIDPolicyBuilder.format(config.getNameIDPolicyFormat()));

            // Add eIDAS extensions (SPType and RequestedAttributes)
            build.addExtension(new EidasNodeGenerator(config.getSpType(), config.getRequestedAttributes(), config.getProviderName()));

            // Add RequestedAuthnContext (LoA)
            build.requestedAuthnContext(new SAML2RequestedAuthnContextBuilder()
                .setComparison(AuthnContextComparisonType.MINIMUM)
                .addAuthnContextClassRef(config.getLoa()));

            // Create Binding Builder
            JaxrsSAML2BindingBuilder binding = new JaxrsSAML2BindingBuilder(session)
                    .relayState(request.getState().getEncoded());

            if (config.isWantAuthnRequestsSigned()) {
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

            AuthnRequestType authnRequest = build.createAuthnRequest();
            if (config.getProviderName() != null && !config.getProviderName().isEmpty()) {
                authnRequest.setProviderName(config.getProviderName());
            }
            Document authnDoc = SAML2Request.convert(authnRequest);

            if (config.isPostBindingAuthnRequest()) {
                return binding.postBinding(authnDoc).request(destinationUrl);
            } else {
                return binding.redirectBinding(authnDoc).request(destinationUrl);
            }

        } catch (Exception e) {
            logger.error("Error preparing Cl@ve SAML request", e);
            throw new RuntimeException("Error preparing Cl@ve SAML request", e);
        }
    }

    @Override
    public Response export(UriInfo uriInfo, RealmModel realm, String format) {
        Response response = super.export(uriInfo, realm, format);
        if (response.getStatus() == 200 && response.getEntity() instanceof EntityDescriptorType) {
            EntityDescriptorType entityDescriptor = (EntityDescriptorType) response.getEntity();
            ClaveSAMLIdentityProviderConfig config = (ClaveSAMLIdentityProviderConfig) getConfig();

            // Add eIDAS Extensions to Metadata
            SPSSODescriptorType spDescriptor = entityDescriptor.getChoiceType().get(0).getDescriptors().stream()
                    .filter(d -> d.getSpDescriptor() != null)
                    .map(d -> d.getSpDescriptor())
                    .findFirst()
                    .orElse(null);

            if (spDescriptor != null) {
                if (spDescriptor.getExtensions() == null) {
                    spDescriptor.setExtensions(new ExtensionsType());
                }

                // Manually inject SPType as a DOM element into extensions
                try {
                    Document doc = org.keycloak.saml.common.util.DocumentUtil.createDocument();
                    Element spTypeElement = doc.createElementNS("http://eidas.europa.eu/saml-extensions", "eidas:SPType");
                    spTypeElement.setTextContent(config.getSpType());
                    spDescriptor.getExtensions().addExtension(spTypeElement);
                } catch (Exception e) {
                    logger.warn("Failed to inject eIDAS extensions into metadata", e);
                }
            }
        }
        return response;
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
