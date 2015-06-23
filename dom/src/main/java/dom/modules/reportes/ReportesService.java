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

@DomainService()
@DomainServiceLayout(named = "Procesos", menuBar = DomainServiceLayout.MenuBar.PRIMARY, menuOrder = "2")
public class ReportesService extends AbstractFactoryAndRepository {

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
    public void reportePorCliente(
            @Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Cliente") final Cliente cliente,
            @Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Desde") final Date desde,
            @Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Hasta") final Date hasta) {
        try {
            java.sql.Date desdeAjustado = ajustadorFecha.ajustarFechaInicial(desde);
            java.sql.Date hastaAjustado = ajustadorFecha.ajustarFechaFinal(hasta);
            ReporteCliente rep = new ReporteCliente(cliente.getId(), desde, hasta);
            rep.build();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}