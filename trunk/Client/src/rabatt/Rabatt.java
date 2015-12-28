package rabatt;

import produkt.Produkt;

import försäljning.*;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

/**
 * Rabatt är superobjektet till samtliga rabattyper i systemet
 *
 * @author Robert Olsson
 */
public abstract class Rabatt implements Serializable {

	private Date användsFöre;
	private String rabattkod;

	/** @author Robert Olsson */
	public Rabatt(String rabattkod, Date användsFöre) {
		this.rabattkod = rabattkod;
		this.användsFöre = användsFöre;
	}

	/** @author Robert Olsson */
	public abstract void setRabatt(List<Produktrad> varulista);

	/** @author Robert Olsson */
	public Date getAnvändsFöre() {
		return användsFöre;
	}

	/** @author Robert Olsson */
	public void setAnvändsFöre(Date användsFöre) {
		this.användsFöre = användsFöre;
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
		return "används-före-datum: " + användsFöre + ", rabattkod: " + rabattkod;
	}

}
