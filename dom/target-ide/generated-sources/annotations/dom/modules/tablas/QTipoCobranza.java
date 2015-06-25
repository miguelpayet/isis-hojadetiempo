package dom.modules.tablas;

import org.datanucleus.query.typesafe.*;
import org.datanucleus.api.jdo.query.*;

public class QTipoCobranza extends PersistableExpressionImpl<TipoCobranza> implements PersistableExpression<TipoCobranza>
{
    public static final QTipoCobranza jdoCandidate = candidate("this");

    public static QTipoCobranza candidate(String name)
    {
        return new QTipoCobranza(null, name, 5);
    }

    public static QTipoCobranza candidate()
    {
        return jdoCandidate;
    }

    public static QTipoCobranza parameter(String name)
    {
        return new QTipoCobranza(TipoCobranza.class, name, ExpressionType.PARAMETER);
    }

    public static QTipoCobranza variable(String name)
    {
        return new QTipoCobranza(TipoCobranza.class, name, ExpressionType.VARIABLE);
    }

    public final ObjectExpression<org.apache.isis.applib.DomainObjectContainer> container;
    public final StringExpression nombre;

    public QTipoCobranza(PersistableExpression parent, String name, int depth)
    {
        super(parent, name);
        this.container = new ObjectExpressionImpl<org.apache.isis.applib.DomainObjectContainer>(this, "container");
        this.nombre = new StringExpressionImpl(this, "nombre");
    }

    public QTipoCobranza(Class type, String name, ExpressionType exprType)
    {
        super(type, name, exprType);
        this.container = new ObjectExpressionImpl<org.apache.isis.applib.DomainObjectContainer>(this, "container");
        this.nombre = new StringExpressionImpl(this, "nombre");
    }
}
