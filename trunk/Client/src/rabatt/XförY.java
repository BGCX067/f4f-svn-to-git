package rabatt;

import försäljning.*;

import java.util.Date;
import java.util.Iterator;

import java.util.List;

import produkt.Produkt;

public class XförY extends ProduktRabatt {

	private int X, Y;

	/** @author Pierre Odengard */
	public XförY(String rabattKod, Date användsFöre, Produkt produkt, int X, int Y) {
		super(rabattKod, användsFöre);
		super.addProdukt(produkt);
		this.X = X;
		this.Y = Y;
	}

	/** @author Pierre Odengard & Robert Olsson
	 * Robert Olsson har fixat så att rabatter stackar
	 */
	public void setRabatt(List<Produktrad> varulista) {
		double rabattPris = 0;

		if (super.getAnvändsFöre().before(new Date()))
			return;

		// Leta upp produktrad kopplad till XförY
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
