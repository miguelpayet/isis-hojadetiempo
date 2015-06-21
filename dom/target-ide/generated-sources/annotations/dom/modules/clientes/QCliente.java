package dom.modules.clientes;

import org.datanucleus.query.typesafe.*;
import org.datanucleus.api.jdo.query.*;

public class QCliente extends PersistableExpressionImpl<Cliente> implements PersistableExpression<Cliente>
{
    public static final QCliente jdoCandidate = candidate("this");

    public static QCliente candidate(String name)
    {
        return new QCliente(null, name, 5);
    }

    public static QCliente candidate()
    {
        return jdoCandidate;
    }

    public static QCliente parameter(String name)
    {
        return new QCliente(Cliente.class, name, ExpressionType.PARAMETER);
    }

    public static QCliente variable(String name)
    {
        return new QCliente(Cliente.class, name, ExpressionType.VARIABLE);
    }

    public final StringExpression direccion;
    public final StringExpression nombre;
    public final StringExpression numDocumento;
    public final StringExpression telefono;
    public final StringExpression tipoDocumento;
    public final StringExpression tipoPersona;

    public QCliente(PersistableExpression parent, String name, int depth)
    {
        super(parent, name);
        this.direccion = new StringExpressionImpl(this, "direccion");
        this.nombre = new StringExpressionImpl(this, "nombre");
        this.numDocumento = new StringExpressionImpl(this, "numDocumento");
        this.telefono = new StringExpressionImpl(this, "telefono");
        this.tipoDocumento = new StringExpressionImpl(this, "tipoDocumento");
        this.tipoPersona = new StringExpressionImpl(this, "tipoPersona");
    }

    public QCliente(Class type, String name, ExpressionType exprType)
    {
        super(type, name, exprType);
        this.direccion = new StringExpressionImpl(this, "direccion");
        this.nombre = new StringExpressionImpl(this, "nombre");
        this.numDocumento = new StringExpressionImpl(this, "numDocumento");
        this.telefono = new StringExpressionImpl(this, "telefono");
        this.tipoDocumento = new StringExpressionImpl(this, "tipoDocumento");
        this.tipoPersona = new StringExpressionImpl(this, "tipoPersona");
    }
}
