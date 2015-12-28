package food4fun;

import produkt.ProduktRegister;
import produkt.Produkt;

import försäljning.Köp;
import försäljning.Produktrad;

import java.io.EOFException;

import java.io.IOException;

import java.io.InvalidClassException;

import utils.InputHelper;

import java.util.Date;

import java.util.Iterator;

import rabatt.RabattRegister;

/**
 * MenySystem - presenterar huvudmenyn i applikationen samt ansvarar för val
 * som användaren gör. Inputen ifrån användaren hanteras emellertid i utils.InputHelper.
 *
 * @author Robert Olsson
 */
public class MenySystem {

	private ProduktRegister produktRegister;
	private RabattRegister rabattRegister;
	private Köp köp;
	private Historik historik;

	private final String databasKöp = "köp.dat";
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
		"1. Registrera nytt köp\n" +
		"2. Ange rabattkod\n" +
		"3. Lista produkter\n" +
		"4. Lista varukorg\n" +
		"5. Lista inmatade rabattkoder\n" +
		"6. Ändra pris på produkt\n" +
		"7. Registrera ny produkt\n" +
		"8. Beräkna total försäljning för period\n" +
		"9. Nollställ total försäljning\n" +
		"0. Spara och avsluta";

	/** @author Robert Olsson */
	public MenySystem() {
		produktRegister = new ProduktRegister();
		rabattRegister = new RabattRegister(produktRegister);
		historik = new Historik();
		köp = skapaKöpRegister();

		if (produktRegister != null && köp != null && historik != null) {
			laddaProduktRegister();
		    laddaRabattRegister();
			visaMeny();
		} else {
			System.out.println("Vissa klassinstanser har inte tilldelats korrekt.");
		}
	}
	
	/**
	 * Returnerar ett Köp-objekt, antingen inläst ifrån disk (lagrad Objekt-fil)
	 * eller genom att skapa en ny instans och returnera denna.
	 * 
	 * @author Robert Olsson */
	public Köp skapaKöpRegister() {
		// Läs in Köp-objekt:
		if (historik.köpLagrat()) {
			try {
				return historik.laddaKöp();
			} catch (EOFException e) {
				System.out.println("Historik -> oväntat fel: " + e.toString());
			} catch (InvalidClassException e) {
				System.out.println("Historik -> felaktig klass: " + e.toString());
			} catch (IOException e) {
				System.out.println("Historik -> I/O-fel: " + e.toString());
			} catch (ClassNotFoundException e) {
				System.out.println("Historik -> felaktig klassinstans: " + e.toString());
			}
		}		
		// Skapa nytt Köp-objekt:
		return new Köp(produktRegister, rabattRegister);
	}

	/** @author Robert Olsson */
	private void laddaProduktRegister() {
		int status = produktRegister.laddaDatabaser();
		switch (status) {
			case ProduktRegister.PRODUKTDB_FEL:
				System.out.println("Ett fel uppstod när produktdatabasen laddades.");
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
					registreraKöp();
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
					ändraProduktPris();
					break;
				case REGISTER_NEW_PRODUCT:
					registreraNyProdukt();
					break;
				case GET_PURCHASE_HISTORY:
					beräknaKöphistorik();
					break;
				case RESET_PURCHASE:
					rensaKöpHistorik();
					break;
			}
		}
		
		sparaOchAvsluta();
	}

	/** @author Robert Olsson */
	private void registreraKöp() {
	    Produktrad köptProdukt = null;
		
		System.out.println("\n-- Köpmeny --\n" +
				"Ange EAN. För att avsluta, skriv in -1.");

		int ean = 0;
		while (ean != -1) {
			ean = InputHelper.getEAN(true);
			if (ean != -1) {
				switch (köp.köpProdukt(ean)) {
					case Köp.PRODUKT_EXISTERAR_EJ:
						System.out.println("Produkten existerar inte.");
						break;
					case Köp.PRODUKT_UTGÅNGEN:
						System.out.println("Produktens bäst-före-datum har dessvärre passerat.");
						break;
					case Köp.PRODUKT_TILLAGD_I_VARUKORG:
						köptProdukt = köp.getProduktradEAN(ean);
						System.out.println("Produkten tillagd i varukorgen:");
						System.out.println("\"" + köptProdukt.getProdukt().getNamn() + "\" ");
						break;
					case Köp.PRODUKT_UPPDATERAD_KVANTITET:
						köptProdukt = köp.getProduktradEAN(ean);
						System.out.println("Produkten redan tillagd i varukorgen:");
						System.out.println("\"" + köptProdukt.getProdukt().getNamn() + "\" ");
						System.out.println("Ökar antalet till " + köptProdukt.getAntal());
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

		switch (köp.angeRabattKod(rabattkod)) {
			case RabattRegister.RABATT_INMATAD:
				System.out.println("Rabattkoden har matats in.");
				break;
			case RabattRegister.RABATT_REDAN_INMATAD:
				System.out.println("Rabattkoden är redan aktiv.");
				break;
			case RabattRegister.RABATT_FELAKTIG:
				System.out.println("Felaktig rabattkod angiven.");
				break;
		}
	}

	/** @author Robert Olsson */
	public void visaKvitto() {
		double totalRabatt = köp.getTotalRabatt();
		String produktRader = "";

		System.out.println("\n-- Kvitto --\n");

		if (köp.getAntalProdukter() == 0) {
			System.out.println("Inga produkter i varukorgen.");
		} else {
			produktRader = produktRader + "EAN\tPris\tAntal\tRabatt\tProdukt\tDate";
			for (int i = 0; i < köp.getAntalProdukter(); i++) {
				produktRader = produktRader + "\n" +
						köp.getProduktrad(i).toString(true);
			}
			System.out.println(produktRader);
		}

		köp.setDatum(new Date());
		System.out.println("\nDatum: " + köp.getDatum() + "\nSumma: " + köp.getSumma() + "\nRabatt: " +
					  köp.getTotalRabatt());

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
		if (köp.getAntalRabatter() == 0) {
			return "Inga rabattkoder inmatade.";
		} else {
			String rabattkoder = "";
			Iterator itr = köp.getRabattkoder();
			while (itr.hasNext()) {
				rabattkoder = rabattkoder + (String)itr.next() + ", ";
			}
			return rabattkoder.substring(0, rabattkoder.length() - 2);
		}
	}

	/** @author Robert Olsson */
	private void ändraProduktPris() {
		System.out.println("\n-- Ändra pris på produkt --\n");

		int ean = InputHelper.getEAN();
		if (produktRegister.produktExisterar(ean)) {
			System.out.print("Pris: ");
			Double pris = InputHelper.getPrice();
			if (pris > 0) {
				produktRegister.nyttPris(ean, pris);
			} else {
				System.out.println("Ett positivt värde måste anges.");
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
		Date bästFöre;

		System.out.println("\n-- Registrera ny produkt --\n");

		ean = InputHelper.getEAN();
		if (!produktRegister.produktExisterar(ean)) {
			System.out.print("Name: ");
			name = InputHelper.getString();
			System.out.print("Pris: ");
			price = InputHelper.getPrice();
			System.out.println("Om du vill lägga till en färskvara, ange datum (yyyy-MM-dd), annars tryck <Enter>.");
			System.out.print("Datum: ");
			bästFöre = InputHelper.getDate();
			if (bästFöre != null) {
				produktRegister.addProdukt(ean, name, price, bästFöre);
				System.out.println("Lade till en ny färskvara.");
			} else {
				produktRegister.addProdukt(ean, name, price);
				System.out.println("Lade till en ny produkt.");
			}
		} else {
			System.out.println("Produkten existerar redan.");
		}
	}

	/** @author Robert Olsson */
	private void beräknaKöphistorik() {
		System.out.println("\n-- Beräkna köphistorik --\n" +
				"Ange datumintervall (format: " + HISTORIK_DATUM_FORMAT + ") för att visa köphistorik.\n");

		System.out.print("Startdatum: ");
		Date startdatum = InputHelper.getDate(HISTORIK_DATUM_FORMAT);
		System.out.print("Slutdatum: ");
		Date slutdatum = InputHelper.getDate(HISTORIK_DATUM_FORMAT);

		// Sätt kl. 23:59:59 för slutDatum:
		if (slutdatum != null)
			slutdatum.setTime(slutdatum.getTime() + 86399999);

		if (historik.köpLagrat()) {
			System.out.println("\nTotal försäljning: " + historik.beräknaFörsäljning(köp, startdatum, slutdatum));
		} else {
			System.out.println("\nIngen köphistorik tillgänglig.");
		}
	}

	/** @author Magnus Olovsson & Robert Olsson */
	private void rensaKöpHistorik() {
		String choice;

		System.out.println("Vill du tömma kvittodatabasen?");
		System.out.print("Ja (J) / Nej (N): ");

		while (true) {
			choice = InputHelper.getString();
			if (choice.toUpperCase().equals("JA") || choice.toUpperCase().equals("J")) {
				switch (historik.tömHistorik()) {
					case Historik.DATABAS_TÖMD:
						System.out.println("Kvittodatabasen har tömts.");
						break;
					case Historik.DATABAS_FEL_EXISTERAR_EJ:
						System.out.println("Kvittodatabasen existerar inte och kunde därför inte tömmas.");
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
	 * Utför köpet (dvs. beräknar rabatter), visar och sparar kvittot
	 * @author Robert Olsson
	 */
	private void sparaOchAvsluta() {
		köp.utförKöp();
		visaKvitto();
		sparaKvitto();
	}

	/** @author Robert Olsson */
	private void sparaKvitto() {
		if (köp.getAntalProdukter() > 0) {
			try {
				historik.sparaKöp(köp);
				System.out.println("\nKvittot har sparats.");
			} catch (EOFException e) {
				System.out.println("\nEtt oväntat fel uppstod när kvittot skulle sparas: " + e.toString());
			} catch (IOException e) {
				System.out.println("\nEtt I/O-fel uppstod när kvittot skulle sparas: " + e.toString());
			} catch (ClassNotFoundException e) {
				System.out.println("\nFelaktig klasstyp funnen när kvittot skulle sparas: " + e.toString());
			}
		}
	}

}
