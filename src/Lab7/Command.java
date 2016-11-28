package Lab7;
//  Generated by StarUML(tm) Java Add-In
//
//  @ Project : Untitled
//  @ File Name : ����ʵ����.java
//  @ Date : 2016/11/28
//  @ Author : 

public class Command {
	private String command;
	private Expression exp;
	private Answer ans;
	private static OperateString op = new OperateString();
	private static final int MAXVARCOUNT = 200;
	private static int[] value = new int[MAXVARCOUNT];
	
	public Command(String command, Expression exp) {
		this.command = command;
		this.exp = exp;
		this.ans = new Answer();
	}

	public boolean judgeSimplify() {
		boolean errorDetected = false;
		final String[] count = command.split(" "); 
		final int num = count.length;
		for (int i = 0; i < num; i++) {
			if (count[i].equals("")) {
				errorDetected = true; break;
			} else if (count[i].charAt(0) == ' ' || count[i].charAt(0) == '=') {
				errorDetected = true; break;
			} else if (op.isLetter(count[i].charAt(0))) {
				String var = op.getVarStr(count[i], 0);
				if (var.length() == count[i].length()) {
					errorDetected =true; break;
				} else if (var.length()+1 >= count[i].length()) {
					errorDetected = true; break;
				} else if (op.isNumber(count[i].charAt(var.length()+1))) {
					String number = op.getNumStr(count[i], var.length()+1);
					if (count[i].length() != var.length() + number.length() + 1) {
						errorDetected = true; break;
					}
				} else if (count[i].charAt(var.length()+1) == '-') {
					errorDetected = true; break;
				}
			}
		}
		return !errorDetected;
	}
	
	public boolean judgeDerivate() {
		if (!op.isLetter(command.charAt(5))) {
			System.out.println("Error, wrong command!");
			return false;
		}
		final String variable = op.getVarStr(command, 5);
		if (5 + variable.length() < command.length()) {
			System.out.println("Error, wrong command!");
			return false;
		}
		return true;
	}
	
	public void simplify() {
		String newString = "error";
		String fun = exp.getExpression();
		OperateString ops = new OperateString();
		final String[] count = command.split(" "); 
		final int num = count.length;
		
		String[] var = new String[num - 1];
		for (int i = 1; i < num; i++) {
			var[i - 1] = ops.getVarStr(count[i], 0);
			final int len = count[i].length();
			final String n = count[i].substring(var[i - 1].length() + 1, len);
			final int v = Integer.parseInt(n);
			value[i - 1] = v;
		}

		String x = "";
		for (int i = 0; i < fun.length(); i++) {
			if (ops.isLetter(fun.charAt(i))) {
				x = ops.getVarStr(fun, i);
				boolean havevalue = false, havesquare = false;
				for (int j = 0; j < num - 1; j++) {
					if (x.equals(var[j])) {
						newString = newString + value[j];
						havevalue = true;
						break;
					} else if ((i + x.length()) < fun.length() 
							&& fun.charAt(i + x.length()) == '^') {
						final String l = ops.getNumStr(fun, i + x.length() + 1);
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

		newString = op.mergePlus(newString);
		ans.setAnswer(newString);
	}
	
	public void derivate() {
		if (!op.isLetter(command.charAt(5))) {
			System.out.println("Error, wrong command!");
		}
		final String variable = op.getVarStr(command, 5);
		if (5 + variable.length() < command.length()) {
			System.out.println("Error, wrong command!");
		}
		String newString = derivation(exp.getExpression(), variable);
		
		ans.setAnswer(newString);
	}
	
	public void setCommand(String newCom, Expression newExp) {
		this.command = newCom;
		this.exp = newExp;
	}	
	
	public static String derivation(final String input, final String x) {
		String str = "";
		if (op.havex(input, x) == 0) {
			str = "0";
		} else {

			final String[] count = input.split("\\+");
			String temp = "";

			for (int i = 0; i < count.length; i++) {
				int cal = 0;
				temp = "";
				cal = op.havex(count[i], x);
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
			str = op.splitSquare(str);
			// System.out.println(str);
			str = op.mergePlus(str);
		}
		return str;
	}
	
	public static String derivationSub(final String input, final String x) {
		String newString = "", str = "";
		final String[] count = input.split("\\-");
		if (op.havex(input, x) == 0) {
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
				cal = op.havex(count[i], x);
				if (cal != 0) {
					temp = op.mergeMul(count[i]);
					int k = 0;
					if (op.isNumber(temp.charAt(0))) {
						numstr = op.getNumStr(temp, 0);
						k = numstr.length();
					}
					for (int j = k; j < temp.length(); j++) {
						if (op.isLetter(temp.charAt(j))) {
							sub = op.getVarStr(temp, j);
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
				newString = op.mergeSquare(newString);
				str = str + '-' + newString;
			}
			str = str.substring(1, str.length());
			str = op.splitSquare(str);
			str = op.mergePlus(str);
		}
		return str;
	}

	public Answer getAns() {
		return ans;
	}

	public void setAns(Answer ans) {
		this.ans = ans;
	}
	
}
