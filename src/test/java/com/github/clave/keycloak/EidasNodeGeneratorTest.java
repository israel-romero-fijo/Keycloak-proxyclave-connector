package com.github.clave.keycloak;

import org.junit.jupiter.api.Test;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringWriter;

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
    public void testEidasNodeGeneratorPrivate() throws Exception {
        EidasNodeGenerator generator = new EidasNodeGenerator("private");

        StringWriter stringWriter = new StringWriter();
        XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);

        generator.write(xmlWriter);

        String xml = stringWriter.toString();
        assertTrue(xml.contains(">private<"));
    }
}
