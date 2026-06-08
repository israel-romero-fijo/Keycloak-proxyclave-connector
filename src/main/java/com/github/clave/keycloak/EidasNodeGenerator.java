package com.github.clave.keycloak;

import org.keycloak.saml.SamlProtocolExtensionsAwareBuilder;
import org.keycloak.saml.common.exceptions.ProcessingException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.List;

/**
 * Generates the eIDAS SPType and RequestedAttributes extensions for SAML AuthnRequests.
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
            writer.writeStartElement("eidas", "SPType", EIDAS_NS);
            writer.writeNamespace("eidas", EIDAS_NS);
            writer.writeCharacters(spType);
            writer.writeEndElement();

            if (requestedAttributes != null && !requestedAttributes.isEmpty()) {
                writer.writeStartElement("eidas", "RequestedAttributes", EIDAS_NS);
                for (String attribute : requestedAttributes) {
                    writer.writeStartElement("eidas", "RequestedAttribute", EIDAS_NS);
                    writer.writeAttribute("Name", attribute);
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
