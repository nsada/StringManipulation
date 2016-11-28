package Lab7;

import java.util.Scanner;

/**
 * simplify and derivate a expression accordingly.
 */
public final class calculation {

	public static void main(final String[] args) {		
		System.out.println("Please input an expression for operation");		
		final Scanner inSys = new Scanner(System.in);		
		
		String fun = "", newString = "";
		while (true) {
			String s = inSys.nextLine();
			JudgeInput judgein = new JudgeInput();
			final int x = judgein.judgeInput(s);
			
			if (x == 3) { // If it is a blank string
				System.out.println("Error, wrong input!");
				continue;
			}

			// System.out.println(s);
			
			if (x == 2) { // The input is a expression
				Expression exp = new Expression(s);
				JudgeExpression judgeExp = new JudgeExpression();
				;
				if (judgeExp.judgeExpression(exp.getExpression())) {
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
