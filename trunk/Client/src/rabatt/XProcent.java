package rabatt;

import f�rs�ljning.*;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class XProcent extends Rabatt {

	private double rabattProcent;

	/** @author Pierre Odengard */
	public XProcent(String rabattKod, Date anv�ndsF�re, double rabattProcent) {
		super(rabattKod, anv�ndsF�re);
		this.rabattProcent = rabattProcent;
	}

	/** @author Pierre Odengard
	 * Fixar av Robert Olsson:
	 * 1. hanterar kvantitet
	 * 2. hanterar multipla rabatter f�r samma produktrad;
	 * 		Ex. "hungrig" + "65+" ger nu 21.6 ist�llet f�r enbart 0.9 som tidigare,
	 *      om man k�per fyra stk Estrella Sourcream [141521]
	 */
	@Override
	public void setRabatt(List<Produktrad> varulista) {

		if (super.getAnv�ndsF�re().before(new Date()))
			return;

		// S�tt procent rabatt p� allt
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
