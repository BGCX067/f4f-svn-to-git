package produkt;

import food4fun.ProduktDatabas;

import java.io.File;
import java.io.IOException;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * ProduktRegister hanterar alla Produkt-objekt i systemet; l�gga till, f�r�ndra eller radera.
 * Kontroll om en viss produkt existerar etc. hanteras ocks� i ProduktRegister.
 * �ven inl�sning av txt-filerna sker i ProdukterRegister, dock via den n�stlade klassen Databas.
 *
 * @author Magnus Olovsson & Robert Olsson
 */
public class ProduktRegister implements Serializable {

	public static final int PRODUKTDB_LADDAD = 0, PRODUKTDB_FEL = -1;

	private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private final String databasProdukter = "produkter.txt";
	private final String databasF�rskvaror = "farskvaror.txt";

	private final int INIT_SIZE = 20;
	private List<Produkt> produkter;

	/** @author Robert Olsson */
	public ProduktRegister() {
		produkter = new ArrayList<Produkt>(INIT_SIZE);
	}

	/** @author Magnus Olovsson */
	public void addProdukt(int ean, String name, Double price) {
		produkter.add(new Produkt(ean, name, price));
	}

	/** @author Robert Olsson */
	public void addProdukt(int ean, String name, Double price, Date bestBeforeDate) {
		produkter.add(new F�rskvara(ean, name, price, bestBeforeDate));
	}

	/** @author Magnus Olovsson */
	public void removeProdukt(int ean) {
		for (int i = 0; i < produkter.size(); i++) {
			if (produkter.get(i).getEAN() == ean) {
				produkter.remove(i);
				break;
			}
		}
	}

	/** @author Robert Olsson */
	public Iterator getProdukter() {
		return produkter.iterator();
	}

	/** @author Robert Olsson */
	public boolean produktExisterar(int ean) {
		Produkt produkt = getProdukt(ean);
		return produkt == null ? false : true;
	}

	/** @author Magnus Olovsson */
	public Produkt getProdukt(int ean) {
		for (int i = 0; i < produkter.size(); i++) {
			if (ean == produkter.get(i).getEAN())
				return produkter.get(i);
		}
		return null;
	}

	/**@author Magnus Olovsson */
	public void nyttPris(int ean, double nyttPris) {
		for (int i = 0; i < produkter.size(); i++) {
			if (produkter.get(i).getEAN() == ean) {
				produkter.get(i).setPris(nyttPris);
			}
		}
	}

	/** @author Robert Olsson */
	public int laddaDatabaser() {
		return laddaDatabas(databasProdukter) && laddaDatabas(databasF�rskvaror)
			? PRODUKTDB_LADDAD : PRODUKTDB_FEL;
	}

	/**
	 * Loads the product database and add them to the ProduktRegister class.
	 * Handles objects of type "Produkt" and "F�rskvara".
	 * @author Robert Olsson
	 */
	private boolean laddaDatabas(String s�kv�g) {
		int ean;
		String namn;
		Double pris;
		Date b�stF�reDatum = null;

		try {
			Scanner input = new Scanner(new File(s�kv�g)).useDelimiter("\n");
			input.nextLine();
			while (input.hasNext()) {
				String[] line = input.next().split("\\t");
				if (line.length < 3)
					break;

				ean = Integer.parseInt(line[0].trim());
				namn = line[1].trim();
				pris = Double.parseDouble(line[2].trim().replace(',', '.'));

				if (line.length == 4 && !line[3].trim().equals("")) {
					try {
						b�stF�reDatum = df.parse(line[3].trim());
						addProdukt(ean, namn, pris, b�stF�reDatum);
					} catch (ParseException e) {
						return false;
					}
				} else {
					addProdukt(ean, namn, pris);
				}
			}
			input.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

}
