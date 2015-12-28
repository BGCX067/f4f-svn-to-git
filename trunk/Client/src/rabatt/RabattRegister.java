package rabatt;

import f�rs�ljning.Produktrad;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.ParseException;

import java.text.SimpleDateFormat;

import java.util.Hashtable;

import java.util.List;

import produkt.ProduktRegister;

public class RabattRegister implements Serializable {

	public final static int RABATT_FELAKTIG = -1, RABATT_REDAN_INMATAD = 0,
		RABATT_INMATAD = 1, RABATTER_SKAPADE = 2, RABATTER_EJ_SKAPADE = 3;

	private ProduktRegister produktRegister;
	private Hashtable rabatter;
	
	private final int HSIZE = 10;
	private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	/** @author Pierre Odengard */
	public RabattRegister(ProduktRegister produktRegister) {
		rabatter = new Hashtable(HSIZE, HSIZE);
		this.produktRegister = produktRegister;
	}
	
	public int skapaRabatter() {
		try {
			// K�p 3 Dajmstrutar f�r priset av 2. Kod �gottegris�, AFD 2011-09-15
			rabatter.put("gottegris", new Xf�rY("gottegris", df.parse("2011-10-15"), produktRegister.getProdukt(423901), 3, 2));
			// K�p 4 Estr Soucr & onion p�sar f�r priset av 3. Kod �hungrig� AFD 2011-09-20
			rabatter.put("hungrig", new Xf�rY("hungrig", df.parse("2011-10-20"), produktRegister.getProdukt(141521), 4, 3));
			// 5% rabatt f�r pension�rer. Kod �65+� 2011-09-21
			rabatter.put("65+", new XProcent("65+", df.parse("2011-10-21"), 5));
			// K�p ett paket Libero Bl�jor f�r 80kr om du handlar f�r minst 200kr. Kod �Bebis� AFD 2011-09-10
			rabatter.put("bebis", new XomY("bebis", df.parse("2011-10-10"), produktRegister.getProdukt(122065), 12.5, 200));
			// K�p 400g Rydb Potatissallad f�r 10kr om du k�per 500g fl�skfile. Kod �Grillkv�ll� AFD 2011-09-15
			rabatter.put("grillkv�ll", new Xf�rYomZ("grillkv�ll", df.parse("2011-10-10"), produktRegister.getProdukt(236756), 14, produktRegister.getProdukt(240220)));
		} catch (ParseException e) {
			return RABATTER_EJ_SKAPADE;
		}
		return RABATTER_SKAPADE;
	}

	/** @author Pierre Odengard */
	public int ber�knaRabatt(List<Produktrad> varulista, String rabattKod) {
		// Leta upp rabatt-objektet i hashtabellen
		Object r = rabatter.get(rabattKod);

		if (r instanceof Xf�rY) {
			Xf�rY rabatt = (Xf�rY)r;
			rabatt.setRabatt(varulista);
			return 1;
		} else if (r instanceof XomY) {
			XomY rabatt = (XomY)r;
			rabatt.setRabatt(varulista);
			return 1;
		} else if (r instanceof XProcent) {
			XProcent rabatt = (XProcent)r;
			rabatt.setRabatt(varulista);
			return 1;
		} else if (r instanceof Xf�rYomZ) {
			Xf�rYomZ rabatt = (Xf�rYomZ)r;
			rabatt.setRabatt(varulista);
			return 1;
		}

		return RABATT_FELAKTIG;
	}

	/** @author Pierre Odengard*/
	public Boolean validRabattKod(String rabattKod) {
		return rabatter.containsKey(rabattKod);
	}

}
