package com.github.clave.keycloak;

import org.keycloak.saml.SamlProtocolExtensionsAwareBuilder;
import org.keycloak.saml.common.exceptions.ProcessingException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Generates the eIDAS SPType extension for SAML AuthnRequests.
 */
import java.util.List;

/**
 * Generates the eIDAS extensions for SAML AuthnRequests.
 * This includes SPType and RequestedAttributes.
 */
import org.keycloak.dom.saml.v2.protocol.AuthnRequestType;
import org.keycloak.saml.SamlProtocolExtensionsAwareBuilder;

public class EidasNodeGenerator implements SamlProtocolExtensionsAwareBuilder.NodeGenerator {
    private final String spType;
    private final List<String> requestedAttributes;
    private final String providerName;
    private static final String EIDAS_NS = "http://eidas.europa.eu/saml-extensions";

    public EidasNodeGenerator(String spType, List<String> requestedAttributes, String providerName) {
        this.spType = spType;
        this.requestedAttributes = requestedAttributes;
        this.providerName = providerName;
    }

    @Override
    public void write(XMLStreamWriter writer) throws ProcessingException {
        // SAML2AuthnRequestBuilder.toDocument() calls write(writer) inside the Extensions element.
        // We can't easily access the AuthnRequest object here to set ProviderName.
        // However, if we are in a context where we can use the writer, we might be able to.
        // But the write method is for Extensions.
        try {
            // Write SPType
            writer.writeStartElement("eidas", "SPType", EIDAS_NS);
            writer.writeNamespace("eidas", EIDAS_NS);
            writer.writeCharacters(spType);
            writer.writeEndElement();

            // Write RequestedAttributes if present
            if (requestedAttributes != null && !requestedAttributes.isEmpty()) {
                writer.writeStartElement("eidas", "RequestedAttributes", EIDAS_NS);
                for (String attribute : requestedAttributes) {
                    writer.writeStartElement("eidas", "RequestedAttribute", EIDAS_NS);
                    writer.writeAttribute("Name", attribute);
                    writer.writeAttribute("NameFormat", "urn:oasis:names:tc:SAML:2.0:attrname-format:uri");
                    writer.writeAttribute("isRequired", "true");
                    writer.writeEndElement();
                }
                writer.writeEndElement();
            }
        } catch (XMLStreamException e) {
            throw new ProcessingException(e);
        }
    }
}
