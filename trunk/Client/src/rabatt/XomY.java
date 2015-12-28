package rabatt;

import försäljning.*;

import java.util.Date;
import java.util.Iterator;

import java.util.List;

import produkt.Produkt;

public class XomY extends ProduktRabatt {

	private double X, Y;

	/** @author Pierre Odengard */
	public XomY(String rabattKod, Date användsFöre, Produkt produkt, double X, double Y) {
		super(rabattKod, användsFöre);
		super.addProdukt(produkt);
		this.X = X;
		this.Y = Y;
	}

	/** @author Pierre Odengard */
	@Override
	public void setRabatt(List<Produktrad> varulista) {
		double summa = 0;
		Produktrad _produktRad = null; // För att spara produktraden om den påträffas

		if (super.getAnvändsFöre().before(new Date()))
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
