package test;

import static org.junit.Assert.*;

import org.junit.Test;

import Lab7.JudgeExpression;

public class calculationTest {

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
