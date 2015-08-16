package dom.modules.hoja;

import dom.modules.clientes.Caso;
import dom.modules.clientes.Cliente;
import dom.modules.clientes.ClienteService;
import dom.modules.tablas.FormaServicio;
import dom.modules.tablas.Tarifa;
import dom.modules.tablas.TipoCobranza;
import org.apache.isis.applib.AbstractDomainObject;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;
import org.isisaddons.module.security.dom.role.ApplicationRole;
import org.isisaddons.module.security.dom.role.ApplicationRoles;
import org.isisaddons.module.security.dom.user.ApplicationUser;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.VersionStrategy;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Queries({
		@Query(name = "find", language = "JDOQL", value = "SELECT FROM hojadetiempo.dom.modules.hoja.HojaDeTiempo "),
		@Query(name = "findByAbogadoyFecha", language = "JDOQL", value = "SELECT FROM dom.modules.hoja.HojaDeTiempo " +
				"WHERE abogado == :abogado && fecha >= :desde && fecha <= :hasta "),
		@Query(name = "findPendientesByAbogado", language = "JDOQL", value = "SELECT FROM dom.modules.hoja" +
				".HojaDeTiempo" +
				" " +
				"WHERE abogado == :abogado && (cartaDA == null || caso == null || formaServicio == null || solicitadoPor" +
				" " +
				"== null)"),
		@Query(name = "findPendientes", language = "JDOQL", value = "SELECT FROM dom.modules.hoja.HojaDeTiempo WHERE " +
				"cartaDA == null || caso == null || formaServicio == null || solicitadoPor == null")})
@DomainObject(objectType = "HojaDeTiempo")
@DomainObjectLayout(named = "Hoja de tiempo", plural = "Hojas de tiempo")
@MemberGroupLayout(columnSpans = {6, 0, 6, 0}, left = {"Detalles"}, right = {"Caso", "Facturación"})
public class HojaDeTiempo extends AbstractDomainObject implements
		Comparable<HojaDeTiempo> {

	private ApplicationUser abogado;
	@Inject
	ApplicationRoles applicationRoles;
	private String cartaDA;
	private Caso caso;
	private Cliente cliente;
	@Inject
	ClienteService clienteService;
	private java.sql.Date fecha;
	private FormaServicio formaServicio;
	private String formaServicioOtros;
	private Integer horasFacturables;
	private Integer horasReales;
	private Integer minutosFacturables;
	private Integer minutosReales;
	private double montoTarifa;
	private String nroPresupuesto;
	private String observaciones;
	private String servicio;
	private String solicitadoPor;
	private Tarifa tarifa;
	private TipoCobranza tipoCobranza;

	public List<Cliente> autoCompleteCliente(final String name) {
		return clienteService.findClientesByName(name);
	}

	public SortedSet<ApplicationUser> choicesAbogado() {
		SortedSet<ApplicationUser> abogados = null;
		ApplicationRole rol = applicationRoles.findRoleByName(HojaDeTiempoService.ROL_ABOGADO);
		if (rol != null) {
			abogados = rol.getUsers();
		}
		return abogados;
	}

	public List<FormaServicio> choicesFormaServicio() {
		List<FormaServicio> servicios = allMatches(new QueryDefault<>(
				FormaServicio.class, "listarPorNombre"));
		Collections.sort(servicios, new Comparator<FormaServicio>() {
			public int compare(FormaServicio servicio1, FormaServicio servicio2) {
				return servicio1.getNombre().compareTo(servicio2.getNombre());
			}
		});
		return servicios;
	}

	public List<Tarifa> choicesTarifa() {
		List<Tarifa> tarifas = allMatches(new QueryDefault<>(Tarifa.class,
				"listarPorNombre"));
		Collections.sort(tarifas, new Comparator<Tarifa>() {
			public int compare(Tarifa tarifa1, Tarifa tarifa2) {
				return tarifa1.getNombre().compareTo(tarifa2.getNombre());
			}
		});
		return tarifas;
	}

	@Override
	public int compareTo(HojaDeTiempo o) {
		return 0;
	}

	@Column(allowsNull = "false")
	@MemberOrder(name = "Caso", sequence = "1")
	public ApplicationUser getAbogado() {
		return abogado;
	}

	@Column(allowsNull = "true")
	@MemberOrder(name = "Detalles", sequence = "5")
	@PropertyLayout(hidden = Where.STANDALONE_TABLES)
	public String getCartaDA() {
		return cartaDA;
	}

	@Column(allowsNull = "false")
	@MemberOrder(name = "Caso", sequence = "2")
	@PropertyLayout(named = "Referencia")
	public Caso getCaso() {
		return caso;
	}

	@Column(allowsNull = "false")
	@MemberOrder(name = "Detalles", sequence = "2")
	public Cliente getCliente() {
		return cliente;
	}

	@Column(allowsNull = "false")
	@MemberOrder(name = "Detalles", sequence = "1")
	public java.sql.Date getFecha() {
		return fecha;
	}

	@Column(allowsNull = "false")
	@MemberOrder(name = "Detalles", sequence = "4")
	@PropertyLayout(named = "Servicio prestado por")
	public FormaServicio getFormaServicio() {
		return formaServicio;
	}

	@Column(allowsNull = "true")
	@MemberOrder(name = "Detalles", sequence = "6")
	@PropertyLayout(named = "Servicio prestado (Otros)", hidden = Where.STANDALONE_TABLES)
	public String getFormaServicioOtros() {
		return formaServicioOtros;
	}

	@Column(allowsNull = "false")
	@MemberOrder(name = "Detalles", sequence = "14")
	@PropertyLayout(named = "Horas Facturables")
	public Integer getHorasFacturables() {
		return horasFacturables;
	}

	@Column(allowsNull = "false")
	@MemberOrder(name = "Detalles", sequence = "12")
	@PropertyLayout(named = "Horas Reales", hidden = Where.STANDALONE_TABLES)
	public Integer getHorasReales() {
		return horasReales;
	}

	@Column(allowsNull = "false")
	@MemberOrder(name = "Detalles", sequence = "15")
	@PropertyLayout(named = "Minutos Facturables")
	public int getMinutosFacturables() {
		return minutosFacturables;
	}

	@Column(allowsNull = "false")
	@MemberOrder(name = "Detalles", sequence = "13")
	@PropertyLayout(named = "Minutos Reales", hidden = Where.STANDALONE_TABLES)
	public Integer getMinutosReales() {
		return minutosReales;
	}

	@Column(allowsNull = "true")
	@MemberOrder(name = "Detalles", sequence = "7")
	@PropertyLayout(named = "Monto Tarifa", hidden = Where.STANDALONE_TABLES)
	public double getMontoTarifa() {
		return montoTarifa;
	}

	@Column(allowsNull = "true")
	@MemberOrder(name = "Detalles", sequence = "8")
	@PropertyLayout(named = "Presupuesto No.", hidden = Where.STANDALONE_TABLES)
	public String getNroPresupuesto() {
		return nroPresupuesto;
	}

	@Column(allowsNull = "true")
	@MemberOrder(name = "Detalles", sequence = "9")
	@PropertyLayout(named = "Observaciones", hidden = Where.STANDALONE_TABLES)
	public String getObservaciones() {
		return observaciones;
	}

	@Column(allowsNull = "false")
	@MemberOrder(name = "Caso", sequence = "3")
	@PropertyLayout(multiLine = 10, named = "Servicio prestado", typicalLength = 250, labelPosition = LabelPosition
			.TOP, hidden = Where.STANDALONE_TABLES)
	public String getServicio() {
		return servicio;
	}

	@Column(allowsNull = "false")
	@MemberOrder(name = "Detalles", sequence = "2")
	@PropertyLayout(hidden = Where.STANDALONE_TABLES)
	public String getSolicitadoPor() {
		return solicitadoPor;
	}

	@Column(allowsNull = "true")
	@PropertyLayout(hidden = Where.STANDALONE_TABLES)
	@MemberOrder(name = "Detalles", sequence = "7")
	public Tarifa getTarifa() {
		return tarifa;
	}

	@Column(allowsNull = "true")
	@MemberOrder(name = "Detalles", sequence = "10")
	@PropertyLayout(named = "Cobranza", hidden = Where.STANDALONE_TABLES)
	public TipoCobranza getTipoCobranza() {
		return tipoCobranza;
	}

	public void setAbogado(ApplicationUser abogado) {
		this.abogado = abogado;
	}

	public void setCartaDA(String cartaDA) {
		this.cartaDA = cartaDA;
	}

	public void setCaso(Caso caso) {
		this.caso = caso;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setFecha(java.sql.Date fecha) {
		this.fecha = fecha;
	}

	public void setFormaServicio(FormaServicio formaServicio) {
		this.formaServicio = formaServicio;
	}

	public void setFormaServicioOtros(String formaServicioOtros) {
		this.formaServicioOtros = formaServicioOtros;
	}

	public void setHorasFacturables(Integer horasFacturables) {
		this.horasFacturables = horasFacturables;
	}

	public void setHorasReales(Integer horasReales) {
		this.horasReales = horasReales;
	}

	public void setMinutosFacturables(int minutosFacturables) {
		this.minutosFacturables = minutosFacturables;
	}

	public void setMinutosReales(Integer minutosReales) {
		this.minutosReales = minutosReales;
	}

	public void setMontoTarifa(double montoTarifa) {
		this.montoTarifa = montoTarifa;
	}

	public void setNroPresupuesto(String nroPresupuesto) {
		this.nroPresupuesto = nroPresupuesto;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public void setSolicitadoPor(String solicitadoPor) {
		this.solicitadoPor = solicitadoPor;
	}

	public void setTarifa(Tarifa tarifa) {
		this.tarifa = tarifa;
	}

	public void setTipoCobranza(TipoCobranza tipoCobranza) {
		this.tipoCobranza = tipoCobranza;
	}

	public String title() {
		final StringBuilder buf = new StringBuilder();
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		buf.append(getAbogado().getName());
		buf.append(" - ");
		buf.append(getCliente().getNombre());
		buf.append(" - ");
		buf.append(sdf.format(getFecha()));
		return buf.toString();
	}

	public String validateFormaServicio(FormaServicio forma) {
		return "";
	}

}
