package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The InputHelper class makes it easier to get proper input values from the user
 * It's static so one can refer it directly, without instancing it
 *
 * @author Robert Olsson
 */
public class InputHelper {

	/** @author Robert Olsson */
	public static int getInteger() {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			try {
				int input = scanner.nextInt();
				return input;
			} catch (InputMismatchException e) {
				System.out.println("Du måste ange ett heltal. Försök igen:");
				scanner.next();
			}
		}
	}

	/** @author Robert Olsson */
	public static double getDouble() {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			try {
				double input = scanner.nextDouble();
				return input;
			} catch (InputMismatchException e) {
				System.out.println("Du måste ange ett flyttal. Försök igen:");
				scanner.next();
			}
		}
	}

	/** @author Robert Olsson */
	public static String getString() {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			try {
				String input = scanner.nextLine();
				return input.trim();
			} catch (InputMismatchException e) {
				System.out.println("Du måste ange en korrekt sträng. Försök igen:");
				scanner.next();
			}
		}
	}

	/** @author Robert Olsson */
	public static int getEAN(boolean allowEscaping) {
		while (true) {
			System.out.print("EAN: ");
			int ean = getInteger();
			if ((allowEscaping && ean == -1) || (ean > 0 && ean < 999999)) {
				return ean;
			} else {
				System.out.println("EAN-numret måste ligga inom intervallet 1 - 999 999. Försök igen:");
			}
		}
	}

	/** @author Robert Olsson */
	public static int getEAN() {
		return getEAN(false);
	}

	/** @author Robert Olsson */
	public static double getPrice() {
		while (true) {
			double price = getDouble();
			if (price > 0) {
				return price;
			} else {
				System.out.println("Priset får inte vara negativt. Försök igen:");
			}
		}
	}

	/** @author Robert Olsson */
	public static Date getDate(String format) {
		DateFormat df = new SimpleDateFormat(format);
		String dateString = getString();

		try {
			return df.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}

	/** @author Robert Olsson */
	public static Date getDate() {
		return getDate("yyyy-MM-dd");
	}

}
