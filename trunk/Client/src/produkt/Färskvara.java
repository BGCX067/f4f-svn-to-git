package produkt;

import java.util.Date;

/**
 * Objektet F�rskvara extendar Produkt, med till�gg Date b�stF�re,
 * som representerar ett b�st-f�re-datum.
 *
 * @author Robert Olsson
 */
public class F�rskvara extends Produkt {
	private Date b�stF�re;

	/** @author Robert Olsson */
	public F�rskvara(int ean, String namn, double pris, Date b�stF�re) {
		super(ean, namn, pris);
		this.b�stF�re = b�stF�re;
	}

	/** @author Robert Olsson */
	public Date getB�stF�re() {
		return b�stF�re;
	}

	/** @author Robert Olsson */
	@Override
	public String toString() {
		return (super.toString() + ", b�st-f�re-datum: " + getB�stF�re());
	}

}
