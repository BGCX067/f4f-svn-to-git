package rabatt;

import produkt.Produkt;

import f�rs�ljning.*;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

/**
 * Rabatt �r superobjektet till samtliga rabattyper i systemet
 *
 * @author Robert Olsson
 */
public abstract class Rabatt implements Serializable {

	private Date anv�ndsF�re;
	private String rabattkod;

	/** @author Robert Olsson */
	public Rabatt(String rabattkod, Date anv�ndsF�re) {
		this.rabattkod = rabattkod;
		this.anv�ndsF�re = anv�ndsF�re;
	}

	/** @author Robert Olsson */
	public abstract void setRabatt(List<Produktrad> varulista);

	/** @author Robert Olsson */
	public Date getAnv�ndsF�re() {
		return anv�ndsF�re;
	}

	/** @author Robert Olsson */
	public void setAnv�ndsF�re(Date anv�ndsF�re) {
		this.anv�ndsF�re = anv�ndsF�re;
	}

	/** @author Robert Olsson */
	public String getRabattkod() {
		return rabattkod;
	}

	/** @author Robert Olsson */
	public void setRabattkod(String rabattkod) {
		this.rabattkod = rabattkod;
	}

	/** @author Robert Olsson */
	@Override
	public String toString() {
		return "anv�nds-f�re-datum: " + anv�ndsF�re + ", rabattkod: " + rabattkod;
	}

}
