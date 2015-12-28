package försäljning;

import java.io.Serializable;

import java.util.Date;

import produkt.Produkt;

/**
 * Objektet Produktrad är det objekt som läggs till i Köp, dvs. den produkt
 * som köps och läggs till i varukorgen. Produktrad innehåller:
 *
 * Produkt produkt:	det Produkt-objekt som skall köpas
 * double rabatt:	en double motsv. den rabatt som produktraden får vid beräkning
 * int antal:		hur många av objektet som skall köpas
 * Date datum:		det datum då Produktrad skapades
 *
 * @author Robert Olsson
 */
public class Produktrad implements Serializable {

	private Produkt produkt;
	private double rabatt;
	private int antal;
	private Date datum;

	/** @author Robert Olsson */
	public Produktrad(Produkt produkt) {
		this.produkt = produkt;
		this.rabatt = 0;
		this.antal = 1;
		this.datum = new Date();
	}

	/** @author Robert Olsson */
	public void setProdukt(Produkt produkt) {
		this.produkt = produkt;
	}

	/** @author Robert Olsson */
	public void setRabatt(Double rabatt) {
		this.rabatt = rabatt;
	}

	/** @author Robert Olsson */
	public void setAntal(int antal) {
		this.antal = antal;
	}

	/** @author Robert Olsson */
	public void setPris(double pris) {
		this.produkt.setPris(pris);
	}
	
	/** @author Robert Olsson */
	public void setDatum(Date datum) {
		this.datum = datum;
	}

	/** @author Robert Olsson */
	public Produkt getProdukt() {
		return produkt;
	}

	/** @author Robert Olsson */
	public double getRabatt() {
		return rabatt;
	}

	/** @author Robert Olsson */
	public int getAntal() {
		return antal;
	}

	/** @author Robert Olsson */
	public double getPris() {
		return produkt.getPris();
	}

	/** @author Robert Olsson */
	public Date getDatum() {
		return datum;
	}
	
	/** @author Robert Olsson */
	@Override
	public String toString() {
		return getProdukt().toString() + ", antal: " + getAntal() + ", rabatt: " + getRabatt();
	}

	/** @author Robert Olsson */
	public String toString(boolean useTabDelimiter) {
		Produkt produkt = getProdukt();
		return produkt.getEAN() + "\t" + getPris() + "\t" + getAntal() + "\t" + getRabatt() + "\t" + produkt.getNamn()
			+ "\t" + getDatum().toString();
	}

}
