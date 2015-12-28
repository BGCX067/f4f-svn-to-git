package försäljning;

import produkt.ProduktRegister;
import produkt.Produkt;

import rabatt.RabattRegister;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;

import java.util.Iterator;
import java.util.List;

import produkt.Färskvara;

/**
 * Motsvarar en varukorg. Klassen ansvarar för köp av produkter, presentering av
 * kvitto med tillhörande hjälpmetoder. Inmatning och beräkning av rabatter sker
 * också med hjälp av RabattRegister.
 *
 * @author Robert Olsson
 */
public class Köp implements Serializable {

	@SuppressWarnings("compatibility:7355110961694367462")
	private static final long serialVersionUID = -3752779431278314466L;

	public final static int PRODUKT_EXISTERAR_EJ = -1, PRODUKT_UTGÅNGEN = 0, PRODUKT_KÖPT = 1,
		PRODUKT_TILLAGD_I_VARUKORG = 2, PRODUKT_UPPDATERAD_KVANTITET = 3;

	private final int INIT_SIZE = 5;
	
	private ProduktRegister produktRegister;
	private RabattRegister rabattRegister;
	
	private List<Produktrad> varukorg;
	private List<String> rabattkoder;
	private Date datum;

	/** @author Robert Olsson */
	public Köp(ProduktRegister produktRegister, RabattRegister rabattRegister) {
		this.produktRegister = produktRegister;
		this.rabattRegister = rabattRegister;
		varukorg = new ArrayList<Produktrad>(INIT_SIZE);
		rabattkoder = new ArrayList<String>(INIT_SIZE);
		datum = new Date();
	}

	/** @author Robert Olsson */
	public void setDatum(Date datum) {
		this.datum = datum;
	}

	/** @author Robert Olsson */
	public Date getDatum() {
		return datum;
	}

	/** @author Robert Olsson */
	public void utförKöp() {
		// Beräkna samtliga rabatter för hela köpet:
		for (int i = 0; i < rabattkoder.size(); i++) {
			String coupon = rabattkoder.get(i);
			rabattRegister.beräknaRabatt(varukorg, coupon);
		}
	}

	/**
	 * Metoden försöker att köpa en produkt. En int returnerar beroende på status:
	 * 
	 * @return  PRODUKT_EXISTERAR_EJ = Produkten existerar inte
	 * 			PRODUKT_UTGÅNGEN = produktens bäst-före-datum har passerat
	 * 			alt. resultatet från lagraKöp(produkt)
	 *
	 * @author Robert Olsson
	 */
	public int köpProdukt(int ean) {
		Produkt produkt = produktRegister.getProdukt(ean);
		if (produkt == null) {
			return PRODUKT_EXISTERAR_EJ;
		} else if (isUtgångenVara(produkt)) {
			return PRODUKT_UTGÅNGEN;
		} else {
			return lagraKöp(produkt);
		}
	}

	/**
	 * lagraKöp(Produkt produkt) lagrar en produktrad i Köp. Om produkten redan finns i varukorgen
	 * uppdateras denna produktrad med ett i kvantitet.
	 *
	 * @return PRODUKT_UPPDATERAD_KVANTITET om produkten redan fanns i varukorgen. Antalet produkter ökar
	 * då. I annat fall returneras PRODUKT_TILLAGD_I_VARUKORG, dvs. att produkten har lagts till i
	 * varukorgen (1 stk).
	 *
	 * @author Robert Olsson */
	public int lagraKöp(Produkt produkt) {
		int köpIndex = redanKöpt(produkt);

		if (köpIndex > -1) {
			Produktrad köptProdukt = varukorg.get(köpIndex);
			köptProdukt.setAntal(köptProdukt.getAntal() + 1);
			return PRODUKT_UPPDATERAD_KVANTITET;
		} else {
			varukorg.add(new Produktrad(produkt));
			return PRODUKT_TILLAGD_I_VARUKORG;
		}
	}

	/**
	 * @return returns the index for the bought product, otherwise -1 is returned
	 * 
	 * @author Robert Olsson
	 */
	public int redanKöpt(Produkt produkt) {
		for (int i = 0; i < varukorg.size(); i++) {
			if (varukorg.get(i).getProdukt().getEAN() == produkt.getEAN()) {
				return i;
			}
		}
		return -1;
	}

	/** @author Robert Olsson */
	public int angeRabattKod(String rabattkod) {
		if (rabattRegister.validRabattKod(rabattkod)) {
			if (!rabattKodInmatad(rabattkod)) {
				rabattkoder.add(rabattkod);
				return RabattRegister.RABATT_INMATAD;
			} else {
				return RabattRegister.RABATT_REDAN_INMATAD;
			}
		} else {
			return RabattRegister.RABATT_FELAKTIG;
		}
	}

	/** @author Robert Olsson */
	public boolean rabattKodInmatad(String rabattkod) {
		return rabattkoder.contains(rabattkod);
	}

	/** @author Robert Olsson */
	public double getSumma() {
		double sum = 0;
		for (int i = 0; i < varukorg.size(); i++) {
			sum += varukorg.get(i).getPris() * varukorg.get(i).getAntal() - varukorg.get(i).getRabatt();
		}
		return sum;
	}

	/** @author Robert Olsson */
	public double getTotalRabatt() {
		double sum = 0;
		for (int i = 0; i < varukorg.size(); i++) {
			sum += varukorg.get(i).getRabatt();
		}
		return sum;
	}

	/** @author Robert Olsson */
	private boolean isUtgångenVara(Object o) {
		if (o instanceof Färskvara) {
			Färskvara produkt = (Färskvara)o;
			if (produkt.getBästFöre().before(new Date()))
				return true;
		}
		return false;
	}

	/** @author Robert Olsson */
	public Iterator getVarukorg() {
		return varukorg.iterator();
	}

	/** @author Robert Olsson */
	public Produktrad getProduktrad(int index) {
		return varukorg.get(index);
	}
	
	/** @author Robert Olsson */
	public Produktrad getProduktradEAN(int ean) {
		for (int i = 0; i < varukorg.size(); i++) {
			if (varukorg.get(i).getProdukt().getEAN() == ean) {
				return varukorg.get(i);
			}
		}
		return null;
	}

	/** @author Robert Olsson */
	public void addProduktrad(Produktrad produktrad) {
		varukorg.add(produktrad);
	}

	/** @author Robert Olsson */
	public Iterator getRabattkoder() {
		return rabattkoder.iterator();
	}

	/** @author Robert Olsson */
	public int getAntalProdukter() {
		return varukorg.size();
	}

	/** @author Robert Olsson */
	public int getAntalRabatter() {
		return rabattkoder.size();
	}

	/** @author Robert Olsson */
	public String getRabattkod(int i) {
		return rabattkoder.get(i);
	}

}
