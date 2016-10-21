package finalversion;

import java.util.Scanner;

//import java.util.*;


/**
 * 
 * simplify and derivate a expression accordingly.
 *
 */
public final class Calculation {
	/**
	 * control the max variable count.
	 */
	private static final int MAXVARCOUNT = 200;
	
	/**
	 * stores the parameters of the expression.
	 */
	private static int[] value = new int[MAXVARCOUNT];


	/**
	 * default constructor.
	 * prevents instantiation
	 */
	
	private Calculation() {
	    throw new AssertionError("Instantiating utility class...");

	}
	/**
	 * initialize the value array.
	 * 
	 * @input void
	 */
		
	public static void initValue() {
		for (int i = 0; i < MAXVARCOUNT; i++) {
			value[i] = 0; }
	}

	/**
	 * read one input string end with enter.
	 * 
	 * @return input string
	 */
	public static String read() {
		final Scanner in = new Scanner(System.in);
		final String input = in.nextLine();
		in.close(); //fixed resource leak.
		return input;
	}

	/**
	 * judge is to judge the type of input string.
	 * 
	 * @param input
	 *            the string user typed in the console
	 * @return 0 for simplify command or 1 for derivation or 2 for expression or
	 *         3 for error
	 */
	public static int judge(final String input) {
		final int 	minLength = 6;
		int 		inputType;
		if (input.charAt(0) == '!') {
			if (input.length() < minLength) {
				inputType = 3; // The command is too short, so error
			}
			if (input.substring(1, 4).equals("d/d")) {
				inputType = 1; // It is a derivation command
			} else if (input.substring(1, 9).equals("simplify")) {
				inputType = 0; // It is simplification command
			} else {
				inputType = 3; // Error
			}
		} else {
			inputType = 2; // Expression
		}
		return inputType;
	}

	/**
	 * Judge whether char a is a number.
	 * 
	 * @param scanCharacter
	 *            char
	 * @return true or false
	 */
	public static boolean isNumber(final char  scanCharacter) {
		return (scanCharacter >= '0' && scanCharacter <= '9');
	}

	/**
	 * Judge whether char a is a letter.
	 * 
	 * @param a
	 *            char
	 * @return true or false
	 */
	public static boolean isLetter(final char a) {
		return ((a >= 'A' && a <= 'Z') || (a >= 'a' && a <= 'z'));
	}

	/**
	 * Judge whether char a is a operative symbol.
	 * 
	 * @param a
	 *            char
	 * @return true or false
	 */
	public static boolean isSymbol(final char a) {
		return (a == '+' || a == '*' || a == '-' || a == '^');
	}

	/**
	 * Judge the expression is legal.
	 * 
	 * @param fun
	 *            String
	 * @return true or false
	 */
	public static boolean judgeFun(final String fun) {
		int cntNum = 0, cntSymbol = 0;
		char currentChar = '*';
		boolean errorDetected = false;
		if (isSymbol(fun.charAt(0)) || isSymbol(fun.charAt(fun.length() - 1))) {
		//symbol not allowed at the head or tail of expression
			errorDetected = true;
		}
		for (int i = 0; i < fun.length(); i++) {
			currentChar = fun.charAt(i);
			if (isNumber(currentChar)) {
				final String l = getNumStr(fun, i);
				if ((i + l.length() < fun.length()) 
						&& 	fun.charAt(i + l.length()) == '^') {
					// Avoid "2^y",etc
					errorDetected = true; 
					}
				i = i + l.length() - 1; // skip the detected number
				cntNum = cntNum + l.length();
				cntSymbol = 0;
			} else if (isLetter(currentChar)) {
				final String l = getVarStr(fun, i); 
				final int len = l.length();
				if (i + len < fun.length() && fun.charAt(i + len) == '^') {
					// Avoid situation like "y^2^2",etc
					if (isNumber(fun.charAt(i + len + 1))) {
						final String ll = getNumStr(fun, i + len + 1);
						if ((i + len + 1 + ll.length() < fun.length())
								&& (fun.charAt(i + len + 1 + ll.length()) == '^')) {
							errorDetected = true;
						}

					} else {
						// if power is not a number
						errorDetected = true;
					}
				}
				i = i + len - 1;
				cntNum = 0;
				cntSymbol = 0;
			} else if (cntSymbol == 0 && isSymbol(currentChar)) { 
				// Avoid continues symbols
				cntSymbol++;
				cntNum = 0;
			} else {
				errorDetected = true;
			}
		}
		
		return !errorDetected;
	}

	/**
	 * simplify function.
	 * 
	 * @param input
	 *            simplification command
	 * @param fun
	 *            the expression to simplify
	 * @return the string simplified
	 */
	public static String simplify(final String input, final String fun) {
		initValue();
		String newString = "";
		boolean errorDetected = false;
		final String[] count = input.split(" "); 
		final int num = count.length;
		for (int i = 0; i < num; i++) {
			if (count[i].equals("")) {
				errorDetected = true;
			} else if (count[i].charAt(0) == ' ' || count[i].charAt(0) == '=') {
				errorDetected = true;
			}
		}
		String[] var = new String[num - 1];
		for (int i = 1; i < num; i++) {
			var[i - 1] = getVarStr(count[i], 0);
			final int len = count[i].length();
			final String n = count[i].substring(var[i - 1].length() + 1, len);
			final int v = Integer.parseInt(n);
			value[i - 1] = v;
		}

		String x = "";
		for (int i = 0; i < fun.length(); i++) {
			if (isLetter(fun.charAt(i))) {
				x = getVarStr(fun, i);
				boolean havevalue = false, havesquare = false;
				for (int j = 0; j < num - 1; j++) {
					if (x.equals(var[j])) {
						newString = newString + value[j];
						havevalue = true;
						break;
					} else if ((i + x.length()) < fun.length() 
							&& fun.charAt(i + x.length()) == '^') {
						final String l = getNumStr(fun, i + x.length() + 1);
						i = i + 1 + l.length();
						newString = newString + x + '^' + l;
						havesquare = true;
					}
				}
				if (!havevalue && !havesquare) {
					newString = newString + x;
				}
				i = i + x.length() - 1;
			} else {
				newString = newString + fun.charAt(i);
			}
		}
		// System.out.println(newString);
		if (errorDetected) {
			newString = "error";
		}
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
	 * To simplify an addition expression.
	 * 
	 * @param input
	 *            string
	 * @return newString
	 */
	public static String mergePlus(final String input) {
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
	 * Diff a expression which includes subtraction.
	 * 
	 * @param input
	 *            string
	 * @param x
	 *            string
	 * @return str
	 */
	public static String derivationSub(final String input, final String x) {
		String newString = "", str = "";
		final String[] count = input.split("\\-");
		if (havex(input, x) == 0) {
			str = "0";
		} else {
			// int sum = 0;
			String temp = "", numstr = "", sub = "";
			for (int i = 0; i < count.length; i++) {
				int mul = 1;
				int cal = 0;
				temp = "";
				numstr = "1";
				newString = "";
				cal = havex(count[i], x);
				if (cal != 0) {
					temp = mergeMul(count[i]);
					int k = 0;
					if (isNumber(temp.charAt(0))) {
						numstr = getNumStr(temp, 0);
						k = numstr.length();
					}
					for (int j = k; j < temp.length(); j++) {
						if (isLetter(temp.charAt(j))) {
							sub = getVarStr(temp, j);
							if (!sub.equals(x)) {
								newString = newString + '*' + sub;
							}
							j = j + sub.length() - 1;
						}
					}
				}
				mul *= Integer.parseInt(numstr) * cal;
				newString = mul + newString;
				for (int j = 0; j < cal - 1; j++) {
					newString = newString + '*' + x;
				}
				newString = mergeSquare(newString);
				str = str + '-' + newString;
			}
			str = str.substring(1, str.length());
			str = splitSquare(str);
			str = mergePlus(str);
		}
		return str;
	}

	/**
	 * derivation.
	 * 
	 * @param input
	 *            string
	 * @param x
	 *            string
	 * @return str
	 */
	public static String derivation(final String input, final String x) {
		String str = "";
		if (havex(input, x) == 0) {
			str = "0";
		} else {

			final String[] count = input.split("\\+");
			String temp = "";

			for (int i = 0; i < count.length; i++) {
				int cal = 0;
				temp = "";
				cal = havex(count[i], x);
				if (cal != 0) {
					temp = derivationSub(count[i], x);
					str = str + '+' + temp;
					// System.out.print("temp: ");System.out.println(temp);
				}
			}
			if (str.charAt(0) == '+') {
				str = str.substring(1);
			}
			// System.out.println(str);
			str = splitSquare(str);
			// System.out.println(str);
			str = mergePlus(str);
		}
		return str;
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

	/**
	 * To delete the space key and tab key in the expression.
	 * 
	 * @param input
	 *            string
	 * @return newString
	 */
	public static String deleteTab(final String input) {
		String newString = "";
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == ' ' || input.charAt(i) == '\t') {
				continue;
			}
			newString = newString + input.charAt(i);
		}
		return newString;
	}

	/**
	 * To show '*', like "3x" to "3*x".
	 * 
	 * @param input
	 *            string
	 * @return newString
	 */
	public static String reMul(final String input) {
		String newString = "";
		for (int i = 0; i < input.length(); i++) {
			// TODO: what if number after letter?
			if (isNumber(input.charAt(i))) {
				final String num = getNumStr(input, i);
				newString = newString + num;
				final int len = num.length();
				if ((i + len) < input.length() 
						&& isLetter(input.charAt(i + len))) {
					newString = newString + '*';
				}
				i = i + len - 1;
			} else {
				newString = newString + input.charAt(i);
			}
		}
		return newString;
	}

	/**
	 * The main function.
	 * 
	 * @param args
	 *            string
	 */
	public static void main(final String[] args) {

		System.out.println("Please input an expression for operation");

		initValue();
		String fun = "", newString = "";
		while (true) {
			String s = read();
			if (s.equals("")) { // If it is a blank string
				System.out.println("Error, wrong input!");
				continue;
			}

			// System.out.println(s);
			final int x = judge(s);
			if (x == 2) { // The input is a expression
				s = deleteTab(s);
				s = reMul(s);
				if (!judgeFun(s)) {
					System.out.println("Error, wrong expression!");
					continue;
				}
				fun = splitSquare(s);
				System.out.println(fun);
			} else if (x == 0) { // The input is a simplification command
				newString = simplify(s, fun);
				if (newString.equals("error")) {
					System.out.println("Error, wrong command!");
					continue;
				}
				// System.out.println("newString: "+newString);
				newString = mergePlus(newString);
				System.out.println(newString);
			} else if (x == 1) { // The input is a diff command
				if (!isLetter(s.charAt(5))) {
					System.out.println("Error, wrong command!");
					continue;
				}
				final String variable = getVarStr(s, 5);
				if (5 + variable.length() < s.length()) {
					System.out.println("Error, wrong command!");
					continue;
				}
				newString = derivation(fun, variable);
				// newString = mergeSquare(newString);
				System.out.println(newString);
			} else if (x == 3) { // Error input
			
				System.out.println("Error, wrong input!");
			}
		}
	}
}
