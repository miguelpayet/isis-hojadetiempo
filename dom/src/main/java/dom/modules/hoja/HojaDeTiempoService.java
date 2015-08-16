package dom.modules.hoja;

import dom.modules.clientes.Caso;
import dom.modules.clientes.Cliente;
import dom.modules.clientes.ClienteService;
import dom.modules.tablas.FormaServicio;
import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.security.RoleMemento;
import org.apache.isis.applib.security.UserMemento;
import org.isisaddons.module.security.app.user.MeService;
import org.isisaddons.module.security.dom.role.ApplicationRole;
import org.isisaddons.module.security.dom.role.ApplicationRoles;
import org.isisaddons.module.security.dom.user.ApplicationUser;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

@DomainService(repositoryFor = HojaDeTiempo.class)
@DomainServiceLayout(named = "Hoja de Tiempo", menuBar = DomainServiceLayout.MenuBar.PRIMARY, menuOrder = "1")
public class HojaDeTiempoService extends AbstractFactoryAndRepository {

	public static String ROL_ABOGADO = "isis-module-security-regular-user";
	public static String ROL_ADMIN = "isis-module-security-admin";
	private final AjustadorFecha ajustadorFecha = new AjustadorFecha();

	@Inject
	ApplicationRoles applicationRoles;
	@Inject
	ClienteService clienteService;
	@Inject
	DomainObjectContainer container;
	@Inject
	private MeService userService;

	public List<Cliente> autoComplete0RegistrarSinAbogado(final String name) {
		return autoCompleteCliente(name);
	}

	public List<Cliente> autoComplete1Registrar(final String name) {
		return autoCompleteCliente(name);
	}

	public List<Caso> autoComplete5Registrar(final ApplicationUser abogado, final Cliente cliente, final FormaServicio
			formaServicio) {
		if (cliente != null) {
			return autoCompleteCasos(cliente);
		} else {
			return null;
		}
	}

	@Programmatic
	public List<Caso> autoCompleteCasos(Cliente cliente) {
		return clienteService.getCasos(cliente);
	}

	@Programmatic
	public List<Cliente> autoCompleteCliente(final String name) {
		return clienteService.findClientesByName(name);

	}

	@Programmatic
	private List<HojaDeTiempo> buscarPendientes() {
		return container.allMatches(new QueryDefault<HojaDeTiempo>(
				HojaDeTiempo.class, "findPendientes"));
	}

	@Programmatic
	private List<HojaDeTiempo> buscarPendientesPorAbogado(
			ApplicationUser abogado) {
		return container.allMatches(new QueryDefault<HojaDeTiempo>(
				HojaDeTiempo.class, "findPendientesByAbogado", "abogado",
				abogado));
	}

	@Programmatic
	private List<HojaDeTiempo> buscarPorAbogadoyFecha(java.sql.Date desde,
	                                                  java.sql.Date hasta, ApplicationUser abogado) {
		Date desdeAjustado = ajustadorFecha.ajustarFechaInicial(desde);
		Date hastaAjustado = ajustadorFecha.ajustarFechaFinal(hasta);
		return container.allMatches(new QueryDefault<HojaDeTiempo>(
				HojaDeTiempo.class, "findByAbogadoyFecha", "abogado", abogado,
				"desde", desdeAjustado, "hasta", hastaAjustado));
	}

	public SortedSet<ApplicationUser> choices0Consultar() {
		return consultarAbogados();
	}

	public SortedSet<ApplicationUser> choices0Registrar() {
		return consultarAbogados();
	}

	public List<Integer> choices10Registrar() {
		return getListaMinutosFacturables();
	}

	public List<Integer> choices6RegistrarSinAbogado() {
		return getListaHorasReales();
	}

	public List<Integer> choices7Registrar() {
		return getListaHorasReales();
	}

	public List<Integer> choices7RegistrarSinAbogado() {
		return getListaMinutosReales();
	}

	public List<Integer> choices8Registrar() {
		return getListaMinutosReales();
	}

	public List<Integer> choices8RegistrarSinAbogado() {
		return getListaHorasFacturables();
	}

	public List<Integer> choices9Registrar() {
		return getListaHorasFacturables();
	}

	public List<Integer> choices9RegistrarSinAbogado() {
		return getListaMinutosFacturables();
	}

	@ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Consultar por abogado y fecha")
	@MemberOrder(sequence = "2")
	public List<HojaDeTiempo> consultar(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Abogado") final ApplicationUser
					abogado,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Desde") final java.sql.Date desde,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Hasta") final java.sql.Date hasta) {
		return buscarPorAbogadoyFecha(desde, hasta, abogado);
	}

	@Programmatic
	public SortedSet<ApplicationUser> consultarAbogados() {
		SortedSet<ApplicationUser> abogados = null;
		ApplicationRole rol = applicationRoles.findRoleByName(ROL_ABOGADO);
		if (rol != null) {
			abogados = rol.getUsers();
		}
		return abogados;
	}

	@ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Revisar hojas de tiempo pendientes")
	@MemberOrder(sequence = "3")
	public List<HojaDeTiempo> consultarPendientes() {
		return buscarPendientes();
	}

	@ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Revisar hojas de tiempo pendientes")
	@MemberOrder(sequence = "3")
	public List<HojaDeTiempo> consultarPendientesSinAbogado() {
		ApplicationUser abogado = userService.me();
		return buscarPendientesPorAbogado(abogado);
	}

	@ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Consultar por fecha")
	@MemberOrder(sequence = "2")
	public List<HojaDeTiempo> consultarSinAbogado(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Desde") final java.sql.Date desde,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Hasta") final java.sql.Date hasta) {
		ApplicationUser abogado = userService.me();
		return buscarPorAbogadoyFecha(desde, hasta, abogado);
	}

	@Programmatic
	private List<Integer> getListaHorasFacturables() {
		return Horas.getListaHorasFacturables();
	}

	@Programmatic
	private List<Integer> getListaHorasReales() {
		return Horas.getListaHorasReales();
	}

	@Programmatic
	private List<Integer> getListaMinutosFacturables() {
		return Minutos.getListaMinutosFacturables();
	}

	@Programmatic
	private List<Integer> getListaMinutosReales() {
		return Minutos.getListaMinutosReales();
	}

	public boolean hideConsultar() {
		return !usuarioEs(ROL_ADMIN);
	}

	public boolean hideConsultarPendientes() {
		return true; // !usuarioEs(ROL_ADMIN);
	}

	public boolean hideConsultarPendientesSinAbogado() {
		return true; // !usuarioEs(ROL_ABOGADO);
	}

	public boolean hideConsultarSinAbogado() {
		return !usuarioEs(ROL_ABOGADO);
	}

	public boolean hideRegistrar() {
		return !usuarioEs(ROL_ADMIN);
	}

	public boolean hideRegistrarSinAbogado() {
		return !usuarioEs(ROL_ABOGADO);
	}

	@ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Registrar")
	@MemberOrder(sequence = "1")
	public HojaDeTiempo registrar(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Abogado") final ApplicationUser
					abogado,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Cliente") final Cliente cliente,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Solicitado por") final String
					solicitadoPor,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Forma Servicio") final
			FormaServicio formaServicio,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Fecha") final java.sql.Date fecha,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Caso") final Caso caso,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Servicio Prestado", multiLine = 5)
			final String servicio,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Horas Reales") final Integer
					horasReales,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Minutos Reales") final Integer
					minutosReales,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Horas Facturables") final Integer
					horasFacturables,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Minutos Facturables") final Integer
					minutosFacturables) {
		final HojaDeTiempo obj = container
				.newTransientInstance(HojaDeTiempo.class);
		obj.setAbogado(abogado);
		obj.setCliente(cliente);
		obj.setSolicitadoPor(solicitadoPor);
		obj.setFormaServicio(formaServicio);
		obj.setFecha(fecha);
		obj.setCaso(caso);
		obj.setServicio(servicio);
		obj.setHorasReales(horasReales);
		obj.setMinutosReales(minutosReales);
		obj.setHorasFacturables(horasFacturables);
		obj.setMinutosFacturables(minutosFacturables);
		container.persistIfNotAlready(obj);
		return obj;
	}

	@ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Registrar hoja de tiempo")
	@MemberOrder(sequence = "1")
	public HojaDeTiempo registrarSinAbogado(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Cliente") final Cliente cliente,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Solicitado por") final String
					solicitadoPor,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Forma Servicio") final
			FormaServicio formaServicio,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Fecha") final java.sql.Date fecha,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Caso") final Caso caso,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Servicio Prestado", multiLine = 5)
			final String servicio,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Horas Reales") final Integer
					horasReales,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Minutos Reales") final Integer
					minutosReales,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Horas Facturables") final Integer
					horasFacturables,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Minutos Facturables") final Integer
					minutosFacturables) {
		final HojaDeTiempo obj = container
				.newTransientInstance(HojaDeTiempo.class);
		ApplicationUser user = userService.me();
		obj.setAbogado(user);
		obj.setCliente(cliente);
		obj.setSolicitadoPor(solicitadoPor);
		obj.setFormaServicio(formaServicio);
		obj.setFecha(fecha);
		obj.setCaso(caso);
		obj.setServicio(servicio);
		obj.setHorasReales(horasReales);
		obj.setMinutosReales(minutosReales);
		obj.setHorasFacturables(horasFacturables);
		obj.setMinutosFacturables(minutosFacturables);
		container.persistIfNotAlready(obj);
		return obj;
	}

	@Programmatic
	public boolean usuarioEs(String nombreRol) {
		Boolean esAbogado = false;
		UserMemento user = container.getUser();
		for (RoleMemento rol : user.getRoles()) {
			esAbogado = esAbogado || (rol.getName().indexOf(nombreRol) > -1);
		}
		return esAbogado;
	}

}
