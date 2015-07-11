package dom.modules.reportes;

import dom.modules.BaseService;
import dom.modules.clientes.Cliente;
import dom.modules.clientes.ClienteService;
import dom.modules.hoja.HojaDeTiempoService;
import net.sf.dynamicreports.report.exception.DRException;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.value.Blob;
import org.isisaddons.module.security.dom.role.ApplicationRole;
import org.isisaddons.module.security.dom.role.ApplicationRoles;
import org.isisaddons.module.security.dom.user.ApplicationUser;

import javax.inject.Inject;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

@DomainService()
@DomainServiceLayout(named = "Reportes", menuBar = DomainServiceLayout.MenuBar.PRIMARY, menuOrder = "2")
public class ReportesService extends BaseService {

	@Inject
	ApplicationRoles applicationRoles;
	@Inject
	ClienteService clienteService;
	@Inject
	IdiomaService idiomaService;

	public List<Cliente> autoComplete0ReportePorCliente(final String name) {
		return autoCompleteCliente(name);
	}

	public List<Cliente> autoComplete0ReportePorClienteYCaso(final String name) {
		return autoCompleteCliente(name);
	}

	@Programmatic
	public List<Cliente> autoCompleteCliente(final String name) {
		return clienteService.findClientesByName(name);
	}

	public SortedSet<ApplicationUser> choices0ReportePorAbogado() {
		return consultarAbogados();
	}

	public SortedSet<Mes> choices1ReportePorClienteYCaso(final Cliente cliente) {
		if (cliente != null) {
			return Mes.getMeses(); //cliente);
		} else {
			return Mes.getMeses();
		}
	}

	public SortedSet<String> choices2ReportePorClienteYCaso(final Cliente cliente, final Mes mes) {
		SortedSet<String> lista = null;
		if (cliente != null && mes != null) {
			Date fechaMes = mes.getFecha();
			lista = new TreeSet<String>(getListaCasos(cliente, fechaMes));
		}
		return lista;
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

	public Idioma default3ReportePorAbogado() {
		return getIdiomaDefault();
	}

	public Idioma default3ReportePorCliente() {
		return getIdiomaDefault();
	}

	public Idioma default3ReportePorClienteYCaso() {
		return getIdiomaDefault();
	}

	@Programmatic
	public Idioma getIdiomaDefault() {
		return idiomaService.getIdiomaDefault();
	}

	@Programmatic
	public List<String> getListaCasos(Cliente cliente, java.util.Date desde) {
		java.util.Date hasta = ajustadorFecha.getUltimoDia(new java.sql.Date(desde.getTime()));
		return clienteService.getCasos(cliente, desde, hasta);
	}

	@ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Reporte por abogado")
	@MemberOrder(sequence = "2")
	public Blob reportePorAbogado(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Abogado") final ApplicationUser
					abogado,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Desde") final Date desde,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Hasta") final Date hasta,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Idioma") final Idioma idioma) {
		Date desdeAjustado = ajustadorFecha.ajustarFechaInicial(desde);
		Date hastaAjustado = ajustadorFecha.ajustarFechaFinal(hasta);
		ReporteAbogado rep = null;
		try {
			rep = new ReporteAbogado(idioma, abogado, ajustadorFecha.ajustarFechaInicial(desde), ajustadorFecha
					.ajustarFechaFinal(hasta));
			rep.build();
			return rep.buildBlob();
		} catch (SQLException | IOException | DRException e) {
			e.printStackTrace();
		}
		return null;
	}

	@ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Reporte por cliente")
	@MemberOrder(sequence = "1")
	public Blob reportePorCliente(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Cliente") final Cliente cliente,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Desde") final Date desde,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Hasta") final Date hasta,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Idioma") final Idioma idioma) {
		ReporteCliente rep = null;
		try {
			rep = new ReporteCliente(idioma, cliente, ajustadorFecha.ajustarFechaInicial(desde), ajustadorFecha
					.ajustarFechaFinal(hasta));
			rep.build();
			return rep.buildBlob();
		} catch (SQLException | IOException | DRException e) {
			e.printStackTrace();
		}
		return null;
	}

	@ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Reporte por cliente y caso")
	@MemberOrder(sequence = "1")
	public Blob reportePorClienteYCaso(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Cliente") final Cliente cliente,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Mes") final Mes mes,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Caso") final String caso,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Idioma") final Idioma idioma) {
		ReporteClienteCaso rep = null;
		try {
			Date desde = ajustadorFecha.ajustarFechaInicial(mes.getFecha());
			Date hasta = ajustadorFecha.ajustarFechaInicial(ajustadorFecha.getUltimoDia(desde));
			rep = new ReporteClienteCaso(idioma, cliente, desde, hasta, caso);
			rep.build();
			return rep.buildBlob();
		} catch (SQLException | IOException | DRException e) {
			e.printStackTrace();
		}
		return null;
	}

}