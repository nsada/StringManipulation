package finalVersion;

import static org.junit.Assert.*;
import org.junit.Test;

public class calculationTest {
	@Test 
	public void testSimplify() {
		assertEquals("2*x*x+y*y*y+3*z",calculation.simplify("!simplify", "2*x*x+y*y*y+3*z"));
		assertEquals("2*1*1+y*y*y+3*z",calculation.simplify("!simplify x=1", "2*x*x+y*y*y+3*z"));
		assertEquals("2*1*1+2*2*2+3*3",calculation.simplify("!simplify x=1 y=2 z=3", "2*x*x+y*y*y+3*z"));
		assertEquals("error",calculation.simplify("!simplify x", "2*x*x+y*y*y+3*z"));
		assertEquals("error",calculation.simplify("!simplify x = 1", "2*x*x+y*y*y+3*z"));
		assertEquals("error",calculation.simplify("!simplify x=1y=2", "2*x*x+y*y*y+3*z"));
		assertEquals("error",calculation.simplify("!simplify x=1.02", "2*x*x+y*y*y+3*z"));
		assertEquals("error",calculation.simplify("!simplify x=-1", "2*x*x+y*y*y+3*z"));	
	}
	
	@Test 
	public void testMergePlus() {
		assertEquals("-18+3*x^3*y+18*y^2",calculation.mergePlus("2+x*3*y*x*x-4*5+3*6*y*y"));
	}
	
	@Test 
	public void testDerivation() {
		assertEquals("4*x",calculation.derivation("2*x*x+new*new*new+3*xx","x"));
		assertEquals("3*new^2",calculation.derivation("2*x*x+new*new*new+3*xx","new"));
		assertEquals("0",calculation.derivation("2*x*x+new*new*new+3*xx","y"));
	
	}

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
