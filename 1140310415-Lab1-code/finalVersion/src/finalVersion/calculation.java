package finalVersion;
import java.util.*;

public class calculation {
	public static int[] value = new int[200];
	
	 //Initialize value[]
	 public static void Ini()
	 {
		 for (int i = 0; i < 200; i++)
			 value[i] = 0;
	 }
	 
	 //Read one input string end with enter
 	 public static String read()
	 {
		 Scanner in = new Scanner(System.in);
		 String input = in.nextLine();
		 return input;
	 }
 	 
 	 //Judge the input string
	 public static int Judge(String input)
	 {
		 if (input.charAt(0) == '!') 
		 {
			 if (input.length() < 6)
				 return 3;//The command is too short, so error
			 if (input.substring(1, 4).equals("d/d"))
				 return 1;//It is a derivation command
			 else if (input.substring(1, 9).equals("simplify")) 
				 return 0;//It is simplification command
			 else
			 {
				 return 3;//Error
			 }
		 }
		 else return 2;//Expression
	 }
	 
	 //Whether char a is a number, if yes return true, or false
	 public static boolean Isnumber(char a)
	 {
		 if (a >= '0' && a <= '9') return true;
		 else return false;
	 }
	 
	//Whether char a is a letter, if yes return true, or false
	 public static boolean Isletter(char a)
	 {
		 if ((a >= 'A' && a <= 'Z') || (a >= 'a' && a <= 'z')) return true;
		 else return false;
	 }
	 
	//Whether char a is a symbol, if yes return true, or false
	 public static boolean Issymbol(char a)
	 {
		 if (a=='+' || a=='*' || a=='-' || a=='^')
			 return true;
		 else return false;
	 }
	 
	 //Judge the expression is legal
	 public static boolean JudgeFun(String fun)
	 {
		 int cnt_num = 0, cnt_letter=0, cnt_symbol=0;
		 char a = '*';
		 if (Issymbol(fun.charAt(0)) || Issymbol(fun.charAt(fun.length()-1)))//If the first or last char is symbol, wrong
			 return false;
		 for (int i = 0; i < fun.length(); i++)
		 {
			 a = fun.charAt(i);
			 if (Isnumber(a)) 
			 {
				 String l = GetNumStr(fun,i);
				 if ((i+l.length() < fun.length()) &&fun.charAt(i+l.length()) == '^')//Avoid such situation like "2^y",etc
					 return false;
				 i = i + l.length()-1;
				 cnt_num = cnt_num+l.length();
				 cnt_letter=0;
				 cnt_symbol=0;
			 }
			 else if (Isletter(a))
			 {
				 String l = GetVarStr(fun, i);
				 int len = l.length();
				 if (i+len < fun.length() && fun.charAt(i+len) == '^')//Avoid such situation like "y^2^2",etc
				 {
					 if (Isnumber(fun.charAt(i+len+1)) == false)
						 return false;
					 else
					 {
						 String ll = GetNumStr(fun, i+len+1);
						 if ((i+len+1+ll.length() < fun.length()) && (fun.charAt(i+len+1+ll.length()) == '^'))
							 return false;
					 }
				 }
				 i = i + len-1;
				 cnt_letter+=len;
				 cnt_num=0;
				 cnt_symbol=0;
			 }
			 else if(cnt_symbol == 0 && Issymbol(a))//Avoid continues symbols
			 {
				 cnt_symbol++;
				 cnt_num=0;
				 cnt_letter=0;
			 }
			 else return false;
		 }
		 return true;
	 }
	 
	 //Simplify function
	 public static String Simplify(String input, String fun)
	 {
		 Ini();
		 String new_s = "";
		 String[] count = input.split(" ");
		 int num = count.length;
		 for (int i = 0; i < num; i++)
		 {
			 if (count[i].equals(""))
			 {return "error";}			 
			 else if (count[i].charAt(0) == ' ' || count[i].charAt(0) == '=')
				 return "error";
		 }
		 String[] var = new String[num-1];
		 for (int i = 1; i < num; i++)
		 {
			 var[i-1] = GetVarStr(count[i],0);			 
			 int len = count[i].length();
			 String n = count[i].substring(var[i-1].length()+1, len);
			 int v = Integer.parseInt(n);
			 value[i-1] = v;
		 }
		 
		 String x = "";
		 for (int i = 0; i < fun.length(); i++)
		 {
			 if (Isletter(fun.charAt(i)))
			 {
				 x = GetVarStr(fun,i);
				 boolean havevalue=false, havesquare=false;
				 for (int j = 0; j < num-1; j++)
				 {
					 if (x.equals(var[j])){
						 new_s = new_s + value[j];
						 havevalue=true;
						 break;
					 }
					 else if ((i+x.length()) < fun.length() && fun.charAt(i+x.length()) == '^')
					 {
						 String l = GetNumStr(fun, i+x.length()+1);
						 i = i+1+l.length();
						 new_s = new_s+x+'^'+l;
						 havesquare = true;
					 }
				 }
				 if (havevalue==false && havesquare==false) new_s = new_s + x;
				 i = i+x.length()-1;
			 }else
				 new_s = new_s + fun.charAt(i);
		 }
		 //System.out.println(new_s);
		 return new_s;
	 }
	 
	 //A function to simplify a multiplication expression
	 public static String MergeMul(String input)
	 {
		 String new_s = "";
		 String sub = "";
		 int Mul=1;
		 for (int i = 0; i < input.length(); i++)
		 {
			 if (Isnumber(input.charAt(i)))
			 {
				 sub = GetNumStr(input,i);
				 int num = Integer.parseInt(sub);
				 if (num==0)
					 return "0";
				 Mul *= num;				 
			 }
			 else if (Isletter(input.charAt(i)))
			 {
				 sub = GetVarStr(input,i);
				 new_s = new_s + '*' + sub;
			 }else
				 sub = "*";
			 i = i+sub.length()-1;
		 }
		 if (Mul!=1)
			 new_s = Mul + new_s;
		 else if (new_s.length()<=1)
			 new_s = "1";
		 else
			 new_s = new_s.substring(1,new_s.length());
		 return new_s;
	 }
	 
	 //A function to judge whether a variable is in the string input
	 public static int havevar(String input)
	 {
		 for (int i = 0; i < input.length(); i++)
		 {
			 if (Isletter(input.charAt(i))){
				 return 1;
			 }
		 }
		 return 0;
	 }
	 
	 //A function to simplify a subtraction expression
	 public static String MergeSub(String input)
	 {
		 int sum = 0;
		 String[] count = input.split("\\-");
		 String temp = "", new_s = "";
		 for (int i = 0; i < count.length; i++)
		 {
			 if (count[i].length()==0) continue;
			 temp = MergeMul(count[i]);
			 temp = MergeSquare(temp);
			 if (havevar(temp)==0){
				 if (i!=0){
					 sum -= Integer.parseInt(temp);
				 }else
					 sum += Integer.parseInt(temp);
			 }else{
				 if (i!=0)
					 new_s = new_s + '-' + temp;
				 else
					 new_s = temp;
			 }	
/*			 System.out.print(temp);
			 System.out.print(' ');
			 System.out.println(new_s);*/
		 }
		 if (new_s.length()==0){
			 new_s = sum+"";
			 return new_s;
		 }
		 if (Issymbol(new_s.charAt(0))==false && sum != 0)
			 new_s = '+' + new_s;
		 if (sum != 0)
			 new_s = sum+new_s;
		 else if (new_s.length() == 1)
			 new_s = "0";
		 //System.out.print("MergeSub: ");System.out.println(new_s);
		 return new_s;
	 }
	 
	 //A function to simplify an addition expression 
	 public static String MergePlus(String input)
	 {
		 String[] count = input.split("\\+");
		 String temp = "", new_s="";
		 int sum=0;
		 String numstr = "";
		 for (int i = 0; i < count.length; i++)
		 {
			 temp = MergeSub(count[i]);
			 //System.out.print("temp:бубу");System.out.println(temp);
			 if (havevar(temp)==0){
				 sum += Integer.parseInt(temp);
			 }else if (temp.charAt(0)=='-'){
				 if (Isnumber(temp.charAt(1))){
					 numstr = GetNumStr(temp,1);
					 int j = numstr.length()+1;
					 //System.out.println(temp.charAt(j));
					 if (temp.charAt(j)=='*'){
						 new_s = new_s + temp;
					 }else{
						 sum -= Integer.parseInt(numstr);
						 new_s = new_s + temp.substring(j);
					 }
				 }else{
					 new_s = new_s + temp;
				 }
			 }else if (Isnumber(temp.charAt(0))){
				 numstr = GetNumStr(temp,0);
				 int j = numstr.length();
				 if (temp.charAt(j)=='-'){
					 sum += Integer.parseInt(numstr);
					 new_s = new_s + temp.substring(j);
				 }else
					 new_s = new_s + '+' + temp;
			 }else
				 new_s = new_s + '+' + temp;
			 //System.out.print("new_s: ");System.out.println(new_s);
			 //System.out.print("sum: ");System.out.println(sum);
		 }
		 if (new_s.length()==0){
			 new_s = sum+"";
			 return new_s;
		 }
		 if (sum != 0)
			 new_s = sum+new_s;
		 else if (new_s.length() <= 1)
			 new_s = "0";
		 else
			 if (new_s.charAt(0)!='-')
				 new_s = new_s.substring(1,new_s.length());
		 //System.out.print("MergePlus: ");System.out.println(new_s);
		 return new_s;
	 }
	 
	 //A function to calculate the number of variable x which shows in the String input
	 public static int havex(String input, String x)
	 {
		 int cnt = 0;
		 String var = "";
		 for (int i = 0; i < input.length(); i++)
		 {
			 if (Isletter(input.charAt(i))){
				 var = GetVarStr(input,i);
				 if (x.equals(var)){
					 cnt++;
				 }
				 i = i+var.length()-1;
			 }
		 }
		 return cnt;
	 }
	 
	 //Get a substring of number at the start position i in the string input
	 public static String GetNumStr(String input, int i)
	 {
		 int j = i + 1;
		 for (; j < input.length() && Isnumber(input.charAt(j)); j++);
		 return input.substring(i,j);
	 }
	 
	 //Diff a expression which includes subtraction
	 public static String DerivationSub(String input, String x)
	 {
		 if (havex(input,x)==0) return "0";
		 String new_s = "", str="";
		 String[] count = input.split("\\-");
		 int sum = 0;
		 String temp="", numstr="", sub="";
		 for (int i = 0; i < count.length; i++)
		 {
			 int mul=1;
			 int cal=0;
			 temp = "";
			 numstr = "1";
			 new_s = "";
			 cal = havex(count[i], x);
			 if (cal != 0)
			 {
				 temp = MergeMul(count[i]);
				 int k = 0;
				 if (Isnumber(temp.charAt(0))){
					 numstr = GetNumStr(temp, 0);
					 k = numstr.length();
				 }
				 for (int j = k; j < temp.length();j++)
				 {
					 if (Isletter(temp.charAt(j))){
						 sub = GetVarStr(temp,j);
						 if (sub.equals(x) != true) 
						 	 new_s = new_s + '*' + sub;
						 j = j+sub.length()-1;
					 }
				 }
			 }
			 mul *= Integer.parseInt(numstr)*cal;
			 new_s = mul + new_s;
			 for (int j = 0; j < cal-1; j++)
			 {
				 new_s = new_s + '*' + x;
			 }			 
			 new_s = MergeSquare(new_s);
			 str = str + '-' + new_s;
		 }		 
		 str = str.substring(1, str.length());
		 str = SplitSquare(str);
		 //System.out.print("DerivationSub: ");System.out.println(str);
		 str = MergePlus(str);
		 //System.out.print("MergePlus: ");System.out.println(str);
		 //System.out.println();
		 return str;		 
	 }
	 
	 //Derivation
	 public static String Derivation(String input, String x)
	 {
		 if (havex(input,x)==0) return "0";
		 String new_s = "", str="";
		 String[] count = input.split("\\+");
		 int sum = 0;
		 String temp="", numstr="", sub="";
		 
		 for (int i = 0; i < count.length; i++)
		 {
			 int mul=1;
			 int cal=0;
			 temp = "";
			 numstr = "1";
			 new_s = "";
			 cal = havex(count[i], x);
			 if (cal != 0)
			 {
				 temp = DerivationSub(count[i], x);
				 str = str + '+' + temp;
			//	 System.out.print("temp: ");System.out.println(temp);
			 }
		 }		 
		 if (str.charAt(0)=='+')
			 str = str.substring(1);
		// System.out.println(str);
		 str = SplitSquare(str);
		// System.out.println(str);
		 str = MergePlus(str);
		 return str;
	 }
	 
	//Get a substring of variable at the start position i in the string input
	 public static String GetVarStr(String input, int i)
	 {
		 int j = i + 1;
		 for (;j < input.length() && Isletter(input.charAt(j)); j++);
		 return input.substring(i,j);
	 }
	 
	 //A funcition to transform '^' to '*' in the expression
	 public static String SplitSquare(String input)
	 {
		 String new_s = "";
		 for (int i = 0; i < input.length(); i++)
		 {
			 if (Isletter(input.charAt(i)))
			 {
				 String var = GetVarStr(input,i);
				 new_s = new_s+var;
				 int len = var.length();
				 
				 if ((i+len)<input.length() && input.charAt(i+len)=='^')
				 {
					 String n = GetNumStr(input,i+len+1);
					 int num = Integer.parseInt(n);
					 for (int j = 0; j < num-1; j++)
					 {
						 new_s = new_s+'*'+var;
					 }
					 i = i+len+n.length();
				 }
				 else
					 i = i+len-1;
			 }
			 else
				 new_s = new_s + input.charAt(i);
		 }
		 
		 return new_s;
	 }
	 
	 //A function to merge continues '*' to '^'
	 public static String MergeSquare(String input)
	 {
		 String[] var = new String[100];
		 String sub = "";
		 int[] cntvar = new int[100];
		 int cnt = 0;
		 boolean havenum=false;
		 for (int j = 0; j < 100; j++)
		 {
			 cntvar[j] = 0;
		 }
		 
		 for (int j = 0; j < input.length(); j++)
		 {
			 boolean havevar=false;
			 if (Isnumber(input.charAt(j)))
			 {
				 String num = GetNumStr(input,j);
				 sub = num+sub;
				 j = j + num.length()-1;
				 havenum=true;
			 }
			 else if (Isletter(input.charAt(j)))
			 {
				 String v = GetVarStr(input,j);
				 int k = 0;
				 for (k = 0; k < cnt; k++)
				 {
					 if (var[k].equals(v)) havevar=true;break;
				 }
				 if (havevar==true)
					 cntvar[k]++;
				 else{
					 var[cnt] = v;
					 cntvar[cnt]++;
					 cnt++;
				 }
				 j = j + v.length()-1;
			 }
		 }		 
		 for (int j = 0; j < cnt; j++)
		 {
			 if (cntvar[j] > 1)
				 sub = sub + '*' + var[j] + '^' + cntvar[j];
			 else
				 sub = sub + '*' + var[j];
		 }
		 if (havenum == false)
			 sub = sub.substring(1,sub.length());
		 //System.out.println(sub);
		 return sub;
	 }
	 
	 //A function to delete the space key and tab key in the expression
	 public static String DeleteTab(String input)
	 {
		 String new_s = "";
		 for (int i = 0; i < input.length(); i++)
		 {
			 if (input.charAt(i) == ' ' || input.charAt(i) == '\t')
				 continue;
			 new_s = new_s + input.charAt(i);
		 }
		 return new_s;
	 }
	 //A funcition to show '*', like "3x" -> "3*x"
	 public static String ReMul(String input)
	 {
		 String new_s = "";
		 for (int i = 0; i < input.length(); i++)
		 {
			 if (Isnumber(input.charAt(i)))
			 {
				 String num = GetNumStr(input, i);
				 new_s = new_s + num;
				 int len = num.length();
				 if ((i+len) < input.length() && Isletter(input.charAt(i+len)))
					 new_s = new_s+'*';
				 i = i + len - 1;
			 }
			 else
				 new_s = new_s + input.charAt(i);
		 }
		 return new_s;
	 }
	 
	 //The main function
	 public static void main(String args[])
	 {
		Ini();
		String fun = "", new_s = "";
		while (true)
		{
			String s = read();
			if (s.equals(""))//If it is a blank string
			{
				System.out.println("Error, wrong input!");
				continue;
			}
			
			//System.out.println(s);
			int x = Judge(s);
			if (x == 2)//The input is a expression
			{
				s = DeleteTab(s);
				s = ReMul(s);
				if (JudgeFun(s) == false)
				{
					System.out.println("Error, wrong expression!");
					continue;
				}
				fun = SplitSquare(s);
				System.out.println(fun);
			}
			else if (x == 0)//The input is a simplification command
			{
				new_s = Simplify(s, fun);
				if (new_s.equals("error"))
				{
					System.out.println("Error, wrong command!");
					continue;
				}
				//System.out.println(new_s);
				new_s = MergePlus(new_s);
				System.out.println(new_s);
			}
			else if (x == 1)//The input is a diff command
			{
				if (Isletter(s.charAt(5)) == false)
				{
					System.out.println("Error, wrong command!");
					continue;
				}
				String variable = GetVarStr(s, 5);
				if (5+variable.length()<s.length())
				{
					System.out.println("Error, wrong command!");
					continue;
				}
				new_s = Derivation(fun, variable);
				//new_s = MergeSquare(new_s);
				System.out.println(new_s);
			}
			else if (x == 3)// Error input
			{
				System.out.println("Error, wrong input!");
			}
		}
	 }
}
