package dom.modules.Procesos;

import dom.modules.BaseService;
import org.apache.isis.applib.annotation.*;

import java.sql.Date;

public class ProcesosService extends BaseService {

	@ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Cierre")
	@MemberOrder(sequence = "3")
	public void cierreMensual(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Desde") final java.sql.Date desde,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Hasta") final java.sql.Date hasta) {
		Date desdeAjustado = ajustadorFecha.ajustarFechaInicial(desde);
		Date hastaAjustado = ajustadorFecha.ajustarFechaFinal(hasta);
	}

}
