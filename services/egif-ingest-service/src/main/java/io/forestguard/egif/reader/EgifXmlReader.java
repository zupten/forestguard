package io.forestguard.egif.reader;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import io.forestguard.egif.dto.IncendioEgif;

@Component
public class EgifXmlReader {

    public List<IncendioEgif> leer(Path archivo) throws Exception {
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newDefaultInstance();

        // Impedir la lectura de recursos externos desde el XML.
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature(
                "http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);

        Document documento = factory.newDocumentBuilder()
                .parse(archivo.toFile());

        if (!"pifs".equals(documento.getDocumentElement().getTagName())) {
            throw new IllegalArgumentException("Raiz XML inesperada");
        }

        XPath xpath = XPathFactory.newInstance().newXPath();

        // Seleccionar registros, sin incluir el esquema XSD.
        NodeList registros = (NodeList) xpath.evaluate(
                "/pifs/Pif", documento, XPathConstants.NODESET);

        if (registros.getLength() == 0) {
            throw new IllegalArgumentException("El archivo no contiene incendios");
        }

        List<IncendioEgif> incendios = new ArrayList<>();
        Set<String> numeros = new HashSet<>();

        for (int i = 0; i < registros.getLength(); i++) {
            Node registro = registros.item(i);
            String numero = texto(xpath, registro, "numeroparte");

            try {
                if (!numeros.add(numero)) {
                    throw new IllegalArgumentException("Numero de parte duplicado");
                }

                double latitud = coordenada(
                        texto(xpath, registro, "pif_localizacion/latitud"),
                        -90, 90);
                double longitud = coordenada(
                        texto(xpath, registro, "pif_localizacion/longitud"),
                        -180, 180);

                incendios.add(new IncendioEgif(
                        numero,
                        texto(xpath, registro, "pif_localizacion/idprovincia"),
                        texto(xpath, registro, "pif_localizacion/idmunicipio"),
                        LocalDateTime.parse(
                                texto(xpath, registro, "pif_tiempos/deteccion")),
                        latitud,
                        longitud,
                        texto(xpath, registro, "pif_localizacion/iddatum")
                ));
            } catch (Exception exception) {
                throw new IllegalArgumentException(
                        "Registro EGIF no valido: " + numero, exception);
            }
        }

        return List.copyOf(incendios);
    }

    private String texto(XPath xpath, Node nodo, String ruta) throws Exception {
        String valor = xpath.evaluate(ruta, nodo).trim();

        if (valor.isEmpty()) {
            throw new IllegalArgumentException("Campo obligatorio ausente: " + ruta);
        }

        return valor;
    }

    private double coordenada(String texto, double minimo, double maximo) {
        double valor = Double.parseDouble(texto);

        if (!Double.isFinite(valor) || valor < minimo || valor > maximo) {
            throw new IllegalArgumentException("Coordenada fuera de rango");
        }

        return valor;
    }
}