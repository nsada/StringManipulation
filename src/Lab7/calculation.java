package Lab7;

import java.util.Scanner;

import Control.Derivate;
import Control.JudgeExpression;
import Control.JudgeInput;
import Control.OutputAnswer;
import Control.Simplify;
import Entity.Answer;
import Entity.Command;
import Entity.Expression;




/**
 * simplify and derivate a expression accordingly.
 */
public final class calculation {

	public static void main(final String[] args) {	
		//System.out.println("ha");
		System.out.println("Please input an expression for operation");		
		final Scanner inSys = new Scanner(System.in);		
		
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
				else ans.setAnswer(exp.getExp());
				break;
			case 0: // The input is a simplification command
				ans = inSimplify(s, exp); break;
			case 1: // The input is a derivation command
				ans = inDerivate(s, exp); break;
			}
			OutputAnswer outputAns = new OutputAnswer(ans);
		}
		
	}

	private static Answer inDerivate(String s, Expression exp) {
		Command com = new Command(s, exp);
		if (com.judgeDerivate()) {
			Derivate dir = new Derivate();
			dir.derivate(com);			
		} else {
			com.getAns().setAnswer("Error, wrong derivate command!");
		}
		return com.getAns();
	}

	private static Answer inSimplify(String s, Expression exp) {
		Command com = new Command(s, exp);
		if (com.judgeSimplify()) {
			Simplify sim = new Simplify();
			sim.simplify(com);			
		} else {
			com.getAns().setAnswer("Error, wrong simplify command!");
		}
		return com.getAns();
	}

	private static Expression inExpression(String s) {
		Expression exp = new Expression(s);
		JudgeExpression judgeExp = new JudgeExpression();
		if (judgeExp.judgeExpression(exp.getExp()) == false) {
			exp.setExp("error");
		} else {
			exp.splitSquare();
		}		
		return exp;
	}
}
