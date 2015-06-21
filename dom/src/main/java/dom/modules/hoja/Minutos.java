package dom.modules.hoja;

import java.util.ArrayList;
import java.util.List;

public class Minutos {

    public static List<Integer> getListaMinutosReales() {
        List<Integer> horas = new ArrayList<Integer>();
        for (Integer i = 1; i <= 60; i++) {
            horas.add(i);
        }
        return horas;
    }

    public static List<Integer> getListaMinutosFacturables() {
        List<Integer> horas = new ArrayList<Integer>();
        horas.add(0);
        horas.add(15);
        horas.add(30);
        horas.add(45);
        return horas;
    }
}
