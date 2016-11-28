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
		Expression exp = new Expression();
		Answer ans;
		while (true) {
			String s = inSys.nextLine();
			JudgeInput judgein = new JudgeInput();
			final int x = judgein.judgeInput(s);		
			ans = new Answer();
			switch (x) {
			case 3: ans.setAnswer("errorInput"); break;
			case 2: 
				exp = inExpression(s); 
				if (exp.equals("error")) ans.setAnswer("errorExp"); 
				break;
			case 0: // The input is a simplification command
				ans = inSimplify(s, exp); break;
			case 1: // The input is a dirivation command
				ans = inDirivate(s, exp); break;
			}
		}
		
	}

	private static Answer inDirivate(String s, Expression exp) {
		Command com = new Command(s, exp);
		if (com.judge()) {
			Dirivate dir = new Dirivate();
			dir.dirivate(com);			
		}
		return com.getAns();
	}

	private static Answer inSimplify(String s, Expression exp) {
		Command com = new Command(s, exp);
		if (com.judge()) {
			Simplify sim = new Simplify();
			sim.simplify(com);			
		}
		return com.getAns();
	}

	private static Expression inExpression(String s) {
		Expression exp = new Expression(s);
		JudgeExpression judgeExp = new JudgeExpression();
		if (judgeExp.judgeExpression(exp.getExpression()) == false) {
			exp.setExpresssion("error");
		} else {
			exp.splitSquare();
		}		
		return exp;
	}
}
