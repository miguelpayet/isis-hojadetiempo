package dom.modules.reportes;

import dom.modules.clientes.Cliente;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.annotation.Title;

import java.util.*;

@DomainObject(objectType = "Mes", nature = Nature.INMEMORY_ENTITY)
public class Mes implements Comparable<Mes> {

	static final private Locale locale = Locale.forLanguageTag("es");
	private Date fechaInicio;
	private String nombre;

	static public SortedSet<Mes> getMeses() {
		SortedSet<Mes> meses = new TreeSet<>();
		Calendar cal = Calendar.getInstance(locale);
		cal.setTimeInMillis(System.currentTimeMillis());
		for (int i = 12; i > 0; i--) {
			meses.add(new Mes(cal.getTime()));
			if (cal.get(Calendar.MONTH) == 0) {
				cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
				cal.set(Calendar.MONTH, 11);
			} else {
				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
			}
		}
		return meses;
	}

	static public Set<Mes> getMeses(Cliente cliente) {
		//TODO: implementar lista de meses descendiente que he registrado al cliente
		return null;
	}

	public Mes() {
	}

	public Mes(Date fecha) {
		Calendar cal = Calendar.getInstance(locale);
		cal.setTime(fecha);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		this.fechaInicio = cal.getTime();
		this.nombre = String.format("%02d/%04d", cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
	}

	public int compareTo(Mes mes) {
		return (this.fechaInicio.compareTo(mes.fechaInicio) * -1);
	}

	public java.sql.Date getFecha() {
		Calendar cal = Calendar.getInstance();
		String[] fecha = getNombre().split("/");
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, Integer.parseInt(fecha[0]) - 1);
		cal.set(Calendar.YEAR, Integer.parseInt(fecha[1]));
		return new java.sql.Date(cal.getTime().getTime());
	}

	@Title
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
}
