package f�rs�ljning;

import produkt.ProduktRegister;
import produkt.Produkt;

import rabatt.RabattRegister;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;

import java.util.Iterator;
import java.util.List;

import produkt.F�rskvara;

/**
 * Motsvarar en varukorg. Klassen ansvarar f�r k�p av produkter, presentering av
 * kvitto med tillh�rande hj�lpmetoder. Inmatning och ber�kning av rabatter sker
 * ocks� med hj�lp av RabattRegister.
 *
 * @author Robert Olsson
 */
public class K�p implements Serializable {

	@SuppressWarnings("compatibility:7355110961694367462")
	private static final long serialVersionUID = -3752779431278314466L;

	public final static int PRODUKT_EXISTERAR_EJ = -1, PRODUKT_UTG�NGEN = 0, PRODUKT_K�PT = 1,
		PRODUKT_TILLAGD_I_VARUKORG = 2, PRODUKT_UPPDATERAD_KVANTITET = 3;

	private final int INIT_SIZE = 5;
	
	private ProduktRegister produktRegister;
	private RabattRegister rabattRegister;
	
	private List<Produktrad> varukorg;
	private List<String> rabattkoder;
	private Date datum;

	/** @author Robert Olsson */
	public K�p(ProduktRegister produktRegister, RabattRegister rabattRegister) {
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
	public void utf�rK�p() {
		// Ber�kna samtliga rabatter f�r hela k�pet:
		for (int i = 0; i < rabattkoder.size(); i++) {
			String coupon = rabattkoder.get(i);
			rabattRegister.ber�knaRabatt(varukorg, coupon);
		}
	}

	/**
	 * Metoden f�rs�ker att k�pa en produkt. En int returnerar beroende p� status:
	 * 
	 * @return  PRODUKT_EXISTERAR_EJ = Produkten existerar inte
	 * 			PRODUKT_UTG�NGEN = produktens b�st-f�re-datum har passerat
	 * 			alt. resultatet fr�n lagraK�p(produkt)
	 *
	 * @author Robert Olsson
	 */
	public int k�pProdukt(int ean) {
		Produkt produkt = produktRegister.getProdukt(ean);
		if (produkt == null) {
			return PRODUKT_EXISTERAR_EJ;
		} else if (isUtg�ngenVara(produkt)) {
			return PRODUKT_UTG�NGEN;
		} else {
			return lagraK�p(produkt);
		}
	}

	/**
	 * lagraK�p(Produkt produkt) lagrar en produktrad i K�p. Om produkten redan finns i varukorgen
	 * uppdateras denna produktrad med ett i kvantitet.
	 *
	 * @return PRODUKT_UPPDATERAD_KVANTITET om produkten redan fanns i varukorgen. Antalet produkter �kar
	 * d�. I annat fall returneras PRODUKT_TILLAGD_I_VARUKORG, dvs. att produkten har lagts till i
	 * varukorgen (1 stk).
	 *
	 * @author Robert Olsson */
	public int lagraK�p(Produkt produkt) {
		int k�pIndex = redanK�pt(produkt);

		if (k�pIndex > -1) {
			Produktrad k�ptProdukt = varukorg.get(k�pIndex);
			k�ptProdukt.setAntal(k�ptProdukt.getAntal() + 1);
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
	public int redanK�pt(Produkt produkt) {
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
	private boolean isUtg�ngenVara(Object o) {
		if (o instanceof F�rskvara) {
			F�rskvara produkt = (F�rskvara)o;
			if (produkt.getB�stF�re().before(new Date()))
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
