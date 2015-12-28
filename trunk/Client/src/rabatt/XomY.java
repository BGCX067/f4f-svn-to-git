package rabatt;

import f�rs�ljning.*;

import java.util.Date;
import java.util.Iterator;

import java.util.List;

import produkt.Produkt;

public class XomY extends ProduktRabatt {

	private double X, Y;

	/** @author Pierre Odengard */
	public XomY(String rabattKod, Date anv�ndsF�re, Produkt produkt, double X, double Y) {
		super(rabattKod, anv�ndsF�re);
		super.addProdukt(produkt);
		this.X = X;
		this.Y = Y;
	}

	/** @author Pierre Odengard */
	@Override
	public void setRabatt(List<Produktrad> varulista) {
		double summa = 0;
		Produktrad _produktRad = null; // F�r att spara produktraden om den p�tr�ffas

		if (super.getAnv�ndsF�re().before(new Date()))
			return;

		for (Iterator i = varulista.iterator(); i.hasNext(); ) {
			Produktrad produktRad = (Produktrad)i.next();
			summa += produktRad.getPris() * produktRad.getAntal();

			if (produktRad.getProdukt().getEAN() == super.getProdukt(0).getEAN()) {
				_produktRad = produktRad;
			}
		}

		if (_produktRad != null & summa > Y) {
			_produktRad.setRabatt(X);
		}

	}

	/** @author Magnus Olovsson */
	@Override
	public String toString() {
		return (super.toString() + ", X = " + X + ", Y = " + Y);
	}

}
