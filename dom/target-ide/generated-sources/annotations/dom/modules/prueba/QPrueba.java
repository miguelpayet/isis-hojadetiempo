package dom.modules.prueba;

import org.datanucleus.query.typesafe.*;
import org.datanucleus.api.jdo.query.*;

public class QPrueba extends PersistableExpressionImpl<Prueba> implements PersistableExpression<Prueba>
{
    public static final QPrueba jdoCandidate = candidate("this");

    public static QPrueba candidate(String name)
    {
        return new QPrueba(null, name, 5);
    }

    public static QPrueba candidate()
    {
        return jdoCandidate;
    }

    public static QPrueba parameter(String name)
    {
        return new QPrueba(Prueba.class, name, ExpressionType.PARAMETER);
    }

    public static QPrueba variable(String name)
    {
        return new QPrueba(Prueba.class, name, ExpressionType.VARIABLE);
    }

    public final dom.modules.tablas.QFormaServicio serviceMode;
    public final StringExpression otherMode;

    public QPrueba(PersistableExpression parent, String name, int depth)
    {
        super(parent, name);
        if (depth > 0)
        {
            this.serviceMode = new dom.modules.tablas.QFormaServicio(this, "serviceMode", depth-1);
        }
        else
        {
            this.serviceMode = null;
        }
        this.otherMode = new StringExpressionImpl(this, "otherMode");
    }

    public QPrueba(Class type, String name, ExpressionType exprType)
    {
        super(type, name, exprType);
        this.serviceMode = new dom.modules.tablas.QFormaServicio(this, "serviceMode", 5);
        this.otherMode = new StringExpressionImpl(this, "otherMode");
    }
}
