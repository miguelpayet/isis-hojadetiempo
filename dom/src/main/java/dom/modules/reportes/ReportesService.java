package dom.modules.reportes;

import dom.modules.clientes.Cliente;
import dom.modules.clientes.ClienteService;
import dom.modules.hoja.AjustadorFecha;
import dom.modules.hoja.HojaDeTiempoService;
import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.*;
import org.isisaddons.module.security.dom.role.ApplicationRole;
import org.isisaddons.module.security.dom.role.ApplicationRoles;
import org.isisaddons.module.security.dom.user.ApplicationUser;

import javax.inject.Inject;
import java.sql.Date;
import java.util.List;
import java.util.SortedSet;
import java.io.IOException;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sf.dynamicreports.report.exception.DRException;
import org.apache.isis.applib.value.Blob;

@DomainService()
@DomainServiceLayout(named = "Procesos", menuBar = DomainServiceLayout.MenuBar.PRIMARY, menuOrder = "2")
public class ReportesService extends AbstractFactoryAndRepository {
    private final static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ReportesService.class);

    private final AjustadorFecha ajustadorFecha = new AjustadorFecha();

    @Inject
    ApplicationRoles applicationRoles;
    @Inject
    ClienteService clienteService;

    @Programmatic
    public List<Cliente> autoComplete0ReportePorCliente(final String name) {
        return autoCompleteCliente(name);
    }

    @Programmatic
    public List<Cliente> autoCompleteCliente(final String name) {
        return clienteService.findClientesByName(name);

    }

    public SortedSet<ApplicationUser> choices0ReportePorAbogado() {
        return consultarAbogados();
    }

    @Programmatic
    public SortedSet<ApplicationUser> consultarAbogados() {
        SortedSet<ApplicationUser> abogados = null;
        ApplicationRole rol = applicationRoles.findRoleByName(HojaDeTiempoService.ROL_ABOGADO);
        if (rol != null) {
            abogados = rol.getUsers();
        }
        return abogados;
    }

    @ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Cierre")
    @MemberOrder(sequence = "3")
    public void cierreMensual(
            @Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Desde") final java.sql.Date desde,
            @Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Hasta") final java.sql.Date hasta) {
        Date desdeAjustado = ajustadorFecha.ajustarFechaInicial(desde);
        Date hastaAjustado = ajustadorFecha.ajustarFechaFinal(hasta);
    }

    @ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Reporte por abogado")
    @MemberOrder(sequence = "2")
    public void reportePorAbogado(
            @Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Abogado") final ApplicationUser abogado,
            @Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Desde") final Date desde,
            @Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Hasta") final Date hasta) {
        Date desdeAjustado = ajustadorFecha.ajustarFechaInicial(desde);
        Date hastaAjustado = ajustadorFecha.ajustarFechaFinal(hasta);
    }

    @ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Reporte por cliente")
    @MemberOrder(sequence = "1")
    public Blob reportePorCliente(
            @Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Cliente") final Cliente cliente,
            @Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Desde") final Date desde,
            @Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Hasta") final Date hasta) {
        ReporteCliente rep = null;
        try {
            //java.text.SimpleDateFormat sdfDate = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");//dd/MM/yyyy
            //java.util.Date now = new java.util.Date();
            //String strDate = sdfDate.format(now);
            rep = new ReporteCliente(cliente.getId(), ajustadorFecha.ajustarFechaInicial(desde), ajustadorFecha.ajustarFechaFinal(hasta));
            rep.build();
            //byte[] ba = rep.buildBlob();
            //LOG.info("ba: " + ba.length);
            //return new Blob("reporte " + strDate + ".pdf", "application/pdf", ba);
            return rep.buildBlob();
        } catch (SQLException | IOException | DRException e) {
            e.printStackTrace();
        }
        return null;
    }
}