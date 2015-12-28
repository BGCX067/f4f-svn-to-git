package rabatt;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import produkt.Produkt;

/** @author Pierre Odengard */
public abstract class ProduktRabatt extends Rabatt {

	private final int INIT_SIZE = 5;
	private List<Produkt> produkter;

	/** @author Pierre Odengard */
	public ProduktRabatt(String rabattKod, Date användsFöre) {
		super(rabattKod, användsFöre);
		produkter = new ArrayList<Produkt>(INIT_SIZE);
	}

	/** @author Pierre Odengard */
	protected void addProdukt(Produkt produkt) {
		produkter.add(produkt);

	}

	/** @author Pierre Odengard */
	protected Produkt getProdukt(int i) {
		return produkter.get(i);
	}

}
