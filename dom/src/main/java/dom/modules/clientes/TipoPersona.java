package dom.modules.clientes;

import java.util.ArrayList;
import java.util.List;

public class TipoPersona {

    public static final String NATURAL = "Natural";
    public static final String JURIDICA = "Jurídica";

    public static List<String> getTipos() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(JURIDICA);
        list.add(NATURAL);
        return list;
    }
}
