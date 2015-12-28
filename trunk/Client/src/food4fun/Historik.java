package food4fun;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import f�rs�ljning.K�p;

import java.io.EOFException;

import java.io.File;

import java.io.InvalidClassException;

import java.util.Date;

/**
 * Hanterar historik f�r varukorg - sparar aktuell varukorg till disk, l�ser in varukorg
 * fr�n disk, t�mmer historiken samt ber�knar f�rs�ljning mellan givet datumintervall.
 *
 * @author Magnus Olovsson & Robert Olsson
 */
public class Historik {

	private final String databasK�p = "k�p.dat";

	public final static int DATABAS_EXISTERAR_EJ = -1,
		DATABAS_FEL_EXISTERAR_EJ = 0, DATABASE_FEL_L�SNING = 1, DATABAS_FEL_SKRIVA = 2,
		DATABAS_SPARAD = 3, DATABAS_T�MD = 4; 


	/** @author Robert Olsson */
	public boolean k�pLagrat() {
		File file = new File(databasK�p);
		return file.exists();
	}

	/** @author Robert Olsson */
	public K�p laddaK�p() throws EOFException, IOException,InvalidClassException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(databasK�p));
		Object o = in.readObject();
		in.close();

		return (o instanceof K�p) ? (K�p) o : null;
	}

	/** @author Robert Olsson */
	public void sparaK�p(K�p k�p) throws EOFException, IOException, ClassNotFoundException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(databasK�p));
		out.writeObject(k�p);
		out.close();
	}

	/** @author Magnus Olovsson & Robert Olsson */
	public int t�mHistorik() {
		try {
			new FileOutputStream(databasK�p);
			return DATABAS_T�MD;
		} catch (FileNotFoundException e) {
			return DATABAS_FEL_EXISTERAR_EJ;
		}
	}
	
	/** @author Robert Olsson */
	public double ber�knaF�rs�ljning(K�p k�p, Date startdatum, Date slutdatum) {
		int i = 0;
		double sum = 0;

		if (k�p != null) {
			while (i < k�p.getAntalProdukter()) {
			    if (k�p.getProduktrad(i).getDatum().after(startdatum)
				  && k�p.getProduktrad(i).getDatum().before(slutdatum))
				{
					// Inkrementera summan med priset och dra d�refter av ev. rabatt:
					sum += k�p.getProduktrad(i).getPris() * k�p.getProduktrad(i).getAntal();
					sum -= k�p.getProduktrad(i).getRabatt();
				}
				i++;
			}
		}
		return sum;
	}

}