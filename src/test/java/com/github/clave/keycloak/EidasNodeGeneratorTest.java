package com.github.clave.keycloak;

import org.junit.jupiter.api.Test;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EidasNodeGeneratorTest {

    @Test
    public void testEidasNodeGeneratorPublic() throws Exception {
        EidasNodeGenerator generator = new EidasNodeGenerator("public", Collections.emptyList(), "TestProvider");

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
        EidasNodeGenerator generator = new EidasNodeGenerator("private", null, null);

        StringWriter stringWriter = new StringWriter();
        XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);

        generator.write(xmlWriter);

        String xml = stringWriter.toString();
        assertTrue(xml.contains(">private<"));
    }

    @Test
    public void testEidasNodeGeneratorWithAttributes() throws Exception {
        List<String> attributes = Arrays.asList("attr1", "attr2");
        EidasNodeGenerator generator = new EidasNodeGenerator("public", attributes, "TestProvider");

        StringWriter stringWriter = new StringWriter();
        XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);

        generator.write(xmlWriter);

        String xml = stringWriter.toString();
        assertTrue(xml.contains("eidas:RequestedAttributes"));
        assertTrue(xml.contains("Name=\"attr1\""));
        assertTrue(xml.contains("Name=\"attr2\""));
        assertTrue(xml.contains("isRequired=\"true\""));
    }
}
