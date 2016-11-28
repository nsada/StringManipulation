package Lab7;

public class OperateString {
	/**
	 * Judge whether char a is a operative symbol. 
	 * @param a char
	 * @return true or false
	 */
	public static boolean isSymbol(final char a) {
		return (a == '+' || a == '*' || a == '-' || a == '^');
	}
	/**
	 * Judge whether char a is a number.
	 * 
	 * @param scanCharacter char
	 * @return true or false
	 */
	public static boolean isNumber(final char  scanCharacter) {
		return (scanCharacter >= '0' && scanCharacter <= '9');
	}

	/**
	 * Judge whether char a is a letter.
	 * 
	 * @param a char
	 * @return true or false
	 */
	public static boolean isLetter(final char a) {
		return ((a >= 'A' && a <= 'Z') || (a >= 'a' && a <= 'z'));
	}
	
	/**
	 * Get a substring of number at the start position i in the string input.
	 * 
	 * @param input
	 *            string
	 * @param index
	 *            int
	 * @return substring
	 */
	public static String getNumStr(final String input, final int index) {
		int j = index + 1;
		while (j < input.length() && isNumber(input.charAt(j))) {
			j++;
		}
		return input.substring(index, j);
	}	
	
	/**
	 * Get a substring of variable at the start position i in the string input.
	 * 
	 * @param input
	 *            string
	 * @param i
	 *            int
	 * @return substring
	 */
	public static String getVarStr(final String input, final int i) {
		int j = i + 1;
		while (j < input.length() && isLetter(input.charAt(j))) {
			j++;
		}
		return input.substring(i, j);
	}	
}
