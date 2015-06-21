package dom.modules.clientes;

import java.util.ArrayList;
import java.util.List;

public class TipoDocumento {

    public static final String RUC = "RUC";
    public static final String DNI = "DNI";

    public static List<String> getTipos() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(DNI);
        list.add(RUC);
        return list;
    }

}
