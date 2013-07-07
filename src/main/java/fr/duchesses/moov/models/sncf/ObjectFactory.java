//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.07.02 at 10:56:44 PM CEST 
//


package fr.duchesses.moov.models.sncf;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the fr.duchesses.moov.models.sncf package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _PassagesTrain_QNAME = new QName("", "train");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: fr.duchesses.moov.models.sncf
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TrainType }
     * 
     */
    public TrainType createTrainType() {
        return new TrainType();
    }

    /**
     * Create an instance of {@link Passages }
     * 
     */
    public Passages createPassages() {
        return new Passages();
    }

    /**
     * Create an instance of {@link TrainType.Date }
     * 
     */
    public TrainType.Date createTrainTypeDate() {
        return new TrainType.Date();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TrainType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "train", scope = Passages.class)
    public JAXBElement<TrainType> createPassagesTrain(TrainType value) {
        return new JAXBElement<TrainType>(_PassagesTrain_QNAME, TrainType.class, Passages.class, value);
    }

}