package Lab7;

public class OperateString {
	private static final int MAXVARCOUNT = 200;
	
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
	
	 /**
	 * To judge whether a variable is in the string input.
	 * 
	 * @param input
	 *            string
	 * @return 0 or 1
	 */
	public static int haveVar(final String input) {
		int letterFlag = 0;
		for (int i = 0; i < input.length(); i++) {
			if (isLetter(input.charAt(i))) {
				letterFlag = 1;
			}
		}
		return letterFlag;
	}
	
	 /**
	 * To calculate the number of variable x which shows in the String input.
	 * 
	 * @param input
	 *            string
	 * @param x
	 *            string
	 * @return the number of var x
	 */
	public static int havex(final String input, final String x) {
		int cnt = 0;
		String var = "";
		for (int i = 0; i < input.length(); i++) {
			if (isLetter(input.charAt(i))) {
				var = getVarStr(input, i);
				if (x.equals(var)) {
					cnt++;
				}
				i = i + var.length() - 1;
			}
		}
		return cnt;
	}
	
	/**
	 * A funcition to transform '^' to '*' in the expression.
	 * 
	 * @param input
	 *            string
	 * @return newString
	 */
	public static String splitSquare(final String input) {
		// XXX: i dont see that coming... is that a good practice?
		String newString = "";
		for (int i = 0; i < input.length(); i++) {
			if (isLetter(input.charAt(i))) {
				final String var = getVarStr(input, i);
				newString = newString + var;
				final int len = var.length();

				if ((i + len) < input.length() 
						&& input.charAt(i + len) == '^') {
					final String n = getNumStr(input, i + len + 1);
					final int num = Integer.parseInt(n);
					for (int j = 0; j < num - 1; j++) {
						newString = newString + '*' + var;
					}
					i = i + len + n.length();
				} else {
					i = i + len - 1;
				}
			} else {
				newString = newString + input.charAt(i);
			}
		}

		return newString;
	}
	
	/**
	 * To simplify an addition expression.
	 * 
	 * @param input
	 *            string
	 * @return newString
	 */
	public static String mergePlus(String input) {
		final String[] count = input.split("\\+");
		String temp = "", newString = "";
		int sum = 0;
		String numstr = "";
		for (int i = 0; i < count.length; i++) {
			temp = mergeSub(count[i]);
			// System.out.print("temp: ");System.out.println(temp);
			if (haveVar(temp) == 0) {
				sum += Integer.parseInt(temp);
			} else if (temp.charAt(0) == '-') {
				if (isNumber(temp.charAt(1))) {
					numstr = getNumStr(temp, 1);
					final int j = numstr.length() + 1;
					// System.out.println(temp.charAt(j));
					if (temp.charAt(j) == '*') {
						newString = newString + temp;
					} else {
						sum -= Integer.parseInt(numstr);
						newString = newString + temp.substring(j);
					}
				} else {
					newString = newString + temp;
				}
			} else if (isNumber(temp.charAt(0))) {
				numstr = getNumStr(temp, 0);
				final int j = numstr.length();
				if (temp.charAt(j) == '-') {
					sum += Integer.parseInt(numstr);
					newString = newString + temp.substring(j);
				} else {
					newString = newString + '+' + temp;
				}
			} else {
				newString = newString + '+' + temp;
			}
			// System.out.print("newString: ");System.out.println(newString);
			// System.out.print("sum: ");System.out.println(sum);
		}
		if (newString.length() == 0) {
			newString = Integer.toString(sum);
			// return newString;
		} else {
			if (sum != 0) {
				newString = sum + newString;
			} else if (newString.length() <= 1) {
				newString = "0";
			} else if (newString.charAt(0) != '-') {
				newString = newString.substring(1, newString.length());
			}
		}
		// System.out.print("mergePlus: ");System.out.println(newString);
		return newString;
	}
	
	/**
	 * To simplify a subtraction expression.
	 * 
	 * @param input
	 *            string
	 * @return newString
	 */
	public static String mergeSub(final String input) {
		int sum = 0;
		final String[] count = input.split("\\-");
		String temp = "", newString = "";
		for (int i = 0; i < count.length; i++) {
			if (count[i].length() == 0) {
				continue;
			}
			temp = mergeMul(count[i]);
			temp = mergeSquare(temp);
			// System.out.println("mergeSub temp: "+temp);
			if (haveVar(temp) == 0) {
				if (i == 0) {
					sum += Integer.parseInt(temp);
				} else {
					sum -= Integer.parseInt(temp);
				}
			} else {
				if (i == 0) {
					newString = temp;
				}	else {
					newString = newString + '-' + temp;
				}
			}
			/*
			 * System.out.print(temp); System.out.print(' ');
			 * System.out.println(newString);
			 */
		}
		if (newString.length() == 0) {
			newString = Integer.toString(sum);
		} else {
			if (!isSymbol(newString.charAt(0)) && sum != 0) {
				newString = '+' + newString;
			}
			if (sum != 0) {
				newString = sum + newString;
			} else if (newString.length() == 1 
					&& isSymbol(newString.charAt(0))) {
				newString = "0";
			}
		}
		// System.out.print("mergeSub: ");System.out.println(newString);
		return newString;
	}

	/**
	 * To simplify a multiplication expression.
	 * 
	 * @param input
	 *            string
	 * @return newString
	 */
	public static String mergeMul(final String input) {
		String newString = "";
		String sub = "";
		boolean zeroTerm = false;
		int mul = 1;
		for (int i = 0; i < input.length(); i++) {
			if (isNumber(input.charAt(i))) {
				sub = getNumStr(input, i);
				final int num = Integer.parseInt(sub);
				if (num == 0) {
					zeroTerm = true;
				}
				mul *= num;
			} else if (isLetter(input.charAt(i))) {
				sub = getVarStr(input, i);
				newString = newString + '*' + sub;
			} else {
				sub = "*";
			}
			i = i + sub.length() - 1;
		}
		if (mul != 1) {
			newString = mul + newString;
		} else if (newString.length() <= 1) {
			newString = "1";
		} else {
			newString = newString.substring(1, newString.length());
		}	
		if (zeroTerm) {
			newString = "0";
		}
		return newString;
	}

	/**
	 * To merge continues '*' to '^'.
	 * 
	 * @param input
	 *            string
	 * @return sub
	 */
	public static String mergeSquare(final String input) {
		String[]	var = new String[MAXVARCOUNT];
		String 		sub = "";
		int[] 		cntvar = new int[MAXVARCOUNT];
		int cnt = 0; // XXX: what does this mean?
		boolean havenum = false;
		for (int j = 0; j < MAXVARCOUNT; j++) {
			cntvar[j] = 0; // XXX: obsolete initialization
		}

		for (int j = 0; j < input.length(); j++) {
			boolean haveVar = false;
			if (isNumber(input.charAt(j))) {
				final String num = getNumStr(input, j);
				sub = num + sub;
				j = j + num.length() - 1;
				havenum = true;
			} else if (isLetter(input.charAt(j))) {
				final String v = getVarStr(input, j);
				// ------------------
				// original FIXME:obsolete initialization
				// int k = 0;
				int k;
				// ------------------
				for (k = 0; k < cnt; k++) {
					if (var[k].equals(v)) {
						haveVar = true;
						break;
					}
					//XXX: fixed deadcode
				}
				if (haveVar) {
					cntvar[k]++;
				} else {
					var[cnt] = v;
					cntvar[cnt]++;
					cnt++;
				}
				j = j + v.length() - 1;
			}
		}
		for (int j = 0; j < cnt; j++) {
			if (cntvar[j] > 1) {
				sub = sub + '*' + var[j] + '^' + cntvar[j];
			} else {
				sub = sub + '*' + var[j];
			}
		}
		if (!havenum) {
			sub = sub.substring(1, sub.length());
		}
		// System.out.println(sub);
		return sub;
	}


}
