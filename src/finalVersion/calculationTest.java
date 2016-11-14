package finalVersion;

import static org.junit.Assert.*;

import org.junit.Test;

public class calculationTest {

	@Test
	public void testJudgeFun() {		
		assertEquals(true,calculation.judgeFun("x+yy^2*7+x*x"));
		assertEquals(false,calculation.judgeFun("x+yy^x^2*7+x*x"));
		assertEquals(false,calculation.judgeFun("x+yy^2^3*7+x*x"));
		assertEquals(false,calculation.judgeFun(""));
		assertEquals(false,calculation.judgeFun("x+yy^2*7+x*x#"));
		assertEquals(false,calculation.judgeFun("x+yy^2*7+)x*x"));
		assertEquals(true,calculation.judgeFun("x+yyy"));
		assertEquals(true,calculation.judgeFun("x+yy^2*7"));
	}

}
