package dom.modules.hoja;

import java.util.ArrayList;
import java.util.List;

public class Minutos {

	public static List<Integer> getListaMinutosReales() {
		return getListaMinutosFacturables();
	}

	public static List<Integer> getListaMinutosFacturables() {
		List<Integer> minutos = new ArrayList<Integer>();
		minutos.add(0);
		minutos.add(15);
		minutos.add(30);
		minutos.add(45);
		return minutos;
	}
}
