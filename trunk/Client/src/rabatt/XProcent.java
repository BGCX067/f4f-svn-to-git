package rabatt;

import försäljning.*;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class XProcent extends Rabatt {

	private double rabattProcent;

	/** @author Pierre Odengard */
	public XProcent(String rabattKod, Date användsFöre, double rabattProcent) {
		super(rabattKod, användsFöre);
		this.rabattProcent = rabattProcent;
	}

	/** @author Pierre Odengard
	 * Fixar av Robert Olsson:
	 * 1. hanterar kvantitet
	 * 2. hanterar multipla rabatter för samma produktrad;
	 * 		Ex. "hungrig" + "65+" ger nu 21.6 istället för enbart 0.9 som tidigare,
	 *      om man köper fyra stk Estrella Sourcream [141521]
	 */
	@Override
	public void setRabatt(List<Produktrad> varulista) {

		if (super.getAnvändsFöre().before(new Date()))
			return;

		// Sätt procent rabatt på allt
		for (Iterator i = varulista.iterator(); i.hasNext(); ) {
			Produktrad produktRad = (Produktrad)i.next();
			double prisProduktrad = produktRad.getPris() * produktRad.getAntal();
			produktRad.setRabatt(produktRad.getRabatt() + (prisProduktrad * (rabattProcent / 100)));
		}
	}

	/** @author Magnus Olovsson */
	@Override
	public String toString() {
		return (super.toString() + ", rabattProcent = " + rabattProcent);
	}

}
