package produkt;

import java.io.Serializable;

/**
 * Objektet Produkt representerar en fysisk produkt som Food4Fun säljer.
 * Består av ett ean, namn och pris, med tillhörande setters/getters.
 *
 * @author Magnus Olovsson & Robert Olsson
 */
public class Produkt implements Serializable {
	private int ean;
	private String namn;
	private Double pris;

	/** @author Magnus Olovsson */
	public Produkt(int ean, String namn, double pris) {
		this.ean = ean;
		this.namn = namn;
		this.pris = pris;
	}

	/** @author Robert Olsson */
	public int getEAN() {
		return ean;
	}

	/** @author Robert Olsson */
	public String getNamn() {
		return namn;
	}

	/** @author Robert Olsson */
	public Double getPris() {
		return pris;
	}

	/** @author Magnus Olovsson */
	public void setEAN(int ean) {
		this.ean = ean;
	}

	/** @author Magnus Olovsson */
	public void setNamn(String namn) {
		this.namn = namn;
	}

	/** @author Magnus Olovsson */
	public void setPris(Double pris) {
		this.pris = pris;
	}

	/** @author Robert Olsson */
	@Override
	public String toString() {
		return "ean: " + ean + ", name: " + namn + ", pris: " + pris;
	}

}
