package dom.modules.hoja;

import org.datanucleus.query.typesafe.*;
import org.datanucleus.api.jdo.query.*;

public class QHojaDeTiempo extends PersistableExpressionImpl<HojaDeTiempo> implements PersistableExpression<HojaDeTiempo>
{
    public static final QHojaDeTiempo jdoCandidate = candidate("this");

    public static QHojaDeTiempo candidate(String name)
    {
        return new QHojaDeTiempo(null, name, 5);
    }

    public static QHojaDeTiempo candidate()
    {
        return jdoCandidate;
    }

    public static QHojaDeTiempo parameter(String name)
    {
        return new QHojaDeTiempo(HojaDeTiempo.class, name, ExpressionType.PARAMETER);
    }

    public static QHojaDeTiempo variable(String name)
    {
        return new QHojaDeTiempo(HojaDeTiempo.class, name, ExpressionType.VARIABLE);
    }

    public final org.isisaddons.module.security.dom.user.QApplicationUser abogado;
    public final ObjectExpression<org.isisaddons.module.security.dom.role.ApplicationRoles> applicationRoles;
    public final StringExpression cartaDA;
    public final StringExpression caso;
    public final dom.modules.clientes.QCliente cliente;
    public final ObjectExpression<dom.modules.clientes.ClienteService> clienteService;
    public final DateExpression fecha;
    public final dom.modules.tablas.QFormaServicio formaServicio;
    public final StringExpression formaServicioOtros;
    public final ObjectExpression<java.lang.Integer> horasFacturables;
    public final ObjectExpression<java.lang.Integer> horasReales;
    public final ObjectExpression<java.lang.Integer> minutosFacturables;
    public final ObjectExpression<java.lang.Integer> minutosReales;
    public final NumericExpression<Double> montoTarifa;
    public final StringExpression nroPresupuesto;
    public final StringExpression observaciones;
    public final StringExpression servicio;
    public final StringExpression solicitadoPor;
    public final dom.modules.tablas.QTarifa tarifa;
    public final dom.modules.tablas.QTipoCobranza tipoCobranza;

    public QHojaDeTiempo(PersistableExpression parent, String name, int depth)
    {
        super(parent, name);
        if (depth > 0)
        {
            this.abogado = new org.isisaddons.module.security.dom.user.QApplicationUser(this, "abogado", depth-1);
        }
        else
        {
            this.abogado = null;
        }
        this.applicationRoles = new ObjectExpressionImpl<org.isisaddons.module.security.dom.role.ApplicationRoles>(this, "applicationRoles");
        this.cartaDA = new StringExpressionImpl(this, "cartaDA");
        this.caso = new StringExpressionImpl(this, "caso");
        if (depth > 0)
        {
            this.cliente = new dom.modules.clientes.QCliente(this, "cliente", depth-1);
        }
        else
        {
            this.cliente = null;
        }
        this.clienteService = new ObjectExpressionImpl<dom.modules.clientes.ClienteService>(this, "clienteService");
        this.fecha = new DateExpressionImpl(this, "fecha");
        if (depth > 0)
        {
            this.formaServicio = new dom.modules.tablas.QFormaServicio(this, "formaServicio", depth-1);
        }
        else
        {
            this.formaServicio = null;
        }
        this.formaServicioOtros = new StringExpressionImpl(this, "formaServicioOtros");
        this.horasFacturables = new ObjectExpressionImpl<java.lang.Integer>(this, "horasFacturables");
        this.horasReales = new ObjectExpressionImpl<java.lang.Integer>(this, "horasReales");
        this.minutosFacturables = new ObjectExpressionImpl<java.lang.Integer>(this, "minutosFacturables");
        this.minutosReales = new ObjectExpressionImpl<java.lang.Integer>(this, "minutosReales");
        this.montoTarifa = new NumericExpressionImpl<Double>(this, "montoTarifa");
        this.nroPresupuesto = new StringExpressionImpl(this, "nroPresupuesto");
        this.observaciones = new StringExpressionImpl(this, "observaciones");
        this.servicio = new StringExpressionImpl(this, "servicio");
        this.solicitadoPor = new StringExpressionImpl(this, "solicitadoPor");
        if (depth > 0)
        {
            this.tarifa = new dom.modules.tablas.QTarifa(this, "tarifa", depth-1);
        }
        else
        {
            this.tarifa = null;
        }
        if (depth > 0)
        {
            this.tipoCobranza = new dom.modules.tablas.QTipoCobranza(this, "tipoCobranza", depth-1);
        }
        else
        {
            this.tipoCobranza = null;
        }
    }

    public QHojaDeTiempo(Class type, String name, ExpressionType exprType)
    {
        super(type, name, exprType);
        this.abogado = new org.isisaddons.module.security.dom.user.QApplicationUser(this, "abogado", 5);
        this.applicationRoles = new ObjectExpressionImpl<org.isisaddons.module.security.dom.role.ApplicationRoles>(this, "applicationRoles");
        this.cartaDA = new StringExpressionImpl(this, "cartaDA");
        this.caso = new StringExpressionImpl(this, "caso");
        this.cliente = new dom.modules.clientes.QCliente(this, "cliente", 5);
        this.clienteService = new ObjectExpressionImpl<dom.modules.clientes.ClienteService>(this, "clienteService");
        this.fecha = new DateExpressionImpl(this, "fecha");
        this.formaServicio = new dom.modules.tablas.QFormaServicio(this, "formaServicio", 5);
        this.formaServicioOtros = new StringExpressionImpl(this, "formaServicioOtros");
        this.horasFacturables = new ObjectExpressionImpl<java.lang.Integer>(this, "horasFacturables");
        this.horasReales = new ObjectExpressionImpl<java.lang.Integer>(this, "horasReales");
        this.minutosFacturables = new ObjectExpressionImpl<java.lang.Integer>(this, "minutosFacturables");
        this.minutosReales = new ObjectExpressionImpl<java.lang.Integer>(this, "minutosReales");
        this.montoTarifa = new NumericExpressionImpl<Double>(this, "montoTarifa");
        this.nroPresupuesto = new StringExpressionImpl(this, "nroPresupuesto");
        this.observaciones = new StringExpressionImpl(this, "observaciones");
        this.servicio = new StringExpressionImpl(this, "servicio");
        this.solicitadoPor = new StringExpressionImpl(this, "solicitadoPor");
        this.tarifa = new dom.modules.tablas.QTarifa(this, "tarifa", 5);
        this.tipoCobranza = new dom.modules.tablas.QTipoCobranza(this, "tipoCobranza", 5);
    }
}
