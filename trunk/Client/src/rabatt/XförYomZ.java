package rabatt;

import f�rs�ljning.Produktrad;

import java.util.Date;
import java.util.Iterator;

import java.util.List;

import produkt.Produkt;

public class Xf�rYomZ extends ProduktRabatt {

	private double rabattPris;

	/** @author Pierre Odengard */
	public Xf�rYomZ(String rabattKod, Date anv�ndsF�re, Produkt produkt1, double rabattPris, Produkt produkt2) {
		super(rabattKod, anv�ndsF�re);
		super.addProdukt(produkt1);
		super.addProdukt(produkt2);
		this.rabattPris = rabattPris;
	}

	/** @author Pierre Odengard */
	@Override
	public void setRabatt(List<Produktrad> varulista) {
		int hittadeZ = 0;
		Produktrad Y = null;

		if (super.getAnv�ndsF�re().before(new Date()))
			return;

		// Leta upp produktrad kopplad till Xf�rYomZ
		for (Iterator i = varulista.iterator(); i.hasNext(); ) {
			Produktrad produktRad = (Produktrad)i.next();

			if (produktRad.getProdukt().getEAN() == super.getProdukt(0).getEAN()) {
				Y = produktRad;
			}

			if (produktRad.getProdukt().getEAN() == super.getProdukt(1).getEAN())
				hittadeZ = 1;
		}


		if (Y != null & hittadeZ > 0)
			Y.setRabatt(rabattPris);

	}

	/** @author Magnus Olovsson */
	@Override
	public String toString() {
		return (super.toString() + ", nytt pris = " + rabattPris);
	}

}
