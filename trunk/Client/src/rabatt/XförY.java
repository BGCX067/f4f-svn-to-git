package rabatt;

import f�rs�ljning.*;

import java.util.Date;
import java.util.Iterator;

import java.util.List;

import produkt.Produkt;

public class Xf�rY extends ProduktRabatt {

	private int X, Y;

	/** @author Pierre Odengard */
	public Xf�rY(String rabattKod, Date anv�ndsF�re, Produkt produkt, int X, int Y) {
		super(rabattKod, anv�ndsF�re);
		super.addProdukt(produkt);
		this.X = X;
		this.Y = Y;
	}

	/** @author Pierre Odengard & Robert Olsson
	 * Robert Olsson har fixat s� att rabatter stackar
	 */
	public void setRabatt(List<Produktrad> varulista) {
		double rabattPris = 0;

		if (super.getAnv�ndsF�re().before(new Date()))
			return;

		// Leta upp produktrad kopplad till Xf�rY
		for (Iterator i = varulista.iterator(); i.hasNext(); ) {
			Produktrad produktRad = (Produktrad)i.next();

			if (produktRad.getProdukt().getEAN() == super.getProdukt(0).getEAN()) {
				for (int j = produktRad.getAntal(); j >= X; j -= X)
					rabattPris += super.getProdukt(0).getPris();

				produktRad.setRabatt(produktRad.getRabatt() + rabattPris);
			}
		}

	}

	/** @author Magnus Olovsson */
	@Override
	public String toString() {
		return (super.toString() + ", X = " + X + ", Y = " + Y);
	}

}
