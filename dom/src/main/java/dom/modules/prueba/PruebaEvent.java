package dom.modules.prueba;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.services.eventbus.AbstractInteractionEvent;
import org.apache.isis.applib.services.eventbus.EventBusService;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;

import dom.modules.tablas.FormaServicio;

@SuppressWarnings("deprecation")
@DomainService
public class PruebaEvent {
    private final static Logger LOG = LoggerFactory.getLogger(PruebaEvent.class);

    @SuppressWarnings("unused")
    @Inject
    private DomainObjectContainer container;
    @Inject
    private EventBusService eventBusService;

    @Programmatic
    @Subscribe
    @EventHandler
    public void on(final Prueba.PruebaEvent ev) {
        if (ev.getPhase() == AbstractInteractionEvent.Phase.VALIDATE) {
            Prueba pru = ev.getSource();
            LOG.warn("evento");
            FormaServicio forma = ev.getNewValue();
            if (forma != null) {
                String nombre = forma.getNombre();
                LOG.warn("nombre es " + nombre);
                if (nombre == null) {
                    ev.veto("nombre de formaservicio es nulo");
                } else if (nombre.equals("Otro") && pru.getOtherMode() == null) {
                    ev.invalidate("Si forma de servicio es Otro, debe especificar forma de servicio.");
                }
            }
        }
    }

    @Programmatic
    @PostConstruct
    public void postConstruct() {
        LOG.warn("postConstruct");
        eventBusService.register(this);
    }

    @Programmatic
    @PreDestroy
    public void preDestroy() {
        LOG.warn("preDestroy");
        eventBusService.unregister(this);
    }

}
