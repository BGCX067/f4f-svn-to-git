package food4fun;

import produkt.ProduktRegister;
import produkt.Produkt;

import f�rs�ljning.K�p;
import f�rs�ljning.Produktrad;

import java.io.EOFException;

import java.io.IOException;

import java.io.InvalidClassException;

import utils.InputHelper;

import java.util.Date;

import java.util.Iterator;

import rabatt.RabattRegister;

/**
 * MenySystem - presenterar huvudmenyn i applikationen samt ansvarar f�r val
 * som anv�ndaren g�r. Inputen ifr�n anv�ndaren hanteras emellertid i utils.InputHelper.
 *
 * @author Robert Olsson
 */
public class MenySystem {

	private ProduktRegister produktRegister;
	private RabattRegister rabattRegister;
	private K�p k�p;
	private Historik historik;

	private final String databasK�p = "k�p.dat";
	private final String HISTORIK_DATUM_FORMAT = "yyMMdd";

	public final int REG_PURCHASE = 1,
		ENTER_COUPON = 2,
		LIST_PRODUCTS = 3,
		LIST_CART = 4,
		LIST_COUPONS = 5,
		CHANGE_PRODUCT_PRICE = 6,
		REGISTER_NEW_PRODUCT = 7,
		GET_PURCHASE_HISTORY = 8,
		RESET_PURCHASE = 9,
		SAVE_AND_QUIT = 0;

	private final String MENU = "\n-- Meny --\n" +
		"1. Registrera nytt k�p\n" +
		"2. Ange rabattkod\n" +
		"3. Lista produkter\n" +
		"4. Lista varukorg\n" +
		"5. Lista inmatade rabattkoder\n" +
		"6. �ndra pris p� produkt\n" +
		"7. Registrera ny produkt\n" +
		"8. Ber�kna total f�rs�ljning f�r period\n" +
		"9. Nollst�ll total f�rs�ljning\n" +
		"0. Spara och avsluta";

	/** @author Robert Olsson */
	public MenySystem() {
		produktRegister = new ProduktRegister();
		rabattRegister = new RabattRegister(produktRegister);
		historik = new Historik();
		k�p = skapaK�pRegister();

		if (produktRegister != null && k�p != null && historik != null) {
			laddaProduktRegister();
		    laddaRabattRegister();
			visaMeny();
		} else {
			System.out.println("Vissa klassinstanser har inte tilldelats korrekt.");
		}
	}
	
	/**
	 * Returnerar ett K�p-objekt, antingen inl�st ifr�n disk (lagrad Objekt-fil)
	 * eller genom att skapa en ny instans och returnera denna.
	 * 
	 * @author Robert Olsson */
	public K�p skapaK�pRegister() {
		// L�s in K�p-objekt:
		if (historik.k�pLagrat()) {
			try {
				return historik.laddaK�p();
			} catch (EOFException e) {
				System.out.println("Historik -> ov�ntat fel: " + e.toString());
			} catch (InvalidClassException e) {
				System.out.println("Historik -> felaktig klass: " + e.toString());
			} catch (IOException e) {
				System.out.println("Historik -> I/O-fel: " + e.toString());
			} catch (ClassNotFoundException e) {
				System.out.println("Historik -> felaktig klassinstans: " + e.toString());
			}
		}		
		// Skapa nytt K�p-objekt:
		return new K�p(produktRegister, rabattRegister);
	}

	/** @author Robert Olsson */
	private void laddaProduktRegister() {
		int status = produktRegister.laddaDatabaser();
		switch (status) {
			case ProduktRegister.PRODUKTDB_FEL:
				System.out.println("Ett fel uppstod n�r produktdatabasen laddades.");
				break;
			case ProduktRegister.PRODUKTDB_LADDAD:
				break;
		}
	}

	/** @author Robert Olsson */
	private void laddaRabattRegister() {
		int status = rabattRegister.skapaRabatter();
	    switch (status) {
	        case RabattRegister.RABATTER_EJ_SKAPADE:
				System.out.print("Rabatterna kunde inte skapas.");
				break;
			case RabattRegister.RABATTER_SKAPADE:
				break;
		}
	}

	/** @author Robert Olsson */
	public void visaMeny() {
		int choice = -1;

		while (choice != SAVE_AND_QUIT) {
			System.out.println(MENU);

			System.out.print("\nMenyval: ");
			choice = InputHelper.getInteger();

			switch (choice) {
				case REG_PURCHASE:
					registreraK�p();
					break;
				case ENTER_COUPON:
					angeRabattkod();
					break;
				case LIST_PRODUCTS:
					listaProdukter();
					break;
				case LIST_CART:
					visaKvitto();
					break;
				case LIST_COUPONS:
					listaInmatadeRabatter();
					break;
				case CHANGE_PRODUCT_PRICE:
					�ndraProduktPris();
					break;
				case REGISTER_NEW_PRODUCT:
					registreraNyProdukt();
					break;
				case GET_PURCHASE_HISTORY:
					ber�knaK�phistorik();
					break;
				case RESET_PURCHASE:
					rensaK�pHistorik();
					break;
			}
		}
		
		sparaOchAvsluta();
	}

	/** @author Robert Olsson */
	private void registreraK�p() {
	    Produktrad k�ptProdukt = null;
		
		System.out.println("\n-- K�pmeny --\n" +
				"Ange EAN. F�r att avsluta, skriv in -1.");

		int ean = 0;
		while (ean != -1) {
			ean = InputHelper.getEAN(true);
			if (ean != -1) {
				switch (k�p.k�pProdukt(ean)) {
					case K�p.PRODUKT_EXISTERAR_EJ:
						System.out.println("Produkten existerar inte.");
						break;
					case K�p.PRODUKT_UTG�NGEN:
						System.out.println("Produktens b�st-f�re-datum har dessv�rre passerat.");
						break;
					case K�p.PRODUKT_TILLAGD_I_VARUKORG:
						k�ptProdukt = k�p.getProduktradEAN(ean);
						System.out.println("Produkten tillagd i varukorgen:");
						System.out.println("\"" + k�ptProdukt.getProdukt().getNamn() + "\" ");
						break;
					case K�p.PRODUKT_UPPDATERAD_KVANTITET:
						k�ptProdukt = k�p.getProduktradEAN(ean);
						System.out.println("Produkten redan tillagd i varukorgen:");
						System.out.println("\"" + k�ptProdukt.getProdukt().getNamn() + "\" ");
						System.out.println("�kar antalet till " + k�ptProdukt.getAntal());
						break;
				}
			}
		}
	}

	/** @author Robert Olsson */
	private void listaProdukter() {
		Iterator itr = produktRegister.getProdukter();

		System.out.println("Listar produkter: ");
		while (itr.hasNext()) {
			Object o = itr.next();
			if (o instanceof Produkt)
				System.out.println((Produkt) o);
		}
	}

	/** @author Robert Olsson */
	private void angeRabattkod() {
		System.out.print("Ange rabattkod: ");
		String rabattkod = InputHelper.getString();

		switch (k�p.angeRabattKod(rabattkod)) {
			case RabattRegister.RABATT_INMATAD:
				System.out.println("Rabattkoden har matats in.");
				break;
			case RabattRegister.RABATT_REDAN_INMATAD:
				System.out.println("Rabattkoden �r redan aktiv.");
				break;
			case RabattRegister.RABATT_FELAKTIG:
				System.out.println("Felaktig rabattkod angiven.");
				break;
		}
	}

	/** @author Robert Olsson */
	public void visaKvitto() {
		double totalRabatt = k�p.getTotalRabatt();
		String produktRader = "";

		System.out.println("\n-- Kvitto --\n");

		if (k�p.getAntalProdukter() == 0) {
			System.out.println("Inga produkter i varukorgen.");
		} else {
			produktRader = produktRader + "EAN\tPris\tAntal\tRabatt\tProdukt\tDate";
			for (int i = 0; i < k�p.getAntalProdukter(); i++) {
				produktRader = produktRader + "\n" +
						k�p.getProduktrad(i).toString(true);
			}
			System.out.println(produktRader);
		}

		k�p.setDatum(new Date());
		System.out.println("\nDatum: " + k�p.getDatum() + "\nSumma: " + k�p.getSumma() + "\nRabatt: " +
					  k�p.getTotalRabatt());

		// Utnyttjade rabatter:
		if (totalRabatt > 0) {
			System.out.println("Utnyttjade rabattkoder:\n" +
					visaRabatter());
		}
	}

	/** @author Robert Olsson */
	public void listaInmatadeRabatter() {
		System.out.println("\n-- Inmatade rabatter --\n" +
				visaRabatter());
	}

	/** @author Robert Olsson */
	public String visaRabatter() {
		if (k�p.getAntalRabatter() == 0) {
			return "Inga rabattkoder inmatade.";
		} else {
			String rabattkoder = "";
			Iterator itr = k�p.getRabattkoder();
			while (itr.hasNext()) {
				rabattkoder = rabattkoder + (String)itr.next() + ", ";
			}
			return rabattkoder.substring(0, rabattkoder.length() - 2);
		}
	}

	/** @author Robert Olsson */
	private void �ndraProduktPris() {
		System.out.println("\n-- �ndra pris p� produkt --\n");

		int ean = InputHelper.getEAN();
		if (produktRegister.produktExisterar(ean)) {
			System.out.print("Pris: ");
			Double pris = InputHelper.getPrice();
			if (pris > 0) {
				produktRegister.nyttPris(ean, pris);
			} else {
				System.out.println("Ett positivt v�rde m�ste anges.");
			}
		} else {
			System.out.println("Produkten existerar inte.");
		}
	}

	/** @author Robert Olsson */
	private void registreraNyProdukt() {
		int ean;
		String name;
		Double price;
		Date b�stF�re;

		System.out.println("\n-- Registrera ny produkt --\n");

		ean = InputHelper.getEAN();
		if (!produktRegister.produktExisterar(ean)) {
			System.out.print("Name: ");
			name = InputHelper.getString();
			System.out.print("Pris: ");
			price = InputHelper.getPrice();
			System.out.println("Om du vill l�gga till en f�rskvara, ange datum (yyyy-MM-dd), annars tryck <Enter>.");
			System.out.print("Datum: ");
			b�stF�re = InputHelper.getDate();
			if (b�stF�re != null) {
				produktRegister.addProdukt(ean, name, price, b�stF�re);
				System.out.println("Lade till en ny f�rskvara.");
			} else {
				produktRegister.addProdukt(ean, name, price);
				System.out.println("Lade till en ny produkt.");
			}
		} else {
			System.out.println("Produkten existerar redan.");
		}
	}

	/** @author Robert Olsson */
	private void ber�knaK�phistorik() {
		System.out.println("\n-- Ber�kna k�phistorik --\n" +
				"Ange datumintervall (format: " + HISTORIK_DATUM_FORMAT + ") f�r att visa k�phistorik.\n");

		System.out.print("Startdatum: ");
		Date startdatum = InputHelper.getDate(HISTORIK_DATUM_FORMAT);
		System.out.print("Slutdatum: ");
		Date slutdatum = InputHelper.getDate(HISTORIK_DATUM_FORMAT);

		// S�tt kl. 23:59:59 f�r slutDatum:
		if (slutdatum != null)
			slutdatum.setTime(slutdatum.getTime() + 86399999);

		if (historik.k�pLagrat()) {
			System.out.println("\nTotal f�rs�ljning: " + historik.ber�knaF�rs�ljning(k�p, startdatum, slutdatum));
		} else {
			System.out.println("\nIngen k�phistorik tillg�nglig.");
		}
	}

	/** @author Magnus Olovsson & Robert Olsson */
	private void rensaK�pHistorik() {
		String choice;

		System.out.println("Vill du t�mma kvittodatabasen?");
		System.out.print("Ja (J) / Nej (N): ");

		while (true) {
			choice = InputHelper.getString();
			if (choice.toUpperCase().equals("JA") || choice.toUpperCase().equals("J")) {
				switch (historik.t�mHistorik()) {
					case Historik.DATABAS_T�MD:
						System.out.println("Kvittodatabasen har t�mts.");
						break;
					case Historik.DATABAS_FEL_EXISTERAR_EJ:
						System.out.println("Kvittodatabasen existerar inte och kunde d�rf�r inte t�mmas.");
						break;
				}
			} else if (choice.toUpperCase().equals("NEJ") || choice.toUpperCase().equals("N")) {
				break;
			} else {
				System.out.println("Ange Ja (J) eller Nej (N):");
			}
		}
	}
	
	/** 
	 * Utf�r k�pet (dvs. ber�knar rabatter), visar och sparar kvittot
	 * @author Robert Olsson
	 */
	private void sparaOchAvsluta() {
		k�p.utf�rK�p();
		visaKvitto();
		sparaKvitto();
	}

	/** @author Robert Olsson */
	private void sparaKvitto() {
		if (k�p.getAntalProdukter() > 0) {
			try {
				historik.sparaK�p(k�p);
				System.out.println("\nKvittot har sparats.");
			} catch (EOFException e) {
				System.out.println("\nEtt ov�ntat fel uppstod n�r kvittot skulle sparas: " + e.toString());
			} catch (IOException e) {
				System.out.println("\nEtt I/O-fel uppstod n�r kvittot skulle sparas: " + e.toString());
			} catch (ClassNotFoundException e) {
				System.out.println("\nFelaktig klasstyp funnen n�r kvittot skulle sparas: " + e.toString());
			}
		}
	}

}
