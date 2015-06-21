package dom.modules.tablas;

import org.datanucleus.query.typesafe.*;
import org.datanucleus.api.jdo.query.*;

public class QTarifa extends PersistableExpressionImpl<Tarifa> implements PersistableExpression<Tarifa>
{
    public static final QTarifa jdoCandidate = candidate("this");

    public static QTarifa candidate(String name)
    {
        return new QTarifa(null, name, 5);
    }

    public static QTarifa candidate()
    {
        return jdoCandidate;
    }

    public static QTarifa parameter(String name)
    {
        return new QTarifa(Tarifa.class, name, ExpressionType.PARAMETER);
    }

    public static QTarifa variable(String name)
    {
        return new QTarifa(Tarifa.class, name, ExpressionType.VARIABLE);
    }

    public final ObjectExpression<org.apache.isis.applib.DomainObjectContainer> container;
    public final StringExpression nombre;

    public QTarifa(PersistableExpression parent, String name, int depth)
    {
        super(parent, name);
        this.container = new ObjectExpressionImpl<org.apache.isis.applib.DomainObjectContainer>(this, "container");
        this.nombre = new StringExpressionImpl(this, "nombre");
    }

    public QTarifa(Class type, String name, ExpressionType exprType)
    {
        super(type, name, exprType);
        this.container = new ObjectExpressionImpl<org.apache.isis.applib.DomainObjectContainer>(this, "container");
        this.nombre = new StringExpressionImpl(this, "nombre");
    }
}
