package renan.util;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Map;

public class Util {

	public static boolean vazio(Object objeto) {

		if (objeto == null) {
			return true;
		}

		if (objeto instanceof String) {
			if (((String) objeto).trim().equals("")) {
				return true;
			} else {
				return false;
			}
		}

		if (objeto instanceof Collection) {
			if (((Collection<?>) objeto).isEmpty()) {
				return true;
			} else {
				return false;
			}
		}

		if (objeto instanceof Map) {
			if (((Map<?, ?>) objeto).isEmpty()) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

	public static boolean preenchido(Object objeto) {
		return !vazio(objeto);
	}

	public static GregorianCalendar copiaGregorianCalendar(GregorianCalendar calendario) {

		GregorianCalendar newCalendar = new GregorianCalendar();

		newCalendar.set(Calendar.HOUR_OF_DAY, calendario.get(Calendar.HOUR_OF_DAY));
		newCalendar.set(Calendar.YEAR, calendario.get(Calendar.YEAR));
		newCalendar.set(Calendar.MONTH, calendario.get(Calendar.MONTH));
		newCalendar.set(Calendar.DAY_OF_MONTH, calendario.get(Calendar.DAY_OF_MONTH));
		newCalendar.set(Calendar.MINUTE, calendario.get(Calendar.MINUTE));
		newCalendar.set(Calendar.SECOND, 0);
		newCalendar.set(Calendar.MILLISECOND, 0);

		return newCalendar;
	}

}
