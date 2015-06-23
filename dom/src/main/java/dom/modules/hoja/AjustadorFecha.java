package dom.modules.hoja;

import org.apache.isis.applib.annotation.Programmatic;

import java.sql.Date;
import java.util.Calendar;

public class AjustadorFecha {
    public AjustadorFecha() {
    }

    @Programmatic
    public Date ajustarFechaInicial(Date desde) {
        return ajustarFecha(desde, 0);
    }

    @Programmatic
    public Date ajustarFechaFinal(Date hasta) {
        return ajustarFecha(hasta, 1);
    }

    @SuppressWarnings("deprecation")
    @Programmatic
    public Date ajustarFecha(Date fecha, int offset) {
        Calendar cal = Calendar.getInstance();
        cal.set(1900 + fecha.getYear(), fecha.getMonth(), fecha.getDate(), 00, 00, 00);
        cal.add(Calendar.DATE, offset);
        return new Date(cal.getTime().getTime());
    }
}