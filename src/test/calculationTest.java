package test;

import static org.junit.Assert.*;

import org.junit.Test;

import Control.JudgeExpression;
import Entity.Command;
import Entity.Expression;

public class calculationTest {
	@Test 
	public void testSimplify() {
		Expression exp = new Expression("2*x*x+y*y*y+3*z");
		Command testCommand = new Command("!simplify", exp);
		testCommand.simplify();
		assertEquals("2*x^2+y^3+3*z",testCommand.getAns().getAnswer());
		
		testCommand.setCommand("!simplify x=1");
		testCommand.simplify();
		assertEquals("2+y^3+3*z",testCommand.getAns().getAnswer());
		
		testCommand.setCommand("!simplify x=1 y=2 z=3");
		testCommand.simplify();
		assertEquals("19",testCommand.getAns().getAnswer());
		
		testCommand.setCommand("!simplify x");
		assertEquals(false,testCommand.judgeSimplify());
		
		testCommand.setCommand("!simplify x = 1");
		assertEquals(false,testCommand.judgeSimplify());
		
		testCommand.setCommand("!simplify x=1y=2");
		assertEquals(false,testCommand.judgeSimplify());
		
		testCommand.setCommand("!simplify x=1.02");
		assertEquals(false,testCommand.judgeSimplify());
		
		testCommand.setCommand("!simplify x=-1");
		assertEquals(false,testCommand.judgeSimplify());	
	}
	
	@Test 
	public void testMergePlus() {
		Expression exp = new Expression("2+x*3*y*x*x-4*5+3*6*y*y");
		exp.mergeExp();
		assertEquals("-18+3*x^3*y+18*y^2",exp.getExp());
	}
	
	@Test 
	public void testDerivation() {
		Expression exp = new Expression("2*x*x+new*new*new+3*xx");
		Command testCommand = new Command("!d/d x", exp);
		testCommand.derivate();
		assertEquals("4*x",testCommand.getAns().getAnswer());
		
		testCommand.setCommand("!d/d new");
		testCommand.derivate();
		assertEquals("3*new^2",testCommand.getAns().getAnswer());
		
		testCommand.setCommand("!d/d y");
		testCommand.derivate();
		assertEquals("0",testCommand.getAns().getAnswer());
	}

	@Test
	public void testJudgeExpression() {		
		assertEquals(false,JudgeExpression.judgeExpression("35^3")); //1
		assertEquals(true,JudgeExpression.judgeExpression("35")); //2
		assertEquals(false,JudgeExpression.judgeExpression("x^3^2")); //3
		assertEquals(true,JudgeExpression.judgeExpression("x")); //4
		assertEquals(false,JudgeExpression.judgeExpression("x^yyy")); //5
		assertEquals(true,JudgeExpression.judgeExpression("x+yy^2*7+x*x")); //6
		assertEquals(true,JudgeExpression.judgeExpression("134*x")); //7
		assertEquals(false,JudgeExpression.judgeExpression("x(y")); //8
		assertEquals(false,JudgeExpression.judgeExpression("")); //9
		assertEquals(false,JudgeExpression.judgeExpression("*x+y")); //10
		
		assertEquals(false,JudgeExpression.judgeExpression("x+yy^2^3*7+x*x"));
		assertEquals(false,JudgeExpression.judgeExpression("x+yy^x^2*7+x*x"));		
		assertEquals(true,JudgeExpression.judgeExpression("x+yy^2*7+x*x"));
		assertEquals(false,JudgeExpression.judgeExpression("x+yy^2*7+x*x#"));
		assertEquals(false,JudgeExpression.judgeExpression("x+yy^2*7+)x*x"));	
	}

}
