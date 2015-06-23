package dom.modules.tablas;

import org.datanucleus.query.typesafe.*;
import org.datanucleus.api.jdo.query.*;

public class QFormaServicio extends PersistableExpressionImpl<FormaServicio> implements PersistableExpression<FormaServicio>
{
    public static final QFormaServicio jdoCandidate = candidate("this");

    public static QFormaServicio candidate(String name)
    {
        return new QFormaServicio(null, name, 5);
    }

    public static QFormaServicio candidate()
    {
        return jdoCandidate;
    }

    public static QFormaServicio parameter(String name)
    {
        return new QFormaServicio(FormaServicio.class, name, ExpressionType.PARAMETER);
    }

    public static QFormaServicio variable(String name)
    {
        return new QFormaServicio(FormaServicio.class, name, ExpressionType.VARIABLE);
    }

    public final StringExpression OTROS;
    public final StringExpression nombre;

    public QFormaServicio(PersistableExpression parent, String name, int depth)
    {
        super(parent, name);
        this.OTROS = new StringExpressionImpl(this, "OTROS");
        this.nombre = new StringExpressionImpl(this, "nombre");
    }

    public QFormaServicio(Class type, String name, ExpressionType exprType)
    {
        super(type, name, exprType);
        this.OTROS = new StringExpressionImpl(this, "OTROS");
        this.nombre = new StringExpressionImpl(this, "nombre");
    }
}
