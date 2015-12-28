package food4fun;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import försäljning.Köp;

import java.io.EOFException;

import java.io.File;

import java.io.InvalidClassException;

import java.util.Date;

/**
 * Hanterar historik för varukorg - sparar aktuell varukorg till disk, läser in varukorg
 * från disk, tömmer historiken samt beräknar försäljning mellan givet datumintervall.
 *
 * @author Magnus Olovsson & Robert Olsson
 */
public class Historik {

	private final String databasKöp = "köp.dat";

	public final static int DATABAS_EXISTERAR_EJ = -1,
		DATABAS_FEL_EXISTERAR_EJ = 0, DATABASE_FEL_LÄSNING = 1, DATABAS_FEL_SKRIVA = 2,
		DATABAS_SPARAD = 3, DATABAS_TÖMD = 4; 


	/** @author Robert Olsson */
	public boolean köpLagrat() {
		File file = new File(databasKöp);
		return file.exists();
	}

	/** @author Robert Olsson */
	public Köp laddaKöp() throws EOFException, IOException,InvalidClassException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(databasKöp));
		Object o = in.readObject();
		in.close();

		return (o instanceof Köp) ? (Köp) o : null;
	}

	/** @author Robert Olsson */
	public void sparaKöp(Köp köp) throws EOFException, IOException, ClassNotFoundException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(databasKöp));
		out.writeObject(köp);
		out.close();
	}

	/** @author Magnus Olovsson & Robert Olsson */
	public int tömHistorik() {
		try {
			new FileOutputStream(databasKöp);
			return DATABAS_TÖMD;
		} catch (FileNotFoundException e) {
			return DATABAS_FEL_EXISTERAR_EJ;
		}
	}
	
	/** @author Robert Olsson */
	public double beräknaFörsäljning(Köp köp, Date startdatum, Date slutdatum) {
		int i = 0;
		double sum = 0;

		if (köp != null) {
			while (i < köp.getAntalProdukter()) {
			    if (köp.getProduktrad(i).getDatum().after(startdatum)
				  && köp.getProduktrad(i).getDatum().before(slutdatum))
				{
					// Inkrementera summan med priset och dra därefter av ev. rabatt:
					sum += köp.getProduktrad(i).getPris() * köp.getProduktrad(i).getAntal();
					sum -= köp.getProduktrad(i).getRabatt();
				}
				i++;
			}
		}
		return sum;
	}

}