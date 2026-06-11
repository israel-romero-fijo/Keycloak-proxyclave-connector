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
public class EidasNodeGenerator implements SamlProtocolExtensionsAwareBuilder.NodeGenerator {
    private final String spType;
    private final List<String> requestedAttributes;
    private static final String EIDAS_NS = "http://eidas.europa.eu/saml-extensions";

    public EidasNodeGenerator(String spType, List<String> requestedAttributes) {
        this.spType = spType;
        this.requestedAttributes = requestedAttributes;
    }

    @Override
    public void write(XMLStreamWriter writer) throws ProcessingException {
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
