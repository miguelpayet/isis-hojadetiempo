package dom.modules.hoja;

import java.util.ArrayList;
import java.util.List;

public class Horas {

    public static List<Integer> getListaHorasReales() {
        List<Integer> horas = new ArrayList<Integer>();
        for(Integer i = 1; i <= 24; i++) {
            horas.add(i);
        }
        return horas;
    }

    public static List<Integer> getListaHorasFacturables() {
        List<Integer> horas = new ArrayList<Integer>();
        for(Integer i = 1; i <= 24; i++) {
            horas.add(i);
        }
        return horas;
    }

}
