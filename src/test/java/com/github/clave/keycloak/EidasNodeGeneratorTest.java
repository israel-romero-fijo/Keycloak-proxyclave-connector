package com.github.clave.keycloak;

import org.junit.jupiter.api.Test;
import org.keycloak.saml.SamlProtocolExtensionsAwareBuilder;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EidasNodeGeneratorTest {

    @Test
    public void testEidasNodeGeneratorPublic() throws Exception {
        // Access private inner class via reflection for testing
        Class<?> clazz = Class.forName("com.github.clave.keycloak.ClaveIdentityProvider$EidasNodeGenerator");
        Constructor<?> constructor = clazz.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);
        Object generator = constructor.newInstance("public");

        Method writeMethod = clazz.getDeclaredMethod("write", XMLStreamWriter.class);
        writeMethod.setAccessible(true);

        StringWriter stringWriter = new StringWriter();
        XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);

        writeMethod.invoke(generator, xmlWriter);

        String xml = stringWriter.toString();
        assertTrue(xml.contains("eidas:SPType"));
        assertTrue(xml.contains(">public<"));
        assertTrue(xml.contains("http://eidas.europa.eu/saml-extensions"));
    }

    @Test
    public void testEidasNodeGeneratorPrivate() throws Exception {
        Class<?> clazz = Class.forName("com.github.clave.keycloak.ClaveIdentityProvider$EidasNodeGenerator");
        Constructor<?> constructor = clazz.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);
        Object generator = constructor.newInstance("private");

        Method writeMethod = clazz.getDeclaredMethod("write", XMLStreamWriter.class);
        writeMethod.setAccessible(true);

        StringWriter stringWriter = new StringWriter();
        XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);

        writeMethod.invoke(generator, xmlWriter);

        String xml = stringWriter.toString();
        assertTrue(xml.contains(">private<"));
    }
}
