package rabatt;

import försäljning.Produktrad;

import java.util.Date;
import java.util.Iterator;

import java.util.List;

import produkt.Produkt;

public class XförYomZ extends ProduktRabatt {

	private double rabattPris;

	/** @author Pierre Odengard */
	public XförYomZ(String rabattKod, Date användsFöre, Produkt produkt1, double rabattPris, Produkt produkt2) {
		super(rabattKod, användsFöre);
		super.addProdukt(produkt1);
		super.addProdukt(produkt2);
		this.rabattPris = rabattPris;
	}

	/** @author Pierre Odengard */
	@Override
	public void setRabatt(List<Produktrad> varulista) {
		int hittadeZ = 0;
		Produktrad Y = null;

		if (super.getAnvändsFöre().before(new Date()))
			return;

		// Leta upp produktrad kopplad till XförYomZ
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
