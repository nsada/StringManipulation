package Lab7;
//  Generated by StarUML(tm) Java Add-In
//
//  @ Project : Untitled
//  @ File Name : ���ʽʵ����.java
//  @ Date : 2016/11/28
//  @ Author : 

public class Expression {
	private static String exp;
	private static OperateString op = new OperateString();
	
	public Expression() {
		exp = "";
	}
	public Expression(String exp) {
		this.exp = exp;
		deleteTab();
		reMul();
	}
	
	public void deleteTab() {
		String newString = "";
		for (int i = 0; i < exp.length(); i++) {
			if (exp.charAt(i) == ' ' || exp.charAt(i) == '\t') {
				continue;
			}
			newString = newString + exp.charAt(i);
		}
		exp = newString;
	}
	/**
	 * To show '*', like "3x" to "3*x".
	 * 
	 * @param input
	 *            string
	 * @return newString
	 */
	public static void reMul() {
		String newString = "";
		OperateString ops = new OperateString();
		for (int i = 0; i < exp.length(); i++) {
			if (ops.isNumber(exp.charAt(i))) {
				final String num = ops.getNumStr(exp, i);
				newString = newString + num;
				final int len = num.length();
				if ((i + len) < exp.length() 
						&& ops.isLetter(exp.charAt(i + len))) {
					newString = newString + '*';
				}
				i = i + len - 1;
			} else {
				newString = newString + exp.charAt(i);
			}
		}
		exp = newString;
	}
	
	public static boolean judge(String expression) {
		int cntNum = 0, cntSymbol = 0;
		char currentChar = '*';
		boolean errorDetected = false;
		if (expression.length() < 1 || op.isSymbol(expression.charAt(0)) || op.isSymbol(expression.charAt(expression.length() - 1))) {
		//symbol not allowed at the head or tail of expression
			errorDetected = true;
		}else{
			for (int i = 0; i < expression.length(); i++) {
				currentChar = expression.charAt(i);
				if (op.isNumber(currentChar)) {
					final String l =op.getNumStr(expression, i);
					if ((i + l.length() < expression.length()) 
							&& 	expression.charAt(i + l.length()) == '^') {
						// Avoid "2^y",etc
						errorDetected = true; 
						}
					i = i + l.length() - 1; // skip the detected number
					cntNum = cntNum + l.length();
					cntSymbol = 0;
				} else if (op.isLetter(currentChar)) {
					final String l = op.getVarStr(expression, i); 
					final int len = l.length();
					if (i + len < expression.length() && expression.charAt(i + len) == '^') {
						// Avoid situation like "y^2^2",etc
						if (op.isNumber(expression.charAt(i + len + 1))) {
							final String ll = op.getNumStr(expression, i + len + 1);
							if ((i + len + 1 + ll.length() < expression.length())
									&& (expression.charAt(i + len + 1 + ll.length()) == '^')) {
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
				} else if (cntSymbol == 0 && op.isSymbol(currentChar)) { 
					// Avoid continues symbols
					cntSymbol++;
					cntNum = 0;
				} else {
					errorDetected = true;
				}
			}
		}
		
		return !errorDetected;
	}
	
	public void mergeExp(String expression) {
	
	}
	
	public String getExpression() {
		return exp;
	}
	
	public void setExpresssion(String newExp) {
	
	}
}
