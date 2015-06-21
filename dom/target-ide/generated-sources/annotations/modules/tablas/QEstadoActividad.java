package modules.tablas;

import dom.modules.tablas.EstadoActividad;
import org.datanucleus.query.typesafe.*;
import org.datanucleus.api.jdo.query.*;

public class QEstadoActividad extends PersistableExpressionImpl<EstadoActividad> implements PersistableExpression<EstadoActividad>
{
    public static final QEstadoActividad jdoCandidate = candidate("this");

    public static QEstadoActividad candidate(String name)
    {
        return new QEstadoActividad(null, name, 5);
    }

    public static QEstadoActividad candidate()
    {
        return jdoCandidate;
    }

    public static QEstadoActividad parameter(String name)
    {
        return new QEstadoActividad(EstadoActividad.class, name, ExpressionType.PARAMETER);
    }

    public static QEstadoActividad variable(String name)
    {
        return new QEstadoActividad(EstadoActividad.class, name, ExpressionType.VARIABLE);
    }

    public final ObjectExpression<org.apache.isis.applib.DomainObjectContainer> container;
    public final StringExpression nombre;

    public QEstadoActividad(PersistableExpression parent, String name, int depth)
    {
        super(parent, name);
        this.container = new ObjectExpressionImpl<org.apache.isis.applib.DomainObjectContainer>(this, "container");
        this.nombre = new StringExpressionImpl(this, "nombre");
    }

    public QEstadoActividad(Class type, String name, ExpressionType exprType)
    {
        super(type, name, exprType);
        this.container = new ObjectExpressionImpl<org.apache.isis.applib.DomainObjectContainer>(this, "container");
        this.nombre = new StringExpressionImpl(this, "nombre");
    }
}
