package com.github.clave.keycloak;

import org.keycloak.saml.SamlProtocolExtensionsAwareBuilder;
import org.keycloak.saml.common.exceptions.ProcessingException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Generates the eIDAS SPType extension for SAML AuthnRequests.
 */
public class EidasNodeGenerator implements SamlProtocolExtensionsAwareBuilder.NodeGenerator {
    private final String spType;
    private static final String EIDAS_NS = "http://eidas.europa.eu/saml-extensions";

    public EidasNodeGenerator(String spType) {
        this.spType = spType;
    }

    @Override
    public void write(XMLStreamWriter writer) throws ProcessingException {
        try {
            writer.writeStartElement("eidas", "SPType", EIDAS_NS);
            writer.writeNamespace("eidas", EIDAS_NS);
            writer.writeCharacters(spType);
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            throw new ProcessingException(e);
        }
    }
}
