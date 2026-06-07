package com.github.clave.keycloak;

import org.junit.jupiter.api.Test;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringWriter;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EidasNodeGeneratorTest {

    @Test
    public void testEidasNodeGeneratorPublic() throws Exception {
        EidasNodeGenerator generator = new EidasNodeGenerator("public");

        StringWriter stringWriter = new StringWriter();
        XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);

        generator.write(xmlWriter);

        String xml = stringWriter.toString();
        assertTrue(xml.contains("eidas:SPType"));
        assertTrue(xml.contains(">public<"));
        assertTrue(xml.contains("http://eidas.europa.eu/saml-extensions"));
    }

    @Test
    public void testEidasNodeGeneratorWithAttributes() throws Exception {
        EidasNodeGenerator generator = new EidasNodeGenerator("public", Arrays.asList(
                "http://eidas.europa.eu/attributes/naturalperson/PersonIdentifier",
                "http://eidas.europa.eu/attributes/naturalperson/CurrentGivenName"
        ));

        StringWriter stringWriter = new StringWriter();
        XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);

        generator.write(xmlWriter);

        String xml = stringWriter.toString();
        assertTrue(xml.contains("eidas:SPType"));
        assertTrue(xml.contains("eidas:RequestedAttributes"));
        assertTrue(xml.contains("Name=\"http://eidas.europa.eu/attributes/naturalperson/PersonIdentifier\""));
        assertTrue(xml.contains("Name=\"http://eidas.europa.eu/attributes/naturalperson/CurrentGivenName\""));
        assertTrue(xml.contains("isRequired=\"true\""));
    }
}
