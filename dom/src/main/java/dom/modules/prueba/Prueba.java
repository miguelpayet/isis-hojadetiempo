package dom.modules.prueba;

import dom.modules.tablas.FormaServicio;
import org.apache.isis.applib.AbstractDomainObject;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.services.eventbus.PropertyDomainEvent;

import javax.jdo.annotations.*;

@DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@DomainObject(objectType = "Prueba")
@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Queries({ @javax.jdo.annotations.Query(	name = "find",
        language = "JDOQL",
        value = "SELECT FROM hojadetiempo.dom.modules.otherMode.Prueba") })
@Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
public class Prueba extends AbstractDomainObject implements Comparable<Prueba> {

    public static class PruebaEvent extends PropertyDomainEvent<Prueba, FormaServicio> {

        private static final long serialVersionUID = 1L;

        public PruebaEvent(final Prueba source, final Identifier identifier) {
            super(source, identifier);
        }

        public PruebaEvent(final Prueba source, final Identifier identifier, final FormaServicio oldValue, final FormaServicio newValue) {
            super(source, identifier, oldValue, newValue);
        }
    }

    private FormaServicio serviceMode;
    private String otherMode;

    @Override
    public int compareTo(final Prueba other) {
        return 1;
    }

    @Column(allowsNull = "true")
    @MemberOrder(sequence = "1")
    @Property(domainEvent = PruebaEvent.class)
    public FormaServicio getServiceMode() {
        System.out.println("getServiceMode");
        return serviceMode;
    }

    @Column(allowsNull = "true")
    @MemberOrder(sequence = "2")
    public String getOtherMode() {
        System.out.println("getOtherMode");
        return otherMode;
    }

    public void setServiceMode(FormaServicio mode) {
        System.out.println("setServiceMode");
        serviceMode = mode;
    }

    public void setOtherMode(String other) {
        System.out.println("setOtherMode " + other);
        otherMode = other;
    }

	/*
	 * public String validateServiceMode(FormaServicio param) {
	 * System.out.println("hola"); if (param.getNombre().equals("Otro") &&
	 * this.getOtherMode() == null) { return "Describe other mode"; } else {
	 * return ""; } }
	 */
}
