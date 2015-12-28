package produkt;

import java.util.Date;

/**
 * Objektet Färskvara extendar Produkt, med tillägg Date bästFöre,
 * som representerar ett bäst-före-datum.
 *
 * @author Robert Olsson
 */
public class Färskvara extends Produkt {
	private Date bästFöre;

	/** @author Robert Olsson */
	public Färskvara(int ean, String namn, double pris, Date bästFöre) {
		super(ean, namn, pris);
		this.bästFöre = bästFöre;
	}

	/** @author Robert Olsson */
	public Date getBästFöre() {
		return bästFöre;
	}

	/** @author Robert Olsson */
	@Override
	public String toString() {
		return (super.toString() + ", bäst-före-datum: " + getBästFöre());
	}

}
