package dom.modules.hoja;

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
import java.util.*;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Queries({
        @Query(name = "find", language = "JDOQL", value = "SELECT FROM hojadetiempo.dom.modules.hoja.HojaDeTiempo "),
        @Query(name = "findByAbogadoyFecha", language = "JDOQL", value = "SELECT FROM dom.modules.hoja.HojaDeTiempo WHERE abogado == :abogado && fecha >= :desde && fecha <= :hasta "),
        @Query(name = "findPendientesByAbogado", language = "JDOQL", value = "SELECT FROM dom.modules.hoja.HojaDeTiempo WHERE abogado == :abogado && (cartaDA == null || caso == null || formaServicio == null || solicitadoPor == null)"),
        @Query(name = "findPendientes", language = "JDOQL", value = "SELECT FROM dom.modules.hoja.HojaDeTiempo WHERE cartaDA == null || caso == null || formaServicio == null || solicitadoPor == null")})
@DomainObject(objectType = "HojaDeTiempo")
@DomainObjectLayout(named = "Hoja de tiempo", plural = "Hojas de tiempo")
@MemberGroupLayout(columnSpans = {6, 0, 6, 0}, left = {"Izquierda"}, right = {"Derecha"})
public class HojaDeTiempo extends AbstractDomainObject implements
        Comparable<HojaDeTiempo> {

    @Inject
    ApplicationRoles applicationRoles;
    @Inject
    ClienteService clienteService;
    private ApplicationUser abogado;
    private Cliente cliente;
    private double montoTarifa;
    private FormaServicio formaServicio;
    private Integer horasFacturables;
    private Integer horasReales;
    private Integer minutosFacturables;
    private Integer minutosReales;
    private java.sql.Date fecha;
    private String cartaDA;
    private String caso;
    private String formaServicioOtros;
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
        ApplicationRole rol = applicationRoles.findRoleByName("rol-abogado");
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
    @MemberOrder(name = "Derecha", sequence = "1")
    public ApplicationUser getAbogado() {
        return abogado;
    }

    public void setAbogado(ApplicationUser abogado) {
        this.abogado = abogado;
    }

    @Column(allowsNull = "true")
    @MemberOrder(name = "Izquierda", sequence = "5")
    @PropertyLayout(hidden = Where.STANDALONE_TABLES)
    public String getCartaDA() {
        return cartaDA;
    }

    public void setCartaDA(String cartaDA) {
        this.cartaDA = cartaDA;
    }

    @Column(allowsNull = "false")
    @MemberOrder(name = "Derecha", sequence = "2")
    @PropertyLayout(hidden = Where.STANDALONE_TABLES)
    public String getCaso() {
        return caso;
    }

    public void setCaso(String caso) {
        this.caso = caso;
    }

    @Column(allowsNull = "false")
    @MemberOrder(name = "Izquierda", sequence = "2")
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Column(allowsNull = "false")
    @MemberOrder(name = "Izquierda", sequence = "1")
    public java.sql.Date getFecha() {
        return fecha;
    }

    public void setFecha(java.sql.Date fecha) {
        this.fecha = fecha;
    }

    @Column(allowsNull = "false")
    @MemberOrder(name = "Izquierda", sequence = "4")
    @PropertyLayout(named = "Servicio prestado por")
    public FormaServicio getFormaServicio() {
        return formaServicio;
    }

    public void setFormaServicio(FormaServicio formaServicio) {
        this.formaServicio = formaServicio;
    }

    @Column(allowsNull = "true")
    @MemberOrder(name = "Izquierda", sequence = "6")
    @PropertyLayout(named = "Servicio prestado (Otros)", hidden = Where.STANDALONE_TABLES)
    public String getFormaServicioOtros() {
        return formaServicioOtros;
    }

    public void setFormaServicioOtros(String formaServicioOtros) {
        this.formaServicioOtros = formaServicioOtros;
    }

    @Column(allowsNull = "false")
    @MemberOrder(name = "Izquierda", sequence = "13")
    @PropertyLayout(named = "Horas Facturables")
    public Integer getHorasFacturables() {
        return horasFacturables;
    }

    public void setHorasFacturables(Integer horasFacturables) {
        this.horasFacturables = horasFacturables;
    }

    @Column(allowsNull = "false")
    @MemberOrder(name = "Izquierda", sequence = "14")
    @PropertyLayout(named = "Minutos Facturables")
    public int getMinutosFacturables() {
        return minutosFacturables;
    }

    public void setMinutosFacturables(int minutosFacturables) {
        this.minutosFacturables = minutosFacturables;
    }

    @Column(allowsNull = "true")
    @MemberOrder(name = "Izquierda", sequence = "7")
    @PropertyLayout(named = "Monto Tarifa")
    public double getMontoTarifa() {
        return montoTarifa;
    }

    public void setMontoTarifa(double montoTarifa) {
        this.montoTarifa = montoTarifa;
    }

    @Column(allowsNull = "true")
    @MemberOrder(name = "Izquierda", sequence = "8")
    @PropertyLayout(named = "Presupuesto No.")
    public String getNroPresupuesto() {
        return nroPresupuesto;
    }

    public void setNroPresupuesto(String nroPresupuesto) {
        this.nroPresupuesto = nroPresupuesto;
    }

    @Column(allowsNull = "true")
    @MemberOrder(name = "Izquierda", sequence = "9")
    @PropertyLayout(named = "Observaciones", hidden = Where.STANDALONE_TABLES)
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Column(allowsNull = "false")
    @MemberOrder(name = "Derecha", sequence = "3")
    @PropertyLayout(multiLine = 10, named = "Servicio prestado", typicalLength = 250, labelPosition = LabelPosition.TOP, hidden = Where.STANDALONE_TABLES)
    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    @Column(allowsNull = "false")
    @MemberOrder(name = "Izquierda", sequence = "2")
    @PropertyLayout(hidden = Where.STANDALONE_TABLES)
    public String getSolicitadoPor() {
        return solicitadoPor;
    }

    public void setSolicitadoPor(String solicitadoPor) {
        this.solicitadoPor = solicitadoPor;
    }

    @Column(allowsNull = "true")
    @PropertyLayout(hidden = Where.STANDALONE_TABLES)
    @MemberOrder(name = "Izquierda", sequence = "7")
    public Tarifa getTarifa() {
        return tarifa;
    }

    public void setTarifa(Tarifa tarifa) {
        this.tarifa = tarifa;
    }

    @Column(allowsNull = "false")
    @MemberOrder(name = "Izquierda", sequence = "12")
    @PropertyLayout(named = "Horas", hidden = Where.STANDALONE_TABLES)
    public Integer getHorasReales() {
        return horasReales;
    }

    public void setHorasReales(Integer horasReales) {
        this.horasReales = horasReales;
    }

    @Column(allowsNull = "false")
    @MemberOrder(name = "Izquierda", sequence = "13")
    @PropertyLayout(named = "Minutos", hidden = Where.STANDALONE_TABLES)
    public Integer getMinutosReales() {
        return minutosReales;
    }

    public void setMinutosReales(Integer minutosReales) {
        this.minutosReales = minutosReales;
    }

    @Column(allowsNull = "true")
    @MemberOrder(name = "Izquierda", sequence = "10")
    @PropertyLayout(named = "Cobranza")
    public TipoCobranza getTipoCobranza() {
        return tipoCobranza;
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
